package com.bridgelabz.exception.custome;

public class ExpiredJwtTokenExeception extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ExpiredJwtTokenExeception(String msg) {
		super(msg);
	}
}
