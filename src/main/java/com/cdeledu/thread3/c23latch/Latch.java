package com.cdeledu.thread3.c23latch;

import java.util.concurrent.TimeUnit;

public abstract class Latch {
	
	//用于控制多少个线程完成任务时才能打开阀门
	protected int limit;
	
	public Latch(int limit){
		this.limit = limit;
	}
	
	/**会使得当前线程一直等待，直到所有线程都完成工作，被阻塞的线程是允许被中断的
	 * @throws InterruptedException
	 */
	public abstract void await() throws InterruptedException;
	
	/**带有超时的等待，如果在指定时间之后，还有人没有完成任务，则会收到超时通知，但并不会中断未完成的线程，未完成的线程还是会继续执行，需要程序员自己控制
	 * @throws InterruptedException
	 */
	public abstract void await(TimeUnit unit, long time) throws InterruptedException, WaitTimeoutException;
	
	/**
	 * 当任务线程完成工作之后调用该方法使得计数器减一
	 */
	public abstract void countDown();

	/**获取当前还有多少个线程没有完成任务
	 * @return
	 */
	public abstract int getUnarrived();
	
}
