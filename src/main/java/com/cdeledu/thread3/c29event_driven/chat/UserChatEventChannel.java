package com.cdeledu.thread3.c29event_driven.chat;

import com.cdeledu.thread3.c29event_driven.async.AsyncChannel;
import com.cdeledu.thread3.c29event_driven.sync.Event;

public class UserChatEventChannel extends AsyncChannel {

	@Override
	protected void handle(Event message) {
		UserChatEvent event = (UserChatEvent)message;
		System.out.println("the user[" + event.getUser().getName() + "] say:" + event.getMessage());
	}

}
