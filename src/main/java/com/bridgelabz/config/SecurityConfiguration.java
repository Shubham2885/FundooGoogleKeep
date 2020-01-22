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
package com.bridgelabz.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;



import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
@EnableSwagger2
@Configuration
public class SecurityConfiguration{
	

	
	@Bean
	public PasswordEncoder getPasswordEncoder()
	{
		return  new BCryptPasswordEncoder();
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
