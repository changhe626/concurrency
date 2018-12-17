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

        /**
         * 这个只是简介。这个类是继承的ThreadPoolExecutor，并且实现了ScheduledExecutorService接口，简单来说，
         * 这个类可以进行一些周期性的线程调度工作。
         那么这个时候肯定可以联想到日常业务中的定时器。的确，不过用这个做定时器，现在很少了（有了spring-quartz）。
         */
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
