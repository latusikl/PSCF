package pl.polsl.demodatasender;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@IntegrationComponentScan
@Configuration
public class MqttConfiguration {



	private static final String TOPIC = "pscf-in";
	//========================================================OUTBOUND

	//Client config
	@Bean
	public MqttPahoClientFactory mqttClientFactory() {
		final DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
		final MqttConnectOptions options = new MqttConnectOptions();
		options.setServerURIs(new String[]{"tcp://localhost:1883",});
		factory.setConnectionOptions(options);
		return factory;
	}


	//Outbound chanel config
	@Bean
	@ServiceActivator(inputChannel = "mqttOutboundChannel")
	public MessageHandler mqttOutbound() {
		final MqttPahoMessageHandler messageHandler =
				new MqttPahoMessageHandler("Demo sender", mqttClientFactory());
		messageHandler.setAsync(true);
		messageHandler.setDefaultTopic(TOPIC);
		return messageHandler;
	}

	//Outbound chanel to broker
	@Bean
	public MessageChannel mqttOutboundChannel() {
		return new DirectChannel();
	}
}

