package com.cdeledu.thread3.c28watchService;

import com.cdeledu.thread3.c28event_bus.Subscribe;

public class FileChangeListener {
	
	@Subscribe
	public void onChange(FileChangeEvent event){
		System.out.printf("%s-%s\n", event.getPath(), event.getKind());
	}

}
