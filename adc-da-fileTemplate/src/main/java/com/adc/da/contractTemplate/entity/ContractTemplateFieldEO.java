package com.adc.da.contractTemplate.entity;

import com.adc.da.base.entity.BaseEntity;

import java.sql.Clob;
import java.util.Date;

/**
 * <b>功能：</b>CONTRACT_TEMPLATE_FIELD ContractTemplateFieldEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-10-29 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class ContractTemplateFieldEO extends BaseEntity {

    private String fileId;
    private String templateJson;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private String tempNo;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>fileId -> file_id</li>
     * <li>templateJson -> template_json</li>
     * <li>createTime -> create_time</li>
     * <li>tempNo -> temp_no</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) { return null; }
        switch (fieldName) {
            case "fileId": return "file_id";
            case "templateJson": return "template_json";
            case "createTime": return "create_time";
            case "tempNo": return "temp_no";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>file_id -> fileId</li>
     * <li>template_json -> templateJson</li>
     * <li>create_time -> createTime</li>
     * <li>temp_no -> tempNo</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) { return null; }
        switch (columnName) {
            case "file_id": return "fileId";
            case "template_json": return "templateJson";
            case "create_time": return "createTime";
            case "temp_no": return "tempNo";
            default: return null;
        }
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
    public String getTemplateJson() {
        return this.templateJson;
    }

    /**  **/
    public void setTemplateJson(String templateJson) {
        this.templateJson = templateJson;
    }

    /**  **/
    public Date getCreateTime() {
        return this.createTime;
    }

    /**  **/
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**  **/
    public String getTempNo() {
        return this.tempNo;
    }

    /**  **/
    public void setTempNo(String tempNo) {
        this.tempNo = tempNo;
    }

}
