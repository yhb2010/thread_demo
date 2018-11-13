package com.cdeledu.thread3.c29event_driven.chat;

import com.cdeledu.thread3.c29event_driven.async.AsyncEventDispatcher;

public class Test {
	
	public static void main(String[] args) {
		final AsyncEventDispatcher dispatcher = new AsyncEventDispatcher();
		dispatcher.registerChannel(UserOnlineEvent.class, new UserOnlineEventChannel());
		dispatcher.registerChannel(UserOfflineEvent.class, new UserOfflineEventChannel());
		dispatcher.registerChannel(UserChatEvent.class, new UserChatEventChannel());
		new UserChatThread(new User("zsl"), dispatcher).start();
		new UserChatThread(new User("cl"), dispatcher).start();
		new UserChatThread(new User("ll"), dispatcher).start();
	}

}
