package com.onyx.concurrency.otherexample;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.RandomAccess;

/**
 * 用来当标记的，是一种标记接口，接口的非典型用法
 * <p>
 * 意思是，随机访问任意下标元素都比较快
 * <p>
 * 用处，当要实现某些算法时，会判断当前类是否实现了RandomAccess接口
 * <p>
 * 会根据结果选择不同的算法
 *
 *
 * 当实现该接口时，说明支持快速访问。
 * 即：
 * for (int i=0, n=list.size(); i<n; i++)
 * list.get(i);
 * 要比
 * for (Iterator i=list.iterator(); i.hasNext(); )
 * i.next();
 * 访问速度快。
 * 当没有实现该接口时，
 * for (int i=0, n=list.size(); i<n; i++)
 * list.get(i);
 * 要比
 * for (Iterator i=list.iterator(); i.hasNext(); )
 * i.next();
 * 访问速度慢。
 *
 */
public class RandomAccessDemo {

    public static void test(List list) {
        long startTime;
        long endTime;
        //遍历前先判读是否实现了RandomAccess接口
        if (list instanceof RandomAccess) {
            System.out.println(list.getClass() + "实现了RandomAccess接口");
        } else {
            System.out.println(list.getClass() + "未实现RandomAccess接口");
        }

        System.out.println("1.以实现RandomAccess接口方式访问");
        startTime = System.currentTimeMillis();
        for (int i = 0; i < list.size(); i++) {
            Object o = list.get(i);
        }
        endTime = System.currentTimeMillis();
        System.out.println("耗时：" + (endTime - startTime));


        System.out.println("2.以未实现RandomAccess接口方式访问");
        startTime = System.currentTimeMillis();
        for (Iterator iter = list.iterator(); iter.hasNext(); ) {
            Object o = iter.next();
        }
        endTime = System.currentTimeMillis();
        System.out.println("耗时：" + (endTime - startTime));


        System.out.println("3.for循环的方式进行遍历");
        startTime = System.currentTimeMillis();
        for (Object o : list) {
            Object o2=0;
        }
        endTime = System.currentTimeMillis();
        System.out.println("耗时：" + (endTime - startTime));


    }

    /**
     * ArrayList 实现了RandomAccess接口,而LinkedList没有实现
     */
    public static void main(String[] args) {
        List arraylist = new ArrayList();
        // 添加1000个元素
        for (int i = 0; i < 100000; i++) {
            arraylist.add("aaa");
        }

        List linkList = new LinkedList();
        // 添加1000个元素
        for (int i = 0; i < 100000; i++) {
            linkList.add("aaa");
        }
        //test(arraylist);// 7  6  5
        test(linkList); //  4156  4  7

    }

}
