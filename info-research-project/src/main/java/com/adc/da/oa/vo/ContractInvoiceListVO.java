package com.adc.da.oa.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * describe:
 *
 * @author 李坤澔
 *     date 2019-09-18
 */
@Getter
@Setter
@ToString
public class ContractInvoiceListVO {

    /**
     * 合同编号
     */
    private String contractNO;

    /**
     * 项目编号
     */
    private String projectNO;

    /**
     * 开票数据
     */
    List<ContractInvoiceVO> contractInvoiceVOList;
}
