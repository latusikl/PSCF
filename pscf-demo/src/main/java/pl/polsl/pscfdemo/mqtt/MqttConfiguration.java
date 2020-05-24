package pl.polsl.pscfdemo.mqtt;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import pl.polsl.pscfdemo.mqtt.in.InputMessageHandler;


/**
 * Configuration class for inbound and outbound connection with broker.
 * Connects to Mqtt broker (Mosquito or other) on standard port 1883.
 * Topics are defined as properties in .properties file.
 * Configuration based on: https://docs.spring.io/spring-integration/docs/5.2.1.RELEASE/reference/html/mqtt.html
 * For messaging inbound and outbound MessageChannels beans are created. As type of bean is the same. Use qualifier during DI.
 */
@IntegrationComponentScan
@Configuration
public class MqttConfiguration {

	@Value("${output.topic}")
	String outputTopic;

	@Value("${input.topic}")
	String inputTopic;

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
				new MqttPahoMessageHandler("Cloud Send Client", mqttClientFactory());
		messageHandler.setAsync(true);
		messageHandler.setDefaultTopic(outputTopic);
		return messageHandler;
	}

	//Outbound chanel to broker
	@Bean
	@Qualifier("mqttOutboundChannel")
	public MessageChannel mqttOutboundChannel() {
		return new DirectChannel();
	}

	//========================================================INBOUND


	@Bean
	@Qualifier("mqttInputChannel")
	public MessageChannel mqttInputChannel() {
		return new DirectChannel();
	}

	@Bean
	public MessageProducer inbound() {
		final MqttPahoMessageDrivenChannelAdapter adapter =
				new MqttPahoMessageDrivenChannelAdapter("tcp://localhost:1883", "Cloud Listen Client",
						inputTopic);
		adapter.setCompletionTimeout(5000);
		adapter.setConverter(new DefaultPahoMessageConverter());
		adapter.setQos(1);
		adapter.setOutputChannel(mqttInputChannel());
		return adapter;
	}

	@Bean
	@ServiceActivator(inputChannel = "mqttInputChannel")
	public MessageHandler handler(final InputMessageHandler inputMessageHandler) {
		return inputMessageHandler;
	}
	//========================================================INBOUND
}
