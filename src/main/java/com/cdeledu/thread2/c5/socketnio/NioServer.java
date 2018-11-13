package com.cdeledu.thread2.c5.socketnio;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NioServer {

	private Selector selector;
	private ExecutorService tp = Executors.newCachedThreadPool();
	//统计服务器线程在一个客户端上花费了多少时间，统计的是某一个socket上花费的时间。
	private Map<Socket, Long> time_stat = new HashMap<>();

	private void startServer() throws Exception{
		//通过工厂方法获得一个Selector对象的实例
		selector = SelectorProvider.provider().openSelector();
		//获得表示服务器端的SocketChannel实例
		ServerSocketChannel ssc = ServerSocketChannel.open();
		//将这个SocketChannel设置为非阻塞模式。实际上，Channel可以像传统的Socket那样按阻塞的方式工作。但在这里，希望使用非阻塞模式，这样我们才可以像Channel注册感兴趣的事件，并在数据准备好后，得到必要的通知
		ssc.configureBlocking(false);

		//进行端口绑定，将这个Channel绑定在8000端口。
		InetSocketAddress isa = new InetSocketAddress(8000);
		ssc.socket().bind(isa);

		//将这个ServerSocketChannel绑定到Selector上，并注册它感兴趣的事件为Accept。这样，Selector就能为这个Channel服务了。当Selector发现ServerSocketChannel
		//有新的客户端连接时，就会通知ServerSocketChannel进行处理。方法register的返回值是一个SelectionKey，SelectionKey表示一对Selector和Channel的关系。
		//当Channel注册到Selector上时，就相当于确立了两者的服务关系。那么SelectionKey就是这个契约。当Selector或者Channel被关闭时，他们对应的SelectionKey就会失效。
		SelectionKey acceptKey = ssc.register(selector, SelectionKey.OP_ACCEPT);

		//无限循环，用于等待——分发网络消息
		for(;;){
			//阻塞方法，如果当前没有数据准备好，就等待。一旦有数据可读，就会返回。它的返回值是已经准备就绪的SelectionKey的数量。这里简单的将其忽略。
			selector.select();
			//获取那些准备好的SelectionKey，因为Selector同时为多个Channel服务，因此已经准备就绪的Channel就有可能是多个。所以这里得到的自然是一个集合。得到这个就绪集合后，剩下的就是遍历这个集合，挨个处理所有的Channel数据。
			Set<SelectionKey> readKeys = selector.selectedKeys();
			Iterator<SelectionKey> i = readKeys.iterator();
			long e = 0;
			while(i.hasNext()){
				SelectionKey sk = i.next();
				//将这个元素移除，注意，这个非常重要，否则就会重复处理相同的SelectionKey。当你处理完一个SelectionKey后，务必将其从集合内删除。
				i.remove();

				//判断当前SelectionKey所代表的Channel是否在Acceptable状态，如果是，就进行客户端的接收（执行doAccept）
				if(sk.isAcceptable()){
					doAccept(sk);
				}
				//判断Channel是否已经可读了，如果是就进行读取(doRead)。这里为了统计系统处理每一个连续的时间，在这里记录了在读取数据之前的一个时间戳
				else if(sk.isValid() && sk.isReadable()){
					if(!time_stat.containsKey(((SocketChannel)sk.channel()).socket())){
						time_stat.put(((SocketChannel)sk.channel()).socket(), System.currentTimeMillis());
					}
					doRead(sk);
				}
				//判断通道是否准备好进行写。如果是就进行写入（doWrite），同时在写入完成后，根据读取前的时间戳，输出处理这个Socket连接的耗时。
				else if(sk.isValid() && sk.isWritable()){
					doWrite(sk);
					e = System.currentTimeMillis();
					long b = time_stat.remove(((SocketChannel)sk.channel()).socket());
					System.out.println("spend:" + (e-b) + "ms");
				}
			}
		}
	}

	private void doWrite(SelectionKey sk) {
		//这个SelectionKey对于同一个客户端来说，是同一个SelectionKey，因此，通过它可以共享EchoClient实例。
		SocketChannel channel = (SocketChannel)sk.channel();
		EchoClient echoClient = (EchoClient)sk.attachment();
		LinkedList<ByteBuffer> outq = echoClient.getOutputQueue();

		//得到列表顶部元素，准备写回客户端。
		ByteBuffer bb = outq.getLast();
		try{
			//进行写回操作，如果全部发送完成，则移出这个缓存对象
			int len = channel.write(bb);
			if(len == -1){
				disconnect(sk);
				return;
			}

			if(bb.remaining() == 0){
				//the buffer was completely written, remove it.
				outq.removeLast();
			}
		}catch(Exception e){
			System.out.println("Failed to write from client.");
			e.printStackTrace();
			disconnect(sk);
		}

		//这里是最容易被忽略的地方：全部数据发送完成后（也就是outq的长度为0），需要将写事件从感兴趣的操作中移除。如果不这么做，每次Channel准备好写时，都会来执行doWrite方法。而实际上，你又无数据可写，这显然是不合理的。
		if(outq.size() == 0){
			sk.interestOps(SelectionKey.OP_READ);
		}
	}

	//当Channel可以读取时，该方法会被调用
	private void doRead(SelectionKey sk) {
		//这个方法接收一个SelectionKey参数，通过这个SelectionKey可以得到当前的客户端Channel，我们准备了8k的缓冲区读取数据。
		SocketChannel channel = (SocketChannel)sk.channel();
		ByteBuffer bb = ByteBuffer.allocate(8192);
		int len;

		try{
			//所有读取到的数据存放到变量bb中。
			len = channel.read(bb);
			if(len < 0){
				disconnect(sk);
				return;
			}
		}catch(Exception e){
			System.out.println("Failed to read from client.");
			e.printStackTrace();
			disconnect(sk);
			return;
		}

		//读取完成后，重置缓冲区，为数据处理做准备。
		bb.flip();
		//在这个实例里，数据处理很简单，但为了模拟复杂的场景，还是使用了线程池进行数据处理，这样，如果数据处理很复杂，就能在单独的线程中进行，而不用阻塞任务派发线程。
		tp.execute(new HandleMsg(sk, bb));
	}

	private void disconnect(SelectionKey sk) {

	}

	private void doAccept(SelectionKey sk) {
		ServerSocketChannel server = (ServerSocketChannel)sk.channel();
		SocketChannel clientChannel;
		try{
			//和socket编程很相似，当有一个新的客户端连接时，就会有一个新的Channel产生代表这个连接。
			//这里的clientChannel就表示和客户端通信的通道。
			clientChannel = server.accept();
			//将这个Channel配置为非阻塞模式，也就是要求系统在准备好io后，再通知我们的线程来读取或者写入
			clientChannel.configureBlocking(false);

			//很关键，他将新生成的Channel注册到Selector选择器上，并告诉Selector，我现在对读op_read操作感兴趣。这样，当Selector发现这个Channel已经准备好读时，就能给线程一个通知。
			SelectionKey clientKey = clientChannel.register(selector, SelectionKey.OP_READ);
			//新建一个对象实例，一个EchoClient实例代表一个客户端
			EchoClient echoClient = new EchoClient();
			//我们将这个客户端实例作为附件，附加到表示这个连接的SelectionKey上，这样在整个连接的处理过程中，我们都可以共享这个EchoClient实例
			clientKey.attach(echoClient);

			InetAddress clientAddress = clientChannel.socket().getInetAddress();
			System.out.println("Accpeted connection from " + clientAddress.getHostAddress() + ".");
		}catch(Exception e){
			System.out.println("Failed to accept new client.");
			e.printStackTrace();
		}
	}

	class HandleMsg implements Runnable {

		SelectionKey sk;
		ByteBuffer bb;

		public HandleMsg(SelectionKey sk, ByteBuffer bb) {
			this.sk = sk;
			this.bb = bb;
		}

		@Override
		//简单的将接收到的数据压入EchoClient的队列，如果需要处理业务逻辑，就可以在这里进行处理。
		//在数据处理完成后，就可以准备将结果写回到客户端，因此，重新注册感兴趣的消息事件，将写操作也作为感兴趣的事件进行提交，这样在通道准备好写入时，就能通知线程。
		public void run() {
			EchoClient echoClient = (EchoClient)sk.attachment();
			echoClient.enqueue(bb);
			sk.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
			//强迫selector立即返回
			selector.wakeup();
		}

	}

	public static void main(String[] args) throws Exception {
		NioServer server = new NioServer();
		server.startServer();
	}


}
