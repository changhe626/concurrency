package com.onyx.concurrency.example.lock;

import java.util.concurrent.locks.LockSupport;

/**
 * @author zk
 * @Description:LockSupport是用来创建锁和其他同步类的基本线程阻塞原语。
 * LockSupport中的park() 和 unpark() 的作用分别是阻塞线程和解除阻塞线程，而且park()和unpark()不会遇到“Thread.suspend 和 Thread.resume所可能引发的死锁”问题。
 * 因为park() 和 unpark()有许可的存在；调用 park() 的线程和另一个试图将其 unpark() 的线程之间的竞争将保持活性。
 *
 *
 * 需要特别注意的一点：park 方法还可以在其他任何时间“毫无理由”地返回，因此通常必须在重新检查返回条件的循环里调用此方法。从这个意义上说，
 * park 是“忙碌等待”的一种优化，它不会浪费这么多的时间进行自旋，但是必须将它与 unpark 配对使用才更高效。
 *
 * LockSupport是不可重入的，如果一个线程连续2次调用LockSupport.park()，那么该线程一定会一直阻塞下去。
 * @date 2018-12-25 15:33
 */
public class LockSupportDemo1 {

    private static Thread mainThread;

    public static void main(String[] args) {

        MyThread ta = new MyThread("ta");
        // 获取主线程
        mainThread = Thread.currentThread();

        System.out.println(Thread.currentThread().getName() + " start ta");
        ta.start();

        System.out.println(Thread.currentThread().getName() + " block");
        // 主线程阻塞
        LockSupport.park(mainThread);

        System.out.println(Thread.currentThread().getName() + " continue");
    }

    static class MyThread extends Thread {

        public MyThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " wakup others");
            // 唤醒“主线程”
            LockSupport.unpark(mainThread);
        }
    }

}
