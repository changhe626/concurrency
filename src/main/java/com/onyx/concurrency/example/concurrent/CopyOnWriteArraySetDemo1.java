package com.onyx.concurrency.example.concurrent;

import com.onyx.concurrency.annotaions.ThreadSafe;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * https://blog.csdn.net/a953713428/article/details/57403164
 *
 * https://www.cnblogs.com/skywang12345/p/3498497.html
 */
@ThreadSafe
public class CopyOnWriteArraySetDemo1 {

    private static Set<Integer> list=new CopyOnWriteArraySet<>();

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
