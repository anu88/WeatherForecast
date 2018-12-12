package com.assignment.weatherforecast.client;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Logger;

@Configuration
@EnableFeignClients(basePackages = "com.assignment.weatherforecast.client")
public class FeignClientConfiguration {

	@Bean
	Logger.Level feignLoggerLevel() {
		return Logger.Level.BASIC;
	}

}
