package com.adc.da.progress.entity;

import com.adc.da.base.entity.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * <b>功能：</b>PR_PROJECT_RATE ProjectRateEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-07-18 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Data
public class ProjectRateEO extends BaseEntity {

    private String id;

    /**
     * 项目id
     */
    private String projectId;

    /**
     * 流程实例id，
     *
     * 当为-1时，  processNameId 表示 PR_STAGE_ORDER project_type_
     */
    private String procInstId;

    /**
     * 默认表示 PR_PROJECT_NAME ID_  ,
     * 当 procInstId 为 -1 时
     * 表示 PR_STAGE_ORDER project_type_
     */
    private String processNameId;

    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;

    private String delFlag = "0";

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
     * <li>projectId -> project_id_</li>
     * <li>procInstId -> proc_inst_id_</li>
     * <li>processNameId -> process_name_id_</li>
     * <li>createTime -> create_time_</li>
     * <li>modifiedTime -> modified_time</li>
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
            case "projectId":
                return "project_id_";
            case "procInstId":
                return "proc_inst_id_";
            case "processNameId":
                return "process_name_id_";
            case "createTime":
                return "create_time_";
            case "modifiedTime":
                return "modified_time";
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
     * <li>project_id_ -> projectId</li>
     * <li>proc_inst_id_ -> procInstId</li>
     * <li>process_name_id_ -> processNameId</li>
     * <li>create_time_ -> createTime</li>
     * <li>modified_time -> modifiedTime</li>
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
            case "project_id_":
                return "projectId";
            case "proc_inst_id_":
                return "procInstId";
            case "process_name_id_":
                return "processNameId";
            case "create_time_":
                return "createTime";
            case "modified_time":
                return "modifiedTime";
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
