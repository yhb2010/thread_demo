package com.cdeledu.thread2.c5.disruptor.demo2;

import com.cdeledu.thread2.c5.disruptor.demo2.common.generic.GenericEvent;
import com.cdeledu.thread2.c5.disruptor.demo2.common.generic.GenericEventFactory;
import com.cdeledu.thread2.c5.disruptor.demo2.common.generic.GenericEventProducer;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.Executors;

/**
 * 提供公用的模板，方便测试。
 * 在控制台输入消息内容回车,输入exit退出程序
 */
public abstract class MainTemplate {

	public void run() {
        GenericEventFactory<GenericEvent<String>> eventFactory = new GenericEventFactory<GenericEvent<String>>();
        int bufferSize = 8;
        Disruptor<GenericEvent<String>> disruptor = new Disruptor(eventFactory, bufferSize, Executors.defaultThreadFactory());

        addHandler(disruptor);

        disruptor.start();

        RingBuffer<GenericEvent<String>> ringBuffer = disruptor.getRingBuffer();

        doAfterDisruptorStart(ringBuffer);

        GenericEventProducer<String> producer = new GenericEventProducer(ringBuffer);

        String s = "如果两个不同的并发变量位于同一个缓存行e";
        Arrays.stream(s.split("")).forEach(msg -> producer.onData(msg));
    }

    protected void doAfterDisruptorStart(final RingBuffer<GenericEvent<String>> ringBuffer) {

    }

    public abstract void addHandler(Disruptor<GenericEvent<String>> disruptor);

}
