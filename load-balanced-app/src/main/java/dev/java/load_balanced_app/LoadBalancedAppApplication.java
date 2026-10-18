package dev.java.load_balanced_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class LoadBalancedAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoadBalancedAppApplication.class, args);
	}

}
