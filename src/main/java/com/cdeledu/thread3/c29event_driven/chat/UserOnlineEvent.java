package com.cdeledu.thread3.c29event_driven.chat;

import com.cdeledu.thread3.c29event_driven.sync.Event;

/**上线事件
 * @author Administrator
 *
 */
public class UserOnlineEvent extends Event {
	
	private final User user;
	
	public UserOnlineEvent(User user){
		this.user = user;
	}
	
	public User getUser(){
		return user;
	}

}
