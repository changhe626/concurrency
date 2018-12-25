package com.onyx.concurrency.example.aqs;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * 并发的访问数的控制
 * 仅能提供有限的访问资源.
 *
 * 非公平性体现在哪里？
 当一个线程A执行acquire方法时，会直接尝试获取许可，而不管同一时刻阻塞队列中是否有线程也在等待许可，如果恰好有线程C执行release释放许可，
 并唤醒阻塞队列中第一个等待的线程B，这个时候，线程A和线程B是共同竞争可用许可，不公平性就是这么体现出来的，线程A一点时间都没等待就和线程B同等对待。

 *
 */
public class SemaphoreDemo2 {
    private final static int threadCount=20;

    public static void main(String[] args) throws InterruptedException {

        final Semaphore semaphore = new Semaphore(3);

        ExecutorService pool = Executors.newCachedThreadPool();

        for (int i = 0; i < threadCount; i++) {
            final int tmp=i;

            pool.execute(()->{
                try {
                    //获取三个许可,控制一次最多能够访问的线程的数量
                    //因为那里就只是提供了三个许可,所以一次就全部拿走了,其他线程只能等待塔执行完成了,释放了,在拿到执行.就是顺序执行了
                    //一次拿走的不能超过设置的,否则一直在哪里等待着
                    semaphore.acquire(3);
                    test(tmp);
                    //释放三个许可
                    semaphore.release(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        pool.shutdown();

    }


    private static void test(int i) throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(1000);
        System.out.println("当前执行的是:"+i);
    }

}
