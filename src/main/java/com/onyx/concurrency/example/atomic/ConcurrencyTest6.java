package com.onyx.concurrency.example.atomic;


import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * AtomicStampedReference  解决其中并发中的ABA问题,通过版本号进行维护.
 * 由于ABA问题带来的隐患，各种乐观锁的实现中通常都会用版本戳version来对记录或对象标记，避免并发操作带来的问题，
 * 在Java中，AtomicStampedReference<E>也实现了这个作用，它通过包装[E,Integer]的元组来对对象标记版本戳stamp，从而避免ABA问题，
 * 例如下面的代码分别用AtomicInteger和AtomicStampedReference来对初始值为100的原子整型变量进行更新，AtomicInteger会成功执行CAS操作，
 * 而加上版本戳的AtomicStampedReference对于ABA问题会执行CAS失败：
 *

 * AtomicLongArray   更新数组中的某一个值.
 * AtomicLongArray的作用则是对"长整形数组"进行原子操作
 */
public class ConcurrencyTest6 {


    public static void main(String[] args) {
        //abaProblem();
        //stampedRed();
        atomicArray();
    }

    private static void atomicArray(){
        long[] arrLong = new long[] {10, 20, 30, 40, 50};
        AtomicLongArray ala = new AtomicLongArray(arrLong);

        ala.set(0, 100);
        for (int i=0, len=ala.length(); i<len; i++){
            System.out.printf("get(%d) : %s\n", i, ala.get(i));
        }

        System.out.printf("%20s : %s\n", "getAndDecrement(0)", ala.getAndDecrement(0));
        System.out.printf("%20s : %s\n", "decrementAndGet(1)", ala.decrementAndGet(1));
        System.out.printf("%20s : %s\n", "getAndIncrement(2)", ala.getAndIncrement(2));
        System.out.printf("%20s : %s\n", "incrementAndGet(3)", ala.incrementAndGet(3));

        System.out.printf("%20s : %s\n", "addAndGet(100)", ala.addAndGet(0, 100));
        System.out.printf("%20s : %s\n", "getAndAdd(100)", ala.getAndAdd(1, 100));

        System.out.printf("%20s : %s\n", "compareAndSet()", ala.compareAndSet(2, 31, 1000));
        System.out.printf("%20s : %s\n", "get(2)", ala.get(2));

    }




    /**
     * ABA问题的解决
     */
    private static AtomicStampedReference<Integer> atomicStampedRef  = new AtomicStampedReference<>(1, 0);
    private static void stampedRed(){
        Thread main = new Thread(() -> {
            System.out.println("操作线程" + Thread.currentThread() +",初始值 a = " + atomicStampedRef.getReference());
            //获取当前标识别
            int stamp = atomicStampedRef.getStamp();
            try {
                Thread.sleep(1000); //等待1秒 ，以便让干扰线程执行
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //此时expectedReference未发生改变，但是stamp已经被修改了,所以CAS失败
            boolean isCASSuccess = atomicStampedRef.compareAndSet(1, 2, stamp, stamp + 1);
            System.out.println("操作线程" + Thread.currentThread() +",CAS操作结果: " + isCASSuccess +",stamp:"+atomicStampedRef.getStamp());
        }, "主操作线程");


        Thread other = new Thread(() -> {
            atomicStampedRef.compareAndSet(1,2,atomicStampedRef.getStamp(),atomicStampedRef.getStamp() +1);
            System.out.println("操作线程" + Thread.currentThread() +",【increment】 ,值 = "+ atomicStampedRef.getReference()+",stamp:"+atomicStampedRef.getStamp());
            atomicStampedRef.compareAndSet(2,1,atomicStampedRef.getStamp(),atomicStampedRef.getStamp() +1);
            System.out.println("操作线程" + Thread.currentThread() +",【decrement】 ,值 = "+ atomicStampedRef.getReference()+",stamp:"+atomicStampedRef.getStamp());
        },"干扰线程");

        main.start();
        other.start();
    }




    /**
     * ABA问题演示
     */
    private  static AtomicInteger a = new AtomicInteger(1);
    private static void abaProblem(){
        Thread main = new Thread(() -> {
            System.out.println("操作线程" + Thread.currentThread() +",初始值 = " + a);  //定义变量 a = 1
            try {
                Thread.sleep(1000);  //等待1秒 ，以便让干扰线程执行
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            boolean isCASSuccess = a.compareAndSet(1,2); // CAS操作
            System.out.println("操作线程" + Thread.currentThread() +",CAS操作结果: " + isCASSuccess);
        },"主操作线程");

        Thread other = new Thread(() -> {
            a.incrementAndGet(); // a 加 1, a + 1 = 1 + 1 = 2
            System.out.println("操作线程" + Thread.currentThread() +",【increment】 ,值 = "+ a);
            a.decrementAndGet(); // a 减 1, a - 1 = 2 - 1 = 1
            System.out.println("操作线程" + Thread.currentThread() +",【decrement】 ,值 = "+ a);
        },"干扰线程");

        main.start();
        other.start();
    }


}
