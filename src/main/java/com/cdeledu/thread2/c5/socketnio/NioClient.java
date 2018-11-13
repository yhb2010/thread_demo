package com.cdeledu.thread2.c5.socketnio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;

//也可以使用nio来实现客户端
//和构造服务器类似，核心元素也是Selector、Channel、SelectionKey
public class NioClient {

	private Selector selector;

	public void init(String ip, int port) throws Exception{
		//创建一个SocketChannel实例，并设置为非阻塞模式。
		SocketChannel channel = SocketChannel.open();
		channel.configureBlocking(false);
		//创建一个selector
		this.selector = SelectorProvider.provider().openSelector();
		//将SocketChannel绑定到Socket上，但由于当前Channel是非阻塞的，因此，connect方法返回时，不一定连接建立成功，在后续使用这个连接时，需要使用finishConnect再次确认。
		channel.connect(new InetSocketAddress(port));
		//将这个channel和selector进行绑定，并注册了感兴趣的事件作为连接
		channel.register(selector, SelectionKey.OP_CONNECT);
	}

	//初始化完成后，这是程序的主要执行逻辑
	public void working() throws Exception {
		while(true){
			if(!selector.isOpen()){
				break;
			}
			//通过selector得到已经准备好的事件，如果当前没有任何事件准备就绪，这里会阻塞。这里的整个处理机制和服务端非常类似，主要处理两个事件，首先是表示连接就绪的Connect事件，以及表示通道可读的Read事件
			selector.select();
			Iterator<SelectionKey> ite = this.selector.selectedKeys().iterator();
			while(ite.hasNext()){
				SelectionKey key = ite.next();
				ite.remove();
				//连接事件发生
				if(key.isConnectable()){
					connect(key);
				}else if(key.isReadable()){
					read(key);
				}
			}
		}
	}

	private void read(SelectionKey key) throws Exception {
		SocketChannel channel = (SocketChannel)key.channel();
		//首先创建100字节的缓冲区，接着从Channel中读取数据，并将其打印在控制台上，最后，关闭Channel和Selector
		//创建读取的缓冲区
		ByteBuffer buffer = ByteBuffer.allocate(100);
		channel.read(buffer);
		byte[] data = buffer.array();
		String msg = new String(data).trim();
		System.out.println("客户端接收到的信息：" + msg);
		channel.close();
		key.selector().close();
	}

	private void connect(SelectionKey key) throws Exception {
		SocketChannel channel = (SocketChannel)key.channel();
		//首先判断连接是否已建立，如果没有，则调用finishConnect完成连接，建立连接后，向Channel写入数据，并同时注册读事件为感兴趣事件
		//如果正在连接，则完成连接
		if(channel.isConnectionPending()){
			channel.finishConnect();
		}
		channel.configureBlocking(false);
		channel.write(ByteBuffer.wrap(new String("hello server!\r\n").getBytes()));
		channel.register(this.selector, SelectionKey.OP_READ);
	}

	public static void main(String[] args) throws Exception {
		NioClient ncient = new NioClient();
		ncient.init("localhost", 8000);
		ncient.working();
	}

}
