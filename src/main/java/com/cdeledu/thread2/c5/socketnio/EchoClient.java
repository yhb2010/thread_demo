package com.cdeledu.thread2.c5.socketnio;

import java.nio.ByteBuffer;
import java.util.LinkedList;

//封装了一个队列，保存在需要回复给这个客户端的所有消息，这样，再进行回复时，只要从outq对象中弹出元素即可。
public class EchoClient {
	
	private LinkedList<ByteBuffer> outq;
	
	public EchoClient() {
		outq = new LinkedList<ByteBuffer>();
	}
	
	public LinkedList<ByteBuffer> getOutputQueue(){
		return outq;
	}
	
	public void enqueue(ByteBuffer bb){
		outq.addFirst(bb);
	}

}
