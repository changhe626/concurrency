package com.onyx.concurrency.example.count;

/**
 * volatile 适合做这种旗帜变量...
 */
public class VolatileDemo {


    private static volatile boolean inited=false;

    public static void main(String[] args) {

        //线程1
        new Thread(()->{
            inited=true;
        }).start();

        //线程2
        new Thread(()->{
            while (!inited) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }




}
