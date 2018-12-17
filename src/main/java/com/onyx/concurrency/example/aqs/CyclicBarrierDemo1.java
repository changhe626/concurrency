package com.onyx.concurrency.example.aqs;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * CyclicBarrier也叫同步屏障，在JDK1.5被引入，可以让一组线程达到一个屏障时被阻塞，直到最后一个线程达到屏障时，所以被阻塞的线程才能继续执行。
 CyclicBarrier好比一扇门，默认情况下关闭状态，堵住了线程执行的道路，直到所有线程都就位，门才打开，让所有线程一起通过。

 *
 * CyclicBarrier类位于java.util.concurrent包下，CyclicBarrier提供2个构造器
 * public CyclicBarrier(int parties, Runnable barrierAction) {
 }

 public CyclicBarrier(int parties) {
 }
 * 参数parties指让多少个线程或者任务等待至barrier状态；参数barrierAction为当这些线程都达到barrier状态时会执行的内容。
 *
 *
 * 应用场景:
 * 想象一个场景，运动会男子100米决赛，8名选手。
 *
 *
 * CountDownLatch的区别

 1.CountDownLatch 允许一个或多个线程等待一些特定的操作完成，而这些操作是在其它的线程中进行的，也就是说会出现 等待的线程 和 被等的线程 这样分明的角色；
 2.CountDownLatch 构造函数中有一个 count 参数，表示有多少个线程需要被等待，对这个变量的修改是在其它线程中调用 countDown 方法，每一个不同的线程
 调用一次 countDown 方法就表示有一个被等待的线程到达，count 变为 0 时，latch（门闩）就会被打开，处于等待状态的那些线程接着可以执行；
 3.CountDownLatch 是一次性使用的，也就是说latch门闩只能只用一次，一旦latch门闩被打开就不能再次关闭，将会一直保持打开状态，因此 CountDownLatch
 类也没有为 count 变量提供 set 的方法；

 */
public class CyclicBarrierDemo1 {

    private static CyclicBarrier barrier = new CyclicBarrier(5);

    public static void main(String[] args) throws InterruptedException {
        ExecutorService pool = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            final int tmp=i;
            Thread.sleep(1000);
            pool.execute(()->{
                try {
                    race(tmp);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        pool.shutdown();
    }

    private static void race(int threadNo) throws Exception {
        Thread.sleep(1000);
        System.out.println("is ready :"+threadNo);
        barrier.await();
        System.out.println(threadNo+"开始执行");
    }


}
