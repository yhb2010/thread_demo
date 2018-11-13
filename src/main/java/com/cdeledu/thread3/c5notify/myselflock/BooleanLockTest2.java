package com.cdeledu.thread3.c5notify.myselflock;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class BooleanLockTest2 {
	
	private final Lock lock = new BooleanLock();
	
	//使用try finally语句块确保lock每次都能被正确释放
	public void syncMethod(){
		try{
			//加锁
			lock.lock();
			int randomInt = ThreadLocalRandom.current().nextInt(10);
			System.out.println(Thread.currentThread()+ " get the lock.");
			TimeUnit.SECONDS.sleep(randomInt);
		}catch(InterruptedException e){
			e.printStackTrace();
		}finally{
			//释放锁
			lock.unlock();
		}
	}
	
	//测试可中断，这可以弥补synchronized不能中断的缺陷
	public static void main(String[] args) throws InterruptedException {
		BooleanLockTest2 t = new BooleanLockTest2();
		new Thread(t::syncMethod, "t1").start();
		TimeUnit.MICROSECONDS.sleep(2);
		Thread t2 = new Thread(t::syncMethod, "t2");
		t2.start();
		TimeUnit.MICROSECONDS.sleep(10);
		t2.interrupt();
	}

}
