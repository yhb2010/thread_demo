package com.cdeledu.thread2.c3concurrent;

import java.util.concurrent.locks.LockSupport;

//是一个方便使用的线程阻塞工具。和Object.await相比，他不需要先获得某个对象的锁，也不会抛出InterruptedException异常。
//LockSupport提供park()和unpark()方法实现阻塞线程和解除线程阻塞,实现的阻塞和解除阻塞是基于”许可(permit)”作为关联,permit相当于一个信号量(0,1),默认是0. 线程之间不再需要一个Object或者其它变量来存储状态,不再需要关心对方的状态.
//park可以阻塞当前线程，类似的还有parkNanos、parkUntil等方法，他们实现了一个限时的等待。
//LockSupport比Object的wait/notify有两大优势：
//1、LockSupport不需要在同步代码块里 。所以线程间也不需要维护一个共享的同步对象了，实现了线程间的解耦。
//2、unpark函数可以先于park调用，所以不需要担心线程间的执行的先后顺序。
//LockSupport使用信号量机制，为每一个线程准备了一个许可，如果许可可用，那么park函数会立刻返回，并消费这个许可（也就是将许可变为不可用），如果许可不可用，就会阻塞。而unpark则使得一个许可变为不可用（许可不能累加，永远只有一个）。
//1.调用一次unpark,
//如果没有可用线程,则给定许可(permit就变成1(不会累计))
//如果有线程被阻塞,解除锁,同时park返回.
//如果给定线程没有启动,则该操作不能保证有任何效果.
//2.调用park,则会检测permit是否为1;
//如果为1则将permit变成0;
//如果不为1,则堵塞线程,直到permit变为1.
//3.park()和unpark()不会有“Thread.suspend和Thread.resume所可能引发的死锁” 问题,由于许可的存在,调用 park 的线程和另一个试图将其 unpark 的线程之间的竞争将保持活性。
//4.如果调用线程被中断,则park方法会返回.
//5.park 方法还可以在其他任何时间"毫无理由"地返回,因此通常必须在重新检查返回条件的循环里调用此方法。从这个意义上说,park 是“忙碌等待”的一种优化,它不会浪费这么多的时间进行自旋,但是必须将它与 unpark 配对使用才更高效
//6.unpark方法可以先于park调用。比如线程1调用unpark给线程2发了一个“许可“,那么当线程2调用park时,发现已经有“许可”了,那么它会马上再继续运行
public class LockSupportDemo {

	public static Object u = new Object();
	static ChangeObjectThread t1 = new ChangeObjectThread("t1");
	static ChangeObjectThread t2 = new ChangeObjectThread("t2");

	public static class ChangeObjectThread extends Thread{
		public ChangeObjectThread(String name){
			super.setName(name);
		}
		@Override
		public void run(){
			System.out.println("in " + getName());
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			LockSupport.park();
			System.out.println("in " + getName() + "结束");
		}
	}

	public static void main(String[] args) throws InterruptedException {
		t1.start();
		Thread.sleep(100);
		t2.start();
		LockSupport.unpark(t1);
		LockSupport.unpark(t2);
	}

}
