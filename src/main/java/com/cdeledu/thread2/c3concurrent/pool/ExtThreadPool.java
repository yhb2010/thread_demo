package com.cdeledu.thread2.c3concurrent.pool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

//扩展线程池：重写afterExecute，beforeExecute
public class ExtThreadPool {

	public static class DefinedThreadpool extends ThreadPoolExecutor {

		private final ThreadLocal<Long> startTime = new ThreadLocal<>();
		private final AtomicLong numTasks = new AtomicLong();
		private final AtomicLong totalTime = new AtomicLong();

		public DefinedThreadpool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue workQueue) {
			super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
		}

		@Override
		//运行任务结束
		protected void afterExecute(Runnable r, Throwable t) {
			try {
				long endTime = System.currentTimeMillis();
				long useTime = endTime - startTime.get();
				numTasks.incrementAndGet();
				totalTime.addAndGet(useTime);
				System.out.println("afterExecute " + r);
			} finally {
				super.afterExecute(r, t);
			}
		}

		@Override
		//运行任务前
		protected void beforeExecute(Thread t, Runnable r) {
			super.beforeExecute(t, r);
			System.out.println("beforeExecute " + r);
			startTime.set(System.currentTimeMillis());
		}

		@Override
		//线程池关闭的时候执行，shutdown只是发送了一个信号，但在shutdown方法执行后，这个线程池就不再接受其他新的任务了。
		protected void terminated() {
			try {
				System.out.println("terminated avg time " + totalTime.get() + " " + numTasks.get());
			} finally {
				super.terminated();
			}
		}

	}

	public static class MyThread extends Thread {
		public void run() {
			System.out.println("run " + Thread.currentThread().getName());
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		DefinedThreadpool pool = new DefinedThreadpool(2, 2, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue(10));
		for (int i = 0; i < 3; i++) {
			pool.execute(new MyThread());
		}
		pool.shutdown();
	}

}
