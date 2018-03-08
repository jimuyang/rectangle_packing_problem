package com.muyi.rectpack.enums;

import lombok.Getter;

/**
 * @Author: muyi-corp
 * @Date: Created in 22:35 2018/1/14
 * @Description: 点和矩形的关系
 */
@Getter
public enum PointRectRelEnum {

    IN(0),      //在内部
    OUT(-1),    //在外部
    EDGE(1),    //在边上
    APEX(2),    //是顶点

    ;
    private int code;

    PointRectRelEnum(int code){
        this.code = code;
    }

}
