package com.onyx.concurrency.example.aqs;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Semaphore（信号量）是用来控制同时访问特定资源的线程数量，它通过协调各个线程，以保证合理的使用公共资源
 *
 * 应用场景
    Semaphore可以用于做流量控制，特别公用资源有限的应用场景，比如数据库连接。假如有一个需求，要读取几万个文件的数据，因为都是IO密集型任务，
    我们可以启动几十个线程并发的读取，但是如果读到内存后，还需要存储到数据库中，而数据库的连接数只有10个，
    这时我们必须控制只有十个线程同时获取数据库连接保存数据，否则会报错无法获取数据库连接。这个时候，我们就可以使用Semaphore来做流控

 Semaphore还提供一些其他方法：

 int availablePermits() ：返回此信号量中当前可用的许可证数。
 int getQueueLength()：返回正在等待获取许可证的线程数。
 boolean hasQueuedThreads() ：是否有线程正在等待获取许可证。
 void reducePermits(int reduction) ：减少reduction个许可证。是个protected方法。
 Collection getQueuedThreads() ：返回所有等待获取许可证的线程集合。是个protected方法。
 */
public class SemaphoreDemo1 {
    private final static int threadCount=20;

    public static void main(String[] args) throws InterruptedException {

        final Semaphore semaphore = new Semaphore(3);

        ExecutorService pool = Executors.newCachedThreadPool();

        for (int i = 0; i < threadCount; i++) {
            final int tmp=i;

            pool.execute(()->{
                try {
                    //获取一个许可,控制一次最多能够访问的线程的数量
                    semaphore.acquire();
                    test(tmp);
                    //释放一个许可
                    semaphore.release();
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
