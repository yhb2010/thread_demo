package com.cdeledu.thread3.c5notify.producer;

import java.util.concurrent.TimeUnit;

public class EventClient {

	public static void main(String[] args) {
		final EventQueue queue = new EventQueue();
		
		new Thread(() -> {
			int index = 1;
			for(;;){
				queue.offer(new EventQueue.Event(index++));
			}
		}, "Producer").start();
		
		//10个消费者一起消费
		for(int i = 0; i < 20; i++){
			new Thread(() -> {
				for(;;){
					queue.take();
					try{
						TimeUnit.MICROSECONDS.sleep(10);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}, "Consumer").start();
		}
	}
	
}
