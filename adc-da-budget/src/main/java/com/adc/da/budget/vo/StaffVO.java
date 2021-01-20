package com.adc.da.budget.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author Administrator
 */
@Data
public class StaffVO {

    @ApiModelProperty("员工ID")
    private String id;

    @ApiModelProperty("员工名字")
    private String name;

    @ApiModelProperty("角色")
    private String role;

    @ApiModelProperty("所属部门")
    private String parentDepartment;
}
