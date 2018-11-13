package com.cdeledu.thread3.c20GuardSuspersion;

import java.util.LinkedList;

/**GuardedSuspension设计模式就是确保、挂起，意思是，当线程访问某个对象时，如果发现条件不满足，就暂时挂起等待条件满足时再次访问。
 * 常见在生产者消费者模式等
 * @author Administrator
 *
 */
public class GuardedSuspensionQueue {

	private final LinkedList<Integer> queue = new LinkedList<>();
	private final int LIMIT = 100;
	
	/**往queue插入数据，如果元素超过了最大容量，则会阻塞
	 * @param data
	 * @throws InterruptedException 
	 */
	public void offer(Integer data) throws InterruptedException{
		synchronized (this) {
			while(queue.size() >= LIMIT){
				wait();
			}
			notifyAll();
			queue.addLast(data);
		}
	}
	
	/**从queue里获取元素，如果队列为空，则当前线程阻塞
	 * @return
	 * @throws InterruptedException 
	 */
	public Integer take() throws InterruptedException{
		synchronized (this) {
			while(queue.isEmpty()){
				wait();
			}
			notifyAll();
			return queue.removeFirst();
		}
	}
	
}
