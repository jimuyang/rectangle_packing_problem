package com.muyi.rectpack.greedy;

import com.muyi.rectpack.domain.Apex;
import com.muyi.rectpack.domain.Point;
import com.muyi.rectpack.domain.Rect;
import com.muyi.rectpack.domain.RectInfo;
import com.muyi.util.BooleanUtil;
import lombok.Data;

/**
 * @Author: muyi-corp
 * @Date: Created in 12:01 2018/1/15
 * @Description: 占角动作
 */
@Data
public class TakeCorner implements Comparable<TakeCorner> {

    private boolean flag;       //是否是一个标识占角

    private Apex apex;          //使用的顶点
    private Point end;          //终点

    private int rectID;         //放入的小块ID
    private int actionSpaceID;  //使用了哪个动作空间

    private HoleDegree holeDegree;

    public static TakeCorner getFlagInstance(){
        TakeCorner takeCorner = new TakeCorner();
        takeCorner.setFlag(true);
        return takeCorner;
    }

    public RectInfo getRectInfo(){
        Point p1 = this.apex.getPoint();
        Point p2 = this.end;

        int leftX = p1.x < p2.x ? p1.x : p2.x;
        int rightX = p1.x > p2.x ? p1.x : p2.x;
        int upY = p1.y < p2.y ? p1.y : p2.y;
        int downY = p1.y > p2.y ? p1.y : p2.y;

        Point leftUp = new Point(leftX,upY);
        Point rightDown = new Point(rightX,downY);

        return new RectInfo(leftUp,rightDown);
    }

    @Override
    public int compareTo(TakeCorner o) {
        //占角动作的好坏首先决定于穴度
        int holeCompare = this.holeDegree.compareTo(o.getHoleDegree());
        if (holeCompare == 0){
            //此时再按字典序比较 面积-长边-左上角-躺优先
            RectInfo meInfo = this.getRectInfo();
            RectInfo youInfo = o.getRectInfo();

            Rect me = meInfo.getRect();
            Rect you = youInfo.getRect();
            //面积
            if (me.getArea() > you.getArea()) return 1;
            else if (me.getArea() == you.getArea()){
                //长边
                if (me.getLength() > you.getLength()) return 1;
                else if (me.getLength() == you.getLength()){
                    //左上角 x
                    if (meInfo.getLeftUp().x < youInfo.getLeftUp().x) return 1;
                    else if (meInfo.getLeftUp().x == youInfo.getLeftUp().x){
                        //左上角 y
                        if (meInfo.getLeftUp().y < youInfo.getLeftUp().y) return 1;
                        else if (meInfo.getLeftUp().y == youInfo.getLeftUp().y){
                            //躺优先
                            int meDir = BooleanUtil.toInt(meInfo.getXEdge() == me.getLength());
                            int youDir = BooleanUtil.toInt(youInfo.getXEdge() == you.getLength());
                            if (meDir > youDir) return 1;
                            if (meDir == youDir) return 0;
                            else return -1;
                        }
                    }
                }
            }
            return -1;
        }else {
            return holeCompare;
        }
    }
}
