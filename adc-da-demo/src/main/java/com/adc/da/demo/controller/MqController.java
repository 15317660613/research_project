package com.adc.da.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adc.da.demo.service.HelloSender;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/test/mq")
@Api(description = "|消息队列测试|")
public class MqController {

	@Autowired
	private HelloSender helloSender;

	@ApiOperation(value = "|发送hello队列|")
	@GetMapping("/page")
	public ResponseMessage page() throws Exception {
		helloSender.sendHello();
		return Result.success();
	}

	@ApiOperation(value = "|发送hi队列|")
	@GetMapping("")
	public ResponseMessage list() throws Exception {
		helloSender.sendHi();
		return Result.success();
	}
}
