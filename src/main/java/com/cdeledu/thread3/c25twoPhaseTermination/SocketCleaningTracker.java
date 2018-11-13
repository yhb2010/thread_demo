package com.cdeledu.thread3.c25twoPhaseTermination;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.net.Socket;

public class SocketCleaningTracker {

	//定义ReferenceQueue
	private static final ReferenceQueue<Object> queue = new ReferenceQueue<>();
	
	static{
		//启动Cleaner线程
		new Cleaner().start();
	}
	
	public static void tracker(Socket socket) {
		new Tracker(socket, queue);
	}
	
	private static class Cleaner extends Thread{
		private Cleaner(){
			super("SocketCleaningTracker");
			setDaemon(true);
		}
		@Override
		public void run(){
			for(;;){
				try{
					//当Tracker被垃圾回收器回收时会加入queue中
					Tracker tracker = (Tracker)queue.remove();
					tracker.close();
				}catch (Exception e) {
				}
			}
		}
	}
	
	//Tracker是一个PhantomReference的子类
	private static class Tracker extends PhantomReference<Object>{
		private final Socket socket;
		public Tracker(Socket socket, ReferenceQueue<? super Object> queue) {
			super(socket, queue);
			this.socket = socket;
		}
		public void close(){
			try{
				socket.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
	}

}
