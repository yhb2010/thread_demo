package com.cdeledu.thread3.c17读写锁;

public class ReadWriteLockImpl implements ReadWriteLock {

	//定义对象锁
	//虽然我们在开发读写锁，但是在实现的内部也需要一个锁进行数据同步以及线程之间的通信，其中MUTEX作用就在于此
	private final Object MUTEX = new Object();
	//当前有多少个线程正在写入
	private int writingWriters = 0;
	//当前有多少个线程正在等待写入
	private int waitingWriters = 0;
	//当前有多少个线程正在read
	private int readingReaders = 0;
	//read和write的偏好设置
	//作用在于控制倾向性，一般来说读写锁非常适合读多写少的场景，如果preferWriter为false，很多读线程都在读数据，那么写线程将很难得到写的机会
	private boolean preferWriter;

	public ReadWriteLockImpl(){
		this(true);
	}
	
	public ReadWriteLockImpl(boolean preferWriter) {
		this.preferWriter = preferWriter;
	}

	@Override
	public Lock readLock() {
		return new ReadLock(this);
	}

	@Override
	public Lock writeLock() {
		return new WriteLock(this);
	}
	
	/**
	 * 使写线程数量加1
	 */
	void incrementWritingWriters(){
		writingWriters++;
	}
	
	/**
	 * 使等待写入线程数量加1
	 */
	void incrementWaitingWriters(){
		waitingWriters++;
	}
	
	/**
	 * 使读线程数量加1
	 */
	void incrementReadingReaders(){
		readingReaders++;
	}
	
	/**
	 * 使写线程数量减1
	 */
	void decrementWritingWriters(){
		writingWriters--;
	}
	
	/**
	 * 使等待写入线程数量减1
	 */
	void decrementWaitingWriters(){
		waitingWriters--;
	}
	
	/**
	 * 使读线程数量减1
	 */
	void decrementReadingReaders(){
		readingReaders--;
	}

	@Override
	public int getWritingWriters() {
		return writingWriters;
	}

	@Override
	public int getWaitingWtiters() {
		return waitingWriters;
	}

	@Override
	public int getReadingReaders() {
		return readingReaders;
	}
	
	/**获取对象锁
	 * @return
	 */
	Object getMutex(){
		return MUTEX;
	}
	
	/**获取当前是否偏向写锁
	 * @return
	 */
	boolean getPreferWriter(){
		return preferWriter;
	}
	
	/**设置写锁偏好
	 * @param preferWriter
	 */
	void setPreferWriter(boolean preferWriter){
		this.preferWriter = preferWriter;
	}

}
