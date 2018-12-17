package com.onyx.concurrency.example.concurrent;

import com.onyx.concurrency.annotaions.ThreadSafe;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * 它主要具有一下特性:

 所有元素都存储在数组里面, 只有当数组进行 remove, update时才在方法上加上 ReentrantLock , 拷贝一份 snapshot 的数组,
 只改变 snapshot 中的元素, 最后再赋值到 CopyOnWriteArrayList 中
 所有的 get方法只是获取数组对应下标上的元素(无需加锁控制)

 从上面两个特性我们也知道: CopyOnWriteArrayList 是使用空间换时间的方式进行工作, 它主要适用于 读多些少,
 并且数据内容变化比较少的场景(最好初始化时就进行加载数据到CopyOnWriteArrayList 中)

 */
@ThreadSafe
public class CopyOnWriteArrayListDemo1 {

    private static List<Integer> list=new CopyOnWriteArrayList<>();

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
