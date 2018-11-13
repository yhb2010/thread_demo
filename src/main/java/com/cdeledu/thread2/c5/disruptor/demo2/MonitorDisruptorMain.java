package com.cdeledu.thread2.c5.disruptor.demo2;

import com.cdeledu.thread2.c5.disruptor.demo2.common.generic.GenericEvent;
import com.cdeledu.thread2.c5.disruptor.demo2.common.generic.GenericSleepEventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 使用ringBuffer.remainingCapacity()监控负载(环上还有多少个slot可用)
 */
public class MonitorDisruptorMain extends MainTemplate {

    public void addHandler(Disruptor<GenericEvent<String>> disruptor) {
        disruptor.handleEventsWith(new GenericSleepEventHandler<String>("handler-1"));
    }

    @Override
    protected void doAfterDisruptorStart(final RingBuffer<GenericEvent<String>> ringBuffer) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("剩余坑位:" + ringBuffer.remainingCapacity());
            }
        }, 1000, 2000);
    }

    public static void main(String[] args) {
        new MonitorDisruptorMain().run();
    }
}