package com.cdeledu.thread3.c27active_objects.demo2;

public class ActiveDaemonThread extends Thread {

	private final ActiveMessageQueue queue;
	
	public ActiveDaemonThread(ActiveMessageQueue queue) {
		super("ActiveDaemonThread");
		this.queue = queue;
		//ActiveDaemonThread为守护线程
		setDaemon(true);
	}
	
	@Override
	public void run(){
		for(;;){
			//从ActiveMessage队列中获取一个ActiveMessage，然后执行execute方法
			ActiveMessage activeMessage = queue.take();
			activeMessage.execute();
		}
	}

}
