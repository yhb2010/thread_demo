package com.cdeledu.thread3.c29event_driven.sync;

/**每一个Event可以被称为Message，Message是对Event更高一个层级的抽象，每一个MEssage都有一个特定的Type用于与对应的Handler做关联
 * @author Administrator
 *
 */
public interface Message {
	
	Class<? extends Message> getType(); 

}
