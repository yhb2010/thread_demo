package com.cdeledu.thread.concurrent;

/**TransferQueue是一个继承了BlockingQueue的接口，并且增加若干新的方法。LinkedTransferQueue是TransferQueue接口的实现类，其定义为一个无界的队列，具有先进先出(FIFO)的特性。
     有人这样评价它："TransferQueue是是ConcurrentLinkedQueue、SynchronousQueue (公平模式下)、无界的LinkedBlockingQueues等的超集。"
     LinkedTransferQueue实现了一个重要的接口TransferQueue，该接口含有下面几个重要方法：
     1. transfer(E e)：若当前存在一个正在等待获取的消费者线程，即立刻移交之；否则，会插入当前元素e到队列尾部，并且等待进入阻塞状态，到有消费者线程取走该元素。
     2. tryTransfer(E e)：若当前存在一个正在等待获取的消费者线程（使用take()或者poll()函数），使用该方法会即刻转移/传输对象元素e；若不存在，则返回false，并且不进入队列。这是一个不阻塞的操作。
     3. tryTransfer(E e, long timeout, TimeUnit unit)：若当前存在一个正在等待获取的消费者线程，会立即传输给它;否则将插入元素e到队列尾部，并且等待被消费者线程获取消费掉；若在指定的时间内元素e无法被消费者线程获取，则返回false，同时该元素被移除。
     4. hasWaitingConsumer()：判断是否存在消费者线程。
     5. getWaitingConsumerCount()：获取所有等待获取元素的消费线程数量。
     6.size()：因为队列的异步特性，检测当前队列的元素个数需要逐一迭代，可能会得到一个不太准确的结果，尤其是在遍历时有可能队列发生更改。
     7.批量操作：类似于addAll，removeAll, retainAll, containsAll, equals, toArray等方法，API不能保证一定会立刻执行。因此，我们在使用过程中，不能有所期待，这是一个具有异步特性的队列。
 * @author DELL
 *
 */

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TransferQueue;
import java.util.concurrent.LinkedTransferQueue;

class TransferProducer implements Runnable {
    private final TransferQueue<String> queue;

    public TransferProducer(TransferQueue<String> queue) {
        this.queue = queue;
    }

    private String produce() {
        return " your lucky number " + (new Random().nextInt(100));
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (queue.hasWaitingConsumer()) {
                    queue.transfer(produce());
                }
                TimeUnit.SECONDS.sleep(1);//生产者睡眠一秒钟,这样可以看出程序的执行过程
            }
        } catch (InterruptedException e) {
        }
    }
}

class TransferConsumer implements Runnable {
    private final TransferQueue<String> queue;

    public TransferConsumer(TransferQueue<String> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            System.out.println(" Consumer " + Thread.currentThread().getName() + queue.take());
        } catch (InterruptedException e) {
        }
    }
}

public class LinkedTransferQueueDemo {

	public static void main(String[] args) {
        TransferQueue<String> queue = new LinkedTransferQueue<String>();
        Thread producer = new Thread(new TransferProducer(queue));
        producer.setDaemon(true); //设置为守护进程使得线程执行结束后程序自动结束运行
        producer.start();
        for (int i = 0; i < 10; i++) {
            Thread consumer = new Thread(new TransferConsumer(queue));
            consumer.setDaemon(true);
            consumer.start();
            try {
                // 消费者进程休眠一秒钟，以便生产者获得CPU，从而生产产品
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
