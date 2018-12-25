package com.onyx.concurrency.example.concurrent;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zk
 * @Description:
 * @date 2018-12-25 10:19
 */
public class ConcurrentHashMapDemo2 {

    public static void main(String[] args) {
        /**
         * 当在实例化HashMap实例时，如果给定了initialCapacity，由于HashMap的capacity都是2的幂，因此这个方法用于找到大于等于initialCapacity的最小的2的幂（initialCapacity如果就是2的幂，则返回的还是这个数）
         */
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>(5);
        //实际的是算出来的是8
        System.out.println(map.size());


    }

}
