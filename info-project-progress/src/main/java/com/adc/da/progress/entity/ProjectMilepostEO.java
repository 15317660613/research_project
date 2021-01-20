package com.adc.da.progress.entity;

import com.adc.da.base.entity.BaseEntity;
import com.adc.da.progress.vo.ProjectMilepostResultVO;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <b>功能：</b>PR_PROJECT_MILEPOST ProjectMilepostEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-07-29 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Data
public class ProjectMilepostEO extends BaseEntity {

    /**
     * id
     */
    private String id;

    /**
     * 项目id
     */
    private String projectId;

    /**
     * 项目名称（因数据源在es，因此进行Oracle缓存）
     */
    private String projectName;

    /**
     * 里程碑名称
     */
    private String milepostName;

    /**
     * 里程碑目标
     */
    private String milepostTarget;

    /**
     * 里程碑负责人id
     */
    private String milepostManagerId;

    /**
     *   负责人名 由
     */
    private String milepostManagerName;

    /**
     * 里程碑开始时间
     */
    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date milepostBeginTime;

    /**
     * 里程碑结束时间
     */
    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date milepostEndTime;

    /**
     * 版本
     */
    private Integer milepostVersion = 0;

    /**
     * 状态
     */
    private String extInfo1;

    private String extInfo2;

    private String extInfo3;

    private String extInfo4;

    /**
     * 历史名称
     */
    private String extInfo5;

    private String stageId;

    /**
     * 完成时间
     */
    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date finishTime;

    /**
     * 里程碑状态，初始0 ，
     */
    private Integer finishStatus = 0;

    private  String resultName;

    private List<ProjectMilepostResultEO> projectMilepostResultEOList = new ArrayList<>();

    private List<ProjectMilepostResultVO> projectMilepostResultVOList = new ArrayList<>();

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>projectId -> project_id</li>
     * <li>projectName -> project_name</li>
     * <li>milepostName -> milepost_name</li>
     * <li>milepostTarget -> milepost_target</li>
     * <li>milepostManagerId -> milepost_manager_id</li>
     * <li>milepostManagerName -> milepost_manager_name</li>
     * <li>milepostBeginTime -> milepost_begin_time</li>
     * <li>milepostEndTime -> milepost_end_time</li>
     * <li>milepostVersion -> milepost_version</li>
     * <li>extInfo1 -> ext_info1</li>
     * <li>extInfo2 -> ext_info2</li>
     * <li>extInfo3 -> ext_info3</li>
     * <li>extInfo4 -> ext_info4</li>
     * <li>extInfo5 -> ext_info5</li>
     * <li>stageId -> stage_id</li>
     * <li>finishTime -> finish_time</li>
     * <li>finishStatus -> finish_status</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) {
            return null;
        }
        switch (fieldName) {
            case "id":
                return "id";
            case "projectId":
                return "project_id";
            case "projectName":
                return "project_name";
            case "milepostName":
                return "milepost_name";
            case "milepostTarget":
                return "milepost_target";
            case "milepostManagerId":
                return "milepost_manager_id";
            case "milepostManagerName":
                return "milepost_manager_name";
            case "milepostBeginTime":
                return "milepost_begin_time";
            case "milepostEndTime":
                return "milepost_end_time";
            case "milepostVersion":
                return "milepost_version";
            case "extInfo1":
                return "ext_info1";
            case "extInfo2":
                return "ext_info2";
            case "extInfo3":
                return "ext_info3";
            case "extInfo4":
                return "ext_info4";
            case "extInfo5":
                return "ext_info5";
            case "stageId":
                return "stage_id";
            case "finishTime":
                return "finish_time";
            case "finishStatus":
                return "finish_status";
            default:
                return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>project_id -> projectId</li>
     * <li>project_name -> projectName</li>
     * <li>milepost_name -> milepostName</li>
     * <li>milepost_target -> milepostTarget</li>
     * <li>milepost_manager_id -> milepostManagerId</li>
     * <li>milepost_manager_name -> milepostManagerName</li>
     * <li>milepost_begin_time -> milepostBeginTime</li>
     * <li>milepost_end_time -> milepostEndTime</li>
     * <li>milepost_version -> milepostVersion</li>
     * <li>ext_info1 -> extInfo1</li>
     * <li>ext_info2 -> extInfo2</li>
     * <li>ext_info3 -> extInfo3</li>
     * <li>ext_info4 -> extInfo4</li>
     * <li>ext_info5 -> extInfo5</li>
     * <li>stage_id -> stageId</li>
     * <li>finish_time -> finishTime</li>
     * <li>finish_status -> finishStatus</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) {
            return null;
        }
        switch (columnName) {
            case "id":
                return "id";
            case "project_id":
                return "projectId";
            case "project_name":
                return "projectName";
            case "milepost_name":
                return "milepostName";
            case "milepost_target":
                return "milepostTarget";
            case "milepost_manager_id":
                return "milepostManagerId";
            case "milepost_manager_name":
                return "milepostManagerName";
            case "milepost_begin_time":
                return "milepostBeginTime";
            case "milepost_end_time":
                return "milepostEndTime";
            case "milepost_version":
                return "milepostVersion";
            case "ext_info1":
                return "extInfo1";
            case "ext_info2":
                return "extInfo2";
            case "ext_info3":
                return "extInfo3";
            case "ext_info4":
                return "extInfo4";
            case "ext_info5":
                return "extInfo5";
            case "stage_id":
                return "stageId";
            case "finish_time":
                return "finishTime";
            case "finish_status":
                return "finishStatus";
            default:
                return null;
        }
    }

}
