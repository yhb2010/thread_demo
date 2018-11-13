package com.cdeledu.thread.reentrantLock;

import java.util.concurrent.locks.ReentrantLock;

/**场景1：如果发现该操作已经在执行中则不再执行（有状态执行）
a、用在定时任务时，如果任务执行时间可能超过下次计划执行时间，确保该有状态任务只有一个正在执行，忽略重复触发。
b、用在界面交互时点击执行较长时间请求操作时，防止多次点击导致后台重复执行（忽略重复触发）。

以上两种情况多用于进行非重要任务防止重复执行，（如：清除无用临时文件，检查某些资源的可用性，数据备份操作等）
tryLock() 方法试图立即锁定 Lock 实例。如果锁定成功，它将返回 true，如果 Lock 实例已被锁定该方法返回 false。这一方法永不阻塞。
 * @author DELL
 *
 */
public class Retry {

	public static void main(String[] args) {
		RetryDomain d = new RetryDomain();
		Thread t1 = new RetryThread(d);
		Thread t2 = new RetryThread2(d);
		t2.start();
		t1.start();
	}
}

class RetryThread extends Thread {

	private RetryDomain domainDemo;

	public RetryThread(RetryDomain domainDemo) {
		super();
		this.domainDemo = domainDemo;
	}

	@Override
	public void run(){
		domainDemo.test(Thread.currentThread().getName());
	}

}

class RetryThread2 extends Thread {

	private RetryDomain domainDemo;

	public RetryThread2(RetryDomain domainDemo) {
		super();
		this.domainDemo = domainDemo;
	}

	@Override
	public void run(){
		domainDemo.test(Thread.currentThread().getName());
	}

}

class RetryDomain{

	private ReentrantLock lock = new ReentrantLock();

	public void test(String tName){
		//tryLock()方法是有返回值的，它表示用来尝试获取锁，如果获取成功，则返回true，如果获取失败（即锁已被其他线程获取），则返回false，这个方法无论如何都会立即返回。在拿不到锁时不会一直在那等待。
		if (lock.tryLock()) {
			System.out.println(tName + "得到锁");
        	try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
	            lock.unlock();
	        }
	    }else{
	    	System.out.println(tName + "没得到锁");
	    }
	}

}