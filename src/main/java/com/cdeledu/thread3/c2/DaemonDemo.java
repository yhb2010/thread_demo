package com.cdeledu.thread3.c2;

public class DaemonDemo {
	
	//得出结论：
	//如果jvm中的所有线程都是守护线程，则jvm线程会退出。守护线程经常用作执行一些后台任务，因此有时他也叫做后台线程，当你希望关闭某些线程的时候，或者退出jvm进行的时候，一些线程能够自动关闭，此时就可以考虑用守护线程为你完成这样的工作。
	public static void main(String[] args) {
		Thread t = new Thread(() -> {
			while(true){
				try{
					Thread.sleep(1);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		t.setDaemon(true);
		t.start();
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Main thread finished lifecycle");
	}

}
