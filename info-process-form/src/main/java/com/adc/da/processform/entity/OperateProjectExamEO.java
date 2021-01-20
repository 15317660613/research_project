package com.adc.da.processform.entity;

import com.adc.da.base.entity.BaseEntity;

import java.util.Date;

/**
 * <b>功能：</b>PF_OPERATE_PROJECT_EXAM OperateProjectExamEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-06-26 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class OperateProjectExamEO extends BaseEntity {

    private String projectid;
    private String projectname;
    private String projectbaerdeptid;
    private String projectbaerdeptname;
    private String projectmanagerid;
    private String projectmanagername;
    private String projectbasedetail;
    private String projectimportantpoint;
    private String projectsuggestionbookfileid;
    private String projectbaerdeptsuggestion;
    private String riskimportantpoint;
    private String riskmanagergroupsuggestion;
    private String bigdeptsuggestion;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date formstarttime;
    private String ext01;
    private String ext02;
    private String ext03;
    private String ext04;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date formupdatetime;
    private String ext05;
    private String ext06;
    private String ext07;
    private String status;
    private String version;
    private Integer delflag;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>projectid -> projectid</li>
     * <li>projectname -> projectname</li>
     * <li>projectbaerdeptid -> projectbaerdeptid</li>
     * <li>projectbaerdeptname -> projectbaerdeptname</li>
     * <li>projectmanagerid -> projectmanagerid</li>
     * <li>projectmanagername -> projectmanagername</li>
     * <li>projectbasedetail -> projectbasedetail</li>
     * <li>projectimportantpoint -> projectimportantpoint</li>
     * <li>projectsuggestionbookfileid -> projectsuggestionbookfileid</li>
     * <li>projectbaerdeptsuggestion -> projectbaerdeptsuggestion</li>
     * <li>riskimportantpoint -> riskimportantpoint</li>
     * <li>riskmanagergroupsuggestion -> riskmanagergroupsuggestion</li>
     * <li>bigdeptsuggestion -> bigdeptsuggestion</li>
     * <li>formstarttime -> formstarttime</li>
     * <li>ext01 -> ext_01</li>
     * <li>ext02 -> ext_02</li>
     * <li>ext03 -> ext_03</li>
     * <li>ext04 -> ext_04</li>
     * <li>formupdatetime -> formupdatetime</li>
     * <li>ext05 -> ext_05</li>
     * <li>ext06 -> ext_06</li>
     * <li>ext07 -> ext_07</li>
     * <li>status -> status</li>
     * <li>version -> version</li>
     * <li>delflag -> delflag</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) { return null; }
        switch (fieldName) {
            case "projectid": return "projectid";
            case "projectname": return "projectname";
            case "projectbaerdeptid": return "projectbaerdeptid";
            case "projectbaerdeptname": return "projectbaerdeptname";
            case "projectmanagerid": return "projectmanagerid";
            case "projectmanagername": return "projectmanagername";
            case "projectbasedetail": return "projectbasedetail";
            case "projectimportantpoint": return "projectimportantpoint";
            case "projectsuggestionbookfileid": return "projectsuggestionbookfileid";
            case "projectbaerdeptsuggestion": return "projectbaerdeptsuggestion";
            case "riskimportantpoint": return "riskimportantpoint";
            case "riskmanagergroupsuggestion": return "riskmanagergroupsuggestion";
            case "bigdeptsuggestion": return "bigdeptsuggestion";
            case "formstarttime": return "formstarttime";
            case "ext01": return "ext_01";
            case "ext02": return "ext_02";
            case "ext03": return "ext_03";
            case "ext04": return "ext_04";
            case "formupdatetime": return "formupdatetime";
            case "ext05": return "ext_05";
            case "ext06": return "ext_06";
            case "ext07": return "ext_07";
            case "status": return "status";
            case "version": return "version";
            case "delflag": return "delflag";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>projectid -> projectid</li>
     * <li>projectname -> projectname</li>
     * <li>projectbaerdeptid -> projectbaerdeptid</li>
     * <li>projectbaerdeptname -> projectbaerdeptname</li>
     * <li>projectmanagerid -> projectmanagerid</li>
     * <li>projectmanagername -> projectmanagername</li>
     * <li>projectbasedetail -> projectbasedetail</li>
     * <li>projectimportantpoint -> projectimportantpoint</li>
     * <li>projectsuggestionbookfileid -> projectsuggestionbookfileid</li>
     * <li>projectbaerdeptsuggestion -> projectbaerdeptsuggestion</li>
     * <li>riskimportantpoint -> riskimportantpoint</li>
     * <li>riskmanagergroupsuggestion -> riskmanagergroupsuggestion</li>
     * <li>bigdeptsuggestion -> bigdeptsuggestion</li>
     * <li>formstarttime -> formstarttime</li>
     * <li>ext_01 -> ext01</li>
     * <li>ext_02 -> ext02</li>
     * <li>ext_03 -> ext03</li>
     * <li>ext_04 -> ext04</li>
     * <li>formupdatetime -> formupdatetime</li>
     * <li>ext_05 -> ext05</li>
     * <li>ext_06 -> ext06</li>
     * <li>ext_07 -> ext07</li>
     * <li>status -> status</li>
     * <li>version -> version</li>
     * <li>delflag -> delflag</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) { return null; }
        switch (columnName) {
            case "projectid": return "projectid";
            case "projectname": return "projectname";
            case "projectbaerdeptid": return "projectbaerdeptid";
            case "projectbaerdeptname": return "projectbaerdeptname";
            case "projectmanagerid": return "projectmanagerid";
            case "projectmanagername": return "projectmanagername";
            case "projectbasedetail": return "projectbasedetail";
            case "projectimportantpoint": return "projectimportantpoint";
            case "projectsuggestionbookfileid": return "projectsuggestionbookfileid";
            case "projectbaerdeptsuggestion": return "projectbaerdeptsuggestion";
            case "riskimportantpoint": return "riskimportantpoint";
            case "riskmanagergroupsuggestion": return "riskmanagergroupsuggestion";
            case "bigdeptsuggestion": return "bigdeptsuggestion";
            case "formstarttime": return "formstarttime";
            case "ext_01": return "ext01";
            case "ext_02": return "ext02";
            case "ext_03": return "ext03";
            case "ext_04": return "ext04";
            case "formupdatetime": return "formupdatetime";
            case "ext_05": return "ext05";
            case "ext_06": return "ext06";
            case "ext_07": return "ext07";
            case "status": return "status";
            case "version": return "version";
            case "delflag": return "delflag";
            default: return null;
        }
    }
    
    /**  **/
    public String getProjectid() {
        return this.projectid;
    }

    /**  **/
    public void setProjectid(String projectid) {
        this.projectid = projectid;
    }

    /**  **/
    public String getProjectname() {
        return this.projectname;
    }

    /**  **/
    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }

    /**  **/
    public String getProjectbaerdeptid() {
        return this.projectbaerdeptid;
    }

    /**  **/
    public void setProjectbaerdeptid(String projectbaerdeptid) {
        this.projectbaerdeptid = projectbaerdeptid;
    }

    /**  **/
    public String getProjectbaerdeptname() {
        return this.projectbaerdeptname;
    }

    /**  **/
    public void setProjectbaerdeptname(String projectbaerdeptname) {
        this.projectbaerdeptname = projectbaerdeptname;
    }

    /**  **/
    public String getProjectmanagerid() {
        return this.projectmanagerid;
    }

    /**  **/
    public void setProjectmanagerid(String projectmanagerid) {
        this.projectmanagerid = projectmanagerid;
    }

    /**  **/
    public String getProjectmanagername() {
        return this.projectmanagername;
    }

    /**  **/
    public void setProjectmanagername(String projectmanagername) {
        this.projectmanagername = projectmanagername;
    }

    /**  **/
    public String getProjectbasedetail() {
        return this.projectbasedetail;
    }

    /**  **/
    public void setProjectbasedetail(String projectbasedetail) {
        this.projectbasedetail = projectbasedetail;
    }

    /**  **/
    public String getProjectimportantpoint() {
        return this.projectimportantpoint;
    }

    /**  **/
    public void setProjectimportantpoint(String projectimportantpoint) {
        this.projectimportantpoint = projectimportantpoint;
    }

    /**  **/
    public String getProjectsuggestionbookfileid() {
        return this.projectsuggestionbookfileid;
    }

    /**  **/
    public void setProjectsuggestionbookfileid(String projectsuggestionbookfileid) {
        this.projectsuggestionbookfileid = projectsuggestionbookfileid;
    }

    /**  **/
    public String getProjectbaerdeptsuggestion() {
        return this.projectbaerdeptsuggestion;
    }

    /**  **/
    public void setProjectbaerdeptsuggestion(String projectbaerdeptsuggestion) {
        this.projectbaerdeptsuggestion = projectbaerdeptsuggestion;
    }

    /**  **/
    public String getRiskimportantpoint() {
        return this.riskimportantpoint;
    }

    /**  **/
    public void setRiskimportantpoint(String riskimportantpoint) {
        this.riskimportantpoint = riskimportantpoint;
    }

    /**  **/
    public String getRiskmanagergroupsuggestion() {
        return this.riskmanagergroupsuggestion;
    }

    /**  **/
    public void setRiskmanagergroupsuggestion(String riskmanagergroupsuggestion) {
        this.riskmanagergroupsuggestion = riskmanagergroupsuggestion;
    }

    /**  **/
    public String getBigdeptsuggestion() {
        return this.bigdeptsuggestion;
    }

    /**  **/
    public void setBigdeptsuggestion(String bigdeptsuggestion) {
        this.bigdeptsuggestion = bigdeptsuggestion;
    }

    /**  **/
    public Date getFormstarttime() {
        return this.formstarttime;
    }

    /**  **/
    public void setFormstarttime(Date formstarttime) {
        this.formstarttime = formstarttime;
    }

    /**  **/
    public String getExt01() {
        return this.ext01;
    }

    /**  **/
    public void setExt01(String ext01) {
        this.ext01 = ext01;
    }

    /**  **/
    public String getExt02() {
        return this.ext02;
    }

    /**  **/
    public void setExt02(String ext02) {
        this.ext02 = ext02;
    }

    /**  **/
    public String getExt03() {
        return this.ext03;
    }

    /**  **/
    public void setExt03(String ext03) {
        this.ext03 = ext03;
    }

    /**  **/
    public String getExt04() {
        return this.ext04;
    }

    /**  **/
    public void setExt04(String ext04) {
        this.ext04 = ext04;
    }

    /**  **/
    public Date getFormupdatetime() {
        return this.formupdatetime;
    }

    /**  **/
    public void setFormupdatetime(Date formupdatetime) {
        this.formupdatetime = formupdatetime;
    }

    /**  **/
    public String getExt05() {
        return this.ext05;
    }

    /**  **/
    public void setExt05(String ext05) {
        this.ext05 = ext05;
    }

    /**  **/
    public String getExt06() {
        return this.ext06;
    }

    /**  **/
    public void setExt06(String ext06) {
        this.ext06 = ext06;
    }

    /**  **/
    public String getExt07() {
        return this.ext07;
    }

    /**  **/
    public void setExt07(String ext07) {
        this.ext07 = ext07;
    }

    /**  **/
    public String getStatus() {
        return this.status;
    }

    /**  **/
    public void setStatus(String status) {
        this.status = status;
    }

    /**  **/
    public String getVersion() {
        return this.version;
    }

    /**  **/
    public void setVersion(String version) {
        this.version = version;
    }

    /**  **/
    public Integer getDelflag() {
        return this.delflag;
    }

    /**  **/
    public void setDelflag(Integer delflag) {
        this.delflag = delflag;
    }

}
