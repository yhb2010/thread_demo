package com.cdeledu.thread3.c27active_objects;

import java.util.HashMap;
import java.util.Map;

import com.cdeledu.thread3.c19future.Future;

/**作用是把OrderService中的每一个方法封装成MethodMessage，然后提交给ActiveMessage队列，在使用OrderService接口方法的时候，实际上是
 * 在调用OrderServiceProxy中的方法。
 * OrderServiceProxy的主要作用是将OrderService接口定义的方法封装成MethodMessage，然后offer给ActiveMessage。若是无返回值的方法，
 * 则只需要提交Message到ActiveMessageQueue即可，若是有返回值的方法，需要返回一个ActiveFuture，该Future的作用是可以立即返回，当调用线程获取
 * 结果时将进入阻塞状态。
 * @author Administrator
 *
 */
public class OrderServiceProxy implements OrderService {
	
	private final OrderService orderService;
	private final ActiveMessageQueue activeMessageQueue;

	public OrderServiceProxy(OrderService orderService, ActiveMessageQueue activeMessageQueue) {
		this.orderService = orderService;
		this.activeMessageQueue = activeMessageQueue;
	}

	@Override
	public Future<String> findOrderDetails(long orderId) {
		//定义一个ActiveFuture，并且可支持立即返回
		final ActiveFuture<String> activeFuture = new ActiveFuture<>();
		//收集方法的入参以及返回的ActiveFuture封装成MethodMessage
		Map<String, Object> params = new HashMap<>();
		params.put("orderId", orderId);
		params.put("activeFuture", activeFuture);
		MethodMessage message = new FindOrderDetailsMessage(params, orderService);
		activeMessageQueue.offer(message);
		return activeFuture;
	}

	@Override
	public void order(String account, long orderId) {
		//收集方法参数，并且封装成MethodMessage，然后offer至队列中
		Map<String, Object> params = new HashMap<>();
		params.put("account", account);
		params.put("orderId", orderId);
		MethodMessage message = new OrderMessage(params, orderService);
		activeMessageQueue.offer(message);
	}

}
