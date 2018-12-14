package com.onyx.concurrency.example.sync;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *https://blog.csdn.net/luoweifu/article/details/46613015
 * synchronized关键字不能继承。
 对于父类中的 synchronized 修饰方法，子类在覆盖该方法时，默认情况下不是同步的，必须显示的使用 synchronized 关键字修饰才行。

 在定义接口方法时不能使用synchronized关键字。

 构造方法不能使用synchronized关键字，但可以使用synchronized代码块来进行同步。

 *
 * synchronized:不可中断锁,适合竞争不激烈,可读性好
 *
 * Lock 可中断锁,多样化同步,竞争激烈时能维持常态
 *
 * Atomic:竞争激烈时能维持常态,比Lock性能好,只能同步一个值
 *
 */
public class SynchronizedDemo1 {

    public static void main(String[] args) {
        SynchronizedDemo1 demo1 = new SynchronizedDemo1();
        SynchronizedDemo1 demo2 = new SynchronizedDemo1();
        ExecutorService service = Executors.newCachedThreadPool();

        service.execute(()->{
            demo2.test1(1);
        });
        service.execute(()->{
            demo1.test1(2);
        });

       /* service.execute(()->{
            demo2.test2();
        });
        service.execute(()->{
            demo1.test2();
        });*/

    }

    /**
     * 修饰代码块.
     * 当两个并发线程(thread1和thread2)访问同一个对象(syncThread)中的synchronized代码块时，在同一时刻只能有一个线程得到执行，
     * 另一个线程受阻塞，必须等待当前线程执行完这个代码块以后才能执行该代码块。Thread1和thread2是互斥的，
     * 因为在执行synchronized代码块时会锁定当前的对象，只有执行完该代码块才能释放该对象锁，下一个线程才能执行并锁定该对象。
     */
    public void test1(int j){
        synchronized (this){
            for (int i = 0; i < 10; i++) {
                System.out.println("test1~~"+j+" i="+i);
            }
        }
    }


    /**
     * 修饰方法
     */
    public synchronized void test2(int j){
        for (int i = 0; i < 10; i++) {
            System.out.println("test2~~"+j+" i="+i);
        }
    }

}
