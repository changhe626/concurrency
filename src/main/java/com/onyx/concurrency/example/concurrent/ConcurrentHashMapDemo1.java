package com.onyx.concurrency.example.concurrent;

import com.onyx.concurrency.annotaions.ThreadSafe;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;


/**
 * 安全共享对象策略-总结
 *
 * 线程限制: 一个呗线程限制的对象,由线程独占,并且只能被占有她的线程修改
 *
 * 共享只读:一个哦共享只读的对象,在没有额外同步的情况下,可以被多个线程并发访问,但是任何线程都不能修改它.
 *
 * 线程安全对象:一个线程安全的对象或者容器,在内部通过同步机制来保证线程安全,所有一塔线程无需额外的同步就可以通过公共接口任意访问它.
 *
 * 被守护对象:只能通过特定的锁来访问
 */
@ThreadSafe
public class ConcurrentHashMapDemo1 {

    private static Map<Integer,Integer> map =new ConcurrentHashMap();

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
