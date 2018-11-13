package com.cdeledu.thread3.c15监控任务的生命周期;

public interface TaskLifecycle<T> {

	//任务启动时触发onStart方法
	void onStart(Thread thread);
	
	//任务正在运行时触发onRunning方法，不同于线程生命周期中的running状态，如果当前线程进入了休眠或者阻塞，那么任务都是running状态
	void onRunning(Thread thread);
	
	//任务结束时触发onFinish方法，其中result是执行结果
	void onFinish(Thread thread, T result);
	
	//任务报错时触发onError方法
	void onError(Thread thread, Exception e);

	//生命周期接口的空实现
	class EmptyLifecycle<T> implements TaskLifecycle<T>{

		@Override
		public void onStart(Thread thread) {
			
		}

		@Override
		public void onRunning(Thread thread) {
			
		}

		@Override
		public void onFinish(Thread thread, T result) {
			
		}

		@Override
		public void onError(Thread thread, Exception e) {
			
		}
		
	}
	
}
