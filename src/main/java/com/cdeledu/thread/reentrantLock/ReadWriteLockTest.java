package com.cdeledu.thread.reentrantLock;

import java.util.Random;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**读写锁：分为读锁和写锁，多个读锁不互斥，读锁与写锁互斥，这是由jvm自己控制的，你只要上好相应的锁即可。如果你的代码只读数据，可以很多人同时读，但不能同时写，那就上读锁；如果你的代码修改数据，只能有一个人在写，且不能同时读取，那就上写锁。总之，读的时候上读锁，写的时候上写锁！
 *
 * 线程进入读锁的前提条件：
    没有其他线程的写锁，
    没有写请求或者有写请求，但调用线程和持有锁的线程是同一个

线程进入写锁的前提条件：
    没有其他线程的读锁
    没有其他线程的写锁
 * @author DELL
 *
 */
public class ReadWriteLockTest {
	public static void main(String[] args) {
		final Queue3 q3 = new Queue3();
		for (int i = 0; i < 3; i++) {
			new Thread() {
				public void run() {
					q3.get();
				}

			}.start();
		}
		for (int i = 0; i < 3; i++) {
			new Thread() {
				public void run() {
					q3.put(new Random().nextInt(10000));
				}

			}.start();
		}

	}
}

class Queue3 {
	private Object data = null;// 共享数据，只能有一个线程能写该数据，但可以有多个线程同时读该数据。
	private ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();

	public void get() {
		rwl.readLock().lock();// 上读锁，其他线程只能读不能写
		System.out.println(Thread.currentThread().getName() + " be ready to read data!");
		try {
			Thread.sleep((long) (Math.random() * 1000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getName() + "have read data :" + data);
		rwl.readLock().unlock(); // 释放读锁，最好放在finnaly里面
	}

	public void put(Object data) {

		rwl.writeLock().lock();// 上写锁，不允许其他线程读也不允许写
		System.out.println(Thread.currentThread().getName() + " be ready to write data!");
		try {
			Thread.sleep((long) (Math.random() * 1000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.data = data;
		System.out.println(Thread.currentThread().getName() + " have write data: " + data);

		rwl.writeLock().unlock();// 释放写锁
	}
}