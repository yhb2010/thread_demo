package com.cdeledu.thread3.c19future;

import java.util.concurrent.atomic.AtomicInteger;

public class FutureServiceImpl<IN, OUT> implements FutureService<IN, OUT> {
	
	private final static String FUTURE_THREAD_PREFIX = "FUTURE-";
	private final AtomicInteger nextCounter = new AtomicInteger(0);
	
	private String getNextName(){
		return FUTURE_THREAD_PREFIX + nextCounter.getAndIncrement();
	}

	@Override
	public Future<?> submit(Runnable runnable) {
		FutureTask<OUT> future = new FutureTask<OUT>();
		Thread t = new Thread(() -> {
			runnable.run();
			//任务结束之后将null作为结果传给future
			future.finish(null);
		}, getNextName());
		t.start();
		return future;
	}

	@Override
	public Future<OUT> submit(Task<IN, OUT> task, IN input) {
		FutureTask<OUT> future = new FutureTask<OUT>();
		Thread t = new Thread(() -> {
			OUT result = task.get(input);
			//任务结束之后将result作为结果传给future
			future.finish(result);
		}, getNextName());
		t.start();
		return future;
	}

	@Override
	public Future<OUT> submit(Task<IN, OUT> task, IN input, Callback<OUT> callback) {
		FutureTask<OUT> future = new FutureTask<OUT>();
		Thread t = new Thread(() -> {
			OUT result = task.get(input);
			//任务结束之后将result作为结果传给future
			future.finish(result);
			if(callback != null){
				callback.call(result);
			}
		}, getNextName());
		t.start();
		return future;
	}

}
