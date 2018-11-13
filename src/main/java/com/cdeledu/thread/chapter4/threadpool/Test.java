package com.cdeledu.thread.chapter4.threadpool;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Test {

	//main线程将会等待所有ConnectionRunner结束后才能继续执行
	static CountDownLatch end;

	public static void main(String[] args) throws InterruptedException {
		end = new CountDownLatch(10);
		ThreadPool<MyJob> excutor = new DefaultThreadPool<>(3);
		for (int i = 0; i < 10; i++) {
			excutor.execute(new MyJob("任务" + i));
		}
		end.await();
		System.out.println("全部执行完");
		TimeUnit.SECONDS.sleep(3);
		excutor.shutdown();
		System.out.println("结束");
	}

	static class MyJob implements Runnable {
		private String name;
		public MyJob(String name){
			this.name = name;
		}
		@Override
		public void run() {
			System.out.println("线程 " + Thread.currentThread().getName() + " 在帮" + name + "干活");
			end.countDown();
		}
	}

}
