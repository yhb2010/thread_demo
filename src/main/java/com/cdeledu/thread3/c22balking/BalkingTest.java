package com.cdeledu.thread3.c22balking;

/**多个线程监控某个共享变量，A线程监控到共享变量发生变化后即将触发某个动作，但是此时发现有另外一个线程B已经对该变量的变化开始了行动，因此A便放弃了准备开始的工作，我们把这样的线程间交互称为Balking模式
 * 简单说就是某个线程因为发现其他线程正在进行相同的工作而放弃即将开始的任务，在本章中，我们将通过模拟word文档自动保存与手动保存的功能讲解Balking模式。
 * 我们在用word时，每次的文字编辑都代表着文档状态的发生了改变，除了我们可以手动保存外，word也有自动保存，如果word自动保存的线程在准备执行保存动作时，我们正好在进行手动保存，那么自动保存的线程将会放弃此次保存动作。
 * @author Administrator
 *
 */
public class BalkingTest {

	public static void main(String[] args) {
		new DocumentEditThread("F:\\zsl", "balking.txt").start();
	}
	
}
