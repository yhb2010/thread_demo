package com.cdeledu.thread3.c21threadlocal;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class Test2 {
	
	//使用了两个ThreadLocal，这也就意味着与之对应的ThreadLocalMap有两份Entry，其中Key分别是confContext、oresContext，value分别是Configuration、OtherResource
	public static void main(String[] args) {
		IntStream.range(0, 5).forEach(index -> new Thread(() -> {
			Configuration conf = ActionContext2.getConfContext();
			OtherResource ores = ActionContext2.getOresContext();
			conf.setName("cpu" + index);
			ores.setName("资源" + index);
			try {
				TimeUnit.MICROSECONDS.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println(Thread.currentThread().getName() + ": " + ActionContext2.getConfContext() + ", " + ActionContext2.getOresContext());
		}, "t-" + index).start());
		try {
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
