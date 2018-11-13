package com.cdeledu.thread2.c5.stream;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

//加法运算
public class Plus implements Runnable {

	public static BlockingQueue<Msg> q = new LinkedBlockingDeque<Msg>();

	@Override
	public void run() {
		while(true){
			try{
				Msg msg = q.take();
				msg.j = msg.i+msg.j;
				Multiply.q.add(msg);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}

}
