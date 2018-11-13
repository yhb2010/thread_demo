package com.cdeledu.thread3.c9initclass;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class ClassInit {

	static{
		try{
			System.out.println(Thread.currentThread().getName() + " the classinit static code block will be invoke.");
			TimeUnit.SECONDS.sleep(1);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}
	
	//同一时间只有一个线程执行到静态代码块中的内容，并且静态代码块仅仅只会被执行一次，jvm保证了<clinit>()方法在多线程的执行环境下的同步语义。
	public static void main(String[] args) {
		IntStream.range(0, 5).forEach(i -> new Thread(ClassInit::new, "t"+i));
	}

}
