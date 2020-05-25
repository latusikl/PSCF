package pl.polsl.pscfdemo.jobs;

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

        final InputBrokerDto lastSent = dataMemoryService.getAllMeasurements().getLast();
        log.info("Processing data beginning at {} and ending at {}.",
                dataMemoryService.getAllMeasurements().getFirst().getTimestamp(), lastSent.getTimestamp());
        if (this.getAvgTemp() > 35.0 || this.getAvgTemp() < 10.0) {
            log.info("TEMPERATURE WARNING!  Current average temperature: {}", this.getAvgTemp());
            emergency();
            return;
        }

        if (!lastSent.getPumpOneState() && !lastSent.getPumpTwoState()) {
            log.info("WARNING! Both pumps are turned off.");
            emergency();
            return;
        }

        if (!lastSent.getPumpOneState() && lastSent.getPumpTwoState() || lastSent.getPumpOneState() && !lastSent.getPumpTwoState()) {
            log.info("WARNING! One pump is turned off.");
        }

        final OutputBrokerDto data = OutputBrokerDto.builder()
                .accident(false)
                .emergencyStop(false)
                .pumpOneState(lastSent.getPumpOneState())
                .pumpTwoState(lastSent.getPumpTwoState())
                .build();

        if (this.getAvgPercentage() < 0.5 || this.getAvgPhValue() < 4.0) {
            final Double newDose = lastSent.getDose() + 0.5;
            data.setDose(newDose);
            log.info("Dose is too low. Changing from {} to {}.", lastSent.getDose(), newDose);
        } else if (this.getAvgPercentage() > 10.0 || this.getAvgPhValue() > 9.0) {
            Double newDose = lastSent.getDose() - 0.5;
            if (newDose < 0) {
                newDose = 0.0;
            }
            data.setDose(newDose);
            log.info("Dose is too high. Changing from {} to {}.", lastSent.getDose(), newDose);
        }

        outputSender.sendToMqtt(data);
    }

    public void checkForAccident(final InputBrokerDto data) {
        if (data.getAccident() || !data.getCarbonFilter() || !data.getGravelFilter() || !data.getReverseOsmosis()) {
            if (data.getAccident()) {
                log.info("WARNING! ACCIDENT! Date: {}", data.getTimestamp());
            }
            if (!data.getCarbonFilter()) {
                log.info("WARNING! Carbon filter needs to be replaced! Date: {}", data.getTimestamp());
            }
            if (!data.getGravelFilter()) {
                log.info("WARNING! Gravel filter needs to be replaced! Date: {}", data.getTimestamp());
            }
            if (!data.getReverseOsmosis()) {
                log.info("WARNING! Reverse osmosis is turned off! Date: {}", data.getTimestamp());
            }
            this.emergency();
        }
    }

    private void emergency() {
        log.info("Emergency signal. Turning system off.");
        OutputBrokerDto emergencyData = OutputBrokerDto.builder()
                .accident(true)
                .emergencyStop(true)
                .pumpTwoState(false)
                .pumpOneState(false)
                .dose(0.0)
                .build();
        outputSender.sendToMqtt(emergencyData);
    }

    private Double getAvgPhValue() {
        final List<InputBrokerDto> dataList = dataMemoryService.getAllMeasurements();
        if (dataList.isEmpty()) {
            return 0.0;
        }
        return dataList.stream().mapToDouble(data -> data.getPhValue()).sum() / dataList.size();
    }

    private Double getAvgPercentage() {
        final List<InputBrokerDto> dataList = dataMemoryService.getAllMeasurements();
        if (dataList.isEmpty()) {
            return 0.0;
        }
        return dataList.stream().mapToDouble(data -> data.getPercentageOfChemicals()).sum() / dataList.size();
    }

    private Double getAvgTemp() {
        final List<InputBrokerDto> dataList = dataMemoryService.getAllMeasurements();
        if (dataList.isEmpty()) {
            return 0.0;
        }
        return dataList.stream().mapToDouble(data -> data.getTemperature()).sum() / dataList.size();
    }

}
