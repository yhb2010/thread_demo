package com.cdeledu.thread.chapter8;

import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 处理银行流水（CyclicBarrier）
 * @author DELL
 *
 */
public class BankWaterService implements Runnable {

	//创建4个屏障，处理完之后执行当前类的run方法
	private CyclicBarrier c = new CyclicBarrier(4, this);
	//假设只有4个sheet页，所以只启动4个线程，每个sheet页分别计算，最后汇总
	private Executor executor = Executors.newFixedThreadPool(4);
	//保存每个sheet计算出的银流结果
	private ConcurrentHashMap<String, Integer> sheetBankWaterCount = new  ConcurrentHashMap<>();

	private void count(){
		for(int i=0; i<4; i++){
			executor.execute(new Runnable() {
				@Override
				public void run() {
					//计算当前sheet的银流数据，计算代码省略
					sheetBankWaterCount.put(Thread.currentThread().getName(), 1);
					try {
						c.await();
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (BrokenBarrierException e) {
						e.printStackTrace();
					}
				}
			});
		}
	}

	@Override
	public void run() {
		int result = 0;
		//汇总每个sheet页计算出的结果
		for(Entry<String, Integer> sheet : sheetBankWaterCount.entrySet()){
			result += sheet.getValue();
		}
		//将结果输出
		sheetBankWaterCount.put("result", result);
		System.out.println(result);
	}

	public static void main(String[] args) {
		BankWaterService service = new BankWaterService();
		service.count();
	}

}
