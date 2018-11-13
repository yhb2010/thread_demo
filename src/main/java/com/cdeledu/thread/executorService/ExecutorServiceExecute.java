package com.cdeledu.thread.executorService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//java.util.concurrent.ExecutorService 接口表示一个异步执行机制，使我们能够在后台执行任务。因此一个 ExecutorService 很类似于一个线程池。实际上，存在于 java.util.concurrent 包里的 ExecutorService 实现就是一个线程池实现。
public class ExecutorServiceExecute {

	public static void main(String[] args) {
		ExecutorService executorService = Executors.newFixedThreadPool(5);

		for (int i = 0; i < 5; i++) {
			//execute(Runnable) 方法要求一个 java.lang.Runnable 对象，然后对它进行异步执行。
			//将任务添加到线程去执行：
			//当将一个任务添加到线程池中的时候，线程池会为每个任务创建一个线程，该线程会在之后的某个时刻自动执行。
			executorService.execute(new TestRunnable());
			System.out.println("************* a" + i + " *************");
		}
		//ExecutorService 关闭
		//使用完 ExecutorService 之后你应该将其关闭，以使其中的线程不再运行。
		//比如，如果你的应用是通过一个 main() 方法启动的，之后 main 方法退出了你的应用，如果你的应用有一个活动的 ExexutorService 它将还会保持运行。ExecutorService 里的活动线程阻止了 JVM 的关闭。
		//要终止 ExecutorService 里的线程你需要调用 ExecutorService 的 shutdown() 方法。ExecutorService 并不会立即关闭，但它将不再接受新的任务，而且一旦所有线程都完成了当前任务的时候，ExecutorService 将会关闭。在 shutdown() 被调用之前所有提交给 ExecutorService 的任务都被执行。
		//如果你想要立即关闭 ExecutorService，你可以调用 shutdownNow() 方法。这样会立即尝试停止所有执行中的任务，并忽略掉那些已提交但尚未开始处理的任务。无法担保执行任务的正确执行。可能它们被停止了，也可能已经执行结束。
		executorService.shutdown();
	}
}

class TestRunnable implements Runnable {
	public void run() {
		System.out.println(Thread.currentThread().getName() + "线程被调用了。");
		while (true) {
			try {
				Thread.sleep(5000);
				System.out.println(Thread.currentThread().getName());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}