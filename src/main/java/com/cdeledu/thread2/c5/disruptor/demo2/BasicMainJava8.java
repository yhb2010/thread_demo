package com.cdeledu.thread2.c5.disruptor.demo2;

import com.cdeledu.thread2.c5.disruptor.demo2.common.LongEvent;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by yanglikun on 2017/8/10.
 */
public class BasicMainJava8 {
    public static void main(String[] args) {

        //only one disruptor in application
        Disruptor<LongEvent> disruptor = new Disruptor<LongEvent>(
        		LongEvent::new, 8,
                Executors.newFixedThreadPool(5),
                ProducerType.MULTI,
                new BlockingWaitStrategy());

        disruptor.handleEventsWith(
                ((event, sequence, endOfBatch) -> {
                    System.out.println("handler Event:" + event.getValue());
                })
        );
        disruptor.start();

        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();

        ExecutorService es = Executors.newFixedThreadPool(100);
        for (int i = 0; i < 10; i++) {
            es.submit(new Runnable() {
                public void run() {
                    ByteBuffer bb = ByteBuffer.allocate(8);
                    for (long l = 0; l < 20; l++) {
                        bb.putLong(0, l);
                        ringBuffer.publishEvent((event, sequence, arg) -> event.set(bb.getLong(0)), "消息");
                    }
                }
            });
        }
        System.out.println("main end...");

    }

}