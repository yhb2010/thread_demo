package com.cdeledu.thread3.c8.myselfthreadpool;

/**任务队列，主要用于缓存提交到线程池中的任务
 * @author Administrator
 *
 */
public interface RunnableQueue {

	/**当有新的任务进来时，首先会offer到队列中
	 * @param runnable
	 */
	void offer(Runnable runnable);
	
	/**工作线程通过take方法获取Runnable
	 * @return
	 * @throws InterruptedException
	 */
	Runnable take() throws InterruptedException;
	
	/**获取任务队列中任务的数量
	 * @return
	 */
	int size();
	
}
