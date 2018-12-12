package com.assignment.weatherforecast.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice(basePackages = "com.assignment.weatherforecast")
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class ExceptionControllerAdvice {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionControllerAdvice.class);

	@ExceptionHandler({ BaseException.class })
	@ResponseBody
	public GenericExceptionResponse baseExceptionHandler(final BaseException ex) {
		LOGGER.error("Exception: ", ex);
		return new GenericExceptionResponse(ex.getErrorCode(), ex.getMessage());
	}

	@ExceptionHandler(Throwable.class)
	@ResponseBody
	public GenericExceptionResponse exceptionHandler(final Throwable ex) {
		LOGGER.error("Exception: ", ex);
		if (ex.getCause() != null && BaseException.class.isAssignableFrom(ex.getCause().getClass())) {
			final BaseException baseException = (BaseException) ex.getCause();
			return new GenericExceptionResponse(baseException.getErrorCode(), baseException.getMessage());
		} else {
			return new GenericExceptionResponse(ex.getMessage());
		}
	}

}
