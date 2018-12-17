package com.onyx.concurrency.example.blockingQueue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 底层采用数组来实现。
 其并发控制采用可重入锁来控制，不管是插入操作还是读取操作，都需要获取到锁才能进行操作
 *
 * 实现并发同步的原理就是，读操作和写操作都需要获取到 AQS 独占锁才能进行操作。如果队列为空，这个时候读操作的线程进入到读线程队列排队，
 * 等待写线程写入新的元素，然后唤醒读线程队列的第一个等待线程。如果队列已满，这个时候写操作的线程进入到写线程队列排队，
 * 等待读线程将队列元素移除腾出空间，然后唤醒写线程队列的第一个等待线程。
 *
 * 构造的时候指定以下三个参数：

 队列容量，其限制了队列中最多允许的元素个数；
 指定独占锁是公平锁还是非公平锁。非公平锁的吞吐量比较高，公平锁可以保证每次都是等待最久的线程获取到锁；
 可以指定用一个集合来初始化，将此集合中的元素在构造方法期间就先添加到队列中。
 */
public class ArrayBlockingQueueDemo {

    public static void main(String[] args) {
        /*ArrayBlockingQueue<Object> queue = new ArrayBlockingQueue<>(2);
        queue.add(1);
        //容量不够直接抛出异常
        queue.add(1);*/

        final BlockingQueue queue = new ArrayBlockingQueue(3);
        for(int i=0;i<2;i++){
            new Thread(()->{
                while(true){
                    try {
                        Thread.sleep((long)(Math.random()*1000));
                        System.out.println(Thread.currentThread().getName() + "准备放数据!");
                        queue.put(1);
                        System.out.println(Thread.currentThread().getName() + "已经放了数据，" +
                                "队列目前有" + queue.size() + "个数据");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }).start();
        }

        new Thread(()->{
            while(true){
                try {
                    //将此处的睡眠时间分别改为100和1000，观察运行结果
                    Thread.sleep(1000);
                    System.out.println(Thread.currentThread().getName() + "准备取数据!");
                    queue.take();
                    System.out.println(Thread.currentThread().getName() + "已经取走数据，" +
                            "队列目前有" + queue.size() + "个数据");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

}
