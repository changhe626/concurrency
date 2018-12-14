package com.onyx.concurrency.example.singleton;


import com.onyx.concurrency.annotaions.Recommend;
import com.onyx.concurrency.annotaions.ThreadSafe;

/**
 * 枚举模式,最安全的
 * 安全发布对象
 */
@ThreadSafe
@Recommend
public class SingletonDemo7 {

    private SingletonDemo7() {
    }

    public static SingletonDemo7 getInstance(){
        return Singleton.INSTANCE.getInstance();
    }

    private enum Singleton{
        INSTANCE;

        private SingletonDemo7 singleton;

        //JVM 保证构造函数只调用一次
        Singleton(){
            singleton=new SingletonDemo7();
        }

        public SingletonDemo7 getInstance(){
            return singleton;
        }
    }


}
