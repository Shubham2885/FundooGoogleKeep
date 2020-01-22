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
package com.bridgelabz.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.bridgelabz.exception.custome.CustomException;

@Component
public class SendEmail {

	@Autowired
	private JavaMailSender javaMailSender;
	@Autowired
	SimpleMailMessage mailMessage;
	
	public void sendMail(String token,String email,String subject,String text) {
		try {
			send(token, email, subject, text);
		}catch (Exception e) {
			throw new CustomException.MailError(MessageReference.MAIL_ERROR);
		}
	}
	
	private void send(String token,String email,String subject,String text) {
		mailMessage.setTo(MessageReference.SENDER_MAIL,email);
		mailMessage.setSubject(subject);
		mailMessage.setText(text+token);
		javaMailSender.send(mailMessage);
	}
	
}
