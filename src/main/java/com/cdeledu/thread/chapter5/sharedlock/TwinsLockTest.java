package com.cdeledu.thread.chapter5.sharedlock;

import java.util.concurrent.locks.Lock;

import com.cdeledu.thread.SleepUtils;

/**
 * 这是一个共享锁例子，同一时刻至多两个线程同时访问，超过两个线程的访问将被阻塞。
 */
public class TwinsLockTest {

	public static void main(String[] args) {
		final Lock lock = new TwinsLock();
		class Worker extends Thread {
			public void run() {
				while (true) {
					lock.lock();
					try {
						SleepUtils.second(1);
						System.out.println(Thread.currentThread().getName());
						SleepUtils.second(1);
					} finally {
						lock.unlock();
					}
				}
			}
		}
		// 启动10个线程
		for (int i = 0; i < 10; i++) {
			Worker w = new Worker();
			w.setDaemon(true);
			w.start();
		}
		// 每隔1秒换行
		for (int i = 0; i < 10; i++) {
			SleepUtils.second(1);
			System.out.println();
		}
	}
}
