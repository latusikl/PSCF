package pl.polsl.pscfdemo.jobs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.polsl.pscfdemo.dto.OutputBrokerDto;
import pl.polsl.pscfdemo.mqtt.out.OutputSender;
import pl.polsl.pscfdemo.services.DataMemoryService;

@Component
@Slf4j
public class DataAnalysis {

	final
	DataMemoryService dataMemoryService;
	final OutputSender outputSender;

	public DataAnalysis(final DataMemoryService dataMemoryService, final OutputSender outputSender) {
		this.dataMemoryService = dataMemoryService;
		this.outputSender = outputSender;
	}

	//TODO implement data processing according to project spec.
	@Scheduled(fixedRate = 60000)
	public void processData(){
		OutputBrokerDto data = OutputBrokerDto.builder().accident(false).dose(10.0).emergencyStop(false).pumpOneState(true).pumpTwoState(false).build();
		log.info(dataMemoryService.getAllMeasurements().get(0).getTimestamp().toString());
		outputSender.sendToMqtt(data);

	}
}
