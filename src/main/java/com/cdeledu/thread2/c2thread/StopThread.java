package com.cdeledu.thread2.c2thread;

import com.cdeledu.thread2.domain.User;

/**错误的停止线程的方式
 * @author DELL
 *
 */
public class StopThread {

	private static User u = new User(0, "0");

	public static class ChangeObject extends Thread {
		@Override
		public void run(){
			while(true){
				synchronized (u) {
					int v = (int)(System.currentTimeMillis()/1000);
					System.out.println(v);
					u.setId(v);
					try{
						Thread.sleep(100);
					}catch(InterruptedException e){
						e.printStackTrace();
					}
					u.setName(String.valueOf(v));
				}
				Thread.yield();
			}
		}
	}

	public static class ReadObject extends Thread {
		@Override
		public void run(){
			while(true){
				synchronized (u) {
					if(u.getId() != Integer.parseInt(u.getName())){
						System.out.println(u);
					}else{
						System.out.println("相等");
					}
				}
				Thread.yield();
			}
		}
	}

	public static void main(String[] args) throws InterruptedException {
		new ReadObject().start();
		while(true){
			Thread t = new ChangeObject();
			t.start();
			Thread.sleep(150);
			t.stop();
		}
	}

}
