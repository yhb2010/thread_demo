package com.cdeledu.thread3.c26worker_thread;

/**传送带上除了有说明书以外，还需要产品自身，产品继承了说明书，每个产品都有产品编号
 * @author Administrator
 *
 */
public class Production extends InstructionBook {
	private final int prodID;
	public Production(int prodID){
		this.prodID = prodID;
	}
	
	@Override
	protected void firstProcess() {
		System.out.println("executor the " + prodID + " first process.");
	}

	@Override
	protected void secondProcess() {
		System.out.println("executor the " + prodID + " second process.");
	}

}
