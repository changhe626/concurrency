package com.onyx.concurrency.example.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * http://www.importnew.com/9281.html
 * https://blog.csdn.net/bohu83/article/details/51098106
 */
public class ConditionDemo1 {
    public static void main(String[] args) {
        ReentrantLock reentrantLock = new ReentrantLock();
        Condition condition = reentrantLock.newCondition();

        /**
         * Condition的执行方式，是当在线程1中调用await方法后，线程1将释放锁，并且将自己沉睡，等待唤醒，

         线程2获取到锁后，开始做事，完毕后，调用Condition的signal方法，唤醒线程1，线程1恢复执行

         以上说明Condition是一个多线程间协调通信的工具类，使得某个，或者某些线程一起等待某个条件（Condition）,只有当该条件具备( signal
         或者 signalAll方法被带调用)时 ，这些等待线程才会被唤醒，从而重新争夺锁
         */
        new Thread(() -> {
            try {
                reentrantLock.lock();
                System.out.println("wait signal"); // 1
                condition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("get signal"); // 4
            reentrantLock.unlock();
        }).start();

        new Thread(() -> {
            reentrantLock.lock();
            System.out.println("get lock"); // 2
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            condition.signalAll();
            System.out.println("send signal ~ "); // 3
            reentrantLock.unlock();
        }).start();
    }
}
