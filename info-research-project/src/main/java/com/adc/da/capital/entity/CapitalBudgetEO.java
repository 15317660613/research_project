package com.adc.da.capital.entity;

import com.adc.da.base.entity.BaseEntity;
import com.adc.da.research.entity.InfoEO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <b>功能：</b>RS_CAPITAL_BUDGET CapitalBudgetEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-08-26 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 *
 * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
 * <p>字段列表：</p>
 * <li>researchProjectId -> research_project_id_</li>
 * <li>centerBudget -> center_budget_</li>
 * <li>centerComment -> center_comment_</li>
 * <li>deptBudget -> dept_budget_</li>
 * <li>deptComment -> dept_comment_</li>
 * <li>otherBudget -> other_budget_</li>
 * <li>otherComment -> other_comment_</li>
 * <li>extInfo1 -> ext_info_1_</li>
 * <li>extInfo2 -> ext_info_2_</li>
 * <li>extInfo3 -> ext_info_3_</li>
 * <li>extInfo4 -> ext_info_4_</li>
 * <li>extInfo5 -> ext_info_5_</li>
 * <li>extInfo6 -> ext_info_6_</li>
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CapitalBudgetEO extends BaseEntity {
    /**
     * 项目id
     *
     * @see InfoEO#getResearchProjectId()
     */
    private String researchProjectId;

    /**
     * 中心预算
     */
    private String centerBudget;

    /**
     * 中心预算-备注
     */
    private String centerComment;

    /**
     * 部门自筹
     */
    private String deptBudget;

    /**
     * 人员费
     */
    private String deptComment;

    /**
     * 其他费用
     */
    private String otherBudget;

    /**
     * 其他-备注
     */
    private String otherComment;

    /**
     * 折旧费
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
