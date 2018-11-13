package com.cdeledu.thread3.c15监控任务的生命周期;

import java.util.concurrent.TimeUnit;

public class Test {

	public static void main(String[] args) {
		final TaskLifecycle<String> lifecycle = new TaskLifecycle.EmptyLifecycle<String>(){
			@Override
			public void onFinish(Thread thread, String result){
				System.out.println("the result is " + result);
			}
		};
		Observable ot = new ObservableThread<String>(lifecycle, () -> {
			try{
				TimeUnit.SECONDS.sleep(1);
			}catch(Exception e){
				e.printStackTrace();
			}
			System.out.println("finish done.");
			return "hello observer";
		});
		ot.start();
	}

}
