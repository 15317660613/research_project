package com.adc.da.progress.timer;

import com.adc.da.workflow.entity.ActInstanceDeleteEO;
import com.adc.da.workflow.service.ActivitiHistoryDeleteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * describe:
 *      清理已经完成日报的流程信息
 * @author 李坤澔
 *     date 2019-07-26
 */

@Component
@Slf4j
public class DeleteEndInstance {
    @Autowired
    private ActivitiHistoryDeleteService service;

    /**
     * 每天上午4点清理已经完成的日报审批流程，
     */
     @Scheduled(cron = "0 0 4 ? * *")
    public void a() {
        ActInstanceDeleteEO eo = new ActInstanceDeleteEO();

        List<String> procDefKeys = new ArrayList<>();
        procDefKeys.add("p478e4b877d504d39a392ba9317bd35e8");

        eo.setProcDefKeys(procDefKeys);
        try {
            service.deleteProcessInstant(eo);
            log.warn("日报审批流程清理成功 ");
        } catch (Exception e) {
            log.warn("日报审批流程清理失败 ", e);
        }

    }
}
