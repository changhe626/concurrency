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



 要想合理的配置线程池，就必须首先分析任务特性，可以从以下几个角度来进行分析：

 任务的性质：CPU 密集型任务，IO 密集型任务和混合型任务。
 任务的优先级：高，中和低。
 任务的执行时间：长，中和短。
 任务的依赖性：是否依赖其他系统资源，如数据库连接。

 任务性质不同的任务可以用不同规模的线程池分开处理。CPU 密集型任务配置尽可能小的线程，如配置 Ncpu+1 个线程的线程池。IO 密集型任务则由于线程并不是一
 直在执行任务，则配置尽可能多的线程，如 2*Ncpu。混合型的任务，如果可以拆分，则将其拆分成一个 CPU 密集型任务和一个 IO 密集型任务，只要这两个任务
 执行的时间相差不是太大，那么分解后执行的吞吐率要高于串行执行的吞吐率，如果这两个任务执行时间相差太大，则没必要进行分解。我们可以通过
 Runtime.getRuntime().availableProcessors() 方法获得当前设备的 CPU 个数。

 优先级不同的任务可以使用优先级队列 PriorityBlockingQueue 来处理。它可以让优先级高的任务先得到执行，需要注意的是如果一直有优先级高的任务提交到
 队列里，那么优先级低的任务可能永远不能执行。

 执行时间不同的任务可以交给不同规模的线程池来处理，或者也可以使用优先级队列，让执行时间短的任务先执行。

 依赖数据库连接池的任务，因为线程提交 SQL 后需要等待数据库返回结果，如果等待的时间越长 CPU 空闲时间就越长，那么线程数应该设置越大，
 这样才能更好的利用 CPU。

 建议使用有界队列，有界队列能增加系统的稳定性和预警能力，可以根据需要设大一点，比如几千。有一次我们组使用的后台任务线程池的队列和线程
 池全满了，不断的抛出抛弃任务的异常，通过排查发现是数据库出现了问题，导致执行 SQL 变得非常缓慢，因为后台任务线程池里的任务全是需要向数
 据库查询和插入数据的，所以导致线程池里的工作线程全部阻塞住，任务积压在线程池里。如果当时我们设置成无界队列，线程池的队列就会越
 来越多，有可能会撑满内存，导致整个系统不可用，而不只是后台任务出现问题。当然我们的系统所有的任务是用的单独的服务器部署的，而我们使用不
 同规模的线程池跑不同类型的任务，但是出现这样问题时也会影响到其他任务。

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


 这里workQueue主要有三种类型：ArrayBlockingQueue、LinkedBlockingQueue、SynchronousQueue，
 第一个是有界阻塞队列，第二个是无界阻塞队列，当然也可以为其指定界限大小，第三个是同步队列，
 对于ArrayBlockingQueue，其是需要指定队列大小的，当队列存满了任务线程池就会创建新的线程执行任务，
 对于LinkedBlockingQueue，如果其指定界限，那么和ArrayBlockingQueue区别不大，如果其不指定界限，那么其理论上是可以存储无限量的任务的，
 实际上能够存储Integer.MAX_VALUE个任务（还是相当于可以存储无限量的任务），此时由于LinkedBlockingQueue是永远无法存满任务的，因而maxPoolSize的
 设定将没有意义，一般其会设定为和corePoolSize相同的值，
 对于SynchronousQueue，其内部是没有任何结构存储任务的，当一个任务添加到该队列时，当前线程和后续添加任务的线程都会被阻塞，直至有一个线程从该队列中
 取出任务，当前线程才会被释放，因而如果线程池使用了该队列，那么一般corePoolSize都会设计得比较小，maxPoolSize会设计得比较大，因为该队列
 比较适合大量并且执行时间较短的任务的执行；


 DiscardPolicy和DiscardOldestPolicy一般不会配合SynchronousQueue使用，因为当同步队列阻塞了任务时，该任务都会被抛弃；对于AbortPolicy，
 因为如果队列已满，那么其会抛出异常，因而使用时需要小心；对于CallerRunsPolicy，由于当有新的任务到达时会使用调用线程执行当前任务，
 因而使用时需要考虑其对服务器响应的影响，并且还需要注意的是，相对于其他几个策略，该策略不会抛弃任务到达的任务，
 因为如果到达的任务使队列满了而只能使用调用线程执行任务时，说明线程池设计得不够合理，如果任其发展，那么所有的调用线程都可能会
 被需要执行的任务所阻塞，导致服务器出现问题


 如何设置参数:
 默认值
 corePoolSize=1
 queueCapacity=Integer.MAX_VALUE
 maxPoolSize=Integer.MAX_VALUE
 keepAliveTime=60s
 allowCoreThreadTimeout=false
 rejectedExecutionHandler=AbortPolicy()

 如何来设置
 需要根据几个值来决定:
 tasks ：每秒的任务数，假设为500~1000
 taskcost：每个任务花费时间，假设为0.1s
 responsetime：系统允许容忍的最大响应时间，假设为1s

 做几个计算
 corePoolSize = 每秒需要多少个线程处理？
 threadcount = tasks/(1/taskcost) =tasks*taskcout =  (500~1000)*0.1 = 50~100 个线程。corePoolSize设置应该大于50
 根据8020原则，如果80%的每秒任务数小于800，那么corePoolSize设置为80即可
 queueCapacity = (coreSizePool/taskcost)*responsetime
 计算可得 queueCapacity = 80/0.1*1 = 80。意思是队列里的线程可以等待1s，超过了的需要新开线程来执行
 切记不能设置为Integer.MAX_VALUE，这样队列会很大，线程数只会保持在corePoolSize大小，当任务陡增时，不能新开线程来执行，响应时间会随之陡增。
 maxPoolSize = (max(tasks)- queueCapacity)/(1/taskcost)
 计算可得 maxPoolSize = (1000-80)/10 = 92
 （最大任务数-队列容量）/每个线程每秒处理能力 = 最大线程数
 rejectedExecutionHandler：根据具体情况来决定，任务不重要可丢弃，任务重要则要利用一些缓冲机制来处理
 keepAliveTime和allowCoreThreadTimeout采用默认通常能满足
 以上都是理想值，实际情况下要根据机器性能来决定。如果在未达到最大线程数的情况机器cpu load已经满了，则需要通过升级硬件（呵呵）和优化代码，降低taskcost来处理。

 */
