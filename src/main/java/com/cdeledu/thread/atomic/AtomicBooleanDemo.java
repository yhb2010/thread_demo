package com.cdeledu.thread.atomic;

import java.util.concurrent.atomic.AtomicBoolean;

//AtomicBoolean 类为我们提供了一个可以用原子方式进行读和写的布尔值，它还拥有一些先进的原子性操作，比如 compareAndSet()。
public class AtomicBooleanDemo {

	public static void main(String[] args) {
		//创建一个 AtomicBoolean
		AtomicBoolean atomicBoolean = new AtomicBoolean();
		AtomicBoolean atomicBoolean2 = new AtomicBoolean(true);
		//获取 AtomicBoolean 的值
		boolean value = atomicBoolean2.get();
		//设置 AtomicBoolean 的值
		atomicBoolean2.set(false);
		//交换 AtomicBoolean 的值
		//你可以通过 getAndSet() 方法来交换一个 AtomicBoolean 实例的值。getAndSet() 方法将返回 AtomicBoolean 当前的值，并将为 AtomicBoolean 设置一个新值。示例如下：
		boolean oldValue = atomicBoolean2.getAndSet(false);
		//比较并设置 AtomicBoolean 的值
		//compareAndSet() 方法允许你对 AtomicBoolean 的当前值与一个期望值进行比较，如果当前值等于期望值的话，将会对 AtomicBoolean 设定一个新值。compareAndSet() 方法是原子性的，因此在同一时间之内有单个线程执行它。因此 compareAndSet() 方法可被用于一些类似于锁的同步的简单实现。
		AtomicBoolean atomicBoolean3 = new AtomicBoolean(true);
		boolean expectedValue = true;
		boolean newValue      = false;
		boolean wasNewValueSet = atomicBoolean3.compareAndSet(expectedValue, newValue);
	}

}
