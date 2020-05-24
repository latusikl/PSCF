package pl.polsl.pscfdemo;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class PscfDemoApplication {


	public PscfDemoApplication() {
	}

	public static void main(final String[] args) {
		final ConfigurableApplicationContext context =
				new SpringApplicationBuilder(PscfDemoApplication.class)
						.web(WebApplicationType.SERVLET)
						.run(args);
	}
}