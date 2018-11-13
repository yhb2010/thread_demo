package com.cdeledu.thread.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**LinkedBlockingQueue由链表结构组成的有界阻塞队列。实现是线程安全的，实现了FIFO（先进先出）等特性. 是作为生产者消费者的首选，LinkedBlockingQueue 可以指定容量，
 * 也可以不指定，不指定的话，默认最大是Integer.MAX_VALUE，其中主要用到put和take方法，put方法在队列满的时候会阻塞直到有队列成员被消费，
 * take方法在队列空的时候会阻塞，直到有队列成员被放进来。
 * @author DELL
 *
 */
public class LinkedBlockingQueueDemo {

	public static void main(String[] args) {
		// 队列
		LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>(10);

		ExecutorService service = Executors.newCachedThreadPool();
		for (int i = 0; i < 6; i++) {
			service.submit(new Consumer2(queue, "X二代" + i));
			service.submit(new Consumer2(queue, "导演" + i));
		}
		for (int i = 0; i < 6; i++) {
			service.submit(new Producer2(queue, "黄金酒," + i));
			service.submit(new Producer2(queue, "美女演员" + i));
		}
		service.shutdown();
	}

}
