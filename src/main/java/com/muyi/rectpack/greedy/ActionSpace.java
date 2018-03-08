package com.muyi.rectpack.greedy;

import com.google.gson.stream.JsonToken;
import com.muyi.rectpack.domain.*;
import com.muyi.rectpack.enums.PointRectRelEnum;
import com.muyi.util.JsonUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author: muyi-corp
 * @Date: Created in 21:30 2018/1/14
 * @Description: 动作空间 是一个虚拟矩形 是一个可放入的极大矩形
 */
@Data
@Slf4j
public class ActionSpace extends RectInfo implements Cloneable{

    public ActionSpace(Point leftUp, Point rightDown){
        super(leftUp,rightDown);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString(){
        return super.toString();
    }

    /**
     * 尝试将一个小块横放或竖放
     */
    public List<TakeCorner> tryPutBy2Dir(Rect rect,int rectID){
        List<TakeCorner> takeCornerList = new ArrayList<>();
        //如果横放 -- 放到四个顶点进行尝试
        takeCornerList.addAll(tryPutIn(rect,rectID));

        //如果竖放 -- 放到四个顶点进行尝试
        Rect rotateRect = new Rect();
        rotateRect.setLength(rect.getWidth());
        rotateRect.setWidth(rect.getLength());
        takeCornerList.addAll(tryPutIn(rotateRect,rectID));

        return takeCornerList;
    }

    //尝试将小块放入四个顶点
    private List<TakeCorner> tryPutIn(Rect rect,int rectID){

        //如果成功放入后 会生成四个占角动作
        List<TakeCorner> takeCornerList = new ArrayList<>();

        //四个顶点
        Apex[] apexes = this.getApexes();

        //第一次尝试后就可以知道能否放入和贴边数 不需要一直计算
        int tieBianNum = -1;

        for (int i = 0; i < 4; i++) {
            Apex apex = apexes[i];

            Point start = apex.getPoint();//放入开始点
            Point end = new Point();      //放入结束点
            end.x = apex.isLeft() ? start.x + rect.getLength() : start.x - rect.getLength();
            end.y = apex.isUp() ? start.y + rect.getWidth() : start.y - rect.getWidth();

            //第一次计算能否放入 -- 看end点和动作空间的关系
            //后续不需要在计算贴边数了
            if (tieBianNum == -1){
                int r = RectInfo.judgePoint2Rect(end,this);
                //放不进去 那么也不需要继续尝试了
                if (r == PointRectRelEnum.OUT.getCode()){
                    return new ArrayList<>();
                }else {
                    //放进去了 -- 此时小块和动作空间的贴边数是一样的
                    tieBianNum = r + 2;
                }
            }
            //放入成功后 产生一个占角动作
            TakeCorner takeCorner = new TakeCorner();

            //计算贴边数
            HoleDegree holeDegree = new HoleDegree();
            holeDegree.setTieBianNum(tieBianNum);

            takeCorner.setRectID(rectID);
            takeCorner.setApex(apex);
            takeCorner.setEnd(end);
            takeCorner.setHoleDegree(holeDegree);
            takeCornerList.add(takeCorner);
        }
        return takeCornerList;
    }

    /**
     * 面对一块新放入的矩形 动作空间应该作何调整
     */
    public List<ActionSpace> handlePutIn(RectInfo rectInfo){

        List<ActionSpace> newActionSpaceList = new LinkedList<>();
        RectInfo joinRect = RectInfo.judgeRect2Rect(rectInfo,this);

        if (joinRect == null || !joinRect.isRectangle()){
            //相离或相接 什么也不发生
            return null;
        }else {
            //相交 那么要删除这个动作空间 并且新增动作空间
            ActionSpace newAS1;
            ActionSpace newAS2;
            ActionSpace newAS3;

            //首先判断冲突部分和动作空间贴了哪条边
            Section asXSection = new Section(this.getLeftUp().x,this.getRightDown().x);
            Section rXSection = new Section(rectInfo.getLeftUp().x,rectInfo.getRightDown().x);

            Section asYSection = new Section(this.getLeftUp().y,this.getRightDown().y);
            Section rYSection = new Section(rectInfo.getLeftUp().y,rectInfo.getRightDown().y);

            //找到不共端点的joinSection 如果没有 则任取一个joinSection
            Section joinXSection = RectInfo.judge2Sections(asXSection,rXSection);
            Section joinYSection = RectInfo.judge2Sections(asYSection,rYSection);

            if (joinYSection == null || joinXSection == null){
                log.info("Something wrong");
            }

            //log.info("【JoinRect】:{}",joinRect.toString());

            if (joinXSection.getLeft() != asXSection.getLeft() && joinXSection.getRight() != asXSection.getRight()){
                // X方向不共端点
                newAS1 = new ActionSpace(this.getLeftUp(),new Point(rectInfo.getLeftUp().x,this.getRightDown().y));
                newAS2 = new ActionSpace(new Point(rectInfo.getRightDown().x,this.getLeftUp().y),this.getRightDown());
                //需要Y方向的不重合段
                Section unJoinYSection;

                if (joinYSection.getLeft() == asYSection.getLeft()){
                    unJoinYSection = new Section(joinYSection.getRight(),asYSection.getRight());//可能是一个点
                }else {
                    unJoinYSection = new Section(asYSection.getLeft(),joinYSection.getLeft());
                }
                newAS3 = new ActionSpace(new Point(this.getLeftUp().x,unJoinYSection.getLeft()),
                                                     new Point(this.getRightDown().x,unJoinYSection.getRight()));

            }else {
                //if (joinYSection.getLeft() != asYSection.getLeft() && joinYSection.getRight() != asYSection.getRight()){
                // Y方向不共端点
                newAS1 = new ActionSpace(this.getLeftUp(),new Point(this.getRightDown().x,rectInfo.getLeftUp().y));
                newAS2 = new ActionSpace(new Point(this.getLeftUp().x,rectInfo.getRightDown().y),this.getRightDown());
                //需要X方向的不重合段
                Section unJoinXSection;
                if (joinXSection.getLeft() == asXSection.getLeft()){
                    unJoinXSection = new Section(joinXSection.getRight(),asXSection.getRight());//可能是一个点
                }else {
                    unJoinXSection = new Section(asXSection.getLeft(),joinXSection.getLeft());
                }
                newAS3 = new ActionSpace(new Point(unJoinXSection.getLeft(),this.getLeftUp().y),
                                                     new Point(unJoinXSection.getRight(),this.getRightDown().y));
            }
            if (newAS1.isRectangle()) newActionSpaceList.add(newAS1);
            if (newAS2.isRectangle()) newActionSpaceList.add(newAS2);
            if (newAS3.isRectangle()) newActionSpaceList.add(newAS3);
        }
        return newActionSpaceList;
    }




}
