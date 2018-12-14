package com.onyx.concurrency.example.singleton;

import com.onyx.concurrency.annotaions.NotThreadSafe;

/**
 * 饿汉模式,第一次使用的时候创建
 * 双重同步锁,单例模式
 */
@NotThreadSafe
public class SingletonDemo4 {

    //私有构造函数
    private SingletonDemo4() {
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


    //单例对象
    private static SingletonDemo4 instance = null;

    //获取单例,静态的工厂方法
    public static SingletonDemo4 getInstance() {
        //双重检测
        if (instance==null) {
            synchronized (SingletonDemo4.class){
                if (instance == null) {
                    instance = new SingletonDemo4();
                }
            }
        }
        return instance;
    }
}
