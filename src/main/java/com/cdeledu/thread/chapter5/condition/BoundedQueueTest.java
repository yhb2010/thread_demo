package com.cdeledu.thread.chapter5.condition;

/**
 * 10-21
 */
public class BoundedQueueTest {
    public static void main(String[] args) {
    	BoundedQueue<String> qu = new BoundedQueue<>(5);
    	for(int i = 0; i< 10; i++){
    		Thread t = new Thread(new Add(qu, i), "add线程" + i);
    		Thread t2 = new Thread(new Remove(qu), "remove线程" + i);
    		t2.start();
    		t.start();
    	}
	}
}

class Add implements Runnable{
	private BoundedQueue<String> qu;
	private int index;
	public Add(BoundedQueue<String> qu, int index){
		this.qu = qu;
		this.index = index;
	}

	@Override
	public void run() {
		try {
			qu.add("sss" + index);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}

class Remove implements Runnable{
	private BoundedQueue<String> qu;
	public Remove(BoundedQueue<String> qu){
		this.qu = qu;
	}

	@Override
	public void run() {
		try {
			qu.remove();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}