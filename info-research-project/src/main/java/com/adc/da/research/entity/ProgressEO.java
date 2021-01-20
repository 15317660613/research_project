package com.adc.da.research.entity;

import com.adc.da.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <b>功能：</b>RS_PROJECT_PROGRESS ProgressEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-08-23 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 *
 * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
 * <p>字段列表：</p>
 * <li>id -> id_</li>
 * <li>researchProjectId -> research_project_id_</li>
 * <li>date -> date_</li>
 * <li>checkDetail -> check_detail_</li>
 * <li>extInfo1 -> ext_info_1_</li>
 * <li>extInfo2 -> ext_info_2_</li>
 * <li>extInfo3 -> ext_info_3_</li>
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ProgressEO extends BaseEntity {
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
     * 检查时间
     */
    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date date;

    /**
     * 检查内容
     */
    private String checkDetail;

    /**
     * 状态 0为未完成，1为已完成
     */
    private Integer extInfo1 = 0;

    /**
     * 预留 流程id
     */
    private String extInfo2 = "-1";

    /**
     * 预留  完成时间
     */
    private String extInfo3;

}
