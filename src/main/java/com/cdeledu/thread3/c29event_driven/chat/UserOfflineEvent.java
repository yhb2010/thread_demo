package com.cdeledu.thread3.c29event_driven.chat;

/**下线事件
 * @author Administrator
 *
 */
public class UserOfflineEvent extends UserOnlineEvent {

	public UserOfflineEvent(User user) {
		super(user);
	}

}
