package com.cdeledu.thread3.c21threadlocal;

/**用ThreadLocal实现一个简单的线程上下文，一般某个动作或者任务都是交给一个线程去执行的，因此线程上下文的类可以命名为ActionContext或者TaskContext之类的。
 * @author Administrator
 *
 */
public class ActionContext2 {
	
	//定义ThreadLocal，并且使用Supplier的方式重写initValue
	private static final ThreadLocal<Configuration> confContext = ThreadLocal.withInitial(Configuration::new);
	private static final ThreadLocal<OtherResource> oresContext = ThreadLocal.withInitial(OtherResource::new);
	
	public static Configuration getConfContext() {
		return confContext.get();
	}
	
	public static OtherResource getOresContext() {
		return oresContext.get();
	}
	
	public static void setConfContext(Configuration conf) {
		confContext.set(conf);
	}
	
	public static void setOresContext(OtherResource ores) {
		oresContext.set(ores);
	}

}
