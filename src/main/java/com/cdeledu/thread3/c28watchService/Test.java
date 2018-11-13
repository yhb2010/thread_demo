package com.cdeledu.thread3.c28watchService;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import com.cdeledu.thread3.c28event_bus.AsyncEventBus;
import com.cdeledu.thread3.c28event_bus.EventBus;

public class Test {
	
	/**在指定目录上，不断创建文件、删除文件
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(2);
		final EventBus eventBus = new AsyncEventBus(executor);
		eventBus.register(new FileChangeListener());
		DirectoryTargetMonitor monitor = new DirectoryTargetMonitor(eventBus, "F:\\zsl");
		monitor.startMonitor();
	}

}
