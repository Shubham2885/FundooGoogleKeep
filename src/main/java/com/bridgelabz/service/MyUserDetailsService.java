package com.bridgelabz.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bridgelabz.exception.custome.UserNotFoundException;
import com.bridgelabz.reposiitory.UserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		com.bridgelabz.model.User user = userRepository.findByEmail(username).orElseThrow(()->new UserNotFoundException("User Not Found.."));
		return new User(user.getEmail(), user.getPassword(), new ArrayList<>());
	}
}
