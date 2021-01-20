package com.adc.da.oa.vo;

import lombok.Data;

@Data
public class ClaimDeptVO {
    /**
     *deptId
     */
    private String deptId;

    /**
     * 部门名称
     */
    private String deptName;

    private Integer deptType = 0 ; // 1：只包含自己 2：包含子部门
}
