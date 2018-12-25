package com.onyx.concurrency.example.threadPool;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * https://www.jianshu.com/p/ade771d2c9c0
 */
public class ThreadPoolDemo6 {


    public static void main(String[] args) {
        ThreadPoolDemo6 tt = new ThreadPoolDemo6();
        tt.start();
    }


    public void start() {
        final AtomicInteger atomicInteger = new AtomicInteger(1);
        /*
         * 定义一个线程池，自定义ThreadFactory，加入自定义的未捕获异常处理器，自定义线程的名字
         */
        MyThreadPoolExecutor pool = new MyThreadPoolExecutor(3, 3, 5, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r, "无敌线程" + atomicInteger.incrementAndGet());
                t.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                    @Override
                    public void uncaughtException(Thread t, Throwable e) {
                        System.out.println("线程 " + t.getId() + " " + t.getName() + " 发生未捕获异常...");
                    }
                });
                return t;
            }
        });

        for (int i = 0; i < 10; i++) {
            final int m = i;
            pool.execute(new Runnable() {

                @Override
                public void run() {
                    System.out.println(m + " execute 线程信息=" + Thread.currentThread().getId() + " " + Thread.currentThread().getName() + " ");
                    if (m == 8) {
                        int n = 10 / 0;
                    }
                    try {
                        Thread.sleep((long) (Math.random() * 15000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    }


    /**
     * 自定义ThreadPoolExecutor
     * <p>
     * 重写newTaskFor beforeExecute afterExecute
     * <p>
     * 线程池里的每一个线程在执行runnable之前之后都需要执行beforeExecute afterExecute方法，这两个方法和runnable在同一个线程中执行。
     * <p>
     * 重写newTaskFor可以自定义返回的FutureTask，重写FutureTask里面的cancle方法可以自定义cancle过程中需要做的事情。
     */
    class MyThreadPoolExecutor extends ThreadPoolExecutor {

        public MyThreadPoolExecutor(int corePoolSize, int maximumPoolSize,
                                    long keepAliveTime, TimeUnit unit,
                                    BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
                    threadFactory);
        }

        @Override
        protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
            return super.newTaskFor(callable);
        }


        @Override
        protected void beforeExecute(Thread t, Runnable r) {
            super.beforeExecute(t, r);
            System.out.println("beforeExecute 线程信息=" + Thread.currentThread().getId() + " " + Thread.currentThread().getName() + " ");
        }

        @Override
        protected void afterExecute(Runnable r, Throwable t) {
            super.afterExecute(r, t);
            System.out.println("afterExecute 线程信息=" + Thread.currentThread().getId() + " " + Thread.currentThread().getName() + " ");
        }

    }


    /**
     * 自定义FutureTask
     */
    class MyFutureTask<T> extends FutureTask<T> {
        public MyFutureTask(Callable<T> callable) {
            super(callable);
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            /**
             * 可以自定义一些cancel的时候应该干的事情........
             */
            return super.cancel(mayInterruptIfRunning);
        }

    }


}
