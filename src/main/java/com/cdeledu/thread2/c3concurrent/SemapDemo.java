package com.cdeledu.thread2.c3concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**信号量为多线程协作提供了更为强大的控制方法，它可以指定多个线程，同时访问某一个资源。
 * acquire：尝试获得一个准入的许可，若无法获得，则线程等待，直到有线程释放一个许可或者当前线程被中断。
 * acquireUninterruptibly：和acquire一样，但不响应中断。
 * tryAcquire：尝试获得一个许可，如果成功返回true，失败返回false，不会进行等待，立刻返回
 * tryAcquire(timeout, timeunit)
 * release：用于在线程访问资源后，释放一个许可，使其他等待许可的线程可以进行资源访问
 * @author DELL
 *
 */
public class SemapDemo implements Runnable {

	final Semaphore semp = new Semaphore(5);

	@Override
	public void run() {
		try{
			semp.acquire();
			//模拟一个耗时操作
			Thread.sleep(1000);
			System.out.println(Thread.currentThread().getId() + ": done!");
			semp.release();
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}

	/**申明了一个包含5个线程的信号量，意味着同时可以有5个线程进入代码段。申请信号量使用acquire操作，在离开时，务必使用release释放信号量。
	 * 这就和释放锁是一个道理，如果不幸发生了信号量的泄露，那么可以进入临界区的线程数量就会越来越少，直到所有线程均不可访问。
	 * @param args
	 */
	public static void main(String[] args) {
		ExecutorService exec = Executors.newFixedThreadPool(20);
		final SemapDemo demo = new SemapDemo();
		for(int i=0; i<20; i++){
			exec.submit(demo);
		}
		System.out.println("结束");
	}

}
