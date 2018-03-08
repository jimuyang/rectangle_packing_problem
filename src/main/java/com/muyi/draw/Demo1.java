package com.muyi.draw;

import javax.swing.*;
import java.awt.*;

/**
 * @Author: muyi-corp
 * @Date: Created in 17:31 2018/1/12
 * @Description:
 */
public class Demo1 {

    public static class DrawCircle extends JFrame {

        private final int OVAL_WIDTH=80;//圆形的宽
        private final int OVAL_HEIGHT=80;//圆形的高
        public DrawCircle(){
            super();
            initialize();//调用初始化方法
        }
        //初始化方法
        private void initialize(){
            this.setSize(300, 200);//设置窗体的大小
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//设置窗体的关闭方式
            setContentPane(new DrawPanel());//设置窗体面板为绘图面板对象
            this.setTitle("绘图实例2");//设置窗体标题
        }

        class DrawPanel extends JPanel{
            public void paint(Graphics g){
                super.paint(g);
                g.drawOval(10, 10, OVAL_WIDTH, OVAL_HEIGHT);//绘制第1个圆形
                g.drawOval(80, 10, OVAL_WIDTH, OVAL_HEIGHT);//绘制第2个圆形
                g.drawOval(150, 10, OVAL_WIDTH, OVAL_HEIGHT);//绘制第3个圆形
                g.drawOval(50, 70, OVAL_WIDTH, OVAL_HEIGHT);//绘制第4个圆形
                g.drawOval(120, 70, OVAL_WIDTH, OVAL_HEIGHT);//绘制第5个圆形

            }
        }
        public static void main(String[] args) {
            // TODO Auto-generated method stub
            DrawCircle dc = new DrawCircle();//初始化对象且调用构造方法
            dc.setVisible(true);//窗体可视化
        }

    }




}
