package com.adc.da.finance.schedule;

import com.adc.da.finance.entity.CostManagementEO;
import com.adc.da.finance.page.CostManagementEOPage;
import com.adc.da.finance.service.CostManagementEOService;
import com.adc.da.finance.service.ProfitManagementEOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName ScheduledService
 * @Description: TODO
 * @Author 丁强
 * @Date 2020/4/24
 * @Version V1.0
 **/
@Component
public class CostManageMentScheduledService {
    @Autowired
    private ProfitManagementEOService profitManagementEOService;

    @Autowired
    private CostManagementEOService costManagementEOService;

    //    @Scheduled(cron = "*/5 * * * * ?")
    //@Scheduled(cron = "0 0 23 * * ?")
//    //public void doTask() {
//        profitManagementEOService.updateProfit();
//    }
    @Scheduled(cron = "0 0 1 * * ?")
    public void doTask() throws Exception {
        CostManagementEOPage costManagementEOPage = new CostManagementEOPage();
        costManagementEOPage.setStatus("1");
        costManagementEOPage.setStatusOperator("<");
        costManagementEOPage.getPager().setPageSize(999);
        List<CostManagementEO> costManagementEOList = costManagementEOService.queryByList(costManagementEOPage);
        costManagementEOService.taskMethod(costManagementEOList);
    }
}
