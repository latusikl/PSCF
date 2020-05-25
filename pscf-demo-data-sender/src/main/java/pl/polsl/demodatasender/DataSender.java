package pl.polsl.demodatasender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.decimal4j.util.DoubleRounder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.Random;

@Component
@Slf4j
public class DataSender{

	private InputBrokerDto lastSentData;

	private final MessageChannel mqttOutboundChannel;
	private final ObjectMapper objectMapper;

	public DataSender(final MessageChannel mqttOutboundChannel) {
		this.mqttOutboundChannel = mqttOutboundChannel;
		this.objectMapper = new ObjectMapper();
		lastSentData = InputBrokerDto.builder()
						.accident(false)
						.carbonFilter(true)
						.dose(5.1)
						.gravelFilter(true)
						.percentageOfChemicals(1.0)
						.phValue(10.0)
						.pumpOneState(true)
						.pumpTwoState(true)
						.reverseOsmosis(true)
						.temperature(20.0)
						.build();
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

	public static boolean generateChosenBooleanWithChosenPercentProbability(boolean chosenBoolean, int chosenPercent) {
		Random r = new Random();
		int value = r.nextInt(100/chosenPercent);
		if(chosenBoolean)
			return value == 0;
		return value != 0;
	}

	//TODO Random messages
	//One second
	@Scheduled(fixedRate = 1000)
	public void sendData() {

		double newPhValue = this.lastSentData.phValue+(this.lastSentData.dose-5)/100 * this.lastSentData.phValue;

		InputBrokerDto data = InputBrokerDto.builder()
				.accident(generateChosenBooleanWithChosenPercentProbability(true, 1))
				.carbonFilter(generateChosenBooleanWithChosenPercentProbability(false, 2))
				.dose(DoubleRounder.round(this.lastSentData.dose+generateRandomDoubleRange(0,0.1), 3))
				.gravelFilter(generateChosenBooleanWithChosenPercentProbability(false, 2))
				.percentageOfChemicals(DoubleRounder.round(this.lastSentData.percentageOfChemicals+generateRandomDoubleRange(0,0.1), 3))
				.phValue(DoubleRounder.round(newPhValue, 3))
				.pumpOneState(generateChosenBooleanWithChosenPercentProbability(false, 1))
				.pumpTwoState(generateChosenBooleanWithChosenPercentProbability(false, 2))
				.reverseOsmosis(generateChosenBooleanWithChosenPercentProbability(false, 2))
				.temperature(DoubleRounder.round(this.lastSentData.temperature+generateRandomDoubleRange(0,0.1), 3))
				.build();
		this.sendToMqtt(data);
		this.lastSentData = data;
		log.info("Sample data send!");
	}

}