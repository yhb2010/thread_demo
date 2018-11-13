package com.cdeledu.thread3.c29event_driven.chat;

/**聊天事件
 * @author Administrator
 *
 */
public class UserChatEvent extends UserOnlineEvent {

	private final String message;
	
	public UserChatEvent(User user, String message) {
		super(user);
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
