package com.cdeledu.thread3.c4.monitor;

import java.util.concurrent.TimeUnit;

public class ClassMonitor {
	
	public static synchronized void m1(){
		System.out.println(Thread.currentThread().getName() + " enter to m1");
		try {
			TimeUnit.SECONDS.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static synchronized void m2(){
		System.out.println(Thread.currentThread().getName() + " enter to m1");
		try {
			TimeUnit.SECONDS.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	//与m2效果一样
	public void m3(){
		synchronized(ClassMonitor.class){
			System.out.println(Thread.currentThread().getName() + " enter to m1");
			try {
				TimeUnit.SECONDS.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	//用synchronized同步某个类的不同静态方法争抢的也是同一个monitor的lock。该monitor关联的引用是ClassMonitor.class实例
	public static void main(String[] args) {
		ClassMonitor th =new ClassMonitor();
		new Thread(ClassMonitor::m1, "T1").start();
		new Thread(ClassMonitor::m2, "T2").start();
	}

}
