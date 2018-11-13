package com.cdeledu.thread2.c3concurrent;

import java.util.concurrent.locks.LockSupport;

public class LockSupportDemo3 {

	public static void main(String[] args) {
		MyThread mt = new MyThread();
		mt.setName("mt");
		mt.start();
		try {
			Thread.currentThread().sleep(10);
			mt.park();
			Thread.currentThread().sleep(10000);
			//10秒之后线程开始执行
			mt.unPark();
			Thread.currentThread().sleep(10000);
			mt.park();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	static class MyThread extends Thread {

		private boolean isPark = false;

		public void run() {
			System.out.println("Enter Thread running.....");
			while (true) {
				if (isPark) {
					System.out.println(Thread.currentThread().getName() + "Thread is Park.....");
					LockSupport.park();
				}
				// do something
				System.out.println(Thread.currentThread().getName() + ">> is running");
				try {
					Thread.currentThread().sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		public void park() {
			isPark = true;
		}

		public void unPark() {
			isPark = false;
			LockSupport.unpark(this);
			System.out.println("Thread is unpark.....");
		}
	}

}
