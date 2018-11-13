package com.cdeledu.thread3.c29event_driven.sync;

import java.util.HashMap;
import java.util.Map;

/**适合在单线程的情况下进行使用，因此不需要考虑多线程安全问题
 * @author Administrator
 *
 */
public class EventDispatcher implements DynamicRoute<Message> {
	
	//用于保存Channel和Message之间的关系
	private final Map<Class<? extends Message>, Channel> routeTable;

	public EventDispatcher() {
		//初始化RouteTable，但是在该实现中，我们使用HashMap作为路由表
		this.routeTable = new HashMap<>();
	}

	@Override
	public void registerChannel(Class<? extends Message> messageType, Channel<? extends Message> channel) {
		routeTable.put(messageType, channel);
	}

	@Override
	public void dispatch(Message message) {
		if(routeTable.containsKey(message.getType())){
			//直接获取对应的Channel处理Message
			routeTable.get(message.getType()).dispatch(message);
		}else{
			throw new MessageMatchException("can not match the channel for[" + message.getType() + "] type.");
		}
	}

}
