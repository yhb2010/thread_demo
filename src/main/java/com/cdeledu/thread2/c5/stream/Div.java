package com.cdeledu.thread2.c5.stream;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

//除法运算
public class Div implements Runnable{

	public static BlockingQueue<Msg> q = new LinkedBlockingDeque<Msg>();

	@Override
	public void run() {
		while(true){
			try{
				Msg msg = q.take();
				msg.i = msg.i/2;
				System.out.println(msg.orgStr + "=" + msg.i);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}

}
