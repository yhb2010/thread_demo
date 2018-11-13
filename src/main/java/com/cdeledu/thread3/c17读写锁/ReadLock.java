package com.cdeledu.thread3.c17读写锁;

public class ReadLock implements Lock {

	private final ReadWriteLockImpl readWriteLock;
	
	public ReadLock(ReadWriteLockImpl readWriteLock) {
		this.readWriteLock = readWriteLock;
	}

	@Override
	public void lock() throws InterruptedException {
		//使用MUTEX作为锁
		synchronized (readWriteLock.getMutex()) {
			//若此时有现成进行写操作，或者有写线程在等待并且偏向写锁的标识为true，就会无法获得读锁，只能被挂起
			while(readWriteLock.getWritingWriters() > 0 || (readWriteLock.getPreferWriter() && readWriteLock.getWaitingWtiters() > 0)){
				readWriteLock.getMutex().wait();
			}
			//成功获得读锁，并且使readingReaders的数量加1
			readWriteLock.incrementReadingReaders();
		}
	}

	@Override
	public void unlock() {
		synchronized (readWriteLock.getMutex()) {
			//释放锁的过程就是使得当前reading数量减1
			//将preferWriter设置为true，使得writer线程获得更多的机会
			//通知唤醒与mutex关联的monitor wait set中的线程
			readWriteLock.decrementReadingReaders();
			readWriteLock.setPreferWriter(true);
			readWriteLock.getMutex().notifyAll();
		}
	}

}
