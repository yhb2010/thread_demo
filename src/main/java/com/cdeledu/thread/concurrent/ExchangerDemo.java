package com.cdeledu.thread.concurrent;

import java.util.concurrent.Exchanger;

//java.util.concurrent.Exchanger 类表示一种两个线程可以进行互相交换对象的汇合点。
//两个线程通过一个 Exchanger 交换对象。
//交换对象的动作由 Exchanger 的两个 exchange() 方法的其中一个完成。
public class ExchangerDemo {

	public static void main(String[] args) {
		Exchanger exchanger = new Exchanger();

		ExchangerRunnable exchangerRunnable1 =
		        new ExchangerRunnable(exchanger, "A");

		ExchangerRunnable exchangerRunnable2 =
		        new ExchangerRunnable(exchanger, "B");

		new Thread(exchangerRunnable1).start();
		new Thread(exchangerRunnable2).start();
	}

}
