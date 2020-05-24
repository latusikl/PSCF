package pl.polsl.demodatasender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DataSender{

	private final MessageChannel mqttOutboundChannel;
	private final ObjectMapper objectMapper;

	public DataSender(final MessageChannel mqttOutboundChannel) {
		this.mqttOutboundChannel = mqttOutboundChannel;
		this.objectMapper = new ObjectMapper();
	}


	public void sendToMqtt(final InputBrokerDto data) {
		Message<String> message = null;
		try {
			message = MessageBuilder.withPayload(objectMapper.writeValueAsString(data)).build();
		} catch (final JsonProcessingException e) {
			e.printStackTrace();
		}
		mqttOutboundChannel.send(message);
	}

	//TODO Random messages
	//One second
	@Scheduled(fixedRate = 1000)
	public void sendData() {
		InputBrokerDto data = InputBrokerDto.builder()
				.accident(false)
				.carbonFilter(true)
				.dose(2.2)
				.gravelFilter(true)
				.percentageOfChemicals(5.6)
				.phValue(10)
				.pumpOneState(true)
				.pumpTwoState(false)
				.reverseOsmosis(true)
				.temperature(10.2)
				.build();
		this.sendToMqtt(data);
		log.info("Sample data send!");
	}
}