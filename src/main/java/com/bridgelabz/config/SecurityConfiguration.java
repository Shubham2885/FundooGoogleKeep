package com.bridgelabz.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.bridgelabz.filter.JwtRequestFilter;
import com.bridgelabz.service.MyUserDetailsService;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
@EnableSwagger2
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private MyUserDetailsService myUserDetailsService;
	@Autowired
	JwtRequestFilter jwtRequestFilter;
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(myUserDetailsService);
	}
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests()
			.antMatchers("/v2/api-docs", "/configuration/**","/swagger*/**","/webjars/**").permitAll()
			.antMatchers("/fundoo/users").permitAll()
			.antMatchers("/fundoo/register").permitAll()
			.antMatchers("/fundoo/login").permitAll()
			.antMatchers("/fundoo/forgetPassword").permitAll()
			.antMatchers("/fundoo/resetPassword").permitAll()
			.antMatchers("/fundoo/AccountVerification").permitAll()
			.anyRequest().authenticated()
			.and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}
	
	@Bean
	public PasswordEncoder getPasswordEncoder()
	{
		return  new BCryptPasswordEncoder();
	}

	
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		// TODO Auto-generated method stub
		return super.authenticationManagerBean();
	}
	
	@Bean
	public SimpleMailMessage getSimpleMailMessage()
	{
		return new SimpleMailMessage();
	}
	
	@Bean
	   public Docket productApi()
	   {
	       return new Docket(DocumentationType.SWAGGER_2)
	        .select()
	               .apis(RequestHandlerSelectors.basePackage("com.bridgelabz"))
	               .paths(PathSelectors.any())
	               //
	               .build();

	   }
}
