package com.cdeledu.thread2.c3concurrent.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

//线程工厂：用于创造线程池里的线程
public class ThreadFactoryDemo2 {

	public static class WorkRunnable implements Runnable
	{
		@Override
		public void run() {
			 System.out.println("complete a task");
		}

	}

	public static class WorkThread extends Thread {
	    private Runnable target;   //线程执行目标
	    private AtomicInteger counter;

	    public WorkThread(Runnable target, AtomicInteger counter) {
	        this.target = target;
	        this.counter = counter;
	    }
	    @Override
	    public void run() {
	        try {
	            target.run();
	        } finally {
	            int c = counter.getAndDecrement();
	            System.out.println("terminate no " + c + " Threads");
	        }
	    }
}

	public static class WorkThreadFactory implements ThreadFactory{

		private AtomicInteger atomicInteger = new AtomicInteger(0);

		@Override
		public Thread newThread(Runnable r)
		{
			int c = atomicInteger.incrementAndGet();
	                System.out.println("create no " + c + " Threads");
			return new WorkThread(r, atomicInteger);//通过计数器，可以更好的管理线程
		}

	}

	public static void main(String[] args) {
		//创建线程（并发）池，自动伸缩(自动条件线程池大小)
		ExecutorService es =  Executors.newCachedThreadPool(new WorkThreadFactory());

		//同时并发5个工作线程
		es.execute(new WorkRunnable());
		es.execute(new WorkRunnable());
		es.execute(new WorkRunnable());
		es.execute(new WorkRunnable());
		es.execute(new WorkRunnable());
		//指示当所有线程执行完毕后关闭线程池和工作线程，如果不调用此方法，jvm不会自动关闭
	       es.shutdown();

	    try {
	         //等待线程执行完毕，不能超过2*60秒，配合shutDown
	          es.awaitTermination(2*60, TimeUnit.SECONDS);
	     } catch (InterruptedException e) {
	            e.printStackTrace();
	     }
	}

}
