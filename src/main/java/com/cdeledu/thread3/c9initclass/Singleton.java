package com.cdeledu.thread3.c9initclass;

public class Singleton {

	private static int x=1;
	private static int y;
	private static Singleton instance = new Singleton();
	
	private Singleton() {
		x++;
		y++;
	}
	
	public static Singleton getInstance(){
		return instance;
	}
	
	public static void main(String[] args) {
		//在连接阶段的准备过程中，每一个变量都被赋予了初始值：x=0, y=0, instance=null
		//类的初始化阶段：会为每一个类变量赋予正确的值，也就是执行<clinit>()方法的过程：x=0, y=0, instance=new Singleton()
		//在new Singleton()的时候执行类的构造函数，而在构造函数中分别对x和y进行了自增：x=1, y=1
		Singleton s = Singleton.getInstance();
		System.out.println(s.x);
		System.out.println(s.y);
	}

}
