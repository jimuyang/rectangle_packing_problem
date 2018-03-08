package com.muyi.rectpack.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: muyi-corp
 * @Date: Created in 13:38 2018/1/15
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Section {

    private int left;
    private int right;

    public boolean isPoint(){
        return left == right;
    }
}
