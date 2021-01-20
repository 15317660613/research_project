package com.adc.da.statistics.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 条形图
 *
 * @author Lee Kwanho 李坤澔
 *     date 2019-11-14
 **/
@Getter
@Setter
@Builder
public class DataBoardGraphVO implements InterfaceDataBoardGraph {
    /**
     * 横坐标
     */
    String[] x;

    /**
     * 纵坐标
     */
    String[] y;
}
