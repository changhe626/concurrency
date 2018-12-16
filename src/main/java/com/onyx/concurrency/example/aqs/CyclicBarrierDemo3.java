package com.onyx.concurrency.example.aqs;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 和CountDownLatch的对比学习
 */
public class CyclicBarrierDemo3 {

    private static CyclicBarrier barrier = new CyclicBarrier(5,()->{
        System.out.println("当到达屏障的时候,优先执行这个runnable,每次到达屏障都会执行的.");
    });

    public static void main(String[] args) throws InterruptedException {
        ExecutorService pool = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            final int tmp=i;
            Thread.sleep(1000);
            pool.execute(()->{
                try {
                    race(tmp);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        pool.shutdown();
    }

    private static void race(int threadNo) throws Exception {
        Thread.sleep(1000);
        System.out.println("is ready :"+threadNo);
        barrier.await();
        System.out.println(threadNo+"开始执行");
    }


}
