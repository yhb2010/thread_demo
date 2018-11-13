package com.cdeledu.thread2.c5.future;

//真实数据，其构造比较慢
public class RealData implements Data {

	protected final String result;

	//构造很慢，需要用户等很久，这里使用sleep模拟
	public RealData(String para) {
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<10;i++){
			sb.append(para);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		result = sb.toString();
	}

	@Override
	public String getResult() {
		return result;
	}

}
