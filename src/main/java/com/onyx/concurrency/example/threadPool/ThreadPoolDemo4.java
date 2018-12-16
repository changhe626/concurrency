package com.onyx.concurrency.example.threadPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 如何给定合适的线程池???
 */
public class ThreadPoolDemo4 {

    public static void main(String[] args) {

        ScheduledExecutorService pool = Executors.newScheduledThreadPool(5);

        //延迟3秒后执行
        pool.schedule(()->{
            System.out.println("执行多线程任务了");
        },3, TimeUnit.SECONDS);

        //固定时间执行,延迟1s,每隔3s执行一次
        pool.scheduleAtFixedRate(()->{
            System.out.println("定时的多线程任务");
        },1,3,TimeUnit.SECONDS);

        //pool.shutdown();


    }
}
