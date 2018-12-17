package com.onyx.concurrency.example.singleton;

import com.onyx.concurrency.annotaions.ThreadSafe;

/**
 * 静态内部类的实现方式
 */
@ThreadSafe
public class SingletonDemo7 {

    private SingletonDemo7() {
    }

    private static class SingletonHolder {
        private static final SingletonDemo7 INSTANCE = new SingletonDemo7();
    }

    public static final SingletonDemo7 getInstance() {
        return SingletonHolder.INSTANCE;
    }

}
