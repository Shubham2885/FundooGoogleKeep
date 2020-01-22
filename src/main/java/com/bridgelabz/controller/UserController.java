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
package com.bridgelabz.controller;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.dto.LoginDTO;
import com.bridgelabz.dto.ResetPasswordDTO;
import com.bridgelabz.dto.UserDTO;
import com.bridgelabz.response.Response;
import com.bridgelabz.service.IUserService;
@RestController
@RequestMapping("/fundoo")
public class UserController {

	@Autowired
	private IUserService userService;
	
	@GetMapping("/userDetails")
	public Response getUserDetails(@RequestHeader String token){
		return userService.getUserDetails(token);
	}
	
	@GetMapping("/users")
	public Response getAllUsers(){
		return userService.getAllUser();
	}
	
	
	//Register
	@PostMapping("/register")
	public Response addUser(@Valid @RequestBody UserDTO userDTO){
	return userService.addUser(userDTO);
	}
	
	//
	@PostMapping("/login")
	public Response login(@Valid @RequestBody LoginDTO loginDTO){
		return userService.login(loginDTO);
	}
	
	//forget password and validated username
	@GetMapping("/forgetPassword")
	public Response forgetPassword(@Valid @RequestParam String username){
		return userService.forgetPassword(username);
	}
	
	//change password
	@PostMapping("/resetPassword")
	public Response resetPassword(@Valid @RequestBody ResetPasswordDTO forgetPasswordDTO,@RequestParam String token){
		return userService.resetPassword(forgetPasswordDTO,token);
	}
		
	@GetMapping("/AccountVerification")	
	public Response AccountVerification(@RequestParam String token){
		return userService.accountVerification(token);
	}
/*
	@PostMapping("/uploadProfilePic")
	public UserResponse uploadProfilePic(@RequestPart(value = "file") MultipartFile file,@RequestHeader String token ) {
		return userService.uploadProfilePic(file, token);
	}
	
	@DeleteMapping("/deleteProfilePic")
	public UserResponse deleteProfilePic(@RequestHeader String token) {
		return userService.deleteFileFromS3Bucket(token);
	}
*/
	@PostMapping("/logout")
	public Response logout(@RequestHeader String token) {
		return userService.logout(token);
	}
}
