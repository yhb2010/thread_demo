package com.cdeledu.thread2.c4.producer;

import java.text.MessageFormat;
import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class Consumer implements Runnable {

	private BlockingQueue<PCData> queue;//内存缓冲区
	private static final int SLEEPTIME = 1000;

	public Consumer(BlockingQueue<PCData> queue) {
		this.queue = queue;
	}

	@Override
	public void run() {
		Random r = new Random();
		System.out.println("start consumer id=" + Thread.currentThread().getId());
		try{
			while(true){
				PCData data = queue.take();
				if(data != null){
					int re = data.getIntData() * data.getIntData();//计算平方
					System.out.println(MessageFormat.format("{0}*{1}={2}", data.getIntData(), data.getIntData(), re));
					Thread.sleep(r.nextInt(SLEEPTIME));
				}
			}
		}catch(InterruptedException e){
			e.printStackTrace();
			Thread.currentThread().interrupt();
		}
	}

}
