package com.onyx.concurrency.example.commonunsafe;

import com.onyx.concurrency.annotaions.NotThreadSafe;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

@NotThreadSafe
public class DateFormatDemo1 {

    /**
     * SimpleDateFormat  不是线程安全的
     */
    private static SimpleDateFormat format=new SimpleDateFormat("yyyymmdd");

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
                    update();
                    semaphore.release();
                } catch (InterruptedException e) {
                    System.out.println(e.toString());
                }
                downLatch.countDown();
            });
        }
        downLatch.await();

        pool.shutdown();
    }

    private static void update(){
        try {
            format.parse("20181216");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
