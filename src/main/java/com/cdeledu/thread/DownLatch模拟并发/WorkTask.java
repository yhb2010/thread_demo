package com.cdeledu.thread.DownLatch模拟并发;

import java.util.concurrent.atomic.AtomicInteger;

public class WorkTask implements Runnable {

	private AtomicInteger iCounter = new AtomicInteger(0);

	@Override
	public void run() {
		for (int i = 0; i < 10; i++) {
			// 发起业务请求...
			iCounter.incrementAndGet();
			System.out.println(System.nanoTime() + " [" + Thread.currentThread().getName() + "] iCounter = " + iCounter.get());
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public int getiCounter() {
		return iCounter.get();
	}

}
