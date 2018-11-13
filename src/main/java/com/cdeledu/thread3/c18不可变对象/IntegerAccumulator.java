package com.cdeledu.thread3.c18不可变对象;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

//不可变对象使用final修饰为了防止由于继承重写而导致失去线程安全性，另外init属性被final修饰不允许线程对其进行改变，在构造函数赋值后将不会再改变。
public final class IntegerAccumulator {

	private final int init;
	
	public IntegerAccumulator(int init){
		this.init = init;
	}
	
	public IntegerAccumulator(IntegerAccumulator acc, int init){
		this.init = acc.getValue() + init;
	}
	
	//每次相加都会产生一个新的IntegerAccumulator
	public IntegerAccumulator add(int i){
		return new IntegerAccumulator(this, i);
	}
	
	public int getValue(){
		return init;
	}
	
	public static void main(String[] args) {
		IntegerAccumulator acc = new IntegerAccumulator(0);
		IntStream.range(0, 3).forEach(i -> new Thread(() -> {
			int inc = 0;
			while(true){
				int oldValue = acc.getValue();
				IntegerAccumulator newAcc = acc.add(inc);
				int result = newAcc.getValue();
				System.out.println(oldValue + " + " + inc + " = " + result);
				if(inc + oldValue != result){
					System.err.println("Error:" + oldValue + " + " + inc + " = " + result);
				}
				inc++;
				slowly();
			}
		}).start());
	}

	private static void slowly() {
		try {
			TimeUnit.MICROSECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
