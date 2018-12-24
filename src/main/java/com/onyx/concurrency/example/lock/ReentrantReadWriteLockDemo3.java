package com.onyx.concurrency.example.lock;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author zk
 * @Description: 使用读写锁模拟一个缓存器
 * @date 2018-12-24 17:32
 */
public class ReentrantReadWriteLockDemo3 {
    private Map<String, Object> map = new HashMap<String, Object>();//缓存器
    private ReadWriteLock lock = new ReentrantReadWriteLock();
    private Lock writeLock = this.lock.writeLock();
    private Lock readLock = this.lock.readLock();


    public static void main(String[] args) {
        ReentrantReadWriteLockDemo3 demo3 = new ReentrantReadWriteLockDemo3();
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                System.out.println(demo3.get("haha"));
            }).start();
        }

    }

    public Object get(String id) {
        Object value = null;
        //首先开启读锁，从缓存中去取
        readLock.lock();
        try {
            value = map.get(id);
            //如果缓存中没有释放读锁，上写锁
            if (value == null) {
                readLock.unlock();
                writeLock.lock();
                try {
                    if (value == null) {
                        value = "aaa";  //此时可以去数据库中查找，这里简单的模拟一下
                    }
                } finally {
                    //释放写锁
                    writeLock.unlock();
                }
                //然后再上读锁
                readLock.lock();
            }
        } finally {
            //最后释放读锁
            readLock.unlock();
        }
        return value;
    }
}
