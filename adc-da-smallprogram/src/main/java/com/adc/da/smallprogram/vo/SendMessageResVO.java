package com.adc.da.smallprogram.vo;

/**
 * @author ：mengqingchen
 * @date ：Created in 2019/2/21 17:19
 * @description：发送服务消息返回信息
 * @modified By：
 */
public class SendMessageResVO {
    /**
     * 错误码
     */
    private Integer errcode;

    /**
     * 错误信息
     */
    private String errmsg;

    public Integer getErrcode() {
        return errcode;
    }

    public void setErrcode(Integer errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }
}
