package com.cdeledu.thread3.c27active_objects;

import com.cdeledu.thread3.c19future.FutureTask;

/**将finish的protect级别改为public级别
 * @author Administrator
 *
 * @param <T>
 */
public class ActiveFuture<T> extends FutureTask<T> {
	
	@Override
	public void finish(T result){
		super.finish(result);
	}

}
