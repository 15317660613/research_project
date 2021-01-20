package com.adc.da.budget.enums;

import com.adc.da.util.utils.StringUtils;
import lombok.Getter;

/**
 * 职级
 *
 * @author liuzixi
 * date 2019-03-08
 */
@Getter
public enum JobClassEnums {

    /**
     * 助理
     */
    ASSISTANT("助理", 1000),
    /**
     * 专员
     */
    COMMISSIONER("专员", 2000),
    /**
     * 工程师
     */
    ENGINEER("工程师", 3000),
    /**
     * 经理
     */
    MANAGER("经理", 4000),
    /**
     * 高级经理
     */
    SENIOR_MANAGER("高级经理", 5000),
    /**
     * 技术总监
     */
    TECHNICAL_DIRECTOR("技术总监", 6000),
    /**
     * 部长助理
     */
    ASSISTANT_MINISTER("部长助理", 7000),
    /**
     * 副部长
     */
    VICE_MINISTER("副部长", 8000),
    /**
     * 部长
     */
    MINISTER("部长", 9000),
    /**
     * 总监助理
     */
    ASSISTANT_DIRECTOR("总监助理", 10000),
    /**
     * 副总监
     */
    VICE_DIRECTOR("副总监", 11000),
    /**
     * 总监
     */
    DIRECTOR("总监", 12000),
    /**
     * 副总工
     */
    VICE_CHIEF_ENGINEER("副总工", 13000),
    /**
     * 主任助理
     */
    ASSISTANT_HEAD("主任助理", 14000),
    /**
     * 总工
     */
    CHIEF_ENGINEER("总工", 15000),
    /**
     * 副主任
     */
    VICE_HEAD("副主任", 16000),
    /**
     * 科委主任
     */
    SCIENCE_COMMITTEE_HEAD("科委主任", 17000),
    /**
     * 主任
     */
    HEAD("主任", 18000);

    /**
     * 职级名称
     */
    private String name;

    /**
     * 职级权重值
     */
    private int value;

    JobClassEnums(String name, int value) {
        this.name = name;
        this.value = value;
    }

    /**
     * 根据名称查权重
     * @param name
     * @return
     * @author liuzixi
     * date 2019-03-08
     */
    public static int getFromName(String name) {
        for (JobClassEnums jobClass : values()) {
            if (StringUtils.equals(jobClass.name, name)) {
                return jobClass.value;
            }
        }
        return 0;
    }
}
