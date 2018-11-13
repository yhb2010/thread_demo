package com.cdeledu.thread3.c10classloader.myclassload;

import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MyClassLoader extends ClassLoader {
	
	//定义默认的class存放路径
	private final static Path DEFAULT_CLASS_PATH = Paths.get("F:/zsl");
	private final Path classDir;

	//使用默认的class路径
	public MyClassLoader() {
		super();
		classDir = DEFAULT_CLASS_PATH;
	}

	//允许传入指定路径的class径路
	public MyClassLoader(String classDir) {
		super();
		this.classDir = Paths.get(classDir);
	}
	
	//指定class径路的同时，指定父类加载器
	public MyClassLoader(String classDir, ClassLoader parent) {
		super(parent);
		this.classDir = Paths.get(classDir);
	}
	
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		//读取class二进制文件
		byte[] classBytes = this.readClassBytes(name);
		if(null == classBytes || classBytes.length == 0){
			throw new ClassNotFoundException("can not load the class " + name);
		}
		//调用defineClass方法定义class
		return this.defineClass(name, classBytes, 0, classBytes.length);
	}

	private byte[] readClassBytes(String name) throws ClassNotFoundException {
		String classPath = name.replaceAll("\\.", "/");
		Path classFullPath = classDir.resolve(Paths.get(classPath + ".class"));
		if(!classFullPath.toFile().exists()){
			throw new ClassNotFoundException("can not load the class " + name);
		}
		try(ByteArrayOutputStream baos = new ByteArrayOutputStream()){
			Files.copy(classFullPath, baos);
			return baos.toByteArray();
		}catch (Exception e) {
			throw new ClassNotFoundException("can not load the class " + name);
		}
	}

	@Override
	public String toString() {
		return "MyClassLoader []";
	}

}
