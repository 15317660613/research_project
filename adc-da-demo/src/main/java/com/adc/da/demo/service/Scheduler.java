package com.adc.da.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AsyncTask asyncTask;

    @Scheduled(fixedRate = 20000)
    public void testTasks() throws InterruptedException {
        logger.info("每20秒执行一次。开始……");
        for (int i = 0; i < 10; i++) {
            asyncTask.doTask2(i);
        }
        logger.info("每20秒执行一次。结束。");
    }

}
