package com.umutgldn.tickethub;

import com.umutgldn.tickethub.auth.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class TickethubApplication {

	public static void main(String[] args) {
		SpringApplication.run(TickethubApplication.class, args);
	}

}
