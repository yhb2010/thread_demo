package com.cdeledu.thread3.c5notify.myselflock;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class BooleanLockTest3 {
	
	private final Lock lock = new BooleanLock();
	
	//使用try finally语句块确保lock每次都能被正确释放
	public void syncMethod(){
		try{
			//加锁
			lock.lock(1000);
			int randomInt = ThreadLocalRandom.current().nextInt(10);
			System.out.println(Thread.currentThread()+ " get the lock.");
			TimeUnit.SECONDS.sleep(randomInt);
		}catch(InterruptedException | TimeoutException e){
			e.printStackTrace();
		}finally{
			//释放锁
			lock.unlock();
		}
	}
	
	//测试阻塞超时
	public static void main(String[] args) throws InterruptedException {
		BooleanLockTest3 t = new BooleanLockTest3();
		new Thread(t::syncMethod, "t1").start();
		TimeUnit.MICROSECONDS.sleep(2);
		Thread t2 = new Thread(t::syncMethod, "t2");
		t2.start();
		TimeUnit.MICROSECONDS.sleep(10);
	}

}
