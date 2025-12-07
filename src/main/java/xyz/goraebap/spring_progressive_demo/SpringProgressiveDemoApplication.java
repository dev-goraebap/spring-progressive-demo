package xyz.goraebap.spring_progressive_demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableAsync
public class SpringProgressiveDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringProgressiveDemoApplication.class, args);
	}

}
