package com.cdeledu.thread3.c17读写锁;

public class WriteLock implements Lock {
	
	private final ReadWriteLockImpl readWriteLock;
	
	public WriteLock(ReadWriteLockImpl readWriteLock) {
		this.readWriteLock = readWriteLock;
	}

	@Override
	public void lock() throws InterruptedException {
		synchronized (readWriteLock.getMutex()) {
			try{
				//首先使得等待获取写入锁的数字加1
				readWriteLock.incrementWaitingWriters();
				//如果此时有其它线程正在进行读操作，或者写操作，那么当前线程将被挂起
				while(readWriteLock.getReadingReaders() > 0 || readWriteLock.getWritingWriters() > 0){
					readWriteLock.getMutex().wait();
				}
			}finally{
				//成功获取到了写入锁，使得等待获取写入锁的计数器减1
				readWriteLock.decrementWaitingWriters();
			}
			//将正在写入的线程数加1
			readWriteLock.incrementWritingWriters();
		}
	}

	@Override
	public void unlock() {
		synchronized (readWriteLock.getMutex()) {
			//减少正在写入的线程数量
			readWriteLock.decrementWritingWriters();
			//将偏好状态修改为false，可以使得读锁被最快速的获得
			readWriteLock.setPreferWriter(false);
			//通知唤醒其它在mutex monitor waitset中的线程
			readWriteLock.getMutex().notifyAll();
		}
	}

}
