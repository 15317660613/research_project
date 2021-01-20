package com.adc.da.research.entity;

import com.adc.da.base.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

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
@Getter
@Setter
public class CheckFormEO extends BaseEntity {
    /**
     * 名称
     */
    private String[] str;

    /**
     * 总数
     */
    private int[] count;

}
