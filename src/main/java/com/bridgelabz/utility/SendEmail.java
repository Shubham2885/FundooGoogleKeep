package com.bridgelabz.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class SendEmail {

	@Autowired
	private JavaMailSender javaMailSender;
	@Autowired
	SimpleMailMessage mailMessage;
	
	public String accountEmailVarification(String token,String email)
	{
		mailMessage.setTo(MessageReference.SENDER_MAIL,email);
		mailMessage.setSubject(MessageReference.VERIFY_MAIL_SUBJECT);
		mailMessage.setText(MessageReference.VERIFY_MAIL_TEXT+token);
		javaMailSender.send(mailMessage);
		return MessageReference.MAIL_SENDED;
	}
	
	public String resetPasswordMail(String email,String token)
	{
		mailMessage.setTo(MessageReference.SENDER_MAIL,email);
		mailMessage.setSubject(MessageReference.RESET_PASSWORD_MAIL_SUBJECT);
		mailMessage.setText(MessageReference.RESET_PASSWORD_MAIL_TEXT+token);
		javaMailSender.send(mailMessage);
		return MessageReference.RESET_PASSWORD_MAIL_SENDED_MESSAGE;
	}
}
