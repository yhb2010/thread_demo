package com.cdeledu.thread3.c7.hook;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;
import java.util.concurrent.TimeUnit;

//我们在开发中经常会遇到hook线程，比如为了防止某个程序被重复启动，在进程启动时会创建一个lock文件，进程收到中断信号的时候会删除这个lock文件。
//hook的注意事项：
//1、hook线程只有在收到退出信号时才会被执行，如果在kill的时候使用了参数-9，那么hook线程不会得到执行，进程将会立即退出，因此.lock文件将得不到清理
//2、hook线程中也可以执行一些资源释放的工作，比如关闭文件句柄，socket连接、数据库connection等
//3、尽量不要在hook线程中执行一些耗时操作，因为会导致程序迟迟不能退出。
public class PreventDuplicated {
	
	private final static String LOCK_PATH = "F:/zsl";
	private final static String LOCK_FILE = ".lock";
	private final static String PERMISSIONS = "rw-rw-rw-";

	public static void main(String[] args) throws IOException {
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run(){
				System.out.println("The program received kill signal.");
				getLockFile().toFile().delete();
			}
		});
		
		//检查是否存在.lock文件
		checkRunning();
		
		//简单模拟当前程序正在运行
		try{
			TimeUnit.SECONDS.sleep(5);
			System.out.println("program is running.");
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}

	private static void checkRunning() throws IOException {
		Path path = getLockFile();
		if(path.toFile().exists()){
			throw new RuntimeException("the program already running.");
		}
		//windows下不支持PosixFilePermission来指定rwx权限。
		//Set<PosixFilePermission> perms = PosixFilePermissions.fromString(PERMISSIONS);
		//Files.createFile(path, PosixFilePermissions.asFileAttribute(perms));
		Files.createFile(path);
	}
	
	private static Path getLockFile(){
		return Paths.get(LOCK_PATH, LOCK_FILE);
	}
	
}
