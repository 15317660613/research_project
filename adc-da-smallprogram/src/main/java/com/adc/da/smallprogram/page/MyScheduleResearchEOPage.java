package com.adc.da.smallprogram.page;

import com.adc.da.base.page.BasePage;
import lombok.Data;

/**
 * <b>功能：</b>TS_SCHEDULE_RESEARCH ScheduleResearchEOPage<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-05-26 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Data
public class MyScheduleResearchEOPage extends BasePage {
    private String year;
    private String month;
    private String userId;
    private int searchMyselfFlag;
}
