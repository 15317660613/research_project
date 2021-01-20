package com.adc.da.business.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 支出创收
 *
 * @author liuzixi
 *     date 2019-03-19
 */
@Getter
@Setter
@Builder
public class DealIncomeVO {

    /**
     * id
     * 可能是员工，也可能是组织
     */
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 支出
     */
    private String amount;
}
