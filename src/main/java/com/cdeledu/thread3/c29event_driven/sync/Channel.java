package com.cdeledu.thread3.c29event_driven.sync;

/**用于接受来自Event Loop分配的消息，每一个Channel负责处理一种类型的消息(当然这取决你对消息如何进行分配)
 * @author Administrator
 *
 * @param <E>
 */
public interface Channel<E extends Message> {
	
	//dispatch方法用于负责Message的调度
	void dispatch(E message);
	
}
