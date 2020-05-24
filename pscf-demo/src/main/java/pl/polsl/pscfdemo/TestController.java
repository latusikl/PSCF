package pl.polsl.pscfdemo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.polsl.pscfdemo.dto.OutputDto;
import pl.polsl.pscfdemo.mqtt.out.OutputSenderImpl;

@RestController
public class TestController {

	final
	OutputSenderImpl outputSenderImpl;

	public TestController(final OutputSenderImpl outputSenderImpl) {
		this.outputSenderImpl = outputSenderImpl;
	}

	@GetMapping("/hello")
	public void sendHello() {
		OutputDto dto = OutputDto.builder().name("Hello").status("Cloud here!").build();
		outputSenderImpl.sendToMqtt(dto);
	}
}
