/******************************************************************************
 *  Compilation:  javac -d bin SpringBootFundooUserSecurityJwtMysqlApplication.java
 *  Execution:    java -cp bin com.bridgelabz;
 *  						  
 *  
 *  Purpose:      This is a main class which is used for run the project
 *  @author  Shubham Chavan
 *  @version 1.0
 *  @since   11-12-2019
 *
 ******************************************************************************/
package com.bridgelabz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SpringBootFundooUserSecurityJwtMysqlApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootFundooUserSecurityJwtMysqlApplication.class, args);
	}

}
