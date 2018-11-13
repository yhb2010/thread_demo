package com.cdeledu.thread3.c26worker_thread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import static java.util.concurrent.ThreadLocalRandom.current;

/**Worker-Thread模式也称为流水线设计模式，类似于工厂流水线，上游工作人员完成某个产品的组装后，将半成品收到流水线传送带上，接下来的加工工作则会交给下游的工人。
 * 线程池在某种意义上也算是Worker-Thread模式的一种实现，线程池初始化时所创建的线程类似于在流水线上等待工作的工人，提交给线程池的Runnable接口类似于需要加工的
 * 产品，而Runnable的run方法则相当于组装该产品的说明书。
 * @author Administrator
 *
 */
public class Test {
	
	public static void main(String[] args) {
		//流水线上有5个工人
		final ProductionChannel channel = new ProductionChannel(5);
		AtomicInteger productionNo = new AtomicInteger(0);
		//流水线上有8个工作人员往传送带上不断的放置等待加工的半成品
		IntStream.range(0, 8).forEach(i -> {
			new Thread(() -> {
				while(true){
					channel.offerProduction(new Production(productionNo.getAndIncrement()));
					try{
						TimeUnit.SECONDS.sleep(current().nextInt(10));
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}).start();
		});
	}

}
