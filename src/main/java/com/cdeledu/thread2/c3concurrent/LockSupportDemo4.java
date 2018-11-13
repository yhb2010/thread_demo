package com.cdeledu.thread2.c3concurrent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;

public class LockSupportDemo4 {

	// 监控工作线程引用
	private static MonitorWorkThread workThread = null;

	public void initalizal() {
		// 实例化之后执行的初始化动作，用于启动值守监控线程来刷新加载数据
		workThread = new MonitorWorkThread();
		workThread.setDaemon(true);
		workThread.setName("AuthTaskMonitor");
		workThread.start();
	}

	/**
	 * 此为对外提供方法用于外部根据监控用户号获取内存中缓存的监控数据
	 * @param userno 监控用户号
	 * @return map key1:totalStatus key2:userno
	 * @throws Exception
	 */
	public Map<String, Object> monitorDataByUser(String userno)
			throws Exception {
		if (null == userno || "".equals(userno)) {
			return null;
		}
		Map<String, Object> retMap = new HashMap<>();
		if (null != workThread) {
			// 每次请求都去看看异步值守线程是否需求唤醒
			workThread.unPack();
		}
		return retMap;
	}
	
	public static void main(String[] args) throws Exception {
		LockSupportDemo4 d = new LockSupportDemo4();
		d.initalizal();
		TimeUnit.SECONDS.sleep(8);
		d.monitorDataByUser("aaa");
	}

}

class MonitorWorkThread extends Thread {
	
	// 当前线程停车标志
	private AtomicBoolean isPark = new AtomicBoolean(false);
	// 工作线程默认一秒钟加载一次,count即为工作监控线程每一次unpack之后会继续工作的时间，此值可根据实际需求配置化
	private int maxWorkCount = 5;

	@Override
	public void run() {
		int indexCount = 0;
		System.out.println("成功启动审核任务监控工作线程，当前工作线程每次unpack连续工作的时间设定为" + maxWorkCount + "秒");
		while (true) {
			if (indexCount >= maxWorkCount) {
				System.out.println("当前监控工作线程已到达连续工作时间设定上限，现在进入pack休眠状态");
				isPark.set(true);
				indexCount = 0;
				LockSupport.park();
			}
			// 从数据库中加载数据至内存
			try {
				loadDataFromDB();
			} catch (Exception e1) {
				System.out.println("从数据库中加载监控数据至内存发生异常");
			}
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				System.out.println("工作线程被异常中断唤醒");
			}
			indexCount++;
		}
	}
	
	/**
	 * 从数据库中加载内存数据至内存
	 */
	private void loadDataFromDB() throws Exception {
		System.out.println("开始从数据库中加载任务监控数据...");
		// do something about business....
		System.out.println("从数据库中加载任务监控数据完毕...");
	}

	/**
	 * 假如当前线程正在运行状态，donothing
	 */
	public void unPack() {
		// 唤醒当前监控工作线程，此处有并发唤醒动作需加锁
		if (isPark.compareAndSet(true, false)) {
			LockSupport.unpark(this);
			System.out.println("当前监控工作线程已被唤醒");
		}
	}

}