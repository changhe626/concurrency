package com.onyx.concurrency.example.others;

public class MultipleThreadPractise {
}
/**
 * 多线程的最佳实践:
 * 1.使用本地变量
 * 2.使用不可变类
 * 3.最小化锁的作用域范围:  S=1/(1-a+a/n)
 *      a 并行比率
 *      n 节点个数
 *      S 加锁比
 * 4.使用线程池的Executors,而不是直接new  Thread执行
 * 5.宁可使用同步也不要使用线程的wait和notify
 * 6.使用BlockingQueue 实现生成-消费模式
 * 7.使用并发集合而不是加了锁的同步集合
 * 8.使用Semaphore 创建有界的访问
 * 9.宁可使用同步代码块,也不使用同步方法
 * 10.避免使用静态变量,如果使用就加上final
 *
 */
