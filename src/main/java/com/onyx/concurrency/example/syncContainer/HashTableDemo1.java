package com.onyx.concurrency.example.syncContainer;

import com.onyx.concurrency.annotaions.ThreadSafe;

import java.util.Hashtable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Hashtable是Java 1.1提供的旧有类，从性能上和使用上都不如其他的替代类，因此已经不推荐使用
 *
 * Hashtable 和 synchronizedMap 所采取的获得同步的简单方法（同步 Hashtable 中或者同步的 Map 包装器对象中的每个方法）有两个主要的不足。
 * 首先，这种方法对于可伸缩性是一种障碍，因为一次只能有一个线程可以访问hash表。 同时，这样仍不足以提供真正的线程安全性，
 * 许多公用的混合操作仍然需要额外的同步。虽然诸如 get() 和 put() 之类的简单操作可以在不需要额外同步的情况下安全地完成，
 * 但还是有一些公用的操作序列 ，例如迭代或者put-if-absent（空则放入），需要外部的同步，以避免数据争用。
 *
 *
 */
@ThreadSafe
public class HashTableDemo1 {

    private static Hashtable<Integer,Integer> map =new Hashtable();

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
        System.out.println(map.size());
    }

    private static void update(int tmp){
        map.put(tmp,tmp);
    }


}
