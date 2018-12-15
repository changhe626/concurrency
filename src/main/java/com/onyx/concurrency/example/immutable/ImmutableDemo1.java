package com.onyx.concurrency.example.immutable;

import com.google.common.collect.Maps;
import com.onyx.concurrency.annotaions.NotThreadSafe;

import java.util.Map;

@NotThreadSafe
public class ImmutableDemo1 {

    private final  static Integer a=1;
    private final  static String b="b";
    private final  static Map<Integer,Integer> maps= Maps.newHashMap();
    static {
        maps.put(1,2);
        maps.put(3,4);
        maps.put(5,6);
    }

    public static void main(String[] args) {

        maps.put(6,7);
        /**
         * map中的值是可以变化的.
         */
        System.out.println(maps);

    }

    private void test(final int a){
        //a=1;
        System.out.println(a);
    }


}
