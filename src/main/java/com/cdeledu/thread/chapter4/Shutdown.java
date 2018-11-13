package com.cdeledu.thread.chapter4;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.TimeUnit;

import com.cdeledu.thread.SleepUtils;

//安全的终止线程
public class Shutdown {

	public static void main(String[] args) throws InterruptedException {
		Runner one = new Runner();
		Thread countThread = new Thread(one, "CountThread");
		countThread.start();
		//睡眠1秒，main线程对countThread进行中断，使countThread能够感知中断而结束
		TimeUnit.SECONDS.sleep(1);
		countThread.interrupt();

		Runner two = new Runner();
		countThread = new Thread(two, "CountThread");
		countThread.start();
		//睡眠1秒，main线程对countThread进行中断，使countThread能够感知中断而结束
		TimeUnit.SECONDS.sleep(1);
		two.cancel();
	}

	static class Runner implements Runnable {
		private long i;
		private volatile boolean on = true;

		@Override
		public void run() {
			while(on && !Thread.currentThread().isInterrupted()){
				i++;
			}
			System.out.println("Count i=" + i);
		}

		public void cancel(){
			on = false;
		}

	}

}
