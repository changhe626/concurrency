package com.onyx.concurrency.example.aqs;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
