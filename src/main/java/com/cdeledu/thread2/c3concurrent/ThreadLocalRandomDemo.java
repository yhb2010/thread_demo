package com.cdeledu.thread2.c3concurrent;

import java.util.concurrent.ThreadLocalRandom;

//Random:产生一个伪随机数（通过相同的老种子，产生的随机数是相同的），多线程下可能拿到相同的老种子。
//任何情况下都不要在多个线程间共享一个Random实例，而该把它放入ThreadLocal之中
//java7在所有情形下都更推荐使用ThreadLocalRandom，它向下兼容已有的代码且运营成本更低
public class ThreadLocalRandomDemo {

	public static void main(String[] args) {
		new Thread(){
			@Override
			public void run(){
				ThreadLocalRandom t = ThreadLocalRandom.current();
				System.out.println(t.nextInt(50));//随机生成0~50的随机数，不包括50
				System.out.println(t.nextInt(30, 50));//随机生成30~50的随机数，不包括50
			}
		}.start();
	}

}
