package com.assignment.weatherforecast.exception;

public abstract class BaseException extends RuntimeException {

	private static final long serialVersionUID = 8646824191194827904L;
	
	private String errorCode;

	private String message;

	protected BaseException() {
		super();
	}

	protected BaseException(final String message, final String errorCode) {
		super(message);
		this.errorCode = errorCode;
		this.message = message;
	}

	protected BaseException(final String message, final String errorCode, final Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(final String errorCode) {
		this.errorCode = errorCode;
	}
}