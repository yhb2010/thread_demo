package com.cdeledu.thread2.c5.sort;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//对于奇偶交换排序，他将排序过程分为两个阶段，奇交换和偶交换，对于奇交换来说，他总是比较奇数索引以及其相邻的后续元素，而偶交换总是比较偶数索引和其相邻的后续元素。并且，奇交换和偶交换会成对出现，这样才保证比较和交换涉及到数组中的每一个元素
//exchFlag用于记录当前迭代是否发生了数据交换，而start变量用来表示是计交换还是偶交换。初始时，start=0，表示偶交换，每次迭代结束后，切换start状态。如果上一次比较发生了数据交换，或者当前正在进行奇交换，循环就不会停止，直到程序不再发生交换，并且当前进行的是偶交换为止（表示奇偶交换成对出现）
public class OddEventSort {

	static int exchangeFlag = 1;
	static ExecutorService pool = Executors.newCachedThreadPool();
	static int[] array = { 1, 4, 2, 6, 35, 3 };

	static synchronized void setExchangeFlag(int v) {
		exchangeFlag = v;
	}

	static synchronized int getExchangeFlag() {
		return exchangeFlag;
	}

	public static class OddEventSortTask implements Runnable {
		int i;
		CountDownLatch latch;

		public OddEventSortTask(int i, CountDownLatch latch) {
			this.i = i;
			this.latch = latch;
		}

		public void run() {
			if (array[i] > array[i + 1]) {
				int temp = array[i];
				array[i] = array[i + 1];
				array[i + 1] = temp;
				setExchangeFlag(1);
			}
			latch.countDown();
		}
	}

	public static void pOddEventSort(int[] arr) throws InterruptedException {
		int start = 0;
		while (getExchangeFlag() == 1 || start == 1) {
			setExchangeFlag(0);
			// 偶数的数组长度,当start=1时候,只有len/2-1 个线程
			CountDownLatch latch = new CountDownLatch(arr.length / 2 - (arr.length % 2 == 0 ? start : 0));
			for (int i = start; i < arr.length; i += 2) {
				pool.submit(new OddEventSortTask(i, latch));
			}
			// 等待所有线程结束
			latch.await();
			if (start == 0) {
				start = 1;
			} else {
				start = 0;
			}
		}
	}

	public static void main(String[] args) throws InterruptedException {
		pOddEventSort(array);
		for (int ar : array) {
			System.out.println(ar);
		}
	}

}
