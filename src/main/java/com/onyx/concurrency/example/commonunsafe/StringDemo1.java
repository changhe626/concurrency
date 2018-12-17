package com.onyx.concurrency.example.commonunsafe;

import com.onyx.concurrency.annotaions.NotThreadSafe;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * StringBuilder中的append方法没有使用synchronized关键字，意味着多个线程可以同时访问这个方法。那么问题就来了额，如果两个线程同时访问到这个方法，
 * 那么AbstractStringBuilder中的count是不是就是相同的，所以这两个线程都是在底层char数组的count位置开始append添加，
 * 那么最终的结果肯定就是在后执行的那个线程append进去的数据会将前面一个覆盖掉。
 */
@NotThreadSafe
public class StringDemo1 {

    /**
     * 客户端的总数
     */
    private static int clientTotal=5000;
    /**
     * 线程数
     */
    private static int threadTotal=200;
    //计数
    private static StringBuilder sb=new StringBuilder();


    public static void main(String[] args) throws InterruptedException {
        ExecutorService pool = Executors.newCachedThreadPool();
        Semaphore semaphore = new Semaphore(threadTotal);
        CountDownLatch downLatch = new CountDownLatch(clientTotal);
        for (int i = 0; i < clientTotal; i++) {
            pool.execute(()->{
                try {
                    semaphore.acquire();
                    update();
                    semaphore.release();
                } catch (InterruptedException e) {
                    System.out.println(e.toString());
                }
                downLatch.countDown();
            });
        }
        downLatch.await();
        System.out.println("值是:"+sb.length());
        pool.shutdown();
    }

    private static void update(){
        sb.append("1");
    }
}
