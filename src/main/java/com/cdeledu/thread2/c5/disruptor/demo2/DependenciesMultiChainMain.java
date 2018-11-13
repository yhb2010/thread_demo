package com.cdeledu.thread2.c5.disruptor.demo2;

import com.cdeledu.thread2.c5.disruptor.demo2.common.generic.GenericEvent;
import com.cdeledu.thread2.c5.disruptor.demo2.common.generic.GenericEventHandler;
import com.lmax.disruptor.dsl.Disruptor;

/**
 * 多个chain
 * chain1、chain2之间没有依赖关系，but
 * chain1中的 step1-1、step1-2有先后关系
 * chain12中的 step2-1、step2-2有先后关系
 * <p>
 * Created by yanglikun on 2017/2/16.
 */
public class DependenciesMultiChainMain extends MainTemplate {


    public void addHandler(Disruptor<GenericEvent<String>> disruptor) {
        //chain1
        disruptor.handleEventsWith(new GenericEventHandler<String>("step1-1"))
                .then(new GenericEventHandler<String>("step1-2"));
        //chain2
        disruptor.handleEventsWith(new GenericEventHandler<String>("step2-1"))
                .then(new GenericEventHandler<String>("step2-2"));
    }

    public static void main(String[] args) {
        new DependenciesMultiChainMain().run();
    }
}