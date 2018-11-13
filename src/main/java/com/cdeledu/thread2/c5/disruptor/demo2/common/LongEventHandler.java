package com.cdeledu.thread2.c5.disruptor.demo2.common;

import com.lmax.disruptor.EventHandler;

/**3、定义事件处理的具体实现
通过实现接口 com.lmax.disruptor.EventHandler<T> 定义事件处理的具体实现。
 * @author DELL
 *
 */
public class LongEventHandler implements EventHandler<LongEvent>
{
	private String handlerName;

    public LongEventHandler(String handlerName) {
        this.handlerName = handlerName;
    }

	//event为从RingBuffer entry中读取的事件内容，消费者从event中读取数据，并完成业务逻辑处理
    public void onEvent(LongEvent event, long sequence, boolean endOfBatch) {
        System.out.println("消费者Event(" + handlerName + "):" + Thread.currentThread().getName() + " " + event.hashCode
                () + ":" + event
                .getValue());
    }
}