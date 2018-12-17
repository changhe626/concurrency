package com.onyx.concurrency.example.aqs;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * CountDownLatch是通过一个计数器来实现的，计数器的初始值为线程的数量。每当一个线程完成了自己的任务后，计数器的值就会减1。当计数器值到达0时，
 * 它表示所有的线程已经完成了任务，然后在闭锁上等待的线程就可以恢复执行任务。
 */
public class CountDownLatchDemo1 {

    private final static int threadCount=200;

    public static void main(String[] args) throws InterruptedException {

        ExecutorService pool = Executors.newCachedThreadPool();
        CountDownLatch count = new CountDownLatch(threadCount);

        /**
         * 放在这里,就会一直等待count.countDown(); 执行完成,在执行打印....
         * count.await();
         * System.out.println("已经全部执行完成了");
         */

        for (int i = 0; i < threadCount; i++) {
            final int tmp=i;
            pool.execute(()->{
                try {
                    test(tmp);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    //这里是放在finally里面的.
                    count.countDown();
                }
            });
        }
        count.await();
        System.out.println("已经全部执行完成了");
        pool.shutdown();

    }


    private static void test(int i) throws InterruptedException {
        Thread.sleep(100);
        System.out.println(i);
        Thread.sleep(100);
    }


}
