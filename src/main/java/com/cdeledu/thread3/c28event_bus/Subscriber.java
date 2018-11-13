package com.cdeledu.thread3.c28event_bus;

import java.lang.reflect.Method;

/**封装了对象实例，和被@Subscribe标记的方法，也就是说一个对象实例有可能会被封装成若干个Subscriber
 * @author Administrator
 *
 */
public class Subscriber {
	
	private final Object subscribeObject;
	private final Method subscribeMethod;
	private boolean disable = false;
	
	public Subscriber(Object subscribeObject, Method subscribeMethod) {
		this.subscribeObject = subscribeObject;
		this.subscribeMethod = subscribeMethod;
	}

	public Object getSubscribeObject() {
		return subscribeObject;
	}

	public Method getSubscribeMethod() {
		return subscribeMethod;
	}

	public boolean isDisable() {
		return disable;
	}

	public void setDisable(boolean disable) {
		this.disable = disable;
	}

}
