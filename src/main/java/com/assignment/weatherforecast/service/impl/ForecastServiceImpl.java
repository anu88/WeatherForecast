package com.assignment.weatherforecast.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.assignment.weatherforecast.client.OpenWeatherRestClient;
import com.assignment.weatherforecast.client.dto.OpenWeatherForecastDto;
import com.assignment.weatherforecast.client.dto.OpenWeatherForecastResponseDto;
import com.assignment.weatherforecast.constant.ApplicationConstant;
import com.assignment.weatherforecast.dto.ForecastDto;
import com.assignment.weatherforecast.exception.ClientException;
import com.assignment.weatherforecast.exception.TechnicalException;
import com.assignment.weatherforecast.service.ForecastService;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ForecastServiceImpl implements ForecastService {

	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter
			.ofPattern(ApplicationConstant.OPEN_WEATHER_API_DATE_FORMAT);

	@Value("${openweather.authentication.key}")
	private String authKey;

	@Value("${openweather.api.forecast.tempUnit:metric}")
	private String tempUnit;

	private OpenWeatherRestClient openWeatherClient;

	@Autowired
	public ForecastServiceImpl(final OpenWeatherRestClient openWeatherClient) {
		this.openWeatherClient = openWeatherClient;
	}

	@Override
	public List<ForecastDto> getWeatherForecast(final String cityName) {
		try {
			final OpenWeatherForecastResponseDto response = openWeatherClient.getWeatherForecast(cityName, tempUnit,
					authKey);
			log.debug("Open weather API response for city {} is: {}", cityName, response);

			final List<List<OpenWeatherForecastDto>> filteredResponse = response.getList().stream()
					.filter(openWeatherDto -> isForecastDateCriteriaMatched(
							LocalDateTime.parse(openWeatherDto.getDt_txt(), DATE_FORMATTER)))
					.collect(groupCollector(4));

			log.debug("Open weather API filtered and grouped response: {}", filteredResponse);

			final Map<LocalDate, List<ForecastDto>> forecastDtosMap = filteredResponse.stream()
					.map(groupedWeatherData -> calculateForecast(groupedWeatherData))
					.collect(Collectors.groupingBy(ForecastDto::getDate));

			log.debug("Weather forecast grouped by day : {}", forecastDtosMap);

			return forecastDtosMap.entrySet().stream()
					.map(entry -> getMergedDayAndNightWeatherData(entry.getKey(), entry.getValue()))
					.collect(Collectors.toList());
		} catch (DateTimeParseException ex) {
			log.error("Exception while parsing the date", ex.getCause());
			throw new TechnicalException("error.parse.date", ex.getMessage(), ex.getCause());
		} catch (FeignException ex) {
			log.error("Exception while interacting with Open weather API", ex.getCause());
			throw new ClientException("error.feign.client", ex.getMessage(), ex.getCause());
		}
	}

	private boolean isForecastDateCriteriaMatched(final LocalDateTime forecastTime) {
		final LocalDateTime startDateTime = LocalDateTime.now(Clock.systemUTC()).truncatedTo(ChronoUnit.DAYS)
				.plusDays(1).plusHours(6);
		final LocalDateTime endDateTime = startDateTime.plusDays(ApplicationConstant.FORECAST_DAYS);
		if ((forecastTime.isEqual(startDateTime) || forecastTime.isAfter(startDateTime))
				&& forecastTime.isBefore(endDateTime)) {
			return true;
		}
		return false;
	}

	private ForecastDto calculateForecast(final List<OpenWeatherForecastDto> groupedWeatherData) {
		final LocalDate date = LocalDateTime.parse(groupedWeatherData.get(0).getDt_txt(), DATE_FORMATTER).toLocalDate();
		final Double tempAvg = groupedWeatherData.stream().mapToDouble(data -> data.getMain().getTemp()).average()
				.getAsDouble();
		final Double pressureAvg = groupedWeatherData.stream().mapToDouble(data -> data.getMain().getPressure())
				.average().getAsDouble();
		final ForecastDto forecastDto = new ForecastDto(date, tempAvg, pressureAvg);
		log.debug("Calculated weather forecast for {} is {}", groupedWeatherData, forecastDto);
		return forecastDto;
	}

	private ForecastDto getMergedDayAndNightWeatherData(final LocalDate date,
			final List<ForecastDto> weatherDtosToBeMerged) {
		final Double tempAvg = weatherDtosToBeMerged.stream().mapToDouble(dto -> dto.getAverageTemperature()).average()
				.getAsDouble();
		final Double pressureAvg = weatherDtosToBeMerged.stream().mapToDouble(dto -> dto.getPressure()).average()
				.getAsDouble();
		final ForecastDto forecastDto = new ForecastDto(date,
				roundOf(weatherDtosToBeMerged.get(0).getAverageTemperature()),
				roundOf(weatherDtosToBeMerged.get(1).getAverageTemperature()), roundOf(tempAvg), roundOf(pressureAvg));
		log.debug("Merged weather forecast for {} is {}", weatherDtosToBeMerged, forecastDto);
		return forecastDto;
	}

	private Collector<OpenWeatherForecastDto, List<List<OpenWeatherForecastDto>>, List<List<OpenWeatherForecastDto>>> groupCollector(
			int groupSize) {
		return Collector.of(ArrayList<List<OpenWeatherForecastDto>>::new, (list, value) -> {
			List<OpenWeatherForecastDto> group = (list.isEmpty() ? null : list.get(list.size() - 1));
			if (group == null || group.size() == groupSize)
				list.add(group = new ArrayList<>(groupSize));
			group.add(value);
		}, (r1, r2) -> {
			throw new UnsupportedOperationException("Parallel processing not supported");
		});
	}

	private Double roundOf(final Double number) {
		BigDecimal bd = new BigDecimal(number);
		bd = bd.setScale(1, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

}
