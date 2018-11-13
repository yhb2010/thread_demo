package com.cdeledu.thread3.c27active_objects;

import com.cdeledu.thread3.c19future.Future;

public class Test {

	/**Active是主动的意思，Active Object是主动对象的意思，所谓主动对象就是指其拥有自己的独立线程，比如java.lang.Thread实例就是一个主动对象，
	 * 不过Active Object模式不仅仅是拥有独立的线程，他还可以接受异步消息，并且能够返回处理的结果。
	 * 频繁使用的System.gc()方法就是一个接受异步消息的主动对象，调用gc方法的线程和gc自身的执行线程并不是同一个线程。
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		OrderService orderService = OrderServiceFactory.toActiveObject(new OrderServiceImpl());
		Future<String> f = orderService.findOrderDetails(123);
		System.out.println("Return immediately");
		System.out.println(f.get());
		Thread.currentThread().join();
	}

}
