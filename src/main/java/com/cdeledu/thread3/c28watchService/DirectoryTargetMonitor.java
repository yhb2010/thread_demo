package com.cdeledu.thread3.c28watchService;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import com.cdeledu.thread3.c28event_bus.EventBus;

public class DirectoryTargetMonitor {
	
	//该类可以基于事件通知的方式监控文件或者目录的变化，文件的改变相当于每一个事件的发生，针对不同的事件执行不同的动作
	private WatchService watchService;
	private final EventBus eventBus;
	private final Path path;
	private volatile boolean start = false;
	
	public DirectoryTargetMonitor(final EventBus eventBus, final String targetPath){
		this(eventBus, targetPath, "");
	}

	/**构造Monitor的时候需要传入EventBus以及需要监控的目录
	 * @param eventBus2
	 * @param targetPath
	 * @param string
	 */
	public DirectoryTargetMonitor(EventBus eventBus, String targetPath, String... morePaths) {
		this.eventBus = eventBus;
		this.path = Paths.get(targetPath, morePaths);
	}
	
	public void startMonitor() throws Exception {
		watchService = FileSystems.getDefault().newWatchService();
		//为路径注册感兴趣的事件
		path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_CREATE);
		System.out.printf("the directory [%s] is monitoring...\n", path);
		start = true;
		while(start){
			WatchKey watchKey = null;
			try{
				//当有事件发生时会返回对应的WatchKey
				watchKey = watchService.take();
				watchKey.pollEvents().forEach(event -> {
					WatchEvent.Kind<?> kind = event.kind();
					Path path = (Path)event.context();
					Path child = this.path.resolve(path);
					//提交FileChangeEvent到EventBus
					eventBus.post(new FileChangeEvent(child, kind));
				});
			}catch(Exception e){
				start = false;
			}finally{
				if(watchKey != null){
					watchKey.reset();
				}
			}
		}
	}

	public void stopMonitor() throws Exception {
		System.out.printf("the directory [%s] monitor will be stop...\n", path);
		Thread.currentThread().interrupt();
		start = false;
		watchService.close();
		System.out.printf("the directory [%s] monitor will be stop done.\n", path);
	}
	
}
