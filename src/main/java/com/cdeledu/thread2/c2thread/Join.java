package com.cdeledu.thread2.c2thread;

/**在一个线程里调用另一个线程的join方法，则当前线程会等待被调用线程。
 * join()方法表示无限等待，一直阻塞当前线程，直到目标线程执行完毕
 * join(long millis)给出了一个最大等待时间，如果超过给定时间目标线程还在执行，当前线程会因为等不及了，而继续往下执行
 *
 * yield()会使线程让出cpu，但是让出不代表当前线程不执行了。当前线程在让出cpu后，还会进行cpu的资源争夺
 * @author DELL
 *
 */
public class Join {

	public volatile static int i = 0;

	public static class T1 extends Thread{
		@Override
		public void run(){
			for(i = 0; i< 100000000; i++){

			}
		}
	}

	public static void main(String[] args) throws InterruptedException {
		Thread t1 = new T1();
		t1.start();
		t1.join();
		System.out.println(i);
	}

}
