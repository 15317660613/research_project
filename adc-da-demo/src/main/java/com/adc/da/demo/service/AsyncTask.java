package com.adc.da.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AsyncTask {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Async("myTaskAsyncPool") // myTaskAsynPool即配置线程池的方法名，此处如果不写自定义线程池的方法名，会使用默认的线程池
	public void doTask1(int i) throws InterruptedException {
		logger.info("Task1->" + i + " started.");
	}

	@Async("myTaskAsyncPool") // myTaskAsynPool即配置线程池的方法名，此处如果不写自定义线程池的方法名，会使用默认的线程池
	public void doTask2(int i) throws InterruptedException {
		logger.info("Task2->" + i + " started.");
	}

	@Async("myTaskAsyncPool") // myTaskAsynPool即配置线程池的方法名，此处如果不写自定义线程池的方法名，会使用默认的线程池
	public void doTask3() throws InterruptedException {
		for (int i = 0; i < 9; i++) {
			logger.info("Task3->" + i + " running.");
		}
	}
}