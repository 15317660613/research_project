package com.adc.da.statistics.vo;

import com.adc.da.statistics.entity.ContractAmountEO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 条形图
 *
 * @author Lee Kwanho 李坤澔
 *     date 2019-11-14
 **/
@Getter
@Setter
@Builder
public class DataBoardGraphContractVO implements InterfaceDataBoardGraph {

    /**
     * 横坐标
     */
    String[] x;

    /**
     * 纵坐标
     */
    String[] y;

    /**
     * 散点图 横坐标
     */
    List<ContractAmountEO> list;

}
