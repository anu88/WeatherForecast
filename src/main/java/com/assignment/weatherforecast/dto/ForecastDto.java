package com.assignment.weatherforecast.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ForecastDto {

	private LocalDate date;

	private Double dayTemperature;

	private Double nightTemperature;

	private Double averageTemperature;

	private Double pressure;

	public ForecastDto(final LocalDate date, final Double averageTemperature, final Double averagePressure) {
		this.date = date;
		this.averageTemperature = averageTemperature;
		this.pressure = averagePressure;
	}
}
