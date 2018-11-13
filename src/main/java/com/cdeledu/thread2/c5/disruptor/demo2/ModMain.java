package com.cdeledu.thread2.c5.disruptor.demo2;

import com.cdeledu.thread2.c5.disruptor.demo2.common.generic.GenericEvent;
import com.cdeledu.thread2.c5.disruptor.demo2.common.generic.GenericEventModHandler;
import com.lmax.disruptor.dsl.Disruptor;

public class ModMain extends MainTemplate {

    public void addHandler(Disruptor<GenericEvent<String>> disruptor) {
        //批量构建一批handler
        int handlerCounts = 10;
        GenericEventModHandler[] handlers = new GenericEventModHandler[handlerCounts];
        for (int i = 0; i < handlerCounts; i++) {
            handlers[i] = new GenericEventModHandler("[handler-" + i + "]", i, handlerCounts);
        }
        disruptor.handleEventsWith(handlers);
    }

    public static void main(String[] args) {
        new ModMain().run();
    }

}