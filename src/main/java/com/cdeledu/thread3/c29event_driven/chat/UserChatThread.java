package com.cdeledu.thread3.c29event_driven.chat;

import java.util.concurrent.TimeUnit;

import com.cdeledu.thread3.c29event_driven.async.AsyncEventDispatcher;

/**代表聊天室参与者的User线程
 * @author Administrator
 *
 */
public class UserChatThread extends Thread {

	private final User user;
	private final AsyncEventDispatcher dispatcher;
	
	public UserChatThread(User user, AsyncEventDispatcher dispatcher) {
		super(user.getName());
		this.user = user;
		this.dispatcher = dispatcher;
	}
	
	@Override
	public void run(){
		try{
			dispatcher.dispatch(new UserOnlineEvent(user));
			for(int i=0; i<5; i++){
				dispatcher.dispatch(new UserChatEvent(user, Thread.currentThread().getName() + "-hello-" + i));
				TimeUnit.SECONDS.sleep(1);
			}
		}catch(InterruptedException e){
			e.printStackTrace();
		}finally{
			dispatcher.dispatch(new UserOfflineEvent(user));
		}
	}
	
}
