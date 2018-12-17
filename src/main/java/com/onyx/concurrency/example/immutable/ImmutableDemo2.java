package com.onyx.concurrency.example.immutable;

import com.google.common.collect.Maps;
import com.onyx.concurrency.annotaions.ThreadSafe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 不可变对象最重要的特征是线程安全。这意味着多个线程能够在同时访问同一个对象，而且不需要担心与其他线程产生冲突。
   如果对象的方法都不能改变对象的状态，那么不管有多少个对象，不管它们被并行调用的频率——不可变对象运行在自己的堆栈中。


 下面几点是支持对象不可变性的一些理由：

 不可变对象更容易构造、测试与使用。
 真正的不可变对象都是线程安全的。
 不可变对象可以避免耦合。
 不可变对象的使用没有副作用（没有保护性拷贝）。
 对象变化的问题得到了避免。
 不可变对象的失败都是原子性的。
 不可变对象更容易缓存。
 不可变对象可以避免空值（NULL）引用，这通常是很糟糕的

 */
@ThreadSafe
public class ImmutableDemo2 {

    private   static Map<Integer,Integer> maps= Maps.newHashMap();
    static {
        maps.put(1,2);
        maps.put(3,4);
        maps.put(5,6);
        /**
         * jdk中的自带的不可变的容器
         */
        List<Object> list = Collections.unmodifiableList(new ArrayList<>());
        Set<Object> set = Collections.unmodifiableSet(new HashSet<>());
        maps= Collections.unmodifiableMap(maps);

    }

    public static void main(String[] args) {

        maps.put(6,7);
        /**
         * map中的值是不可以变化的.
         */
        System.out.println(maps);

    }


}
