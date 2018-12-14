package com.onyx.concurrency.example.singleton;

import com.onyx.concurrency.annotaions.NotThreadSafe;

/**
 * 饿汉模式,第一次使用的时候创建
 */
@NotThreadSafe
public class SingletonDemo1 {

    //私有构造函数
    private SingletonDemo1() {
    }

    //单例对象
    private static SingletonDemo1 instance = null;

    //获取单例,静态的工厂方法
    public static SingletonDemo1 getInstance() {
        if (instance == null) {
            instance = new SingletonDemo1();
        }
        return instance;
    }
}
