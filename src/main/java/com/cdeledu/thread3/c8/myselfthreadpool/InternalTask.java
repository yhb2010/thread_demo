package com.cdeledu.thread3.c8.myselfthreadpool;

/**用于线程池内部，会使用到RunnableQueue，然后不断的从queue中取出某个runnable，并运行run方法。
 * @author Administrator
 *
 */
public class InternalTask implements Runnable {
	
	private final RunnableQueue runnableQueue;
	private volatile boolean running = true;

	public InternalTask(RunnableQueue runnableQueue) {
		super();
		this.runnableQueue = runnableQueue;
	}

	@Override
	public void run() {
		//如果当前任务为running并且没有被中断，则其将不断从queue中获取runnable，然后执行run方法
		while(running && !Thread.currentThread().isInterrupted()){
			try{
				Runnable task = runnableQueue.take();
				task.run();
			}catch(InterruptedException e){
				running = false;
				break;
			}
		}
	}
	
	/**
	 * 停止当前任务，主要会在线程池的shutdown方法中使用
	 */
	public void stop(){
		running = false;
	}

}
