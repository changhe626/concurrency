package com.onyx.concurrency.example.aqs;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CountDownLatchDemo2 {

    private final static int threadCount=200;

    public static void main(String[] args) throws InterruptedException {

        ExecutorService pool = Executors.newCachedThreadPool();
        CountDownLatch count = new CountDownLatch(threadCount);

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
        //只是等待制定的时间,不管执行完成了吗?都执行下面的代码...
        count.await(10, TimeUnit.MILLISECONDS);
        System.out.println("已经全部执行完成了");
        pool.shutdown();

    }


    private static void test(int i) throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
        System.out.println(i);
    }


}
