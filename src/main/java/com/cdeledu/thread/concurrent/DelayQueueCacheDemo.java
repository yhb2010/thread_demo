package com.cdeledu.thread.concurrent;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

//首先，这种队列中只能存放实现Delayed接口的对象，而此接口有两个需要实现的方法。最重要的就是getDelay，这个方法需要返回对象过期前的时间。简单说，队列在某些方法处理前，会调用此方法来判断对象有没有超时。
//Delayed扩展了Comparable接口，比较的基准为延时的时间值。
/**业务场景二：具有过期时间的缓存
向缓存添加内容时，给每一个key设定过期时间，系统自动将超过过期时间的key清除。
这个场景中几个点需要注意：
当向缓存中添加key-value对时，如果这个key在缓存中存在并且还没有过期，需要用这个key对应的新过期时间。
为了能够让DelayQueue将其已保存的key删除，需要重写实现Delayed接口添加到DelayQueue的DelayedItem的hashCode函数和equals函数。
当缓存关闭，监控程序也应关闭，因而监控线程应当用守护线程。
具体实现如下：
 * @author DELL
 *
 * @param <K>
 * @param <V>
 */
public class DelayQueueCacheDemo<K, V> {

	public ConcurrentHashMap<K, V> map = new ConcurrentHashMap<K, V>();
    public DelayQueue<DelayedItem<K>> queue = new DelayQueue<DelayedItem<K>>();

    public void put(K k,V v,long liveTime){
        V v2 = map.put(k, v);
        DelayedItem<K> tmpItem = new DelayedItem<K>(k, liveTime * 1000);
        if (v2 != null) {
            queue.remove(tmpItem);
        }
        queue.put(tmpItem);
    }

    public DelayQueueCacheDemo(){
        Thread t = new Thread(){
            @Override
            public void run(){
                dameonCheckOverdueKey();
            }
        };
        t.setDaemon(true);
        t.start();
    }

    public void dameonCheckOverdueKey(){
        while (true) {
            DelayedItem<K> delayedItem = queue.poll();
            if (delayedItem != null) {
                map.remove(delayedItem.getT());
                System.out.println(System.nanoTime()+" remove "+delayedItem.getT() +" from cache");
            }
            try {
                Thread.sleep(300);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }

    /**
     * TODO
     * @param args
     * 2014-1-11 上午11:30:36
     * @author:孙振超
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        int cacheNumber = 10;
        int liveTime = 0;
        DelayQueueCacheDemo<String, Integer> cache = new DelayQueueCacheDemo<String, Integer>();

        for (int i = 0; i < cacheNumber; i++) {
            liveTime = random.nextInt(10);
            System.out.println(i+"  "+liveTime);
            cache.put(i+"", i, liveTime);
        }

        Thread.sleep(15000);
        System.out.println();
    }

}

class DelayedItem<T> implements Delayed{

    private T t;
    private long expTime; // 过期时间

    public DelayedItem(T t,long liveTime){
        this.setT(t);
        this.expTime = System.currentTimeMillis() + liveTime;
    }

    @Override
    //指定元素的顺序，例如，让延时时间最长的放在队列的末尾。
    public int compareTo(Delayed o) {
    	if (o == null) return 1;
        if (o == this) return 0;
        if (o instanceof DelayedItem){
            DelayedItem<T> tmpDelayedItem = (DelayedItem<T>)o;
            if (expTime > tmpDelayedItem.expTime ) {
                return 1;
            }else if (expTime == tmpDelayedItem.expTime) {
                return 0;
            }else {
                return -1;
            }
        }
        long diff = getDelay(TimeUnit.MICROSECONDS) - o.getDelay(TimeUnit.MICROSECONDS);
        return diff > 0 ? 1:diff == 0? 0:-1;
    }

    @Override
    public long getDelay(TimeUnit unit) {
    	return expTime - System.currentTimeMillis();
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }
    @Override
    public int hashCode(){
        return t.hashCode();
    }

    @Override
    public boolean equals(Object object){
        if (object instanceof DelayedItem) {
            return object.hashCode() == hashCode() ?true:false;
        }
        return false;
    }

}