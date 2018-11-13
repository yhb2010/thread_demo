package com.cdeledu.thread3.c27active_objects.demo2;

import java.lang.reflect.Method;

import com.cdeledu.thread3.c19future.Future;
import com.cdeledu.thread3.c27active_objects.ActiveFuture;

/**相较于MethodMessage，ActiveMessage更加通用，其可以满足所有Active Object接口方法的要求，与MethodMessage类似，
 * ActiveMessage也是用于收集接口方法信息和具体的调用方法的。
 * 包可见，ActiveMessage只在框架内部使用，不会对外暴露。
 * @author Administrator
 *
 */
class ActiveMessage {
	
	//接口方法参数
	private final Object[] objects;
	//接口方法
	private final Method method;
	//有返回值的方法，会返回ActiveFuture<?>
	private final ActiveFuture<Object> future;
	//具体的Service接口
	private final Object service;
	
	/**构造ActiveMessage是由Builder来完成的
	 * @param builder
	 */
	public ActiveMessage(Builder builder){
		this.objects = builder.objects;
		this.method = builder.method;
		this.future = builder.future;
		this.service = builder.service;
	}
	
	/**
	 * ActiveMessage的方法通过反射的方式调用执行的具体实现
	 */
	public void execute(){
		try{
			//执行接口方法
			Object result = method.invoke(service, objects);
			if(future != null){
				//如果有返回值的接口方法，则需要通过get方法获得最终的结果
				Future<?> realFuture = (Future<?>)result;
				Object realResult = realFuture.get();
				//将结果交给ActiveFuture，接口方法的线程会得到返回
				future.finish(realResult);
			}
		}catch(Exception e){
			//如果发生异常，那么有返回值的方法将会显示的指定结果为null，无返回值的接口方法则会忽略异常
			if(future != null){
				future.finish(null);
			}
		}
	}
	
	//Builder主要负责对ActiveMessage的构建，是一种典型的Gof Builder设计模式
	static class Builder{
		private Object[] objects;
		private Method method;
		private ActiveFuture<Object> future;
		private Object service;
		
		public Builder useMethod(Method method){
			this.method = method;
			return this;
		}
		
		public Builder returnFuture(ActiveFuture<Object> future){
			this.future = future;
			return this;
		}
		
		public Builder withObjects(Object[] objects){
			this.objects = objects;
			return this;
		}
		
		public Builder forService(Object service){
			this.service = service;
			return this;
		}
		
		public ActiveMessage build(){
			return new ActiveMessage(this);
		}
	}

}
