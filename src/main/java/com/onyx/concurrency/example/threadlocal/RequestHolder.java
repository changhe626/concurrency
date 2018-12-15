package com.onyx.concurrency.example.threadlocal;

/**
 * 线程封闭
 *
 * Ad-hoc 线程封闭:程序控制实现,最糟糕,忽略
 *
 * 堆栈封闭,局部变量,无并发
 * ThreadLocal  线程封闭,特别好的封闭方法
 *
 * ThreadLocal容器
 */
public class RequestHolder {

    private final static  ThreadLocal<Long> requestHolder=new ThreadLocal<>();

    public static void add(Long id){
        requestHolder.set(id);
    }

    public static Long getId(){
        return requestHolder.get();
    }

    /**
     * 如果不删除,会造成内存泄漏
     */
    public static void remove(){
        requestHolder.remove();
    }

}
