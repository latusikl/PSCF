package pl.polsl.demodatasender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableScheduling
public class DemoDataSenderApplication {

	final
	DataSender dataSender;

	public DemoDataSenderApplication(final DataSender dataSender) {
		this.dataSender = dataSender;
	}

	public static void main(String[] args) {

		SpringApplication.run(DemoDataSenderApplication.class, args);
	}
}
