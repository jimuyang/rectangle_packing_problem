package com.muyi.draw.greedy;

import com.muyi.rectpack.domain.Rect;
import com.muyi.rectpack.domain.RectInfo;
import com.muyi.rectpack.greedy.ActionSpace;
import lombok.Data;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: muyi-corp
 * @Date: Created in 14:27 2018/1/16
 * @Description:
 */
@Data
public class DrawPanel extends JPanel {

    private Rect container;
    private List<RectInfo> putInRectList = new ArrayList<>();

    private ActionSpace actionSpace;
    private Color asColor = new Color(0,0,0);

    //颜色数组
    private Color[] colors = new Color[5];
    private int colorIndex;

    DrawPanel(Rect container){
        super();

        //初始化颜色数组
        colors[0] = new Color(27,80,255);
        colors[1] = new Color(255,62,59);
        colors[2] = new Color(68,255,53);
        colors[3] = new Color(255,233,43);
        colors[4] = new Color(255,88,247);
        //初始化container和putInRectList
        this.container = container;

    }

    public Color getOneColor(){
        if (this.colorIndex > 4) this.colorIndex -= 5;
        Color color = this.colors[colorIndex];
        this.colorIndex ++;
        return color;
    }


    @Override
    public void paint(Graphics g) {
        super.paint(g);
        //先画出 container
        g.drawRect(0,0,this.container.getLength(),this.container.getWidth());

        //重置colorIndex
        this.colorIndex = 0;

        for (int i = 0; i < putInRectList.size(); i ++) {
            RectInfo rectInfo = putInRectList.get(i);
            this.fillRect(g,rectInfo,this.getOneColor());
        }

        //如果动作空间存在 就画出 动作空间
        if (this.actionSpace != null){
            this.fillRect(g,this.actionSpace,this.asColor);
        }
    }

    private void fillRect(Graphics g, RectInfo rectInfo, Color color){
        g.setColor(color);
        g.fillRect(
                rectInfo.getLeftUp().x,
                rectInfo.getLeftUp().y,
                rectInfo.getXEdge(),
                rectInfo.getYEdge());
    }

}
