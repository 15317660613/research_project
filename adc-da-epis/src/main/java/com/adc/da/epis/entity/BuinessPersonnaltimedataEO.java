package com.adc.da.epis.entity;

import com.adc.da.base.entity.BaseEntity;

import java.util.Date;

/**
 * <b>功能：</b>BUINESS_PERSONNALTIMEDATA BuinessPersonnaltimedataEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-11-07 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class BuinessPersonnaltimedataEO extends BaseEntity {

    private String timedataId;
    private String peojectId;
    private String personnalId;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date timedataMouth;
    private Integer timedataActoal;
    private Integer timedataNormal;
    private String inputType;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date creattime;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updatetime;
    private String creatuser;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>timedataId -> timedata_id</li>
     * <li>peojectId -> peoject_id</li>
     * <li>personnalId -> personnal_id</li>
     * <li>timedataMouth -> timedata_mouth</li>
     * <li>timedataActoal -> timedata_actoal</li>
     * <li>timedataNormal -> timedata_normal</li>
     * <li>inputType -> input_type</li>
     * <li>creattime -> creattime</li>
     * <li>updatetime -> updatetime</li>
     * <li>creatuser -> creatuser</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) {return null;}
        switch (fieldName) {
            case "timedataId": return "timedata_id";
            case "peojectId": return "peoject_id";
            case "personnalId": return "personnal_id";
            case "timedataMouth": return "timedata_mouth";
            case "timedataActoal": return "timedata_actoal";
            case "timedataNormal": return "timedata_normal";
            case "inputType": return "input_type";
            case "creattime": return "creattime";
            case "updatetime": return "updatetime";
            case "creatuser": return "creatuser";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>timedata_id -> timedataId</li>
     * <li>peoject_id -> peojectId</li>
     * <li>personnal_id -> personnalId</li>
     * <li>timedata_mouth -> timedataMouth</li>
     * <li>timedata_actoal -> timedataActoal</li>
     * <li>timedata_normal -> timedataNormal</li>
     * <li>input_type -> inputType</li>
     * <li>creattime -> creattime</li>
     * <li>updatetime -> updatetime</li>
     * <li>creatuser -> creatuser</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) {return null;}
        switch (columnName) {
            case "timedata_id": return "timedataId";
            case "peoject_id": return "peojectId";
            case "personnal_id": return "personnalId";
            case "timedata_mouth": return "timedataMouth";
            case "timedata_actoal": return "timedataActoal";
            case "timedata_normal": return "timedataNormal";
            case "input_type": return "inputType";
            case "creattime": return "creattime";
            case "updatetime": return "updatetime";
            case "creatuser": return "creatuser";
            default: return null;
        }
    }
    
    /**  **/
    public String getTimedataId() {
        return this.timedataId;
    }

    /**  **/
    public void setTimedataId(String timedataId) {
        this.timedataId = timedataId;
    }

    /**  **/
    public String getPeojectId() {
        return this.peojectId;
    }

    /**  **/
    public void setPeojectId(String peojectId) {
        this.peojectId = peojectId;
    }

    /**  **/
    public String getPersonnalId() {
        return this.personnalId;
    }

    /**  **/
    public void setPersonnalId(String personnalId) {
        this.personnalId = personnalId;
    }

    /**  **/
    public Date getTimedataMouth() {
        return this.timedataMouth;
    }

    /**  **/
    public void setTimedataMouth(Date timedataMouth) {
        this.timedataMouth = timedataMouth;
    }

    /**  **/
    public Integer getTimedataActoal() {
        return this.timedataActoal;
    }

    /**  **/
    public void setTimedataActoal(Integer timedataActoal) {
        this.timedataActoal = timedataActoal;
    }

    /**  **/
    public Integer getTimedataNormal() {
        return this.timedataNormal;
    }

    /**  **/
    public void setTimedataNormal(Integer timedataNormal) {
        this.timedataNormal = timedataNormal;
    }

    /**  **/
    public String getInputType() {
        return this.inputType;
    }

    /**  **/
    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    /**  **/
    public Date getCreattime() {
        return this.creattime;
    }

    /**  **/
    public void setCreattime(Date creattime) {
        this.creattime = creattime;
    }

    /**  **/
    public Date getUpdatetime() {
        return this.updatetime;
    }

    /**  **/
    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    /**  **/
    public String getCreatuser() {
        return this.creatuser;
    }

    /**  **/
    public void setCreatuser(String creatuser) {
        this.creatuser = creatuser;
    }

}
