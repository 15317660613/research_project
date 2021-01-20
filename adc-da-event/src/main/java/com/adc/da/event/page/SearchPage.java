package com.adc.da.event.page;

import com.adc.da.base.page.BasePage;


public class SearchPage extends BasePage {
    private String userId ;
    private String queryFlag;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getQueryFlag() {
        return queryFlag;
    }

    public void setQueryFlag(String queryFlag) {
        this.queryFlag = queryFlag;
    }
}
