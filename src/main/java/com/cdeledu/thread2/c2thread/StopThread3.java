package com.cdeledu.thread2.c2thread;

/**通过线程中断方式停止：
 * 1、Thread.interrupt()//中断线程
 * 2、Thread.isInterrupted()//判断是否被中断
 * 3、Thread.interrupted()//判断是否被中断，并清除当前中断状态
 * 严格来说，中断并不会立刻退出程序，而是给线程发送一个通知，告知目标线程，有人希望你退出了。至于目标线程接到通知后如何处理，则完全由目标现成自行决定。
 * @author DELL
 *
 */
public class StopThread3 {

	public static void main(String[] args) throws InterruptedException {
		Thread t1 = new Thread(){
			@Override
			public void run(){
				while(true){
					if(Thread.currentThread().isInterrupted()){
						//这里还可以做一些后续的处理，保证数据的一致性和完整性
						System.out.println("Interrupted!");
						break;
					}
					try {
						//sleep方法由于中断而抛出异常，此时，它会清除中断标记，如果不加处理，那么在下一次循环开始时，就无法捕获这个中断，故在异常处理中，再次设置这个中断标记位
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						System.out.println("Interrupted when Sleep");
						//设置中断状态
						Thread.currentThread().interrupt();
					}
					Thread.yield();
				}
			}
		};
		t1.start();
		Thread.sleep(2000);
		t1.interrupt();
	}

}
