package com.cdeledu.thread2.c3concurrent;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import javax.sound.midi.Track;

public class ConditionDemo implements Runnable{

	public static ReentrantLock lock = new ReentrantLock();
	public static Condition condition = lock.newCondition();

	@Override
	public void run() {
		try{
			lock.lock();
			condition.await();
			System.out.println("Thread os going on");
		}catch(InterruptedException e){
			e.printStackTrace();
		}finally{
			lock.unlock();
		}
	}

	public static void main(String[] args) throws InterruptedException {
		ConditionDemo tl = new ConditionDemo();
		Thread t1 = new Thread(tl);
		t1.start();
		Thread.sleep(2000);
		//通知线程t1继续执行
		lock.lock();
		//在调用signal之前，要求先获得锁。
		//在signal方法调用后，一般要释放相关的锁，谦让给被唤醒的线程，让他可以就行执行，比如，第37行就释放了锁，如果省略该行，
		//那么，虽然已经唤醒了线程t1，但是由于他无法重新获得锁，因而也无法真正的继续执行。
		condition.signal();
		lock.unlock();
	}

}
