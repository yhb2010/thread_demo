package com.cdeledu.thread3.c25twoPhaseTermination;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;

/**Two Phase Termination：当一个线程正常结束，或者因被打断而结束，或者因出现异常而结束时，我们需要考虑如何同时释放线程中资源，比如文件句柄、socket、数据库连接等比较稀缺的资源。
 * 在进行线程两阶段终结的时候需要考虑下面几个问题：
 * 1、第二阶段的终止保证安全性，比如涉及对共享资源的操作
 * 2、要百分百保证线程结束，假设在第二阶段出现了死循环、阻塞等异常导致无法结束
 * 3、对资源的释放时间要控制在一个可控范围内
 * 
 * 以第24章的例子为例说明，要释放掉socket资源
 * 
 * 还可以借助Phantom Reference，Phantom Reference必须和ReferenceQueue配合使用，Phantom Reference的get方法始终返回null，
 * 当垃圾收回器决定回收Phantom Reference时，会将其插入ReferenceQueue中，使用Phantom Reference进行清理动作比Object的finalize方法更加灵活。
 * @author Administrator
 *
 */
public class ClientHandler implements Runnable {
	
	private final Socket socket;
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
		}finally{
			//任务执行结束，执行释放资源工作
			release();
		}
	}

	private void release() {
		try {
			if(socket != null)
				socket.close();
		} catch (Throwable e) {
			//将socket实例加入Tracker中
			SocketCleaningTracker.tracker(socket);
		}
	}

	private void chat() throws IOException {
		BufferedReader bufferedReader = wrap2Reader(socket.getInputStream());
		PrintStream printStream = wrap2Print(socket.getOutputStream());
		String received;
		while((received = bufferedReader.readLine()) != null){
			System.out.printf("client: %s-message:%s\n", clientIdentify, received);
			if("quit".equals(received)){
				writer2Client(printStream, "client will close.");
				socket.close();
				break;
			}
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
