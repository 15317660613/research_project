package com.adc.da.progress.page;

import com.adc.da.base.page.BasePage;
import lombok.Data;

import java.util.List;

/**
 * <b>功能：</b>PR_PROJECT_NAME ProjectNameEOPage<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-07-18 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Data
public class ProjectNameEOPage extends BasePage {

    private String extInfo6;

    private String extInfo6Operator = "=";

    private String id;

    private String idOperator = "=";

    private String stageOrderId;

    private String stageOrderIdOperator = "=";

    /**
     * 用于in查询
     */
    private List<String> stageOrderIds;

    private String procName;

    private String procNameOperator = "=";

    private String procDefKey;

    private String procDefKeyOperator = "=";

    private String delFlag;

    private String delFlagOperator = "=";

    private String extInfo1;

    private String extInfo1Operator = "=";

    private String extInfo2;

    private String extInfo2Operator = "=";

    private String extInfo3;

    private String extInfo3Operator = "=";

    private String extInfo4;

    private String extInfo4Operator = "=";

    private String extInfo5;

    private String extInfo5Operator = "=";

}
