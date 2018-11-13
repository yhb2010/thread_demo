package com.cdeledu.thread3.c3.interrupt;
import java.util.concurrent.TimeUnit;


public class ThreadInterrupt {
	
	//调用下面的方法会使线程阻塞：
	//Object的wait方法
	//Object的wait(long)方法
	//Object的wait(long, int)方法
	//Thread的sleep(long)方法
	//Thread的sleep(long, int)方法
	//Thread的join()方法
	//Thread的join(long)方法
	//Thread的join(long, int)方法
	//InterruptibleChannel的io操作
	//Selector的wakeup方法
	//上述方法都可以使线程进入阻塞状态，若另外一个线程调用被阻塞线程的interrupt方法，则会打断这种阻塞，因此这种方法有时会被称为可中断方法，打断一个线程并不等于该线程的生命周期结束，仅仅是打断了当前线程的阻塞状态
	//一旦线程在阻塞状态下被打断，都会抛出一个称为InterruptedException的异常。
	public static void main(String[] args) throws InterruptedException {
		Thread t = new Thread(() -> {
			try{
				TimeUnit.MINUTES.sleep(1);
			}catch(Exception e){
				System.out.println("oh, i am be interrupted.");
			}
		});
		t.start();
		// 确保线程已经启动
		TimeUnit.MICROSECONDS.sleep(2);
		t.interrupt();
	}

}
