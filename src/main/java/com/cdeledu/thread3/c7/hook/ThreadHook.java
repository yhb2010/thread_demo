package com.cdeledu.thread3.c7.hook;

import java.util.concurrent.TimeUnit;

//jvm进程的退出是由于jvm进程中没有活跃的非守护线程，或者收到了系统中断信号，向jvm程序注入了一个hook线程，在jvm进程退出的时候，hook线程会启动执行，通过Runtime可以为jvm注入多个hook线程。
public class ThreadHook {

	public static void main(String[] args) {
		//为应用程序注入钩子线程
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run(){
				try{
					System.out.println("The hook thread 1 is running.");
					TimeUnit.SECONDS.sleep(1);
				}catch(InterruptedException e){
					e.printStackTrace();
				}
				System.out.println("The hook thread 1 will exit.");
			}
		});
		
		//钩子线程可注册多个
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run(){
				try{
					System.out.println("The hook thread 2 is running.");
					TimeUnit.SECONDS.sleep(1);
				}catch(InterruptedException e){
					e.printStackTrace();
				}
				System.out.println("The hook thread 2 will exit.");
			}
		});
		
		//给java程序注入了两个钩子线程，在main线程结束后，也就意味着jvm中没有了活动的非守护线程，jvm进程即将结束，两个hook线程会被启动并且运行。
		System.out.println("the program will is stopping.");
	}
	
}
