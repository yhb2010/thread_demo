package com.cdeledu.thread2.c5.disruptor.demo2;

import com.cdeledu.thread2.c5.disruptor.demo2.common.generic.GenericEvent;
import com.cdeledu.thread2.c5.disruptor.demo2.common.generic.GenericEventHandler;
import com.lmax.disruptor.dsl.Disruptor;

/**
 * 链式
 * step1、step2、step3会按照顺序处理
 */
public class DependenciesChainMain extends MainTemplate {

    public void addHandler(Disruptor<GenericEvent<String>> disruptor) {
        disruptor.handleEventsWith(new GenericEventHandler<String>("step1"))
                .then(new GenericEventHandler<String>("step2"))
                .then(new GenericEventHandler<String>("step3"));
    }

    public static void main(String[] args) {
        new DependenciesChainMain().run();
    }
}