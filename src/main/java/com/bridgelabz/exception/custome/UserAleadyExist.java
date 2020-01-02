package com.bridgelabz.exception.custome;

public class UserAleadyExist extends RuntimeException{

	public UserAleadyExist(String message) {
		super(message);
	}
}
