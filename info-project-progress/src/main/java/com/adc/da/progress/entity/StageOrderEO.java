package com.adc.da.progress.entity;

import com.adc.da.base.entity.BaseEntity;
import com.adc.da.excel.annotation.Excel;
import lombok.Data;

/**
 * <b>功能：</b>PR_STAGE_ORDER StageOrderEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-07-18 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Data
public class StageOrderEO extends BaseEntity {

    private String id;

    @Excel(name = "PROJECT_TYPE", orderNum = "1")
    private String projectType;

    @Excel(name = "STAGE_NAME_", orderNum = "2")
    private String stageName;

    @Excel(name = "LEVEL_", orderNum = "3")
    private Integer level;

    @Excel(name = "DEL_FLAG_", orderNum = "4")
    private String delFlag;

    private String extInfo1;

    private String extInfo2;

    private String extInfo3;

    private String extInfo4;

    private String extInfo5;

    private String extInfo6;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id_</li>
     * <li>projectType -> project_type</li>
     * <li>stageName -> stage_name_</li>
     * <li>level -> level_</li>
     * <li>delFlag -> del_flag_</li>
     * <li>extInfo1 -> ext_info_1_</li>
     * <li>extInfo2 -> ext_info_2_</li>
     * <li>extInfo3 -> ext_info_3_</li>
     * <li>extInfo4 -> ext_info_4_</li>
     * <li>extInfo5 -> ext_info_5_</li>
     * <li>extInfo6 -> ext_info_6_</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) {
            return null;
        }
        switch (fieldName) {
            case "id":
                return "id_";
            case "projectType":
                return "project_type";
            case "stageName":
                return "stage_name_";
            case "level":
                return "level_";
            case "delFlag":
                return "del_flag_";
            case "extInfo1":
                return "ext_info_1_";
            case "extInfo2":
                return "ext_info_2_";
            case "extInfo3":
                return "ext_info_3_";
            case "extInfo4":
                return "ext_info_4_";
            case "extInfo5":
                return "ext_info_5_";
            case "extInfo6":
                return "ext_info_6_";
            default:
                return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id_ -> id</li>
     * <li>project_type -> projectType</li>
     * <li>stage_name_ -> stageName</li>
     * <li>level_ -> level</li>
     * <li>del_flag_ -> delFlag</li>
     * <li>ext_info_1_ -> extInfo1</li>
     * <li>ext_info_2_ -> extInfo2</li>
     * <li>ext_info_3_ -> extInfo3</li>
     * <li>ext_info_4_ -> extInfo4</li>
     * <li>ext_info_5_ -> extInfo5</li>
     * <li>ext_info_6_ -> extInfo6</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) {
            return null;
        }
        switch (columnName) {
            case "id_":
                return "id";
            case "project_type":
                return "projectType";
            case "stage_name_":
                return "stageName";
            case "level_":
                return "level";
            case "del_flag_":
                return "delFlag";
            case "ext_info_1_":
                return "extInfo1";
            case "ext_info_2_":
                return "extInfo2";
            case "ext_info_3_":
                return "extInfo3";
            case "ext_info_4_":
                return "extInfo4";
            case "ext_info_5_":
                return "extInfo5";
            case "ext_info_6_":
                return "extInfo6";
            default:
                return null;
        }
    }

}
