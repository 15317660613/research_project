package com.adc.da.dashboard.entity;

import com.adc.da.base.entity.BaseEntity;

/**
 * <b>功能：</b>DB_PROVINCE_AREA ProvinceAreaEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-04-08 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class ProvinceAreaEO extends BaseEntity {

    private Integer id;

    private String province;

    private String area;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>province -> province</li>
     * <li>area -> area</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) { return null; }
        switch (fieldName) {
        case "id":
            return "id";
        case "province":
            return "province";
        case "area":
            return "area";
        default:
            return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>province -> province</li>
     * <li>area -> area</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) { return null; }
        switch (columnName) {
        case "id":
            return "id";
        case "province":
            return "province";
        case "area":
            return "area";
        default:
            return null;
        }
    }

    /**  **/
    public Integer getId() {
        return this.id;
    }

    /**  **/
    public void setId(Integer id) {
        this.id = id;
    }

    /**  **/
    public String getProvince() {
        return this.province;
    }

    /**  **/
    public void setProvince(String province) {
        this.province = province;
    }

    /**  **/
    public String getArea() {
        return this.area;
    }

    /**  **/
    public void setArea(String area) {
        this.area = area;
    }

}
