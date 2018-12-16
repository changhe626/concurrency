package com.onyx.concurrency.example.aqs;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CyclicBarrierDemo1 {

    private static CyclicBarrier barrier = new CyclicBarrier(5);

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
