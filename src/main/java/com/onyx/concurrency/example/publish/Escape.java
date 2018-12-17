package com.onyx.concurrency.example.publish;

import com.onyx.concurrency.annotaions.NotRecommend;
import com.onyx.concurrency.annotaions.NotThreadSafe;

/**
 * 对象溢出
 * 一个对象超出了她应有的作用域，或者未被构造完全就发布了，称为对象逸出。很多情况下我们不希望对象以及对象的内部属性不被暴露出去，
 * 但是在其他一些情况中，我们又必须使对象发布出去，但是如果不经意中把对象的内部状态也发布出去了，就使得其他线程可以修改这个对象的内部状态，
 * 造成不可预知的线程安全性风险，同时，如果对象没有构造完全就发布出去了同样会危及线程安全。
 *
 *
 * 逸出:
 逸出即为发布了本不该发布的对象；


 逸出的方式：
 1、 静态变量的引用对象：
 将对象的引用保存在一个公有的静态变量中，是发布对象的最直接和最简单的方式。
 例如以下代码示例，在示例中我们也看到，由于任何代码都可以遍历我们发布persons集合，导致我们间接的发布了Person实例，
 自然也就造成可以肆意的访问操作集合中的Person元素。


 public class ObjectPublish {

 public static HashSet<Person> persons ;
 public void init()
 {
 persons = new HashSet<Person>();
 }
 }
 这样任何代码都可以遍历这个集合，并获得对这个新Person对象引用。



 2、非私有的方法返回对象的引用：

 在非私有的方法内返回一个私有变量的引用会导致私有变量的逸出，例如以下代码

 public class ObjectPublish {
 private  HashSet<Person> persons=  new HashSet<Person>();
 public HashSet<Person>  getPersons()
 {
 return this.persons;
 }
 }
 在这个实例中，该变量本是私有的变量，但是这样已经逃出它所在的作用域范围，因为任何调用者都能修改这个集合里面的内容

 3、发布一个对象也会导致此对象的所有非私有的字段对象的发布 (this逃逸)：

 this逃逸就是说，在构造函数返回之前，其他线程就已经取得了该对象的引用，由于构造函数还没有完成，所以，对象也可能是残缺的，
 所以，取得对象引用的线程使用残缺的对象极有可能发生错误的情况。因为这两个线程是异步的，取得对象引用的线程并不一定会等待构造对象的线程完结后在使用引用。

 stackoverflow上http://stackoverflow.com/questions/3705425/java-reference-escape讨论了this逸出的问题。
 下面给出2个具体点的例子，更容易理解this逸出的问题。

 public class ThisEscape {
 private final int var;

 public ThisEscape(EventSource source) {
 source.registerListener(
 new EventListener() {
 public void onEvent(Event e) {
 doSomething(e);
 }
 }
 );

 // more initialization
 // ...

 var = 10;
 }

 // result can be 0 or 10
 int doSomething(Event e) {
 return var;
 }
 }
 事件监听器一旦注册成功，就能够监听用户的操作，调用对应的回调函数。比如出发了e这个事件，会执行doSomething()回调函数。
 这个时候由于是异步的，ThisEscape对象的构造函数很有可能还没有执行完（没有给var赋值），此时doSomething中获取到的数据很有可能是0，这不符合预期。

 结论 ：

 1， 就是不要在 建构函数随便创建匿名类然后 发布它们。
 2， 不用再建构函数中随便起线程， 如果起要看有没有发布匿名类对象。

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
