package com.cdeledu.thread3.c27active_objects;

public class OrderServiceFactory {
	
	private OrderServiceFactory(){
		
	}
	
	//将activeMessageQueue定义成static的目的是，保持其在整个jvm中是唯一的，并且ActiveDaemonThread会在此刻启动
	private static ActiveMessageQueue activeMessageQueue = new ActiveMessageQueue();
	
	public static OrderService toActiveObject(OrderService orderService){
		return new OrderServiceProxy(orderService, activeMessageQueue);
	}

}
