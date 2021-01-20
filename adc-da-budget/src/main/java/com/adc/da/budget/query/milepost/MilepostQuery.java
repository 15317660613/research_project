package com.adc.da.budget.query.milepost;

import com.adc.da.base.page.BasePage;
import com.adc.da.budget.query.QueryVO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>功能：</b>TS_BUDGET BudgetEOPage<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-06-21 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Data
public class MilepostQuery extends BasePage {

    private List<QueryVO> projectName = new ArrayList<>();

    private List<QueryVO> milepostName = new ArrayList<>();

    private  List<QueryVO> milepostLeader = new ArrayList<>();

    private  List<QueryVO> milepostTarget = new ArrayList<>();

    private  List<QueryVO> milepostBeginTime = new ArrayList<>();

    private  List<QueryVO> milepostEndTime = new ArrayList<>();

    private  List<QueryVO> finishStatus = new ArrayList<>();

    private  List<QueryVO> milepostResult = new ArrayList<>();


}
