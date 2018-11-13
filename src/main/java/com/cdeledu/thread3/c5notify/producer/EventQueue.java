package com.cdeledu.thread3.c5notify.producer;

import java.util.LinkedList;
import java.util.List;

public class EventQueue {
	
	private final int max;
	
	static class Event{
		int index;
		public Event(int index){
			this.index = index;
		}
		@Override
		public String toString(){
			return "event" + index;
		}
	}
	
	private final LinkedList<Event> eventQueue = new LinkedList<>();
	
	private final static int DEFAULT_MAX_EVENT = 10;
	
	public EventQueue(){
		this(DEFAULT_MAX_EVENT);
	}
	
	public EventQueue(int max){
		this.max = max;
	}
	
	public void offer(Event event){
		synchronized (eventQueue) {
			while(eventQueue.size() >= max){
				console("the queue is full");
				try {
					eventQueue.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			console("the new " + event + " is submitted!");
			eventQueue.add(event);
			eventQueue.notifyAll();
		}
	}
	
	public Event take(){
		synchronized (eventQueue) {
			while(eventQueue.size() == 0){
				console("the queue is empty");
				try {
					eventQueue.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			Event event = eventQueue.removeFirst();
			eventQueue.notifyAll();
			console("the event " + event + " is handled");
			return event;
		}
	}
	
	private void console(String msg){
		System.out.printf("%s:%s\n", Thread.currentThread().getName(), msg);
	}

}
