package com.cdeledu.thread3.c7.uncaughtException;
import java.util.concurrent.TimeUnit;


//线程在执行单元中是不允许抛出checked异常的，而且线程运行在自己的上下文中，派生出它的线程将无法直接获得他运行中出现的异常信息，对此，java为我们提供了一个UncaughtExceptionHandler
//接口，当线程在运行过程中出现异常时，会回调这个接口，从而得知哪个线程在运行时出现异常，以及出现了什么错误。
public class CaptureThreadException {
	
	public static void main(String[] args) {
		Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
			System.out.println(t.getName() + " occur exception.");
			e.printStackTrace();
		});
		final Thread t = new Thread(() -> {
			try{
				TimeUnit.SECONDS.sleep(2);	
			}catch(Exception e){
				
			}
			//这里会抛出unchecked异常
			System.out.println(1/0);
		}, "t1");
		t.start();
	}

}
