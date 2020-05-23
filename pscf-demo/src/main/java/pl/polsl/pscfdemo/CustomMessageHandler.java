package pl.polsl.pscfdemo;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

public class CustomMessageHandler implements MessageHandler {
	@Override
	public void handleMessage(final Message<?> message) throws MessagingException {
		System.out.println(message);
	}
}
