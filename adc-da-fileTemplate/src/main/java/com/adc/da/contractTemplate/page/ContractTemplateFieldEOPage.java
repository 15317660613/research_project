package com.adc.da.contractTemplate.page;

import com.adc.da.base.page.BasePage;

import java.sql.Clob;
import java.util.Date;

/**
 * <b>功能：</b>CONTRACT_TEMPLATE_FIELD ContractTemplateFieldEOPage<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-10-29 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class ContractTemplateFieldEOPage extends BasePage {

    private String fileId;
    private String fileIdOperator = "=";
    private String templateJson;
    private String templateJsonOperator = "=";
    private String createTime;
    private String createTime1;
    private String createTime2;
    private String createTimeOperator = "=";
    private String tempNo;
    private String tempNoOperator = "=";

    public String getFileId() {
        return this.fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileIdOperator() {
        return this.fileIdOperator;
    }

    public void setFileIdOperator(String fileIdOperator) {
        this.fileIdOperator = fileIdOperator;
    }

    public String getTemplateJson() {
        return this.templateJson;
    }

    public void setTemplateJson(String templateJson) {
        this.templateJson = templateJson;
    }

    public String getTemplateJsonOperator() {
        return this.templateJsonOperator;
    }

    public void setTemplateJsonOperator(String templateJsonOperator) {
        this.templateJsonOperator = templateJsonOperator;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateTime1() {
        return this.createTime1;
    }

    public void setCreateTime1(String createTime1) {
        this.createTime1 = createTime1;
    }

    public String getCreateTime2() {
        return this.createTime2;
    }

    public void setCreateTime2(String createTime2) {
        this.createTime2 = createTime2;
    }

    public String getCreateTimeOperator() {
        return this.createTimeOperator;
    }

    public void setCreateTimeOperator(String createTimeOperator) {
        this.createTimeOperator = createTimeOperator;
    }

    public String getTempNo() {
        return this.tempNo;
    }

    public void setTempNo(String tempNo) {
        this.tempNo = tempNo;
    }

    public String getTempNoOperator() {
        return this.tempNoOperator;
    }

    public void setTempNoOperator(String tempNoOperator) {
        this.tempNoOperator = tempNoOperator;
    }

}
