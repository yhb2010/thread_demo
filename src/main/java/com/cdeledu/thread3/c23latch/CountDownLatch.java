package com.cdeledu.thread3.c23latch;

import java.util.concurrent.TimeUnit;

/**无限等待CountDownLatch实现
 * @author Administrator
 *
 */
public class CountDownLatch extends Latch {
	
	//回调函数：当所有任务都执行完成后回调
	protected Runnable callback;
	
	public CountDownLatch(int limit){
		super(limit);
	}
	
	public CountDownLatch(int limit, Runnable callback){
		super(limit);
		this.callback = callback;
	}

	@Override
	public void await() throws InterruptedException {
		synchronized (this) {
			//当limit>0时，当前线程进入阻塞状态
			while(limit > 0) {
				wait();
			}
		}
		if(callback != null){
			callback.run();
		}
	}
	
	@Override
	public void await(TimeUnit unit, long time) throws InterruptedException, WaitTimeoutException {
		if(time < 0){
			throw new IllegalArgumentException("The time is invalid.");
		}
		//将time转换为纳秒
		long remainingNanos = unit.toNanos(time);
		//等待任务将在endNanos纳秒后超时
		final long endNanos = System.nanoTime() + remainingNanos;
		synchronized (this) {
			//当limit>0时，当前线程进入阻塞状态
			while(limit > 0) {
				//如果超时则抛出WaitTimeoutException异常
				if(TimeUnit.NANOSECONDS.toMillis(remainingNanos) <= 0){
					throw new WaitTimeoutException("The wait time over specify time.");
				}
				//等待remainingNanos，在等待的过程中有可能会被中断，需要重新计算endNanos
				wait(TimeUnit.NANOSECONDS.toMillis(remainingNanos));
				remainingNanos = endNanos - System.nanoTime();
			}
		}
		if(callback != null){
			callback.run();
		}
	}

	@Override
	public void countDown() {
		synchronized (this) {
			if(limit <= 0){
				throw new IllegalStateException("all of task already arrived.");
			}
			limit--;
			notifyAll();
		}
	}

	//在多线程下，某个线程在获取Unarrived任务数时，有可能limit又被减少了，所以getUnarrived是一个评估值
	@Override
	public int getUnarrived() {
		return limit;
	}

}
