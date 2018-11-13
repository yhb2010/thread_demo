package com.cdeledu.thread3.c19future;

public interface Future<T> {

	//获取结果，将会导致阻塞
	T get() throws InterruptedException;
	
	//判断是否已经执行完成
	boolean done();
	
}
