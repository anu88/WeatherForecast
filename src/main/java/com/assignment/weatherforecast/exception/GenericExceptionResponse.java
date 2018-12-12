package com.assignment.weatherforecast.exception;

public class GenericExceptionResponse {

	private String errorCode;
	private String message;

	public GenericExceptionResponse() {
		// no args constructor
	}

	public GenericExceptionResponse(final String message) {
		this.setMessage(message);
	}

	public GenericExceptionResponse(final String errorCode, final String message) {
		this.setErrorCode(errorCode);
		this.setMessage(message);
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(final String errorCode) {
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(final String message) {
		this.message = message;
	}

}
