package com.cdeledu.thread.concurrent;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;

public class Producer2 implements Runnable {
	private BlockingQueue<String> queue;
	private String produce;

	public Producer2(BlockingQueue<String> queue, String produce) {
		this.queue = queue;
		if (null != produce)
			this.produce = produce;
		else
			this.produce = "null ";
	}

	@Override
	public void run() {
		String uuid = UUID.randomUUID().toString();
		try {
			Thread.sleep(200);// 生产需要时间
			queue.put(produce + " : " + uuid);
			System.out.println("Produce \"" + produce + "\" : " + uuid + " " + Thread.currentThread());
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
	}
}