package com.cdeledu.thread.concurrent;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

/**PriorityBlockingQueue一个支持优先级排序的无界阻塞队列。默认情况下元素采用自然顺序升序排列。
 * 里面存储的对象必须是实现Comparable接口。队列通过这个接口的compare方法确定对象的priority。
规则是：当前和其他对象比较，如果compare方法返回负数，那么在队列里面的优先级就比较高。
PriorityBlockingQueue队列添加新元素时候不是将全部元素进行顺序排列，而是从某个指定位置开始将新元素与之比较，一直比到队列头，这样既能保证队列头一定是优先级最高的元素，又能减少排序带来的性能消耗。
但是这样会出现一个情况，取完队列头时候，后面的剩余的元素不是排序的，岂不是不符合要求了，继续查看源码，发现每取一个头元素时候，都会对剩余的元素做一次调整，这样就能保证每次队列头的元素都是优先级最高的元素。
 * @author DELL
 *
 */
public class PriorityQueueDemo {

	static Random r=new Random(47);

    public static void main(String args[]) throws InterruptedException{
        final PriorityBlockingQueue q=new PriorityBlockingQueue();
        ExecutorService se=Executors.newCachedThreadPool();
        //execute producer
        se.execute(new Runnable(){
            public void run() {
                int i=0;
                while(true){
                    q.put(new PriorityEntity(r.nextInt(10),i++));
                    try {
                        TimeUnit.MILLISECONDS.sleep(r.nextInt(1000));
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });

        //execute consumer
        se.execute(new Runnable(){
            public void run() {
                while(true){
                    try {
                        System.out.println("take-- "+q.take()+" left:-- ["+q.toString()+"]");
                        try {
                            TimeUnit.MILLISECONDS.sleep(r.nextInt(1000));
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("shutdown");
    }

}

class PriorityEntity implements Comparable<PriorityEntity> {
    private static int count=0;
    private int id=count++;
    private int priority;
    private int index=0;

    public PriorityEntity(int _priority,int _index) {
        this.priority = _priority;
        this.index=_index;
    }

    public String toString(){
        return id+"# [index="+index+" priority="+priority+"]";
    }

    //数字小，优先级高
    public int compareTo(PriorityEntity o) {
        return this.priority > o.priority ? 1
                : this.priority < o.priority ? -1 : 0;
    }

    //数字大，优先级高
//  public int compareTo(PriorityTask o) {
//      return this.priority < o.priority ? 1
//              : this.priority > o.priority ? -1 : 0;
//  }
}