package com.cdeledu.thread.imooc;

//interrupt方法不能停止线程
public class InterruptedDemo3 extends Thread {

	public static void main(String[] args) {
		InterruptedDemo3 thread = new InterruptedDemo3();
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
		//如果线程进入阻塞状态，比如sleep，此时调用interrupt，那么线程的中断状态会被清除，同时抛出InterruptedException异常，
		//所以下面代码线程不会正常停止。
		while(!this.isInterrupted()){
			System.out.println("Thread is running...");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}