package com.muyi.rectpack.greedy;

import lombok.Data;

/**
 * @Author: muyi-corp
 * @Date: Created in 22:54 2018/1/14
 * @Description:
 */

/**
 * 穴度
 * 穴度表示占角动作与当前动作空间的紧密程度,
 * 其具体定义为一个三元组,即〈贴边数,平整度,他贴边数〉
 * 可依字典序比较不同穴度的大小.穴度越大,说明放入块与当前动作空间相贴的边越多,对剩余空间的损害越小,与其他已放入块的关系越紧密,相应的占角动作越好
 */
@Data
public class HoleDegree implements Comparable<HoleDegree>{

    //表示放入块与当前动作空间相贴的边的数目,ki∈{2,3,4}.
    private int tieBianNum;
    //表示块放入后剩余空间的平整程度
    private int pingZhengDegree;
    //表示其他已放入块及矩形框有多少条边被该动作块贴住.
    private int taTieBianNum;

    @Override
    public int compareTo(HoleDegree other) {
        //贴边数越大越好
        if (this.tieBianNum > other.tieBianNum) return 1;
        else if (this.tieBianNum == other.tieBianNum){
            //平整度越小越好
            if (this.pingZhengDegree < other.pingZhengDegree) return 1;
            else if (this.pingZhengDegree == other.pingZhengDegree){
                //他贴边数越大越好
                if (this.taTieBianNum > other.taTieBianNum) return 1;
                else if (this.taTieBianNum == other.taTieBianNum) return 0;
            }
        }
        return -1;
    }

    @Override
    public String toString() {
        return "HoleDegree{" +
                "tieBianNum=" + tieBianNum +
                ", pingZhengDegree=" + pingZhengDegree +
                ", taTieBianNum=" + taTieBianNum +
                '}';
    }
}
