package com.cdeledu.thread3.c29event_driven.simple;

import java.util.LinkedList;
import java.util.Queue;

/**EDA(Event-Driven-Architecture)是一种实现组件之间松耦合、易扩展的架构方式。
 * Event：需要被处理的数据
 * Event Handler：处理Event的方式方法
 * Event Loop：维护Event和Event Handler之间的交互流程
 * @author Administrator
 *
 */
public class FooEventDrivenExample {
	
	/**Event Handler：处理event的方法，比如一些filtering或者transforming数据的操作等
	 * @param e
	 */
	public static void handleEventA(Event e){
		System.out.println(e.getData().toLowerCase());
	}
	
	public static void handleEventB(Event e){
		System.out.println(e.getData().toLowerCase());
	}
	
	public static void main(String[] args) {
		//接受事件消息的通道
		Queue<Event> events = new LinkedList<>();
		events.add(new Event("A", "Hello"));
		events.add(new Event("A", "i am event a"));
		events.add(new Event("B", "i am event b"));
		events.add(new Event("B", "World"));
		Event e;
		//Event loop：维护Event和Event Handler之间的交互流程
		while(!events.isEmpty()){
			//从消息队列中不断移除，根据不同的类型进行处理
			e = events.remove();
			switch (e.getType()) {
			case "A":
				handleEventA(e);
				break;
			case "B":
				handleEventB(e);
				break;
			}
		}
	}

}
