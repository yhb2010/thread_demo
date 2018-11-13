package com.cdeledu.thread3.c28event_bus;

import java.lang.reflect.Method;

/**Event接口提供了获取消息源、消息体、以及该消息是由哪一个Subscriber的哪一个subscribe方法所接受，主要用于消息推送出错时被回调接口EventExceptionHandler
 * 使用。
 * @author Administrator
 *
 */
public interface EventContext {
	
	String getSource();
	
	Object getSubscriber();
	
	Method getSubscribe();
	
	Object getEvent();

}
