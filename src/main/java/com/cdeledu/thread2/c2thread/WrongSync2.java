package com.cdeledu.thread2.c2thread;

/**得到的结果不是20000，因为Integer属于不变对象，也就是对象一旦创建了就不能被修改。如果一个int值是1，要改为2，就是新建一个int。
 * 由于多个线程间，并不一定能够看到同一个对象（因为i对象一直在变），因此，两个线程每次加锁可能都加载不同的对象实例上，从而导致对临界区代码控制出现问题。
 * 解决办法是对instance加锁。
 * @author DELL
 *
 */
public class WrongSync2 implements Runnable {
	static Integer i = 0;
	static WrongSync2 instance = new WrongSync2();
	public void instance(){
		//synchronized (instance) {
		synchronized (i) {
			i++;
		}
	}

	public static void main(String[] args) throws InterruptedException {
		Thread t1 = new Thread(instance);
		Thread t2 = new Thread(instance);
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
