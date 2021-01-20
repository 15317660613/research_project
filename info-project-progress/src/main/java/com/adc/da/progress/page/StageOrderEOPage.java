package com.adc.da.progress.page;

import com.adc.da.base.page.BasePage;
import lombok.Data;

/**
 * <b>功能：</b>PR_STAGE_ORDER StageOrderEOPage<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-07-18 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Data
public class StageOrderEOPage extends BasePage {

    private String id;

    private String idOperator = "=";

    private String projectType;

    private String projectTypeOperator = "=";

    private String stageName;

    private String stageNameOperator = "=";

    private String level;

    private String levelOperator = "=";

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

    private String extInfo6;

    private String extInfo6Operator = "=";

}
