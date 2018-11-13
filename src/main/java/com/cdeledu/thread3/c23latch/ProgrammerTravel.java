package com.cdeledu.thread3.c23latch;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**模拟程序员出游，大家通过不同的交通工具到达约定地点，然后一起去公园
 * @author Administrator
 *
 */
public class ProgrammerTravel extends Thread {

	private final Latch latch;
	//程序员
	private final String programmer;
	//交通工具
	private final String transportation;
	
	public ProgrammerTravel(Latch latch, String programmer, String transportation) {
		super();
		this.latch = latch;
		this.programmer = programmer;
		this.transportation = transportation;
	}
	
	@Override
	public void run(){
		System.out.println(programmer + " start take the transportation [" + transportation + "]");
		try {
			TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(10));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(programmer + " arrived by transportation [" + transportation + "]");
		latch.countDown();
	}
	
	public static void main(String[] args) throws InterruptedException {
		Latch latch = new CountDownLatch(4);
		new ProgrammerTravel(latch, "Alex", "Bus").start();
		new ProgrammerTravel(latch, "Gavin", "Walking").start();
		new ProgrammerTravel(latch, "Jack", "Subway").start();
		new ProgrammerTravel(latch, "Dillon", "Bicycle").start();
		latch.await();
		System.out.println("== all of programmer arrived ==");
		
		//带有超时的等待
		latch = new CountDownLatch(4);
		new ProgrammerTravel(latch, "Alex", "Bus").start();
		new ProgrammerTravel(latch, "Gavin", "Walking").start();
		new ProgrammerTravel(latch, "Jack", "Subway").start();
		new ProgrammerTravel(latch, "Dillon", "Bicycle").start();
		try {
			latch.await(TimeUnit.SECONDS, 3);
			System.out.println("== all of programmer arrived ==");
		} catch (WaitTimeoutException e) {
			e.printStackTrace();
		}
	}
	
}
