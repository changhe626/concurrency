package com.onyx.concurrency.example.threadPool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 如何给定合适的线程池???
 *
 1、用ThreadPoolExecutor自定义线程池，看线程是的用途，如果任务量不大，可以用无界队列，如果任务量非常大，要用有界队列，防止OOM
 2、如果任务量很大，还要求每个任务都处理成功，要对提交的任务进行阻塞提交，重写拒绝机制，改为阻塞提交。保证不抛弃一个任务
 3、最大线程数一般设为2N+1最好，N是CPU核数
 4、核心线程数，看应用，如果是任务，一天跑一次，设置为0，合适，因为跑完就停掉了，如果是常用线程池，看任务量，是保留一个核心还是几个核心线程数
 5、如果要获取任务执行结果，用CompletionService，但是注意，获取任务的结果的要重新开一个线程获取，如果在主线程获取，就要等任务都提交后才获取，就会阻塞大量任务结果，队列过大OOM，所以最好异步开个线程获取结果
 */
public class ThreadPoolDemo5 {

    public static void main(String[] args) {

        /**
         * corePoolSize
         指的就是线程池的核心线程数。当当前线程池中的线程个数小于corePoolSize时，对新来的任务，直接开启一个新的线程去执行它。

         maximumPoolSize
         代表最大能够容纳的线程数量。当线程池中的线程个数大于等于corePoolSize后，当需要执行一个新的任务时会先把任务放入缓存队列中，
         等待后续空闲的线程去执行。如果此时缓存队列已满，那么就会新启一个线程去执行它，如果线程数量已经超过了maximumPoolSize，
         那么就会调用reject方法，拒绝执行该次任务（后边会分析reject方法）。

         keepAliveTime
         用于指定线程存活的时间，当线程池中的线程大于corePoolSize后，会监控每一个线程的空闲时间，如果某个线程的空闲时间大于keepAliveTime，
         那么就会销毁该线程，释放资源。

         unit
         这个是keepAliveTime的单位，可以为秒、毫秒等等。

         workQueue
         这个就是我们的任务缓存队列了。是一个阻塞队列的类型，常用的有ArrayBlockingQueue、LinkedBlockingQueue
         （默认容量是Integer.MAX_VALUE）和SynchronousQueue。

         threadFactory
         这个就是创建线程的工厂类。用于新建线程实体。

         handler
         这是拒绝某个任务的回调。当线程池不能够处理某个任务时，会通过调用handler.rejectedExecution()去处理。内置了四种策略
         AbortPolicy（默认情况）：直接丢弃，并且抛出RejectedExecutionException异常。
         DiscardPolicy：直接丢弃，不做任何处理。
         DiscardOldestPolicy：从缓存队列丢弃最老的任务，然后调用execute立刻执行该任务。
         CallerRunsPolicy：在调用者的当前线程去执行这个任务。
         */
        ThreadPoolExecutor pool = new ThreadPoolExecutor(5, 5, 100, TimeUnit.MINUTES, new ArrayBlockingQueue<>(5));

        //使用直接丢弃任务本身的拒绝策略：DiscardPolicy
        pool.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());

        for (int i = 0; i < 10; i++) {
            final int tmp=i;
            pool.execute(()->{
                System.out.println("tmp:"+tmp);
            });
        }
        pool.shutdown();


    }
}

/**
 * 线程池规则
 线程池的线程执行规则跟任务队列有很大的关系。

 下面都假设任务队列没有大小限制：

 如果线程数量<=核心线程数量，那么直接启动一个核心线程来执行任务，不会放入队列中。
 如果线程数量>核心线程数，但<=最大线程数，并且任务队列是LinkedBlockingDeque的时候，超过核心线程数量的任务会放在任务队列中排队。
 如果线程数量>核心线程数，但<=最大线程数，并且任务队列是SynchronousQueue的时候，线程池会创建新线程执行任务，这些任务也不会被放在任务队列中。这些线程属于非核心线程，在任务完成后，闲置时间达到了超时时间就会被清除。
 如果线程数量>核心线程数，并且>最大线程数，当任务队列是LinkedBlockingDeque，会将超过核心线程的任务放在任务队列中排队。也就是当任务队列是LinkedBlockingDeque并且没有大小限制时，线程池的最大线程数设置是无效的，他的线程数最多不会超过核心线程数。
 如果线程数量>核心线程数，并且>最大线程数，当任务队列是SynchronousQueue的时候，会因为线程池拒绝添加任务而抛出异常。
 任务队列大小有限时

 当LinkedBlockingDeque塞满时，新增的任务会直接创建新线程来执行，当创建的线程数量超过最大线程数量时会抛异常。
 SynchronousQueue没有数量限制。因为他根本不保持这些任务，而是直接交给线程池去执行。当任务数量超过最大线程数时会直接抛异常。

 */
