package com.cdeledu.thread.reentrantLock;

import java.util.concurrent.locks.ReentrantLock;

/**场景2：如果发现该操作已经在执行，等待一个一个执行（同步执行，类似synchronized）
这种比较常见大家也都在用，主要是防止资源使用冲突，保证同一时间内只有一个操作可以使用该资源。
但与synchronized的明显区别是性能优势（伴随jvm的优化这个差距在减小）。同时Lock有更灵活的锁定方式，公平锁与不公平锁，而synchronized永远是公平的。

这种情况主要用于对资源的争抢（如：文件操作，同步消息发送，有状态的操作等）

ReentrantLock默认情况下为不公平锁

不公平锁与公平锁的区别：

公平情况下，操作会排一个队按顺序执行，来保证执行顺序。（会消耗更多的时间来排队）
不公平情况下，是无序状态允许插队，jvm会自动计算如何处理更快速来调度插队。（如果不关心顺序，这个速度会更快）
 * @author DELL
 *
 */
public class Lock {

	public static void main(String[] args) {
		LockDomain d = new LockDomain();
		Thread t1 = new LockThread(d);
		Thread t2 = new LockThread2(d);
		t2.start();
		t1.start();
	}
}

class LockThread extends Thread {

	private LockDomain domainDemo;

	public LockThread(LockDomain domainDemo) {
		super();
		this.domainDemo = domainDemo;
	}

	@Override
	public void run(){
		domainDemo.test(Thread.currentThread().getName());
	}

}

class LockThread2 extends Thread {

	private LockDomain domainDemo;

	public LockThread2(LockDomain domainDemo) {
		super();
		this.domainDemo = domainDemo;
	}

	@Override
	public void run(){
		domainDemo.test(Thread.currentThread().getName());
	}

}

class LockDomain{

	private ReentrantLock lock = new ReentrantLock(); //参数默认false，不公平锁
	//private ReentrantLock lock = new ReentrantLock(true); //公平锁

	public void test(String tName){
    	try {
    		lock.lock(); //如果被其它资源锁定，会在此等待锁释放，达到暂停的效果
    		System.out.println(tName + "得到锁");
    		Thread.sleep(2000);
    		System.out.println(tName + "释放锁");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			//unlock()方法对Lock实例解锁。一个Lock实现将只允许锁定了该对象的线程来调用此方法。其他(没有锁定该Lock对象的线程)线程对unlock()方法的调用将会抛一个未检查异常(RuntimeException)。
            lock.unlock();
        }
	}

}