package com.onyx.concurrency.example.concurrent;

import com.onyx.concurrency.annotaions.ThreadSafe;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

@ThreadSafe
public class CopyOnWriteArrayListDemo1 {

    private static List<Integer> list=new CopyOnWriteArrayList<>();

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