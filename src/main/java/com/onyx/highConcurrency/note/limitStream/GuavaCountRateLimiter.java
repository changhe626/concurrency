package com.onyx.highConcurrency.note.limitStream;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author zk
 * @Description:使用Guava的Cache来存储计数器，过期时间设置为2秒（保证1秒内的计数器是有的），
 * 然后我们获取当前时间戳然后取秒数来作为KEY进行计数统计和限流
 * @date 2018-12-19 9:33
 */
public class GuavaCountRateLimiter {

    private LoadingCache<Long,AtomicLong> counter= CacheBuilder.newBuilder().
            expireAfterWrite(2, TimeUnit.SECONDS)
            .build(new CacheLoader<Long, AtomicLong>() {
                @Override
                public AtomicLong load(Long aLong) throws Exception {
                    return new AtomicLong(0);
                }
            });


    /**
     * 限制每秒10个
     */
    private long limit=10;

    /**
     * 使用Guava的Cache来存储计数器，过期时间设置为2秒（保证1秒内的计数器是有的），
     * 然后我们获取当前时间戳然后取秒数来作为KEY进行计数统计和限流
     */
    private boolean tryAcquire() throws ExecutionException {
        //得到当前秒
        long currentSeconds = System.currentTimeMillis() / 1000;
        if(counter.get(currentSeconds).incrementAndGet()>limit){
            System.out.println("refuse_quest:count="+counter.get(currentSeconds));
            return true;
        }else {
            System.out.println("refuse_quest:count="+counter.get(currentSeconds));
            return false;
        }
    }

    public static void main(String[] args) {
        GuavaCountRateLimiter limiter = new GuavaCountRateLimiter();
        ExecutorService pool = Executors.newCachedThreadPool();
        for (int i = 0; i < 1000; i++) {
            pool.execute(()->{
                double random = (new Random()).nextDouble();
                long a = (long)(random * 1000);
                try {
                    Thread.sleep(a);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    limiter.tryAcquire();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            });
        }
        pool.shutdown();
    }


}
