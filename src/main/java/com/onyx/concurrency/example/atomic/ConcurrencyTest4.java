package com.onyx.concurrency.example.atomic;

import com.onyx.concurrency.annotaions.ThreadSafe;

import java.util.concurrent.atomic.AtomicReference;


@ThreadSafe
public class ConcurrencyTest4 {


    private static AtomicReference<Integer> count=new AtomicReference<>(0);

    public static void main(String[] args) {
        count.compareAndSet(0,2);
        count.compareAndSet(0,1);
        count.compareAndSet(1,3);
        count.compareAndSet(2,4);
        count.compareAndSet(3,5);
        System.out.println(count.get());
    }


}
