package pl.polsl.pscfdemo;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
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

@IntegrationComponentScan
@Configuration
public class MqttConfiguration {

	@Value("${output.topic}")
	String outputTopic;

	//========================================================OUTBOUND

	//Client config
	@Bean
	public MqttPahoClientFactory mqttClientFactory() {
		DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
		MqttConnectOptions options = new MqttConnectOptions();
		options.setServerURIs(new String[]{"tcp://localhost:1883",});
		factory.setConnectionOptions(options);
		return factory;
	}


	//Input chanel config
	@Bean
	@ServiceActivator(inputChannel = "mqttOutboundChannel")
	public MessageHandler mqttOutbound() {
		MqttPahoMessageHandler messageHandler =
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

	@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
	public interface MyGateway {

		void sendToMqtt(String data);

	}

	//========================================================OUTBOUND
	//========================================================INBOUND


	@Bean
	@Qualifier("mqttInputChannel")
	public MessageChannel mqttInputChannel() {
		return new DirectChannel();
	}

	@Bean
	public MessageProducer inbound() {
		MqttPahoMessageDrivenChannelAdapter adapter =
				new MqttPahoMessageDrivenChannelAdapter("tcp://localhost:1883", "Cloud Listen Client",
						"pscf-in");
		adapter.setCompletionTimeout(5000);
		adapter.setConverter(new DefaultPahoMessageConverter());
		adapter.setQos(1);
		adapter.setOutputChannel(mqttInputChannel());
		return adapter;
	}

	@Bean
	@ServiceActivator(inputChannel = "mqttInputChannel")
	public MessageHandler handler() {
		return new CustomMessageHandler();
	}
	//========================================================INBOUND
}
