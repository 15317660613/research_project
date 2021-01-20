package com.adc.da.crm.vo;

import com.adc.da.base.entity.BaseEntity;
import com.adc.da.crm.annotation.MatchField;
import com.adc.da.crm.annotation.MatchFieldCollection;
import com.adc.da.crm.entity.InvoiceInfoEO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <b>功能：</b>INVOICE_APPROVAL InvoiceApprovalEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-11-29 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class InvoiceApprovalVO extends BaseEntity {

    @MatchField("合同名称")
    private String contractName;
    private Long contractAmount;
    private Long invoiceAmount;
    @MatchField("所属平台")
    private String platform;
    @MatchField("所属板块")
    private String block;
    @MatchField("发票类型")
    private String invoiceType;
    @MatchField("费用名称")
    private String costName;
    @MatchField("付款单位")
    private String payCompany;
    @MatchField("发票收件人")
    private String receiver;
    @MatchField("联系电话")
    private String tel;
    @MatchField("单位名称")
    private String company;
    @MatchField("地址")
    private String address;
    @MatchField("开票单位")
    private String invoiceCompany;
    @MatchField("备注")
    private String test;
    @MatchField("是否含仪器仪表")
    private String meter;
    @MatchField("出库单编号")
    private String outNo;
    @MatchField("审批意见")
    private String approvalView;
    private String delFlag;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createdTime;
    private String createdUser;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime2;
    private String modifiedUser2;
    private String id;
    @MatchField("申请日期")
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date applyData;
    @MatchField("单据编号")
    private String docNo;
    @MatchField("申请人")
    private String applyUser;
    @MatchField("申请部门")
    private String applyDept;
    @MatchField("合同编号")
    private String contractNo;

    @MatchFieldCollection
    private List<InvoiceInfoEO> invoiceInfoEOList = new ArrayList<>();

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>customerName -> customer_name</li>
     * <li>contractAmount -> contract_amount</li>
     * <li>invoiceAmount -> invoice_amount</li>
     * <li>platform -> platform</li>
     * <li>block -> block</li>
     * <li>invoiceType -> invoice_type</li>
     * <li>costName -> cost_name</li>
     * <li>payCompany -> pay_company</li>
     * <li>receiver -> receiver</li>
     * <li>tel -> tel</li>
     * <li>company -> company</li>
     * <li>address -> address</li>
     * <li>invoiceCompany -> invoice_company</li>
     * <li>test -> test</li>
     * <li>meter -> meter</li>
     * <li>outNo -> out_no</li>
     * <li>view -> view</li>
     * <li>delFlag -> del_flag</li>
     * <li>createdTime -> created_time</li>
     * <li>createdUser -> created_user</li>
     * <li>modifiedTime2 -> modified_time2</li>
     * <li>modifiedUser2 -> modified_user2</li>
     * <li>id -> id</li>
     * <li>applyData -> apply_data</li>
     * <li>docNo -> doc_no</li>
     * <li>applyUser -> apply_user</li>
     * <li>applyDept -> apply_dept</li>
     * <li>contractNo -> contract_no</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) {return null;}
        switch (fieldName) {
            case "customerName": return "customer_name";
            case "contractAmount": return "contract_amount";
            case "invoiceAmount": return "invoice_amount";
            case "platform": return "platform";
            case "block": return "block";
            case "invoiceType": return "invoice_type";
            case "costName": return "cost_name";
            case "payCompany": return "pay_company";
            case "receiver": return "receiver";
            case "tel": return "tel";
            case "company": return "company";
            case "address": return "address";
            case "invoiceCompany": return "invoice_company";
            case "test": return "test";
            case "meter": return "meter";
            case "outNo": return "out_no";
            case "view": return "view";
            case "delFlag": return "del_flag";
            case "createdTime": return "created_time";
            case "createdUser": return "created_user";
            case "modifiedTime2": return "modified_time2";
            case "modifiedUser2": return "modified_user2";
            case "id": return "id";
            case "applyData": return "apply_data";
            case "docNo": return "doc_no";
            case "applyUser": return "apply_user";
            case "applyDept": return "apply_dept";
            case "contractNo": return "contract_no";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>customer_name -> customerName</li>
     * <li>contract_amount -> contractAmount</li>
     * <li>invoice_amount -> invoiceAmount</li>
     * <li>platform -> platform</li>
     * <li>block -> block</li>
     * <li>invoice_type -> invoiceType</li>
     * <li>cost_name -> costName</li>
     * <li>pay_company -> payCompany</li>
     * <li>receiver -> receiver</li>
     * <li>tel -> tel</li>
     * <li>company -> company</li>
     * <li>address -> address</li>
     * <li>invoice_company -> invoiceCompany</li>
     * <li>test -> test</li>
     * <li>meter -> meter</li>
     * <li>out_no -> outNo</li>
     * <li>view -> view</li>
     * <li>del_flag -> delFlag</li>
     * <li>created_time -> createdTime</li>
     * <li>created_user -> createdUser</li>
     * <li>modified_time2 -> modifiedTime2</li>
     * <li>modified_user2 -> modifiedUser2</li>
     * <li>id -> id</li>
     * <li>apply_data -> applyData</li>
     * <li>doc_no -> docNo</li>
     * <li>apply_user -> applyUser</li>
     * <li>apply_dept -> applyDept</li>
     * <li>contract_no -> contractNo</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) {return null;}
        switch (columnName) {
            case "customer_name": return "customerName";
            case "contract_amount": return "contractAmount";
            case "invoice_amount": return "invoiceAmount";
            case "platform": return "platform";
            case "block": return "block";
            case "invoice_type": return "invoiceType";
            case "cost_name": return "costName";
            case "pay_company": return "payCompany";
            case "receiver": return "receiver";
            case "tel": return "tel";
            case "company": return "company";
            case "address": return "address";
            case "invoice_company": return "invoiceCompany";
            case "test": return "test";
            case "meter": return "meter";
            case "out_no": return "outNo";
            case "view": return "view";
            case "del_flag": return "delFlag";
            case "created_time": return "createdTime";
            case "created_user": return "createdUser";
            case "modified_time2": return "modifiedTime2";
            case "modified_user2": return "modifiedUser2";
            case "id": return "id";
            case "apply_data": return "applyData";
            case "doc_no": return "docNo";
            case "apply_user": return "applyUser";
            case "apply_dept": return "applyDept";
            case "contract_no": return "contractNo";
            default: return null;
        }
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    /**  **/
    public Long getContractAmount() {
        return this.contractAmount;
    }

    /**  **/
    public void setContractAmount(Long contractAmount) {
        this.contractAmount = contractAmount;
    }

    /**  **/
    public Long getInvoiceAmount() {
        return this.invoiceAmount;
    }

    /**  **/
    public void setInvoiceAmount(Long invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    /**  **/
    public String getPlatform() {
        return this.platform;
    }

    /**  **/
    public void setPlatform(String platform) {
        this.platform = platform;
    }

    /**  **/
    public String getBlock() {
        return this.block;
    }

    /**  **/
    public void setBlock(String block) {
        this.block = block;
    }

    /**  **/
    public String getInvoiceType() {
        return this.invoiceType;
    }

    /**  **/
    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    /**  **/
    public String getCostName() {
        return this.costName;
    }

    /**  **/
    public void setCostName(String costName) {
        this.costName = costName;
    }

    /**  **/
    public String getPayCompany() {
        return this.payCompany;
    }

    /**  **/
    public void setPayCompany(String payCompany) {
        this.payCompany = payCompany;
    }

    /**  **/
    public String getReceiver() {
        return this.receiver;
    }

    /**  **/
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    /**  **/
    public String getTel() {
        return this.tel;
    }

    /**  **/
    public void setTel(String tel) {
        this.tel = tel;
    }

    /**  **/
    public String getCompany() {
        return this.company;
    }

    /**  **/
    public void setCompany(String company) {
        this.company = company;
    }

    /**  **/
    public String getAddress() {
        return this.address;
    }

    /**  **/
    public void setAddress(String address) {
        this.address = address;
    }

    /**  **/
    public String getInvoiceCompany() {
        return this.invoiceCompany;
    }

    /**  **/
    public void setInvoiceCompany(String invoiceCompany) {
        this.invoiceCompany = invoiceCompany;
    }

    /**  **/
    public String getTest() {
        return this.test;
    }

    /**  **/
    public void setTest(String test) {
        this.test = test;
    }

    /**  **/
    public String getMeter() {
        return this.meter;
    }

    /**  **/
    public void setMeter(String meter) {
        this.meter = meter;
    }

    /**  **/
    public String getOutNo() {
        return this.outNo;
    }

    /**  **/
    public void setOutNo(String outNo) {
        this.outNo = outNo;
    }

    public String getApprovalView() {
        return approvalView;
    }

    public void setApprovalView(String approvalView) {
        this.approvalView = approvalView;
    }

    /**  **/
    public String getDelFlag() {
        return this.delFlag;
    }

    /**  **/
    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    /**  **/
    public Date getCreatedTime() {
        return this.createdTime;
    }

    /**  **/
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    /**  **/
    public String getCreatedUser() {
        return this.createdUser;
    }

    /**  **/
    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }

    /**  **/
    public Date getModifiedTime2() {
        return this.modifiedTime2;
    }

    /**  **/
    public void setModifiedTime2(Date modifiedTime2) {
        this.modifiedTime2 = modifiedTime2;
    }

    /**  **/
    public String getModifiedUser2() {
        return this.modifiedUser2;
    }

    /**  **/
    public void setModifiedUser2(String modifiedUser2) {
        this.modifiedUser2 = modifiedUser2;
    }

    /**  **/
    public String getId() {
        return this.id;
    }

    /**  **/
    public void setId(String id) {
        this.id = id;
    }

    /**  **/
    public Date getApplyData() {
        return this.applyData;
    }

    /**  **/
    public void setApplyData(Date applyData) {
        this.applyData = applyData;
    }

    /**  **/
    public String getDocNo() {
        return this.docNo;
    }

    /**  **/
    public void setDocNo(String docNo) {
        this.docNo = docNo;
    }

    /**  **/
    public String getApplyUser() {
        return this.applyUser;
    }

    /**  **/
    public void setApplyUser(String applyUser) {
        this.applyUser = applyUser;
    }

    /**  **/
    public String getApplyDept() {
        return this.applyDept;
    }

    /**  **/
    public void setApplyDept(String applyDept) {
        this.applyDept = applyDept;
    }

    /**  **/
    public String getContractNo() {
        return this.contractNo;
    }

    /**  **/
    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public List<InvoiceInfoEO> getInvoiceInfoEOList() {
        return invoiceInfoEOList;
    }

    public void setInvoiceInfoEOList(List<InvoiceInfoEO> invoiceInfoEOList) {
        this.invoiceInfoEOList = invoiceInfoEOList;
    }
}
