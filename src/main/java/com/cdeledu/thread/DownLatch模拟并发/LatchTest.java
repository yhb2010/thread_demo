package com.cdeledu.thread.DownLatch模拟并发;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class LatchTest {

	public static void main(String[] args) throws InterruptedException {
		LatchTest latchTest = new LatchTest();
        latchTest.startTaskAllInOnce(5, new WorkTask());
	}

	public long startTaskAllInOnce(final int threadNums, final Runnable task) throws InterruptedException {
		final CountDownLatch startGate = new CountDownLatch(1);
		final CountDownLatch endGate = new CountDownLatch(threadNums);
		for(int i = 0; i < threadNums; i++) {
			Thread t = new Thread(() -> {
				try {
					// 使线程在此等待，当开始门打开时，一起涌入门中
					startGate.await();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
                    task.run();
                } finally {
                    // 将结束门减1，减到0时，就可以开启结束门了
                    endGate.countDown();
                }
			});
			t.start();
		}
		long startTime = System.nanoTime();
		//TimeUnit.SECONDS.sleep(1);
		System.out.println(startTime + " [" + Thread.currentThread() + "] All thread is ready, concurrent going...");
		// 因开启门只需一个开关，所以立马就开启开始门
        startGate.countDown();
        // 等等结束门开启
        endGate.await();
        long endTime = System.nanoTime();
        System.out.println(endTime + " [" + Thread.currentThread() + "] All thread is completed.");
        System.out.println(((WorkTask)task).getiCounter());
        return endTime - startTime;
	}

}
