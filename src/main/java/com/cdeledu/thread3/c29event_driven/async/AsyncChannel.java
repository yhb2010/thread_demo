package com.cdeledu.thread3.c29event_driven.async;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.cdeledu.thread3.c29event_driven.sync.Channel;
import com.cdeledu.thread3.c29event_driven.sync.Event;

public abstract class AsyncChannel implements Channel<Event> {
	
	//在AsyncChannel中使用ExecutorService多线程的方式提交给Message
	private final ExecutorService executorService;
	
	public AsyncChannel(){
		this(Executors.newFixedThreadPool(2));
	}

	public AsyncChannel(ExecutorService executorService) {
		this.executorService = executorService;
	}
	
	//重写dispatch方法，并且用final修饰，避免子类重写
	@Override
	public final void dispatch(Event message){
		executorService.submit(() -> handle(message));
	}

	protected abstract void handle(Event message);

	//提供关闭ExecutorService的方法
	void stop(){
		if(null != executorService && !executorService.isShutdown()){
			executorService.shutdown();
		}
	}
	
}
