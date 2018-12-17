package com.onyx.concurrency.example.commonunsafe;

import com.onyx.concurrency.annotaions.NotThreadSafe;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 *
 void addEntry(int hash, K key, V value, int bucketIndex) {
 Entry<K,V> e = table[bucketIndex];
 table[bucketIndex] = new Entry<K,V>(hash, key, value, e);
 if (size++ >= threshold)
 resize(2 * table.length);
 }


 在hashmap做put操作的时候会调用到以上的方法。现在假如A线程和B线程同时对同一个数组位置调用addEntry，两个线程会同时得到现在的头结点，
 然后A写入新的头结点之后，B也写入新的头结点，那B的写入操作就会覆盖A的写入操作造成A的写入操作丢失


 当多个线程同时检测到总数量超过门限值的时候就会同时调用resize操作，各自生成新的数组并rehash后赋给该map底层的数组table，
 结果最终只有最后一个线程生成的新数组被赋给table变量，其他线程的均会丢失。而且当某些线程已经完成赋值而其他线程刚开始的时候，
 就会用已经被赋值的table作为原始数组，这样也会有问题。


 https://mp.weixin.qq.com/s/i_r1aLlQR8qTz7kz8vKk6w?

 http://www.importnew.com/22011.html

 */
@NotThreadSafe
public class HashMapDemo1 {

    private static HashMap<Integer,Integer> map =new HashMap();

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
        System.out.println(map.size());
    }

    private static void update(int tmp){
        map.put(tmp,tmp);
    }


}
