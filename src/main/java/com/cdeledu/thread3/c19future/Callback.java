package com.cdeledu.thread3.c19future;

/**类似于Consumer函数接口
 * @author Administrator
 *
 * @param <T>
 */
@FunctionalInterface
public interface Callback<T> {

	/**给定一个参数，经过计算返回结果
	 * @param input
	 * @return
	 */
	void call(T t);
	
}
