package com.adc.da.progress.timer;

import com.adc.da.workflow.service.ActivitiHistoryDeleteService;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * describe:
 * 清理已经完成日报的流程信息
 *
 * @author 李坤澔
 *     date 2019-07-26
 */

@Component
@Slf4j
public class DeleteInstanceByProcessInstantId {
    @Autowired
    private ActivitiHistoryDeleteService service;

    @Autowired
    private HistoryService historyService;

    Integer timerLoop = 0;

    /**
     * 清理流程，需要流程已结束 （ACT_HI_PROCINST 结束时间有数据即可）
     */
    //@Scheduled(cron = "0/5 * * ? * *")
    public void a() {
        if (timerLoop > 0) {
            return;
        } else {
            timerLoop++;
        }

        String procInstId = "5847444";

        historyService.deleteHistoricProcessInstance(procInstId);

    }
}
