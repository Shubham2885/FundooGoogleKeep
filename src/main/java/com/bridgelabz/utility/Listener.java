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

import java.io.Serializable;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.bridgelabz.model.RabbitMqModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

@JsonIgnoreProperties 
@Service
@RabbitListener(queues="myqueue")
public class Listener implements Serializable{
	@Autowired
	JavaMailSender sender;
	ObjectMapper mapper = new ObjectMapper();
	private static final long serialVersionUID = 1L;
	
	    public void sendMessage(byte[] message) {
	    	String msg = new String(message);
	    	RabbitMqModel model = new Gson().fromJson(msg, RabbitMqModel.class);
	        System.out.println(model.getText());
	        SimpleMailMessage mailMessage =  new SimpleMailMessage();
	        mailMessage.setTo("forgotbridge70@gmail.com",model.getSendTo());
	        mailMessage.setSubject(model.getSubject());
	        mailMessage.setText(model.getText());
	        sender.send(mailMessage);
	        System.out.println("Received a new notification..."+model.getText());
	}
}
