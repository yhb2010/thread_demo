package com.cdeledu.thread.chapter4;

import java.util.concurrent.TimeUnit;

/**
 * ThreadLocal即线程变量，是一个以ThreadLocal对象为键、任意对象为值的存储结构。这个结构被附带在线程上，也就是说一个线程可以根据一个ThreadLocal
 * 对象查询到绑定在这个线程上的值。
 * 可以通过set方法来设置一个值，在当前线程下再通过get方法获取到原先设置的值。
 * ThreadLocal2可以被用在方法调用耗时统计上，在方法的入口前执行begin，调用后执行end，好处是两个方法的调用不用在一个方法或者类中，比如在aop中，可以
 * 在方法调用前的切入点执行begin，而在方法调用后的切入点执行end，这样依旧可以获取方法的执行耗时。
 * */
public class ThreadLocal2 {

	//第一个get方法被调用时会进行初始化（如果set方法没有调用），每个线程会调用一次
	private static ThreadLocal<Long> Time = new ThreadLocal<Long>() {
		public Long initialValue() {
			return System.currentTimeMillis();
		}
	};

	public static final void begin() {
		Time.set(System.currentTimeMillis());
	}

	public static final long end() {
		return System.currentTimeMillis() - Time.get();
	}

	public static void main(String[] args) throws InterruptedException {
		ThreadLocal2.begin();
		TimeUnit.SECONDS.sleep(1);
		System.out.println("Cost: " + ThreadLocal2.end() + " mills.");
	}

}
