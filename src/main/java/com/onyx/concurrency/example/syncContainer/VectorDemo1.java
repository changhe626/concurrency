package com.onyx.concurrency.example.syncContainer;

import com.onyx.concurrency.annotaions.ThreadSafe;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;


@ThreadSafe
public class VectorDemo1 {

    private static Vector<Integer> vector =new Vector();

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
        System.out.println(vector.size());
    }

    private static void update(int tmp){
        vector.add(tmp);
    }


}
/**
 * 有些同学可能觉得这段代码不会有问题，因为Vector是线程安全的，在多线程环境下理应正常运行。但是这个线程安全是有缺陷的，再迭代的情况下，
 * 我们需要的实际上是对整个迭代过程加锁，而不是对迭代器的hasNext、next等单独的方法加锁。这段代码会报ConcurrentModificationException异常
 *
 * 解决方案很简单，对IteratorRunnable的迭代过程加锁就可以了：
 public void run() {
 while(true) {
 // 对迭代过程加锁
 synchronized (vector) {
 for (Integer i : vector) {

 }
 }
 }
 }

 *
 *


 */
