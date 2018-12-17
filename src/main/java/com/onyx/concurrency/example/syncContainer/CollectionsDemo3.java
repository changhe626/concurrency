package com.onyx.concurrency.example.syncContainer;

import com.google.common.collect.Maps;
import com.onyx.concurrency.annotaions.NotThreadSafe;
import com.onyx.concurrency.annotaions.ThreadSafe;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 ConcurrentHashMap
 当你程序需要高度的并行化的时候，你应该使用ConcurrentHashMap
 尽管没有同步整个Map，但是它仍然是线程安全的
 读操作非常快，而写操作则是通过加锁完成的
 在对象层次上不存在锁（即不会阻塞线程）
 锁的粒度设置的非常好，只对哈希表的某一个key加锁
 ConcurrentHashMap不会抛出ConcurrentModificationException，即使一个线程在遍历的同时，另一个线程尝试进行修改。
 ConcurrentHashMap会使用多个锁

 SynchronizedHashMap
 会同步整个对象
 每一次的读写操作都需要加锁
 对整个对象加锁会极大降低性能
 这相当于只允许同一时间内至多一个线程操作整个Map，而其他线程必须等待
 它有可能造成资源冲突（某些线程等待较长时间）
 SynchronizedHashMap会返回Iterator，当遍历时进行修改会抛出异常
 */
@ThreadSafe
public class CollectionsDemo3 {

    private static Map<Integer,Integer> map = Collections.<Integer,Integer>synchronizedMap(Maps.newHashMap());

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
