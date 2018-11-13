package com.cdeledu.thread.chapter3.happensbefore;

/**下面是Java内存模型中的八条可保证happen—before的规则，它们无需任何同步器协助就已经存在，可以在编码中直接使用。如果两个操作之间的关系不在此列，并且无法从下列规则推导出来的话，它们就没有顺序性保障，虚拟机可以对它们进行随机地重排序。

    1、程序次序规则：在一个单独的线程中，按照程序代码的执行流顺序，（时间上）先执行的操作happen—before（时间上）后执行的操作。

    2、管理锁定规则：一个unlock操作happen—before后面（时间上的先后顺序，下同）对同一个锁的lock操作。

    3、volatile变量规则：对一个volatile变量的写操作happen—before后面对该变量的读操作。

    4、线程启动规则：Thread对象的start（）方法happen—before此线程的每一个动作。

    5、线程终止规则：线程的所有操作都happen—before对此线程的终止检测，可以通过Thread.join（）方法结束、Thread.isAlive（）的返回值等手段检测到线程已经终止执行。

    6、线程中断规则：对线程interrupt（）方法的调用happen—before发生于被中断线程的代码检测到中断时事件的发生。

    7、对象终结规则：一个对象的初始化完成（构造函数执行结束）happen—before它的finalize（）方法的开始。

    8、传递性：如果操作A happen—before操作B，操作B happen—before操作C，那么可以得出A happen—before操作C。

这个例子说的是第1条原则
 * @author DELL
 *
 */
public class HappensBefore {

	public static void main(String[] args) {
		DomainDemo d = new DomainDemo();
		Thread t1 = new HappensBeforeThread(d);
		Thread t2 = new HappensBeforeThread2(d);
		t2.start();
		t1.start();
	}

}

class HappensBeforeThread extends Thread {

	private DomainDemo domainDemo;

	public HappensBeforeThread(DomainDemo domainDemo) {
		super();
		this.domainDemo = domainDemo;
	}

	@Override
	public void run(){
		System.out.println(domainDemo.getI());
	}

}

class HappensBeforeThread2 extends Thread {

	private DomainDemo domainDemo;

	public HappensBeforeThread2(DomainDemo domainDemo) {
		super();
		this.domainDemo = domainDemo;
	}

	@Override
	public void run(){
		domainDemo.setI(3);
	}

}

class DomainDemo{

	private int i = 0;

	public synchronized int getI(){
		return i;
	}

	public synchronized void setI(int i){
		System.out.println("set begin");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.i = i;
		System.out.println("set end");
	}

}