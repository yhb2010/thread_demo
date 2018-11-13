package com.cdeledu.thread3.c27active_objects;

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
			//从MethodMessage队列中获取一个MethodMessage，然后执行execute方法
			MethodMessage methodMessage = queue.take();
			methodMessage.execute();
		}
	}

}
