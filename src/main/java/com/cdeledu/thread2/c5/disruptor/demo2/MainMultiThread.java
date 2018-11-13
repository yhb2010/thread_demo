package com.cdeledu.thread2.c5.disruptor.demo2;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MainMultiThread {

	public static void main(String[] args) {

        //only one disruptor in application
        Disruptor<StringEvent> disruptor = new Disruptor<StringEvent>(
                StringEvent::new, 8,
                Executors.newFixedThreadPool(5),
                ProducerType.MULTI,
                new BlockingWaitStrategy());


        disruptor.handleEventsWith(
                ((event, sequence, endOfBatch) -> {
                    System.out.println(Thread.currentThread().getName() + " handler Event:" + event.value);
                    TimeUnit.SECONDS.sleep(2);
                })
        );
        disruptor.start();

        //multiple threads use one disruptor
        for (int i = 0; i < 20; i++) {
            int threadNO = i;
            new Thread("thread-" + threadNO) {
                public void run() {
                    disruptor.getRingBuffer()
                            .publishEvent((event, sequence, arg) -> event.value = arg, "t-" + threadNO);
                    System.out.println("已提交:"+threadNO);
                }
            }.start();
        }

        System.out.println("main end...");

    }

    static class StringEvent {
        public String value;
    }

}
