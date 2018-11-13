package com.cdeledu.thread2.c5.disruptor.demo2;

import com.cdeledu.thread2.c5.disruptor.demo2.common.generic.GenericEvent;
import com.cdeledu.thread2.c5.disruptor.demo2.common.generic.GenericWorkHandler;
import com.lmax.disruptor.dsl.Disruptor;

/**
 * 一个消息只被一个handler处理
 * Created by yanglikun on 2017/2/16.
 */
public class WorkerPoolMain extends MainTemplate {

    public void addHandler(Disruptor<GenericEvent<String>> disruptor) {


        disruptor.handleEventsWithWorkerPool(new GenericWorkHandler<String>("workHandler-1")
                , new GenericWorkHandler<String>("workHandler-2")
                , new GenericWorkHandler<String>("workHandler-3")
                , new GenericWorkHandler<String>("workHandler-4")
                , new GenericWorkHandler<String>("workHandler-5"));

        //必须像上面一样一次全部添加进去，要不然还是会被处理处理多次(向下面这样)
//        disruptor.handleEventsWithWorkerPool(new GenericWorkHandler<String>("workHandler-1"));
//        disruptor.handleEventsWithWorkerPool(new GenericWorkHandler<String>("workHandler-2"));
//        disruptor.handleEventsWithWorkerPool(new GenericWorkHandler<String>("workHandler-3"));

    }

    public static void main(String[] args) {
        new WorkerPoolMain().run();
    }
}