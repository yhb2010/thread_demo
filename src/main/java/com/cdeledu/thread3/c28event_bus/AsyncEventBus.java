package com.cdeledu.thread3.c28event_bus;

import java.util.concurrent.ThreadPoolExecutor;

/**异步的EventBus，重写了EventBus的构造函数，使用ThreadPoolExecutor替代Executor
 * @author Administrator
 *
 */
public class AsyncEventBus extends EventBus {
	
	AsyncEventBus(String busName, EventExceptionHandler exceptionHandler, ThreadPoolExecutor executor){
		super(busName, exceptionHandler, executor);
	}
	
	public AsyncEventBus(String busName, ThreadPoolExecutor executor){
		this(busName, null, executor);
	}
	
	public AsyncEventBus(ThreadPoolExecutor executor){
		this("default-async", null, executor);
	}
	
	public AsyncEventBus(EventExceptionHandler exceptionHandler, ThreadPoolExecutor executor){
		this("default-async", exceptionHandler, executor);
	}

}
