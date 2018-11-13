package com.cdeledu.thread.chapter4;

/**
 * 早在JDK 1.2的版本中就提供java.lang.ThreadLocal，ThreadLocal为解决多线程程序的并发问题提供了一种新的思路。使用这个工具类可以很简洁地编写出优美的多线程程序。
　　当使用ThreadLocal维护变量时，ThreadLocal为每个使用该变量的线程提供独立的变量副本，所以每一个线程都可以独立地改变自己的副本，而不会影响其它线程所对应的副本。
　　从线程的角度看，目标变量就象是线程的本地变量，这也是类名中“Local”所要表达的意思。
　　所以，在Java中编写线程局部变量的代码相对来说要笨拙一些，因此造成线程局部变量没有在Java开发者中得到很好的普及。
	ThreadLocal的接口方法
	ThreadLocal类接口很简单，只有4个方法，我们先来了解一下：
	void set(Object value)设置当前线程的线程局部变量的值。
	public Object get()该方法返回当前线程所对应的线程局部变量。
	public void remove()将当前线程局部变量的值删除，目的是为了减少内存的占用，该方法是JDK 5.0新增的方法。需要指出的是，当线程结束后，对应该线程的局部变量将自动被垃圾回收，所以显式调用该方法清除线程的局部变量并不是必须的操作，但它可以加快内存回收的速度。
	protected Object initialValue()返回该线程局部变量的初始值，该方法是一个protected的方法，显然是为了让子类覆盖而设计的。这个方法是一个延迟调用方法，在线程第1次调用get()或set(Object)时才执行，并且仅执行1次。ThreadLocal中的缺省实现直接返回一个null。
　　值得一提的是，在JDK5.0中，ThreadLocal已经支持泛型，该类的类名已经变为ThreadLocal<T>。API方法也相应进行了调整，新版本的API方法分别是void set(T value)、T get()以及T initialValue()。
　　ThreadLocal是如何做到为每一个线程维护变量的副本的呢？其实实现的思路很简单：在ThreadLocal类中有一个Map，用于存储每一个线程的变量副本，Map中元素的键为线程对象，而值对应线程的变量副本。我们自己就可以提供一个简单的实现版本：
 *
 * */
public class ThreadLocal1 {

	// ①通过匿名内部类覆盖ThreadLocal的initialValue()方法，指定初始值
	//ThreadLocal只提供了浅拷贝，可以通过重写initialValue函数实现自己的深拷贝
	private static ThreadLocal<Integer> seqNum = new ThreadLocal<Integer>() {
		public Integer initialValue() {
			return 0;
		}
	};

	// ②获取下一个序列值
	public int getNextNum() {
		seqNum.set(seqNum.get() + 1);
		return seqNum.get();
	}

	public static void main(String[] args) {
		ThreadLocal1 sn = new ThreadLocal1();
		// ③ 3个线程共享sn，各自产生序列号
		TestClient t1 = new TestClient(sn);
		TestClient t2 = new TestClient(sn);
		TestClient t3 = new TestClient(sn);
		t1.start();
		t2.start();
		t3.start();
	}

	private static class TestClient extends Thread {
		private ThreadLocal1 sn;

		public TestClient(ThreadLocal1 sn) {
			this.sn = sn;
		}

		/**
		 * 通常我们通过匿名内部类的方式定义ThreadLocal的子类，提供初始的变量值，如例子中①处所示。TestClient线程产生一组序列号，在③处，我们生成3个TestClient，它们共享同一个TestNum实例。运行以上代码，在控制台上输出以下的结果：
			thread[Thread-0] --> sn[1]
			thread[Thread-1] --> sn[1]
			thread[Thread-2] --> sn[1]
			thread[Thread-1] --> sn[2]
			thread[Thread-0] --> sn[2]
			thread[Thread-1] --> sn[3]
			thread[Thread-2] --> sn[2]
			thread[Thread-0] --> sn[3]
			thread[Thread-2] --> sn[3]
		 *
		 * */
		public void run() {
			for (int i = 0; i < 3; i++) {
				// ④每个线程打出3个序列值
				System.out.println("thread[" + Thread.currentThread().getName()
						+ "] --> sn[" + sn.getNextNum() + "]");
			}
		}
	}

}
