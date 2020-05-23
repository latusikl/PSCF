package pl.polsl.pscfdemo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.polsl.pscfdemo.dto.OutputDto;

@RestController
public class TestController {

	final
	OutputGateway outputGateway;

	public TestController(final OutputGateway outputGateway) {
		this.outputGateway = outputGateway;
	}

	@GetMapping("/hello")
	public void sendHello() {
		OutputDto dto = OutputDto.builder().name("Hello").status("Cloud here!").build();
		outputGateway.sendToMqtt(dto);
	}
}
