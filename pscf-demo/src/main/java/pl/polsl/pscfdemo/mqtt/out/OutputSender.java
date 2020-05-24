package pl.polsl.pscfdemo.mqtt.out;

import pl.polsl.pscfdemo.dto.OutputBrokerDto;

public interface OutputSender {

	public void sendToMqtt(final String data);

	public void sendToMqtt(final OutputBrokerDto data);
}
