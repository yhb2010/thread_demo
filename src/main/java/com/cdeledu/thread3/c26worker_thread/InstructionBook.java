package com.cdeledu.thread3.c26worker_thread;

/**抽象类：产品及组装说明书
 * 在流水线上需要被加工的产品，create作为一个模板方法，提供了加工产品的说明书
 * @author Administrator
 *
 */
public abstract class InstructionBook {

	public final void create(){
		firstProcess();
		secondProcess();
	}

	protected abstract void firstProcess();
	protected abstract void secondProcess();
	
}
