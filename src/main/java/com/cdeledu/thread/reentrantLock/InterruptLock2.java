package com.cdeledu.thread.reentrantLock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * lock 与 lockInterruptibly比较区别在于：
lock 优先考虑获取锁，待获取锁成功后，才响应中断。
lockInterruptibly 优先考虑响应中断，而不是响应锁的普通获取或重入获取。

详细区别：
ReentrantLock.lockInterruptibly允许在等待时由其它线程调用等待线程的Thread.interrupt方法来中断等待线程的等待而直接返回，这时不用获取锁，
而会抛出一个InterruptedException。 ReentrantLock.lock方法不允许Thread.interrupt中断,即使检测到Thread.isInterrupted,一样会继续尝试
获取锁，失败则继续休眠。只是在最后获取锁成功后再把当前线程置为interrupted状态,然后再中断线程。
 * @author DELL
 *
 */
public class InterruptLock2 {
	public static void main(String[] args) {
		InterruptLockDomain2 d = new InterruptLockDomain2();
		Thread t1 = new InterruptLock2Thread(d);
		Thread t2 = new InterruptLock2Thread2(d);
		t1.start();
		t2.start();
		t2.interrupt();
	}
}

class InterruptLock2Thread extends Thread {

	private InterruptLockDomain2 domainDemo;

	public InterruptLock2Thread(InterruptLockDomain2 domainDemo) {
		super("A");
		this.domainDemo = domainDemo;
	}

	@Override
	public void run(){
		domainDemo.test(Thread.currentThread().getName());
	}

}

class InterruptLock2Thread2 extends Thread {

	private InterruptLockDomain2 domainDemo;

	public InterruptLock2Thread2(InterruptLockDomain2 domainDemo) {
		super("B");
		this.domainDemo = domainDemo;
	}

	@Override
	public void run(){
		domainDemo.test(Thread.currentThread().getName());
	}

}

class InterruptLockDomain2{

	private ReentrantLock lock = new ReentrantLock(); //参数默认false，不公平锁
	//private ReentrantLock lock = new ReentrantLock(true); //公平锁

	public void test(String tName){
    	try {
    		lock.lockInterruptibly();
    		System.out.println(tName + "得到锁");
    		Thread.sleep(2000);
    		System.out.println(tName + "释放锁");
		} catch (InterruptedException e) {
			System.out.println(tName + "被打断");
			e.printStackTrace();
		} finally {
            lock.unlock();
        }
	}

}