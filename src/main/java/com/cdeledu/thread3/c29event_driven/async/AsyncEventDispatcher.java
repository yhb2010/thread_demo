package com.cdeledu.thread3.c29event_driven.async;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.cdeledu.thread3.c29event_driven.sync.Channel;
import com.cdeledu.thread3.c29event_driven.sync.DynamicRoute;
import com.cdeledu.thread3.c29event_driven.sync.Event;
import com.cdeledu.thread3.c29event_driven.sync.MessageMatchException;

/**适合在单线程的情况下进行使用，因此不需要考虑多线程安全问题
 * @author Administrator
 *
 */
public class AsyncEventDispatcher implements DynamicRoute<Event> {
	
	//用于保存Channel和Message之间的关系
	private final Map<Class<? extends Event>, AsyncChannel> routeTable;

	public AsyncEventDispatcher() {
		//初始化RouteTable，但是在该实现中，我们使用HashMap作为路由表
		this.routeTable = new ConcurrentHashMap<>();
	}

	@Override
	public void registerChannel(Class<? extends Event> messageType, Channel<? extends Event> channel) {
		if(!(channel instanceof AsyncChannel)){
			throw new IllegalArgumentException("the channel must be AsyncChannel type.");
		}
		routeTable.put(messageType, (AsyncChannel)channel);
	}

	@Override
	public void dispatch(Event message) {
		if(routeTable.containsKey(message.getType())){
			//直接获取对应的Channel处理Message
			routeTable.get(message.getType()).dispatch(message);
		}else{
			throw new MessageMatchException("can not match the channel for[" + message.getType() + "] type.");
		}
	}
	
	public void shutdown(){
		routeTable.values().forEach(AsyncChannel::stop); 
	}

}
