package com.adc.da.dashboard.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @ClassName OrgContractInvoiceVO
 * @Description: 各部门数据
 * @Author 丁强
 * @Date 2020/4/8
 * @Version V1.0
 **/
@Data
@Builder
public class OrgContractInvoiceVO {
    boolean isBigOrg = false; //是否为本部级别

    String orgName; // 部门名称

    String orgId; // 部门id

    Double monthContractAmount;  //月开票额

    Double monthIncreaseRate; // 月增长率

    Double allContractAmount; //累计开票额

    Double finishedRate; // 全年目标完成率

}
