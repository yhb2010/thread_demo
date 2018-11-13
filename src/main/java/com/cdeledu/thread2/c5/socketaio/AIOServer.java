package com.cdeledu.thread2.c5.socketaio;

import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

//虽然nio在网络操作中，提供了非阻塞的方法，但是nio的io行为还是同步的，对于nio来说，我们的业务线程是在io操作准备好，得到通知，接着就由这个线程自行进行io操作，io操作本身还是同步的。
//但对于aio来说，他不是在io准备好后在通知线程，而是在io操作已经完成后，再给线程发出通知，因此，aio是完全不会阻塞的，此时，我们的业务逻辑将变成一个回调函数，等待io操作完成后，由系统自动触发。
public class AIOServer {

	public final static int PORT = 8000;
	private AsynchronousServerSocketChannel server;

	public AIOServer() throws Exception {
		//绑定8000端口，使用AsynchronousServerSocketChannel异步Channel作为服务器
		server = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(PORT));
		//通过setOption配置Socket
        server.setOption(StandardSocketOptions.SO_REUSEADDR, true);
        server.setOption(StandardSocketOptions.SO_RCVBUF, 16 * 1024);
	}

	//start开启服务器，这个方法除了第一条打印外，只调用了server.accept函数。
	//server.accept会立即返回，他并不会真的这等待客户端到来。它的第一个参数是一个附件，可以是任意类型，用于让当前线程和后续的回调方法可以共享信息，它会在后续调用中，传递给handler。
	//server.accept实际上做了两件事，第一是发起accept请求，告诉系统可以开始监听端口了。
	//第二，注册CompletionHandler实例，告诉系统，一旦有客户端前来连接，如果成功连接，就去执行CompletionHandler.completed；如果连接失败，就去执行CompletionHandler.failed方法。
	public void start() throws Exception{
		System.out.println("Server listen on " + PORT);
		//注册事件和事件完成后的处理器
		server.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {

			final ByteBuffer buffer = ByteBuffer.allocate(1024);

			@Override
			public void completed(AsynchronousSocketChannel result, Object attachment) {
				System.out.println(Thread.currentThread().getName());
				Future<Integer> writeResult = null;
				try{
					buffer.clear();
					//read方法也是异步的，返回的结果是一个Future，为了编程方便，这里直接调用get方法，进行等待，将这个异步方法变成同步方法。
					result.read(buffer).get(100, TimeUnit.SECONDS);
					buffer.flip();
					//将数据写回给客户端，也是异步的，会立即返回Future对象。
					writeResult = result.write(buffer);
					buffer.flip();
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					try{
						//这里服务器进行下一个客户端连接的准备，同时关闭当前正在处理的客户端连接。但在关闭之前，得先确保之前的write操作已经完成。
						server.accept(null, this);
						writeResult.get();
						result.close();
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}

			@Override
			public void failed(Throwable exc, Object attachment) {
				System.out.println("failed:" + exc);
			}

		});
	}

	public static void main(String[] args) throws Exception {
		new AIOServer().start();
		//由于start里面使用的都是异步方法，因此他会马上返回，不像阻塞方法那样会进行等待，因此，这里要进行等待
		while(true){
			Thread.sleep(1000);
		}
	}

}
