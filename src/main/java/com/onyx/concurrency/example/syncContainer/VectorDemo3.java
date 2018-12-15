package com.onyx.concurrency.example.syncContainer;

import com.onyx.concurrency.annotaions.NotThreadSafe;

import java.util.Iterator;
import java.util.Vector;

/**
 * 遍历循环和删除分开,遍历的时候不去更新....,可以遍历做标记...
 */
@NotThreadSafe
public class VectorDemo3 {

    private static void test1(Vector<Integer> v){
        for (Integer integer : v) {
            if(integer.compareTo(3)==0){
                v.remove(integer);
            }
        }
    }

    private static void test2(Vector<Integer> v){
        Iterator<Integer> iterator = v.iterator();
        while (iterator.hasNext()) {
            Integer next = iterator.next();
            if(next.equals(3)){
                v.remove(next);
            }
        }
    }


    //success
    private static void test3(Vector<Integer> v){
        for (int i = 0; i < v.size(); i++) {
             if(v.get(i).equals(3)){
                 v.remove(i);
             }
        }
    }




    public static void main(String[] args) {
        Vector<Integer> vector = new Vector();
        vector.add(1);
        vector.add(2);
        vector.add(3);

        //test1(vector);
        //test2(vector);
        test3(vector);


    }


}
