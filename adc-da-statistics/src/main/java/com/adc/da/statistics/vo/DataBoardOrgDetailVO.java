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
public class DataBoardOrgDetailVO {

    /***/
    private long sumProject;

    /**
     * Total contract 合同 合同 执行额 = 今年签订数据 + 往年结余数据
     */
    private long totalContract;

    /**
     * Total billing 开票
     */
    private long totalBilling;

    /**
     * Total credit  进账
     */
    private long totalCredit;

    /**
     * Total expenditure 支出
     */
    private long totalExpenditure;

    /** 工时 */
    private long workTime;

    /**
     * 开票-图
     */
    private DataBoardGraphVO billGraph;

    /**
     * 支出-图
     */
    private DataBoardGraphVO expenditureGraph;

    /**
     * 合同-图  仅统计当年合同数据
     */
    private InterfaceDataBoardGraph contractGraph;

    /**
     * 进账-图
     */
    private DataBoardGraphVO creditGraph;

    /**
     * 人力投入-图
     */
    private DataBoardGraphVO workTimeGraph;

}
