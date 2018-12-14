package com.onyx.concurrency.example.singleton;

import com.onyx.concurrency.annotaions.NotRecommend;
import com.onyx.concurrency.annotaions.ThreadSafe;

/**
 * 饿汉模式,第一次使用的时候创建
 */
@ThreadSafe
@NotRecommend
public class SingletonDemo3 {

    //私有构造函数
    private SingletonDemo3() {
    }

    //单例对象
    private static SingletonDemo3 instance = null;

    //获取单例,静态的工厂方法
    public static synchronized SingletonDemo3 getInstance() {
        if (instance == null) {
            instance = new SingletonDemo3();
        }
        return instance;
    }
}
