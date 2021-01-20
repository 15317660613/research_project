package com.adc.da.research.entity;

import com.adc.da.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <b>功能：</b>RS_PROJECT_SUMMARY SummaryEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-08-22 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 *
 * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
 * <p>字段列表：</p>
 * <li>id -> id_</li>
 * <li>researchProjectId -> research_project_id_</li>
 * <li>researchProjectTarget -> research_project_target_</li>
 * <li>researchProjectBase -> research_project_base_</li>
 * <li>totalTarget -> total_target_</li>
 * <li>researchProjectContent -> research_project_content_</li>
 * <li>researchProjectInnovation -> research_project_innovation_</li>
 * <li>researchProjectProspect -> research_project_prospect_</li>
 * <li>otherDescription -> other_description_</li>
 * <li>extInfo1 -> ext_info_1_</li>
 * <li>extInfo2 -> ext_info_2_</li>
 * <li>extInfo3 -> ext_info_3_</li>
 * <li>extInfo4 -> ext_info_4_</li>
 * <li>extInfo5 -> ext_info_5_</li>
 * <li>extInfo6 -> ext_info_6_</li>
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SummaryEO extends BaseEntity {

    /**
     * 主键
     */
    private String id;

    /**
     * 项目id
     *
     * @see InfoEO#getResearchProjectId()
     */
    private String researchProjectId;

    /**
     * 目标 科研项目目标
     */
    private String researchProjectTarget;

    /**
     * 工作基础
     */
    private String researchProjectBase;

    /**
     * 总体目标
     */
    private String totalTarget;

    /**
     * 研发内容
     */
    private String researchProjectContent;

    /**
     * 创新点
     */
    private String researchProjectInnovation;

    /**
     * 成果展望
     */
    private String researchProjectProspect;

    /**
     * 其他说明
     */
    private String otherDescription;

    /**
     * 预留
     */
    private String extInfo1;

    /**
     * 预留
     */
    private String extInfo2;

    /**
     * 预留
     */
    private String extInfo3;

    /**
     * 预留
     */
    private String extInfo4;

    /**
     * 预留
     */
    private String extInfo5;

    /**
     * 预留
     */
    private String extInfo6;

}
