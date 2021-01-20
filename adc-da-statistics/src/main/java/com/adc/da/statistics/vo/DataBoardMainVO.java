package com.adc.da.statistics.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Lee Kwanho 李坤澔
 *     date 2019-11-14
 **/
@Getter
@Setter
@Builder
public class DataBoardMainVO {

    /**
     * Total contract 合同 执行额 = 今年签订数据 + 往年结余数据
     */
    private long totalContract;

    /**
     * 开票-图 bar chart
     */
    private DataBoardGraphVO billGraph;

    /**
     * 开票-图 pie chart
     */
    private DataBoardGraphVO billGraphPieChart;

    /**
     * Total billing 开票
     */
    private long totalBilling;

    /**
     * 合同-图  仅统计当年合同数据
     */
    private DataBoardGraphVO contractGraph;

    /**
     * 合同-图  pie chart 仅统计当年合同数据
     */
    private DataBoardGraphVO contractGraphPieChart;

    /**
     * Total credit  进账
     */
    private long totalCredit;

    /**
     * 进账-图
     */
    private DataBoardGraphVO creditGraph;

    /**
     * 进账-图  pie chart
     */
    private DataBoardGraphVO creditGraphPieChart;

}
