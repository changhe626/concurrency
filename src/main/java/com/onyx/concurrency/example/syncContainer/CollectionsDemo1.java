package com.onyx.concurrency.example.syncContainer;

import com.google.common.collect.Lists;
import com.onyx.concurrency.annotaions.ThreadSafe;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;


/**
 * 在并发的时候优先使用并发容器.而不是同步容器!!!
 *
 * 同步容器
 *
 * Vector比Collections.synchronizedList快一点
 *
 * SynchronizedList<E>类使用了委托(delegation)，实质上存储还是使用了构造时传进来的list，只是将list作为底层存储，对它做了一层包装。
 * 正是因为多了一层封装，所以就会比直接操作数据的Vector慢那么一点点。
 *
 从上面的代码我们也可以看出来，SynchronizedList的同步，使用的是synchronized代码块对mutex对象加锁，这个mutex对象还能够通过构造函数传进来，
 也就是说我们可以指定锁定的对象。而Vector则使用了synchronized方法，同步方法的作用范围是整个方法，所以没办法对同步进行细粒度的控制。
 而且同步方法加锁的是this对象，没办法控制锁定的对象。这也是vector和SynchronizedList的一个区别。

 *
 * synchronizedList在迭代的时候，需要开发者自己加上线程锁控制代码，因为在整个迭代的过程中如果在循环外面不加同步代码，
 * 在一次次迭代之间，其他线程对于这个容器的add或者remove会影响整个迭代的预期效果，所以这里需要用户在整个循环外面加上synchronized(list);
 */
@ThreadSafe
public class CollectionsDemo1 {

    private static List<Integer> list = Collections.synchronizedList(Lists.newArrayList());

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
        System.out.println(list.size());
    }

    private static void update(int tmp){
        list.add(tmp);
    }


}
