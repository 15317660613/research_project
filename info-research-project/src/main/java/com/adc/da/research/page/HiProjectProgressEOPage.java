package com.adc.da.research.page;

import lombok.Getter;
import lombok.Setter;

/**
 * <b>功能：</b>RS_HI_PROJECT_PROGRESS HiProjectProgressEOPage<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-10-30 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Setter
@Getter
public class HiProjectProgressEOPage extends ProgressEOPage implements HiBasePageInterface{
    /** */
    private String procBusinessKey;

    /** */
    private String procBusinessKeyOperator = "=";

    /** */
    private String mask;

    /** */
    private String maskOperator = "=";

}
