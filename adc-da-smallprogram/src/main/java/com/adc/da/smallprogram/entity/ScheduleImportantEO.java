package com.adc.da.smallprogram.entity;

import com.adc.da.base.entity.BaseEntity;

import java.util.Date;

/**
 * <b>功能：</b>TS_SCHEDULE_IMPORTANT ScheduleImportantEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-12-11 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class ScheduleImportantEO extends BaseEntity {

    private String name;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date beginTime;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    private String createUserId;
    private Date createTime;
    private String fileId;
    private String id;
    private String extInfo1;
    private String extInfo2;
    private String extInfo3;
    private String extInfo4;
    private String extInfo5;
    private String extInfo6;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>name -> name_</li>
     * <li>beginTime -> begin_time_</li>
     * <li>endTime -> end_time_</li>
     * <li>createUserId -> create_user_id_</li>
     * <li>createTime -> create_time_</li>
     * <li>fileId -> file_id_</li>
     * <li>id -> id_</li>
     * <li>extInfo1 -> ext_info1_</li>
     * <li>extInfo2 -> ext_info2_</li>
     * <li>extInfo3 -> ext_info3_</li>
     * <li>extInfo4 -> ext_info4_</li>
     * <li>extInfo5 -> ext_info5_</li>
     * <li>extInfo6 -> ext_info6_</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) { return null; }
        switch (fieldName) {
            case "name": return "name_";
            case "beginTime": return "begin_time_";
            case "endTime": return "end_time_";
            case "createUserId": return "create_user_id_";
            case "createTime": return "create_time_";
            case "fileId": return "file_id_";
            case "id": return "id_";
            case "extInfo1": return "ext_info1_";
            case "extInfo2": return "ext_info2_";
            case "extInfo3": return "ext_info3_";
            case "extInfo4": return "ext_info4_";
            case "extInfo5": return "ext_info5_";
            case "extInfo6": return "ext_info6_";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>name_ -> name</li>
     * <li>begin_time_ -> beginTime</li>
     * <li>end_time_ -> endTime</li>
     * <li>create_user_id_ -> createUserId</li>
     * <li>create_time_ -> createTime</li>
     * <li>file_id_ -> fileId</li>
     * <li>id_ -> id</li>
     * <li>ext_info1_ -> extInfo1</li>
     * <li>ext_info2_ -> extInfo2</li>
     * <li>ext_info3_ -> extInfo3</li>
     * <li>ext_info4_ -> extInfo4</li>
     * <li>ext_info5_ -> extInfo5</li>
     * <li>ext_info6_ -> extInfo6</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) { return null; }
        switch (columnName) {
            case "name_": return "name";
            case "begin_time_": return "beginTime";
            case "end_time_": return "endTime";
            case "create_user_id_": return "createUserId";
            case "create_time_": return "createTime";
            case "file_id_": return "fileId";
            case "id_": return "id";
            case "ext_info1_": return "extInfo1";
            case "ext_info2_": return "extInfo2";
            case "ext_info3_": return "extInfo3";
            case "ext_info4_": return "extInfo4";
            case "ext_info5_": return "extInfo5";
            case "ext_info6_": return "extInfo6";
            default: return null;
        }
    }

    /**  **/
    public String getName() {
        return this.name;
    }

    /**  **/
    public void setName(String name) {
        this.name = name;
    }

    /**  **/
    public Date getBeginTime() {
        return this.beginTime;
    }

    /**  **/
    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    /**  **/
    public Date getEndTime() {
        return this.endTime;
    }

    /**  **/
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**  **/
    public String getCreateUserId() {
        return this.createUserId;
    }

    /**  **/
    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
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
    public String getFileId() {
        return this.fileId;
    }

    /**  **/
    public void setFileId(String fileId) {
        this.fileId = fileId;
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
    public String getExtInfo1() {
        return this.extInfo1;
    }

    /**  **/
    public void setExtInfo1(String extInfo1) {
        this.extInfo1 = extInfo1;
    }

    /**  **/
    public String getExtInfo2() {
        return this.extInfo2;
    }

    /**  **/
    public void setExtInfo2(String extInfo2) {
        this.extInfo2 = extInfo2;
    }

    /**  **/
    public String getExtInfo3() {
        return this.extInfo3;
    }

    /**  **/
    public void setExtInfo3(String extInfo3) {
        this.extInfo3 = extInfo3;
    }

    /**  **/
    public String getExtInfo4() {
        return this.extInfo4;
    }

    /**  **/
    public void setExtInfo4(String extInfo4) {
        this.extInfo4 = extInfo4;
    }

    /**  **/
    public String getExtInfo5() {
        return this.extInfo5;
    }

    /**  **/
    public void setExtInfo5(String extInfo5) {
        this.extInfo5 = extInfo5;
    }

    /**  **/
    public String getExtInfo6() {
        return this.extInfo6;
    }

    /**  **/
    public void setExtInfo6(String extInfo6) {
        this.extInfo6 = extInfo6;
    }

}
