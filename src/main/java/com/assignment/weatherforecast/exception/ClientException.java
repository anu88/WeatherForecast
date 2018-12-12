package com.assignment.weatherforecast.exception;

public class ClientException extends BaseException {

	private static final long serialVersionUID = -939152134977915215L;

	public ClientException() {
		super();
	}

	public ClientException(final String errorCode, final String message) {
		super(message, errorCode);
	}

	public ClientException(final String errorCode, final String message, final Throwable cause) {
		super(message, errorCode, cause);
	}

}
