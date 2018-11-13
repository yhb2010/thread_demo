package com.cdeledu.thread.chapter3.volatite;

//Java 语言提供了一种稍弱的同步机制,即 volatile 变量.用来确保将变量的更新操作通知到其他线程,保证了新值能立即同步到主内存,以及每次使用前立即从主内存刷新. 当把变量声明为volatile类型后,编译器与运行时都会注意到这个变量是共享的.
//volatile 变量对所有线程是立即可见的,对 volatile 变量所有的写操作都能立即反应到
//其他线程之中,换句话说:volatile 变量在各个线程中是一致的,所以基于 volatile 变量的运算是线程安全的.
//这句话论据貌似没有错,论点确实错的.
public class VolatileTest {

	public static void main(String[] args) {
		VolatileDomain d = new VolatileDomain();
		for(int i = 0; i<100000; i++){
			Thread t1 = new VolatileTestThread(d);
			t1.start();
		}
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(d.i);
	}
}

class VolatileTestThread extends Thread {

	private VolatileDomain domainDemo;

	public VolatileTestThread(VolatileDomain domainDemo) {
		super();
		this.domainDemo = domainDemo;
	}

	@Override
	public void run(){
		domainDemo.increase();
	}

}

/**
 *
public class VolatileTest {
  public static volatile int i;

  public VolatileTest();
    Code:
       0: aload_0
       1: invokespecial #1                  // Method java/lang/Object."<init>":()V
       4: return
    LineNumberTable:
      line 1: 0

  public static void increase();
    Code:
       0: getstatic     #2                  // Field i:I, 把i的值取到了操作栈顶,volatile保证了i值此时是正确的.
       3: iconst_1
       4: iadd                              // increase,但其他线程此时可能已经把i值加大了好多
       5: putstatic     #2                  // Field i:I ,把这个已经out of date的i值同步回主内存中,i值被破坏了.
       8: return
    LineNumberTable:
      line 6: 0
      line 7: 8
}
 *
 * @author DELL
 *
 */
class VolatileDomain{

    public volatile int i;

    public void increase(){
        i++;
    }
}