package com.cdeledu.thread2.c3concurrent;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**读读之间不阻塞
 * 读写、写读、写写阻塞
 * @author DELL
 *
 */
public class ReadLockDemo {

	private static ReentrantReadWriteLock readWriterLock = new ReentrantReadWriteLock();
	private static Lock readLock = readWriterLock.readLock();
	private static Lock writeLock = readWriterLock.writeLock();
	private int value;

	public Object handleRead(Lock lock) throws InterruptedException{
		try{
			lock.lock();
			Thread.sleep(2000);//模拟读操作，读操作耗时越多，读写锁的优势越明显
			System.out.println("进入读");
			return value;
		}finally{
			lock.unlock();
		}
	}

	public void handleWrite(Lock lock, int index) throws InterruptedException{
		try{
			lock.lock();
			Thread.sleep(2000);
			System.out.println("进入写");
			value = index;
		}finally{
			lock.unlock();
		}
	}

	public static void main(String[] args) {
		final ReadLockDemo demo = new ReadLockDemo();
		Runnable readR = new Runnable() {
			@Override
			public void run() {
				try {
					System.out.println(demo.handleRead(readLock));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		Runnable writeR = new Runnable() {
			@Override
			public void run() {
				try {
					demo.handleWrite(writeLock, 1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};

		//由于使用了读写锁，读操作并行，而写会阻塞读
		for(int i=18; i<20; i++){
			new Thread(writeR).start();
		}
		for(int i=0; i<18; i++){
			new Thread(readR).start();
		}
	}

}
