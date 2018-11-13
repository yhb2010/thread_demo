package com.cdeledu.thread2.c4.threadlocal;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

//性能比较：针对Random
public class Demo3 {
	private static final int GEN_COUNT = 10000000;
	private static final int THREAD_COUNT = 4;
	private static ExecutorService exe = Executors.newFixedThreadPool(THREAD_COUNT);
	private static Random rnd = new Random(123);
	private static final ThreadLocal<Random> tRnd = new ThreadLocal<Random>(){
		@Override
		protected Random initialValue(){
			return new Random(123);
		}
	};

	public static class RndTask implements Callable<Long>{
		//mod=0多线程共享一个rnd
		//mod=1多线程个分配一个rnd
		private int mod=0;
		public RndTask(int mod) {
			this.mod = mod;
		}
		public Random getRandom(){
			if(mod==0){
				return rnd;
			}else if(mod == 1){
				return tRnd.get();
			}else{
				return null;
			}
		}
		@Override
		public Long call() {
			long b = System.currentTimeMillis();
			for(long i=0;i<GEN_COUNT;i++){
				getRandom().nextInt();
			}
			long e = System.currentTimeMillis();
			System.out.println(Thread.currentThread().getName() + " spend " + (e-b) + " ms");
			return e-b;
		}
	}

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		Future<Long>[] futs = new Future[THREAD_COUNT];
		for(int i=0;i<THREAD_COUNT;i++){
			futs[i] = exe.submit(new RndTask(0));
		}
		long totaltime = 0;
		for(int i=0;i<THREAD_COUNT;i++){
			totaltime += futs[i].get();
		}
		System.out.println("多线程访问同一个Random实例：" + totaltime);

		for(int i=0;i<THREAD_COUNT;i++){
			futs[i] = exe.submit(new RndTask(1));
		}
		totaltime = 0;
		for(int i=0;i<THREAD_COUNT;i++){
			totaltime += futs[i].get();
		}
		System.out.println("使用ThreadLocal包装Random实例：" + totaltime);
	}

}
