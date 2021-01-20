package com.adc.da.datatable.entity;

import com.adc.da.base.entity.BaseEntity;

import java.util.Date;

/**
 * <b>功能：</b>TS_MODEL ModelEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-06-11 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class ModelEO extends BaseEntity {

    private String mName;
    private String mId;
    private String mInfo;
    private String mParam;
    private String mTables;
    private String mAssociates;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date mCreate;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date mChange;
    private String createMan;
    private String changeMan;
    private String mX;
    private String mY;
    private String mConfig;
    private String mCode;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>mName -> m_name</li>
     * <li>mId -> m_id</li>
     * <li>mInfo -> m_info</li>
     * <li>mParam -> m_param</li>
     * <li>mTables -> m_tables</li>
     * <li>mAssociates -> m_associates</li>
     * <li>mCreate -> m_create</li>
     * <li>mChange -> m_change</li>
     * <li>createMan -> create_man</li>
     * <li>changeMan -> change_man</li>
     * <li>mX -> m_x</li>
     * <li>mY -> m_y</li>
     * <li>mConfig -> m_config</li>
     * <li>mCode -> m_code</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) return null;
        switch (fieldName) {
            case "mName": return "m_name";
            case "mId": return "m_id";
            case "mInfo": return "m_info";
            case "mParam": return "m_param";
            case "mTables": return "m_tables";
            case "mAssociates": return "m_associates";
            case "mCreate": return "m_create";
            case "mChange": return "m_change";
            case "createMan": return "create_man";
            case "changeMan": return "change_man";
            case "mX": return "m_x";
            case "mY": return "m_y";
            case "mConfig": return "m_config";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>m_name -> mName</li>
     * <li>m_id -> mId</li>
     * <li>m_info -> mInfo</li>
     * <li>m_param -> mParam</li>
     * <li>m_tables -> mTables</li>
     * <li>m_associates -> mAssociates</li>
     * <li>m_create -> mCreate</li>
     * <li>m_change -> mChange</li>
     * <li>create_man -> createMan</li>
     * <li>change_man -> changeMan</li>
     * <li>m_x -> mX</li>
     * <li>m_y -> mY</li>
     * <li>m_config -> mConfig</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) return null;
        switch (columnName) {
            case "m_name": return "mName";
            case "m_id": return "mId";
            case "m_info": return "mInfo";
            case "m_param": return "mParam";
            case "m_tables": return "mTables";
            case "m_associates": return "mAssociates";
            case "m_create": return "mCreate";
            case "m_change": return "mChange";
            case "create_man": return "createMan";
            case "change_man": return "changeMan";
            case "m_x": return "mX";
            case "m_y": return "mY";
            case "m_config": return "mConfig";
            default: return null;
        }
    }

    /**  **/
    public String getMName() {
        return this.mName;
    }

    /**  **/
    public void setMName(String mName) {
        this.mName = mName;
    }

    /**  **/
    public String getMId() {
        return this.mId;
    }

    /**  **/
    public void setMId(String mId) {
        this.mId = mId;
    }

    /**  **/
    public String getMInfo() {
        return this.mInfo;
    }

    /**  **/
    public void setMInfo(String mInfo) {
        this.mInfo = mInfo;
    }

    /**  **/
    public String getMParam() {
        return this.mParam;
    }

    /**  **/
    public void setMParam(String mParam) {
        this.mParam = mParam;
    }

    /**  **/
    public String getMTables() {
        return this.mTables;
    }

    /**  **/
    public void setMTables(String mTables) {
        this.mTables = mTables;
    }

    /**  **/
    public String getMAssociates() {
        return this.mAssociates;
    }

    /**  **/
    public void setMAssociates(String mAssociates) {
        this.mAssociates = mAssociates;
    }

    /**  **/
    public Date getMCreate() {
        return this.mCreate;
    }

    /**  **/
    public void setMCreate(Date mCreate) {
        this.mCreate = mCreate;
    }

    /**  **/
    public Date getMChange() {
        return this.mChange;
    }

    /**  **/
    public void setMChange(Date mChange) {
        this.mChange = mChange;
    }

    /**  **/
    public String getCreateMan() {
        return this.createMan;
    }

    /**  **/
    public void setCreateMan(String createMan) {
        this.createMan = createMan;
    }

    /**  **/
    public String getChangeMan() {
        return this.changeMan;
    }

    /**  **/
    public void setChangeMan(String changeMan) {
        this.changeMan = changeMan;
    }

    /**  **/
    public String getMX() {
        return this.mX;
    }

    /**  **/
    public void setMX(String mX) {
        this.mX = mX;
    }

    /**  **/
    public String getMY() {
        return this.mY;
    }

    /**  **/
    public void setMY(String mY) {
        this.mY = mY;
    }

    /**  **/
    public String getMConfig() {
        return this.mConfig;
    }

    /**  **/
    public void setMConfig(String mConfig) {
        this.mConfig = mConfig;
    }

    public String getmCode() {
        return mCode;
    }

    public void setmCode(String mCode) {
        this.mCode = mCode;
    }
}
