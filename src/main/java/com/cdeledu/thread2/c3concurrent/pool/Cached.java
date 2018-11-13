package com.cdeledu.thread2.c3concurrent.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//通过输出比较和Fixed的区别
public class Cached {

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

	public static void main(String[] args) throws InterruptedException {
		MyTask task = new MyTask();
		ExecutorService exec = Executors.newCachedThreadPool();
		for(int i=0;i<5;i++){
			exec.submit(task);
		}
		Thread.sleep(2000);
		for(int i=0;i<6;i++){
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
