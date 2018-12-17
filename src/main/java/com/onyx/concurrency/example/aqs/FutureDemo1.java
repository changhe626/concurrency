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
/**
 * Future<V>接口是用来获取异步计算结果的:
 cancel方法用来取消任务，如果取消任务成功则返回true，如果取消任务失败则返回false。参数mayInterruptIfRunning表示是否允许取消正在执行却没有执行完毕
    的任务，如果设置true，则表示可以取消正在执行过程中的任务。如果任务已经完成，则无论mayInterruptIfRunning为true还是false，此方法肯定返回false，
    即如果取消已经完成的任务会返回false；如果任务正在执行，若mayInterruptIfRunning设置为true，则返回true，若mayInterruptIfRunning设置为false，
    则返回false；如果任务还没有执行，则无论mayInterruptIfRunning为true还是false，肯定返回true。
 isCancelled方法表示任务是否被取消成功，如果在任务正常完成前被取消成功，则返回 true。
 isDone方法表示任务是否已经完成，若任务完成，则返回true；
 get()方法用来获取执行结果，这个方法会产生阻塞，会一直等到任务执行完毕才返回；
 get(long timeout, TimeUnit unit)用来获取执行结果，如果在指定时间内，还没获取到结果，就直接返回null。
 */
