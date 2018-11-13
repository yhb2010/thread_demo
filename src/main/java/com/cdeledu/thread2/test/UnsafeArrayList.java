package com.cdeledu.thread2.test;

import java.util.ArrayList;

/**调试多线程：
 * 加断点条件：((!Thread.currentThread().getName().equals("main")) && size == 9)：除了主线程并且当size==9时中断。
 * （list初始化10个元素空间，每次检查是否需要扩容，在多线程下，如果两个线程都检查是9，此时两个线程都认为可以继续添加元素，一个线程添加完后，另一个线程再添加就会报错。）
 * 这时断点切到t1线程，让程序执行到elementData[size++]=e上，再切换到t2线程，也让程序执行到elementData[size++]=e上，再换回t1，让t1继续，换回t2，让t2继续，此时t2会报异常：
 * java.lang.ArrayIndexOutOfBoundsException: 10
 * @author DELL
 *
 */
public class UnsafeArrayList {

	static ArrayList<Integer> al = new ArrayList<>();

	static class AddTask implements Runnable{
		@Override
		public void run() {
			try{
				Thread.sleep(100);
			}catch(Exception e){
				e.printStackTrace();
			}
			for(int i=0; i<1000000; i++){
				al.add(i);
			}
		}
	}

	public static void main(String[] args) {
		Thread t1 = new Thread(new AddTask(), "t1");
		Thread t2 = new Thread(new AddTask(), "t2");
		t1.start();
		t2.start();

		Thread t3 = new Thread(new Runnable() {
			@Override
			public void run() {
				while(true){
					try{
						Thread.sleep(1000);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		});
		t3.start();
	}

}
