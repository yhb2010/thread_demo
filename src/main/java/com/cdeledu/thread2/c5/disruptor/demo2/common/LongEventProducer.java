package com.cdeledu.thread2.c5.disruptor.demo2.common;

import java.nio.ByteBuffer;

import com.lmax.disruptor.RingBuffer;

/**
* 发布事件
* 和LongEventProducerWithTranslator做的是一件事
*/
public class LongEventProducer {
	// 生产者持有RingBuffer实例，可以直接向RingBuffer实例中的entry写入数据
	private final RingBuffer<LongEvent> ringBuffer;

    public LongEventProducer(RingBuffer<LongEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void onData(ByteBuffer bb) {
        long sequence = ringBuffer.next();  // Grab the next sequence 获取下一个sequence
        try {
            LongEvent event = ringBuffer.get(sequence); // Get the entry in the Disruptor 获取entry对象
            // for the sequence 对应sequence位置上的event
            event.set(bb.getLong(0));  // Fill with data 填充业务数据
        } finally {
        	//最后的 ringBuffer.publish 方法必须包含在 finally 中以确保必须得到调用；如果某个请求的 sequence 未被提交，将会堵塞后续的发布操作或者其它的 producer。
            ringBuffer.publish(sequence);
        }
    }
}