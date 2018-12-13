package com.onyx.concurrency.example.sync;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 子类继承父类中含有synchronized 的方法,是不能使用到synchronized的效果的,需要他自己显示的设置
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
            demo2.test2(1);
        });
        service.execute(()->{
            demo1.test2(2);
        });

       /* service.execute(()->{
            demo2.test2();
        });
        service.execute(()->{
            demo1.test2();
        });*/

    }

    //修饰代码块.
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
