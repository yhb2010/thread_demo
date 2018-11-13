package com.cdeledu.thread.chapter8;import java.util.concurrent.ExecutorService;import java.util.concurrent.Executors;import java.util.concurrent.Semaphore;/** * * @author tengfei.fangtf */public class SemaphoreTest {    private static final int       THREAD_COUNT = 30;    private static ExecutorService threadPool   = Executors.newFixedThreadPool(THREAD_COUNT);    //表示可用的许可证数量，10表示允许10个线程获取许可证，也就是最大并发数是10.    private static Semaphore       s            = new Semaphore(10);    public static void main(String[] args) {        for (int i = 0; i < THREAD_COUNT; i++) {            threadPool.execute(new Runnable() {                @Override                public void run() {                    try {                    	//获取一个许可证                    	//tryAcquire方法尝试获取许可证                        s.acquire();                        System.out.println("save data");                        //使用完之后，调用release方法归还许可证                        s.release();                    } catch (InterruptedException e) {                    }                }            });        }        threadPool.shutdown();    }}