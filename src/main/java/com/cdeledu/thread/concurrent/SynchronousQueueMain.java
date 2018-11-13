package com.cdeledu.thread.concurrent;

import java.util.Random;
import java.util.concurrent.SynchronousQueue;

/**不像ArrayBlockingQueue或LinkedListBlockingQueue，SynchronousQueue内部并没有数据缓存空间，你不能调用peek()方法来看队列中是否有数据元素，因为数据元素只有当你试着取走的时候才可能存在，不取走而只想偷窥一下是不行的，当然遍历这个队列的操作也是不允许的。队列头元素是第一个排队要插入数据的线程，而不是要交换的数据。数据是在配对的生产者和消费者线程之间直接传递的，并不会将数据缓冲数据到队列中。可以这样来理解：生产者和消费者互相等待对方，握手，然后一起离开。

SynchronousQueue的一个使用场景是在线程池里。Executors.newCachedThreadPool()就使用了SynchronousQueue，这个线程池根据需要（新任务到来时）创建新的线程，如果有空闲线程则会重复使用，线程空闲了60秒后会被回收。

SynchronousQueue 类实现了 BlockingQueue 接口。
SynchronousQueue 是一个特殊的队列，它的内部同时只能够容纳单个元素。如果该队列已有一元素的话，试图向队列中插入一个新元素的线程将会阻塞，直到另一个线程将该元素从队列中抽走。同样，如果该队列为空，试图向队列中抽取一个元素的线程将会阻塞，直到另一个线程向队列中插入了一条新的元素。
据此，把这个类称作一个队列显然是夸大其词了。它更多像是一个汇合点。
 * @author DELL
 *
 */
public class SynchronousQueueMain {

	public static void main(String[] args) throws Exception {
        // 如果为 true，则等待线程以 FIFO 的顺序竞争访问；否则顺序是未指定的。
        // SynchronousQueue<Integer> sc =new SynchronousQueue<>(true);//fair
        SynchronousQueue<Integer> sc = new SynchronousQueue<>(); // 默认不指定的话是false，不公平的
        new Thread(() -> { //生产者线程，使用的是lambda写法，需要使用JDK1.8
            while (true) {
                try {
                    sc.put(new Random().nextInt(50));
                    //将指定元素添加到此队列，如有必要则等待另一个线程接收它。
                    // System.out.println("sc.offer(new Random().nextInt(50)): "+sc.offer(new Random().nextInt(50)));
                    // 如果另一个线程正在等待以便接收指定元素，则将指定元素插入到此队列。如果没有等待接受数据的线程则直接返回false
                    // System.out.println("sc.offer(2,5,TimeUnit.SECONDS):
                    // "+sc.offer(2,5,TimeUnit.SECONDS));//如果没有等待的线程，则等待指定的时间。在等待时间还没有接受数据的线程的话，直接返回false
                    System.out.println("添加操作运行完毕...");//是操作完毕，并不是添加或获取元素成功!
                    Thread.sleep(1000);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(() -> {//消费者线程。使用的是lambda创建的线程写法需要使用jdk1.8
            while (true) {
                try {
                    System.out.println("-----------------> sc.take: " + sc.take());
                    System.out.println("-----------------> 获取操作运行完毕...");//是操作完毕，并不是添加或获取元素成功!
                    Thread.sleep(1000);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
