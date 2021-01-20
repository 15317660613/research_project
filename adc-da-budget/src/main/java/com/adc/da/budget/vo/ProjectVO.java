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

import java.util.*;


/**
 * @author qichunxu
 */
@Data
public class ProjectVO {

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
    @ApiModelProperty("业务ID")
    private String budgetId;
    @ApiModelProperty("业务名称")
    private String budget;
    @ApiModelProperty("业务类型ID")
    private String businessId;
    @ApiModelProperty("业务类型名称")
    private String business;
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
    @ApiModelProperty("创建人")
    private String createUserId;
    @ApiModelProperty("业务经理")
    private String pm;
    @ApiModelProperty("组")
    private String projectTeam;
    @ApiModelProperty("合同编号")
    private String contractNo;
    @ApiModelProperty("业务创建人")
    private String businessCreateUserId;
    @ApiModelProperty("是否具有修改权限")
    private Boolean manager;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date modifyTime;

    private List<Map<String,String>> mapList;

    private List<Map<String,String>> userIdDeptNameMapList;
    //提供给前段判断是否能点状态按钮 0表示进行中
    private String btnFlag;
    private String approveUserId;

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


    /**
     * 商务经理id
     *
     */
    private String businessManagerId;

    /**
     * 商务经理姓名
     *
     */
    private String businessManagerName;

    private String businessAdminId ;

    private String businessAdminName ;

    private String projectAdminId ;

    @Field(type = FieldType.String, analyzer = "not_analyzed")
    private String projectAdminName ;

    private int projectYear;

    private int projectMonth;

    @Field(type = FieldType.String, analyzer = "not_analyzed")
    private String province;

    //乙方
    @Field(type = FieldType.String, analyzer = "not_analyzed")
    private String partyB;

    //项目成员更新时间
    @Field(type = FieldType.Date, analyzer = "not_analyzed")
    private Date projectMemberUpdatingTime;

    //    // 所选部门ID和类型 如类型=1 只选当前自身 类型=2 包含子部门
    private ArrayList<Map<String,String>> deptInfoListMap = new ArrayList<>();
    ////    {
////        "deptId":"",
////        "type":1,
////        "deptName":""
////    },
////    {
////        "deptId":"",
////            "type":2,
////            "deptName":""
////    }
//    //所选部门id 及部门下用户id List
    private HashMap<String,List<String>> deptIdUserIdList = new HashMap<>();
////    {
////        "deptId":"[userId,userId]"
////    }

}
