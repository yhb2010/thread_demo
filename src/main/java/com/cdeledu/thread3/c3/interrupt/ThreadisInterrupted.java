package com.cdeledu.thread3.c3.interrupt;

import java.util.concurrent.TimeUnit;

public class ThreadisInterrupted {

	//isInterrupted是Thread的一个成员方法，它主要判断当前线程是否被中断，该方法仅仅是对interrupt标识的一个判断，并不会影响标识发生任何改变。
	public static void main(String[] args) throws InterruptedException {
		Thread t = new Thread(){
			@Override
			public void run(){
				while(true){
					//do nothing, just empty loop
				}
			}
		};
		
		t.setDaemon(true);
		t.start();
		TimeUnit.MICROSECONDS.sleep(2);
		System.out.printf("Thread is interrupted ? %s\n", t.isInterrupted());
		t.interrupt();
		System.out.printf("Thread is interrupted ? %s\n", t.isInterrupted());
	}
	
}
