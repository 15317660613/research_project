package com.adc.da.research.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * <b>功能：</b>RS_END_PROJECT_KPI EndProjectKpiEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-11-06 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 *
 *
 * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
 * <p>字段列表：</p>
 * <li>procBusinessKey -> proc_business_key_</li>
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
 *
 * @see KpiEO
 */
@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
public class EndProjectKpiEO extends KpiEO implements EndBaseInterface {

    /***/
    private String procBusinessKey;
}
