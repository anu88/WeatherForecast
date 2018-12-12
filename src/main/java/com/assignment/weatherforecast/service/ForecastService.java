package com.assignment.weatherforecast.service;

import java.util.List;

import com.assignment.weatherforecast.dto.ForecastDto;

public interface ForecastService {

	List<ForecastDto> getWeatherForecast(String cityName);

}
