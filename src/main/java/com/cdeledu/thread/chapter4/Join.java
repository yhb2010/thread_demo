package com.cdeledu.thread.chapter4;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.util.concurrent.TimeUnit;

//如果一个线程a执行了thread.join()方法，其含义是：当前线程a等待thread线程终止之后才从thrad.join()返回。线程Thread除了提供join方法外，还提供了具备超时时间
//的方法，这两个超时方法表示：如果线程thread在给定的超时时间里没有终止，那么将会从该超时方法中返回。
//当线程终止时，会调用线程自身的notifyAll方法，会通知所有等待在该线程对象上的线程，可以看到join方法的逻辑结构与433节描述的等待/通知经典范式一致，即加锁、循环和处理逻辑三个步骤。
public class Join {

	public static void main(String[] args) throws IOException, InterruptedException {
		Thread previous = Thread.currentThread();
		for(int i = 0; i<10; i++){
			//每个线程拥有前一个线程的引用，需要等待前一个线程终止，才能从等待中返回
			Thread thread = new Thread(new Domino(previous), String.valueOf(i));
			thread.start();
			previous = thread;
		}
		TimeUnit.SECONDS.sleep(2);
		System.out.println(Thread.currentThread().getName() + " terminate.");
	}

	static class Domino implements Runnable{

		private Thread thread;

		public Domino(Thread thread){
			this.thread = thread;
		}

		@Override
		public void run() {
			try{
				System.out.println(Thread.currentThread().getName() + " start.");
				thread.join();
				TimeUnit.SECONDS.sleep(1);
			}catch(Exception e){

			}
			System.out.println(Thread.currentThread().getName() + " terminate.");
		}

	}

}
