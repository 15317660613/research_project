package com.adc.da.budget.constant;

import lombok.Getter;

/**
 * describe:
 *
 * @author 李坤澔
 *     date 2019-09-26
 */
@Getter
public class ProjectType {
    private ProjectType() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 经营类项目
     */
    public static final Integer BUSINESS_PROJECT = 0;

    /**
     * 日常事务类
     */
    public static final Integer NO_BUSINESS_PROJECT = 1;

    /**
     * 科研类
     */
    public static final Integer RESEARCH_PROJECT = 2;

}
