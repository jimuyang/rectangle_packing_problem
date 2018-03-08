package com.muyi.rectpack.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * @Author: muyi-corp
 * @Date: Created in 16:42 2018/1/12
 * @Description: 二维空间向量
 */
@Data
@NoArgsConstructor
public class Point {
    public int x;
    public int y;

    public Point(int x, int y){
        this.x = x;
        this.y = y;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Point point = (Point) o;
        return getX() == point.getX() &&
                getY() == point.getY();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getX(), getY());
    }
}
