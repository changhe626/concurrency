package com.onyx.concurrency.example.aqs;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * CountDownLatch和CyclicBarrier都能够实现线程之间的等待，只不过它们侧重点不同：

 　CountDownLatch一般用于某个线程A等待若干个其他线程执行完任务之后，它才执行；

 　而CyclicBarrier一般用于一组线程互相等待至某个状态，然后这一组线程再同时执行；

 　另外，CountDownLatch是不能够重用的，而CyclicBarrier是可以重用的。
 */
public class CyclicBarrierDemo2 {

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
        try {
            barrier.await(2000, TimeUnit.MILLISECONDS);
        }catch (BrokenBarrierException e){
            System.out.println(e);
        }
        System.out.println(threadNo+"开始执行");
    }


}
