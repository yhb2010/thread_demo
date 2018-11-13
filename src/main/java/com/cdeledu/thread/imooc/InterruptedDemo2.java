package com.cdeledu.thread.imooc;

//interrupt方法不能停止线程
public class InterruptedDemo2 extends Thread {

	public static void main(String[] args) {
		InterruptedDemo2 thread = new InterruptedDemo2();
		System.out.println("starting thread...");
		thread.start();

		try{
			Thread.sleep(3000);
		}catch(InterruptedException e){
			e.printStackTrace();
		}

		System.out.println("interrupted thread...");
		//interrupted不会停止线程
		thread.interrupt();

		try{
			Thread.sleep(3000);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		System.out.println("stop application...");
	}

	public void run(){
		//while(true){
		//改为this.isInterrupted()判断，可以停止线程，this.isInterrupted()方法：如果线程被中断，返回true
		while(!this.isInterrupted()){
			System.out.println("Thread is running...");
			long time = System.currentTimeMillis();
			while((System.currentTimeMillis() - time < 1000)){
				//减少屏幕输出的空循环
			}
		}
	}

}