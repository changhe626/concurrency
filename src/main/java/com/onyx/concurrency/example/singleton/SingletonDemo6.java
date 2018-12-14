package com.onyx.concurrency.example.singleton;

import com.onyx.concurrency.annotaions.ThreadSafe;

/**
 * 懒汉模式,类装在的时候加初始化完成.
 */
@ThreadSafe
public class SingletonDemo6 {

    //私有构造函数
    //构造函数没有太多的处理,这个类一定会被使用,否则资源浪费了.
    private SingletonDemo6() {
    }

    //单例对象
    private static SingletonDemo6 instance=null;


    //static要放在变量的声明后面,否则就是null了,一定注意
    static {
        instance=new SingletonDemo6();
    }


    //获取单例,静态的工厂方法
    public static SingletonDemo6 getInstance(){
        return instance;
    }


    public static void main(String[] args) {

        System.out.println(getInstance());
        System.out.println(getInstance());
        System.out.println(getInstance());
    }
}
