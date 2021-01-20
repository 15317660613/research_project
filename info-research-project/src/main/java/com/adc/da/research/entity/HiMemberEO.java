package com.adc.da.research.entity;

import com.adc.da.research.utils.CompareObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * <b>功能：</b>RS_HI_PROJECT_MEMBER HiProjectMemberEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-10-25 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class HiMemberEO extends MemberEO implements HiBaseInterface {

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
    public void initMask(HiMemberEO source, int level) {
        StringBuilder builder = CompareObject.compareHiField(this, source, level);
        if (builder.length() > 0) {
            this.mask = (builder.substring(0, builder.length() - 1));
        }
    }
}
