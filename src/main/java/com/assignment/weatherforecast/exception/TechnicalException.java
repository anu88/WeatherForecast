package com.assignment.weatherforecast.exception;

public class TechnicalException extends BaseException {

	private static final long serialVersionUID = -939152134977915215L;

	public TechnicalException() {
		super();
	}

	public TechnicalException(final String errorCode, final String message) {
		super(message, errorCode);
	}

	public TechnicalException(final String errorCode, final String message, final Throwable cause) {
		super(message, errorCode, cause);
	}

}
