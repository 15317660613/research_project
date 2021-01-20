package com.adc.da.demo.service;

import java.util.Date;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HelloSender {

	@Autowired
	private AmqpTemplate rabbitTemplate;

	public void sendHi() {
		String sendMsg = "Hi " + new Date();
		System.out.println("sendHi : " + sendMsg);
		this.rabbitTemplate.convertAndSend("adc-exchange", "adc-key", sendMsg);
	}

	public void sendHello() {
		String sendMsg = "Hello " + new Date();
		System.out.println("sendHello : " + sendMsg);
		this.rabbitTemplate.convertAndSend("adc-exchange", "cdf-key", sendMsg);
	}
}