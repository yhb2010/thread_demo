package com.cdeledu.thread3.c27active_objects.demo2;

import com.cdeledu.thread3.c19future.Future;
import com.cdeledu.thread3.c27active_objects.OrderService;

public class Test {

	/**标准的Active Objects要将每一个方法都封装成Message，然后提交至Message队列中，这样的做法有点类似与远程方法调用RPC，如果某个接口的方法很多，
	 * 那么需要封装很多的Message类，同样如果有很多接口需要成为Active Object，则需要封装成非常多的Message类，这样显然不好，在本例中，将设计一个更加通用的
	 * Active Object框架，可以将任意的接口转换成Active Object。
	 * 我们使用jdk动态代理实现一个更为通用的Active Objects，可以将任意接口方法转换为Active Objects，当然如果接口方法有返回值，则必须要返回Future
	 * 类型才可以，否则抛出IllegalActiveMethod异常。
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		OrderService orderService = ActiveServiceFactory.active(new OrderServiceImpl());
		Future<String> f = orderService.findOrderDetails(123);
		System.out.println("Return immediately");
		System.out.println(f.get());
		Thread.currentThread().join();
	}

}
