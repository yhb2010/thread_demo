package com.cdeledu.thread3.c29event_driven.sync;

/**类似于Event Loop，主要作用是帮助Event找到合适的Channel并且传送给它
 * @author Administrator
 *
 * @param <E>
 */
public interface DynamicRoute<E extends Message> {
	
	/**针对每一种message类型注册相关的Channel，只有找到合适的Channel该Message才会被处理
	 * @param messageType
	 * @param channel
	 */
	void registerChannel(Class<? extends E> messageType, Channel<? extends E> channel);
	
	/**为相应的Channel分配Message
	 * @param message
	 */
	void dispatch(E message);

}
