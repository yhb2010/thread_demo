package com.cdeledu.thread3.c3.interrupt;

import java.util.concurrent.TimeUnit;

public class ThreadInterrupted {

	//interrupted是一个静态方法，虽然也用于判断当前线程是否被中断，但是他和成员方法isInterrupted还是有很大区别的，调用该方法会直接擦除掉线程的interrupt标识，需要注意的是，
	//如果当前线程被打断了，那么第一次调用interrupted方法会返回true，并且立即擦除了interrupt标识，第二次包括以后的调用永远都会返回false，除非在此期间线程又一次被打断。
	//输出结果是很多false中会有两个true。
	public static void main(String[] args) throws InterruptedException {
		Thread t = new Thread(){
			@Override
			public void run(){
				while(true){
					System.out.println(Thread.interrupted());
				}
			}
		};
		
		t.setDaemon(true);
		t.start();
		TimeUnit.MICROSECONDS.sleep(2);
		t.interrupt();
		TimeUnit.MICROSECONDS.sleep(2);
		t.interrupt();
	}
	
}
