package com.adc.da.progress.vo;

import com.adc.da.progress.entity.ProjectMilepostResultEO;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ProjectMilepostVO {

    /**
     * id
     */
    private String id;

    /**
     * 项目id
     */
    private String projectId;

    /**
     * 项目名称（因数据源在es，因此进行Oracle缓存）
     */
    private String projectName;

    /**
     * 里程碑名称
     */
    private String milepostName;

    /**
     * 里程碑目标
     */
    private String milepostTarget;

    /**
     * 里程碑负责人id
     */
    private String milepostManagerId;

    /**
     *   负责人名 由
     */
    private String milepostManagerName;

    /**
     * 里程碑开始时间
     */
    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date milepostBeginTime;

    /**
     * 里程碑结束时间
     */
    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date milepostEndTime;

    /**
     * 版本
     */
    private Integer milepostVersion = 0;

    private String extInfo1;

    private String extInfo2;

    private String extInfo3;

    private String extInfo4;

    private String extInfo5;

    private String stageId;

    /**
     * 完成时间
     */
    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date finishTime;

    /**
     * 里程碑状态，初始0
     */
    private Integer finishStatus = 0;

    private  String resultName;

    private String projectLeaderId ;

    private List<ProjectMilepostResultEO> projectMilepostResultEOList = new ArrayList<>();

    private List<ProjectMilepostResultVO> projectMilepostResultVOList = new ArrayList<>();
}
