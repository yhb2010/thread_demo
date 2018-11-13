package com.cdeledu.thread2.c5.sort;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ShellSortParallel {

	static int[] arr = {1, 4, 2, 5, 6, 3};

	// //////////////希尔算法的并行程序
	static ExecutorService pool = Executors.newCachedThreadPool();

	public static class ShellSortTask implements Runnable {
		int i = 0;
		int h = 0;
		CountDownLatch l;

		public ShellSortTask(int i, int h, CountDownLatch lathc) {
			this.i = i;
			this.h = h;
			this.l = lathc;
		}

		public void run() {
			if (arr[i] < arr[i - h]) {
				int tmp = arr[i];
				int j = i - h;
				while (j >= 0 && arr[j] > tmp) {
					arr[j + h] = arr[j];
					j -= h;
				}
				arr[j + h] = tmp;
			}
			l.countDown();
		}
	}

	public static void pShellSort(int[] arr) throws InterruptedException {
		// 计算出最大的n值
		int h = 1;
		CountDownLatch lathc = null;
		while (h <= arr.length / 3) {
			h = h * 3 + 1;
		}
		while (h > 0) {
			System.out.println("h=" + h);
			if (h >= 4) {
				lathc = new CountDownLatch(arr.length - h);
			}
			for (int i = h; i < arr.length; i++) {
				// 控制线程数量
				if (h >= 4) {
					pool.submit(new ShellSortTask(i, h, lathc));
				} else {
					if (arr[i] < arr[i - h]) {
						int tmp = arr[i];
						int j = i - h;
						while (j >= 0 && arr[j] > tmp) {
							arr[j + h] = arr[j];
							j -= h;
						}
						arr[j + h] = tmp;
					}

					System.out.println(Arrays.toString(arr));
				}
			}
			// 等线程排序完成,进入下一次排序
			lathc.await();
			// 计算下一个h值
			h = (h - 1) / 3;
		}

	}

	public static void main(String[] args) throws InterruptedException {
		System.out.println(Arrays.toString(arr));
		pShellSort(arr);
	}

}