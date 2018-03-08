package com.muyi.rectpack.greedy;

import com.google.gson.internal.bind.JsonTreeWriter;
import com.muyi.rectpack.domain.Rect;
import com.muyi.util.JsonUtil;
import com.muyi.util.RandomUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.awt.event.ActionListener;
import java.util.ArrayList;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @Author: muyi-corp
 * @Date: Created in 17:07 2018/1/12
 * @Description: 矩形packing问题的贪心算法实现
 */
@Slf4j
public class GreedyImpl {

    private Layout cLayout;

    public void start() throws Exception{

        int width = 600;
        int length = 800;
        List<Rect> waitPutRectList = new ArrayList<>();

        //生成512以内的 小块 n 个
        //int n = RandomUtil.randomInt(16);
        for (int i = 0; i < 10; i ++){
            waitPutRectList.add(
                    new Rect(RandomUtil.randomInt(512),RandomUtil.randomInt(512))
            );
        }

        //初始化一个空的格局和等待放入的小块列表
        this.cLayout =  Layout.initLayout(width,length,waitPutRectList);

        //每次放入一个小块
        while (true){
            List<TakeCorner> betterTakeCornerList = this.loopActionSpace(this.cLayout);
            if (betterTakeCornerList == null) break;
            //最好的占角
            TakeCorner bestTakeCorner = betterTakeCornerList.get(0);
            //log.warn("最好的占角动作：{}", JsonUtil.toJson(bestTakeCorner));

            //现在执行这个占角动作
            this.cLayout.doTakeCorner(bestTakeCorner);
        }
        log.info("【等待放入的小块列表】:{}",JsonUtil.toJson(waitPutRectList));
        this.cLayout.getShow().showAll();
    }

    /**
     * 在当前格局下,对每个动作空间的每个角区、框外剩余的每个块、块的不同放置方向,
     * 将块放入到角区,若构成一个占角动作,则计算其穴度.
     * 选出更好的占角动作
     */
    public List<TakeCorner> loopActionSpace(Layout layout) throws Exception{

        List<ActionSpace> actionSpaces = layout.getActionSpaceList();
        //log.info("【动作空间】All:{}",JsonUtil.toPrettyJson(actionSpaces));
//        for (ActionSpace ac : actionSpaces) {
//            log.info("【动作空间】All: {}",ac.toString());
//        }

        List<Rect> unPutRectList = layout.getUnPutRectList();

        //占角动作列表
        List<TakeCorner> takeCornerList = new ArrayList<>();

        //log.info("动作空间列表：");
        for (int asID = 0; asID < actionSpaces.size(); asID ++) {
            //log.info("动作空间{}：{}", asID, JsonUtil.toJson(actionSpaces.get(asID)));
            //this.cLayout.getShow().showActionSpace(actionSpaces.get(asID));
            for (int rectID = 0; rectID < unPutRectList.size(); rectID ++){
                ActionSpace cActionSpace = actionSpaces.get(asID);
                Rect cRect = unPutRectList.get(rectID);
                takeCornerList.addAll(cActionSpace.tryPutBy2Dir(cRect,rectID));
            }
        }

        //log.info("【占角动作列表】:{}",JsonUtil.toPrettyJson(takeCornerList));

        //占角动作列表是空时 代表剩下的小块都放不进去
        if (takeCornerList.size() == 0) return null;

        //遍历占角动作列表 得到贴边数最大的 一些动作
        int maxTieBianNum = 2; //开始默认最大为2
        for (TakeCorner takeCorner : takeCornerList) {
            if (takeCorner.getHoleDegree().getTieBianNum() > maxTieBianNum){
                maxTieBianNum = takeCorner.getHoleDegree().getTieBianNum();
            }
        }
        List<TakeCorner> betterTakeCornerList = new ArrayList<>();
        for (TakeCorner takeCorner : takeCornerList) {
            if (takeCorner.getHoleDegree().getTieBianNum() == maxTieBianNum){
                betterTakeCornerList.add(takeCorner);
            }
        }
        //对于更好的占角动作 进行字典排序
        //平整度-他贴边数-面积-长边长-左上角更接近(0,0)-躺优先

        //计算平整度和他贴边数
        for (TakeCorner takeCorner : betterTakeCornerList){
            layout.calcPingZhengDegree(takeCorner);
            layout.calcTaTieBianNum(takeCorner);
        }

        //now排序
        Collections.sort(betterTakeCornerList);
        Collections.reverse(betterTakeCornerList);//降序

//        //输出5个最好的占角动作
//        int i = betterTakeCornerList.size() > 5 ? 5 : betterTakeCornerList.size();
//        for(int j = 0; j < i; j ++){
//            log.info("排名第 {} 的占角动作：{}", j, JsonUtil.toJson(betterTakeCornerList.get(j)));
//        }
//        log.warn("---------------------");

//        //最好的占角
//        TakeCorner bestTakeCorner = betterTakeCornerList.get(0);
//        log.warn("最好的占角动作：{}", JsonUtil.toJson(bestTakeCorner));


//
//        //现在执行这个占角动作
//        layout.doTakeCorner(bestTakeCorner);
//
//
        return betterTakeCornerList;
    }



}
