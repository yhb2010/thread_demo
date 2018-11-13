package com.cdeledu.thread2.c3concurrent;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

//和CountDownLatch差不多，但比他更加强大复杂。
//首先用来阻止线程执行，要求线程在栅栏处等待。前面的Cyclic意为循环，也就是说这个计数器可以反复使用。比如，假设我们将计数器设置为10，
//那么凑齐第一批10个线程后，计数器归零，然后接着凑齐下一批10个线程，这就是循环栅栏的含义。
public class CyclicBarrierDemo {

	//演示司令命令士兵完成任务的场景
	public static class Soldier implements Runnable{
		private String soldier;
		private final CyclicBarrier cyclic;
		public Soldier(CyclicBarrier cyclic, String soldier) {
			this.cyclic = cyclic;
			this.soldier = soldier;
		}
		@Override
		public void run() {
			try{
				//等待所有士兵到齐
				cyclic.await();
				doWork();
				//等待所有士兵完成任务
				cyclic.await();
			}catch(InterruptedException e){
				e.printStackTrace();
			}catch (BrokenBarrierException e) {
				e.printStackTrace();
			}
		}
		private void doWork() {
			try{
				Thread.sleep(Math.abs(new Random().nextInt()%10000));
			}catch(Exception e){
				e.printStackTrace();
			}
			System.out.println(soldier + ":任务完成");
		}
	}

	public static class BarrierRun implements Runnable{
		boolean flag;
		int N;
		public BarrierRun(boolean flag, int N){
			this.flag = flag;
			this.N = N;
		}
		@Override
		public void run() {
			if(flag){
				System.out.println("司令：[士兵" + N + "个，任务完成！]");
			}else{
				System.out.println("司令：[士兵" + N + "个，集合完毕！]");
				flag = true;
			}
		}
	}

	public static void main(String[] args) {
		final int N = 10;
		Thread[] all = new Thread[N];
		boolean flag = false;
		//创建了CyclicBarrier实例，并将计数器设置为10，并要求在计数器达到指标时，执行BarrierRun的run方法。
		//每一个士兵线程会执行Soldier的run方法，在第23行每一个士兵线程都会等待，直到所有士兵都集合完毕。集合完毕后，意味着CyclicBarrier的一次计数完成，
		//当再一次调用await时，会进行下一次计数。这次计数的目的是监控是否所有士兵都完成了任务。一旦任务全部完成，会再次调用BarrierRun的run方法。
		CyclicBarrier cyclic = new CyclicBarrier(N, new BarrierRun(flag, N));
		//设置屏障点，主要是为了执行这个方法
		System.out.println("集合队伍！");
		for(int i=0; i<N; ++i){
			System.out.println("士兵" + i + "报道！");
			all[i] = new Thread(new Soldier(cyclic, "士兵" + i));
			all[i].start();
			//如果使得第5个士兵中断线程，那么我们可能会得到一个InterruptedException和BrokenBarrierException异常。
			//这个InterruptedException是被中断线程抛出的，而其他9个BrokenBarrierException是由等待在当前CyclicBarrier上的线程抛出的，这个异常可以避免其他9个线程进行永久的、无谓的等待。
			//if(i == 5){
				//all[i].interrupt();
			//}
		}
	}

}
