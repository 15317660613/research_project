package com.adc.da.demo.service;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
// @RabbitListener(queues = "helloQueue, hiQueue")
public class HelloReceiver {



	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(value = "helloQueue", durable = "true"),
			exchange = @Exchange(value = "adc-exchange", ignoreDeclarationExceptions = "true"),
			key = "cdf-key"))
	public void hello(String hello) {
		System.out.println("hello Receiver : " + hello);
	}
	
	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(value = "helloQueue2", durable = "true"),
			exchange = @Exchange(value = "adc-exchange", ignoreDeclarationExceptions = "true"),
			key = "cdf-key"))
	public void hello2(String hello2) {
		System.out.println("hello2 Receiver : " + hello2);
	}

	@RabbitListener(bindings = @QueueBinding(
				value = @Queue(value = "hiQueue", durable = "true"),
				exchange = @Exchange(value = "adc-exchange", ignoreDeclarationExceptions = "true"),
				key = "adc-key"))
	public void hi(String hi) {
		System.out.println("Receiver : " + hi);
	}
}