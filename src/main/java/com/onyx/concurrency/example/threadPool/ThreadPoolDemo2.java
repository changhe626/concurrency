package com.onyx.concurrency.example.threadPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池状态转换
 *
 * 各状态之间可能的转变有以下几种：
 RUNNING -> SHUTDOWN
     调用了shutdown方法，线程池实现了finalize方法。
     在finalize内调用了shutdown方法。
     因此shutdown可能是在finalize中被隐式调用的
 (RUNNING or SHUTDOWN) -> STOP
    调用了shutdownNow方法
 SHUTDOWN -> TIDYING
    当队列和线程池均为空的时候
 STOP -> TIDYING
    当线程池为空的时候
 TIDYING -> TERMINATED
    terminated()钩子方法调用完毕


 */
public class ThreadPoolDemo2 {

    public static void main(String[] args) {

        /**
         * 这个其实也很好理解，新起一个线程池：
         线程池最小大小和最大大小保持一致，若该线程没有可供执行的任务，那么即刻关闭该线程，
         当有新task加入时没有空闲线程，那么将该线程加入到无界阻塞队列中，等待执行；
         */
        ExecutorService pool = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 10; i++) {
            final int tmp=i;
            pool.execute(()->{
                System.out.println("tmp:"+tmp);
            });
        }
        pool.shutdown();


    }
}
