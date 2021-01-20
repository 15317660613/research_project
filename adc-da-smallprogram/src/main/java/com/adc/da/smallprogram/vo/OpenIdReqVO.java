package com.adc.da.smallprogram.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author ：mengqingchen
 * @date ：Created in 2019/2/21 15:29
 * @description：获取openId入参
 * @modified By：
 */
public class OpenIdReqVO {
    /**
     * 小程序 appId
     */
    @ApiModelProperty(name = "appId")
    private String appId;
    /**
     * 小程序 appSecret
     */
    @ApiModelProperty(name = "secret")
    private String secret;

    /**
     * 登录时获取的 code
     */
    @ApiModelProperty(name = "js_code")
    private String js_code;

    /**
     * 授权类型，此处只需填写 authorization_code
     */
    private String grant_type;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getJs_code() {
        return js_code;
    }

    public void setJs_code(String js_code) {
        this.js_code = js_code;
    }

    public String getGrant_type() {
        return grant_type;
    }

    public void setGrant_type(String grant_type) {
        this.grant_type = grant_type;
    }
}
