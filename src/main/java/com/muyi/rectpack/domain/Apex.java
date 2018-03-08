package com.muyi.rectpack.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author: muyi-corp
 * @Date: Created in 22:08 2018/1/14
 * @Description: 矩形顶点 包括一个点 和它相对于矩形中心的位置
 */
@Data
@AllArgsConstructor
public class Apex {
    Point point;
    boolean left;
    boolean up;
}
