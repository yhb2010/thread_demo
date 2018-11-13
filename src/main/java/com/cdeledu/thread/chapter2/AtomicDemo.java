package com.cdeledu.thread.chapter2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicDemo {

	private AtomicInteger atomicI = new AtomicInteger(0);
	private AtomicInteger atomicI2 = new AtomicInteger(0);
	private int i = 0;
	private int i2 = 0;

	public static void main(String[] args) {
		final AtomicDemo cas = new AtomicDemo();
		List<Thread> ts = new ArrayList<>(600);
		long start = System.currentTimeMillis();
		for(int j = 0; j<100; j++){
			Thread t = new Thread(new Runnable() {

				@Override
				public void run() {
					for(int i=0; i<10000; i++){
						cas.count();
						cas.safeCount();
						cas.safeCount2();
					}
				}
			});
			ts.add(t);
		}

		for (Thread t : ts) {
			t.start();
		}
		//等待所有线程都执行完
		for (Thread t : ts) {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		System.out.println(cas.i);
		System.out.println(cas.atomicI.get());
		System.out.println(cas.i2);
		System.out.println(System.currentTimeMillis() - start);

	}

	protected void safeCount2() {
		i2 = atomicI2.incrementAndGet();
	}

	protected void safeCount() {
		for(;;){
			int i = atomicI.get();
			boolean suc = atomicI.compareAndSet(i, ++i);
			if(suc){
				break;
			}
		}
	}

	protected void count() {
		i++;
	}

}
