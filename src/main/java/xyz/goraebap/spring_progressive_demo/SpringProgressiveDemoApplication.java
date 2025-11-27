package xyz.goraebap.spring_progressive_demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SpringProgressiveDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringProgressiveDemoApplication.class, args);
	}

}
