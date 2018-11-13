package com.cdeledu.thread3.c10classloader.myclassload;

public class HelloWorld {

	static{
		System.out.println("hello world class is init");
	}
	
	public String welcome(){
		return "hello world";
	}

	@Override
	public String toString() {
		return "HelloWorld []";
	}

}
