package com.adc.da.event.entity;

import com.adc.da.base.entity.BaseEntity;


/**
 * <b>功能：</b>WR_EVENT_FILE EventFileEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-08-12 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class EventFileEO extends BaseEntity {

    private String eventId;
    private String fileId;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>eventId -> event_id_</li>
     * <li>fileId -> file_id_</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) { return null; }
        switch (fieldName) {
            case "eventId": return "event_id_";
            case "fileId": return "file_id_";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>event_id_ -> eventId</li>
     * <li>file_id_ -> fileId</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) { return null; }
        switch (columnName) {
            case "event_id_": return "eventId";
            case "file_id_": return "fileId";
            default: return null;
        }
    }
    
    /**  **/
    public String getEventId() {
        return this.eventId;
    }

    /**  **/
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    /**  **/
    public String getFileId() {
        return this.fileId;
    }

    /**  **/
    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

}
