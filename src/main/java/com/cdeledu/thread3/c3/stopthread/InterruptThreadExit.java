package com.cdeledu.thread3.c3.stopthread;

import java.util.concurrent.TimeUnit;

//捕获中断信号：通过检查线程interrupt的标识来确定是否退出，如果在线程中执行某个可中断方法，则可以通过捕获中断信号来决定是否退出
public class InterruptThreadExit {

	public static void main(String[] args) throws InterruptedException {
		Thread t = new Thread(){
			@Override
			public void run(){
				System.out.println("i will start work");
				while(!isInterrupted()){
					//working
				}
				System.out.println("i will be exiting");
			}
		};
		t.start();
		TimeUnit.SECONDS.sleep(5);
		System.out.println("System will be shutdown");
		t.interrupt();
	}
	
}
