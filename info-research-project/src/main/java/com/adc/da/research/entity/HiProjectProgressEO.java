package com.adc.da.research.entity;

import com.adc.da.research.utils.CompareObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * <b>功能：</b>RS_HI_PROJECT_PROGRESS HiProjectProgressEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-10-30 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 * * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
 * * <p>字段列表：</p>
 * * <li>id -> id_</li>
 * * <li>procBusinessKey -> proc_business_key_</li>
 * * <li>mask -> mask_</li>
 * * <li>researchProjectId -> research_project_id_</li>
 * * <li>date -> date_</li>
 * * <li>checkDetail -> check_detail_</li>
 * * <li>extInfo1 -> ext_info_1_</li>
 * * <li>extInfo2 -> ext_info_2_</li>
 * * <li>extInfo3 -> ext_info_3_</li>
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class HiProjectProgressEO extends ProgressEO implements HiBaseInterface {
    /**
     *
     */

    private String procBusinessKey;

    /**
     *
     */

    private String mask = "";

    /**
     * 用反射对mask进行初始化
     *
     * @param source
     * @see com.adc.da.research.utils.CompareObject
     */
    public void initMask(HiProjectProgressEO source, int level) {
        StringBuilder builder = CompareObject.compareHiField(this, source, level);
        if (builder.length() > 0) {
            this.mask = (builder.substring(0, builder.length() - 1));
        }
    }
}
