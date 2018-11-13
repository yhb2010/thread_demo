package com.cdeledu.thread2.c3concurrent.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//newFixedThreadPool固定线程数量的线程池，当有一个新任务提交时，线程池中若有空闲线程，则立即执行。如没有，则新的我任务会被暂存在一个任务队列中，待有线程空闲时，便处理在任务队列中的任务
//newSingleThreadExecutor返回只有一个线程的线程池，若多于一个任务被提交到该线程池，任务会被保存在一个任务队列中，待线程空闲，按先入先出的顺序执行队列中的任务。
//newCachedThreadPool该方法返回一个可根据实际情况调整线程数量的线程池，线程池的数量不确定，但若有空闲线程可以复用，则优先使用可复用的线程，若所有线程均在工作，又有新任务提交，则会创建新线程处理任务，所有线程在当前任务执行完毕后，将返回线程池进行复用。
//newSingleThreadScheduleExecutor线程池大小为1，在给定时间执行某任务，如在某个固定的延时之后执行，或周期性执行某个任务。
//newScheduleThreadPool可以指定线程数量的执行任务。
public class Fixed {

	public static class MyTask implements Runnable{
		@Override
		public void run() {
			System.out.println(System.currentTimeMillis() + ":Thread ID:" + Thread.currentThread().getId());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		MyTask task = new MyTask();
		ExecutorService exec = Executors.newFixedThreadPool(5);
		for(int i=0;i<10;i++){
			exec.submit(task);
		}
	}

	/**
	 * 1529130183187:Thread ID:10
		1529130183188:Thread ID:12
		1529130183188:Thread ID:13
		1529130183188:Thread ID:14
		1529130183188:Thread ID:11
		1529130184187:Thread ID:10
		1529130184188:Thread ID:13
		1529130184188:Thread ID:11
		1529130184188:Thread ID:14
		1529130184188:Thread ID:12
		总是10,11,12,13,14这5个线程
	 * */

}
