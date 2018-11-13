package com.cdeledu.thread2.c2thread;

/**错误的synchronize用法：
 * 执行这段代码的两个线程都指向了不同的Runnable实例。因此线程1会在进入同步方法前加锁自己的Runnable实例，线程2也关注于自己的对象锁，换言之，两个线程使用了两把不同的锁
 * @author DELL
 *
 */
public class WrongSync implements Runnable {
	static int i = 0;
	//解决办法就是把instance()方法写成静态的，这样请求的就是当前类的锁，而不是对象的锁
	//public static synchronized void instance(){
	public synchronized void instance(){
		i++;
	}

	public static void main(String[] args) throws InterruptedException {
		Thread t1 = new Thread(new WrongSync());
		Thread t2 = new Thread(new WrongSync());
		t1.start();
		t2.start();
		t1.join();
		t2.join();
		System.out.println(i);
	}

	@Override
	public void run() {
		for(int j=0;j<10000;j++){
			instance();
		}
	}

}
