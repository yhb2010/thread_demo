package com.cdeledu.thread.chapter4.pool;

import java.sql.Connection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import com.cdeledu.thread.chapter4.pool.ConnectionPool;

//测试连接池的工作情况，模拟客户端ConnectionRunner获取、使用、最后释放连接的过程，当它使用时连接将会增加获取到连接的数量，反之，将会增加未获取到连接的数量。
public class ConnectionPoolTest {

	static ConnectionPool pool = new ConnectionPool(10);
	//保证所有ConnectionRunner能够同时开始
	static CountDownLatch start = new CountDownLatch(1);
	//main线程将会等待所有ConnectionRunner结束后才能继续执行
	static CountDownLatch end;

	public static void main(String[] args) throws InterruptedException {
		//线程数量，可以修改线程数量进行观察
		int threadCount = 50;
		end = new CountDownLatch(threadCount);
		int count = 20;
		AtomicInteger got = new AtomicInteger();
		AtomicInteger notGot = new AtomicInteger();
		for(int i=0; i<threadCount; i++){
			Thread thread = new Thread(new ConnectionRunner(count, got, notGot), "ConnectionRunnerThread");
			thread.start();
		}
		start.countDown();
		end.await();
		System.out.println("total invoke:" + (threadCount * count));
		System.out.println("got connection:" + got);
		System.out.println("notGot connection:" + notGot);
	}

	static class ConnectionRunner implements Runnable{

		int count;
		AtomicInteger got;
		AtomicInteger notGot;

		public ConnectionRunner(int count, AtomicInteger got, AtomicInteger notGot){
			this.count = count;
			this.got = got;
			this.notGot = notGot;
		}

		@Override
		public void run() {
			try{
				start.await();
			}catch(Exception e){

			}
			while(count > 0){
				try{
					//从线程池中获取连接，如果50ms内无法获取到，将会返回null
					//分别统计连接获取的数量got和未获取到连接的数量notGot
					Connection conn = pool.fetchConnection(50);
					if(conn != null){
						try{
							conn.createStatement();
							conn.commit();
						}finally{
							pool.releaseConnection(conn);
							got.incrementAndGet();
						}
					}else{
						notGot.incrementAndGet();
					}
				}catch(Exception ex){

				}finally{
					count--;
				}
			}
			end.countDown();
		}

	}

}
