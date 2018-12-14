package com.onyx.concurrency.example.singleton;

import com.onyx.concurrency.annotaions.ThreadSafe;

/**
 * 懒汉模式,类装在的时候加初始化完成.
 */
@ThreadSafe
public class SingletonDemo2 {

    //私有构造函数
    //构造函数没有太多的处理,这个类一定会被使用,否则资源浪费了.
    private SingletonDemo2() {
    }

    //单例对象
    private static SingletonDemo2 instance=new SingletonDemo2();

    //获取单例,静态的工厂方法
    public static SingletonDemo2 getInstance(){
        return instance;
    }
}
