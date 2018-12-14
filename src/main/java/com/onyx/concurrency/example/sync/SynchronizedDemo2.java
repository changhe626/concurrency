package com.onyx.concurrency.example.sync;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 一、概念
 synchronized 是 Java 中的关键字，是利用锁的机制来实现同步的。
 锁机制有如下两种特性：
 互斥性：即在同一时间只允许一个线程持有某个对象锁，通过这种特性来实现多线程中的协调机制，这样在同一时间只有一个线程对需同步的代码块(复合操作)进行访问。
 互斥性我们也往往称为操作的原子性。

 可见性：必须确保在锁被释放之前，对共享变量所做的修改，对于随后获得该锁的另一个线程是可见的（即在获得锁时应获得最新共享变量的值），
 否则另一个线程可能是在本地缓存的某个副本上继续操作从而引起不一致。


 对象锁和类锁
 1. 对象锁
 在 Java 中，每个对象都会有一个 monitor 对象，这个对象其实就是 Java 对象的锁，通常会被称为“内置锁”或“对象锁”。类的对象可以有多个，
 所以每个对象有其独立的对象锁，互不干扰。
 2. 类锁
 在 Java 中，针对每个类也有一个锁，可以称为“类锁”，类锁实际上是通过对象锁实现的，即类的 Class 对象锁。每个类只有一个 Class 对象，
 所以每个类只有一个类锁。

 总结：
 A. 无论synchronized关键字加在方法上还是对象上，如果它作用的对象是非静态的，则它取得的锁是对象；如果synchronized作用的对象是一个静态方法或一个类，则它取得的锁是对类，该类所有的对象同一把锁。
 B. 每个对象只有一个锁（lock）与之相关联，谁拿到这个锁谁就可以运行它所控制的那段代码。
 C. 实现同步是要很大的系统开销作为代价的，甚至可能造成死锁，所以尽量避免无谓的同步控制。

 */

/**
 * 编译之后，切换到SynchronizedDemo1.class的同级目录之后，然后用javap -v SynchronizedDemo1.class查看字节码文件
 * 查看java字节码的工具jclasslib
 *
 * 执行同步代码块后首先要先执行monitorenter指令，退出的时候monitorexit指令。通过分析之后可以看出，使用Synchronized进行同步，
 * 其关键就是必须要对对象的监视器monitor进行获取，当线程获取monitor后才能继续往下执行，否则就只能等待。而这个获取的过程是互斥的，
 * 即同一时刻只有一个线程能够获取到monitor。
 */

public class SynchronizedDemo2 {

    public static void main(String[] args) {
        SynchronizedDemo2 demo1 = new SynchronizedDemo2();
        SynchronizedDemo2 demo2 = new SynchronizedDemo2();
        ExecutorService service = Executors.newCachedThreadPool();

        service.execute(()->{
            demo2.test1(1);
        });
        service.execute(()->{
            demo1.test1(2);
        });

       /* service.execute(()->{
            demo2.test2();
        });
        service.execute(()->{
            demo1.test2();
        });*/

    }

    //修饰代码块.
    public void test1(int j){
        synchronized (SynchronizedDemo2.class){
            for (int i = 0; i < 10; i++) {
                System.out.println("test1~~"+j+" i="+i);
            }
        }
    }


    /**
     * 修饰方法,静态方法是属于类的而不属于对象的。同样的，synchronized修饰的静态方法锁定的是这个类的所有对象
     *
     */
    public synchronized static  void test2(int j){
        for (int i = 0; i < 10; i++) {
            System.out.println("test2~~"+j+" i="+i);
        }
    }

}
