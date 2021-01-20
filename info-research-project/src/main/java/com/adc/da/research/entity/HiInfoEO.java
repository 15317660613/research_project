package com.adc.da.research.entity;

import com.adc.da.util.utils.StringUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * <b>功能：</b>RS_HI_PROJECT_INFO HiProjectInfoEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-10-24 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 *
 * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
 * <p>字段列表：</p>
 * <li>procBusinessKey -> proc_business_key_</li>
 * <li>mask -> mask_</li>
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
 *
 * @see InfoEO
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class HiInfoEO extends InfoEO implements HiBaseInterface {

    /**
     * 流程实例key
     */
    String procBusinessKey;

    /**
     * 变更内容标记
     */
    private String mask = "";

    /**
     * 进行变更mask校验
     * cooperationDepartmentName
     * ownDepartmentName
     * ownPlatformName
     * researchProjectTypeName
     * researchProjectBeginTime
     * researchProjectEndTime
     *
     * @param target
     * @return
     */
    public void initMaskAndProcBusinessKey(HiInfoEO target) {
        this.procBusinessKey = target.procBusinessKey;
        StringBuilder maskBuilder = new StringBuilder();
        if ((
            StringUtils.isEmpty(this.cooperationDepartmentName)
                && StringUtils.isNotEmpty(target.cooperationDepartmentName))
            || (
            StringUtils.isNotEmpty(this.cooperationDepartmentName)
                && !this.cooperationDepartmentName.equals(target.cooperationDepartmentName))) {
            /*
             *  原数据为null  现数据 != "" 或 原数据 ！= 现数据
             */
            this.cooperationDepartmentName = target.cooperationDepartmentName;
            maskBuilder.append("cooperationDepartmentName").append(',');
        } else if (StringUtils.isEmpty(this.cooperationDepartmentName)
            && ("").equals(target.cooperationDepartmentName)) {
            /*
             *  原数据为null  现数据为 ""
             */
            this.cooperationDepartmentName = "";
        }

        if (!this.ownDepartmentName.equals(target.ownDepartmentName)) {
            this.ownDepartmentName = target.ownDepartmentName;
            this.ownDepartmentId = target.ownDepartmentId;
            maskBuilder.append("ownDepartmentName").append(',');
        }
        if (!this.ownPlatformName.equals(target.ownPlatformName)) {
            this.ownPlatformName = target.ownPlatformName;
            this.ownPlatformId = target.ownPlatformId;
            maskBuilder.append("ownPlatformName").append(',');
        }
        if (!this.researchProjectTypeName.equals(target.researchProjectTypeName)) {
            this.researchProjectTypeName = target.researchProjectTypeName;
            this.researchProjectTypeId = target.researchProjectTypeId;
            maskBuilder.append("researchProjectTypeName").append(',');
        }
        if (!this.researchProjectBeginTime.equals(target.researchProjectBeginTime)) {
            this.researchProjectBeginTime = target.researchProjectBeginTime;
            maskBuilder.append("researchProjectBeginTime").append(',');
        }
        if (!this.researchProjectEndTime.equals(target.researchProjectEndTime)) {
            this.researchProjectEndTime = target.researchProjectEndTime;
            maskBuilder.append("researchProjectEndTime").append(',');
        }
        if (maskBuilder.length() > 0) {
            this.mask = (maskBuilder.substring(0, maskBuilder.length() - 1));
        }
    }

}
