package com.onyx.concurrency.example.threadPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 如何给定合适的线程池???
 */
public class ThreadPoolDemo2 {

    public static void main(String[] args) {

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
