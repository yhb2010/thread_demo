package com.cdeledu.thread2.c3concurrent;

import java.util.concurrent.locks.LockSupport;

//LockSupport可以响应中断，但不会抛出异常，可以用interrupted等方法获得中断标记
public class LockSupportDemo2 {

	public static Object u = new Object();
	static ChangeObjectThread t1 = new ChangeObjectThread("t1");
	static ChangeObjectThread t2 = new ChangeObjectThread("t2");

	public static class ChangeObjectThread extends Thread{
		public ChangeObjectThread(String name){
			super.setName(name);
		}
		@Override
		public void run(){
			System.out.println("in " + getName());
			LockSupport.park();
			if(Thread.interrupted()){
				System.out.println(getName() + "被中断了！");
			}
			System.out.println(getName() + "结束");
		}
	}

	public static void main(String[] args) throws InterruptedException {
		t1.start();
		Thread.sleep(100);
		t2.start();
		t1.interrupt();
		LockSupport.unpark(t2);
	}

}
