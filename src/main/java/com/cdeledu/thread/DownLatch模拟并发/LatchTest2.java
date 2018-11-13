package com.cdeledu.thread.DownLatch模拟并发;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

public class LatchTest2 {

	public static void main(String[] args) throws InterruptedException {
		LatchTest2 latchTest = new LatchTest2();
        latchTest.startNThreadsByBarrier(5, new WorkTask());
	}

	private long startNThreadsByBarrier(int threadNums, Runnable task) throws InterruptedException {
		// 设置栅栏解除时的动作，比如初始化某些值
        CyclicBarrier barrier = new CyclicBarrier(threadNums, new Runnable() {
			@Override
			public void run() {
				System.out.println("All thread is ready, concurrent going...");
			}
		});
        final CountDownLatch endGate = new CountDownLatch(threadNums);
		for(int i = 0; i < threadNums; i++) {
			new Thread(new CounterTask(barrier, task, endGate)).start();
		}
		long startTime = System.nanoTime();
        // 等等结束门开启
        endGate.await();
        long endTime = System.nanoTime();
        System.out.println(endTime + " [" + Thread.currentThread() + "] All thread is completed.");
        System.out.println(((WorkTask)task).getiCounter());
        return endTime - startTime;
	}

}

class CounterTask implements Runnable {

	// 传入栅栏，一般考虑更优雅方式
	private CyclicBarrier barrier;
	private Runnable task;
	private CountDownLatch endGate;

	public CounterTask(final CyclicBarrier barrier, final Runnable task, final CountDownLatch endGate) {
		this.barrier = barrier;
		this.task = task;
		this.endGate = endGate;
	}

	public void run() {
		System.out.println(Thread.currentThread().getName() + " - " + System.currentTimeMillis() + " is ready...");
		try {
			// 设置栅栏，使在此等待，到达位置的线程达到要求即可开启大门
			TimeUnit.SECONDS.sleep(1);
			barrier.await();
			try {
                task.run();
            } finally {
                // 将结束门减1，减到0时，就可以开启结束门了
                endGate.countDown();
            }
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			e.printStackTrace();
		}
	}

}