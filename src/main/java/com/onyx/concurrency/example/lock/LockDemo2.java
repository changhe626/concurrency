package com.onyx.concurrency.example.lock;

import com.onyx.concurrency.annotaions.ThreadSafe;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


@ThreadSafe
public class LockDemo2 {

   private final static ReentrantReadWriteLock lock=new ReentrantReadWriteLock();

    /**
     * 悲观锁.分别获取读写锁
     */
   private final static Lock readLock=lock.readLock();
   private final static Lock writeLock=lock.writeLock();

   private final static Map<String,Data> map=new TreeMap<>();

   public Data getData(String key){
       readLock.lock();
       try {
           return map.get(key);
       }finally {
           readLock.unlock();
       }
   }


   public Set<String> getKeys(){
       readLock.lock();
       try {
           return map.keySet();
       }finally {
           readLock.unlock();
       }
   }

   public void put(String key,Data value){
       writeLock.lock();
       try{
           map.put(key,value);
       }finally {
           writeLock.unlock();
       }
   }


   class Data{

   }


}
