package com.cdeledu.thread3.c15监控任务的生命周期;

public class ObservableThread<T> extends Thread implements Observable {
	
	private final TaskLifecycle<T> lifecycle;
	private final Task<T> task;
	private Cycle cycle;

	//指定task实现，默认情况下使用EmptyLifecycle
	public ObservableThread(Task<T> task) {
		this(new TaskLifecycle.EmptyLifecycle<T>(), task);
	}

	public ObservableThread(TaskLifecycle<T> lifecycle, Task<T> task) {
		super();
		//task不允许为null
		if(task == null){
			throw new IllegalArgumentException("the task is required.");
		}
		this.lifecycle = lifecycle;
		this.task = task;
	}
	
	//不允许子类继承run方法，run方法在线程运行期间，可监控任务在执行过程中的各个生命周期阶段，任务每经过一个阶段相当于发生了一次事件。
	@Override
	public final void run(){
		//在执行线程逻辑单元的时候，分别触发相应的事件
		update(Cycle.STARTED, null, null);
		try{
			update(Cycle.RUNNING, null, null);
			T result = task.call();
			update(Cycle.DONE, result, null);
		}catch(Exception e){
			update(Cycle.ERROR, null, e);
		}
	}
	
	private void update(Cycle cycle, T result, Exception e) {
		this.cycle = cycle;
		if(lifecycle == null){
			return;
		}
		try{
			switch (cycle) {
			case STARTED:
				lifecycle.onStart(Thread.currentThread());
				break;
			case RUNNING:
				lifecycle.onRunning(Thread.currentThread());
				break;
			case DONE:
				lifecycle.onFinish(Thread.currentThread(), result);
				break;
			case ERROR:
				lifecycle.onError(Thread.currentThread(), e);
				break;
			}
		}catch(Exception ex){
			if(cycle == Cycle.ERROR){
				throw ex;
			}
		}
	}

	@Override
	public Cycle getCycle() {
		return cycle;
	}

}
