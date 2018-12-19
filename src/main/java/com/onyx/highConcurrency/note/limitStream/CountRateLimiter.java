package com.onyx.highConcurrency.note.limitStream;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author zk
 * @Description:计数器法   用一个计算器来实现
 * @date 2018-12-19 9:31
 */
public class CountRateLimiter {

    /**
     * 计数器
     */
    private AtomicLong counter=new AtomicLong(0);
    /**
     * 初始时间
     */
    private static long timestamp=System.currentTimeMillis();

    /**
     * 时间窗口内最大的请求个数
     */
    private long limit;

    public CountRateLimiter(long limit) {
        this.limit = limit;
    }

    public  boolean tryAcquire(int i){
        long now = System.currentTimeMillis();
        //1s内的请求
        if(now-timestamp<1000){
            if(counter.get()<limit){
                counter.incrementAndGet();
                System.out.println("请求通过:"+i);
                return true;
            }else {
                System.out.println("请求拒绝:"+i);
                return false;
            }
        }else {
            counter=new AtomicLong(0);
            timestamp=now;
            System.out.println("时间结束,拒绝请求:"+i);
            return false;
        }
    }

    public static void main(String[] args) {
        CountRateLimiter rateLimiter = new CountRateLimiter(10);
        ExecutorService pool = Executors.newCachedThreadPool();
        for (int i = 0; i < 1000; i++) {
            final int tmp=i;
            pool.execute(()->{
                double random = (new Random()).nextDouble();
                long a = (long)(random * 1000);
                try {
                    Thread.sleep(a);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                rateLimiter.tryAcquire(tmp);
            });
        }
        pool.shutdown();
    }


}

/**
 * 计数器法是限流算法里最简单也是最容易实现的一种算法。比如我们规定，对于A接口来说，我们1分钟的访问次数不能超过100个。那么我们可以这么做：
 * 在一开 始的时候，我们可以设置一个计数器counter，每当一个请求过来的时候，counter就加1，如果counter的值大于100并且该请求与第一个
 * 请求的间隔时间还在1分钟之内，那么说明请求数过多；如果该请求与第一个请求的间隔时间大于1分钟，且counter的值还在限流范围内，那么就重置 counter
 *
 * 有一个十分致命的问题，那就是临界问题
 * 假设有一个恶意用户，他在0:59时，瞬间发送了100个请求，并且1:00又瞬间发送了100个请求，那么其实这个用户在 1秒里面，瞬间发送了200个请求。
 * 我们刚才规定的是1分钟最多100个请求，也就是每秒钟最多1.7个请求，用户通过在时间窗口的重置节点处突发请求，
 * 可以瞬间超过我们的速率限制。用户有可能通过算法的这个漏洞，瞬间压垮我们的应用。
 */

