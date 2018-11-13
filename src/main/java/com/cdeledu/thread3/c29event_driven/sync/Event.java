package com.cdeledu.thread3.c29event_driven.sync;

/**Event是对Message的一个最简单的实现，在以后的使用中，将Event直接作为其他Message的基类即可
 * @author Administrator
 *
 */
public class Event implements Message {

	@Override
	public Class<? extends Message> getType() {
		return getClass();
	}

}
