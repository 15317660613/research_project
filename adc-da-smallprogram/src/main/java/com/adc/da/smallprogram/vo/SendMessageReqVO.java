package com.adc.da.smallprogram.vo;

import java.util.Map;

/**
 * @author ：mengqingchen
 * @date ：Created in 2019/2/21 16:16
 * @description：推送消息入参
 * @modified By：
 */
public class SendMessageReqVO {
    /**
     * 接口调用凭证
     */
    private String access_token;
    /**
     * 用户openid，可以是小程序的openid，也可以是mp_template_msg.appid对应的公众号的openid
     */
    private String touser;

    /**
     *所需下发的模板消息的id
     */
    private String template_id;
    /**
     *点击模板卡片后的跳转页面，仅限本小程序内的页面。支持带参数,（示例index?foo=bar）。该字段不填则模板无跳转
     */
    private String page;
    /**
     * 表单提交场景下，为 submit 事件带上的 formId；支付场景下，为本次支付的 prepay_id
     */
    private String form_id;

    /**
     * 模板需要放大的关键词，不填则默认无放大
     */
    private String emphasis_keyword;
    /**
     * 模板内容，不填则下发空模板
     */
    private Map<String,TemplateDataVO> data;


    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getForm_id() {
        return form_id;
    }

    public void setForm_id(String form_id) {
        this.form_id = form_id;
    }

    public String getEmphasis_keyword() {
        return emphasis_keyword;
    }

    public void setEmphasis_keyword(String emphasis_keyword) {
        this.emphasis_keyword = emphasis_keyword;
    }

    public Map<String, TemplateDataVO> getData() {
        return data;
    }

    public void setData(Map<String, TemplateDataVO> data) {
        this.data = data;
    }


}
