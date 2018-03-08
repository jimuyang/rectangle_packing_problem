package com.muyi.draw.greedy;

import com.muyi.rectpack.domain.Point;
import com.muyi.rectpack.domain.Rect;
import com.muyi.rectpack.domain.RectInfo;
import com.muyi.rectpack.greedy.ActionSpace;
import com.muyi.rectpack.greedy.Layout;


import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * @Author: muyi-corp
 * @Date: Created in 14:17 2018/1/16
 * @Description:
 */
public class GreedyShow extends JFrame {

    private DrawPanel drawPanel;
    private JPanel waitPutPanel;

    public GreedyShow(Rect container) {
        init(container);
    }

    public List<RectInfo> getPutInRectList() {
        return this.drawPanel.getPutInRectList();
    }

    //初始化布局
    private void init(Rect container) {
        //添加两个Panel
        this.drawPanel = new DrawPanel(container);

        //测试话两个矩形
//        RectInfo rectInfo1 = new RectInfo(new Point(0,0),new Point(100,30));
//        RectInfo rectInfo2 = new RectInfo(new Point(100,30),new Point(200,70));
//        this.drawPanel.getPutInRectList().add(rectInfo1);
//        this.drawPanel.getPutInRectList().add(rectInfo2);

        this.waitPutPanel = new JPanel();
        this.waitPutPanel.setPreferredSize(new Dimension(1000, 100));

        //添加组件
        this.add(waitPutPanel, BorderLayout.NORTH);
        this.add(drawPanel, BorderLayout.CENTER);

        //设置窗体属性
        this.setTitle("演示矩形Packing问题的贪心算法");
        this.setSize(1000, 1000);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void putOne(RectInfo rectInfo) throws Exception {
        Thread.sleep(1000L);
        this.drawPanel.setActionSpace(null);
        this.repaint();
        //
        Thread.sleep(1000L);
        this.getPutInRectList().add(rectInfo);
        this.repaint();
    }

    public void showLayout(Layout layout) throws Exception {
//        Thread.sleep(1000L);
        this.drawPanel.setPutInRectList(layout.getPutInRectList());
        this.repaint();
    }

    public void showActionSpace(ActionSpace actionSpace) throws Exception {
        Thread.sleep(1000L);
        this.drawPanel.setActionSpace(actionSpace);
        this.repaint();
    }

    public void showAll() throws Exception {
        this.repaint();
    }


    public static void main(String[] args) throws Exception {
        GreedyShow greedyShow = new GreedyShow(new Rect(800, 600));
        //Thread.sleep(2000L);

        RectInfo rectInfo = new RectInfo(new Point(400, 30), new Point(500, 70));
        greedyShow.getPutInRectList().add(rectInfo);

        greedyShow.repaint(10);
    }


}
