package com.adc.da.statics.vo;

import lombok.Data;

@Data
public class StaticOperationOrgContractInvoiceVO {
    private boolean isBigOrg = false; //是否为本部级别

    private String orgName; // 部门名称

    private String orgId; // 部门id

    private Double monthContractAmount;  //月开票额

    private Double monthIncreaseRate; // 月增长率

    private Double allContractAmount; //累计开票额

    private Double finishedRate; // 全年目标完成率
}
