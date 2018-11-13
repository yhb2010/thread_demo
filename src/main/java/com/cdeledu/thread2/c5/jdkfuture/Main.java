package com.cdeledu.thread2.c5.jdkfuture;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class Main {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		//构造FutureTask
		FutureTask<String> future = new FutureTask<>(new RealData("a"));
		ExecutorService exe = Executors.newFixedThreadPool(1);
		//执行FutureTask，相当于上例中的client.request("a")发送请求，在这里开启线程进行RealData的call执行
		exe.submit(future);

		System.out.println("请求完毕");
		try{
			Thread.sleep(2000);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		//取得call方法的返回值，如果call方法没有执行完，会等待
		System.out.println("数据=" + future.get());
	}

}
