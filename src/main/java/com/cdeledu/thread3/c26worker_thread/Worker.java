package com.cdeledu.thread3.c26worker_thread;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**流水线工人是Thread的子类，不断的从流水线上提取产品然后进行加工，加工的方法是create()(对该产品的加工方法说明书)
 * @author Administrator
 *
 */
public class Worker extends Thread {

	private final ProductionChannel channel;
	//主要用于获取一个随机数，模拟加工一个产品需要耗时，当然每个工人的操作花费时间可能不一样
	private final static Random random = new Random(7);

	public Worker(String workerName, ProductionChannel channel){
		super(workerName);
		this.channel = channel;
	}

	@Override
	public void run(){
		while(true){
			try{
				//从传动带上获取产品
				Production production = channel.takeProduction();
				System.out.println(getName() + " process the " + production);
				//对产品进行加工
				production.create();
				TimeUnit.SECONDS.sleep(random.nextInt(10));
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}

}
