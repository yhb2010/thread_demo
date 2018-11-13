package com.cdeledu.thread3.c22balking;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**自动保存功能
 * @author Administrator
 *
 */
public class AutoSaveThread extends Thread {

	private final Document document;
	
	public AutoSaveThread(Document document){
		super("DocumentAutoSaveThread");
		this.document = document;
	}
	
	@Override
	public void run(){
		while(true){
			try {
				document.save();
				//每隔1秒自动保存一次
				TimeUnit.SECONDS.sleep(5);
			} catch (IOException |InterruptedException e) {
				break;
			}
		}
	}
	
}
