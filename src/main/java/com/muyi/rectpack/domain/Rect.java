package com.muyi.rectpack.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: muyi-corp
 * @Date: Created in 21:22 2018/1/14
 * @Description: 只有长宽
 */

@Data
@NoArgsConstructor
public class Rect implements Cloneable {

    private int length;
    private int width;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public int getArea(){
        return this.length * this.width;
    }

    public Rect(int length,int width){
        this.length = length > width ? length : width;
        this.width = width < length ? width : length;
    }
}
