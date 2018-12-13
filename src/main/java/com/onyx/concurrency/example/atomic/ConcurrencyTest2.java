package com.onyx.concurrency.example.atomic;

import com.onyx.concurrency.annotaions.ThreadSafe;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 模拟并发测试
 * AtomicLong
 */
@ThreadSafe
public class ConcurrencyTest2 {

    /**
     * 客户端的总数
     */
    private static int clientTotal=5000;
    /**
     * 线程数
     */
    private static int threadTotal=200;
    //计数
    private static AtomicLong count=new AtomicLong(0L);


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
        System.out.println("值是:"+count.get());
        pool.shutdown();
    }

    private static void add(){
        //先增加再获取
        /**
         * 最终是依靠底层的compareAndSet 进行不对的比对,然后进行值的设置.
         * CAS 的原理.
         * 工作内存和主内存.
         */
        count.incrementAndGet();
        //先获取再增加
        //count.getAndIncrement();
    }

}
