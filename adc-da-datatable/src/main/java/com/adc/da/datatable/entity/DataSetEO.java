package com.adc.da.datatable.entity;

import com.adc.da.base.entity.BaseEntity;

import java.util.HashMap;


public class DataSetEO extends BaseEntity {

    private String tableName; //附表
    private String subPrimaryKey = "id"; //附表主键名

    private String mainTableName;//主表
    private String mainPrimaryKey = "usid"; //主表主键名

    private String idParam;
    private String fieldName;

    public String getTableName() {
        return tableName;
    }
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    public String getMainTableName() {
        return mainTableName;
    }
    public void setMainTableName(String mainTableName) {
        this.mainTableName = mainTableName;
    }


    public String getMainPrimaryKey() {
        return mainPrimaryKey;
    }
    public void setMainPrimaryKey(String mainPrimaryKey) {
        this.mainPrimaryKey = mainPrimaryKey;
    }
    public String getSubPrimaryKey() {
        return subPrimaryKey;
    }
    public void setSubPrimaryKey(String subPrimaryKey) {
        this.subPrimaryKey = subPrimaryKey;
    }

}
