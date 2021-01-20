package com.adc.da.smallprogram.entity;

import com.adc.da.base.entity.BaseEntity;


/**
 * <b>功能：</b>TS_SCHEDULE_RESEARCH_MARK ScheduleResearchMarkEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-05-26 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class ScheduleResearchMarkEO extends BaseEntity {

    private String id;
    private String researchId;
    private String userId;
    private String userName;
    private Integer top;
    private Integer collect;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>researchId -> research_id</li>
     * <li>userId -> user_id</li>
     * <li>userName -> user_name</li>
     * <li>top -> top</li>
     * <li>collect -> collect</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) { return null; }
        switch (fieldName) {
            case "id": return "id";
            case "researchId": return "research_id";
            case "userId": return "user_id";
            case "userName": return "user_name";
            case "top": return "top";
            case "collect": return "collect";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>research_id -> researchId</li>
     * <li>user_id -> userId</li>
     * <li>user_name -> userName</li>
     * <li>top -> top</li>
     * <li>collect -> collect</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) { return null; }
        switch (columnName) {
            case "id": return "id";
            case "research_id": return "researchId";
            case "user_id": return "userId";
            case "user_name": return "userName";
            case "top": return "top";
            case "collect": return "collect";
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
    public String getResearchId() {
        return this.researchId;
    }

    /**  **/
    public void setResearchId(String researchId) {
        this.researchId = researchId;
    }

    /**  **/
    public String getUserId() {
        return this.userId;
    }

    /**  **/
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**  **/
    public String getUserName() {
        return this.userName;
    }

    /**  **/
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**  **/
    public Integer getTop() {
        return this.top;
    }

    /**  **/
    public void setTop(Integer top) {
        this.top = top;
    }

    /**  **/
    public Integer getCollect() {
        return this.collect;
    }

    /**  **/
    public void setCollect(Integer collect) {
        this.collect = collect;
    }

}
