package pl.polsl.pscfdemo.mqtt.in;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;
import pl.polsl.pscfdemo.dto.InputBrokerDto;
import pl.polsl.pscfdemo.jobs.DataAnalysis;
import pl.polsl.pscfdemo.services.DataMemoryService;

/**
 * Handles input message from mqtt broker.
 * Subscribed topic is called pscf-in
 */
@Component
@Slf4j
public class InputMessageHandler implements MessageHandler {

    private final ObjectMapper objectMapper;
    private final DataMemoryService dataMemoryService;
    private final DataAnalysis dataAnalysis;

    public InputMessageHandler(final ObjectMapper objectMapper, final DataMemoryService dataMemoryService, final DataAnalysis dataAnalysis) {
        this.objectMapper = objectMapper;
        this.dataMemoryService = dataMemoryService;
        this.dataAnalysis = dataAnalysis;
    }

    /*
    In this method you are receiving message.
     */
    @Override
    public void handleMessage(final Message<?> message) throws MessagingException {
        log.info("New message received!");
        final String messageBody = (String) message.getPayload();
        try {
            InputBrokerDto data = objectMapper.readValue(messageBody, InputBrokerDto.class);
            dataAnalysis.checkForAccident(data);
            dataMemoryService.addMeasurement(data);
            log.info("Object JSON mapped to Input DTO.");
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            throw new MessagingException(e.getMessage());
        }
    }
}
