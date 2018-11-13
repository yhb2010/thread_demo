package com.cdeledu.thread2.c3concurrent.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

//AbortPolicy：该策略会直接抛出异常，阻止系统正常工作
//CallerRunsPolicy：只要线程池未关闭，该策略直接在调用者线程中，运行当前被丢弃的任务。显然这样做不会真的丢弃任务，但是，任务提交线程的性能有可能下降
//DiscardOledestPolicy：该策略将丢弃最老的一个请求，也就是即将被执行的一个任务，并尝试再次提交当前任务
//DiscardPolicy：丢弃无法处理的任务，不予任何处理，如果允许任务丢失，可以使用这个策略
public class AbortPolicyDemo {

	//自定义线程池和拒绝策略
	public static class MyTask implements Runnable{
		@Override
		public void run() {
			System.out.println(System.currentTimeMillis()+":Thread ID:"+Thread.currentThread().getId());
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws InterruptedException {
		MyTask task = new MyTask();
		ExecutorService es = new ThreadPoolExecutor(5, 5, 0l, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>(10), Executors.defaultThreadFactory(), new RejectedExecutionHandler() {
			@Override
			//r为请求执行的任务，executor为当前的线程池
			public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
				//当拒绝的时候记日志
				System.out.println(r.toString() + " is discard");
			}
		});
		for(int i=0;i<Integer.MAX_VALUE;i++){
			es.submit(task);
			Thread.sleep(10);
		}
	}

}
