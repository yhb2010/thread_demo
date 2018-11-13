package com.cdeledu.thread2.c5.disruptor.demo2.common.generic;

public class GenericEvent<T> {

	private T value;

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }

}
