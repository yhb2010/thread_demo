package com.cdeledu.thread3.c8.myselfthreadpool;

/**提供了创建线程的接口，以便于个性化的定制Thread，比如Thread应该被加到哪个Group中，优先级、线程名字以及是否为守护线程等
 * @author Administrator
 *
 */
@FunctionalInterface
public interface ThreadFactory {
	
	/**用于创建线程
	 * @param runnable
	 * @return
	 */
	Thread createThread(Runnable runnable);

}
