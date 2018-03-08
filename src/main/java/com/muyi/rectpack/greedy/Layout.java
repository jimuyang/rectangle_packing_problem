package com.muyi.rectpack.greedy;
import com.muyi.draw.greedy.GreedyShow;
import com.muyi.rectpack.domain.Point;
import com.muyi.rectpack.domain.Rect;
import com.muyi.rectpack.domain.RectInfo;

import com.muyi.util.*;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author: muyi-corp
 * @Date: Created in 16:44 2018/1/12
 * @Description: 布局 存储了现在的格局
 */
@Data
@Slf4j
public class Layout implements Serializable,Cloneable {

    private static final long serialVersionUID = 1038525435831064250L;

    private GreedyShow show;

    //格局的宽高 面积
    private int width;
    private int length;
    private int area;

    //已经放置了的矩形
    private int putInNum;
    private List<RectInfo> putInRectList;

    //还未放置的矩形
    private List<Rect> unPutRectList;

    //动作空间链表
    private List<ActionSpace> actionSpaceList;

    public Layout(int width,int length){
        this.length = length;
        this.width = width;
        this.area = length * width;
        this.show = new GreedyShow(new Rect(length,width));
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Layout layout = (Layout)super.clone();

        List<ActionSpace> asCopy = new LinkedList<>();
        List<RectInfo> putInCopy = new ArrayList<>();
        List<Rect> unPutCopy = new ArrayList<>();
        try {
            for (ActionSpace as : this.actionSpaceList) {
                asCopy.add((ActionSpace)as.clone());
            }
            for (RectInfo rectInfo : this.putInRectList) {
                putInCopy.add((RectInfo) rectInfo.clone());
            }
            for (Rect rect : this.unPutRectList) {
                unPutCopy.add((Rect)rect.clone());
            }
        }catch (CloneNotSupportedException cnse){
            cnse.printStackTrace();
        }
        layout.setPutInRectList(putInCopy);
        layout.setUnPutRectList(unPutCopy);
        layout.setActionSpaceList(asCopy);
        return layout;
    }

    public static Layout initLayout(int width, int length, List<Rect> unPutRectList){
        Layout layout;
        if (length > width){
            layout = new Layout(width,length);
        }else {
            layout = new Layout(length,width);
        }

        layout.setPutInNum(0);
        layout.setPutInRectList(new ArrayList<>());

        //初始化等待放入的小矩形列表
        layout.setUnPutRectList(unPutRectList);

        //初始化动作空间链表
        //第一个动作空间就是容器本身
        layout.setActionSpaceList(new LinkedList<>());
        Point leftUp = new Point(0,0);
        Point rightDown = new Point(layout.getLength(),layout.getWidth());
        ActionSpace actionSpace = new ActionSpace(leftUp,rightDown);
        layout.getActionSpaceList().add(actionSpace);

        return layout;
    }
    /**
     * 计算格局已放入小块的面积
     */
    public int calcArea(){
        int area = 0;
        for (RectInfo rectInfo : this.putInRectList) {
            area += rectInfo.getRect().getArea();
        }
        return area;
    }

    /**
     * @return layout 当前格局的一个深度copy
     */
    public Layout getCopy() throws Exception{
        return (Layout) ObjectCopy.deepCopy(this);
    }

    /**
     * 获取一份actionSpace的copy
     */
    public List<ActionSpace> copyActionSpaceList(){
        List<ActionSpace> copy = new LinkedList<>();
        try {
            for (ActionSpace as : this.actionSpaceList) {
                copy.add((ActionSpace)as.clone());
            }
        }catch (CloneNotSupportedException cnse){
            cnse.printStackTrace();
        }
        return copy;
    }

    /**
     * 当前格局下进行一个占角动作 计算平整度
     */
    public int calcPingZhengDegree(TakeCorner takeCorner){
        //这里需要修改 actionSpaceList
        // 1. 每一个占角动作都生成一份 actionSpaceList的deepCopy，操作这个链表
        // TODO: 2018/1/15 有没有其他的解决方案
        List<ActionSpace> copyActionSpaceList = this.copyActionSpaceList();
        this.adjustActionSpace(copyActionSpaceList,takeCorner);
        int pingzhengDegree = copyActionSpaceList.size();
        takeCorner.getHoleDegree().setPingZhengDegree(pingzhengDegree);
        return pingzhengDegree;
    }

