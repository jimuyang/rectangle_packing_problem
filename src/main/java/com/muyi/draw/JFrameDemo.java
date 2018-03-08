package com.muyi.draw;

import javax.swing.*;

/**
 * @Author: muyi-corp
 * @Date: Created in 11:41 2018/1/16
 * @Description:
 */
public class JFrameDemo extends JFrame {

    public JFrameDemo(){
        super();
        this.initialize();
    }

    public void initialize(){
        this.setSize(1000, 800);//设置窗体的大小
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//设置窗体的关闭方式
        this.setTitle("JFrame 一个容器");//设置窗体标题
    }
}
