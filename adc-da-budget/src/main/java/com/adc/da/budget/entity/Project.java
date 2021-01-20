package com.adc.da.budget.entity;

import com.adc.da.base.entity.BaseEntity;
import com.adc.da.budget.annotation.MatchField;
import com.adc.da.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.Collator;
import java.util.*;

@Getter
@Setter
@Document(indexName = "financial_prd", type = "project")
public class Project extends BaseEntity implements Comparable<Project> {

    public int compareTo(Project o) {
        return Collator.getInstance(java.util.Locale.CHINA).compare(this.getName(), o.getName());
    }

    //    matchField
//     项目名称,业务方,人力投入,项目描述,所属业务,创建时间,项目负责人,人员
    //@Excel(name = "项目ID", orderNum = "1")
    @Id
    private String id;

    @Excel(name = "项目名称", orderNum = "1")
    @MatchField("项目名称")
    //@Field
    private String name;

    //项目负责人ID
    @MatchField(value = "项目负责人", checkId = true)
    private String projectLeaderId;

    @Excel(name = "项目负责人", orderNum = "2")
    @MatchField(value = "项目负责人")
    private String projectLeader;

    private String deptId;

    @Excel(name = "项目组成员", orderNum = "3")
    @MatchField("人员")
    private String projectMemberNames;

    private String[] memberNames;

    @MatchField(value = "人员", checkId = true)
    private String[] projectMemberIds;

    // @Excel(name = "所属业务ID", orderNum = "5")
    @MatchField("所属业务ID")
    private String budgetId;

    /**
     * 经营类 （OA 项目编号）
     */
    private String budgetDomainId;

    /**
     * 经营类 （OA 项目编号）
     */
    private String contractNoDomainId;

    @Excel(name = "所属业务", orderNum = "4")
    @MatchField("所属业务")
    private String budget;

    //@Excel(name = "业务类型ID", orderNum = "7")
    @MatchField("业务类型ID")
    private String businessId;

    @Excel(name = "业务类型", orderNum = "5")
    @MatchField("业务类型")
    private String business;

    @Excel(name = "业务方", orderNum = "6")
    @MatchField("业务方")
    private String projectOwner;

    @Excel(name = "创建时间", orderNum = "7", exportFormat = "yyyy-MM-dd HH:mm:ss", importFormat = "yyyy-MM-dd HH:mm:ss")
    @MatchField("创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @Excel(name = "项目开始时间", orderNum = "8", exportFormat = "yyyy-MM-dd HH:mm:ss", importFormat = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date projectStartTime;

    @Excel(name = "项目完成状态", orderNum = "9")
    private String finishedStatus;

    @Excel(name = "人力投入（人/天）", orderNum = "10")
    @MatchField("人力投入")
    @Field(type = FieldType.Integer)
    private Integer personInput;

    //项目组成员  name 和 id
    private List<Map<String, String>> mapList;

    private List<Map<String,String>> userIdDeptNameMapList;


    private Date createTime;
    private Date modifyTime;

    //业务 n:1
    //质量考核 1:1
    private GualityInspection gualityInspection;

    //任务 1:n
    private List<Task> raskList;

    //收入费用 1:n
    private List<RevenueExpense> revenueExpenseList;

    // 支出费用 1:n
    private List<ExpensesIncurred> expensesIncurredList;

    @MatchField("项目描述")
    private String projectDescription;

    @Field(type = FieldType.String, analyzer = "not_analyzed")
    private String createUserId;

    private String createUserName;

    //删除标记
    private Boolean delFlag;

    @Field(type = FieldType.String, analyzer = "not_analyzed")
    private String pm;

    @Field(type = FieldType.String, analyzer = "not_analyzed")
    /*
     * 项目所属业务所在部门
     */
    private String projectTeam;

    @MatchField(value = "合同编号")
    private String contractNo;

    //业务创建人字段
    @Field(type = FieldType.String, analyzer = "not_analyzed")
    private String businessCreateUserId;

    //提供给前段判断是否能点状态按钮 0表示进行中
    private String btnFlag;

    private String approveUserId;

    @ApiModelProperty("是否具有修改权限")
    private Boolean manager;

    /**
     * 合同合计，转化为float型，存在失真，所以原始数据用 ，该字段仅应用于范围筛选
     *
     * @see #contractAmountStr
     */
    @Field(type = FieldType.Float)
    private float contractAmount;

    /**
     * 保证 表单中的数据精度
     * 项目搜索中 这个字段表示累计投入工时
     */
    private String contractAmountStr;
    /**
     * 开票金额
     */
    private List<ProjectContractInvoiceEO> projectContractInvoiceEOList;

    /**
     * 开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date projectBeginTime;

    /**
     * 结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date projectEndTime;

    /** 0. 经营类项目 ， 1.日常类事务项目 ， 2. 科研类项目*/
    private int projectType;

    private int projectYear;

    private int projectMonth;

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

    @Field(type = FieldType.String, analyzer = "not_analyzed")
    private String province;

    //乙方
    @Field(type = FieldType.String, analyzer = "not_analyzed")
    private String partyB;

    //项目成员更新时间
    @Field(type = FieldType.Date, analyzer = "not_analyzed")
    private Date projectMemberUpdatingTime;

    //    // 所选部门ID和类型 如类型=1 只选当前自身 类型=2 包含子部门
    private ArrayList<Map<String,String>> deptInfoListMap = new ArrayList<>();;
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

    // 项目下所有任务的成员id
    @Field(ignoreFields = "taskMemberIdList")
    private List<String> taskMemberIdList;
}
