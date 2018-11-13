package com.cdeledu.thread2.c4.threadlocal;

public class Demo4 {

	public static class PaseDate implements Runnable{

		@Override
		public void run() {
			Context.setTrackerID(1);
			int i = Context.getTrackerID();
			try {
				Thread.sleep(100);
				i++;
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Context.setTrackerID(i);
			System.out.println(Context.getTrackerID());
		}

	}

	public static void main(String[] args) {
		for(int i=0; i<1000; i++){
			new Thread(new PaseDate()).start();
		}
	}

}
