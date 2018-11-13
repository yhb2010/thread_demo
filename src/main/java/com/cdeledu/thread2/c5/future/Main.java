package com.cdeledu.thread2.c5.future;

public class Main {

	public static void main(String[] args) {
		Client client = new Client();
		Data data = client.request("name");
		//这里用一个sleep代替了对其它业务逻辑的处理，在处理这些逻辑的过程中，RealData被创建，从而充分利用等待时间
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("数据=" + data.getResult());
	}

}
