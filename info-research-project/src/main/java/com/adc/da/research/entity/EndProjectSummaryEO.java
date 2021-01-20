package com.adc.da.research.entity;

import com.adc.da.base.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * <b>功能：</b>RS_END_PROJECT_SUMMARY EndProjectSummaryEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-11-06 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 *
 * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
 * <p>字段列表：</p>
 * <li>procBusinessKey -> proc_business_key_</li>
 * <li>researchProjectId -> research_project_id_</li>
 * <li>totalTarget -> total_target_</li>
 * <li>totalTargetAct -> total_target_act_</li>
 * <li>researchProjectContent -> research_project_content_</li>
 * <li>researchProjectContentAct -> research_project_content_act_</li>
 * <li>extInfo1 -> ext_info_1_</li>
 * <li>extInfo2 -> ext_info_2_</li>
 * <li>extInfo3 -> ext_info_3_</li>
 * <li>extInfo4 -> ext_info_4_</li>
 * <li>extInfo5 -> ext_info_5_</li>
 * <li>extInfo6 -> ext_info_6_</li>
 */
@Setter
@Getter
public class EndProjectSummaryEO extends BaseEntity implements EndBaseInterface {

    /***/
    private String procBusinessKey;

    /***/
    private String researchProjectId;

    /***/
    private String totalTarget;

    /***/
    private String totalTargetAct;

    /***/
    private String researchProjectContent;

    /***/
    private String researchProjectContentAct;

    /***/
    private String extInfo1;

    /***/
    private String extInfo2;

    /***/
    private String extInfo3;

    /***/
    private String extInfo4;

    /***/
    private String extInfo5;

    /***/
    private String extInfo6;

}
