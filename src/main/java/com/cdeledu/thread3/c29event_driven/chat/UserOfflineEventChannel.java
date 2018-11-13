package com.cdeledu.thread3.c29event_driven.chat;

import com.cdeledu.thread3.c29event_driven.async.AsyncChannel;
import com.cdeledu.thread3.c29event_driven.sync.Event;

public class UserOfflineEventChannel extends AsyncChannel {

	@Override
	protected void handle(Event message) {
		UserOfflineEvent event = (UserOfflineEvent)message;
		System.out.println("the user[" + event.getUser().getName() + "] is offline.");
	}

}
