package com.onyx.concurrency.example.aqs;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author zk
 * @Description: 实现一个流控程序。控制客户端每秒调用某个远程服务不超过N次，客户端是会多线程并发调用
 * @date 2018-12-25 14:31
 */
public class SemaphoreDemo5 {


    final static int MAX_QPS=10;
    final static Semaphore semaphore=new Semaphore(MAX_QPS);

    public static void main(String[] args) throws InterruptedException {
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() -> {
            semaphore.release(MAX_QPS / 2);
        }, 1000, 500, TimeUnit.MICROSECONDS);

        //lots of concurrent calls:100 * 1000
        ExecutorService pool = Executors.newFixedThreadPool(100);
        for (int i = 0; i < 100; i++) {
            final int x=i;
            pool.execute(()->{
                for (int j = 0; j < 1000; j++) {
                    semaphore.acquireUninterruptibly(1);
                    remoteCall(x,j);
                }
            });
        }
        /**
         * 将线程池状态置为SHUTDOWN,并不会立即停止：

         停止接收外部submit的任务
         内部正在跑的任务和队列里等待的任务，会执行完
         等到第二步完成后，才真正停止
         */
        pool.shutdown();


        /**
         * 当等待超过设定时间时，会监测ExecutorService是否已经关闭，若关闭则返回true，否则返回false。一般情况下会和shutdown方法组合使用
         */
        pool.awaitTermination(5,TimeUnit.SECONDS);
        System.out.println("DONE");

        /**
         * 将线程池状态置为STOP。企图立即停止，事实上不一定：

         跟shutdown()一样，先停止接收外部提交的任务
         忽略队列里等待的任务
         尝试将正在跑的任务interrupt中断
         返回未执行的任务列表
         */
        //pool.shutdownNow();



    }

    private static void remoteCall(int i, int j) {
        System.out.println(String.format("%s - %s: %d %d",new Date(),
                Thread.currentThread(), i, j));
    }

}
