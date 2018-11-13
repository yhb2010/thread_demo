package com.cdeledu.thread2.c5.socketio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//socket服务端，为每一个客户端连接启用一个线程。
//服务端处理请求之所以慢，并不是因为在服务端有多少繁重的任务，而仅仅是因为服务线程在等待io而已。让高度运转的cpu去等待及其低效的网络
//io是非常不合算的行为，那么我们是不是有什么方法，将网络io的等待时间从线程中分离出来呢？
public class MultiThreadEchoServer {

	private static ExecutorService tp = Executors.newCachedThreadPool();
	static class HandleMsg implements Runnable{
		Socket clientSocket;
		public HandleMsg(Socket clientSocket) {
			this.clientSocket = clientSocket;
		}
		@Override
		public void run() {
			BufferedReader is = null;
			PrintWriter os = null;
			try{
				is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				os = new PrintWriter(clientSocket.getOutputStream(), true);
				String inputLine = null;
				long b = System.currentTimeMillis();
				while((inputLine = is.readLine()) != null){
					os.println(inputLine);
				}
				long e = System.currentTimeMillis();
				System.out.println("spend:" + (e-b) + "ms");
			}catch(IOException e){
				e.printStackTrace();
			}finally{
				try{
					if(is != null){
						is.close();
					}
					if(os != null){
						os.close();
					}
					clientSocket.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}

	}

	public static void main(String[] args) throws Exception {
		ServerSocket echoServer = null;
		Socket clientSocket = null;
		try{
			echoServer = new ServerSocket(8000);
		}catch(IOException e){
			e.printStackTrace();
		}
		while ((clientSocket = echoServer.accept()) != null) {
			System.out.println(clientSocket.getRemoteSocketAddress() + " connect!");
            // 接收一个客户端Socket，生成一个HttpRequestHandler，放入线程池执行
            tp.execute(new HandleMsg(clientSocket));
        }
		echoServer.close();
	}

}
