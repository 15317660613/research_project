package com.adc.da.statistics.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;

import java.math.BigDecimal;

/**
 * @author Lee Kwanho 李坤澔
 *     date 2019-11-14
 **/
@Getter
@Setter
@Builder
public class DataBoardTreeEO {
    /**
     *
     */
    String id;

    /**
     * 业务名称 / 部门名称
     */
    String name;

    /**
     * 金额
     */
    BigDecimal amount;

    @Tolerate
    public DataBoardTreeEO() {
        // enable new DataBoardTreeVO
    }
}
