package com.cdeledu.thread2.c5.disruptor.demo2;

import com.cdeledu.thread2.c5.disruptor.demo2.common.generic.GenericEvent;
import com.cdeledu.thread2.c5.disruptor.demo2.common.generic.GenericEventHandler;
import com.lmax.disruptor.dsl.Disruptor;

/**
 * 菱形
 * step1-1、step1-2是并行执行的<br/>
 * step1-1和step1-2都执行完之后step2才会执行
 */
public class DependenciesDiamondMain extends MainTemplate {

    public void addHandler(Disruptor<GenericEvent<String>> disruptor) {
        disruptor.handleEventsWith(new GenericEventHandler<String>("step1-1"),
                new GenericEventHandler<String>("step1-2"))
                .then(new GenericEventHandler<String>("step2"));//这里是用了then
    }

    public static void main(String[] args) {
        new DependenciesDiamondMain().run();
    }
}