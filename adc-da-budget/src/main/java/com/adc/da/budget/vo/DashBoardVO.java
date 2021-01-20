package com.adc.da.budget.vo;

import lombok.Data;

@Data
public class DashBoardVO {
    private int year;
    private int projectOwnerNum;
    private int currentYearIncreaseProjectOwnerNum ;
    private int contactNum;
    private int currentYearContactIncreaseNum;
    private int policymakerNum;
}
