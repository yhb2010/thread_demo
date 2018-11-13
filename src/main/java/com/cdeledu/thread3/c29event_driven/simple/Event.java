package com.cdeledu.thread3.c29event_driven.simple;

/**需要被处理的数据
 * @author Administrator
 *
 */
public class Event {
	
	private final String type;
	private final String data;
	
	public Event(String type, String data) {
		this.type = type;
		this.data = data;
	}

	public String getType() {
		return type;
	}

	public String getData() {
		return data;
	}

}
