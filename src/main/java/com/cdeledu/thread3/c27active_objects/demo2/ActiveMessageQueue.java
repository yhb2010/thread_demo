package com.cdeledu.thread3.c27active_objects.demo2;

import java.util.LinkedList;

/**对应于Worker Thread模式中的传送带，主要用于传送调用线程通过Proxy提交过来的MethodMessage，但是这个传送带允许存放无限的MethodMessage(没有limit的约束，
 * 理论上可以放无限多个MethodMessage直到发生堆内存溢出)
 * @author Administrator
 *
 */
public class ActiveMessageQueue {
	
	//用于存放提交的MethodMessage消息
	private final LinkedList<ActiveMessage> messages = new LinkedList<>();
	
	/**
	 * 创建ActiveMessageQueue时同时启动ActiveDaemonThread线程，ActiveDaemonThread主要用于进行异步的方法执行
	 */
	public ActiveMessageQueue(){
		//启动worker线程
		new ActiveDaemonThread(this).start();
	}
	
	/**没有进行limit限制，允许提交无限个MethodMessage，并且当有新的Message加入时会通知ActiveDaemonThread线程
	 * @param activeMessage
	 */
	public void offer(ActiveMessage activeMessage){
		synchronized (this) {
			messages.addFirst(activeMessage);
			//因为只有一个线程负责take数据，因此没有必要使用notifyAll
			notify();
		}
	}
	
	/**主要是被ActiveDaemonThread线程使用，当messages为空时ActiveDaemonThread线程将被挂起
	 * @return
	 */
	protected ActiveMessage take() {
		synchronized (this) {
			//当messages中没有MethodMessage对象时，执行线程进入阻塞
			while(messages.isEmpty()){
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			//获取其中一个MethodMessage并且从队列中移除
			return messages.removeFirst();
		}
	}

}
