package com.cdeledu.thread2.c4.threadlocal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Demo2 {

	private static final ThreadLocal<SimpleDateFormat> tl = new ThreadLocal<>();

	public static class PaseDate implements Runnable{
		int i=0;
		public PaseDate(int i) {
			this.i= i;
		}
		@Override
		public void run() {
			//从这里可以看出，为每一个线程人手分配一个对象的工作并不是由ThreadLocal完成的，而是需要在应用层面保证的。如果在应用上
			//为每一个线程分配了相同的对象实例，那么ThreadLocal也不能保证线程安全，ThreadLocal只是起到了简单的容器作用。
			try {
				if(tl.get() == null){
					tl.set(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
				}
				Date t = tl.get().parse("2015-03-29 19:29:"+i%60);
				System.out.println(i+":"+t);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		ExecutorService es = Executors.newFixedThreadPool(10);
		for(int i=0;i<1000;i++){
			es.execute(new PaseDate(i));
		}
	}

}
