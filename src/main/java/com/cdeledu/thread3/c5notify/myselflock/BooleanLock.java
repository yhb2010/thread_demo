package com.cdeledu.thread3.c5notify.myselflock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

import static java.lang.Thread.currentThread;

public class BooleanLock implements Lock {

	private Thread currentThread;//当前拥有锁的线程
	private boolean locked = false;//开关，false代表当前该锁没有被任何线程获得或者已经释放，true表示该锁已经被某个线程获得，该线程就是currentThread
	private final List<Thread> blockedList= new ArrayList<>();//用来存储哪些线程在获取当前线程时进入了阻塞状态
	
	@Override
	public void lock() throws InterruptedException {
		//使用同步代码块的方式进行方法同步
		synchronized (this) {
			//如果当前锁已经被某个线程获得，则该线程将加入阻塞队列，并且使当前线程wait释放对this monitor的所有权
			while(locked){
				final Thread tmp = currentThread();
				try{
					if(!blockedList.contains(tmp)){
						blockedList.add(tmp);
					}
					this.wait();
				}catch(InterruptedException e){
					//如果当前线程被中断，则从blockedList中将其删除，避免内存泄露
					blockedList.remove(tmp);
					throw e;
				}
			}
			//如果当前锁没有没其他线程获得，则该线程将尝试从阻塞队列中删除自己（如果未加入过阻塞队列，删除方法不会有任何影响；如果当前线程是从wait set中被唤醒，则需要从阻塞队列中将自己删除）
			blockedList.remove(currentThread());
			//locked开关被设置为true
			locked = true;
			//记录获取锁的线程
			this.currentThread = currentThread();
		}
	}
	@Override
	public void lock(long mills) throws InterruptedException, TimeoutException {
		synchronized (this) {
			//mills不合法，则默认调用lock方法，当然也可以抛出参数非法的异常
			if(mills <= 0){
				lock();
			}else{
				long remainMills = mills;
				long endMills = System.currentTimeMillis() + remainMills;
				while(locked){
					//如果remainMills小于等于0，说明当前线程被其它线程唤醒或者在指定的wait时间到了之后还没有获取锁，这种情况下抛出超时异常
					if(remainMills <= 0){
						throw new TimeoutException("can not get the lock during " + mills);
					}
					if(!blockedList.contains(currentThread())){
						blockedList.add(currentThread());
					}
					//等待remainMills时间，该值最开始是由其它线程传入的，但在多次wait的过程中会重新计算
					this.wait(remainMills);
					//重新计算remainMills
					remainMills = endMills - System.currentTimeMillis();
				}
				//获得该锁，并且从block列表中删除当前线程，将locked状态修改为true并且指定获得锁的线程为当前线程
				blockedList.remove(currentThread());
				locked = true;
				this.currentThread = currentThread();
			}
		}
	}
	@Override
	public void unlock() {
		synchronized (this) {
			//判断是否为获取锁的线程，只有加了锁的线程才有资格解锁
			if(currentThread == currentThread()){
				//将锁的locked状态修改为false
				locked = false;
				Optional.of(Thread.currentThread().getName() + " release the lock.").ifPresent(System.out::println);
				//通知其他在wait set中的线程，可以再次尝试获取锁了
				this.notifyAll();
			}
		}
	}
	@Override
	public List<Thread> getBlockedThreads() {
		return null;
	}
	
}
