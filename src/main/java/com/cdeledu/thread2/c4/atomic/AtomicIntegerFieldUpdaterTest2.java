package com.cdeledu.thread2.c4.atomic;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public class AtomicIntegerFieldUpdaterTest2 {

	public static class Candidate{
		int id;
		volatile int score;
	}

    private final static AtomicIntegerFieldUpdater<Candidate> scoreUpdater = AtomicIntegerFieldUpdater.newUpdater(Candidate.class, "score");
    //检查updater是否正常工作
    private static AtomicInteger allScore = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
    	final Candidate stu = new Candidate();
    	Thread[] t = new Thread[10000];
    	for(int i=0;i<10000;i++){
    		t[i] = new Thread(){
    			public void run(){
    				if(Math.random() > 0.4){
    					scoreUpdater.incrementAndGet(stu);
    					allScore.incrementAndGet();
    				}
    			}
    		};
    		t[i].start();
    	}
    	for(int i=0;i<10000;i++){
    		t[i].join();
    	}
    	//上述代码模拟了计票场景，候选人的票数记录在stu.score中，注意，score是一个普通的volatile变量，而volatile变量并不是线程安全的。如果scoreUpdater真的保证了线程安全，
    	//那么scoreUpdater和allScore的值应该是相等的。
    	System.out.println("score=" + stu.score);
    	System.out.println("allScore=" + allScore.get());
    }

}
