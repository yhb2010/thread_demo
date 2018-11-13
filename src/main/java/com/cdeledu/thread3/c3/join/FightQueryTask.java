package com.cdeledu.thread3.c3.join;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class FightQueryTask extends Thread implements FightQuery {
	
	private final String origin;
	private final String destination;
	private final List<String> flightList = new ArrayList<>();

	public FightQueryTask(String airline, String origin, String destination) {
		super("[" + airline + "]");
		this.origin = origin;
		this.destination = destination;
	}
	
	@Override
	public void run(){
		System.out.printf("%s-query from %s to %s \n", getName(), origin, destination);
		int randomVal = ThreadLocalRandom.current().nextInt(10);
		try{
			//模拟调用各航空公司接口的用时
			TimeUnit.SECONDS.sleep(randomVal);
			flightList.add(getName() + "-" + randomVal);
			System.out.printf("The Fight: %s list query successful\n", getName());
		}catch(Exception e){
			
		}
	}

	@Override
	public List<String> get() {
		return flightList;
	}

}
