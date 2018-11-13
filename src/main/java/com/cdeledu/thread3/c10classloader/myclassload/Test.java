package com.cdeledu.thread3.c10classloader.myclassload;

import java.lang.reflect.Method;

public class Test {

	public static void main(String[] args) throws Exception {
		//如果调用默认的构造方法，就需要删除ide开发工具的编译目录下的class文件，不然会用应用类加载器加载，而不用自定义类加载器加载；或者直接使用MyClassLoader2
		//MyClassLoader loader = new MyClassLoader();
		//MyClassLoader loader = new MyClassLoader("F:/zsl", null);
		MyClassLoader2 loader = new MyClassLoader2();
		Class<?> aClass = loader.loadClass("com.cdeledu.thread3.c10classloader.myclassload.HelloWorld");
		System.out.println(aClass.getClassLoader());
		Object o = aClass.newInstance();
		System.out.println(o);
		Method m = aClass.getMethod("welcome");
		String r = (String)m.invoke(o);
		System.out.println(r);
	}

}
