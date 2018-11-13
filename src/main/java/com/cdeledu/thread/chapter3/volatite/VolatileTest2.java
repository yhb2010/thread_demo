package com.cdeledu.thread.chapter3.volatite;

//Java 语言提供了一种稍弱的同步机制,即 volatile 变量.用来确保将变量的更新操作通知到其他线程,保证了新值能立即同步到主内存,以及每次使用前立即从主内存刷新. 当把变量声明为volatile类型后,编译器与运行时都会注意到这个变量是共享的.
//volatile 变量对所有线程是立即可见的,对 volatile 变量所有的写操作都能立即反应到
//其他线程之中,换句话说:volatile 变量在各个线程中是一致的,所以基于 volatile 变量的运算是线程安全的.
//这句话论据貌似没有错,论点确实错的.

//下面代码无输出或输出1
public class VolatileTest2 {

	public static void main(String[] args) {
		VolatileDomain2 d = new VolatileDomain2();
		Thread t1 = new VolatileTest2Thread(d);
		Thread t2 = new VolatileTest2Thread2(d);
		t2.start();
		t1.start();
	}
}

class VolatileTest2Thread extends Thread {

	private VolatileDomain2 domainDemo;

	public VolatileTest2Thread(VolatileDomain2 domainDemo) {
		super();
		this.domainDemo = domainDemo;
	}

	@Override
	public void run(){
		domainDemo.writer();
	}

}

class VolatileTest2Thread2 extends Thread {

	private VolatileDomain2 domainDemo;

	public VolatileTest2Thread2(VolatileDomain2 domainDemo) {
		super();
		this.domainDemo = domainDemo;
	}

	@Override
	public void run(){
		domainDemo.reader();
	}

}

class VolatileDomain2{

	int a = 0;
	volatile boolean flag = false;

	public void writer(){
		a = 1;//1
		flag = true;//2
	}

	public void reader(){
		if(flag){//3
			//int i = a;//4
			System.out.println(a);
		}
	}
}