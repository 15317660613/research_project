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
public class CheckBaseVO {

    /**
     * 项目id
     */
    private String researchProjectId;

    /**
     * 项目名称
     */
    private String researchProjectName;

    /**
     * 所属部门名称
     */
    private String ownDepartmentName;

    /**
     * 所属部门ID
     */
    private String ownDepartmentId;

    /**
     * 负责人姓名
     */
    private String leaderName;

    /**
     * 负责人id
     */
    private String leaderId;

}
