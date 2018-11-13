package com.cdeledu.thread2.c5.socketaio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

//AIO客户端
public class AIOClient {

	public static void main(String[] args) throws Exception {
		final AsynchronousSocketChannel client = AsynchronousSocketChannel.open();
		client.connect(new InetSocketAddress("localhost", 8000), null, new CompletionHandler<Void, Object>() {

			@Override
			public void completed(Void result, Object attachment) {
				client.write(ByteBuffer.wrap("hello server!\r\n".getBytes()), null, new CompletionHandler<Integer, Object>() {

					@Override
					public void completed(Integer result, Object attachment) {
						try{
							ByteBuffer buffer = ByteBuffer.allocate(1024);
							client.read(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {

								@Override
								public void completed(Integer result, ByteBuffer attachment) {
									buffer.flip();
									byte[] data = buffer.array();
									String msg = new String(data).trim();
									System.out.println(msg);
									try{
										client.close();
									}catch(Exception e){
										e.printStackTrace();
									}
								}

								@Override
								public void failed(Throwable exc, ByteBuffer attachment) {

								}

							});
						}catch(Exception e){
							e.printStackTrace();
						}
					}

					@Override
					public void failed(Throwable exc, Object attachment) {

					}

				});
			}

			@Override
			public void failed(Throwable exc, Object attachment) {

			}

		});

		//由于主线程马上结束，这里等待上述处理全部完成
		Thread.sleep(1000);
	}

}
