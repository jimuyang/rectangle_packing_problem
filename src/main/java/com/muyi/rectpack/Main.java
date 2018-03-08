package com.muyi.rectpack;

import com.muyi.rectpack.greedy.GreedyImpl;
import com.muyi.rectpack.greedy.GreedyImplAdvice;
import com.muyi.rectpack.testcase.TestCase1;

/**
 * @Author: muyi-corp
 * @Date: Created in 16:41 2018/1/12
 * @Description:
 */
public class Main {


    public static void main(String[] args) throws Exception{
        new GreedyImplAdvice().begin(TestCase1.newInstance());
    }


}
