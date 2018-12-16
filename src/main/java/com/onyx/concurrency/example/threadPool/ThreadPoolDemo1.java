package com.onyx.concurrency.example.threadPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 如何给定合适的线程池
 *
 * CPU 秘籍型任务,就需要尽量压榨CPU,餐克制可以设为NCPU+1
 *
 * IO密集型任务,参考值可以设置为2*NCPU
 *
 *
 *
 */
public class ThreadPoolDemo1 {

    public static void main(String[] args) {

        ExecutorService pool = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            final int tmp=i;
            pool.execute(()->{
                System.out.println("tmp:"+tmp);
            });
        }
        pool.shutdown();


    }
}
