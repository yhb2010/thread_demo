package com.cdeledu.thread2.c4.threadlocal;

public class Context {

	private static final ThreadLocal<Integer> mThreadLocal = new ThreadLocal<Integer>();

	public static void setTrackerID(Integer id) {
		mThreadLocal.set(id);
	}

	public static Integer getTrackerID() {
		return mThreadLocal.get();
	}

}
