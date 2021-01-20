package com.adc.da.processform.vo;

import com.adc.da.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName ContractInvoiceVO
 * @Description: TODO
 * @Author 丁强
 * @Date 2020/4/14
 * @Version V1.0
 **/
@Data
public class ContractInvoiceVO {
    @Excel(name = "合同编号", orderNum = "13")
    String contractNO;

    @Excel(name = "OA上点开票的时间", importFormat = "yyyy-MM-dd HH:mm", exportFormat = "yyyy-MM-dd HH:mm", orderNum = "2")
    Date contractFinishedTime;

    @Excel(name = "合同名称", orderNum = "14")
    String contractName;
}
