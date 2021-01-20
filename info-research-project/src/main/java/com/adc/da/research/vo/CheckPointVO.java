package com.adc.da.research.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * describe:
 *
 * @author 李坤澔
 *     date 2019-09-02
 */
@Getter
@Setter
public class CheckPointVO extends CheckBaseVO {

    /**
     * 检查内容id
     */
    private String[][] checkDetailArray;

    /**
     * 检查方式
     */
    private String checkMethod;

    /**
     * 检查地点
     */
    private String checkAddress;

    /**
     * 检查时间
     */
    private Date checkDate;

    /**
     * 评审人员
     */
    private String reviewer;

    /**
     * 检查记录
     */
    private String checkRecords;

}
