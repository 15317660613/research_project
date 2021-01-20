package com.adc.da.research.entity;

import com.adc.da.base.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

import static com.adc.da.research.utils.RSPendingStatus.RS_DECLARATION_PENDING;

/**
 * <b>功能：</b>RS_PROJECT_DECLARATION DeclarationEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-11-26 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 *
 * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
 * <p>字段列表：</p>
 * <li>id -> id_</li>
 * <li>projectName -> project_name_</li>
 * <li>leaderId -> leader_id_</li>
 * <li>deptId -> dept_id_</li>
 * <li>projectTypeId -> project_type_id_</li>
 * <li>undertakingId -> undertaking_id_</li>
 * <li>createTime -> create_time_</li>
 * <li>beginTime -> begin_time_</li>
 * <li>endTime -> end_time_</li>
 * <li>timeArea -> time_area_</li>
 * <li>amount -> amount_</li>
 * <li>summaryDoc01 -> summary_doc_01_</li>
 * <li>summaryDoc02 -> summary_doc_02_</li>
 * <li>summaryDoc03 -> summary_doc_03_</li>
 * <li>summaryDoc04 -> summary_doc_04_</li>
 * <li>summaryDoc05 -> summary_doc_05_</li>
 * <li>delFlag -> del_flag_</li>
 * <li>status -> status_</li>
 * <li>extInfo1 -> ext_info_1_</li>
 * <li>extInfo2 -> ext_info_2_</li>
 * <li>extInfo3 -> ext_info_3_</li>
 * <li>extInfo4 -> ext_info_4_</li>
 * <li>extInfo5 -> ext_info_5_</li>
 * <li>extInfo6 -> ext_info_6_</li>
 */
@Getter
@Setter
public class DeclarationEO extends BaseEntity {
    /**
     * 负责人名称，需要联查
     */
    private String leaderName;

    /**
     * 创建人人名称，需要联查
     */
    private String creatorName;

    /**
     * 需要联查 部门名称
     */
    private String deptName;

    /***/
    private String id;

    /***/
    private String projectName;

    /***/
    private String leaderId;

    /***/
    private String deptId;

    /**
     * 项目类型 - id
     */
    private String projectTypeId;

    /**
     * 项目类型 - 数据库联查
     */
    private String projectTypeName;

    /**
     * 承办方式 - id
     */
    private String undertakingId;

    /**
     * 承办方式 - 数据库联查
     */
    private String undertakingName;

    /***/
    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /***/
    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;

    /***/
    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**
     * 时间范围
     */
    private String timeArea;

    /**
     * 金额 double 型
     *
     * @see #extInfo5
     */
    private Double amount;

    /***/
    private String summaryDoc01;

    /***/
    private String summaryDoc02;

    /***/
    private String summaryDoc03;

    /***/
    private String summaryDoc04;

    /***/
    private String summaryDoc05;

    /**
     * 0为正常
     * 1为已删除
     */
    private Integer delFlag = 0;

    /**
     * 0 表示可编辑
     *
     * @see com.adc.da.research.utils.RSPendingStatus
     */
    private Integer status = RS_DECLARATION_PENDING;

    /**
     * 具体说明
     */
    private String extInfo1;

    /**
     * 承办方式说明  表示项目类型
     */
    private String extInfo2;

    /**
     * 导出时会覆盖该字段值
     */
    private String extInfo3;

    /***/
    private String extInfo4;

    /**
     * amount的str型
     */
    private String extInfo5;

    /**
     * 创建人
     */
    private String extInfo6;

    /**
     * 提交时间
     */
    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date submissionTime;

    /**
     * 类 转 数据库字段
     *
     * @param fieldName
     * @return
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) {
            return null;
        }
        switch (fieldName) {
            case "id":
                return "id_";
            case "projectName":
                return "project_name_";
            case "leaderId":
                return "leader_id_";
            case "deptId":
                return "dept_id_";
            case "projectTypeId":
                return "project_type_id_";
            case "undertakingId":
                return "undertaking_id_";
            case "createTime":
                return "create_time_";
            case "beginTime":
                return "begin_time_";
            case "endTime":
                return "end_time_";
            case "timeArea":
                return "time_area_";
            case "amount":
                return "amount_";
            case "summaryDoc01":
                return "summary_doc_01_";
            case "summaryDoc02":
                return "summary_doc_02_";
            case "summaryDoc03":
                return "summary_doc_03_";
            case "summaryDoc04":
                return "summary_doc_04_";
            case "summaryDoc05":
                return "summary_doc_05_";
            case "delFlag":
                return "del_flag_";
            case "status":
                return "status_";
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
            case "submissionTime":
                return "submission_time_";
            default:
                return null;
        }
    }

}
