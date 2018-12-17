package com.onyx.concurrency.example.lock;

import com.onyx.concurrency.annotaions.ThreadSafe;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 模拟并发测试
 *
 *
 * // 获取锁
 takeLock.lock();

 try {
 // 业务逻辑
 } finally {
 // 释放锁
 takeLock.unlock();
 }

 使用try-finally的形式

 */
@ThreadSafe
public class LockDemo1 {

    /**
     * 客户端的总数
     */
    private static int clientTotal=5000;
    /**
     * 线程数
     */
    private static int threadTotal=200;
    //计数
    private static int count=0;

    /**
     * 通过传入一个布尔值来设置公平锁，为true则是公平锁，false则为非公平锁,默认是不公平锁
     */
    private final static Lock lock=new ReentrantLock();


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

    private  static void add(){
        lock.lock();
        try {
            count++;
        }finally {
            lock.unlock();
        }
        /**
         * tryLock()方法会返回一个布尔值，获取锁成功则为true
         * lock.tryLock(3, TimeUnit.SECONDS)
         *
         */
    }

}
