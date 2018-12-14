package com.onyx.concurrency.example.publish;

import com.onyx.concurrency.annotaions.NotThreadSafe;

import java.util.Arrays;

@NotThreadSafe
public class NotSafePublish {

    private String[] arr={"1","2","3"};

    /**
     * 发布对象,线程不安全的
     * @return
     */
    public String[] getArr(){
        return arr;
    }

    public static void main(String[] args) {
        String[] arr = new NotSafePublish().getArr();
        System.out.println(Arrays.toString(arr));

        arr[0]="zhaojun";
        System.out.println(Arrays.toString(arr));

    }


}
