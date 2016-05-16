package com.guohuai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ComponentScan(basePackages = { "com.guohuai", "com.ghg" })
@EnableAutoConfiguration
@SpringBootApplication
@EnableConfigurationProperties
@EnableScheduling
public class AssetPlatformBoot {

	public static void main(String[] args) {
		for (String arg : args) {
			System.out.println(arg);
		}
		SpringApplication.run(AssetPlatformBoot.class, args);
	}

}
