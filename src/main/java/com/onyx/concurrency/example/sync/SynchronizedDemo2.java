package com.onyx.concurrency.example.sync;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class SynchronizedDemo2 {

    public static void main(String[] args) {
        SynchronizedDemo2 demo1 = new SynchronizedDemo2();
        SynchronizedDemo2 demo2 = new SynchronizedDemo2();
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

    //修饰代码块.
    public void test1(int j){
        synchronized (SynchronizedDemo2.class){
            for (int i = 0; i < 10; i++) {
                System.out.println("test1~~"+j+" i="+i);
            }
        }
    }


    /**
     * 修饰方法
     */
    public synchronized static  void test2(int j){
        for (int i = 0; i < 10; i++) {
            System.out.println("test2~~"+j+" i="+i);
        }
    }

}
