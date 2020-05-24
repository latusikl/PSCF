package pl.polsl.pscfdemo.mqtt.in;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

/**
 * Handles input message from mqtt broker.
 * Subscribed topic is called pscf-in
 */
public class InputMessageHandler implements MessageHandler {

	/*
	In this method you are receiving message.
	 */
	@Override
	public void handleMessage(final Message<?> message) throws MessagingException {
		System.out.println(message);
	}
}
