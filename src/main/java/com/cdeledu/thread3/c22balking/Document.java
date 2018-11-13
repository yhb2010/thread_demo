package com.cdeledu.thread3.c22balking;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**代表文档本身，在Document中有两个主要方法save和edit分别用于保存文档和编辑文档
 * @author Administrator
 *
 */
public class Document {
	
	//如果文档发生了变化，会被设置为true
	private boolean changed = false;
	//一次需要保存的内容，可以将其理解为内容缓存
	private List<String> content = new ArrayList<>();
	private final FileWriter writer;
	//自动保存文档的线程
	private static AutoSaveThread autoSaveThread;
	
	/**构造函数需要传入文档的保存路径和文档名称
	 * @param documentPath
	 * @param documentName
	 * @throws IOException 
	 */
	private Document(String documentPath, String documentName) throws IOException{
		writer = new FileWriter(new File(documentPath, documentName));
	}
	
	/**静态方法，主要用于创建文档，顺便启动自动保存文档的线程
	 * @param documentPath
	 * @param documentName
	 * @return
	 * @throws IOException 
	 */
	public static Document create(String documentPath, String documentName) throws IOException{
		Document document = new Document(documentPath, documentName);
		autoSaveThread = new AutoSaveThread(document);
		autoSaveThread.start();
		return document;
	}
	
	/**编辑文档，其实就是往content队列中提交字符串
	 * 同步是为了防止当文档在保存过程中如果遇到新的内容被编辑时引起的共享资源冲突问题
	 * @param content
	 */
	public void edit(String str){
		synchronized (this) {
			content.add(str);
			changed = true;
		}
	}
	
	/**
	 * 文档关闭的时候首先中断自动保存线程，然后关闭writer释放资源
	 * @throws IOException 
	 */
	public void close() throws IOException{
		autoSaveThread.interrupt();
		writer.close();
	}
	
	public void save() throws IOException{
		synchronized (this) {
			//balking，如果文档已经被保存了，则直接返回
			if(!changed){
				return;
			}
			System.out.println(Thread.currentThread().getName() + " executr the save action.");
			//将内容写入文档中
			for (String cacheLine : content) {
				writer.write(cacheLine);
				writer.write("\r\n");
			}
			writer.flush();
			//将changed改为false，表明此刻再没有新的内容编辑
			changed = false;
			content.clear();
		}
	}

}
