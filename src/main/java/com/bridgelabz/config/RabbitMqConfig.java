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

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;

import com.bridgelabz.utility.Listener;

@Configuration
@EnableRabbit
public class RabbitMqConfig {

	public static final String EXCHAGE_NAME="myexchange";
	public static final String QUEUE_NAME="myqueue";
	
	
	@Bean
	Queue createQueue() {
		return new Queue(QUEUE_NAME,false);
	}
	
	@Bean
	TopicExchange exchange() {
		return new TopicExchange(EXCHAGE_NAME);
	}
	
	@Bean
	Binding binding(Queue queue,TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with("shubham.#");
	}
	
	@Bean
	SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,MessageListenerAdapter messageListenerAdapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory((ConnectionFactory) connectionFactory);
		container.setQueueNames(QUEUE_NAME);
		container.setMessageListener(messageListenerAdapter);
		return container;
	}
	@Bean
	@Primary
	Listener getListener() {
		return new Listener();
	}
	
	@Bean
	MessageListenerAdapter listenerAdapter(Listener listener) {
		return new MessageListenerAdapter(listener,"sendMessage");
	}
	
	@Bean
    public MappingJackson2MessageConverter consumerJackson2MessageConverter() {
        return new MappingJackson2MessageConverter();
    }

	@Bean
    public RabbitTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
