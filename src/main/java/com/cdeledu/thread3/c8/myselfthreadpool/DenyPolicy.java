package com.cdeledu.thread3.c8.myselfthreadpool;

/**用于当Queue中的Runnable达到了limit上限时，决定采用何种策略通知提交者。定义了三个默认实现。
 * @author Administrator
 *
 */
@FunctionalInterface
public interface DenyPolicy {

	void reject(Runnable runnable, ThreadPool threadPool);
	
	/**该拒绝策略会直接将任务丢弃
	 * @author Administrator
	 *
	 */
	class DiscardDenyPolicy implements DenyPolicy {
		@Override
		public void reject(Runnable runnable, ThreadPool threadPool) {
			//do nothing
			System.out.println("discard");
		}
	}
	
	/**该拒绝策略会向任务提交者抛出异常
	 * @author Administrator
	 *
	 */
	class AbortDenyPolicy implements DenyPolicy {
		@Override
		public void reject(Runnable runnable, ThreadPool threadPool) {
			throw new RunnableDenyException("the runnable " + runnable + " will be abort.");
		}
	}
	
	/**该拒绝策略会使任务在提交者所在的线程中执行任务
	 * @author Administrator
	 *
	 */
	class RunnerDenyPolicy implements DenyPolicy {
		@Override
		public void reject(Runnable runnable, ThreadPool threadPool) {
			if(!threadPool.isShutdown()){
				runnable.run();
			}
		}
	}
	
}
