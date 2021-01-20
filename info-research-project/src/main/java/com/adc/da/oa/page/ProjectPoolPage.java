package com.adc.da.oa.page;

import com.adc.da.base.page.BasePage;
import lombok.Getter;
import lombok.Setter;

/**
 * <b>功能：</b>PR_STAGE_ORDER StageOrderEOPage<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-07-18 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Getter
@Setter
public class ProjectPoolPage extends BasePage {
    /**
     * contractAmountStr: "3"
     * createUserName: "1"
     * name: "2"
     */
    private String contractAmountStr;

    /**
     * 合同名称
     */
    private String name;

    /**
     *  合同编号
     */
    private String contractNo;
}
