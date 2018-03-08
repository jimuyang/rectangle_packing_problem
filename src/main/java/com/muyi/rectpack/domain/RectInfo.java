package com.muyi.rectpack.domain;

import lombok.Data;

/**
 * @Author: muyi-corp
 * @Date: Created in 16:46 2018/1/12
 * @Description: 存储一个矩形的位置和长宽等信息
 */

@Data
public class RectInfo implements Cloneable{

    //因为矩形不能歪着放置 那么一定会有一个左上角 一个右下角
    private Point leftUp;    //左上角
    private Point rightDown; //右下角

    private Point leftDown;  //左下角
    private Point rightUp;   //右上角

    private Apex[] apexes;   //矩形的顶点们 另一种储存点的方式

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String toString(){
        return String.format("[(%d,%d):(%d,%d)]",
                leftUp.x,leftUp.y,rightDown.x,rightDown.y);
    }

    /**
     * 两个点就可以确定一个矩形的位置 这里取 左上角 右下角
     * 左上角更靠近 (0,0) 点
     */
    public RectInfo(Point leftUp, Point rightDown){

        //处理下两个点的顺序
        if (leftUp.x > rightDown.x){
            Point temp = leftUp;
            leftUp = rightDown;
            rightDown = temp;
        }

        //生成四个点
        this.leftUp = leftUp;
        this.rightDown = rightDown;
        this.leftDown = new Point(leftUp.x,rightDown.y);
        this.rightUp = new Point(rightDown.x,leftUp.y);

        //生成顶点数组
        Apex[] apexes = new Apex[4];
        apexes[0] = new Apex(this.leftUp,true,true);
        apexes[1] = new Apex(this.rightDown,false,false);
        apexes[2] = new Apex(this.leftDown,true,false);
        apexes[3] = new Apex(this.rightUp,false,true);
        this.apexes = apexes;
    }

    //得到rect x方向的长度
    public int getXEdge(){
        return this.getRightDown().x - this.getLeftUp().x;
    }

    //得到rect y方向的长度
    public int getYEdge(){
        return this.getRightDown().y - this.getLeftUp().y;
    }

    /**
     * 得到长宽面积
     */
    public Rect getRect(){
        return new Rect(this.getRightDown().y - this.getLeftUp().y,
                        this.getRightDown().x - this.getLeftUp().x);
    }

    /**
     * 判断是不是一个矩形
     */
    public boolean isRectangle(){
        return this.leftUp.x < this.rightDown.x
                && this.leftUp.y < this.rightDown.y;
    }

    /**
     * 判断一个点和一个矩形的关系
     */
    public static int judgePoint2Rect(Point point,RectInfo rectInfo){

        //一个矩形是由四条直线组成

        //在这个矩形的内部或边上
        if (rectInfo.getLeftUp().x <= point.x && point.x <= rectInfo.getRightDown().x
                && rectInfo.getLeftUp().y <= point.y && point.y <= rectInfo.getRightDown().y ){
            //判断是否在矩形的边上
            int i = 0;
            if (point.x == rectInfo.getLeftUp().x || point.x == rectInfo.getRightDown().x){
                i ++;
            }
            if (point.y == rectInfo.getLeftUp().y || point.y == rectInfo.getRightDown().y){
                i ++;
            }
            return i;
        }
        return -1;
    }
    /**
     * 判断是否在另一个矩形内或包裹另一个矩形
     */
    public int contain(RectInfo rectInfo){
        RectInfo joinRect = judgeRect2Rect(this,rectInfo);
        if (joinRect == null){
            return 0;
        }
        if (joinRect.getLeftUp().equals(rectInfo.getLeftUp()) && joinRect.getRightDown().equals(rectInfo.getRightDown())){
            //this包裹了另一个矩形
            return 1;
        }
        if (joinRect.getLeftUp().equals(this.getLeftUp()) && joinRect.getRightDown().equals(this.getRightDown())){
            //this被另一个矩形包裹
            return -1;
        }
        return 0;
    }

    /**
     * 判断 2个矩形的关系
     *  rect1 与 rect2 相交 相接 相离
     *  （相接于一个点处理为相离）
     */
    public static RectInfo judgeRect2Rect(RectInfo rect1, RectInfo rect2){
        //矩形的关系 转化为x,y方向上的区间关系
        //x 方向 两个区间
        Section r1XSection = new Section(rect1.getLeftUp().x,rect1.getRightDown().x);
        Section r2XSection = new Section(rect2.getLeftUp().x,rect2.getRightDown().x);
        Section xJoinSection = judge2Sections(r1XSection,r2XSection);

        //y 方向 两个区间
        Section r1YSection = new Section(rect1.getLeftUp().y,rect1.getRightDown().y);
        Section r2YSection = new Section(rect2.getLeftUp().y,rect2.getRightDown().y);
        Section yJoinSection = judge2Sections(r1YSection,r2YSection);

        if (xJoinSection == null || yJoinSection == null || (xJoinSection.isPoint() && yJoinSection.isPoint())) {
            //只要有一个方向不重叠 或者 两个区间都是点 相离
            return null;
//        }else if ((xJoinSection.isPoint() && !yJoinSection.isPoint())
//                || (yJoinSection.isPoint() && !xJoinSection.isPoint()))
        }else {
            //相接 或 相交
            return new RectInfo(
                    new Point(xJoinSection.getLeft(),yJoinSection.getLeft()),
                    new Point(xJoinSection.getRight(),yJoinSection.getRight()));
        }
    }

    /**
     * 比较 2个区间的关系
     * @return 相交 -- 相交的区间
     * @return 相接 -- 一个点
     * @return 相离 -- null
     */
    public static Section judge2Sections(Section section1,Section section2){

        //先排个序 section1在左
        if (section1.getLeft() > section2.getLeft()){
            Section temp = section1;
            section1 = section2;
            section2 = temp;
        }

        if (section2.getLeft() <= section1.getRight()){
            //此时一定相交或相离
            if (section2.getRight() > section1.getRight()){
                return new Section(section2.getLeft(),section1.getRight());
            }else {
                return new Section(section2.getLeft(),section2.getRight());
            }
//        }else if (section2.getLeft() == section1.getRight()){
//            //此时一定相接
//            return new Section(section2.getLeft(),section2.getLeft());
        }else {
            //此时相离
            return null;
        }
    }
}
