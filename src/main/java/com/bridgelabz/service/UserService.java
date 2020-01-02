package com.bridgelabz.service;


import java.util.Optional;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bridgelabz.dto.LoginDTO;
import com.bridgelabz.dto.ResetPasswordDTO;
import com.bridgelabz.dto.UserDTO;
import com.bridgelabz.exception.custome.BadCredintial;
import com.bridgelabz.exception.custome.UserNotFoundException;
import com.bridgelabz.model.User;
import com.bridgelabz.reposiitory.NoteRepository;
import com.bridgelabz.reposiitory.UserRepository;
import com.bridgelabz.response.AllUserResponse;
import com.bridgelabz.response.UserResponse;
import com.bridgelabz.utility.JwtUtil;
import com.bridgelabz.utility.SendEmail;
@Service
public class UserService {
	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	MyUserDetailsService userDetailsService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	JwtUtil jwtUtil;
	@Autowired
	private SendEmail sendEmail;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	NoteRepository noteRepository;
	ModelMapper mapper = new ModelMapper();

	/**
	 * @purpose : return all users
	 * @return : return users list
	 */
	public AllUserResponse getAllUser() {
		return new AllUserResponse(UserResponse.STATUS200, "All Users", userRepository.findAll());
	}

	/**
	 * @purpose : add new user
	 * @param userDTO : store info of user
	 * @return : return ok if everything is ok
	 */
	public UserResponse addUser(UserDTO userDTO) {
		Optional<User> user1 = userRepository.findByEmail(userDTO.getEmail());
		if (user1.isEmpty()) {
			if (userDTO.getPassword().equals(userDTO.getCheckPassword())) {
				User user = mapper.map(userDTO, User.class);
				user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
				userRepository.save(user);
				try {
					return new UserResponse(UserResponse.STATUS200,
							UserResponse.USER_ADDED + " " + sendEmail
									.accountEmailVarification(jwtUtil.generateToken(user.getEmail()), user.getEmail()),
							null);
				} catch (Exception e) {
					e.printStackTrace();
					return new UserResponse(UserResponse.STATUS400, "internal error", null);
				}
			} else
				return new UserResponse(UserResponse.STATUS200, UserResponse.PASSWORD_DOES_NOT_MATCH, null);
		} else {
			return new UserResponse(UserResponse.STATUS200, UserResponse.USER_EMAIL_EXIST, null);
		}
	}

	/**
	 * @purpose : checking the login activity if credential is true then return token
	 * @param loginDTO : credential info
	 * @return : return the token
	 * @throws BadCredintial : exception if user provide bad credential
	 */
	public UserResponse login(LoginDTO loginDTO) throws BadCredintial {
		/*
		 * User user =
		 * userRepository.findByEmail(loginDTO.getEmail()).orElseThrow(()->new
		 * UserNotFoundException("User Not Found..")); if
		 * (loginDTO.getPassword().equals(user.getPassword())) { String token =
		 * jwtUtil.generateToken(loginDTO.getEmail()); return new
		 * Response(Response.STATUS200, Response.LOGIN_SUCCESSFULL+" Token : "+token,
		 * null); } else { return new Response(Response.STATUS200,
		 * Response.LOGIN_UNSUCCESSFULL, null); }
		 */
		User user = userRepository.findByEmail(loginDTO.getEmail())
				.orElseThrow(() -> new UserNotFoundException("User Not Found.."));
		if (user.isActive()) {
			try {
				authenticationManager.authenticate(
						new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
			} catch (BadCredintial e) {
				throw new BadCredintial("Incorret username and password");
			}
			final UserDetails userDetails = userDetailsService.loadUserByUsername(loginDTO.getEmail());
			final String token = jwtUtil.generateToken(userDetails.getUsername());
			return new UserResponse(UserResponse.STATUS200, UserResponse.LOGIN_SUCCESSFULL + " Token=" + token, null);
		} else {
			return new UserResponse(UserResponse.STATUS200, "Please verify account first", null);
		}
	}

	/**
	 * @purpose : verify user name as email id and return reset password link
	 * @param username : store user name as email id
	 * @return : return reset password link through provide mail id
	 */
	public UserResponse forgetPassword(String username) {
		userRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("User Not Found.."));
		String token = jwtUtil.generateToken(username);
		try {
			return new UserResponse(UserResponse.STATUS200, sendEmail.resetPasswordMail(username, token), null);
		} catch (Exception e) {
			e.printStackTrace();
			return new UserResponse(UserResponse.STATUS400, "internal error", null);
		}
	}

	/**
	 * @purpose : Reset the password 
	 * @param resetPasswordDTO : store password credential
	 * @param token : current user token for extracting user name of user
	 * @return : return OK message
	 */
	public UserResponse resetPassword(ResetPasswordDTO resetPasswordDTO, String token) {
		if (resetPasswordDTO.getPassword().equals(resetPasswordDTO.getCheckPassword())) {
			User user = userRepository.findByEmail(jwtUtil.validateToken(token))
					.orElseThrow(() -> new UserNotFoundException("User Not Found.."));
			user.setPassword(passwordEncoder.encode(resetPasswordDTO.getPassword()));
			userRepository.save(user);
			return new UserResponse(UserResponse.STATUS200, UserResponse.PASSWORD_CHANGE, null);
		} else {
			return new UserResponse(UserResponse.STATUS200, UserResponse.PASSWORD_DOES_NOT_MATCH, null);
		}
	}

	/**
	 * @purpose : if user is not active then send link for active account
	 * @param token : account verification token
	 * @return : return OK message
	 */
	public UserResponse accountVerification(String token) {
		User user = userRepository.findByEmail(jwtUtil.validateToken(token))
				.orElseThrow(() -> new UserNotFoundException("User Not Found.."));
		user.setActive(true);
		userRepository.save(user);
		return new UserResponse(UserResponse.STATUS200, "Account Activated...you can do login...", null);
	}

	/**
	 * @purpose : return current user details 
	 * @param token : current user token
	 * @return : return user details
	 */
	public UserResponse getUserDetails(String token) {
		Optional<User> user = userRepository.findByEmail(jwtUtil.validateToken(token));
		return new UserResponse(UserResponse.STATUS200, "Details Of User", user);
	}
}
