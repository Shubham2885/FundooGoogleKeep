package com.bridgelabz.controller;

import javax.el.ELException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
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
import com.bridgelabz.response.AllUserResponse;
import com.bridgelabz.response.UserResponse;
import com.bridgelabz.service.MyUserDetailsService;
import com.bridgelabz.service.UserService;
import com.bridgelabz.utility.JwtUtil;

@RestController
@RequestMapping("/fundoo")
public class UserController {

	@Autowired
	private UserService userService;
	
	@GetMapping("/userDetails")
	public UserResponse getUserDetails(@RequestHeader String token)
	{
		return userService.getUserDetails(token);
	}
	
	@GetMapping("/users")
	public AllUserResponse getAllUsers()
	{
		return userService.getAllUser();
	}
	
	
	//Register
	@PostMapping("/register")
	public UserResponse addUser(@Valid @RequestBody UserDTO userDTO)
	{
	return userService.addUser(userDTO);
	}
	
	//
	@PostMapping("/login")
	public UserResponse login(@Valid @RequestBody LoginDTO loginDTO)
	{
		return userService.login(loginDTO);
	}
	
	//forget password and validated username
	@GetMapping("/forgetPassword")
	public UserResponse forgetPassword(@Valid @RequestParam String username)
	{
		System.out.println(username);
		return userService.forgetPassword(username);
	}
	
	//change password
		@PostMapping("/resetPassword")
		public UserResponse resetPassword(@Valid @RequestBody ResetPasswordDTO forgetPasswordDTO,@RequestParam String token)
		{
			return userService.resetPassword(forgetPasswordDTO,token);
		}
		
	@GetMapping("/AccountVerification")	
	public UserResponse AccountVerification(@RequestParam String token)
	{
		return userService.accountVerification(token);
	}
}
