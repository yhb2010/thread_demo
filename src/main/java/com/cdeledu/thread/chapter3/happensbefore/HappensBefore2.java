package com.cdeledu.thread.chapter3.happensbefore;

/**
这个例子说的是第3条原则
 * @author DELL
 *
 */
public class HappensBefore2 {

	public static void main(String[] args) {
		DomainDemo2 d = new DomainDemo2();
		Thread t1 = new HappensBefore2Thread(d);
		Thread t2 = new HappensBefore2Thread2(d);
		t2.start();
		t1.start();
	}

}

class HappensBefore2Thread extends Thread {

	private DomainDemo2 domainDemo;

	public HappensBefore2Thread(DomainDemo2 domainDemo) {
		super();
		this.domainDemo = domainDemo;
	}

	@Override
	public void run(){
		System.out.println(domainDemo.getI());
	}

}

class HappensBefore2Thread2 extends Thread {

	private DomainDemo2 domainDemo;

	public HappensBefore2Thread2(DomainDemo2 domainDemo) {
		super();
		this.domainDemo = domainDemo;
	}

	@Override
	public void run(){
		domainDemo.setI(3);
	}

}

class DomainDemo2{

	private volatile int i = 0;

	public int getI(){
		return i;
	}

	public void setI(int i){
		this.i = i;
	}

}