package com.onyx.concurrency.example.immutable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.onyx.concurrency.annotaions.ThreadSafe;

import java.util.List;


/**
 * 不可变容器
 * 尽量不用可变容器,用不可变的,在多线程下好
 *
 * 推荐使用guava里面的不可变的容器,不要使用Collections里面的.
 */
@ThreadSafe
public class ImmutableDemo4 {

    /**
     * guava 中的不可变的容器,都是实现了java中对应的接口
     * 推荐使用ImmutableList接口进行接收,这样使用add方法的时候就会有不能使用的提示了,使用List接收就没有.
     */
    private static ImmutableList<Integer> list=ImmutableList.of(1,2,3,5,5);

    private static List<Integer> list2=ImmutableList.of(1,2,3,5,5);

    private static ImmutableSet<Integer> set=ImmutableSet.copyOf(list);

    private static ImmutableMap<Integer,Integer> map=ImmutableMap.of(1,2,3,4,5,6);

    private static ImmutableMap<Integer,Integer> map2=ImmutableMap.<Integer,Integer>builder().put(1,2).put(3,4).put(5,6).build();


    public static void main(String[] args) {

        list.add(1);
        //list2.add(1);
        //set.add(1);
        System.out.println(list);
        System.out.println(set);


    }
}
