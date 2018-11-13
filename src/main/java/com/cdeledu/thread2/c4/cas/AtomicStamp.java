package com.cdeledu.thread2.c4.cas;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

//产生ABA的原因是没有维护状态数据，使用AtomicStampReference可以解决。
public class AtomicStamp {

	static AtomicStampedReference<Integer> money = new AtomicStampedReference<>(19, 0);

	public static void main(String[] args) {
		//模拟多个线程同时更新后台数据库，为用户充值
		for(int i=0;i<3;i++){
			new Thread(){
				public void run(){
					int stamp = money.getStamp();
					while(true){
						while(true){
							Integer m = money.getReference();
							if(m<20){
								if(money.compareAndSet(m, m+20, stamp, stamp+1)){
									System.out.println("余额小于20元，充值成功，余额：" + money.getReference() + "元");
									break;
								}
							}else{
								break;
							}
						}
					}
				}
			}.start();
		}

		//用户消费线程，模拟消费行为
		new Thread(){
			public void run(){
				for(int i=0;i<100;i++){
					while(true){
						Integer m=money.getReference();
						int stamp = money.getStamp();
						if(m>10){
							System.out.println("大于10元");
							if(money.compareAndSet(m, m-10, stamp, stamp+1)){
								System.out.println("成功消费10元，余额：" + money.getReference());
								break;
							}
						}else{
							System.out.println("没有足够余额");
							break;
						}
					}
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

}
