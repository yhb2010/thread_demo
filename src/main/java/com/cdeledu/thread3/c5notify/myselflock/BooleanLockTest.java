package com.cdeledu.thread3.c5notify.myselflock;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class BooleanLockTest {
	
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
	
	public static void main(String[] args) {
		BooleanLockTest t = new BooleanLockTest();
		IntStream.range(0, 10).mapToObj(i -> new Thread(t::syncMethod)).forEach(Thread::start);
	}

}
