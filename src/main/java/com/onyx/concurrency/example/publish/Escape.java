package com.onyx.concurrency.example.publish;

import com.onyx.concurrency.annotaions.NotRecommend;
import com.onyx.concurrency.annotaions.NotThreadSafe;

/**
 * 对象溢出
 */
@NotThreadSafe
@NotRecommend
public class Escape {

    private int canBeEscape=1;

    public Escape() {
        new InnerClass();
    }

    private class InnerClass{
        public InnerClass(){
            //在类还没有正确构造之前就已经发布了....这是不正确的
            System.out.println("变量是:"+Escape.this.canBeEscape);
        }
    }

    public static void main(String[] args) {
        new Escape();
    }
}
