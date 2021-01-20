package com.adc.da.smallprogram.vo;


import com.adc.da.sys.entity.UserEO;

public class SmallUserVO extends UserEO {
    private String openId;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }
}
