package com.adc.da.capital.entity;

import com.adc.da.research.entity.HiBaseInterface;
import com.adc.da.research.utils.CompareObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * <b>功能：</b>RS_HI_CAPITAL_DETAIL HiCapitalDetailEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-10-30 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class HiCapitalDetailEO extends CapitalExpenditureDetailEO implements HiBaseInterface {
    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>procBusinessKey -> proc_business_key_</li>
     * <li>mask -> mask_</li>
     * <li>id -> id_</li>
     * <li>researchProjectId -> research_project_id_</li>
     * <li>detailType -> detail_type_</li>
     * <li>name -> name_</li>
     * <li>count -> count_</li>
     * <li>unit -> unit_</li>
     * <li>unitPrice -> unit_price_</li>
     * <li>sort -> sort_</li>
     * <li>extInfo1 -> ext_info_1_</li>
     * <li>extInfo2 -> ext_info_2_</li>
     * <li>extInfo3 -> ext_info_3_</li>
     * <li>extInfo4 -> ext_info_4_</li>
     * <li>extInfo5 -> ext_info_5_</li>
     * <li>extInfo6 -> ext_info_6_</li>
     */
    private String procBusinessKey;

    /***/
    private String mask = "";


    /**
     * 用反射对mask进行初始化
     *
     * @param source
     * @see com.adc.da.research.utils.CompareObject
     */
    public void initMask(HiCapitalDetailEO source, int level) {
        StringBuilder builder = CompareObject.compareHiField(this, source, level);
        if (builder.length() > 0) {
            this.mask = (builder.substring(0, builder.length() - 1));
        }
    }
}

