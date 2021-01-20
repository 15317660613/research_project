package com.adc.da.research.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * describe:
 *
 * @author 李坤澔
 *     date 2019-09-02
 */
@Getter
@Setter
public class CheckMidtermVO extends CheckBaseVO {

    /**
     * 项目目标
     */
    private String target;

    /**
     * 研究内容
     */
    private String content;

    /**
     * 考核指标
     */
    private String assessmentIndex;

}
