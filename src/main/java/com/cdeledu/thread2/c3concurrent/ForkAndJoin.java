package com.cdeledu.thread2.c3concurrent;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

//fork用来创建子进程，使得系统进程可以多一个执行分支。
//join表示等待，也就是fork后系统多了一个执行分支（线程），所以需要等待这个执行分支执行完毕，才有可能得到最终的结果。
//RecursiveTask：没有返回值
//RecursiveAction：有返回值
public class ForkAndJoin extends RecursiveTask<Long> {
	private static final int THRESHOLD = 10000;
	private long start;
	private long end;
	public ForkAndJoin(long start, long end) {
		this.start = start;
		this.end = end;
	}
	@Override
	protected Long compute() {
		long sum = 0;
		boolean canCompute = (end-start)<THRESHOLD;
		if(canCompute){
			for(long i =start; i<=end; i++){
				sum += i;
			}
		}else{
			//分成100个小任务
			long step = (start+end)/100;
			ArrayList<ForkAndJoin> subs = new ArrayList<>();
			long pos = start;
			for(int i=0;i<100;i++){
				long lastOne = pos + step;
				if(lastOne>end)lastOne=end;
				ForkAndJoin sub = new ForkAndJoin(pos, lastOne);
				pos += step+1;
				subs.add(sub);
				sub.fork();
			}
			for(ForkAndJoin t: subs){
				sum += t.join();
			}
		}
		return sum;
	}

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		ForkJoinPool pool = new ForkJoinPool();
		ForkAndJoin task = new ForkAndJoin(0, 200000l);
		ForkJoinTask<Long> result = pool.submit(task);
		long res = result.get();
		System.out.println(res);
	}

}
