package com.adc.da.research.project.entity;

import com.adc.da.base.entity.BaseEntity;


/**
 * <b>功能：</b>RS_RESEARCH_PROJECT_MENU ResearchProjectMenuEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-11-06 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class ResearchProjectMenuEO extends BaseEntity {

    private String urlhref;
    private String parenturl;
    private String extInfo1;
    private String extInfo2;
    private String id;
    private String name;
    private String parentId;
    private String href;
    private String icon;
    private String remarks;
    private String sort;
    private Integer delFlag;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>urlhref -> urlhref</li>
     * <li>parenturl -> parenturl</li>
     * <li>extInfo1 -> ext_info_1_</li>
     * <li>extInfo2 -> ext_info_2_</li>
     * <li>id -> id_</li>
     * <li>name -> name_</li>
     * <li>parentId -> parent_id_</li>
     * <li>href -> href_</li>
     * <li>icon -> icon_</li>
     * <li>remarks -> remarks_</li>
     * <li>sort -> sort_</li>
     * <li>delFlag -> del_flag_</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) { return null; }
        switch (fieldName) {
            case "urlhref": return "urlhref";
            case "parenturl": return "parenturl";
            case "extInfo1": return "ext_info_1_";
            case "extInfo2": return "ext_info_2_";
            case "id": return "id_";
            case "name": return "name_";
            case "parentId": return "parent_id_";
            case "href": return "href_";
            case "icon": return "icon_";
            case "remarks": return "remarks_";
            case "sort": return "sort_";
            case "delFlag": return "del_flag_";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>urlhref -> urlhref</li>
     * <li>parenturl -> parenturl</li>
     * <li>ext_info_1_ -> extInfo1</li>
     * <li>ext_info_2_ -> extInfo2</li>
     * <li>id_ -> id</li>
     * <li>name_ -> name</li>
     * <li>parent_id_ -> parentId</li>
     * <li>href_ -> href</li>
     * <li>icon_ -> icon</li>
     * <li>remarks_ -> remarks</li>
     * <li>sort_ -> sort</li>
     * <li>del_flag_ -> delFlag</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) { return null; }
        switch (columnName) {
            case "urlhref": return "urlhref";
            case "parenturl": return "parenturl";
            case "ext_info_1_": return "extInfo1";
            case "ext_info_2_": return "extInfo2";
            case "id_": return "id";
            case "name_": return "name";
            case "parent_id_": return "parentId";
            case "href_": return "href";
            case "icon_": return "icon";
            case "remarks_": return "remarks";
            case "sort_": return "sort";
            case "del_flag_": return "delFlag";
            default: return null;
        }
    }
    
    /**  **/
    public String getUrlhref() {
        return this.urlhref;
    }

    /**  **/
    public void setUrlhref(String urlhref) {
        this.urlhref = urlhref;
    }

    /**  **/
    public String getParenturl() {
        return this.parenturl;
    }

    /**  **/
    public void setParenturl(String parenturl) {
        this.parenturl = parenturl;
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
    public String getId() {
        return this.id;
    }

    /**  **/
    public void setId(String id) {
        this.id = id;
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
    public String getParentId() {
        return this.parentId;
    }

    /**  **/
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    /**  **/
    public String getHref() {
        return this.href;
    }

    /**  **/
    public void setHref(String href) {
        this.href = href;
    }

    /**  **/
    public String getIcon() {
        return this.icon;
    }

    /**  **/
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**  **/
    public String getRemarks() {
        return this.remarks;
    }

    /**  **/
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**  **/
    public String getSort() {
        return this.sort;
    }

    /**  **/
    public void setSort(String sort) {
        this.sort = sort;
    }

    /**  **/
    public Integer getDelFlag() {
        return this.delFlag;
    }

    /**  **/
    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

}
