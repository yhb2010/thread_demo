package com.cdeledu.thread3.c24thread_per_message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.cdeledu.thread3.c8.myselfthreadpool.BasicThreadPool;
import com.cdeledu.thread3.c8.myselfthreadpool.ThreadPool;

/**服务器端，用于接收客户端的连接，并且与之进行tcp通信交互，当服务器端接收到了每一次的客户端连接后便会给线程池提交一个任务用于与客户端进行交互，进而提高并发响应能力
 * @author Administrator
 *
 */
public class ChatServer {

	//服务端端口
	private final int port;
	//定义线程池，该线程池是第8章定义的那个
	private ThreadPool threadPool;
	//服务端socket
	private ServerSocket serverSocket;
	
	public ChatServer(int port) {
		super();
		this.port = port;
	}

	public ChatServer() {
		this(13312);
	}
	
	public void startServer() throws IOException{
		//创建线程池，初始化一个线程，核心线程数量为1，最大线程数量为2，阻塞队列中最大可加入1000个任务
		threadPool = new BasicThreadPool(1, 2, 1, 1000);
		serverSocket = new ServerSocket(port);
		serverSocket.setReuseAddress(true);
		System.out.println("Chat server is started and listen at port: " + port);
		listen();
	}

	private void listen() throws IOException {
		for(;;){
			//accept方法是阻塞方法，当有新的连接进入时才会返回，并且返回的是客户端的连接
			Socket client = serverSocket.accept();
			//将客户端连接作为一个Request封装成对应的Handler然后提交给线程池
			threadPool.execute(new ClientHandler(client));
		}
	}
	
}
