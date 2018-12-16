package com.onyx.concurrency.example.aqs;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FutureDemo1 {

    static class CallAbleTask implements Callable<String>{
        @Override
        public String call() throws Exception {
            System.out.println("开始执行call方法了");
            Thread.sleep(5000);
            return "success";
        }
    }


    public static void main(String[] args) throws Exception {

        ExecutorService pool = Executors.newCachedThreadPool();
        Future<String> future = pool.submit(new CallAbleTask());
        System.out.println("do some thing");
        Thread.sleep(5000);
        System.out.println("返回的是:"+future.get());
        pool.shutdown();
    }

}
