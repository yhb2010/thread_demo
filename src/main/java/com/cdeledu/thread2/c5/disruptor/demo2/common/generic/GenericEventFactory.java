package com.cdeledu.thread2.c5.disruptor.demo2.common.generic;

import com.lmax.disruptor.EventFactory;

/**
 */
public class GenericEventFactory<T> implements EventFactory<GenericEvent<T>> {

    public GenericEvent<T> newInstance() {
        return new GenericEvent();
    }
}