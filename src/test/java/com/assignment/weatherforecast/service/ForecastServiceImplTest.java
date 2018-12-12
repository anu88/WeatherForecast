package com.assignment.weatherforecast.service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.assignment.weatherforecast.client.OpenWeatherRestClient;
import com.assignment.weatherforecast.client.dto.OpenWeatherData;
import com.assignment.weatherforecast.client.dto.OpenWeatherForecastDto;
import com.assignment.weatherforecast.client.dto.OpenWeatherForecastResponseDto;
import com.assignment.weatherforecast.constant.ApplicationConstant;
import com.assignment.weatherforecast.dto.ForecastDto;
import com.assignment.weatherforecast.exception.ClientException;
import com.assignment.weatherforecast.exception.TechnicalException;
import com.assignment.weatherforecast.service.impl.ForecastServiceImpl;

import feign.FeignException;

public class ForecastServiceImplTest {

	@InjectMocks
	private ForecastServiceImpl target;

	@Mock
	private OpenWeatherRestClient restClient;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		ReflectionTestUtils.setField(target, "authKey", "dummyKey");
		ReflectionTestUtils.setField(target, "tempUnit", "celcius");
	}

	@Test
	public void getWeatherForecast_ValidApiResponse_ExpectOneDayWeatherForecast() throws Exception {
		Mockito.when(restClient.getWeatherForecast("berlin", "celcius", "dummyKey"))
				.thenReturn(getOpenWeatherForecastResponse());

		List<ForecastDto> forecastDtos = target.getWeatherForecast("berlin");

		Mockito.verify(restClient, Mockito.times(1)).getWeatherForecast("berlin", "celcius", "dummyKey");
		Assert.assertEquals(1, forecastDtos.size());
		Assert.assertEquals(Double.valueOf(45), forecastDtos.get(0).getAverageTemperature());
		Assert.assertEquals(Double.valueOf(25), forecastDtos.get(0).getDayTemperature());
		Assert.assertEquals(Double.valueOf(65), forecastDtos.get(0).getNightTemperature());
		Assert.assertEquals(Double.valueOf(4500), forecastDtos.get(0).getPressure());
		Assert.assertEquals(LocalDate.now(Clock.systemUTC()).plusDays(1), forecastDtos.get(0).getDate());
	}

	@Test(expected = ClientException.class)
	public void getWeatherForecast_OpenWeatherApiError_ExceptionThrown() throws Exception {
		Mockito.doThrow(FeignException.class).when(restClient).getWeatherForecast("berlin", "celcius", "dummyKey");
		try {
			target.getWeatherForecast("berlin");
		} catch (Throwable ex) {
			Assert.assertEquals(true, ex instanceof ClientException);
			Assert.assertEquals("error.feign.client", ((ClientException) ex).getErrorCode());
			Mockito.verify(restClient, Mockito.times(1)).getWeatherForecast("berlin", "celcius", "dummyKey");
			throw ex;
		}
	}

	@Test(expected = TechnicalException.class)
	public void getWeatherForecast_InvalidDateFormatInApiResponse_ExceptionThrown() throws Exception {
		OpenWeatherForecastResponseDto response = getOpenWeatherForecastResponse();
		response.getList().get(0).setDt_txt("12-12-2018 09:00:00");
		Mockito.when(restClient.getWeatherForecast("berlin", "celcius", "dummyKey")).thenReturn(response);
		try {
			target.getWeatherForecast("berlin");
		} catch (Throwable ex) {
			Assert.assertEquals(true, ex instanceof TechnicalException);
			Assert.assertEquals("error.parse.date", ((TechnicalException) ex).getErrorCode());
			Mockito.verify(restClient, Mockito.times(1)).getWeatherForecast("berlin", "celcius", "dummyKey");
			throw ex;
		}
	}

	private OpenWeatherForecastResponseDto getOpenWeatherForecastResponse() {

		final LocalDateTime date = LocalDateTime.now(Clock.systemUTC()).truncatedTo(ChronoUnit.DAYS).plusDays(1)
				.plusHours(3);
		OpenWeatherForecastResponseDto response = new OpenWeatherForecastResponseDto();
		List<OpenWeatherForecastDto> list = IntStream.range(1, 9)
				.mapToObj(i -> getOpenWeatherForecastDto(1000d * i, 10d * i, date.plusHours(3 * i)))
				.collect(Collectors.toList());
		response.setList(list);
		return response;
	}

	private OpenWeatherForecastDto getOpenWeatherForecastDto(Double pressure, Double temp, LocalDateTime date) {
		OpenWeatherForecastDto forecast = new OpenWeatherForecastDto();
		OpenWeatherData data = new OpenWeatherData();
		data.setPressure(pressure);
		data.setTemp(temp);
		forecast.setMain(data);
		forecast.setDt_txt(date.format(DateTimeFormatter.ofPattern(ApplicationConstant.OPEN_WEATHER_API_DATE_FORMAT)));
		return forecast;
	}

}
