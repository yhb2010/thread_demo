package com.cdeledu.thread.atomic;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerDemo {

	public static void main(String[] args) {
		//增加 AtomicInteger 值
		//AtomicInteger 类包含有一些方法，通过它们你可以增加 AtomicInteger 的值，并获取其值（以原子方式）。这些方法如下：
		//addAndGet()
		//getAndAdd()
		//getAndIncrement()
		//incrementAndGet()
		//第一个 addAndGet() 方法给 AtomicInteger 增加了一个值，然后返回增加后的值。getAndAdd() 方法为 AtomicInteger 增加了一个值，但返回的是增加以前的 AtomicInteger 的值。具体使用哪一个取决于你的应用场景。以下是这两种方法的示例：
		AtomicInteger atomicInteger = new AtomicInteger();
		System.out.println(atomicInteger.getAndAdd(10));
		System.out.println(atomicInteger.addAndGet(10));
		//getAndIncrement() 和 incrementAndGet() 方法类似于 getAndAdd() 和 addAndGet()，但每次只将 AtomicInteger 的值加 1。
	}

}
