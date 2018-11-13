package com.cdeledu.thread3.c27active_objects;

import java.util.concurrent.TimeUnit;

import com.cdeledu.thread3.c19future.Future;
import com.cdeledu.thread3.c19future.FutureService;

public class OrderServiceImpl implements OrderService {

	@Override
	public Future<String> findOrderDetails(long orderId) {
		//使用第19章开发的Future返回结果
		return FutureService.<Long, String>newService().submit(input -> {
			try{
				TimeUnit.SECONDS.sleep(10);
				System.out.println("prcess the orderID->" + orderId);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
			return "the order details information";
		}, orderId, null);
	}

	@Override
	public void order(String account, long orderId) {
		try{
			TimeUnit.SECONDS.sleep(10);
			System.out.println("process the order for account " + account + " , orderId " + orderId);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}

}
