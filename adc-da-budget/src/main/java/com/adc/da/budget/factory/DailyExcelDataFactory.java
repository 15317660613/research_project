package com.adc.da.budget.factory;

import com.adc.da.budget.entity.Daily;
import com.adc.da.budget.entity.DailyExcelData;

public class DailyExcelDataFactory {
    public static DailyExcelData map(Daily daily){
        DailyExcelData dailyExcelData = DailyExcelData.builder()
                .budgetName(daily.getBudgetName())
                .projectName(daily.getProjectName())
                .taskName(daily.getTaskName())
                .childrenTaskName(daily.getTaskName())
                .persons(daily.getCreateUserName())
                .status(String.valueOf(daily.getApproveState()))
                .build();
        dailyExcelData.setDept(daily.getDept());
        dailyExcelData.setDailyCreateTime(daily.getDailyCreateTime());
        dailyExcelData.setTimeSlot(daily.getTimeSlot());
        dailyExcelData.setApproveUserName(daily.getApproveUserName());
        dailyExcelData.setWorkDescription(dailyExcelData.getWorkDescription());
        return dailyExcelData;
    }
}
