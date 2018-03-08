package com.muyi.draw;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @Author: muyi-corp
 * @Date: Created in 11:33 2018/1/16
 * @Description:
 */
public class ButtonDemo extends JFrame {

    private JButton yesButton;
    private JButton noButton;
    private String message;

    public ButtonDemo(){
        super("BUTTON DEMO");

        Container container = this.getContentPane();
        container.setLayout(new BorderLayout());

        JPanel jPanel = new JPanel(new FlowLayout());
        this.yesButton = new JButton("YES");
        this.noButton = new JButton("NO");

        jPanel.add(this.yesButton);
        jPanel.add(this.noButton);
        container.add(jPanel);

        this.setVisible(true);
        this.setSize(1000,800);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }



    public static void main(String[] args) {
        new ButtonDemo();
    }

}
