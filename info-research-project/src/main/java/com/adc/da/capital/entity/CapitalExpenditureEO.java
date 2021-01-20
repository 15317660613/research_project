package com.adc.da.capital.entity;

import com.adc.da.base.entity.BaseEntity;
import com.adc.da.research.entity.InfoEO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <b>功能：</b>RS_CAPITAL_EXPENDITURE CapitalExpenditureEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-08-26 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 *
 * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
 * <p>字段列表：</p>
 * <li>id -> id_</li>
 * <li>researchProjectId -> research_project_id_</li>
 * <li>budget0 -> budget_0_</li>
 * <li>budget1 -> budget_1_</li>
 * <li>budget2 -> budget_2_</li>
 * <li>other -> other_</li>
 * <li>extInfo1 -> ext_info_1_</li>
 * <li>extInfo2 -> ext_info_2_</li>
 * <li>extInfo3 -> ext_info_3_</li>
 * <li>extInfo4 -> ext_info_4_</li>
 * <li>extInfo5 -> ext_info_5_</li>
 * <li>extInfo6 -> ext_info_6_</li>
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CapitalExpenditureEO extends BaseEntity {

    /**
     * 主键
     */
    private String id;

    /**
     * @see InfoEO#getResearchProjectId()
     */
    private String researchProjectId;

    /**
     * 今年
     */
    private String budget0;

    /**
     * 明年
     */
    private String budget1;

    /**
     * 后年
     */
    private String budget2;

    /**
     * 计算依据/ 特别说明
     */
    private String other;

    /**
     * 申请中心预算
     */
    private String extInfo1;

    /**
     * 预留 顺序
     */
    private String extInfo2;

    /**
     * 菜单id
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
