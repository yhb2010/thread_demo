package com.cdeledu.thread3.c27active_objects;

import java.util.Map;

import com.cdeledu.thread3.c19future.Future;

public class FindOrderDetailsMessage extends MethodMessage {

	public FindOrderDetailsMessage(Map<String, Object> params, OrderService orderService){
		super(params, orderService);
	}
	
	@Override
	public void execute() {
		// 获取参数
		long orderId = (Long)params.get("orderId");
		ActiveFuture<String> activeFuture = (ActiveFuture<String>)params.get("activeFuture");
		Future<String> realFuture = orderService.findOrderDetails(orderId);
		try{
			//此方法会导致阻塞直到findOrderDetails方法完全执行结束
			String result = realFuture.get();
			//当findOrderDetails执行结束时，将结果通过finish的方法传递给activeFuture
			activeFuture.finish(result);
		}catch(InterruptedException e){
			activeFuture.finish(null);
		}
	}

}
