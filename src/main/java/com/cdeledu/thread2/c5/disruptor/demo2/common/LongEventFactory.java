package com.cdeledu.thread2.c5.disruptor.demo2.common;

import com.lmax.disruptor.EventFactory;

/**2、定义事件工厂
事件工厂(Event Factory)定义了如何实例化前面第1步中定义的事件(Event)，需要实现接口 com.lmax.disruptor.EventFactory<T>。
Disruptor 通过 EventFactory 在 RingBuffer 中预创建 Event 的实例。
一个 Event 实例实际上被用作一个“数据槽”，发布者发布前，先从 RingBuffer 获得一个 Event 的实例，然后往 Event 实例中填充数据，之后再发布到 RingBuffer 中，之后由 Consumer 获得该 Event 实例并从中读取数据。


RingBuffer使用数组Object[] entries作为存储元素，如下图所示，初始化RingBuffer时，会将所有的entries的每个元素指定为特定的Event，这时候event中的detail属性是null；后面生产者向RingBuffer中写入消息时，
RingBuffer不是直接将enties[7]指向其他的event对象，而是先获取event对象，然后更改event对象的detail属性；消费者在消费时，也是从RingBuffer中读取出event，然后取出其detail属性。可以看出，生产/消费过程中，
RingBuffer的entities[7]元素并未发生任何变化，未产生临时对象，entities及其元素对象一直存活，直到RingBuffer消亡。故而可以最小化GC的频率，提升性能。
 * @author DELL
 *
 */
public class LongEventFactory implements EventFactory<LongEvent>
{
    public LongEvent newInstance()
    {
        return new LongEvent();
    }
}