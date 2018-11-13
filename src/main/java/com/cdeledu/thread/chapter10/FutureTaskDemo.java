package com.cdeledu.thread.chapter10;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

//当一个线程需要等待另一个线程把某个任务执行完后它才能继续执行，此时可以使用FutureTask。假设有多个线程执行若干任务，每个任务最多只能被
//执行一次，当多个线程试图同时执行同一个任务时，只允许一个线程执行任务，其它线程需要等待这个任务执行完后才能继续执行。
public class FutureTaskDemo {

    private final ConcurrentMap<Object, Future<String>> taskCache = new ConcurrentHashMap<Object, Future<String>>();

    private String executionTask(final String taskName) throws ExecutionException, InterruptedException {
        while (true) {
        	System.out.println(Thread.currentThread().getName() + "进入");
            Future<String> future = taskCache.get(taskName); //1.1,2.1
            System.out.println(Thread.currentThread().getName() + "获取" + future);
            if (future == null) {
                Callable<String> task = new Callable<String>() {
                    public String call() throws InterruptedException {
                    	System.out.println(Thread.currentThread().getName() + "正在执行");
                        TimeUnit.SECONDS.sleep(2);
                        System.out.println(Thread.currentThread().getName() + "执行完");
                        return taskName;
                    }
                };
                //1.2创建任务
                FutureTask<String> futureTask = new FutureTask<String>(task);
                System.out.println(Thread.currentThread().getName() + "创建" + futureTask);
                future = taskCache.putIfAbsent(taskName, futureTask); //1.3
                if (future == null) {
                    future = futureTask;
                    System.out.println(Thread.currentThread().getName() + "执行" + future);
                    futureTask.run(); //1.4执行任务
                }
            }

            try {
                return future.get(); //1.5,2.2线程在此等待任务执行完成
            } catch (CancellationException e) {
                taskCache.remove(taskName, future);
            }
        }
    }

    public static void main(String[] args) {
    	FutureTaskDemo d = new FutureTaskDemo();
		Thread t1 = new Thread(() -> {
			try {
				d.executionTask("aaa");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}, "t1");
		Thread t2 = new Thread(() -> {
			try {
				d.executionTask("aaa");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}, "t2");
		Thread t3 = new Thread(() -> {
			try {
				d.executionTask("bbb");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}, "t3");
		t2.start();
		t3.start();
		t1.start();
	}

}
