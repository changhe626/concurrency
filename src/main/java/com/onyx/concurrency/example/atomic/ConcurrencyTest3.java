package com.onyx.concurrency.example.atomic;

import com.onyx.concurrency.annotaions.Recommend;
import com.onyx.concurrency.annotaions.ThreadSafe;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.LongAdder;

/**
 * 模拟并发测试
 * jdk1.8新增的
 * LongAddr
 *
 * 对于普通long和double的读写不是线程安全的,jvm允许拆分成两个32位的操作
 *
 * AtomicBoolean  的compareAndSet  进行唯一的线程的判断.只有一个线程执行任务.
 *
 */
@ThreadSafe
@Recommend
public class ConcurrencyTest3 {

    /**
     * 客户端的总数
     */
    private static int clientTotal=5000;
    /**
     * 线程数
     */
    private static int threadTotal=200;
    //计数
    private static LongAdder count=new LongAdder();


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
        System.out.println("值是:"+count.longValue());
        pool.shutdown();
    }

    private static void add(){
        count.increment();
    }

}
