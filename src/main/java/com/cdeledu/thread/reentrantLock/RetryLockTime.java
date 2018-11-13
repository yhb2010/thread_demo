package com.cdeledu.thread.reentrantLock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**场景3：如果发现该操作已经在执行，则尝试等待一段时间，等待超时则不执行（尝试等待执行）
这种其实属于场景2的改进，等待获得锁的操作有一个时间的限制，如果超时则放弃执行。
用来防止由于资源处理不当长时间占用导致死锁情况（大家都在等待资源，导致线程队列溢出）。
 * @author DELL
 *
 */
public class RetryLockTime {

	public static void main(String[] args) {
		RetryTimeDomain d = new RetryTimeDomain();
		Thread t1 = new RetryTimeThread(d);
		Thread t2 = new RetryTimeThread2(d);
		t2.start();
		t1.start();
	}
}

class RetryTimeThread extends Thread {

	private RetryTimeDomain domainDemo;

	public RetryTimeThread(RetryTimeDomain domainDemo) {
		super();
		this.domainDemo = domainDemo;
	}

	@Override
	public void run(){
		domainDemo.test(Thread.currentThread().getName());
	}

}

class RetryTimeThread2 extends Thread {

	private RetryTimeDomain domainDemo;

	public RetryTimeThread2(RetryTimeDomain domainDemo) {
		super();
		this.domainDemo = domainDemo;
	}

	@Override
	public void run(){
		domainDemo.test(Thread.currentThread().getName());
	}

}

class RetryTimeDomain{

	private ReentrantLock lock = new ReentrantLock();

	public void test(String tName){
		//tryLock(long timeout, TimeUnit timeUnit)的工作类似于 tryLock() 方法，除了它在放弃锁定 Lock 之前等待一个给定的超时时间之外。
		try{
			if (lock.tryLock(1, TimeUnit.SECONDS)) {
				System.out.println(tName + "得到锁");
	        	try {
					Thread.sleep(2000);
					System.out.println(tName + "执行完");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
		            lock.unlock();
		        }
		    }else{
		    	System.out.println(tName + "没得到锁");
		    }
		}catch (InterruptedException e) {
            e.printStackTrace(); //当前线程被中断时(interrupt)，会抛InterruptedException
        }
	}

}