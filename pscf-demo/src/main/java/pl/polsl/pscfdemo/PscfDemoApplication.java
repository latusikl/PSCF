package pl.polsl.pscfdemo;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class PscfDemoApplication {



	public PscfDemoApplication() {
	}

	public static void main(String[] args) {
		ConfigurableApplicationContext context =
				new SpringApplicationBuilder(PscfDemoApplication.class)
						.web(WebApplicationType.SERVLET)
						.run(args);
	}
}