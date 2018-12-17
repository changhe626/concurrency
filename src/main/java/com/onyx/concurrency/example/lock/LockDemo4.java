package com.onyx.concurrency.example.lock;

import com.onyx.concurrency.annotaions.ThreadSafe;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.StampedLock;

/**
 * jdk1.8新增
 * 读不阻塞写的实现思路：
 在读的时候如果发生了写，则应当重读而不是在读的时候直接阻塞写！即读写之间不会阻塞对方，但是写和写之间还是阻塞的！


 ReentrantReadWriteLock 在沒有任何读写锁时，才可以取得写入锁，这可用于实现了悲观读取。然而，如果读取很多，写入很少的情况下，
 使用 ReentrantReadWriteLock 可能会使写入线程遭遇饥饿问题，也就是写入线程无法竞争到锁定而一直处于等待状态。

 StampedLock有三种模式的锁，用于控制读取/写入访问。StampedLock的状态由版本和模式组成。锁获取操作返回一个用于展示和访问锁状态的票据（stamp）变量，
 它用相应的锁状态表示并控制访问，数字0表示没有写锁被授权访问。在读锁上分为悲观锁和乐观锁。锁释放以及其他相关方法需要使用邮编（stamps）变量作为参数，
 如果他们和当前锁状态不符则失败，这三种模式为：

 • 写入：方法writeLock可能为了获取独占访问而阻塞当前线程，返回一个stamp变量，能够在unlockWrite方法中使用从而释放锁。
 也提供了tryWriteLock。当锁被写模式所占有，没有读或者乐观的读操作能够成功。

 • 读取：方法readLock可能为了获取非独占访问而阻塞当前线程，返回一个stamp变量，能够在unlockRead方法中用于释放锁。也提供了tryReadLock。

 • 乐观读取：方法tryOptimisticRead返回一个非0邮编变量，仅在当前锁没有以写入模式被持有。如果在获得stamp变量之后没有被写模式持有，
 方法validate将返回true。这种模式可以被看做一种弱版本的读锁，可以被一个写入者在任何时间打断。
 乐观读取模式仅用于短时间读取操作时经常能够降低竞争和提高吞吐量。

 https://blog.csdn.net/zero__007/article/details/55805789
 http://ifeve.com/jdk8%E4%B8%ADstampedlock%E5%8E%9F%E7%90%86%E6%8E%A2%E7%A9%B6/

 */
@ThreadSafe
public class LockDemo4 {

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

    private final static StampedLock lock=new StampedLock();


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
        long stamp = lock.writeLock();
        try {
            count++;
        }finally {
            lock.unlockWrite(stamp);
        }
    }

}
