package com.onyx.concurrency.example.atomic;


import com.onyx.concurrency.annotaions.ThreadSafe;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * 其基本的特性就是在多线程环境下，当有多个线程同时执行这些类的实例包含的方法时，具有排他性，即当某个线程进入方法，执行其中的指令时，
 * 不会被其他线程打断，而别的线程就像自旋锁一样，一直等到该方法执行完成，才由JVM从等待队列中选择一个另一个线程进入，这只是一种逻辑上的理解。
 * 实际上是借助硬件的相关指令来实现的，不会阻塞线程(或者说只是在硬件级别上阻塞了)。
 *
 * 一般情况下，我们使用 AtomicBoolean 高效并发处理 “只初始化一次” 的功能要求
 * AtomicBoolean就是使用了Volatile属性来完成的
 */
@ThreadSafe
public class ConcurrencyTest7 {

    private static AtomicBoolean isHappened=new AtomicBoolean(false);

    /**
     * 客户端的总数
     */
    private static int clientTotal=5000;
    /**
     * 线程数
     */
    private static int threadTotal=200;

    public static void main(String[] args) throws InterruptedException {
        ExecutorService pool = Executors.newCachedThreadPool();
        Semaphore semaphore = new Semaphore(threadTotal);
        CountDownLatch downLatch = new CountDownLatch(clientTotal);
        for (int i = 0; i < clientTotal; i++) {
            pool.execute(()->{
                try {
                    semaphore.acquire();
                    test();
                    semaphore.release();
                } catch (InterruptedException e) {
                    System.out.println(e.toString());
                }
                downLatch.countDown();
            });
        }
        downLatch.await();
        System.out.println("值是:"+isHappened.get());
        pool.shutdown();

    }

    private static void test(){
        if(isHappened.compareAndSet(false,true)){
            System.out.println("execute");
        }
    }





}
