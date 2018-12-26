package com.onyx.concurrency.example.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zk
 * @Description:
 * @date 2018-12-26 10:44
 */
public class ConditionDemo2 {

    final Lock lock = new ReentrantLock();//锁对象
    final Condition notFull = lock.newCondition();//写线程条件 
    final Condition notEmpty = lock.newCondition();//读线程条件 

    final Object[] items = new Object[100];//缓存队列
    int putptr/*写索引*/, takeptr/*读索引*/, count/*队列中存在的数据个数*/;

    public void put(Object x) throws InterruptedException {
        lock.lock();
        try {
            while (count == items.length) {
                //如果队列满了 
                notFull.await();//阻塞写线程
            }
            items[putptr] = x;//赋值 
            if (++putptr == items.length) {
                putptr = 0;//如果写索引写到队列的最后一个位置了，那么置为0
            }
            ++count;//个数++
            notEmpty.signal();//唤醒读线程
        } finally {
            lock.unlock();
        }
    }

    public Object take() throws InterruptedException {
        lock.lock();
        try {
            while (count == 0) {
                //如果队列为空
                notEmpty.await();//阻塞读线程
            }
            Object x = items[takeptr];//取值 
            if (++takeptr == items.length) {
                takeptr = 0;//如果读索引读到队列的最后一个位置了，那么置为0
            }
            --count;//个数--
            notFull.signal();//唤醒写线程
            return x;
        } finally {
            lock.unlock();
        }
    }


}
/**
 * 这是一个处于多线程工作环境下的缓存区，缓存区提供了两个方法，put和take，put是存数据，take是取数据，内部有个缓存队列，具体变量和方法说明见代码，
 * 这个缓存区类实现的功能：有多个线程往里面存数据和从里面取数据，其缓存队列(先进先出后进后出)能缓存的最大数值是100，多个线程间是互斥的，
 * 当缓存队列中存储的值达到100时，将写线程阻塞，并唤醒读线程，当缓存队列中存储的值为0时，将读线程阻塞，并唤醒写线程，这也是
 * ArrayBlockingQueue的内部实现。下面分析一下代码的执行过程：
         1. 一个写线程执行，调用put方法；
         2. 判断count是否为100，显然没有100；
         3. 继续执行，存入值；
         4. 判断当前写入的索引位置++后，是否和100相等，相等将写入索引值变为0，并将count+1；
         5. 仅唤醒读线程阻塞队列中的一个；
         6. 一个读线程执行，调用take方法；
         7. ……
         8. 仅唤醒写线程阻塞队列中的一个。

这就是多个Condition的强大之处，假设缓存队列中已经存满，那么阻塞的肯定是写线程，唤醒的肯定是读线程，相反，阻塞的肯定是读线程，
 唤醒的肯定是写线程，那么假设只有一个Condition会有什么效果呢，缓存队列中已经存满，这个Lock不知道唤醒的是读线程还是写线程了，
 如果唤醒的是读线程，皆大欢喜，如果唤醒的是写线程，那么线程刚被唤醒，又被阻塞了，这时又去唤醒，这样就浪费了很多时间。
 */
