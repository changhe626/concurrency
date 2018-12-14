package com.onyx.concurrency.example.singleton;

import com.onyx.concurrency.annotaions.Recommend;
import com.onyx.concurrency.annotaions.ThreadSafe;

/**
 * 饿汉模式,第一次使用的时候创建
 * 双重同步锁,单例模式
 */
@ThreadSafe
@Recommend
public class SingletonDemo5 {

    //私有构造函数
    private SingletonDemo5() {
    }

    //1.memory=allocate()  分类对象空间
    //2.ctroInst() 初始化对象
    //3.instance=memory() 设置instance指向刚分配的内存

    /**
     * 多线程时候:
     * jvm 和CPU优化,发生了指令的重排
     * 顺序变成了 1  3  2
     * 就获取到了没有初始化完成的对象了...
     *
     */


    //单例对象,使用了volatile 就不会发生指令重排了
    private static  volatile SingletonDemo5 instance = null;

    //获取单例,静态的工厂方法
    public static SingletonDemo5 getInstance() {
        //双重检测
        if (instance==null) {
            synchronized (SingletonDemo5.class){
                if (instance == null) {
                    instance = new SingletonDemo5();
                }
            }
        }
        return instance;
    }
}
