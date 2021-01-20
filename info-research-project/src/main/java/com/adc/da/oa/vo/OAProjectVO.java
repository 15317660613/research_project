package com.adc.da.oa.vo;

import com.adc.da.budget.entity.BudgetEO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * describe:
 *
 * @author 李坤澔
 *     date 2019-09-18
 */
@Getter
@Setter
@ToString
public class OAProjectVO {

    /*
     * 合同名称
     * 合同编号
     * 合同企业名称(客户名称)
     * 合同金额
     * 合同签订时间
     * 开始时间(生效日期)
     * 结束时间(终止日期)
     *
     * 项目负责人
     * 项目组成员 （项目组成员）
     * 业务类型(所属板块)
     * 所属业务(项目名称)
     * 预计人力投入
     * 项目描述
     */

    /**
     * 合同名称
     */
    private String contractName;

    /**
     * 合同编号
     */
    private String contractNO;

    /**
     * 合同企业名称
     */
    private String contractCompany;

    /**
     * 合同金额
     */
    private String contractAmount;

    /**
     * 合同签订时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date contractCreateTime;

    /**
     * 开始时间(生效日期)
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date contractBeginTime;

    /**
     * 结束时间(终止日期)
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date contractEndTime;



    /**
     * 负责人部门
     */
    private String leaderDept;

    /**
     * * 所属业务(项目名称) / 编号
     *
     * @see BudgetEO#getDomainId()
     */
    private String budgetNO;

    /**
     * 业务类型(所属板块)
     */
    private String type;

    /**
     * 作废/执行
     *
     */
    private String state;


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

    // 合同年份
    private int projectYear;

    //合同月份
    private int projectMonth;

    //合同省份
    private String province;

    //乙方
    private String partyB;

}
