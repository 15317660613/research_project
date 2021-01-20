package com.adc.da.budget.enums;


import lombok.Getter;

/**
 * 项目状态
 * @author qichunxu
 */
@Getter
public enum ProjectStatusEnums {

    /**
     * 项目进行中
     */
    EXECUTE(0,"进行中"),
    /**
     * 项目审批中
     */
    AUDIT(1,"审批中"),
    /**
     * 项目已完成
     */
    COMPLETE(2,"已完成");

    /**
     * 类型
     */
    private int type;

    /**
     * 状态
     */
    private String status;

    ProjectStatusEnums(int type,String status){
        this.type = type;
        this.status = status;
    }

    public static String getStatus(int type){
        for (ProjectStatusEnums value : values()) {
            if (value.type == type){
                return value.status;
            }
        }
        return null;
    }
}
