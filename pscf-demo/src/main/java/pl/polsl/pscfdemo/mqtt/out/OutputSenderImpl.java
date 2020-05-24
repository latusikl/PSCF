package pl.polsl.pscfdemo.mqtt.out;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import pl.polsl.pscfdemo.dto.OutputDto;

@Component
public class OutputSenderImpl implements OutputSender {

	private final MessageChannel messageChannelOut;
	private final ObjectMapper objectMapper;

	public OutputSenderImpl(final @Qualifier("mqttOutboundChannel") MessageChannel messageChannelOut, final ObjectMapper objectMapper) {
		this.messageChannelOut = messageChannelOut;
		this.objectMapper = objectMapper;
	}

	@Override
	public void sendToMqtt(final String data) {
		final Message<String> mes = MessageBuilder.withPayload("Hello from Spring!").build();
		messageChannelOut.send(mes);
	}

	@Override
	public void sendToMqtt(final OutputDto data) {
		Message<String> message = null;
		try {
			message = MessageBuilder.withPayload(objectMapper.writeValueAsString(data)).build();
		} catch (final JsonProcessingException e) {
			e.printStackTrace();
		}
		messageChannelOut.send(message);
	}


}
