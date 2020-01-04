package com.bridgelabz.exception.custome;

public class DateParseException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public DateParseException(String msg) {
		super(msg);
	}
}
