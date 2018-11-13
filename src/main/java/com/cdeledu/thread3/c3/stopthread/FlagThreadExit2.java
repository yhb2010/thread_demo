package com.cdeledu.thread3.c3.stopthread;

import java.util.concurrent.TimeUnit;

public class FlagThreadExit2 {

	static class MyTask extends Thread {
		
		private boolean closed = false;
		
		@Override
		public void run(){
			System.out.println("i will start work");
			while(!closed && !isInterrupted()){
				//working
			}
			System.out.println("i will be exiting");
		}
		
		public void close(){
			closed = true;
			interrupt();
		}
		
	}
	
	public static void main(String[] args) throws InterruptedException {
		MyTask t = new MyTask();
		t.start();
		TimeUnit.SECONDS.sleep(5);
		System.out.println("System will be shutdown");
		t.close();
	}
	
}
