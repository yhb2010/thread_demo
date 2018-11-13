package com.cdeledu.thread3.c29event_driven.chat;

import com.cdeledu.thread3.c29event_driven.async.AsyncChannel;
import com.cdeledu.thread3.c29event_driven.sync.Event;

public class UserOnlineEventChannel extends AsyncChannel {

	@Override
	protected void handle(Event message) {
		UserOnlineEvent event = (UserOnlineEvent)message;
		System.out.println("the user[" + event.getUser().getName() + "] is online.");
	}

}
