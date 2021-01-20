package com.adc.da.budget.query;

import com.adc.da.base.page.BasePage;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>功能：</b>TS_BUDGET BudgetEOPage<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-06-21 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Getter
@Setter
public class BaseESQueryPage extends BasePage {


    private List<QueryVO> allObject = new ArrayList<>();
    private String projectType;

}
