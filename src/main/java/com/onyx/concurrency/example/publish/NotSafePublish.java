package com.onyx.concurrency.example.publish;

import com.onyx.concurrency.annotaions.NotThreadSafe;

import java.util.Arrays;

/**
 * 如何安全发布对象
 为了安全的发布对象，对象的引用以及对象的状态必须同时对其他线程可见。那么如何才能安全的发布对象呢，下面是正确发布对象的几种方法

 1.通过静态初始化器初始化对象的引用
 常见的例子是在构造方法中注册监听器或启动线程。如果非要在构造函数中启动线程或启动线程，可以使用私有的构造函数和公有的工厂方法，
 比如上面注册监听器的例子：

 2.线程封闭
 需要线程同步的基础是线程间共享对象和数据，如果线程不共享数据和对象，对象只封闭在线程内部，那么就不需要线程同步，也就不需要发布对象，即使对象是不线程安全的。

 2.1 线程限制
 通过编程人员维护对象安全发布，而没有变量修饰词或线程本地变量把对象限制在线程上。这种方法非常容易出错，因此也叫非正式线程限制（Ad-hoc线程限制）。
 下面是几种常见的线程限制方法：

 volatile变量
 一张典型的线程限制是使用volatile关键词。只要你确保只要一个线程修改volatile变量，那么volatile变量的读取-修改-写入操作就限制在一个线程中，
 避免了线程的竞争条件。并且volatile的可见性可以保证其他线程能看到变量的最新值，因此是线程安全的。

 栈限制
 即使变量的作用域只局限于线程内部，比如局部变量。

 2.3 threadlocal
 threadlocal允许每个线程与对象关联在一起，它提供了set/get方法，为每个使用它的线程维护一份单独的拷贝。
 threadlocal通常使用在防止基于可变的单例或全局变量中出现不正确的共享。

 2.4 不变对象
 不变对象一旦被创建就不能更改，所以永远是线程安全的。所谓的不变对象，必须满足3个条件：创建后状态不能更改，所有域必须是final的，
 被正确构建（构造期间没有发生对象逸出）。不变对象一般是一些比较简单的对象，它的内部状态在对象构造方法里就赋值了。

 3.将对象的引用存储到被正确同步的变量或对象中
 具体有下面几种做法：
 将对象的引用存储到volatile域或AutomaticReference中；
 将对象的引用存储到正确创建对象的final域中
 将对象的引用存储到被锁正确保护的域中，比如线程安全的容器 vector，synchronizedMap,concurrentMap,synchronizedList,synchronizedSet，BlockingQueue,concurrentLinkedQueue等。
 */
@NotThreadSafe
public class NotSafePublish {

    private String[] arr={"1","2","3"};

    /**
     * 发布对象,线程不安全的,外面的可以修改里面的值,应该复制一份数据出去,然后发布到外面去
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
