package com.cdeledu.thread3.c17读写锁;

public interface Lock {
	
	/**获取显式锁，没有获得锁的线程将被阻塞
	 * @throws InterruptedException
	 */
	void lock() throws InterruptedException;

	/**
	 * 释放获取的锁，其主要目的是为了减少reader和writer的数量
	 */
	void unlock();
	
}