    /**
     * 当前格局下进行一个占角动作 计算他贴边数
     */
    public int calcTaTieBianNum(TakeCorner takeCorner){
        return this.calcTaTieBianNum(takeCorner,this.getPutInRectList());
    }

    /**
     * 当前格局下进行一个占角动作 计算他贴边数
     */
    public int calcTaTieBianNum(TakeCorner takeCorner,List<RectInfo> putInRectList){
        RectInfo rectInfo = takeCorner.getRectInfo();
        //数一数有多少个放入的矩形与之相接
        int taTieBianNum = 0;
        for (RectInfo putIn : putInRectList) {
            RectInfo joinRect = RectInfo.judgeRect2Rect(putIn,rectInfo);
            if (joinRect != null && !joinRect.isRectangle()){
                //相接
                taTieBianNum ++;
            }
        }
        //有几条边与矩形框相接
        if (rectInfo.getLeftUp().x == 0) taTieBianNum ++;
        if (rectInfo.getLeftUp().y == 0) taTieBianNum ++;
        if (rectInfo.getRightDown().x == this.length) taTieBianNum ++;
        if (rectInfo.getRightDown().y == this.width) taTieBianNum ++;

        takeCorner.getHoleDegree().setTaTieBianNum(taTieBianNum);
        return taTieBianNum;
    }

    /**
     * 在当前layout下 进行一次占角动作
     */
    public void doTakeCorner(TakeCorner takeCorner) throws Exception{
        //修改未放入小块列表
        this.unPutRectList.remove(takeCorner.getRectID());
        //修改已放入小块列表
        this.putInRectList.add(takeCorner.getRectInfo());
        //修改已放入小块数量
        this.putInNum ++;
        //调整动作空间链表
        this.adjustActionSpace(this.actionSpaceList,takeCorner);

        //画图展示
        //this.show.putOne(takeCorner.getRectInfo());
    }

    /**
     * 在动作空间链表下 进行一次占角动作 调整动作空间链表
     */
    public void adjustActionSpace(List<ActionSpace> actionSpaces,TakeCorner takeCorner){

        List<ActionSpace> newActionSpaces = new LinkedList<>();

        Iterator<ActionSpace> iterator = actionSpaces.iterator();
        while (iterator.hasNext()){
            ActionSpace actionSpace = iterator.next();
            //这个动作空间在放入小块后是否受到影响
            RectInfo rectInfo = takeCorner.getRectInfo();
            List<ActionSpace> newASList = actionSpace.handlePutIn(rectInfo);

//            log.warn("【动作空间】{}  放入矩形:{}",actionSpace.toString(),rectInfo.toString());
//            log.info("          新产生的动作空间: {}",JsonUtil.toJson(newASList));

            if (newASList == null){
                //不需要调整
            }else {
                //需要删除原来的动作空间 并加入新增的动作空间
                iterator.remove();
                //actionSpaces.addAll(newASList);
                newActionSpaces.addAll(newASList);
            }
        }

        //将新的动作空间加入原来的列表中，如果出现包裹 就删除被包裹的动作空间
        Iterator<ActionSpace> newIt = newActionSpaces.iterator();
        while (newIt.hasNext()){
            ActionSpace newAS = newIt.next();
            boolean canJoin = true; //是否可以加入动作空间

            Iterator<ActionSpace> oldIt = actionSpaces.iterator();
            while (oldIt.hasNext()){
                ActionSpace oldAS = oldIt.next();
                if (oldAS.contain(newAS) == 1){
                    //new动作空间被旧的包裹 不用加入
                    canJoin = false;
                    break;
                }
                if (newAS.contain(oldAS) == 1){
                    //old动作空间被新的包裹 删掉旧的
                    oldIt.remove();
                }
            }
            if (canJoin) actionSpaces.add(newAS);
        }
    }



}
