package com.adc.da.research.entity;

import com.adc.da.base.entity.BaseEntity;
import com.adc.da.research.utils.RSPendingStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

import static com.adc.da.research.utils.RSPendingStatus.RS_PENDING_CREATION;

/**
 * <b>功能：</b>RS_PROJECT_INFO InfoEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-08-22 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 *
 * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
 * <p>字段列表：</p>
 * <li>researchProjectId -> research_project_id_</li>
 * <li>researchProjectName -> research_project_name_</li>
 * <li>researchProjectPropertyId -> research_project_property_id_</li>
 * <li>cooperationDepartmentName -> cooperation_department_name_</li>
 * <li>ownDepartmentName -> own_department_name_</li>
 * <li>ownDepartmentId -> own_department_id_</li>
 * <li>ownPlatformName -> own_platform_name_</li>
 * <li>ownPlatformId -> own_platform_id_</li>
 * <li>researchProjectTypeId -> research_project_type_id_</li>
 * <li>researchProjectTypeName -> research_project_type_name_</li>
 * <li>researchProjectBeginTime -> research_project_begin_time_</li>
 * <li>researchProjectEndTime -> research_project_end_time_</li>
 * <li>businessId -> business_id_</li>
 * <li>businessName -> business_name_</li>
 * <li>extInfo1 -> ext_info_1_</li>
 * <li>extInfo2 -> ext_info_2_</li>
 * <li>extInfo3 -> ext_info_3_</li>
 * <li>extInfo4 -> ext_info_4_</li>
 * <li>extInfo5 -> ext_info_5_</li>
 * <li>extInfo6 -> ext_info_6_</li>
 */
@Setter
@Getter
public class InfoEO extends BaseEntity {

    /**
     * 项目id
     */
    protected String researchProjectId;

    /**
     * 项目名称
     */
    protected String researchProjectName;

    /**
     * 科研项目性质
     */
    protected String researchProjectPropertyId;

    /**
     * 合作部门
     */
    protected String cooperationDepartmentName;

    /**
     * 所属部门名称
     */
    protected String ownDepartmentName;

    /**
     * 所属部门ID
     */
    protected String ownDepartmentId;

    /**
     * 所属平台名称 数据字典
     */
    protected String ownPlatformName;

    /**
     * 所属平台id  数据字典
     */
    protected String ownPlatformId;

    /**
     * 项目类别ID  数据字典
     */
    protected String researchProjectTypeId;

    /**
     * 项目类别名称 数据字典
     */
    protected String researchProjectTypeName;

    /**
     * 开始时间
     */
    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date researchProjectBeginTime;

    /**
     * 结束时间
     */
    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date researchProjectEndTime;

    /**
     * 所属业务ID
     */
    protected String businessId;

    /**
     * 所属业务名称
     */
    protected String businessName;

    /**
     * 创建人
     */
    protected String extInfo1;

    /**
     * 合同编号
     */
    protected String extInfo2;

    /**
     * 预留 年份
     */
    protected String extInfo3;

    /**
     * 状态
     * 3 为 流程中
     * 2 为 未发起（变更）
     * 0 为 未发起（新建）
     * 1 为 已完成
     * -1 为 删除
     *
     * @see RSPendingStatus
     */
    protected String extInfo4 = RS_PENDING_CREATION;

    /**
     * 创建时间 时间戳
     */
    protected String extInfo5;

    /**
     * 预留
     * businessKey  和
     * proc_inst_id
     */
    protected String extInfo6;

}
