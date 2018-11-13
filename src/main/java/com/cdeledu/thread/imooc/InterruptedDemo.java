package com.cdeledu.thread.imooc;

//当一个线程处于睡眠阻塞时,若被其他线程调用.interrupt方法中断,则sleep方法会抛出InterruptedException异常
public class InterruptedDemo {

	public static void main(String[] args) {
		/*
         * 表演者：处于睡眠阻塞的线程。
         * 当一个方法中的局部内部类中需要引用该方法的其他局部变量，那么这个变量必须是final的
         */
        final Thread lin = new Thread() {
            public void run() {
                System.out.println("林:刚美完容，睡觉吧!");
                try{
                    // 当一个线程处于睡眠阻塞时，若被其他线程调用interrupt()方法中断，则sleep()方法会抛出 InterruptedException异常
                    Thread.sleep(100000000);
                }
                catch(InterruptedException e){
                    System.out.println("林:干嘛呢！都破了相了!");
                }
            }
        };
        /*
         * 表演者:中断睡眠阻塞的线程
         */
        Thread huang = new Thread() {
            public void run() {
                System.out.println("黄:开始砸墙！");
                for(int i = 0; i < 5; i++){
                    System.out.println("黄:80!");
                    try{
                        Thread.sleep(1000);
                    }
                    catch(InterruptedException e){
                    }
                }
                System.out.println("咣当!");
                System.out.println("黄:搞定！");

                // 中断lin的睡眠阻塞
                lin.interrupt();
            }
        };
        lin.start();
        huang.start();
	}

}
