package com.onyx.concurrency.example.atomic;

import com.onyx.concurrency.annotaions.ThreadSafe;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;


/**
 * 其实这个类的实现和其他的Atomic类的实现差不多，都是一个cas 的原理，只不过在这基础上用了反射来控制对象的字段。
 * AtomicIntegerfieldupdater  实现了普通变量的原子操作 ，加法减都可以
 */
@ThreadSafe
public class ConcurrencyTest5 {


   private static AtomicIntegerFieldUpdater<ConcurrencyTest5> updater=
           AtomicIntegerFieldUpdater.newUpdater(ConcurrencyTest5.class,"count");

    public int getCount() {
        return count;
    }

    /**
     * 必须是volatile 且不能是static,不能是final
     * 对于AtomicIntegerFieldUpdater和AtomicLongFieldUpdater只能修改int/long类型的字段，不能修改其包装类型（Integer/Long）。
     * 如果要修改包装类型就需要使用AtomicReferenceFieldUpdater
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
