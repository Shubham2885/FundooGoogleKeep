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
package com.bridgelabz.service;

import com.bridgelabz.dto.LoginDTO;
import com.bridgelabz.dto.ResetPasswordDTO;
import com.bridgelabz.dto.UserDTO;
import com.bridgelabz.exception.custome.CustomException;
import com.bridgelabz.response.Response;

public interface IUserService {

	Response getAllUser();
	Response addUser(UserDTO userDTO);
	Response login(LoginDTO loginDTO) throws CustomException.BadCredintial;
	Response forgetPassword(String username);
	Response resetPassword(ResetPasswordDTO resetPasswordDTO, String token);
	Response accountVerification(String token);
	Response getUserDetails(String token);
	Response logout(String token);
}
