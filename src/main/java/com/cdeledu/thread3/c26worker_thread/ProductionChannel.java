package com.cdeledu.thread3.c26worker_thread;

/**流水线的传送带主要用于传送待加工的产品，上游的工作人员将完成的半成品放到传送带上，工作人员从传送带上取下产品进行再次加工
 * @author Administrator
 *
 */
public class ProductionChannel {

	//传送带上最多可以有多少个待加工的产品
	private final static int MAX_PROD = 20;
	//主要用来存放待加工产品，也就是传送带
	private final Production[] productionQueue;
	//队列尾
	private int tail;
	//队列头
	private int head;
	//当前在流水线上有多少个待加工的产品
	private int total;
	//在流水线上工作的工人
	private final Worker[] workers;

	//创建ProductionChannel时应指定需要多少个流水线工人
	public ProductionChannel(int workerSize){
		workers = new Worker[workerSize];
		productionQueue = new Production[MAX_PROD];
		for (int i = 0; i < workerSize; i++) {
			workers[i] = new Worker("worker-" + i, this);
			workers[i].start();
		}
	}

	//接受来自上游的半成品(待加工的产品)
	public void offerProduction(Production production){
		synchronized (this) {
			//当传送带上待加工的产品超过了最大值时需要阻塞上游再次传送产品
			while(total > productionQueue.length){
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			//将产品放到传送带，并且通知工人线程工作
			productionQueue[tail] = production;
			tail = (tail + 1) % productionQueue.length;
			total++;
			notifyAll();
		}
	}

	//工人线程(Worker)从传送带上获取产品，进行加工
	public Production takeProduction() {
		synchronized (this) {
			//当传送带上没有产品时，工人等待产品从上游输送到传送带上
			while(total <= 0){
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			//获取产品
			Production prod = productionQueue[head];
			head = (head + 1) % productionQueue.length;
			total--;
			notifyAll();
			return prod;
		}
	}

}
