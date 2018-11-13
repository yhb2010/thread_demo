package com.cdeledu.thread.imooc;

public class JoinDemo2 {

    public static void main(String[] args) {
    	Thread t1 = new Thread(new R1(), "r1");
    	Thread t2 = new Thread(new R2(), "r2");
    	t1.start();
    	t2.start();
    	for(int i = 0; i<1000;i++){
			System.out.println(Thread.currentThread().getName() + ":" + i);
			try {
				//在main线程里调用t2的join方法，则main线程阻塞，等待t2线程结束，之后再进入就绪状态等待cpu分配时间片
				t2.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
    }

}

class R1 implements Runnable{

	@Override
	public void run() {
		for(int i = 0; i<1000;i++){
			System.out.println(Thread.currentThread().getName() + ":" + i);
		}
	}

}

class R2 implements Runnable{

	@Override
	public void run() {
		for(int i = 0; i<1000;i++){
			System.out.println(Thread.currentThread().getName() + ":" + i);
		}
	}

}