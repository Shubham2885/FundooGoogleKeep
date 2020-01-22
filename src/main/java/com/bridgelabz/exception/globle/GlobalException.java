/******************************************************************************
 *  Compilation:  javac -d bin ElasticSearchConfig.java
 *  Execution:    java -cp bin com.bridgelabz.config;
 *  						  
 *  
 *  Purpose:      ElasticSearch configuration class
 *  @author  Shubham Chavan
 *  @version 1.0
 *  @since   11-12-2019
 *
 ******************************************************************************/
package com.bridgelabz.exception.globle;

import java.nio.file.AccessDeniedException;
import java.util.Arrays;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.bridgelabz.exception.custome.CustomException;
import com.bridgelabz.response.Response;


import io.jsonwebtoken.ExpiredJwtException;
@RestControllerAdvice
public class GlobalException {

	/**
	 * @Perpose User to throw Global EXecption
	 * @param e Exception
	 * @return Exception
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Response> exception(Exception e)
	{
		return new ResponseEntity<Response>(new Response(400, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	/**
	 * @Perpose User to throw User Already Exception
	 * @param e Exception
	 * @return Exception
	 */
	@ExceptionHandler(CustomException.UserAleadyExist.class)
	public ResponseEntity<Response> userAleadyExist(CustomException.UserAleadyExist e)
	{
		return new ResponseEntity<Response>(new Response(302, e.getMessage(), null), HttpStatus.FOUND);
	}
	
	/**
	 * @Perpose User to throw User Already Exception
	 * @param e Exception
	 * @return Exception
	 */
	@ExceptionHandler(CustomException.UserNotFoundException.class)
	public ResponseEntity<Response> userNotFound(CustomException.UserNotFoundException e)
	{
		return new ResponseEntity<Response>(new Response(404, e.getMessage(), null), HttpStatus.NOT_FOUND);
	}
	
	/**
	 * @Perpose User to throw User Already Exception
	 * @param e Exception
	 * @return Exception
	 */
	@ExceptionHandler(CustomException.NoteNotAvailable.class)
	public ResponseEntity<Response> noteNotAvailable(CustomException.NoteNotAvailable e)
	{
		return new ResponseEntity<Response>(new Response(404, e.getMessage(), null), HttpStatus.NOT_FOUND);
	}
	
	/**
	 * @Perpose User to throw User Already Exception
	 * @param e Exception
	 * @return Exception
	 */
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<Response> accessDeniedException(AccessDeniedException e)
	{
		return new ResponseEntity<Response>(new Response(404, "Go To Login", null), HttpStatus.NOT_FOUND);
	}
	
	/**
	 * @Perpose User to throw Token Expired Exception
	 * @param e Exception
	 * @return Exception
	 */
	@ExceptionHandler(ExpiredJwtException.class)
	public ResponseEntity<Response> accessDeniedException(ExpiredJwtException e)
	{
		return new ResponseEntity<Response>(new Response(404, "Token is expired", null), HttpStatus.NOT_FOUND);
	}
	
	/**
	 * @Perpose Invalid patter or null value in validation throw this Exception
	 * @param e Exception
	 * @return Exception
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Response> methodArgumentNotValidException(MethodArgumentNotValidException e){
		String arr[]=e.getLocalizedMessage().split(";");
	//DefaultMessageSourceResolvable x =  new DefaultMessageSourceResolvable(message);
		return new ResponseEntity<Response>(new Response(404,arr[arr.length-1].toString().replaceFirst("]", "").replaceFirst("default message", ""), null), HttpStatus.NOT_FOUND);	
	}
	
	/**
	 * @Perpose User not login Exception
	 * @param e Exception
	 * @return Exception
	 */
	@ExceptionHandler(CustomException.UserNotLogin.class)
	public ResponseEntity<Response> userNotLogin(CustomException.UserNotLogin e){
		return new ResponseEntity<Response>(new Response(404, e.getMessage(), null),HttpStatus.NOT_FOUND);
	}
}
