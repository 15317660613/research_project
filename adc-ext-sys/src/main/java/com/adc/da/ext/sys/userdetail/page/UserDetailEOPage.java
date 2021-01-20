package com.adc.da.ext.sys.userdetail.page;

import com.adc.da.base.page.BasePage;

import java.util.Date;

/**
 * <b>功能：</b>TS_USER_DETAIL UserDetailEOPage<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-09-27 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class UserDetailEOPage extends BasePage {

    private String id;
    private String idOperator = "=";
    private String userId;
    private String userIdOperator = "=";
    private String updatePwdTime;
    private String updatePwdTime1;
    private String updatePwdTime2;
    private String updatePwdTimeOperator = "=";
    private String frozenFlag;
    private String frozenFlagOperator = "=";

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

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserIdOperator() {
        return this.userIdOperator;
    }

    public void setUserIdOperator(String userIdOperator) {
        this.userIdOperator = userIdOperator;
    }

    public String getUpdatePwdTime() {
        return this.updatePwdTime;
    }

    public void setUpdatePwdTime(String updatePwdTime) {
        this.updatePwdTime = updatePwdTime;
    }

    public String getUpdatePwdTime1() {
        return this.updatePwdTime1;
    }

    public void setUpdatePwdTime1(String updatePwdTime1) {
        this.updatePwdTime1 = updatePwdTime1;
    }

    public String getUpdatePwdTime2() {
        return this.updatePwdTime2;
    }

    public void setUpdatePwdTime2(String updatePwdTime2) {
        this.updatePwdTime2 = updatePwdTime2;
    }

    public String getUpdatePwdTimeOperator() {
        return this.updatePwdTimeOperator;
    }

    public void setUpdatePwdTimeOperator(String updatePwdTimeOperator) {
        this.updatePwdTimeOperator = updatePwdTimeOperator;
    }

    public String getFrozenFlag() {
        return this.frozenFlag;
    }

    public void setFrozenFlag(String frozenFlag) {
        this.frozenFlag = frozenFlag;
    }

    public String getFrozenFlagOperator() {
        return this.frozenFlagOperator;
    }

    public void setFrozenFlagOperator(String frozenFlagOperator) {
        this.frozenFlagOperator = frozenFlagOperator;
    }

}
