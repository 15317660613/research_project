package com.adc.da.listener.entity;

import lombok.Data;

/**
 * describe:
 *  里程碑变更中的时间
 * @author 李坤澔
 *     date 2019-08-05
 */
@Data
public class MilestoneDateEO {
    /**
     * 开始时间， yyyy-mm-dd
     */
    private String startValue;

    /**
     * 控件id
     */
    private String startName;

    /**
     * 结束时间， yyyy-mm-dd
     */
    private String endValue;
    /**
     * 控件id
     */
    private String endName;
}
