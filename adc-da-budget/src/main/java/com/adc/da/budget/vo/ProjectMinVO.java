package com.adc.da.budget.vo;


import com.adc.da.budget.entity.ProjectContractInvoiceEO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.Collator;
import java.util.*;


/**
 * @author qichunxu
 */
@Data
public class ProjectMinVO implements Comparable<ProjectMinVO> {

public int compareTo(ProjectMinVO o) {
        return Collator.getInstance(java.util.Locale.CHINA).compare(this.getName(), o.getName());
        }

    @ApiModelProperty("ID")
    private String id;
    @NotBlank(message = "项目名称不能为空！")
    @ApiModelProperty("项目名称")
    private String name;
    @ApiModelProperty("项目负责人ID")
    private String projectLeaderId;
    @ApiModelProperty("项目负责人名字")
    private String projectLeader;
    @ApiModelProperty("部门ID")
    private String deptId;
    @ApiModelProperty("项目成员名称")
    private String projectMemberNames;
    private String[] memberNames;
    @ApiModelProperty("项目成员ID")
    @NotEmpty(message = "项目组成员不能为空！")
    private String[] projectMemberIds;

    private String projectOwner;
    @ApiModelProperty("开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;
    @ApiModelProperty("项目开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date projectStartTime;
    @ApiModelProperty("项目完成状态")
    private String finishedStatus;
    @ApiModelProperty("人力投入")
    private Integer personInput;
    @ApiModelProperty("项目详情")
    private String projectDescription;

    @ApiModelProperty("组")
    private String projectTeam;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date modifyTime;

    private List<Map<String,String>> mapList;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date projectBeginTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date projectEndTime;
    /**
     * 合同合计
     */
    private float contractAmount ;

    private String contractAmountStr ;

    // 0-经营类业务，1-非经营类业务,2-科研类业务
    private int projectType ;
    private List<ProjectContractInvoiceEO> projectContractInvoiceEOList ;//开票金额


}
