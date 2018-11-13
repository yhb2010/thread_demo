package com.cdeledu.thread.imooc;

import java.text.SimpleDateFormat;
import java.util.Date;

//Thread.sleep(times)使当前线程从Running状态放弃处理器进入Block状态,休眠times毫秒，再返回Runnable状态。
public class SleepDemo {

	public static void main(String[] args) {
		new Thread(new Runnable() {
		    @Override
		    public void run() {
		        SimpleDateFormat format=new SimpleDateFormat("hh:mm:ss");
		        //输出系统时间的时分秒。每隔一秒显示一次。可能会出现跳秒的情况，因为阻塞1秒过后进入runnable状态，等待分配时间片进入running状态后还需要一点时间
		        while(true){
		            try {
		                Thread.sleep(1000);

		            } catch (InterruptedException e) {
		                 e.printStackTrace();

		            }
		             System.out.println(format.format(new Date()));
		          }
		    }
		}).start();
	}

}
