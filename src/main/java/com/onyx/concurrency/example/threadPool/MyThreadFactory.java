package com.onyx.concurrency.example.threadPool;
 
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 重写线程池工厂
 * https://zhuanlan.zhihu.com/p/34972660
 */
public class MyThreadFactory implements ThreadFactory {
 
	private final ThreadGroup threadGroup;
	
	private final AtomicInteger threadNumber = new AtomicInteger(0);
	
	private final String threadPrefix;
	
	public MyThreadFactory(String name) {
		//如果已经为当前应用程序建立了安全管理器，则返回此安全管理器；否则，返回 null
		SecurityManager security = System.getSecurityManager();
		//security.getThreadGroup()调用此方法时，返回所有新创建的线程实例化后所在的线程组。默认情况下，
		//返回当前线程所在的线程组。应该由指定的安全管理器重写此方法，以返回适当的线程组。
		threadGroup = (security!=null)?security.getThreadGroup():Thread.currentThread().getThreadGroup();
		threadPrefix = name + "-thread-";
	}

	@Override
	public Thread newThread(Runnable target) {
		//以原子方式将当前值+1，最后一个参数指定线程的堆栈大小
		Thread thread = new Thread(threadGroup,target,threadPrefix+threadNumber.getAndIncrement(),0);
		//设置为非守护级线程
		//守护级线程不属于程序的核心部分，当所有非守护级线程运行结束时，程序也就结束了
		//只要还有非守护级线程存在，程序就不能结束
		if(thread.isDaemon()) {
			thread.setDaemon(false);
		}
		if(thread.getPriority()!=Thread.NORM_PRIORITY) {
			thread.setPriority(Thread.NORM_PRIORITY);
		}
		System.out.println("执行了线程工厂的newThread()方法");
		System.out.println("线程名："+thread.getName()+",线程组："+thread.getThreadGroup());
		return thread;
	}
 
 
}
