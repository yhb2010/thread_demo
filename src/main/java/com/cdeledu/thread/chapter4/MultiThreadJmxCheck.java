package com.cdeledu.thread.chapter4;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

public class MultiThreadJmxCheck {

	public static void main(String[] args) {
		//获取java线程管理MXBean
		ThreadMXBean bean = ManagementFactory.getThreadMXBean();
		//不需要获取同步的monitor和synchronizer信息，仅获取线程和线程堆栈信息
		ThreadInfo[] infos = bean.dumpAllThreads(false, false);
		//遍历线程信息，仅打印线程id和线程名称信息
		for (ThreadInfo info : infos) {
			System.out.println("[" + info.getThreadId() + "]" + info.getThreadName());
		}

		//输出：
		//[4]Signal Dispatcher，分发处理发送给jvm信号的线程
		//[3]Finalizer，调用对象finalize方法的线程
		//[2]Reference Handler，清除reference的线程
		//[1]main，main线程，用户程序入口
		//可以看到，一个java程序的运行不仅仅是main方法的运行，而是main线程和多个其它线程的同时运行。

	}

}
