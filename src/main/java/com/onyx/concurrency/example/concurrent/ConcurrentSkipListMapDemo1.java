package com.onyx.concurrency.example.concurrent;

import com.onyx.concurrency.annotaions.ThreadSafe;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;


/**
 * ConcurrentSkipListMap 比 ConcurrentHashMap 多用存储空间(用空间换时间), 但它有着ConcurrentHashMap不能比拟的优点: 有序数据存储,
 * 基于的就是 skip list.
 *
 * concurrentHashMap与ConcurrentSkipListMap性能测试
 在4线程1.6万数据的条件下，ConcurrentHashMap 存取速度是ConcurrentSkipListMap 的4倍左右。

 但ConcurrentSkipListMap有几个ConcurrentHashMap 不能比拟的优点：
 1、ConcurrentSkipListMap 的key是有序的。
 2、ConcurrentSkipListMap 支持更高的并发。ConcurrentSkipListMap 的存取时间是log（N），和线程数几乎无关。也就是说在数据量一定的情况下，
 并发的线程越多，ConcurrentSkipListMap越能体现出他的优势。
 *
 * https://www.jianshu.com/p/edc2fd149255
 * https://blog.csdn.net/guangcigeyun/article/details/8278349
 */
@ThreadSafe
public class ConcurrentSkipListMapDemo1 {

    private static Map<Integer,Integer> map =new ConcurrentSkipListMap<>();

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
