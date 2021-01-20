package com.adc.da.budget.entity;

import lombok.Data;

@Data
public class ActivitiTaskNumberVO {
    private Long toDoListSize;
    private Long doedListSize;
    private Long delegateListSize;
    private Long outTimeListSize;
    private Long dailySize;
}
