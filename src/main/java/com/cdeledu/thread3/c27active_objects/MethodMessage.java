package com.cdeledu.thread3.c27active_objects;

import java.util.Map;

/**作用是收集每一个接口的方法参数，并且提供execute方法供ActiveDaemonThread直接调用，该对象就是Worker Thread模型中的Product(附有使用说明书的半成品，
 * 等待流水线工人的加工)，execute方法则是加工该产品的说明书。
 * 其中params主要用来收集方法参数，orderService是具体的接口实现，每一个方法都会被拆分成不同的Message，在orderService中有两个方法，因此需要实现两个MethodMessage
 * @author Administrator
 *
 */
public abstract class MethodMessage {
	
	protected final Map<String, Object> params;
	protected final OrderService orderService;
	
	public MethodMessage(Map<String, Object> params, OrderService orderService) {
		this.params = params;
		this.orderService = orderService;
	}

	//抽象方法，扮演Worker Thread模型中的说明书
	public abstract void execute();
	
}
