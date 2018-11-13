package com.cdeledu.thread2.c5.future;

//返回DAta对象，立即返回FutureData，并开启ClientThread线程装配RealData
public class Client {

	public Data request(final String queryStr){
		final FutureData future = new FutureData();
		new  Thread(){
			public void run(){
				//RealData的构建很慢，所以在单独的线程中进行
				RealData realdata = new RealData(queryStr);
				future.setRealData(realdata);
			}
		}.start();
		//FutureData会被立即返回
		return future;
	}

}
