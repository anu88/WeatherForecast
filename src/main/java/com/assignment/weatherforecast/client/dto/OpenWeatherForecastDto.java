package com.assignment.weatherforecast.client.dto;

import lombok.Data;

@Data
public class OpenWeatherForecastDto {

	private String dt_txt;

	private OpenWeatherData main;
}
