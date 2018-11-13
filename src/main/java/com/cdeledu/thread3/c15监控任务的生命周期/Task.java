package com.cdeledu.thread3.c15监控任务的生命周期;

@FunctionalInterface
public interface Task<T> {
	
	/**任务执行接口，允许返回值
	 * @return
	 */
	T call();

}
