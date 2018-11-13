package com.cdeledu.thread3.c19future;

/**提供给调用者实现计算逻辑之用的，可以接受一个参数并且返回最终的计算结果，类似CAllable接口
 * @author Administrator
 *
 * @param <IN>
 * @param <OUT>
 */
@FunctionalInterface
public interface Task<IN, OUT> {

	/**给定一个参数，经过计算返回结果
	 * @param input
	 * @return
	 */
	OUT get(IN input);
	
}
