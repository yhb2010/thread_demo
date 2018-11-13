package com.cdeledu.thread2.c3concurrent;

import java.util.concurrent.locks.ReentrantLock;

public class IntLock implements Runnable {

	public static ReentrantLock lock1 = new ReentrantLock();
	public static ReentrantLock lock2 = new ReentrantLock();
	int lock;

	public IntLock(int lock){
		this.lock = lock;
	}

	@Override
	public void run() {
		try{
			if(lock == 1){
				//可以对中断进行响应的锁申请动作，即在等待锁的过程中，可以响应中断
				lock1.lockInterruptibly();
				System.out.println(Thread.currentThread().getId() + "申请到lock1");
				try{
					Thread.sleep(500);
				}catch(InterruptedException e){

				}
				lock2.lockInterruptibly();
			}else{
				lock2.lockInterruptibly();
				System.out.println(Thread.currentThread().getId() + "申请到lock2");
				try{
					Thread.sleep(500);
				}catch(InterruptedException e){

				}
				lock1.lockInterruptibly();
			}
		}catch(InterruptedException e){
			e.printStackTrace();
			System.out.println(Thread.currentThread().getId() + "被中断了");
		}finally{
			//isHeldByCurrentThread()查询当前线程是否保持此锁定
			if(lock1.isHeldByCurrentThread()){
				lock1.unlock();
				System.out.println(Thread.currentThread().getId() + "释放锁lock1");
			}
			if(lock2.isHeldByCurrentThread()){
				lock2.unlock();
				System.out.println(Thread.currentThread().getId() + "释放锁lock2");
			}
			System.out.println(Thread.currentThread().getId() + "线程退出");
		}
	}

	public static void main(String[] args) throws InterruptedException {
		IntLock r1 = new IntLock(1);
		IntLock r2 = new IntLock(2);
		Thread t1 = new Thread(r1);
		Thread t2 = new Thread(r2);
		//t1和t2启动后，t1先占用lock1，在占用lock2；t2先占用lock2，再请求lock1,。因此，很容易形成t1、t2之间互相等待。
		t1.start();
		t2.start();
		//主线程处于休眠，此时，两个线程处于死锁的状态
		Thread.sleep(1000);
		//这里由于t2线程被中断，故t2会放弃对lock1的申请，同时释放已获得的lock2，这个操作导致t1线程可以顺利得到lock2而继续执行下去
		t2.interrupt();
	}

}
