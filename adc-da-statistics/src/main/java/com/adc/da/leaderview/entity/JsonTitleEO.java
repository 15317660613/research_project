package com.adc.da.leaderview.entity;

import com.adc.da.base.entity.BaseEntity;


/**
 * <b>功能：</b>ST_JSON_TITLE JsonTitleEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-09-24 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class JsonTitleEO extends BaseEntity {

    private String id;
    private String data;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id_</li>
     * <li>data -> data_</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) { return null; }
        switch (fieldName) {
            case "id": return "id_";
            case "data": return "data_";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id_ -> id</li>
     * <li>data_ -> data</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) { return null; }
        switch (columnName) {
            case "id_": return "id";
            case "data_": return "data";
            default: return null;
        }
    }
    
    /**  **/
    public String getId() {
        return this.id;
    }

    /**  **/
    public void setId(String id) {
        this.id = id;
    }

    /**  **/
    public String getData() {
        return this.data;
    }

    /**  **/
    public void setData(String data) {
        this.data = data;
    }

}
