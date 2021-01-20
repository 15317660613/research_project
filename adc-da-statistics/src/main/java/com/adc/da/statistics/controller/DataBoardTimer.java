package com.adc.da.statistics.controller;

import com.adc.da.statistics.service.DataBoardSyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * describe:
 *
 * @author 李坤澔
 *     date 2019-06-18
 */
@Component
@Slf4j
public class DataBoardTimer {
    @Autowired
    private DataBoardSyncService dataBoardSyncService;

    /**
     * 每天5时进行统计
     */
    @Scheduled(cron = "0 0 5 ? * *")
    public void autoSync() {
        try {
            dataBoardSyncService.syncDataFromES(true);
            log.warn("同步合同数据到Oracle 完成");
        } catch (Exception e) {
            log.error("同步合同数据到Oracle异常 {}", e.getMessage(), e);
        }

    }
}
