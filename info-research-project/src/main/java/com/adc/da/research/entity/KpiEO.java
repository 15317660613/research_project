package com.adc.da.research.entity;

import com.adc.da.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <b>功能：</b>RS_PROJECT_KPI KpiEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-08-23 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 *
 * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
 * <p>字段列表：</p>
 * <li>researchProjectId -> research_project_id_</li>
 * <li>numInventionPatents -> num_invention_patents_</li>
 * <li>numUtilityModels -> num_utility_models_</li>
 * <li>numAppearancePatents -> num_appearance_patents_</li>
 * <li>numCopyright -> num_copyright_</li>
 * <li>numCorePapers -> num_core_papers_</li>
 * <li>numOtherPapers -> num_other_papers_</li>
 * <li>other -> other_</li>
 * <li>extInfo1 -> ext_info_1_</li>
 * <li>extInfo2 -> ext_info_2_</li>
 * <li>extInfo3 -> ext_info_3_</li>
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class KpiEO extends BaseEntity {
    /**
     * 项目（infoEO）id , primaryKey
     *
     * @see InfoEO#getResearchProjectId()
     */
    private String researchProjectId;

    /**
     * 发明专利数
     */
    private Integer numInventionPatents;

    /**
     * 实用新型
     */
    private Integer numUtilityModels;

    /**
     * 外观专利
     */
    private Integer numAppearancePatents;

    /**
     * 著作
     */
    private Integer numCopyright;

    /**
     * 核心论文
     */
    private Integer numCorePapers;

    /**
     * 其他论文
     */
    private Integer numOtherPapers;

    /**
     * 其他
     */
    private String other;

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

}
