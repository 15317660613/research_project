package com.adc.da.research.page;

import com.adc.da.base.page.BasePage;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * <b>功能：</b>RS_PROJECT_DECLARATION DeclarationEOPage<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-11-26 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Setter
@Getter
public class DeclarationEOPage extends BasePage {

    /***/
    private String id;

    /***/
    private String idOperator = "=";

    /***/
    private String projectName;
    /***/
    private String  deptName;
    /***/
    private String creatorName;

    /***/
    private String leaderName;

    /***/
    private String leaderId;

    /***/
    private String leaderIdOperator = "=";

    /***/
    private String deptId;

    /***/
    private String deptIdOperator = "=";

    /***/
    private String projectTypeId;

    /***/
    private String projectTypeIdOperator = "=";

    /***/
    private String undertakingId;

    /***/
    private String undertakingIdOperator = "=";

    /***/
    private String createTime;

    /**
     * begin
     */
    private Date createTime1;

    /**
     * end
     */
    private Date createTime2;

    /***/
    private String createTimeOperator = "=";

    /***/
    private String beginTime;

    /***/
    private String beginTime1;

    /***/
    private String beginTime2;

    /***/
    private String beginTimeOperator = "=";

    /***/
    private String endTime;

    /***/
    private String endTime1;

    /***/
    private String endTime2;

    /***/
    private String endTimeOperator = "=";

    /***/
    private String timeArea;

    /***/
    private String timeAreaOperator = "=";

    /***/
    private String amount;

    /***/
    private String amountOperator = "=";

    /***/
    private String summaryDoc01;

    /***/
    private String summaryDoc01Operator = "=";

    /***/
    private String summaryDoc02;

    /***/
    private String summaryDoc02Operator = "=";

    /***/
    private String summaryDoc03;

    /***/
    private String summaryDoc03Operator = "=";

    /***/
    private String summaryDoc04;

    /***/
    private String summaryDoc04Operator = "=";

    /***/
    private String summaryDoc05;

    /***/
    private String summaryDoc05Operator = "=";

    /***/
    private String delFlag;

    /***/
    private String delFlagOperator = "=";

    /***/
    private Integer status;

    /**
     * 科委会id 与 status 联查
     */
    private String committeeUserId;

    /***/
    private String extInfo1;

    /***/
    private String extInfo1Operator = "=";

    /***/
    private String extInfo2;

    /***/
    private String extInfo2Operator = "=";

    /***/
    private String extInfo3;

    /***/
    private String extInfo3Operator = "=";

    /**
     * 提交时间
     */
    private String extInfo4;

    /**
     * 0 begin
     *
     */
    private Date submissionTimeBegin;

    /**
     * 1 end
     */
    private Date submissionTimeEnd;

    /***/
    private String extInfo4Operator = "=";

    /**
     * 金额 str 型
     */
    private String extInfo5;

    /***/
    private String extInfo5Operator = "=";

    /**
     * 创建人
     */
    private String extInfo6;

    /***/
    private String extInfo6Operator = "=";

}
