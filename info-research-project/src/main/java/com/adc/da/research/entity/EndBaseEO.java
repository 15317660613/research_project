package com.adc.da.research.entity;

import com.adc.da.base.entity.BaseEntity;
import com.adc.da.research.utils.HiConstant;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * <b>功能：</b>RS_END_BASE EndBaseEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-11-06 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 *
 * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
 * <p>字段列表：</p>
 * <li>procBusinessKey -> proc_business_key_</li>
 * <li>researchProjectId -> research_project_id_</li>
 * <li>rsFileId -> rs_file_id_</li>
 * <li>updateDate -> update_date_</li>
 * <li>version -> version_</li>
 * <li>extInfo1 -> ext_info_1_</li>
 * <li>extInfo2 -> ext_info_2_</li>
 * <li>extInfo3 -> ext_info_3_</li>
 */

@Getter
@Setter
public class EndBaseEO extends BaseEntity {

    /***/
    private String procBusinessKey;

    /***/
    private String researchProjectId;

    /***/
    private String rsFileId = "";

    /***/
    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateDate;

    /***/
    private Integer version = 1;

    /**
     * 标识，用于标识状态
     * 0 表示草稿
     * 1 表示流程中/已完成
     */
    private Integer extInfo1 = HiConstant.DRAFT_STATUS;

    /***/
    private String extInfo2;

    /***/
    private String extInfo3;

}
