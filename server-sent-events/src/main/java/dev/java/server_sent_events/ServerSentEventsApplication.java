package dev.java.server_sent_events;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class ServerSentEventsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerSentEventsApplication.class, args);
	}

}
