package com.onyx.concurrency.example.concurrent;

import com.onyx.concurrency.annotaions.ThreadSafe;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;


/**
 * ConcurrentSkipListSet和TreeSet，它们虽然都是有序的集合。但是，第一，它们的线程安全机制不同，TreeSet是非线程安全的，
 * 而ConcurrentSkipListSet是线程安全的。第二，ConcurrentSkipListSet是通过ConcurrentSkipListMap实现的，而TreeSet是通过TreeMap实现的。
 */
@ThreadSafe
public class ConcurrentSkipListSetDemo1 {

    private static Set<Integer> list=new ConcurrentSkipListSet<>();

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
            final int tmp=i;
            pool.execute(()->{
                try {
                    semaphore.acquire();
                    update(tmp);
                    semaphore.release();
                } catch (InterruptedException e) {
                    System.out.println(e.toString());
                }
                downLatch.countDown();
            });
        }
        downLatch.await();
        pool.shutdown();
        System.out.println(list.size());
    }

    private static void update(int tmp){
        list.add(tmp);
    }


}
