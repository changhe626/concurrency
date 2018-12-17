package com.onyx.concurrency.example.threadPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 如何给定合适的线程池
 *
 * CPU 秘籍型任务,就需要尽量压榨CPU,餐克制可以设为NCPU+1
 *
 * IO密集型任务,参考值可以设置为2*NCPU
 *
 * https://blog.csdn.net/Holmofy/article/details/81271839
 *
 */
public class ThreadPoolDemo1 {

    public static void main(String[] args) {

        /**
         * 这个方法是新起一个线程池，允许线程池内的线程在空闲一定时间后，若还没有新任务加入，才关闭该线程，而不是直接关闭该线程。
         *
         * 这个的意思是：新起一个线程池，特点：
         最小线程数为0，最大为近乎无限，在线程池内该线程持续60s没有任务执行时，才关闭该线程，当有任务时，直接新起一个线程进行执行，
         若没有线程，则执行默认的处理策略进行处理(默认直接丢弃)。


         newFixedThreadPool由于有一个近乎无限的缓冲队列，那么任意多的请求过来，都可以放到缓冲队列中等待处理，但是由于处理的线程数固定，
         对于突发性的业务爆发，无法进行应对(若平时开启的很大线程数，浪费系统资源)，可能会导致系统反应缓慢；

         newCachedThreadPool由于在理论上只要有请求，都可以即时开启一个新线程进行响应，因而相应相对较快;但是一个系统内的最大线程数是有限的，
         一旦超过系统最大的线程数，那么多余的请求都会被直接抛弃掉(这种情况很难排查)，这个时候可能反而起反作用。
         若同时开启的线程数较多，且jvm参数设置不合理(如内存分配较小，gc参数设置不合理)，那么可能会导致系统发生频繁的gc，或者直接导致oom事件发生。


         */
        ExecutorService pool = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            final int tmp=i;
            pool.execute(()->{
                System.out.println("tmp:"+tmp);
            });
        }
        /**
         * 这个方法主要是提供了关闭线程池的操作，调用此方法后，线程池不再接收新的任务，但是会把当前缓存队列的任务全部执行完毕。
         */
        pool.shutdown();

        /**
         *这个方法调用后，不但不能接收新的任务，也会尝试中断正在执行的任务，同时不再执行缓存队列中的任务。
         */
        //pool.shutdownNow();


    }
}
