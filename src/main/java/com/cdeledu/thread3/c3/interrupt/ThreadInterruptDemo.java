package com.cdeledu.thread3.c3.interrupt;
import java.util.concurrent.TimeUnit;


public class ThreadInterruptDemo {
	
	//如果一个线程在没有执行可中断方法之前就被打断，那么接下来将执行可中断方法会发生什么？可中断方法一执行会立即中断
	public static void main(String[] args) throws InterruptedException {
		//判断当前线程是否被中断
		System.out.printf("Thread is interrupted ? %s\n", Thread.interrupted());
		//中断当前线程
		Thread.currentThread().interrupt();
		//判断当前线程是否已经被中断
		System.out.printf("Thread is interrupted ? %s\n", Thread.currentThread().isInterrupted());
		
		try{
			//当前线程执行可中断方法
			TimeUnit.MINUTES.sleep(1);
		}catch(Exception e){
			//捕获中断信号
			System.out.println("i will be interrupted still");
		}
	}

}
