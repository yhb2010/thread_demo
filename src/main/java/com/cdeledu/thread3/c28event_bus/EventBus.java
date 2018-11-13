package com.cdeledu.thread3.c28event_bus;

import java.util.concurrent.Executor;

/**同步EventBus是最核心的一个类，它实现了Bus的所有功能，但是该类对Event的广播推送采用同步方式。
 * @author Administrator
 *
 */
public class EventBus implements Bus {
	
	//用于维护Subscriber的注册表
	private final Registry registry = new Registry();
	//EventBus的名字
	private String busName;
	//默认的EventBus的名字
	private final static String DEFAULT_BUS_NAME = "default";
	//默认的topic名字
	private final static String DEFAULT_TOPIC = "default-topic";
	//用于分发广播消息到各个Subscriber的类
	private final Dispatcher dispatcher;
	
	public EventBus(){
		this(DEFAULT_BUS_NAME, null, Dispatcher.SEQ_EXECUTOR_SERVICE);
	}
	
	public EventBus(String busName){
		this(busName, null, Dispatcher.SEQ_EXECUTOR_SERVICE);
	}
	
	public EventBus(String busName, EventExceptionHandler exceptionHandler, Executor executor){
		this.busName = busName;
		this.dispatcher = Dispatcher.newDispatcher(exceptionHandler, executor);
	}
	
	public EventBus(EventExceptionHandler exceptionHandler){
		this(DEFAULT_BUS_NAME, exceptionHandler, Dispatcher.SEQ_EXECUTOR_SERVICE);
	}
	
	//将注册Subscriber的动作直接委托给Registry
	@Override
	public void register(Object subscriber) {
		registry.bind(subscriber);
	}

	@Override
	public void unregister(Object subscriber) {
		registry.unregister(subscriber);
	}

	@Override
	public void post(Object event) {
		post(event, DEFAULT_TOPIC);
	}

	//提交Event到指定的topic，具体的动作是由Dispathcer完成
	@Override
	public void post(Object event, String topic) {
		dispatcher.dispatch(this, registry, event, topic);
	}

	@Override
	public void close() {
		dispatcher.close();
	}

	@Override
	public String getBusName() {
		return busName;
	}

}
