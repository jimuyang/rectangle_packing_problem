package com.muyi.rectpack.greedy;

import com.muyi.rectpack.domain.Rect;
import com.muyi.util.JsonUtil;
import com.muyi.util.RandomUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * @Author: muyi-corp
 * @Date: Created in 18:22 2018/1/19
 * @Description: 使用回溯对贪心算法进行增强
 */
@Data
@Slf4j
public class GreedyImplAdvice {

    //private Layout cLayout;
    private GreedyImpl greedy = new GreedyImpl();

    private int lowerBound = 5;
    private int upperBound = 10;
    private int k = 4;

    //为了时间开销 将每一步的格局都存储在一个栈中
    private Stack<Layout> layoutHistory = new Stack<>();

    //还没有试过的占角动作
    private Stack<TakeCorner> takeCornerStack = new Stack<>();

    //目前为止面积最大的终止格局 也用占角动作栈来存储 可以看出放入顺序
    //private Stack<TakeCorner> bestEndLayout = new Stack<>();
    private Layout bestLayout;
    private int bestArea = 0;

    /**
     * 算法入口
     */
    public void start() throws Exception {

        this.greedy = new GreedyImpl();

        int width = 600;
        int length = 800;
        List<Rect> waitPutRectList = new ArrayList<>();

        //生成512以内的 小块 n 个
        //int n = RandomUtil.randomInt(16);
        for (int i = 0; i < 10; i++) {
            waitPutRectList.add(
                    new Rect(RandomUtil.randomInt(512), RandomUtil.randomInt(512))
            );
        }

        //初始化一个空的格局和等待放入的小块列表
        Layout initLayout = Layout.initLayout(width, length, waitPutRectList);

        this.begin(initLayout);

    }

    /**
     *
     */
    public void begin(Layout initLayout) throws Exception{
        //算法开始 --layoutHistory中有一个初始格局
        this.layoutHistory.push(initLayout);
        this.pushGoodTakeCornerList(this.layoutHistory.peek());

        while (!this.takeCornerStack.empty()) {
            //log.info("占角动作栈：{}",this.takeCornerStack.size());
            //log.info("layoutHistory:{}",this.layoutHistory.size());

            TakeCorner takeCorner = this.takeCornerStack.pop();
            if (!takeCorner.isFlag()) {
                //这个占角动作还没有尝试过
                //push一个flag
                this.takeCornerStack.push(TakeCorner.getFlagInstance());
                Layout layout = (Layout) this.layoutHistory.peek().clone();
                layout.doTakeCorner(takeCorner);
                //此时为新格局
                this.layoutHistory.push(layout);
                //该格局还可以继续占角吗？
                boolean canTakeCorner = this.pushGoodTakeCornerList(layout);
                if (!canTakeCorner) {
                    //此时为终止格局 计算面积并比较
                    int area = layout.calcArea();
                    if (area > this.bestArea) {
                        this.bestLayout = layout;
                        this.bestArea = area;

                        //最后画出最好的格局
                        this.bestLayout.getShow().showLayout(this.bestLayout);
                    }
                }
            }else {
                //遇到了flag 说明该占角动作及后续的情况都已经尝试过
                this.layoutHistory.pop();
            }
        }

        //最后画出最好的格局
        this.bestLayout.getShow().showLayout(this.bestLayout);
        log.info("END!");

    }


    /**
     * 当前格局下取得前 1/k 的占角动作
     * 并压入takeCornerStack中
     */
    private boolean pushGoodTakeCornerList(Layout layout) throws Exception {
        List<TakeCorner> takeCorners = this.greedy.loopActionSpace(layout);
        if (takeCorners == null || takeCorners.size() == 0) {
            return false;
        }
        int n = takeCorners.size() / this.k;
        n = n > this.upperBound ? this.upperBound : n;
        n = n < this.lowerBound ? this.lowerBound : n;
        n = n > takeCorners.size() ? takeCorners.size() : n;
        // return takeCorners.subList(0, n);
        for (int i = n-1; i >= 0; i--) {
            this.takeCornerStack.push(takeCorners.get(i));
        }
        return true;
    }

    /**
     * 从初始格局出发 依据占角动作栈 生成终止格局
     */
    private void genEndLayout(Stack<TakeCorner> takeCorners, Layout initLayout) throws Exception {
        while (!takeCorners.empty()) {
            initLayout.doTakeCorner(takeCorners.pop());
        }
    }


}
