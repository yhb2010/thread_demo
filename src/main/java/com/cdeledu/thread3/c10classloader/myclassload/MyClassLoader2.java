package com.cdeledu.thread3.c10classloader.myclassload;

//破坏双亲委派，只需要重写loadClass方法
public class MyClassLoader2 extends MyClassLoader {
	
	@Override
	protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
		//根据类的全路径名称进行加锁，确保每一个类在多线程的情况下只被加载一次
		synchronized (getClassLoadingLock(name)) {
			//到已加载类的缓存中查看该类是否已经被加载，如果已经被加载则直接返回
			Class<?> klass = findLoadedClass(name);
			if(klass == null){
				//若缓存中没有被加载的类，则需要对其进行首次加载，如果类的全路径以java.和javax开头，则直接委托给系统类加载器进行加载
				if(name.startsWith("java.") || name.startsWith("javax")){
					try{
						klass = getSystemClassLoader().loadClass(name);
					}catch(Exception e){
						e.printStackTrace();
					}
				}else{
					try{
						//如果不是以java.和javax开头，则尝试使用我们自定义的类加载器进行加载
						klass = this.findClass(name);
					}catch(ClassNotFoundException e){
						e.printStackTrace();
					}
					//若自定义类加载器仍旧没有完成类的加载，则委托给其父类加载器进行加载或者系统类加载器进行加载
					if(klass == null){
						if(getParent() != null){
							klass = getParent().loadClass(name);
						}else{
							klass = getSystemClassLoader().loadClass(name);
						}
					}
				}
			}	
			//若经过若干次的尝试后，还是无法对类进行加载，则抛出无法找到类的异常
			if(null == klass){
				throw new ClassNotFoundException("the class " + name + " not found.");
			}
			if(resolve){
				resolveClass(klass);
			}
			return klass;
		}
	}

}