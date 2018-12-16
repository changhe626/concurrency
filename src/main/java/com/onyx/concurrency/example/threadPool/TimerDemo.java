package com.onyx.concurrency.example.threadPool;

import com.onyx.concurrency.annotaions.NotRecommend;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 不推荐使用
 */
@NotRecommend
public class TimerDemo {
    public static void main(String[] args) {
        //每5秒执行一次
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("执行了");
            }
        },new Date(),5*1000);
    }
}
