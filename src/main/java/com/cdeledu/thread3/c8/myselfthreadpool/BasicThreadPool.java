package com.cdeledu.thread3.c8.myselfthreadpool;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

//线程池需要有数量控制属性、创建线程工厂、任务队列策略等功能。
//一个线程池除了控制参数外，最主要的是应该有活动线程，其中Queue<ThreadTask>主要用来存放活动线程，BasicThreadPool同时也是Thread的子类，它在初始化的时候启动，在keepalive时间间隔
//到了之后再自动维护活动线程数量(采用继承Thread的方式不好，因为会暴露Thread的方法，建议使用组合的方式)
public class BasicThreadPool extends Thread implements ThreadPool {

	/**
	 * 初始化线程数量
	 */
	private final int initSize;
	/**
	 * 线程池最大线程数量
	 */
	private final int maxSize;
	/**
	 * 线程池核心线程数量
	 */
	private final int coreSize;
	/**
	 * 当前活跃的线程数量
	 */
	private int activeCount;
	/**
	 * 创建线程所需工厂
	 */
	private final ThreadFactory threadFactory;
	/**
	 * 任务队列
	 */
	private final RunnableQueue runnableQueue;
	/**
	 * 线程池是否已经被shutdown
	 */
	private volatile boolean isShutdown = false;
	/**
	 * 工作线程队列
	 */
	private final Queue<ThreadTask> threadQueue = new ArrayDeque<>();
	private final static DenyPolicy DEFAULT_DENY_POLICY = new DenyPolicy.DiscardDenyPolicy();
	private final static ThreadFactory DEFAULT_THREAD_FACTORY = new DefaultThreadFactory();
	private final long keepAliveTime;
	private final TimeUnit timeUnit;
	
	/**构造时需要传递的参数：初始的线程数量、最大线程数量、核心线程数量、任务队列的最大数量
	 * @param initSize
	 * @param maxSize
	 * @param coreSize
	 * @param queueSize
	 */
	public BasicThreadPool(int initSize, int maxSize, int coreSize, int queueSize) {
		this(initSize, maxSize, coreSize, DEFAULT_THREAD_FACTORY, queueSize, DEFAULT_DENY_POLICY, 1, TimeUnit.SECONDS);
	}
	
	/**构造线程池时需要传入的参数，该构造函数需要的参数比较多
	 * @param initSize
	 * @param maxSize
	 * @param coreSize
	 * @param threadFactory
	 * @param queueSize
	 * @param denyPolicy
	 * @param keepAliveTime
	 * @param timeUnit
	 */
	public BasicThreadPool(int initSize, int maxSize, int coreSize,
			ThreadFactory threadFactory, int queueSize, 
			DenyPolicy denyPolicy, long keepAliveTime, TimeUnit timeUnit) {
		super();
		this.initSize = initSize;
		this.maxSize = maxSize;
		this.coreSize = coreSize;
		this.threadFactory = threadFactory;
		this.runnableQueue = new LinkedRunnableQueue(queueSize, denyPolicy, this);
		this.keepAliveTime = keepAliveTime;
		this.timeUnit = timeUnit;
		this.init();
	}
	
	/**
	 * 初始化，先创建initSize个线程
	 */
	private void init() {
		start();
		for(int i=0; i<initSize; i++){
			newThread();
		}
	}

	/**
	 * 线程池中线程数量的维护主要由run负责，这也是为什么BasicThreadPool继承了Thread
	 */
	private void newThread() {
		//创建任务线程，并且启动
		InternalTask internalTask = new InternalTask(runnableQueue);
		Thread thread = threadFactory.createThread(internalTask);
		ThreadTask threadTask = new ThreadTask(thread, internalTask);
		threadQueue.offer(threadTask);
		activeCount++;
		thread.start();
	}
	
	@Override
	public void run(){
		//run方法继承自Thread，主要用于维护线程数量，比如扩容、回收等工作
		while(!isShutdown &&!isInterrupted()){
			try {
				timeUnit.sleep(keepAliveTime);
			} catch (InterruptedException e) {
				isShutdown = true;
				break;
			}
			synchronized (this) {
				if(isShutdown)
					break;
				//当前的队列中有任务尚未处理，并且activeCount<coreSize则继续扩容
				if(runnableQueue.size() > 0 && activeCount < coreSize){
					for(int i=initSize; i< coreSize; i++){
						newThread();
					}
					System.out.println("increment to coreSize");
					//continue的目的在于不想让线程的扩容直接达到maxSize
					continue;
				}
				//当前的队列中有任务尚未处理，并且activeCount<maxSize则继续扩容
				if(runnableQueue.size() > 0 && activeCount < maxSize){
					for(int i=coreSize; i< maxSize; i++){
						newThread();
					}
					System.out.println("increment to maxSize");
				}
				//如果任务队列中没有任务，则需要回收，回收至coreSize即可
				if(runnableQueue.size() == 0 && activeCount > coreSize){
					for(int i=coreSize; i< activeCount; i++){
						removeThread();
					}
					System.out.println("decrement to coreSize");
				}
			}
		}
	}
	
	/**
	 * 从线程池中移除某个线程
	 */
	private void removeThread(){
		ThreadTask threadTask = threadQueue.remove();
		threadTask.internalTask.stop();
		activeCount--;
	}
	
	@Override
	public void execute(Runnable runnable) {
		if(this.isShutdown){
			throw new IllegalStateException("the thread pool us destroy");
		}
		//提交任务只是简单的往任务队列中插入Runnable
		runnableQueue.offer(runnable);
	}

	@Override
	//主要是为了停止BasicThreadPool线程，停止线程池中的活动线程并且将isShutdown开关变量更改为true
	public void shutdown() {
		synchronized (this) {
			if(isShutdown)
				return;
			isShutdown = true;
			threadQueue.forEach(threadTask -> {
				threadTask.internalTask.stop();
				threadTask.thread.interrupt();
			});
			this.interrupt();
		}
	}

	@Override
	public int getInitSize() {
		if(isShutdown){
			throw new IllegalStateException("the thread pool is destory");
		}
		return initSize;
	}

	@Override
	public int getMaxSize() {
		if(isShutdown){
			throw new IllegalStateException("the thread pool is destory");
		}
		return maxSize;
	}

	@Override
	public int getCoreSize() {
		if(isShutdown){
			throw new IllegalStateException("the thread pool is destory");
		}
		return coreSize;
	}

	@Override
	public int getQueueSize() {
		if(isShutdown){
			throw new IllegalStateException("the thread pool is destory");
		}
		return runnableQueue.size();
	}

	@Override
	public int getActiveCount() {
		synchronized (this) {
			return activeCount;
		}
	}

	@Override
	public boolean isShutdown() {
		return isShutdown;
	}
	
	private static class DefaultThreadFactory implements ThreadFactory {

		private static final AtomicInteger GROUP_COUNTER = new AtomicInteger(1);
		private static final ThreadGroup group = new ThreadGroup("MyThreadPool-" + GROUP_COUNTER.getAndIncrement());
		private static final AtomicInteger COUNTER = new AtomicInteger(0);
		
		@Override
		public Thread createThread(Runnable runnable) {
			return new Thread(group, runnable, "thread-pool-" + COUNTER.getAndIncrement());
		}
		
	}
	
	/**ThreadTask只是InternalTask和Thread的一个组合
	 * @author Administrator
	 *
	 */
	private static class ThreadTask{
		Thread thread;
		InternalTask internalTask;
		public ThreadTask(Thread thread, InternalTask internalTask) {
			super();
			this.thread = thread;
			this.internalTask = internalTask;
		}
	}

}
