package com.cdeledu.thread2.c2thread;

/**通过线程中断方式停止：
 * 1、Thread.interrupt()//中断线程
 * 2、Thread.isInterrupted()//判断是否被中断
 * 3、Thread.interrupted()//判断是否被中断，并清除当前中断状态
 * 严格来说，中断并不会立刻退出程序，而是给线程发送一个通知，告知目标线程，有人希望你退出了。至于目标线程接到通知后如何处理，则完全由目标现成自行决定。
 * @author DELL
 *
 */
public class WaitAndNotify {

	final static Object object = new Object();

	public static class T1 extends Thread{
		@Override
		public void run(){
			synchronized (object) {
				System.out.println(System.currentTimeMillis() + ":T1 start!");
				try {
					System.out.println(System.currentTimeMillis() + ":T1 wait for object!");
					//wait和sleep都可以让线程等待若干时间，除了wait方法可以被唤醒外，另外一个主要的区别是wait方法会释放目标对象的锁。
					object.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println(System.currentTimeMillis() + ":T1 end!");
			}
		}
	}

	public static class T2 extends Thread{
		@Override
		public void run(){
			synchronized (object) {
				System.out.println(System.currentTimeMillis() + ":T2 start!");
				object.notify();
				System.out.println(System.currentTimeMillis() + ":T2 end!");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) throws InterruptedException {
		Thread t1 = new T1();
		Thread t2 = new T2();
		t1.start();
		t2.start();
	}

}
