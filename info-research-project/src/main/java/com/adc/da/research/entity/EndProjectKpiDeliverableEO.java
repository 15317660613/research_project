package com.adc.da.research.entity;

import com.adc.da.base.entity.BaseEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * <b>功能：</b>RS_END_PROJECT_KPI_DELIVERABLE EndProjectKpiDeliverableEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-11-06 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 *
 * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
 * <p>字段列表：</p>
 * <li>id -> id_</li>
 * <li>procBusinessKey -> proc_business_key_</li>
 * <li>researchProjectId -> research_project_id_</li>
 * <li>name -> name_</li>
 * <li>kpi -> kpi_</li>
 * <li>extInfo1 -> ext_info_1_</li>
 * <li>extInfo2 -> ext_info_2_</li>
 * <li>extInfo3 -> ext_info_3_</li>
 *
 * @see KpiEO
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class EndProjectKpiDeliverableEO extends BaseEntity implements EndBaseInterface {

    /***/
    private String id;

    /***/
    private String procBusinessKey;

    /***/
    private String researchProjectId;

    /***/
    private String name;

    /***/
    private String kpi;

    /***/
    private String extInfo1;

    /***/
    private String extInfo2;

    /***/
    private String extInfo3;

}
