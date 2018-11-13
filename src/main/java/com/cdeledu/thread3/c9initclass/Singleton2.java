package com.cdeledu.thread3.c9initclass;

public class Singleton2 {

	private static Singleton2 instance = new Singleton2();
	private static int x=0;
	private static int y;
	
	private Singleton2() {
		x++;
		y++;
	}
	
	public static Singleton2 getInstance(){
		return instance;
	}
	
	public static void main(String[] args) {
		//在连接阶段的准备过程中，每一个变量都被赋予了初始值：instance=null, x=0, y=0
		//在类的初始化阶段，需要为每一个类赋予程序编写时所期望的正确的初始值，首先会进入instance的构造函数，执行完构造函数后，各个变量的值如下：instance=Singleton2@3f99bd52, x=1, y=1
		//然后，为x初始化，由于x有显示的进行赋值，因此0才是所期望的正确赋值，而y由于没有给定初始值，在构造函数中计算所得到值就是所谓的正确赋值，因此结果为：instance=Singleton2@3f99bd52, x=0, y=1
		Singleton2 s = Singleton2.getInstance();
		System.out.println(s.x);
		System.out.println(s.y);
	}

}
