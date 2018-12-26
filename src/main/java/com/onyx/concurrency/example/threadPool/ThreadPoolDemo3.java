package com.onyx.concurrency.example.threadPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ThreadPoolExecutor还提供了如下几个hook（钩子）方法：

 protected void beforeExecute(Thread t, Runnable r);
 protected void afterExecute(Runnable r, Throwable t);
 protected void terminated();

 在ThreadPoolExecutor中这几个方法默认都是空方法，beforeExecute()会在每次任务执行之前调用，afterExecute()会在每次任务结束之后调用，
 terminated()方法则会在线程池被终止时调用。使用这几个方法的方式就是声明一个子类继承ThreadPoolExecutor，并且在子类中重写需要定制的钩子方法，
 最后在创建线程池时使用该子类实例即可。
 */
public class ThreadPoolDemo3 {

    public static void main(String[] args) {

        //就一个线程,以此执行
        ExecutorService pool = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 10; i++) {
            final int tmp=i;
            pool.execute(()->{
                System.out.println("tmp:"+tmp);
            });
        }
        pool.shutdown();


    }
}
