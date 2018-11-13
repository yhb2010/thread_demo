package com.cdeledu.thread2.c5.singleton;

/**单例最佳模式：
 * 首先getInstance方法没有锁，这使得在高并发下性能优越。
 * 其次，只有在getInstance方法被第一次调用时，LazySingleton实例才会被创建。因为这种方法使用了内部类和类的初始化方式。
 * 内部类SingleHolder被声明为private，使得外部不能访问。而只能通过getInstance内部对SingleHolder类进行初始化。
 * @author DELL
 *
 */
public class LazySingleton{
	private LazySingleton(){
		System.out.println("LazySingleton is create");
	}
	private static class SingleHolder{
		private static LazySingleton instance = new LazySingleton();
	}
	public static LazySingleton getInstance(){
		return SingleHolder.instance;
	}
}