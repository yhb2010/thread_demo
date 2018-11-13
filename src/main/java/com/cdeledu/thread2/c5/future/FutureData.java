package com.cdeledu.thread2.c5.future;

//Future数据，构造很快，但是是一个虚拟的数据，需要装配RealData
public class FutureData implements Data {

	protected RealData realdata = null;
	protected boolean isReady = false;

	public synchronized void setRealData(RealData realdata){
		if(isReady){
			return;
		}
		this.realdata = realdata;
		isReady = true;
		notifyAll();
	}

	@Override
	public synchronized String getResult() {
		while(!isReady){
			try{
				wait();
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		return realdata.result;
	}

}
