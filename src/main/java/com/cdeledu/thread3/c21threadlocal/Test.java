package com.cdeledu.thread3.c21threadlocal;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import com.cdeledu.thread3.c21threadlocal.ActionContext.Context;

public class Test {
	
	//只是用了一个ThreadLocal，这也就意味着与之对应的ThreadLocalMap有一份Entry，其中Key是ThreadLocal，value是Context
	public static void main(String[] args) {
		IntStream.range(0, 5).forEach(index -> new Thread(() -> {
			Context context = ActionContext.get();
			context.setConf(new Configuration("cpu" + index));
			context.setOres(new OtherResource("资源" + index));
			try {
				TimeUnit.MICROSECONDS.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println(Thread.currentThread().getName() + ": " + context);
		}, "t-" + index).start());
		try {
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
