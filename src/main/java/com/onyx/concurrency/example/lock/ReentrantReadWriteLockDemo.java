package com.onyx.concurrency.example.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 使用读/写锁同步数据访问

 锁所提供的最重要的改进之一就是ReadWriteLock接口和唯一 一个实现它的ReentrantReadWriteLock类。这个类提供两把锁，
 一把用于读操作和一把用于写操作。同时可以有多个线程执行读操作，但只有一个线程可以执行写操作。当一个线程正在执行一个写操作，不可能有任何线程执行读操作。

 在这个指南中，你将会学习如何使用ReadWriteLock接口实现一个程序，使用它来控制访问一个存储两个产品价格的对象。
 *
 *
 正如我们前面提及到的，ReentrantReadWriteLock类有两把锁，一把用于读操作，一把用于写操作。用于读操作的锁，
 是通过在 ReadWriteLock接口中声明的readLock()方法获取的。这个锁是实现Lock接口的一个对象，所以我们可以使用lock()， unlock() 和tryLock()方法。
 用于写操作的锁，是通过在ReadWriteLock接口中声明的writeLock()方法获取的。这个锁是实现Lock接 口的一个对象，所以我们可以使用lock()，
 unlock() 和tryLock()方法。确保正确的使用这些锁，使用它们与被设计的目的是一样的，这是程序猿的职责。
 当你获得Lock接口的读锁时，不能修改这个变量的值。否则，你可能会有数据不一致的错误。
 */
public class ReentrantReadWriteLockDemo {

    public static void main(String[] args) {
        PricesInfo pricesInfo=new PricesInfo();
        Reader readers[]=new Reader[5];
        Thread threadsReader[]=new Thread[5];
        for (int i = 0; i < 5; i++) {
            readers[i]=new Reader(pricesInfo);
            threadsReader[i]=new Thread(readers[i]);
        }
        Writer writer=new Writer(pricesInfo);
        Thread threadWriter=new Thread(writer);

        for (int i = 0; i < 5; i++) {
            threadsReader[i].start();
        }
        threadWriter.start();

    }

}

class PricesInfo {
    private double price1;
    private double price2;

    private ReadWriteLock lock;

    public PricesInfo() {
        price1=1.0;
        price2=2.0;
        lock=new ReentrantReadWriteLock();
    }

    //它使用读锁来控制这个属性值的访问
    public double getPrice1() {
        lock.readLock().lock();
        double value=price1;
        lock.readLock().unlock();
        return value;
    }

    //它使用读锁来控制这个属性值的访问
    public double getPrice2() {
        lock.readLock().lock();
        double value=price2;
        lock.readLock().unlock();
        return value;
    }

    //使用写锁来控制对它们的访问
    public void setPrices(double price1,double price2) {
        lock.writeLock().lock();
        this.price1 = price1;
        this.price2 = price2;
        lock.writeLock().unlock();
    }

}

class Reader implements Runnable{
    private PricesInfo pricesInfo;

    public Reader(PricesInfo pricesInfo) {
        this.pricesInfo = pricesInfo;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.printf("%s: Price 1: %f\n", Thread.
                    currentThread().getName(),pricesInfo.getPrice1());
            System.out.printf("%s: Price 2: %f\n", Thread.
                    currentThread().getName(),pricesInfo.getPrice2());
        }
    }
}

class Writer implements Runnable{
    private PricesInfo pricesInfo;

    public Writer(PricesInfo pricesInfo) {
        this.pricesInfo = pricesInfo;
    }

    @Override
    public void run() {
        for (int i = 0; i < 3; i++) {
            System.out.printf("Writer: Attempt to modify the prices.\n");
            pricesInfo.setPrices(Math.random()*10, Math.random()*8);
            System.out.printf("Writer: Prices have been modified.\n");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
