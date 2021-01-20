package com.adc.da.event.vo;

import com.adc.da.base.entity.BaseEntity;

import java.util.Date;

public class HistoryFileVO extends BaseEntity {
    private String id;
    private String fileId;
    private String filePath;
    private String editorId;
    private String editorName;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date editTime;

    private String eventCreateUserId;
    private String eventCreateUserName;

    public String getEventCreateUserId() {
        return eventCreateUserId;
    }

    public void setEventCreateUserId(String eventCreateUserId) {
        this.eventCreateUserId = eventCreateUserId;
    }

    public String getEventCreateUserName() {
        return eventCreateUserName;
    }

    public void setEventCreateUserName(String eventCreateUserName) {
        this.eventCreateUserName = eventCreateUserName;
    }

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id_</li>
     * <li>fileId -> file_id_</li>
     * <li>filePath -> file_path_</li>
     * <li>editorId -> editor_id_</li>
     * <li>editorName -> editor_name_</li>
     * <li>editTime -> edit_time_</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) { return null; }
        switch (fieldName) {
            case "id": return "id_";
            case "fileId": return "file_id_";
            case "filePath": return "file_path_";
            case "editorId": return "editor_id_";
            case "editorName": return "editor_name_";
            case "editTime": return "edit_time_";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id_ -> id</li>
     * <li>file_id_ -> fileId</li>
     * <li>file_path_ -> filePath</li>
     * <li>editor_id_ -> editorId</li>
     * <li>editor_name_ -> editorName</li>
     * <li>edit_time_ -> editTime</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) { return null; }
        switch (columnName) {
            case "id_": return "id";
            case "file_id_": return "fileId";
            case "file_path_": return "filePath";
            case "editor_id_": return "editorId";
            case "editor_name_": return "editorName";
            case "edit_time_": return "editTime";
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
    public String getFileId() {
        return this.fileId;
    }

    /**  **/
    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    /**  **/
    public String getFilePath() {
        return this.filePath;
    }

    /**  **/
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**  **/
    public String getEditorId() {
        return this.editorId;
    }

    /**  **/
    public void setEditorId(String editorId) {
        this.editorId = editorId;
    }

    /**  **/
    public String getEditorName() {
        return this.editorName;
    }

    /**  **/
    public void setEditorName(String editorName) {
        this.editorName = editorName;
    }

    /**  **/
    public Date getEditTime() {
        return this.editTime;
    }

    /**  **/
    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }

}
