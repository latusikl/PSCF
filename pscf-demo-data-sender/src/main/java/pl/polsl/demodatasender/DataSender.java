package pl.polsl.demodatasender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.Random;

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
	
	public static double generateRandomDoubleRange(double min, double max) {
		Random r = new Random();
		return min + r.nextDouble() * (max - min);
	}

	public static boolean generateRandomBoolean() {
		Random r = new Random();
		return r.nextBoolean();
	}

	//TODO Random messages
	//One second
	@Scheduled(fixedRate = 1000)
	public void sendData() {
		InputBrokerDto data = InputBrokerDto.builder()
				.accident(generateRandomBoolean())
				.carbonFilter(generateRandomBoolean())
				.dose(generateRandomDoubleRange(1,4))
				.gravelFilter(generateRandomBoolean())
				.percentageOfChemicals(generateRandomDoubleRange(0,10))
				.phValue(generateRandomDoubleRange(7,14))
				.pumpOneState(generateRandomBoolean())
				.pumpTwoState(generateRandomBoolean())
				.reverseOsmosis(generateRandomBoolean())
				.temperature(generateRandomDoubleRange(5,40))
				.build();
		this.sendToMqtt(data);
		log.info("Sample data send!");
	}

}