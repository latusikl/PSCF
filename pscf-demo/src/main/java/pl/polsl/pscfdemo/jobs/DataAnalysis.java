package pl.polsl.pscfdemo.jobs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.polsl.pscfdemo.dto.InputBrokerDto;
import pl.polsl.pscfdemo.dto.OutputBrokerDto;
import pl.polsl.pscfdemo.mqtt.out.OutputSender;
import pl.polsl.pscfdemo.services.DataMemoryService;

@Component
@Slf4j
public class DataAnalysis {

    private final DataMemoryService dataMemoryService;
    private final OutputSender outputSender;

    public DataAnalysis(final DataMemoryService dataMemoryService, final OutputSender outputSender) {
        this.dataMemoryService = dataMemoryService;
        this.outputSender = outputSender;
    }

    @Scheduled(fixedRate = 60000)
    public void processData() {
        if (dataMemoryService.getAllMeasurements().size() > 0) {
            log.info(dataMemoryService.getAllMeasurements().get(0).getTimestamp().toString());

            OutputBrokerDto data = OutputBrokerDto.builder()
                    .accident(false)
                    .dose(10.0).emergencyStop(false)
                    .pumpOneState(true)
                    .pumpTwoState(false)
                    .build();
            outputSender.sendToMqtt(data);
        }
    }

    public void checkForAccident(final InputBrokerDto data) {
        if (data.getAccident() || !data.getCarbonFilter() || !data.getGravelFilter()) {
            OutputBrokerDto emergencyData = OutputBrokerDto.builder().accident(true).emergencyStop(true).build();
            outputSender.sendToMqtt(emergencyData);
            if (!data.getAccident()) {
                log.info("WARNING!!! ACCIDENT! Date: " + data.getTimestamp());
            }
            if (!data.getCarbonFilter()) {
                log.info("WARNING!!! Carbon filter needs to be replaced! Date: " + data.getTimestamp());
            }
            if (!data.getGravelFilter()) {
                log.info("WARNING!!! Gravel filter needs to be replaced! Date: " + data.getTimestamp());
            }
        }
    }
}
