package com.adc.da.leaderview.page;

import com.adc.da.base.page.BasePage;


/**
 * <b>功能：</b>ST_JSON_TITLE JsonTitleEOPage<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-09-24 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class JsonTitleEOPage extends BasePage {

    private String id;
    private String idOperator = "=";
    private String data;
    private String dataOperator = "=";

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

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDataOperator() {
        return this.dataOperator;
    }

    public void setDataOperator(String dataOperator) {
        this.dataOperator = dataOperator;
    }

}
