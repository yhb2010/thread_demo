package com.cdeledu.thread3.c4.monitor;

import java.util.concurrent.TimeUnit;

public class ThisMonitor {
	
	public synchronized void m1(){
		System.out.println(Thread.currentThread().getName() + " enter to m1");
		try {
			TimeUnit.SECONDS.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void m2(){
		System.out.println(Thread.currentThread().getName() + " enter to m1");
		try {
			TimeUnit.SECONDS.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	//与m2效果一样
	public void m3(){
		synchronized(this){
			System.out.println(Thread.currentThread().getName() + " enter to m1");
			try {
				TimeUnit.SECONDS.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	//可见，使用synchronized关键字同步类的不同实例方法，争抢的是同一个monitor的lock，而与之关联的引用则是ThisMonitor的实例引用。
	public static void main(String[] args) {
		ThisMonitor th =new ThisMonitor();
		new Thread(th::m1, "T1").start();
		new Thread(th::m2, "T2").start();
	}

}
