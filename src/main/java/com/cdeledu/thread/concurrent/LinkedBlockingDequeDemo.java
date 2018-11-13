package com.cdeledu.thread.concurrent;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * deque(双端队列) 是 "Double Ended Queue" 的缩写。因此，双端队列是一个你可以从任意一端插入或者抽取元素的队列。
 * LinkedBlockingDeque 是一个双端队列，在它为空的时候，一个试图从中抽取数据的线程将会阻塞，无论该线程是试图从哪一端抽取数据。
 *
 *  BlockingQueue	BlockingDeque
	add()			addLast()
	offer() x 2		offerLast() x 2
	put()			putLast()

	remove()		removeFirst()
	poll() x 2		pollFirst()
	take()			takeFirst()

	element()		getFirst()
	peek()			peekFirst()
 */
public class LinkedBlockingDequeDemo {

    public static void main(String[] args) {
    	BlockingDeque<String> deque = new LinkedBlockingDeque<String>();

    	deque.addFirst("1");
    	deque.addLast("2");

    	try {
			String two = deque.takeLast();
			String one = deque.takeFirst();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}