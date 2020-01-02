package com.bridgelabz.exception.globle;

import java.nio.file.AccessDeniedException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.bridgelabz.exception.custome.NoteNotAvailable;
import com.bridgelabz.exception.custome.UserAleadyExist;
import com.bridgelabz.exception.custome.UserNotFoundException;
import com.bridgelabz.response.NoteResponse;
import com.bridgelabz.response.UserResponse;

import io.jsonwebtoken.ExpiredJwtException;
@RestControllerAdvice
public class GlobalException {

	/**
	 * @Perpose User to throw Global EXecption
	 * @param e Exception
	 * @return Exception
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<UserResponse> exception(Exception e)
	{
		return new ResponseEntity<UserResponse>(new UserResponse(400, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	/**
	 * @Perpose User to throw User Already Exception
	 * @param e Exception
	 * @return Exception
	 */
	@ExceptionHandler(UserAleadyExist.class)
	public ResponseEntity<UserResponse> userAleadyExist(UserAleadyExist e)
	{
		return new ResponseEntity<UserResponse>(new UserResponse(302, e.getMessage(), null), HttpStatus.FOUND);
	}
	
	/**
	 * @Perpose User to throw User Already Exception
	 * @param e Exception
	 * @return Exception
	 */
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<UserResponse> userNotFound(UserNotFoundException e)
	{
		return new ResponseEntity<UserResponse>(new UserResponse(404, e.getMessage(), null), HttpStatus.NOT_FOUND);
	}
	
	/**
	 * @Perpose User to throw User Already Exception
	 * @param e Exception
	 * @return Exception
	 */
	@ExceptionHandler(NoteNotAvailable.class)
	public ResponseEntity<NoteResponse> noteNotAvailable(NoteNotAvailable e)
	{
		return new ResponseEntity<NoteResponse>(new NoteResponse(404, e.getMessage(), null), HttpStatus.NOT_FOUND);
	}
	
	/**
	 * @Perpose User to throw User Already Exception
	 * @param e Exception
	 * @return Exception
	 */
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<NoteResponse> accessDeniedException(AccessDeniedException e)
	{
		return new ResponseEntity<NoteResponse>(new NoteResponse(404, "Go To Login", null), HttpStatus.NOT_FOUND);
	}
	
	/**
	 * @Perpose User to throw Token Expired Exception
	 * @param e Exception
	 * @return Exception
	 */
	@ExceptionHandler(ExpiredJwtException.class)
	public ResponseEntity<NoteResponse> accessDeniedException(ExpiredJwtException e)
	{
		return new ResponseEntity<NoteResponse>(new NoteResponse(404, e.getMessage(), null), HttpStatus.NOT_FOUND);
	}
	
	
}
