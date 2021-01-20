package com.adc.da.statistics.page;

import com.adc.da.base.page.BasePage;
import lombok.Getter;
import lombok.Setter;

/**
 * <b>功能：</b>ST_CONTRACT_AMOUNT ContractAmountEOPage<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-11-18 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Getter
@Setter
public class ContractAmountEOPage extends BasePage {

    /***/
    private String projectId;

    /***/
    private String projectIdOperator = "=";

    /***/
    private String contractNo;

    /***/
    private String contractNoOperator = "=";

    /***/
    private String businessId;

    /***/
    private String businessIdOperator = "=";

    /***/
    private String deptId;

    /***/
    private String deptIdOperator = "=";

    /***/
    private String contractAmount;

    /***/
    private String contractAmountOperator = "=";

    /***/
    private String createTime;

    /***/
    private String createTime1;

    /***/
    private String createTime2;

    /***/
    private String createTimeOperator = "=";

    /***/
    private String projectBeginTime;

    /***/
    private String projectBeginTime1;

    /***/
    private String projectBeginTime2;

    /***/
    private String projectBeginTimeOperator = "=";

    /***/
    private String extInfo01;

    /***/
    private String extInfo01Operator = "=";

    /***/
    private String extInfo02;

    /***/
    private String extInfo02Operator = "=";

    /***/
    private String extInfo03;

    /***/
    private String extInfo03Operator = "=";

}
