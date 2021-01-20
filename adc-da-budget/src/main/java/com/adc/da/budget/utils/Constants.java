package com.adc.da.budget.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * // 常量类
 *
 * @author ZhengZhiwei
 * @date 2018-11-28
 */
public class Constants {

    //客户档案表
    protected static final Map<String, List<String>> CUSTOMER_RECORD = new HashMap<>();
    //客户联系信息
    protected static final List<String> CUSTOMER_CONTACT_INFO_LIST = new ArrayList<>();

    //客户拜访记录
    protected static final List<String> CUSTOMER_VISIT_RECORD_LIST = new ArrayList<>();

    static{
        CUSTOMER_CONTACT_INFO_LIST.add("部门");
        CUSTOMER_CONTACT_INFO_LIST.add("联系人");
        CUSTOMER_CONTACT_INFO_LIST.add("职务");
        CUSTOMER_CONTACT_INFO_LIST.add("性别");
        CUSTOMER_CONTACT_INFO_LIST.add("手机");
        CUSTOMER_CONTACT_INFO_LIST.add("座机");
        CUSTOMER_CONTACT_INFO_LIST.add("邮箱");
        CUSTOMER_CONTACT_INFO_LIST.add("备注");
        CUSTOMER_RECORD.put("CUSTOMER_CONTACT",CUSTOMER_CONTACT_INFO_LIST);
        CUSTOMER_VISIT_RECORD_LIST.add("拜访时间");
        CUSTOMER_VISIT_RECORD_LIST.add("拜访部门");
        CUSTOMER_VISIT_RECORD_LIST.add("拜访人员");
        CUSTOMER_VISIT_RECORD_LIST.add("沟通内容");
        CUSTOMER_VISIT_RECORD_LIST.add("备注");
        CUSTOMER_VISIT_RECORD_LIST.add("出差审批流程");
        CUSTOMER_RECORD.put("CUSTOMER_VISIT_RECORD",CUSTOMER_VISIT_RECORD_LIST);

    }

    //出差审批单
    protected static final Map<String, List<String>> TRAVEL_APPROVAL = new HashMap<>();
    //客户拜访记录
    protected static final List<String> BTRAVEL_CUSVISIT_RECORD_LIST = new ArrayList<>();
    //项目拜访情况
    protected static final List<String> BTRAVEL_PROVISIT_INFO_LIST = new ArrayList<>();

    static {
        BTRAVEL_CUSVISIT_RECORD_LIST.add("拜访时间");
        BTRAVEL_CUSVISIT_RECORD_LIST.add("拜访部门");
        BTRAVEL_CUSVISIT_RECORD_LIST.add("拜访人员");
        BTRAVEL_CUSVISIT_RECORD_LIST.add("沟通内容");
        BTRAVEL_CUSVISIT_RECORD_LIST.add("备注");
        TRAVEL_APPROVAL.put("BTRAVEL_CUSVISIT_RECORD", BTRAVEL_CUSVISIT_RECORD_LIST);
        BTRAVEL_PROVISIT_INFO_LIST.add("项目编号");
        BTRAVEL_PROVISIT_INFO_LIST.add("项目名称");
        BTRAVEL_PROVISIT_INFO_LIST.add("沟通内容");
        BTRAVEL_PROVISIT_INFO_LIST.add("备注");
        TRAVEL_APPROVAL.put("BTRAVEL_CUSVISIT_RECORD", BTRAVEL_PROVISIT_INFO_LIST);
    }

    //项目目标值底表
    protected static final Map<String, List<String>> PROJECT_TARGET_BOTTOM = new HashMap();
    //预算分解
    protected static final List<String> PROJECT_TARGET_BOTTOM_LIST = new ArrayList<>();

    static {
        PROJECT_TARGET_BOTTOM_LIST.add("月份");
        PROJECT_TARGET_BOTTOM_LIST.add("合同目标值");
        PROJECT_TARGET_BOTTOM_LIST.add("开票目标值");
        PROJECT_TARGET_BOTTOM.put("PROJECT_TARGET_BOTTOM", PROJECT_TARGET_BOTTOM_LIST);
    }

    //发票申请表
    protected static final Map<String, List<String>> INVOICE_INFO = new HashMap<>();
    //开票信息
    protected static final List<String> INVOICE_INFO_LIST = new ArrayList<>();

    static {
        INVOICE_INFO_LIST.add("项目编号");
        INVOICE_INFO_LIST.add("项目名称");
        INVOICE_INFO_LIST.add("开票金额");
        INVOICE_INFO_LIST.add("开票信息");
        INVOICE_INFO.put("INVOICE_INFO", INVOICE_INFO_LIST);
    }
    //合同申请审批表
    protected static final Map<String, List<String>> CONTRACT_APPROVAL = new HashMap<>();
    //项目合同金额明细
    protected static final List<String> PROJECT_CONTRACT_DETAIL_LIST = new ArrayList<>();
    //项目开票信息明细
    protected static final List<String> PROJECT_INVOICE_INFO_LIST = new ArrayList<>();

    static {
        PROJECT_CONTRACT_DETAIL_LIST.add("项目编号");
        PROJECT_CONTRACT_DETAIL_LIST.add("项目名称");
        PROJECT_CONTRACT_DETAIL_LIST.add("合同金额");
        PROJECT_CONTRACT_DETAIL_LIST.add("备注");
        CONTRACT_APPROVAL.put("PROJECT_CONTRACT_DETAIL", PROJECT_CONTRACT_DETAIL_LIST);
        PROJECT_INVOICE_INFO_LIST.add("项目编号");
        PROJECT_INVOICE_INFO_LIST.add("项目名称");
        PROJECT_INVOICE_INFO_LIST.add("开票金额");
        PROJECT_INVOICE_INFO_LIST.add("开票时间");
        CONTRACT_APPROVAL.put("PROJECT_INVOICE_INFO", PROJECT_INVOICE_INFO_LIST);
    }

}
