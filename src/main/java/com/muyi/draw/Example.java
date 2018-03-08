package com.muyi.draw;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * @Author: muyi-corp
 * @Date: Created in 17:33 2018/1/12
 * @Description:
 */
public class Example extends JFrame{

    public Example(){
        super();
        initialize();
    }

    //初始化方法
    public void initialize(){
        this.setSize(1000, 800);//设置窗体的大小
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//设置窗体的关闭方式
        this.setTitle("开始画图把");//设置窗体标题
        add(new CanvasPanel());
    }

    //创建内部类
    class CanvasPanel extends JPanel{
        @Override
        public void paint(Graphics g) {
            super.paint(g);

            g.drawRect(0,0,100,100);

            g.setColor(Color.GREEN);
            g.fillRect(400,200,100,100);

            g.setColor(Color.BLACK);
            g.fillRect(400,500,100,100);



            Graphics2D graphics2D = (Graphics2D)g;
            Shape rectShape =  new Rectangle2D.Double(110,5,100,100);
            graphics2D.setColor(Color.RED);
            graphics2D.fill(rectShape);

        }

    }



    public static void main(String[] args) {
//        new Example().setVisible(true);
        JFrame jFrame = new Example();
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
    }

}
