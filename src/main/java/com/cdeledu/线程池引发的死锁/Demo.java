package com.cdeledu.线程池引发的死锁;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Demo {
	
	/**看起来没什么问题 —— 所有信息按照预期的样子呈现在屏幕上：
INFO [pool-1-thread-1]: First
INFO [pool-1-thread-2]: Second
INFO [pool-1-thread-1]: Third
注意我们用 get() 阻塞线程，在显示“Third”之前必须等待内部线程（Runnable）运行完成。这是个大坑！等待内部任务完成意味着需要从线程池额外获取一个线程来执行任务。然而，我们已经使用到了一个线程，所以内部任务在获取到第二个线程前将一直阻塞。当前我们的线程池足够大，运行没问题。让我们稍微改变一下代码，将线程池缩减到只有一个线程。
ExecutorService pool = Executors.newSingleThreadExecutor();
死锁出现了！我们来一步一步分析：
打印“First”的任务被提交到只有一个线程的线程池
任务开始执行并打印“First”
我们向线程池提交了一个内部任务，来打印“Second”
内部任务进入等待任务队列。没有可用线程因为唯一的线程正在被占用
我们阻塞住并等待内部任务执行结果。不幸的是，我们等待内部任务的同时也在占用着唯一的可用线程
get() 方法无限等待，无法获取线程
死锁
这是否意味单线程的线程池是不好的？并不是，相同的问题会在任意大小的线程池中出现，只不过是在高负载情况下才会出现，这维护起来更加困难。你在技术层面上可以使用一个无界线程池，但这样太糟糕了。
	 * @param args
	 */
	public static void main(String[] args) {
		//ExecutorService pool = Executors.newFixedThreadPool(10);
		ExecutorService pool = Executors.newSingleThreadExecutor();
		pool.submit(() -> {
			try {
				System.out.println("First");
				pool.submit(() -> System.out.println("Second")).get();
				System.out.println("Third");
			} catch (InterruptedException | ExecutionException e) {
				System.out.println(e);
			}
		});

	}

}
