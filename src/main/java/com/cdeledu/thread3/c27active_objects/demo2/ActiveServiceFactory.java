package com.cdeledu.thread3.c27active_objects.demo2;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.validation.constraints.Future;

import com.cdeledu.thread3.c27active_objects.ActiveFuture;

/**是通用Active Object的核心类，其负责生成Service的代理以及构建ActiveMessage。
 * @author Administrator
 *
 */
public class ActiveServiceFactory {
	
	//定义ActiveMessageQueue，用于存放ActiveMessage
	private final static ActiveMessageQueue queue = new ActiveMessageQueue();
	
	//静态方法active会根据Active Service实例生成一个动态代理实例，其中会用到ActiveInvocationHandler作为newProxyInstance的InvocationHandler
	public static <T> T active(T instance){
		//生成Service的代理类
		Object proxy = Proxy.newProxyInstance(instance.getClass().getClassLoader(), instance.getClass().getInterfaces(), new ActiveInvocationHandler<>(instance));
		return (T)proxy;
	}
	
	private static class ActiveInvocationHandler<T> implements InvocationHandler {
		private final T instance;
		ActiveInvocationHandler(T instance){
			this.instance = instance;
		}
		
		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			//如果接口方法被@ActiveMessage标记，则会转换为ActiveMessage
			if(method.isAnnotationPresent(ActiveMethod.class)){
				//检测该方法是否符合规范：有返回值，必须是Future
				checkMethod(method);
				//定义ActiveMessage.Builder分别使用method、方法参数数据以及Active Service实例，如果该方法是Future的返回类型，则还需要定义ActiveFuture
				ActiveMessage.Builder builder = new ActiveMessage.Builder();
				builder.useMethod(method).withObjects(args).forService(instance);
				Object result = null;
				if(isReturnFutureType(method)){
					result = new ActiveFuture<T>();
					builder.returnFuture((ActiveFuture)result);
				}
				//将ActiveMessage加入队列中，并且返回method方法的invoke结果
				queue.offer(builder.build());
				return result;
			}else{
				//如果是普通方法(没有@ActiveMessage标记)，则会正常返回
				return method.invoke(instance, args);
			}
		}

		//判断方法是否为Future返回类型
		private boolean isReturnFutureType(Method method) {
			return method.getReturnType().isAssignableFrom(Future.class);
		}
		
		//判断方法是否无返回类型
		private boolean isReturnVoidType(Method method) {
			return method.getReturnType().equals(Void.TYPE);
		}

		//检查有返回值的方法是否为Future，否则将会抛出IllegalActiveMethod异常
		private void checkMethod(Method method) throws IllegalActiveMethod {
			//有返回值，必须是ActiveFuture类型的返回值
			if(!isReturnVoidType(method) && !isReturnFutureType(method)){
				throw new IllegalActiveMethod("the method [" + method.getName() + " ] return type must be void/Future");
			}
		}
		
	}

}
