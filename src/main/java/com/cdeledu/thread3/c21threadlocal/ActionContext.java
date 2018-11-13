package com.cdeledu.thread3.c21threadlocal;

/**用ThreadLocal实现一个简单的线程上下文，一般某个动作或者任务都是交给一个线程去执行的，因此线程上下文的类可以命名为ActionContext或者TaskContext之类的。
 * @author Administrator
 *
 */
public class ActionContext {
	
	//定义ThreadLocal，并且使用Supplier的方式重写initValue
	private static final ThreadLocal<Context> context = ThreadLocal.withInitial(Context::new);
	
	public static Context get(){
		return context.get();
	}
	
	/**每一个线程都会有一个独立的Context实例
	 * @author Administrator
	 *
	 */
	static class Context{
		//在Context中的其他成员
		private Configuration conf;
		private OtherResource ores;
		
		public Configuration getConf() {
			return conf;
		}
		public void setConf(Configuration conf) {
			this.conf = conf;
		}
		public OtherResource getOres() {
			return ores;
		}
		public void setOres(OtherResource ores) {
			this.ores = ores;
		}
		@Override
		public String toString() {
			return "Context [conf=" + conf + ", ores=" + ores + "]";
		}
	}

}
