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
public class DeclarationPageVO {
    /**
     * order
     */
    private String orderBy;

    /**
     * 分页参数
     */
    private Integer pageSize;

    /**
     * 分页参数
     */
    private Integer pageNo;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 创建人名称
     */
    private String creatorName;

    /**
     * 负责人名称
     */
    private String leaderName;

    /**
     * 部门
     */
    private String deptName;

    /**
     * 创建时间
     * 时间戳 - 时间戳
     */
    private String createdTimeArea;

    /**
     * 提交时间
     * 时间戳 - 时间戳
     */
    private String submissionTimeArea;

    /**
     * 项目类型id
     */
    private String projectTypeId;

    /**
     * 承办方式id
     */
    private String undertakingId;
}
