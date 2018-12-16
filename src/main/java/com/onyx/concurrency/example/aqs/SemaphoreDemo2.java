package com.onyx.concurrency.example.aqs;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * 并发的访问数的控制
 * 仅能提供有限的访问资源.
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
