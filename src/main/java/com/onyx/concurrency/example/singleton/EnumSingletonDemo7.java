package com.onyx.concurrency.example.singleton;


import com.onyx.concurrency.annotaions.Recommend;
import com.onyx.concurrency.annotaions.ThreadSafe;

/**
 * 枚举模式,最安全的
 * 安全发布对象
 */
@ThreadSafe
@Recommend
public class EnumSingletonDemo7 {

    private EnumSingletonDemo7() {
    }

    public static EnumSingletonDemo7 getInstance(){
        return Singleton.INSTANCE.getInstance();
    }

    private enum Singleton{
        INSTANCE;

        private EnumSingletonDemo7 singleton;

        //JVM 保证构造函数只调用一次
        Singleton(){
            singleton=new EnumSingletonDemo7();
        }

        public EnumSingletonDemo7 getInstance(){
            return singleton;
        }
    }


}
