package com.cdeledu.thread3.c17读写锁;

public interface ReadWriteLock {
	
	/**创建reader锁
	 * @return
	 */
	Lock readLock();

	/**创建write锁
	 * @return
	 */
	Lock writeLock();
	
	/**获取当前有多少线程正在执行写操作，最多只能有1个
	 * @return
	 */
	int getWritingWriters();
	
	/**获取当前有多少线程正在等待获取写入锁
	 * @return
	 */
	int getWaitingWtiters();
	
	/**获取当前有多少线程正在进行读操作
	 * @return
	 */
	int getReadingReaders();
	
	/**工厂方法，创建ReadWriteLock
	 * @return
	 */
	static ReadWriteLock readWriteLock(){
		return new ReadWriteLockImpl();
	}
	
	/**工厂方法，创建ReadWriteLock，并且传入是否偏向写
	 * @param preferWriter
	 * @return
	 */
	static ReadWriteLock readWriteLock(boolean preferWriter){
		return new ReadWriteLockImpl(preferWriter);
	}
	
}
