package pl.polsl.pscfdemo.jobs;

import com.google.common.collect.Iterables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.polsl.pscfdemo.dto.InputBrokerDto;
import pl.polsl.pscfdemo.dto.OutputBrokerDto;
import pl.polsl.pscfdemo.mqtt.out.OutputSender;
import pl.polsl.pscfdemo.services.DataMemoryService;

import java.util.List;

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
        if (dataMemoryService.getAllMeasurements().isEmpty()) {
            return;
        }

        final InputBrokerDto lastSent = Iterables.getLast(dataMemoryService.getAllMeasurements());
        log.info("Processing data beginning at {} and ending at {}.",
                dataMemoryService.getAllMeasurements().iterator().next().getTimestamp(), lastSent.getTimestamp());
        if (this.getAvgTemp() > 35.0 || this.getAvgTemp() < 10.0) {
            log.info("TEMPERATURE WARNING!!!  Current average temperature: {}", this.getAvgTemp());
            stopPumps();
            return;
        }

        final OutputBrokerDto data = new OutputBrokerDto();
        if (this.getAvgPercentage() < 0.5 || this.getAvgPhValue() < 4.0) {
            data.setDose(lastSent.getDose() + 0.5);
        } else if (this.getAvgPercentage() > 10.0 || this.getAvgPhValue() > 9.0) {
            double newDose = lastSent.getDose() - 0.5;
            if (newDose > 0) {
                data.setDose(newDose);
            } else {
                data.setDose(0.0);
            }
        }
        
        outputSender.sendToMqtt(data);
    }

    public void checkForAccident(final InputBrokerDto data) {
        if (data.getAccident() || !data.getCarbonFilter() || !data.getGravelFilter() || !data.getReverseOsmosis()) {
            this.stopPumps();
            if (data.getAccident()) {
                log.info("WARNING!!! ACCIDENT! Date: {}", data.getTimestamp());
            }
            if (!data.getCarbonFilter()) {
                log.info("WARNING!!! Carbon filter needs to be replaced! Date: {}", data.getTimestamp());
            }
            if (!data.getGravelFilter()) {
                log.info("WARNING!!! Gravel filter needs to be replaced! Date: {}", data.getTimestamp());
            }
            if (!data.getGravelFilter()) {
                log.info("WARNING!!! Reverse osmosis is turned off! Date: {}", data.getTimestamp());
            }
        }
    }

    private void stopPumps() {
        log.info("Stopping pumps");
        OutputBrokerDto emergencyData = OutputBrokerDto.builder().accident(true).emergencyStop(true).build();
        outputSender.sendToMqtt(emergencyData);
    }

    private Double getAvgPhValue() {
        final List<InputBrokerDto> dataList = dataMemoryService.getAllMeasurements();
        if (dataList.isEmpty()) {
            return 0.0;
        }
        return dataList.stream().mapToDouble(data -> data.getPhValue()).sum();
    }

    private Double getAvgPercentage() {
        final List<InputBrokerDto> dataList = dataMemoryService.getAllMeasurements();
        if (dataList.isEmpty()) {
            return 0.0;
        }
        return dataList.stream().mapToDouble(data -> data.getPercentageOfChemicals()).sum();
    }

    private Double getAvgTemp() {
        final List<InputBrokerDto> dataList = dataMemoryService.getAllMeasurements();
        if (dataList.isEmpty()) {
            return 0.0;
        }
        return dataList.stream().mapToDouble(data -> data.getTemperature()).sum();
    }

}
