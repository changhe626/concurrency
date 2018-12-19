package com.onyx.highConcurrency.note.limitStream;

import com.google.common.util.concurrent.RateLimiter;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * 限流往往是服务端接口的必要特性之一，用于对抗大规模恶意请求，保护有限的计算和存储资源
 * <p>
 * google开源工具包guava提供了限流工具类RateLimiter，该类基于“令牌桶算法”，非常方便使用
 */
public class RateLimiterTest {

    public static void main(String[] args) {
        //每秒生产两个令牌
        final RateLimiter rateLimiter = RateLimiter.create(20.0);

        ExecutorService executorService = Executors.newCachedThreadPool();
        IntStream.range(0, 10).forEach(i -> {
            executorService.submit(() -> {
                //随机休眠
                Random random = new Random();
                int r = random.nextInt(1000);
                try {
                    TimeUnit.MICROSECONDS.sleep(r);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //每个线程需要一个令牌
                boolean token = rateLimiter.tryAcquire();
                if (token) {
                    System.out.println("token pass");
                } else {
                    System.out.println("token refuse");
                }
            });
        });

    }
}
/**
 * 漏桶和令牌桶比较
 * <p>
 *        令牌桶可以在运行时控制和调整数据处理的速率，处理某时的突发流量。放令牌的频率增加可以提升整体数据处理的速度，而通过每次获取令牌的个数增加或者放慢令牌的
 * 发放速度和降低整体数据处理速度。而漏桶不行，因为它的流出速率是固定的，程序处理速度也是固定的。整体而言，令牌桶算法更优，但是实现更为复杂一些。
 */