package com.cdeledu.thread3.c19future;

/**是Future的实现，除了实现get及done方法，还额外增加了finish方法，该方法用于接收任务被完成的通知
 * @author Administrator
 *
 * @param <T>
 */
public class FutureTask<T> implements Future<T> {

	//计算结果
	private T result;
	//任务是否完成
	private boolean isDone = false;
	//定义对象锁
	private final Object LOCK = new Object();
	
	@Override
	public T get() throws InterruptedException {
		synchronized (LOCK) {
			while(!done()){
				LOCK.wait();
			}
			return result;
		}
	}
	
	protected void finish(T result) {
		synchronized (LOCK) {
			if(isDone){
				return;
			}
			isDone = true;
			this.result = result;
			LOCK.notifyAll();
		}
	}

	@Override
	public boolean done() {
		return isDone;
	}

}
