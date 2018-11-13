package com.cdeledu.thread3.c17读写锁;

import static java.lang.Thread.currentThread;

import java.util.concurrent.TimeUnit;

public class Test {
	
	private final static String text = "AB";
	
	public static void main(String[] args) {
		final ShareData s = new ShareData(10);
		for(int i=0; i<2; i++){
			new Thread(() -> {
				for(int index=0; index<text.length(); index++){
					try{
						char c = text.charAt(index);
						s.write(c);
						System.out.println(currentThread() + " write " + c);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}).start();
		}
		for(int i=0; i<10; i++){
			new Thread(() -> {
				while(true){
					try{
						System.out.println(currentThread() + " read " + new String(s.read()));
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}).start();
		}
		try {
			TimeUnit.SECONDS.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
