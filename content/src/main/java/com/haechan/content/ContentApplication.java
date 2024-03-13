package com.haechan.content;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableDiscoveryClient
@EnableFeignClients
@EnableScheduling
@EnableBatchProcessing
@EnableJpaAuditing
@SpringBootApplication
public class ContentApplication {

	public static void main(String[] args) {
		SpringApplication.run(ContentApplication.class, args);
	}

}