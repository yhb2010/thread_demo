package com.cdeledu.thread2.c5.stream;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

//乘法运算
public class Multiply implements Runnable {

	public static BlockingQueue<Msg> q = new LinkedBlockingDeque<Msg>();

	@Override
	public void run() {
		while(true){
			try{
				Msg msg = q.take();
				msg.i = msg.i*msg.j;
				Div.q.add(msg);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}

}
