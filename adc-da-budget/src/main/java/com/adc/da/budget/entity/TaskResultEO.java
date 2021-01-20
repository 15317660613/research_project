package com.adc.da.budget.entity;

import com.adc.da.base.entity.BaseEntity;

import java.util.Date;

/**
 * <b>功能：</b>PF_TASK_RESULT TaskResultEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-09-04 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class TaskResultEO extends BaseEntity {

    private String id; //id
    private String taskId; //任务/子任务 id
    private String resultName; // 交付物名称
    private String resultType; //交付物类型
    private String resultDescription; // 交付物描述
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime; //创建日期
    private String extInfo1;
    private String extInfo2;
    private String extInfo3;
    private String extInfo4;
    private String extInfo5;
    private String extInfo6;
    private String delFlag; // 是否被删除
    private Integer taskType; //任务类型 1： 任务 2： 子任务

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id_</li>
     * <li>taskId -> task_id</li>
     * <li>resultName -> result_name</li>
     * <li>resultType -> result_type</li>
     * <li>resultDescription -> result_description</li>
     * <li>createTime -> create_time</li>
     * <li>extInfo1 -> ext_info1</li>
     * <li>extInfo2 -> ext_info2</li>
     * <li>extInfo3 -> ext_info3</li>
     * <li>extInfo4 -> ext_info4</li>
     * <li>extInfo5 -> ext_info5</li>
     * <li>extInfo6 -> ext_info6</li>
     * <li>delFlag -> del_flag</li>
     * <li>taskType -> task_type</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) { return null; }
        switch (fieldName) {
            case "id": return "id_";
            case "taskId": return "task_id";
            case "resultName": return "result_name";
            case "resultType": return "result_type";
            case "resultDescription": return "result_description";
            case "createTime": return "create_time";
            case "extInfo1": return "ext_info1";
            case "extInfo2": return "ext_info2";
            case "extInfo3": return "ext_info3";
            case "extInfo4": return "ext_info4";
            case "extInfo5": return "ext_info5";
            case "extInfo6": return "ext_info6";
            case "delFlag": return "del_flag";
            case "taskType": return "task_type";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id_ -> id</li>
     * <li>task_id -> taskId</li>
     * <li>result_name -> resultName</li>
     * <li>result_type -> resultType</li>
     * <li>result_description -> resultDescription</li>
     * <li>create_time -> createTime</li>
     * <li>ext_info1 -> extInfo1</li>
     * <li>ext_info2 -> extInfo2</li>
     * <li>ext_info3 -> extInfo3</li>
     * <li>ext_info4 -> extInfo4</li>
     * <li>ext_info5 -> extInfo5</li>
     * <li>ext_info6 -> extInfo6</li>
     * <li>del_flag -> delFlag</li>
     * <li>task_type -> taskType</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) { return null; }
        switch (columnName) {
            case "id_": return "id";
            case "task_id": return "taskId";
            case "result_name": return "resultName";
            case "result_type": return "resultType";
            case "result_description": return "resultDescription";
            case "create_time": return "createTime";
            case "ext_info1": return "extInfo1";
            case "ext_info2": return "extInfo2";
            case "ext_info3": return "extInfo3";
            case "ext_info4": return "extInfo4";
            case "ext_info5": return "extInfo5";
            case "ext_info6": return "extInfo6";
            case "del_flag": return "delFlag";
            case "task_type": return "taskType";
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
    public String getTaskId() {
        return this.taskId;
    }

    /**  **/
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    /**  **/
    public String getResultName() {
        return this.resultName;
    }

    /**  **/
    public void setResultName(String resultName) {
        this.resultName = resultName;
    }

    /**  **/
    public String getResultType() {
        return this.resultType;
    }

    /**  **/
    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    /**  **/
    public String getResultDescription() {
        return this.resultDescription;
    }

    /**  **/
    public void setResultDescription(String resultDescription) {
        this.resultDescription = resultDescription;
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

    /**  **/
    public String getDelFlag() {
        return this.delFlag;
    }

    /**  **/
    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    /**  **/
    public Integer getTaskType() {
        return this.taskType;
    }

    /**  **/
    public void setTaskType(Integer taskType) {
        this.taskType = taskType;
    }

}
