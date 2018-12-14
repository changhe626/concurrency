package com.onyx.concurrency.example.atomic;

import com.onyx.concurrency.annotaions.Recommend;
import com.onyx.concurrency.annotaions.ThreadSafe;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.LongAdder;

/**
 * 模拟并发测试
 * jdk1.8新增的
 * LongAddr
 *
 * 对于普通long和double的读写不是线程安全的,jvm允许拆分成两个32位的操作
 *
 * AtomicBoolean  的compareAndSet  进行唯一的线程的判断.只有一个线程执行任务.
 *
 * LongAdder是jdk8新增的用于并发环境的计数器，目的是为了在高并发情况下，
 * 替AtomicLong/AtomicInt，成为一个用于高并发情况下的高效的通用计数器
 *
 *高并发下计数，一般最先想到的应该是AtomicLong/AtomicInt，AtmoicXXX使用硬件级别的指令 CAS 来更新计数器的值，
 * 这样可以避免加锁，机器直接支持的指令，效率也很高。但是AtomicXXX中的 CAS 操作在出现线程竞争时，失败的线程会白白地循环一次，
 * 在并发很大的情况下，因为每次CAS都只有一个线程能成功，竞争失败的线程会非常多。失败次数越多，循环次数就越多，
 * 很多线程的CAS操作越来越接近 自旋锁（spin lock）。计数操作本来是一个很简单的操作，实际需要耗费的cpu时间应该是越少越好，
 * AtomicXXX在高并发计数时，大量的cpu时间都浪费会在 自旋 上了，这很浪费，也降低了实际的计数效率。
 *
 *
 * 说LongAdder比在高并发时比AtomicLong更高效，这么说有什么依据呢？LongAdder是根据ConcurrentHashMap这类为并发设计的类的基本原理——锁分段，
 * 来实现的，它里面维护一组按需分配的计数单元，并发计数时，不同的线程可以在不同的计数单元上进行计数，这样减少了线程竞争，提高了并发效率。
 * 本质上是用空间换时间的思想，不过在实际高并发情况中消耗的空间可以忽略不计。
 * 现在，在处理高并发计数时，应该优先使用LongAdder，而不是继续使用AtomicLong。当然，线程竞争很低的情况下进行计数，使用Atomic还是更简单更直接，
 * 并且效率稍微高一些。
 * 其他情况，比如序号生成，这种情况下需要准确的数值，全局唯一的AtomicLong才是正确的选择，此时不应该使用LongAdder。
 * https://www.jianshu.com/p/22d38d5c8c2a
 *
 */
@ThreadSafe
@Recommend
public class ConcurrencyTest3 {

    /**
     * 客户端的总数
     */
    private static int clientTotal=5000;
    /**
     * 线程数
     */
    private static int threadTotal=200;
    //计数
    private static LongAdder count=new LongAdder();


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
        System.out.println("值是:"+count.longValue());
        pool.shutdown();
    }

    private static void add(){
        count.increment();
    }

}
