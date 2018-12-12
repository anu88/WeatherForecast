package com.assignment.weatherforecast.client.dto;

import java.util.List;

import lombok.Data;

@Data
public class OpenWeatherForecastResponseDto {
	
	private List<OpenWeatherForecastDto> list;

}
