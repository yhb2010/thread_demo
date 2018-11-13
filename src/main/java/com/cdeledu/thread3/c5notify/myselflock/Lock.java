package com.cdeledu.thread3.c5notify.myselflock;

import java.util.List;
import java.util.concurrent.TimeoutException;

public interface Lock {
	
	//方法永远阻塞，除非获取到了锁，这一点和synchronized非常类似，但是该方法是可以被中断的，中断时会抛出InterruptedException异常
	void lock() throws InterruptedException;
	
	//方法除了可以被中断以外，还增加了对应的超时功能
	void lock(long mills) throws InterruptedException, TimeoutException;
	
	//用来进行锁的释放
	void unlock();
	
	//获取当前有哪些线程被阻塞
	List<Thread> getBlockedThreads();

}
