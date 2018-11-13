package com.cdeledu.thread3.c24thread_per_message;

import java.io.IOException;

/**Thread-per-Message意思是为每一个消息的处理开辟一个线程使得消息能够以并发的方式进行处理，从而提高系统的整体吞吐能力
 * @author Administrator
 *
 */
public class ChatDemo {
	
	public static void main(String[] args) throws IOException {
		new ChatServer().startServer();
	}

}
