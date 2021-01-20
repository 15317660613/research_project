package com.adc.da.crm.page;

import com.adc.da.base.page.BasePage;

public class AdcFormDataVOPage extends BasePage {
    private String id;
    private String idOperator = "=";
    private String formTitle;
    private String formTitleOperator = "=";
    private String createTime;
    private String createTimeOperator = "=";
    private String createName;
    private String createNameOperator = "=";
    private String formState;
    private String formStateOperator = "=";
    private Integer delFlag;
    private String delFlagOperator = "=";


    private String formId = "";
    private String formIdOperator = "=";

    public AdcFormDataVOPage() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdOperator() {
        return this.idOperator;
    }

    public void setIdOperator(String idOperator) {
        this.idOperator = idOperator;
    }

    public String getFormTitle() {
        return this.formTitle;
    }

    public void setFormTitle(String formTitle) {
        this.formTitle = formTitle;
    }

    public String getFormTitleOperator() {
        return this.formTitleOperator;
    }

    public void setFormTitleOperator(String formTitleOperator) {
        this.formTitleOperator = formTitleOperator;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateTimeOperator() {
        return this.createTimeOperator;
    }

    public void setCreateTimeOperator(String createTimeOperator) {
        this.createTimeOperator = createTimeOperator;
    }

    public String getCreateName() {
        return this.createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getCreateNameOperator() {
        return this.createNameOperator;
    }

    public void setCreateNameOperator(String createNameOperator) {
        this.createNameOperator = createNameOperator;
    }

    public String getFormState() {
        return this.formState;
    }

    public void setFormState(String formState) {
        this.formState = formState;
    }

    public String getFormStateOperator() {
        return this.formStateOperator;
    }

    public void setFormStateOperator(String formStateOperator) {
        this.formStateOperator = formStateOperator;
    }

    public Integer getDelFlag() {
        return this.delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public String getDelFlagOperator() {
        return this.delFlagOperator;
    }

    public void setDelFlagOperator(String delFlagOperator) {
        this.delFlagOperator = delFlagOperator;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getFormIdOperator() {
        return formIdOperator;
    }

    public void setFormIdOperator(String formIdOperator) {
        this.formIdOperator = formIdOperator;
    }
}
