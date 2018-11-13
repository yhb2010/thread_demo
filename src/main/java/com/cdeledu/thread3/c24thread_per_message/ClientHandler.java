package com.cdeledu.thread3.c24thread_per_message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;

public class ClientHandler implements Runnable {
	
	//客户端的socket连接
	private final Socket socket;
	//客户端的identify
	private final String clientIdentify;

	public ClientHandler(final Socket socket) {
		this.socket = socket;
		clientIdentify = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
	}

	@Override
	public void run() {
		try{
			chat();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	private void chat() throws IOException {
		BufferedReader bufferedReader = wrap2Reader(socket.getInputStream());
		PrintStream printStream = wrap2Print(socket.getOutputStream());
		String received;
		while((received = bufferedReader.readLine()) != null){
			//将客户端发送的消息输出到控制台
			System.out.printf("client: %s-message:%s\n", clientIdentify, received);
			if("quit".equals(received)){
				//如果客户端发送了quit命令，则断开与客户端的连接
				writer2Client(printStream, "client will close.");
				socket.close();
				break;
			}
			//向客户端发送消息
			writer2Client(printStream, "Server:" + received);
		}
	}
	
	/**将输入字节流封装成BufferedReader缓冲字符流
	 * @param inputStream
	 * @return
	 */
	private BufferedReader wrap2Reader(InputStream inputStream){
		return new BufferedReader(new InputStreamReader(inputStream));
	}
	
	/**将输出字节流封装成PrintStream
	 * @param outputStream
	 * @return
	 */
	private PrintStream wrap2Print(OutputStream outputStream){
		return new PrintStream(outputStream);
	}
	
	/**向客户端发送消息
	 * @param print
	 * @param message
	 */
	private void writer2Client(PrintStream print, String message){
		print.println(message);
		print.flush();
	}
}
