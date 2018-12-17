package com.onyx.concurrency.example.aqs;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class FutureTaskDemo1 {

    public static void main(String[] args) throws Exception {

        FutureTask<String> task = new FutureTask<>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println("开始执行call方法了");
                Thread.sleep(5000);
                return "success";
            }
        });
        new Thread(task).start();
        System.out.println("do some thing");
        Thread.sleep(5000);
        System.out.println("返回的是:"+task.get());
    }

}
/**
 * https://blog.csdn.net/javazejian/article/details/50896505
 *
 * FutureTask除了实现了Future接口外还实现了Runnable接口，因此FutureTask也可以直接提交给Executor执行。
 * 当然也可以调用线程直接执行（FutureTask.run()）
 */
