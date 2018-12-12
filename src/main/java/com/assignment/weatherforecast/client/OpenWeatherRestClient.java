package com.assignment.weatherforecast.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.assignment.weatherforecast.client.dto.OpenWeatherForecastResponseDto;

@FeignClient(name = "openWeatherfeignClient", url = "${openweather.api.forecast.url:api.openweathermap.org/data/2.5/forecast}")
public interface OpenWeatherRestClient {

	@GetMapping
	public OpenWeatherForecastResponseDto getWeatherForecast(@RequestParam("q") String cityName,
			@RequestParam("units") String tempUnit, @RequestParam("APPID") String authKey);
}
