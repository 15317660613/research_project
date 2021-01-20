package com.adc.da.dashboard.page;

import com.adc.da.base.page.BasePage;

/**
 * <b>功能：</b>DB_PROVINCE_AREA ProvinceAreaEOPage<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-04-08 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class ProvinceAreaEOPage extends BasePage {

    private String id;

    private String idOperator = "=";

    private String province;

    private String provinceOperator = "=";

    private String area;

    private String areaOperator = "=";

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

    public String getProvince() {
        return this.province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getProvinceOperator() {
        return this.provinceOperator;
    }

    public void setProvinceOperator(String provinceOperator) {
        this.provinceOperator = provinceOperator;
    }

    public String getArea() {
        return this.area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAreaOperator() {
        return this.areaOperator;
    }

    public void setAreaOperator(String areaOperator) {
        this.areaOperator = areaOperator;
    }

}
