package com.cdeledu.thread3.c9initclass;

import java.util.Random;

class InnerGlobalConstants {

	static{
		System.out.println("the GlobalConstants will be init");
	}
	//其他类中使用MAX不会导致InnerGlobalConstants初始化，静态代码块不会输出
	public final static int MAX = 100;
	//虽然RANDOM是静态常量，但是由于计算复杂，只有初始化之后才能得到结果，因此在其他类中使用RANDOM会导致InnerGlobalConstants初始化
	public final static int RANDOM = new Random().nextInt();

}

public class GlobalConstants {

	public static void main(String[] args) {
		//创建数组不会导致类初始化，事实上该操作只不过是在堆内存中开辟了一段连续的地址空间
		InnerGlobalConstants[] arr = new InnerGlobalConstants[10];
		System.out.println(InnerGlobalConstants.MAX);
		//new InnerGlobalConstants();
		//System.out.println(InnerGlobalConstants.RANDOM);
	}

}
