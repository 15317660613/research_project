package com.adc.da.research.entity;

import com.adc.da.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <b>功能：</b>RS_PROJECT_KPI_DELIVERABLE KpiDeliverableEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-08-23 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 *
 * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
 * <p>字段列表：</p>
 * <li>id -> id_</li>
 * <li>researchProjectId -> research_project_id_</li>
 * <li>name -> name_</li>
 * <li>kpi -> kpi_</li>
 * <li>extInfo1 -> ext_info_1_</li>
 * <li>extInfo2 -> ext_info_2_</li>
 * <li>extInfo3 -> ext_info_3_</li>
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class KpiDeliverableEO extends BaseEntity {

    /**
     * 主键
     */
    private String id;

    /**
     * 项目（infoEO）id
     *
     * @see InfoEO#getResearchProjectId()
     */
    private String researchProjectId;

    /**
     * 交付物 名称
     */
    private String name;

    /**
     * 技术指标
     */
    private String kpi;

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
