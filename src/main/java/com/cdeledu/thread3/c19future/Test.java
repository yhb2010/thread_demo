package com.cdeledu.thread3.c19future;

import java.util.concurrent.TimeUnit;

public class Test {
	
	public static void main(String[] args) throws InterruptedException {
		FutureService<Void, Void> fs = FutureService.newService();
		Future<?> f = fs.submit(() -> {
			try {
				TimeUnit.SECONDS.sleep(3);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		System.out.println("i am finish done.");
		//会使线程阻塞
		f.get();
		System.out.println("i am get result.");
		System.out.println("-----------------------------");
		FutureService<Integer, Integer> fs2 = FutureService.newService();
		Future<Integer> f2 = fs2.submit(num -> {
			try {
				TimeUnit.SECONDS.sleep(3);
				return num + num;
			} catch (Exception e) {
				e.printStackTrace();
				return 0;
			}
		}, 100);
		System.out.println("i am finish done.");
		//会使线程阻塞
		System.out.println("i am get result." + f2.get());
		System.out.println("-----------------------------");
		Future<Integer> f3 = fs2.submit(num -> {
			try {
				TimeUnit.SECONDS.sleep(3);
				return num + num;
			} catch (Exception e) {
				e.printStackTrace();
				return 0;
			}
		}, 100, result -> {
			System.out.println("i am get result." + result);
		});
		System.out.println("i am finish done.");
	}

}
