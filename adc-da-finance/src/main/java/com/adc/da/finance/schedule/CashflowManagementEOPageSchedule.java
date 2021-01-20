package com.adc.da.finance.schedule;

import com.adc.da.finance.service.CashflowManagementEOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CashflowManagementEOPageSchedule {


    @Autowired
    private CashflowManagementEOService cashflowManagementEOService;


    @Scheduled(cron = "0 0 1 1 * ?")
    //@Scheduled(cron = "0 0/5 * * * ?")
    public void doTask() {
        try {
            cashflowManagementEOService.createCashflow();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
