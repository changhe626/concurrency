package com.onyx.concurrency.example.atomic;

import com.onyx.concurrency.annotaions.ThreadSafe;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;


@ThreadSafe
public class ConcurrencyTest5 {


   private static AtomicIntegerFieldUpdater<ConcurrencyTest5> updater=
           AtomicIntegerFieldUpdater.newUpdater(ConcurrencyTest5.class,"count");

    public int getCount() {
        return count;
    }

    /**
     * 必须是volatile 且不能使static
     */
    public volatile int count=100;


    public static void main(String[] args) {
        ConcurrencyTest5 test=new ConcurrencyTest5();

        if(updater.compareAndSet(test,100,120)){
            System.out.println("1--count:"+test.getCount());
        }
        if(updater.compareAndSet(test,100,120)){
            System.out.println("2--count:"+test.getCount());
        }else {
            System.out.println("now is :"+test.getCount());
        }
    }


}
