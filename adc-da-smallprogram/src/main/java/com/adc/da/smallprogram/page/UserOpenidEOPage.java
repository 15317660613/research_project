package com.adc.da.smallprogram.page;


import com.adc.da.base.page.BasePage;

/**
 * <b>功能：</b>TS_USER_OPENID UserOpenidEOPage<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-03-18 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class UserOpenidEOPage extends BasePage {

    private String id;
    private String idOperator = "=";
    private String userId;
    private String userIdOperator = "=";
    private String openId;
    private String openIdOperator = "=";
    private String delFlag;
    private String delFlagOperator = "=";

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

    public String getOpenId() {
        return this.openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getOpenIdOperator() {
        return this.openIdOperator;
    }

    public void setOpenIdOperator(String openIdOperator) {
        this.openIdOperator = openIdOperator;
    }

    public String getDelFlag() {
        return this.delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getDelFlagOperator() {
        return this.delFlagOperator;
    }

    public void setDelFlagOperator(String delFlagOperator) {
        this.delFlagOperator = delFlagOperator;
    }

}
