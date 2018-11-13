package com.cdeledu.thread2.c5.search;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

//并行查找算法
public class ParallelSearch {

	// 要找的数组
	static int[] arr = { 1, 2, 4, 5, 6, 3, 4, 5, 6, 7, 8 };

	// cachedThreadPool，按需创建线程池，有就用，没有就创建，过了一段时间还空闲就撤销线程
	static ExecutorService pool = Executors.newCachedThreadPool();
	static final int threadNum = 2;

	// 存放下标，表示要找的值在这个位置
	static AtomicInteger result = new AtomicInteger(-1);

	static class SearchTask implements Callable<Integer> {

		// 分别表示开始下标，结束下标，要搜索的值
		int begin, end, searchValue;

		// 构造方法
		SearchTask(int b, int e, int searchValue) {
			this.begin = b;
			this.end = e;
			this.searchValue = searchValue;
		}

		@Override
		public Integer call() throws Exception {
			return search(begin, end, searchValue);
		}

	}

	// 搜索任务要调用的搜索方法，这个方法是关键。
	public static int search(int b, int e, int val) {

		for (int i = b; i < e; i++) {
			if (result.get() >= 0) { // /已经找到结果了，无需再找，直接返回
				return result.get();
			}
			// 没有找到，找找看
			if (arr[i] == val) { // 找到了，利用CAS操作设置result的值
				if (!result.compareAndSet(-1, i)) { // 设置失败，表示原来的值已经不是-1，说明已经找到了一个结果
					return result.get();
				}
				return i; // 设置成功，返回i
			}
		}

		return -1;
	}

	//根据线程数对arr数组进行划分，并建立对应的任务提交给线程池进行处理
	public static int pSearch(int searchValue) throws InterruptedException, ExecutionException {
		int subArrSize = arr.length / threadNum + 1;
		List<Future<Integer>> re = new ArrayList<Future<Integer>>();
		for (int i = 0; i < arr.length; i += subArrSize) {
			int end = i + subArrSize;
			if (end >= arr.length)
				end = arr.length;
			re.add(pool.submit(new SearchTask(i, end, searchValue)));
		}
		for (Future<Integer> futrue : re) {
			if (futrue.get() >= 0) {
				return futrue.get();
			}
		}
		return -1;
	}

	public static void main(String[] args) {
		try {
			int res = pSearch(3);
			System.out.println(res);
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

}