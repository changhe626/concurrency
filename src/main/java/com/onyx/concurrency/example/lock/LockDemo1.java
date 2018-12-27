package com.onyx.concurrency.example.lock;

import com.onyx.concurrency.annotaions.ThreadSafe;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 模拟并发测试
 *
 *
 * // 获取锁
 takeLock.lock();

 try {
 // 业务逻辑
 } finally {
 // 释放锁
 takeLock.unlock();
 }

 使用try-finally的形式

 */
@ThreadSafe
public class LockDemo1 {

    /**
     * 客户端的总数
     */
    private static int clientTotal=5000;
    /**
     * 线程数
     */
    private static int threadTotal=200;
    //计数
    private static int count=0;

    /**
     * 通过传入一个布尔值来设置公平锁，为true则是公平锁，false则为非公平锁,默认是不公平锁
     * 不公平锁是直接进行比较设置,抢占式
     * 公平锁是线程在一个链表中进行排队.
     */
    private final static Lock lock=new ReentrantLock();


    public static void main(String[] args) throws InterruptedException {
        ExecutorService pool = Executors.newCachedThreadPool();
        Semaphore semaphore = new Semaphore(threadTotal);
        CountDownLatch downLatch = new CountDownLatch(clientTotal);
        for (int i = 0; i < clientTotal; i++) {
            pool.execute(()->{
                try {
                    semaphore.acquire();
                    add();
                    semaphore.release();
                } catch (InterruptedException e) {
                    System.out.println(e.toString());
                }
                downLatch.countDown();
            });
        }
        downLatch.await();
        System.out.println("值是:"+count);
        pool.shutdown();
    }

    private  static void add(){
        lock.lock();
        try {
            count++;
        }finally {
            lock.unlock();
        }
        /**
         * tryLock()方法会返回一个布尔值，获取锁成功则为true
         * lock.tryLock(3, TimeUnit.SECONDS)
         *
         */
    }

}
/**
 ReentrantLock
 中获取到锁之后调用了: Thread.currentThread().interrupt()

 首先，一个线程不应该由其他线程来强制中断或停止，而是应该由线程自己自行停止。所以，Thread.stop, Thread.suspend, Thread.resume 都已经被废弃了。
 而 Thread.interrupt 的作用其实也不是中断线程，而是「通知线程应该中断了」，具体到底中断还是继续运行，应该由被通知的线程自己处理。具体来说，
 当对一个线程，调用 interrupt() 时，① 如果线程处于被阻塞状态（例如处于sleep, wait, join 等状态），那么线程将立即退出被阻塞状态，并抛出一个
 InterruptedException异常。仅此而已。② 如果线程处于正常活动状态，那么会将该线程的中断标志设置为 true，仅此而已。被设置中断标志的线程将继续正
 常运行，不受影响。interrupt() 并不能真正的中断线程，需要被调用的线程自己进行配合才行。也就是说，一个线程如果有被中断的需求，那么就可以这样做。
 ① 在正常运行任务时，经常检查本线程的中断标志位，如果被设置了中断标志就自行停止线程。② 在调用阻塞方法时正确处理InterruptedException异常。
 （例如，catch异常后就结束线程。）
 Thread thread = new Thread(() -> {
 while (!Thread.interrupted()) {
 // do more work.
 }
 });
 thread.start();

 // 一段时间以后
 thread.interrupt();
 具体到你的问题，Thread.interrupted()清除标志位是为了下次继续检测标志位。如果一个线程被设置中断标志后，选择结束线程那么自然不存在下次的问题，
 而如果一个线程被设置中断标识后，进行了一些处理后选择继续进行任务，而且这个任务也是需要被中断的，那么当然需要清除标志位了。




 https://www.jianshu.com/p/28bf4345b125


 注意：synchornized、reentrantLock.lock()获取锁操作造成的阻塞在中断状态下不能抛出InterruptException，即获取锁操作是不能被中断的，
 要一直阻塞等到到它获取到锁为止。也就是说如果产生了死锁，则不能被中断。可以使用超时锁来打破死锁，reentrantLock.tryLock(timeout,unit)
 在timeout后会抛出InterruptException，唤醒线程做响应操作

 https://segmentfault.com/a/1190000016083002
 *
 *
 */
