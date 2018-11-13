package com.cdeledu.thread.concurrent;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

//java.util.concurrent.CyclicBarrier 类是一种同步机制，它能够对处理一些算法的线程实现同步。换句话讲，它就是一个所有线程必须等待的一个栅栏，直到所有线程都到达这里，然后所有线程才可以继续做其他事情。
//通过调用 CyclicBarrier 对象的 await() 方法，两个线程可以实现互相等待。一旦 N 个线程在等待 CyclicBarrier 达成，所有线程将被释放掉去继续运行。
//当然，你也可以为等待线程设定一个超时时间。等待超过了超时时间之后，即便还没有达成 N 个线程等待 CyclicBarrier 的条件，该线程也会被释放出来。以下是定义超时时间示例：
//barrier.await(10, TimeUnit.SECONDS);
//满足以下任何条件都可以让等待 CyclicBarrier 的线程释放：
//最后一个线程也到达 CyclicBarrier(调用 await())
//当前线程被其他线程打断(其他线程调用了这个线程的 interrupt() 方法)
//其他等待栅栏的线程被打断
//其他等待栅栏的线程因超时而被释放
//外部线程调用了栅栏的 CyclicBarrier.reset() 方法
public class CyclicBarrierDemo {

	private static final int THREAD_NUM = 5;

	public static class WorkerThread implements Runnable {

		CyclicBarrier barrier;
		Long time = 0l;

		public WorkerThread(CyclicBarrier b, Long t) {
			this.barrier = b;
			this.time = t;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				System.out.println("Worker's waiting");
				Thread.sleep(2000);
				// 线程在这里等待，直到所有线程都到达barrier。
				if(time > 0l){
					barrier.await(time, TimeUnit.MILLISECONDS);
				}else{
					barrier.await();
				}
				System.out.println("ID:" + Thread.currentThread().getId() + " Working");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CyclicBarrier cb = new CyclicBarrier(THREAD_NUM, new Runnable() {
			// 当所有线程到达barrier时执行
			@Override
			public void run() {
				System.out.println("Inside Barrier");
			}
		});

		for (int i = 0; i < THREAD_NUM; i++) {
			if(i == 2){
				new Thread(new WorkerThread(cb, 50l)).start();
			}else{
				new Thread(new WorkerThread(cb, 0l)).start();
			}
		}
	}

}
