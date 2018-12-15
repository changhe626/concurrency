package com.onyx.concurrency.example.immutable;

import com.google.common.collect.Maps;
import com.onyx.concurrency.annotaions.ThreadSafe;

import java.util.Collections;
import java.util.Map;

@ThreadSafe
public class ImmutableDemo2 {

    private   static Map<Integer,Integer> maps= Maps.newHashMap();
    static {
        maps.put(1,2);
        maps.put(3,4);
        maps.put(5,6);
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
