package com.cdeledu.thread3.c2;

public class ThreadGroupDemo {
	
	//得出结论：
	//1、main线程所在的ThreadGroup称为main
	//2、构造一个线程的时候如果没有显式的指定ThreadGroup，那么他将会和父线程同属于一个ThreadGroup
	public static void main(String[] args) {
		Thread t1 = new Thread("t1");
		ThreadGroup group = new ThreadGroup("testGroup");
		Thread t2 = new Thread(group, "t2");
		ThreadGroup mainGroup = Thread.currentThread().getThreadGroup();
		System.out.println(mainGroup.getName());
		System.out.println(mainGroup == t1.getThreadGroup());
		System.out.println(mainGroup == t2.getThreadGroup());
		System.out.println(group == t2.getThreadGroup());
	}

}
