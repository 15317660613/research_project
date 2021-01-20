package com.adc.da.processform.entity;

import com.adc.da.base.entity.BaseEntity;


/**
 * <b>功能：</b>PF_FORM_FILE FormFileEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-06-30 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class FormFileEO extends BaseEntity {

    private String id;
    private String processinstid;
    private String fileid;
    private String delflag;
    private String filebelong;
    private String ext01;
    private String ext02;
    private String ext03;
    private String ext04;
    private String ext05;
    private String ext06;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>processinstid -> processinstid</li>
     * <li>fileid -> fileid</li>
     * <li>delflag -> delflag</li>
     * <li>filebelong -> filebelong</li>
     * <li>ext01 -> ext_01</li>
     * <li>ext02 -> ext_02</li>
     * <li>ext03 -> ext_03</li>
     * <li>ext04 -> ext_04</li>
     * <li>ext05 -> ext_05</li>
     * <li>ext06 -> ext_06</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) { return null; }
        switch (fieldName) {
            case "id": return "id";
            case "processinstid": return "processinstid";
            case "fileid": return "fileid";
            case "delflag": return "delflag";
            case "filebelong": return "filebelong";
            case "ext01": return "ext_01";
            case "ext02": return "ext_02";
            case "ext03": return "ext_03";
            case "ext04": return "ext_04";
            case "ext05": return "ext_05";
            case "ext06": return "ext_06";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>processinstid -> processinstid</li>
     * <li>fileid -> fileid</li>
     * <li>delflag -> delflag</li>
     * <li>filebelong -> filebelong</li>
     * <li>ext_01 -> ext01</li>
     * <li>ext_02 -> ext02</li>
     * <li>ext_03 -> ext03</li>
     * <li>ext_04 -> ext04</li>
     * <li>ext_05 -> ext05</li>
     * <li>ext_06 -> ext06</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) { return null; }
        switch (columnName) {
            case "id": return "id";
            case "processinstid": return "processinstid";
            case "fileid": return "fileid";
            case "delflag": return "delflag";
            case "filebelong": return "filebelong";
            case "ext_01": return "ext01";
            case "ext_02": return "ext02";
            case "ext_03": return "ext03";
            case "ext_04": return "ext04";
            case "ext_05": return "ext05";
            case "ext_06": return "ext06";
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
    public String getProcessinstid() {
        return this.processinstid;
    }

    /**  **/
    public void setProcessinstid(String processinstid) {
        this.processinstid = processinstid;
    }

    /**  **/
    public String getFileid() {
        return this.fileid;
    }

    /**  **/
    public void setFileid(String fileid) {
        this.fileid = fileid;
    }

    /**  **/
    public String getDelflag() {
        return this.delflag;
    }

    /**  **/
    public void setDelflag(String delflag) {
        this.delflag = delflag;
    }

    /**  **/
    public String getFilebelong() {
        return this.filebelong;
    }

    /**  **/
    public void setFilebelong(String filebelong) {
        this.filebelong = filebelong;
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

}
