package com.cdeledu.thread3.c19future;

/**用于提交任务，提交的任务主要有两种，第一种不需要返回值，第二种是需要获得最终的计算结果
 * @author Administrator
 *
 * @param <T>
 */
public interface FutureService<IN, OUT> {

	/**提交不需要返回值的任务，future.get方法返回的将是null
	 * @param runnable
	 * @return
	 */
	Future<?> submit(Runnable runnable);
	
	/**提交需要返回值的任务，其中Task接口替代Runnable接口
	 * @param runnable
	 * @return
	 */
	Future<OUT> submit(Task<IN, OUT> task, IN input);
	
	/**提交需要返回值的任务，其中Task接口替代Runnable接口，支持回调
	 * @param runnable
	 * @return
	 */
	Future<OUT> submit(Task<IN, OUT> task, IN input, Callback<OUT> callback);
	
	/**使用静态方法创建一个FutureService的实现
	 * @return
	 */
	static <T, R> FutureService<T, R> newService(){
		return new FutureServiceImpl<T, R>();
	}
	
}
