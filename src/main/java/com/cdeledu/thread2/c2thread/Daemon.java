package com.cdeledu.thread2.c2thread;

/**守护线程是一种特殊的线程，它是系统的守护者，在后台默默的完成一些工作。与之对应的用户线程，用户线程可以认为是系统的工作线程，它会完成这个程序应该
 * 要完成的业务操作。如果用户线程全部结束了，这也意味着这个程序实际上无事可做了。守护线程要守护的对象已经不存在了，那么整个应用程序就自然应该结束了。
 * 因此，当一个java应用内，只有守护线程时，java虚拟机就会自然退出了。
 * @author DELL
 *
 */
public class Daemon {

	public static class T1 extends Thread{
		@Override
		public void run(){
			while(true){
				System.out.println("i am alive");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) throws InterruptedException {
		Thread t1 = new T1();
		//必须写在开始，如果写在start后面，会报异常，但程序可以正常执行，只是被当做一个用户线程而已。
		//因此，如果不小心忽略了异常信息，你就会奇怪为什么程序永远停不下来了。
		//在这个程序中，由于t1是守护线程，系统中只有主线程main为用户线程，因此在main休眠了2秒后，整个线程也随之结束。但如果不把t1设置为守护线程，main线程结束后，t1线程还会不停地打印，永远不会结束。
		t1.setDaemon(true);
		t1.start();
		Thread.sleep(2000);
	}

}
