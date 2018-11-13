package com.cdeledu.thread3.c3.interrupt;

import java.util.concurrent.TimeUnit;

public class ThreadisInterrupted2 {

	//可中断方法捕获到中断信号后，也就是捕获了interrupt异常之后会擦除interrupt的标识。对ThreadisInterrupted稍作修改，你会发现大不同。
	//由于在run方法中使用了sleep这个可中断方法，它会捕获到中断信号，并且会擦除interrupt标识，因此程序的执行结果都会是false。
	public static void main(String[] args) throws InterruptedException {
		Thread t = new Thread(){
			@Override
			public void run(){
				while(true){
					try {
						TimeUnit.MINUTES.sleep(1);
					} catch (InterruptedException e) {
						System.out.printf("i am be interrupted ? %s\n", isInterrupted());
					}
				}
			}
		};
		
		t.setDaemon(true);
		t.start();
		TimeUnit.MICROSECONDS.sleep(2);
		System.out.printf("Thread is interrupted ? %s\n", t.isInterrupted());
		t.interrupt();
		TimeUnit.MICROSECONDS.sleep(2);
		System.out.printf("Thread is interrupted ? %s\n", t.isInterrupted());
	}
	
}
