package com.cdeledu.thread2.c5.disruptor.demo2.common;

/**1、定义事件
事件(Event)就是通过 Disruptor 进行交换的数据类型。
 * @author DELL
 *
 */
public class LongEvent {
	public LongEvent() {
        System.out.println("构建longevent...");
    }

    private long value;

    public void set(long value)
    {
        this.value = value;
    }

    public long getValue() {
        return value;
    }
}