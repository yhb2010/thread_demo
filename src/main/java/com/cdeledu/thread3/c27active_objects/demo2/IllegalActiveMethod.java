package com.cdeledu.thread3.c27active_objects.demo2;

/**若方法不符合则其被转换为Active方法时会抛出异常
 * @author Administrator
 *
 */
public class IllegalActiveMethod extends Exception {
	
	public IllegalActiveMethod(String message){
		super(message);
	}

}
