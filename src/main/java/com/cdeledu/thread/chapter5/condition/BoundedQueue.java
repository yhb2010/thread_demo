package com.cdeledu.thread.chapter5.condition;

import java.util.Arrays;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * 10-21
 */
public class BoundedQueue<T> {
    private Object[]  items;
    // 添加的下标，删除的下标和数组当前数量
    private int       addIndex, removeIndex, count;
    private Lock      lock     = new ReentrantLock();
    private Condition notEmpty = lock.newCondition();
    private Condition notFull  = lock.newCondition();

    public BoundedQueue(int size) {
        items = new Object[size];
    }

    // 添加一个元素，如果数组满，则添加线程进入等待状态，直到有“空位”
    public void add(T t) throws InterruptedException {
    	//首先要获取锁，目的是确保数组修改的排他性和可见性。当数组元素数量等于数组长度时，表示数组已满，则调用notFull.await()，当前线程随之释放锁并进入等待
    	//状态。如果数组元素数量不等于数组长度，表示数组未满，则添加元素到数组中，同时通知等待在notEmpty上的线程，数组中已经有新元素可以获取。
        lock.lock();
        try {
            while (count == items.length){
                notFull.await();
            }
            System.out.println(Thread.currentThread().getName() + "插入元素:" + t.toString());
            items[addIndex] = t;
            System.out.println(Arrays.stream(items).map(o -> "" + o).collect(Collectors.joining(",")));
            if (++addIndex == items.length){
                addIndex = 0;
            }
            ++count;
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    // 由头部删除一个元素，如果数组空，则删除线程进入等待状态，直到有新添加元素
    @SuppressWarnings("unchecked")
    public T remove() throws InterruptedException {
        lock.lock();
        try {
            while (count == 0)
                notEmpty.await();
            Object x = items[removeIndex];
            items[removeIndex] = null;
            System.out.println(Thread.currentThread().getName() + "消费了元素:" + x.toString());
            if (++removeIndex == items.length)
                removeIndex = 0;
            --count;
            notFull.signal();
            return (T) x;
        } finally {
            lock.unlock();
        }
    }
}
