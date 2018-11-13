package com.cdeledu.thread3.c28event_bus;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**维护了topic和subscriber之间的关系，当有Event被post之后，Dispatcher需要知道该消息应该发送给哪个subscriber的实例和对应的方法，
 * subscriber对象没有任何特殊要求，就是普通的类不需要继承任何父类或者实现任何接口。
 * @author Administrator
 *
 */
class Registry {
	
	//存储subscriber集合和topic之间关系的map
	private final ConcurrentHashMap<String, ConcurrentLinkedQueue<Subscriber>> subscriberContainer = new ConcurrentHashMap<>();

	public void bind(Object subscriber) {
		//获取subscriber object的方法集合然后进行绑定
		List<Method> subscribeMethods = getSubscribeMethod(subscriber);
		subscribeMethods.forEach(m -> tierSubscriber(subscriber, m));
	}

	private List<Method> getSubscribeMethod(Object subscriber) {
		final List<Method> methods = new ArrayList<>();
		Class<?> temp = subscriber.getClass();
		//不断获取当前类和父类的所有@Subscribe方法
		while(temp != null){
			//获取所有的方法
			Method[] decleardMethods = temp.getDeclaredMethods();
			//只有public方法&&有一个参数&&最重要的是被@Subscribe标记的方法才符合回调方法
			Arrays.stream(decleardMethods)
				.filter(m -> m.isAnnotationPresent(Subscribe.class) && m.getParameterCount() == 1 && m.getModifiers() == Modifier.PUBLIC)
				.forEach(methods::add);
			temp = temp.getSuperclass();
		}
		return methods;
	}

	private Object tierSubscriber(Object subscriber, Method method) {
		final Subscribe subscribe = method.getDeclaredAnnotation(Subscribe.class);
		String topic = subscribe.topic();
		//当某topic没有Subscriber Queue的时候创建一个
		subscriberContainer.computeIfAbsent(topic, key -> new ConcurrentLinkedQueue<>());
		//创建一个Subscriber并且加入Subscriber列表中
		subscriberContainer.get(topic).add(new Subscriber(subscriber, method));
		return null;
	}

	public void unregister(Object subscriber) {
		//为了提高速度，只对Subscriber进行失效操作
		subscriberContainer.forEach((key, queue) -> queue.forEach(s -> {
			if(s.getSubscribeObject() == subscriber){
				s.setDisable(true);
			}
		}));
	}
	
	public ConcurrentLinkedQueue<Subscriber> scanSubscriber(final String topic){
		return subscriberContainer.get(topic);
	}

}
