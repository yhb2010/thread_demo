package com.cdeledu.thread2.c5.socketio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.LockSupport;

//一个非常耗时的客户端。
//服务端处理请求之所以慢，并不是因为在服务端有多少繁重的任务，而仅仅是因为服务线程在等待io而已。让高度运转的cpu去等待及其低效的网络
//io是非常不合算的行为，那么我们是不是有什么方法，将网络io的等待时间从线程中分离出来呢？
public class HeavyClient {

	private static ExecutorService tp = Executors.newCachedThreadPool();
	private static final int sleep_time = 1000*1000*1000;
	private static class EchoClient implements Runnable{

		@Override
		public void run() {
			Socket client = null;
			PrintWriter writer = null;
			BufferedReader reader = null;
			try{
				client = new Socket();
				client.connect(new InetSocketAddress("localhost", 8000));
				writer = new PrintWriter(client.getOutputStream(), true);
				writer.print("H");
				LockSupport.parkNanos(sleep_time);
				writer.print("e");
				LockSupport.parkNanos(sleep_time);
				writer.print("l");
				LockSupport.parkNanos(sleep_time);
				writer.print("l");
				LockSupport.parkNanos(sleep_time);
				writer.print("o");
				LockSupport.parkNanos(sleep_time);
				writer.print("!");
				LockSupport.parkNanos(sleep_time);
				writer.println();
				writer.flush();
				reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
				System.out.println("from server:" + reader.readLine());
			}catch(UnknownHostException e){
				e.printStackTrace();
			}catch(IOException e){
				e.printStackTrace();
			}finally{
				try{
					if(writer != null){
						writer.close();
					}
					if(reader != null){
						reader.close();
					}
					if(client != null){
						client.close();
					}
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}

	}

	public static void main(String[] args) {
		EchoClient ec = new EchoClient();
		for(int i=0;i<10;i++){
			tp.execute(ec);
		}
	}

}
