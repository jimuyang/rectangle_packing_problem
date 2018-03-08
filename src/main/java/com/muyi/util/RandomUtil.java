package com.muyi.util;

import java.util.Random;

/**
 * @Author: muyi-corp
 * @Date: Created in 18:35 2018/1/16
 * @Description:
 */
public class RandomUtil {

    public static int randomInt(int max){
       return new Random().nextInt(max) + 1;
    }
}
