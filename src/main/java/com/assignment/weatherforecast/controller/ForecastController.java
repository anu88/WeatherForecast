package com.assignment.weatherforecast.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.weatherforecast.dto.ForecastDto;
import com.assignment.weatherforecast.exception.BaseException;
import com.assignment.weatherforecast.service.ForecastService;

@RestController
@RequestMapping(value = "/data")
public class ForecastController {

	private ForecastService forecastService;

	@Autowired
	public ForecastController(final ForecastService forecastService) {
		this.forecastService = forecastService;
	}

	@GetMapping("/{cityName}")
	public ResponseEntity<List<ForecastDto>> getWeatherForecast(@PathVariable @NotBlank @Valid final String cityName)
			throws BaseException {
		return new ResponseEntity<List<ForecastDto>>(forecastService.getWeatherForecast(cityName), HttpStatus.OK);
	}
}
