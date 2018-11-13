package com.cdeledu.thread3.c28event_bus;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**类似于mq
 * @author Administrator
 *
 */
public class Test {
	
	public static void main(String[] args) {
		m1();
		System.out.println("================");
		m2();
	}

	private static void m2() {
		Bus bus2 = new AsyncEventBus("TestBus2", (ThreadPoolExecutor)Executors.newFixedThreadPool(10));
		bus2.register(new SimpleSubscriber1());
		bus2.register(new SimpleSubscriber2());
		bus2.post("Hello");
		System.out.println("----------------");
		bus2.post("Hello", "test");
	}

	private static void m1() {
		Bus bus = new EventBus("TestBus");
		bus.register(new SimpleSubscriber1());
		bus.register(new SimpleSubscriber2());
		bus.post("Hello");
		System.out.println("----------------");
		bus.post("Hello", "test");
	}

}

class SimpleSubscriber1{
	@Subscribe
	public void method1(String message){
		System.out.println("SimpleSubscriber1:method1:" + message);
	}
	@Subscribe(topic = "test")
	public void method2(String message){
		System.out.println("SimpleSubscriber1:method2:" + message);
	}
}

class SimpleSubscriber2{
	@Subscribe
	public void method1(String message){
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("SimpleSubscriber2:method1:" + message);
	}
	@Subscribe(topic = "test")
	public void method2(String message){
		System.out.println("SimpleSubscriber2:method2:" + message);
	}
}