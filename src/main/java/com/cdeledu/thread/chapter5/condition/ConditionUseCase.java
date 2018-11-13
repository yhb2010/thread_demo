package com.cdeledu.thread.chapter5.condition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * condition定义了等待/通知两种类型的方法，当前线程调用这些方法时，需要提前获取到Condition对象关联的锁。
 * Condition对象是由Lock对象（调用Lock对象的newCondition()方法）创建的，换句话说，Condition是依赖Lock对象的。
 */
public class ConditionUseCase {
    Lock      lock      = new ReentrantLock();
    //一般都会将Condition对象作为成员变量。
    Condition condition = lock.newCondition();

    public void conditionWait() throws InterruptedException {
        lock.lock();
        try {
        	//调用await()方法后，当前线程会释放锁并在此等待，而其他线程调用Condition的signal()方法，通知当前线程后，当前线程才从await()方法返回，并在返回前已经获取了锁。
            condition.await();
        } finally {
            lock.unlock();
        }
    }

    public void conditionSignal() throws InterruptedException {
        lock.lock();
        try {
            condition.signal();
        } finally {
            lock.unlock();
        }
    }
}
