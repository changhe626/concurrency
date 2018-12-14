package com.onyx.concurrency.example.count;

import com.onyx.concurrency.annotaions.NotThreadSafe;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * 模拟并发测试
 */
@NotThreadSafe
public class ConcurrencyTest4 {

    /**
     * 客户端的总数
     */
    private static int clientTotal=5000;
    /**
     * 线程数
     */
    private static int threadTotal=200;
    //计数
    private static volatile int count=0;


    public static void main(String[] args) throws InterruptedException {
        ExecutorService pool = Executors.newCachedThreadPool();
        Semaphore semaphore = new Semaphore(threadTotal);
        CountDownLatch downLatch = new CountDownLatch(clientTotal);
        for (int i = 0; i < clientTotal; i++) {
            pool.execute(()->{
                try {
                    semaphore.acquire();
                    add();
                    semaphore.release();
                } catch (InterruptedException e) {
                    System.out.println(e.toString());
                }
                downLatch.countDown();
            });
        }
        downLatch.await();
        System.out.println("值是:"+count);
        pool.shutdown();
    }

    private static void add(){
        count++;
        //1.取出count
        //2.+1
        //3.count写会主存.
    }
    /**
     * volatile ++  不具有原子性.
     * 适合作为标志变量.
     */

}
