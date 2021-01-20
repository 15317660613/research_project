package com.adc.da.progress.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

import static com.adc.da.progress.util.StatusUtils.NOT_COMPLETED;

/**
 * describe:
 *
 * @author 李坤澔
 *     date 2019-07-18
 */
@Data
public class ProjectProgressEO {

    /**
     * 阶段序号
     */
    @ApiModelProperty("阶段序号")
    private Integer stageLevel;

    /**
     * 阶段名称
     */
    @ApiModelProperty("阶段名称")
    private String stageName;

    /**
     * 阶段名称ID
     */
    @ApiModelProperty("阶段名称ID")
    private String stageNameId;

    /**
     * 项目id
     */
    private String projectId;

    /**
     * 阶段详情
     */
    private List<ProgressDetailEO> progressDetailEOList;

    /**
     * 阶段状态
     */
    private Integer stageStatus = NOT_COMPLETED;

}
