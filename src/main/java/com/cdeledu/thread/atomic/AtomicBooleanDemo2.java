package com.cdeledu.thread.atomic;

import java.util.concurrent.atomic.AtomicBoolean;

//compareAndSet可以原子执行，确保程序的正确执行
public class AtomicBooleanDemo2 {
    public static void main(String[] args) {
        Thread thread3 = new Thread(new TestAtomicBoolean("李四"));
        Thread thread4 = new Thread(new TestAtomicBoolean("张三"));

        thread3.start();
        thread4.start();
    }
}

class NormalBoolean2 implements Runnable{


    public static boolean exits = false;

    private String name;

    public NormalBoolean2(String name) {
        this.name = name;
    }

    @Override
    public void run() {

        if(!exits){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            exits = true;
            System.out.println(name + ",step 1");

            System.out.println(name + ",step 2");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(name + ",step 3");
            exits = false;
        } else {
            System.out.println(name + ",step else");
        }

    }
}

class TestAtomicBoolean implements Runnable{

    public static AtomicBoolean exits = new AtomicBoolean(false);

    private String name;

    public TestAtomicBoolean(String name) {
        this.name = name;
    }

    @Override
    public void run() {

        if(exits.compareAndSet(false,true)){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(name + ",step 1");

            System.out.println(name + ",step 2");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(name + ",step 3");
            exits.set(false);
        } else {
            System.out.println(name + ",step else");
        }

    }
}