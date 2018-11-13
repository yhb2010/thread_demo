package com.cdeledu.thread.chapter4;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.TimeUnit;

import com.cdeledu.thread.SleepUtils;

public class Interrupted {

	public static void main(String[] args) throws InterruptedException {
		//sleepThread不停地尝试休眠
		Thread sleepThread = new Thread(new SleepRunner(), "sleepThread");
		sleepThread.setDaemon(true);
		//busyThread不停地运行
		Thread busyRunner = new Thread(new BusyRunner(), "busyRunner");
		busyRunner.setDaemon(true);
		sleepThread.start();
		busyRunner.start();
		//休眠5秒，让sleepThread和busyThread充分运行
		TimeUnit.SECONDS.sleep(5);
		sleepThread.interrupt();
		busyRunner.interrupt();
		//可以看出，抛出InterruptedException的线程SleepRunner，其中断标识位被清除了，而一直忙碌运作的线程BusyRunner，中断标识位没有被清除。
		System.out.println("3、SleepRunner interrupt is " + sleepThread.isInterrupted());
		System.out.println("3、busyRunner interrupt is " + busyRunner.isInterrupted());
		//防止sleepThread和busyThread立刻退出
		SleepUtils.second(2);
	}

	//一直休眠
	static class SleepRunner implements Runnable {

		@Override
		public void run() {
			while(true){
				SleepUtils.second(10);
			}
		}

	}

	//一直运行
	static class BusyRunner implements Runnable {

		@Override
		public void run() {
			while(true){
			}
		}

	}

}
