package com.adc.da.research.page;

import lombok.Getter;
import lombok.Setter;

/**
 * <b>功能：</b>RS_PROJECT_KPI_DELIVERABLE KpiDeliverableEOPage<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-08-23 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Getter
@Setter
public class KpiDeliverableEOPage extends RSBasePage {

    private String id;

    private String idOperator = "=";

    private String name;

    private String nameOperator = "=";

    private String kpi;

    private String kpiOperator = "=";

    private String extInfo1;

    private String extInfo1Operator = "=";

    private String extInfo2;

    private String extInfo2Operator = "=";

    private String extInfo3;

    private String extInfo3Operator = "=";

}
