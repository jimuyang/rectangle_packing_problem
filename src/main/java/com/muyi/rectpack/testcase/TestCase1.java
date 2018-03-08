package com.muyi.rectpack.testcase;

import com.muyi.rectpack.domain.Rect;
import com.muyi.rectpack.greedy.Layout;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: muyi-corp
 * @Date: Created in 13:47 2018/1/22
 * @Description:
 */
public class TestCase1 {

    public static Layout newInstance(){
        List<Rect> unPutRectList = new ArrayList<>();
        unPutRectList.add(new Rect(100,300));
        unPutRectList.add(new Rect(100,300));
        unPutRectList.add(new Rect(100,300));
        unPutRectList.add(new Rect(100,300));
        unPutRectList.add(new Rect(200,200));
        Layout layout = Layout.initLayout(400,400,unPutRectList);
        return layout;
    }
}
