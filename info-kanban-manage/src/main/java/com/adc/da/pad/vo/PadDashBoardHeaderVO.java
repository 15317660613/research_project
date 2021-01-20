package com.adc.da.pad.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @ClassName PadDashBoardHeaderVO
 * @Description: 经营数据看板 头部三个小块展示数据包装类
 * @Author 丁强
 * @Date 2020/4/1
 * @Version V1.0
 **/
@Data
public class PadDashBoardHeaderVO {
    Double yearContractAmount = 0.0D; //年累计合同额
    Double monthContractAmount = 0.0D; //当月合同额

    Double yearInvoiceAmount = 0.0D;//年累计开票额
    Double monthInvoiceAmount = 0.0D;//月累计开票额

    Double yearIncomeAmount = 0.0D; //年累计进账额
    Double monthIncomeAmount = 0.0D;//月累计进账额


}
