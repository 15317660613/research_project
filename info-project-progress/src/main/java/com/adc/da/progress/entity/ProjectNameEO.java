package com.adc.da.progress.entity;

import com.adc.da.base.entity.BaseEntity;
import com.adc.da.excel.annotation.Excel;
import lombok.Getter;
import lombok.Setter;

/**
 * <b>功能：</b>PR_PROJECT_NAME ProjectNameEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-07-18 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Getter
@Setter
public class ProjectNameEO extends BaseEntity {

    private String extInfo6;

    private String id;

    @Excel(name = "STAGE_ORDER_ID_", orderNum = "1")
    private String stageOrderId;

    @Excel(name = "PROC_NAME_", orderNum = "2")
    private String procName;

    @Excel(name = "PROC_DEF_KEY_", orderNum = "3")
    private String procDefKey;

    @Excel(name = "DEL_FLAG_", orderNum = "4")
    private String delFlag;

    /**
     * 排序字段
     */
    private String extInfo1;

    /**
     * 发起次数限制字段 ，
     * 默认为null，
     * 0为非流程回显已完成
     */
    private Integer extInfo2 = -1;

    private String extInfo3;

    private String extInfo4;

    private String extInfo5;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>extInfo6 -> ext_info_6_</li>
     * <li>id -> id_</li>
     * <li>stageOrderId -> stage_order_id_</li>
     * <li>procName -> proc_name_</li>
     * <li>procDefKey -> proc_def_key_</li>
     * <li>delFlag -> del_flag_</li>
     * <li>extInfo1 -> ext_info_1_</li>
     * <li>extInfo2 -> ext_info_2_</li>
     * <li>extInfo3 -> ext_info_3_</li>
     * <li>extInfo4 -> ext_info_4_</li>
     * <li>extInfo5 -> ext_info_5_</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) {
            return null;
        }
        switch (fieldName) {
            case "extInfo6":
                return "ext_info_6_";
            case "id":
                return "id_";
            case "stageOrderId":
                return "stage_order_id_";
            case "procName":
                return "proc_name_";
            case "procDefKey":
                return "proc_def_key_";
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
            default:
                return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>ext_info_6_ -> extInfo6</li>
     * <li>id_ -> id</li>
     * <li>stage_order_id_ -> stageOrderId</li>
     * <li>proc_name_ -> procName</li>
     * <li>proc_def_key_ -> procDefKey</li>
     * <li>del_flag_ -> delFlag</li>
     * <li>ext_info_1_ -> extInfo1</li>
     * <li>ext_info_2_ -> extInfo2</li>
     * <li>ext_info_3_ -> extInfo3</li>
     * <li>ext_info_4_ -> extInfo4</li>
     * <li>ext_info_5_ -> extInfo5</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) {
            return null;
        }
        switch (columnName) {
            case "ext_info_6_":
                return "extInfo6";
            case "id_":
                return "id";
            case "stage_order_id_":
                return "stageOrderId";
            case "proc_name_":
                return "procName";
            case "proc_def_key_":
                return "procDefKey";
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
            default:
                return null;
        }
    }

}
