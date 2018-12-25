package com.onyx.concurrency.example.blockingQueue;

import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author zk
 * @Description: 不阻塞队列
阻塞，顾名思义：当我们的生产者向队列中生产数据时，若队列已满，那么生产线程会暂停下来，直到队列中有可以存放数据的地方，才会继续工作；
而当我们的消费者向队列中获取数据时，若队列为空，则消费者线程会暂停下来，直到容器中有元素出现，才能进行获取操作。

入队方法：
add()：底层调用offer();
offer()：Queue接口继承下来的方法，实现队列的入队操作，不会阻碍线程的执行，插入成功返回true；


出队方法：
poll()：移动头结点指针，返回头结点元素，并将头结点元素出队；队列为空，则返回null；
peek()：移动头结点指针，返回头结点元素，并不会将头结点元素出队；队列为空，则返回null；


http://ifeve.com/%E5%B9%B6%E5%8F%91%E9%98%9F%E5%88%97-%E6%97%A0%E7%95%8C%E9%9D%9E%E9%98%BB%E5%A1%9E%E9%98%9F%E5%88%97concurrentlinkedqueue%E5%8E%9F%E7%90%86%E6%8E%A2%E7%A9%B6/
 * @date 2018-12-25 9:44
 */
public class ConcurrentLinkedQueueDemo {

    public static void main(String[] args) {
        /**
         * ConcurrentLinkedQueue是一个线程安全的队列，基于链表结构实现，是一个无界队列，理论上来说队列的长度可以无限扩大。
         * 先进先出（FIFO）入队规则
         * 如果涉及到队列是否为空的判断，切记不可使用size()==0的做法，因为在size()方法中，是通过遍历整个链表来实现的，
         * 在队列元素很多的时候，size()方法十分消耗性能和时间，只是单纯的判断队列为空使用isEmpty()即可
         */
        /*ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue();
        queue.add("1");
        queue.add("1");
        queue.add("2");
        System.out.println(queue.size());
        System.out.println(queue.poll());
        System.out.println(queue.peek()+"~~"+queue.size());*/



        ExecutorService executorService = Executors.newFixedThreadPool(4);
        for(int x=0;x<1000;x++){
            executorService.submit(new Offer());
            executorService.submit(new Poll());
        }
        executorService.shutdown();


    }

    public static ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<String>();


    static class Offer implements Runnable {
        @Override
        public void run() {
            if(queue.size()==0){
                String ele = new Random().nextInt(Integer.MAX_VALUE)+"";
                queue.offer(ele);
                System.out.println("入队元素为"+ele);
            }
        }
    }

    static class Poll implements Runnable {
        @Override
        public void run() {
            if(!queue.isEmpty()){
                String ele = queue.poll();
                System.out.println("出队元素为"+ele);
            }
        }
    }


}
