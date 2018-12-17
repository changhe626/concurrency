package com.onyx.concurrency.example.commonunsafe;

import com.onyx.concurrency.annotaions.NotThreadSafe;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;


/**
 * 如果让ArrayList变成线程安全的？
 List list1 = Collections.synchronizedList(new ArrayList());

 或者用List list1 = new Vector();
 */
@NotThreadSafe
public class ArrayListDemo1 {

    private static ArrayList<Integer> list=new ArrayList();

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

/**
 * 我们先来看看 ArrayList 的 add 操作源码。
 public boolean add(E e) {
 ensureCapacityInternal(size + 1);
 elementData[size++] = e;
 return true;
 }

 ArrayList 的不安全主要体现在两个方面。

 其一：
 elementData[size++] = e;

 不是一个原子操作，是分两步执行的。
 elementData[size] = e;
 size++;

 单线程执行这段代码完全没问题，可是到多线程环境下可能就有问题了。可能一个线程会覆盖另一个线程的值。

 列表为空 size = 0。
 线程 A 执行完 elementData[size] = e;之后挂起。A 把 "a" 放在了下标为 0 的位置。此时 size = 0。
 线程 B 执行 elementData[size] = e; 因为此时 size = 0，所以 B 把 "b" 放在了下标为 0 的位置，于是刚好把 A 的数据给覆盖掉了。
 线程 B 将 size 的值增加为 1。
 线程 A 将 size 的值增加为 2。

 这样子，当线程 A 和线程 B 都执行完之后理想情况下应该是 "a" 在下标为 0 的位置，"b" 在标为 1 的位置。而实际情况确是下标为 0 的位置为 "b"，下标为 1 的位置啥也没有。

 其二：
 ArrayList 默认数组大小为 10。假设现在已经添加进去 9 个元素了，size = 9。

 线程 A 执行完 add 函数中的ensureCapacityInternal(size + 1)挂起了。
 线程 B 开始执行，校验数组容量发现不需要扩容。于是把 "b" 放在了下标为 9 的位置，且 size 自增 1。此时 size = 10。
 线程 A 接着执行，尝试把 "a" 放在下标为 10 的位置，因为 size = 10。但因为数组还没有扩容，最大的下标才为 9，所以会抛出数组越界异常 ArrayIndexOutOfBoundsException


 还有就是List扩容后的,把新的数组的地址给原来替换原来数组的地址,也不是线程安全的.
 */
