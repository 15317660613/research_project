package com.adc.da.research.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * <b>功能：</b>RS_HI_BASE HiBaseEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-10-24 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 *
 * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
 * <p>字段列表：</p>
 * <li>extInfo1 -> ext_info_1_</li>
 * <li>extInfo2 -> ext_info_2_</li>
 * <li>extInfo3 -> ext_info_3_</li>
 * <li>researchProjectId -> research_project_id_</li>
 * <li>changeReason -> change_reason_</li>
 * <li>rsFileId -> rs_file_id_</li>
 * <li>updateDate -> update_date_</li>
 * <li>version -> version_</li>
 * <li>procBusinessKey -> proc_business_key_</li>
 *
 * @see EndBaseEO
 */
@Getter
@Setter
public class HiBaseEO extends EndBaseEO {

    /**
     * 变更原因
     */
    private String changeReason;

}
