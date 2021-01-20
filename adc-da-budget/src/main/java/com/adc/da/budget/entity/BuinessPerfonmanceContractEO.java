package com.adc.da.budget.entity;

import com.adc.da.base.entity.BaseEntity;
import com.adc.da.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Date;

/**
 * <b>功能：</b>BUINESS_PERFONMANCE_CONTRACT BuinessPerfonmanceContractEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-11-08 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class BuinessPerfonmanceContractEO extends BaseEntity {

    private String id;
    @Excel(name = "部门", orderNum = "2")
    private String deptId;
    @Excel(name = "合同编号", orderNum = "1")
    private String contractNo;
    @Excel(name = "乙方", orderNum = "3")
    private String secondParty;
    @Excel(name = "合同名称", orderNum = "4")
    private String contractName;
    @Excel(name = "合同金额", orderNum = "5")
    private String conMoney;
    @Excel(name = "生效日期", orderNum = "6")
    private String startDate;
    @Excel(name = "终止日期", orderNum = "7")
    private String endDate;
    @Excel(name = "是否上会审批", orderNum = "8")
    private String ifWillApprove;
    @Excel(name = "上会时间", orderNum = "9")
    private String willTime;
    @Excel(name = "上会项目名称", orderNum = "10")
    private String willProjectName;
    @Excel(name = "合同状态", orderNum = "11")
    private String conState;
    @Excel(name = "合同归属", orderNum = "12")
    private String contractBelong;
    @Excel(name = "合同费用类型", orderNum = "13")
    private String conType;
    @Excel(name = "若为试验车和库存商品，2018年前是否处置", orderNum = "14")
    private String isStock;
    @Excel(name = "已开票", orderNum = "15")
    private String hasInvoice;
    @Excel(name = "已支付", orderNum = "16")
    private String prepaid;
    @Excel(name = "未开票", orderNum = "17")
    private String wkpje;
    @Excel(name = "未支付", orderNum = "18")
    private String wzcje;
    @Excel(name = "12月报销", orderNum = "19")
    private String expenseNov;
    @Excel(name = "12月支出", orderNum = "20")
    private String expenditureNov;
    @Excel(name = "2019年1月报销", orderNum = "21")
    private String yjbxfpje2018s4;
    @Excel(name = "2019年1月支付", orderNum = "22")
    private String yjzfje2018s4;
    @Excel(name = "2019年2月报销", orderNum = "23")
    private String yjbxfpje2019;
    @Excel(name = "2019年2月支付", orderNum = "24")
    private String yjzfje2019;
    @Excel(name = "责任人", orderNum = "25")
    private String zerenren;
    @Excel(name = "备注", orderNum = "26")
    private String remark;
    @Excel(name = "是否锁定", orderNum = "27")
    private String islock;

//    @Excel(name = "合同类型1", orderNum = "10")
    private String conType1;
//    @Excel(name = "2020年预计报销发票金额", orderNum = "18")
    private String yjbxfpje2020;
//    @Excel(name = "2020年预计支出金额", orderNum = "19")
    private String yjzfje2020;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date startTimre;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updatetTime;
    private String admin;
    private String processInstanceId;



    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>deptId -> deptId</li>
     * <li>contractNo -> contractNo</li>
     * <li>secondParty -> secondParty</li>
     * <li>startDate -> startDate</li>
     * <li>endDate -> endDate</li>
     * <li>contractName -> contractName</li>
     * <li>conMoney -> conMoney</li>
     * <li>conState -> conState</li>
     * <li>conType -> conType</li>
     * <li>conType1 -> conType1</li>
     * <li>isStock -> isStock</li>
     * <li>wkpje -> wkpje</li>
     * <li>wzcje -> wzcje</li>
     * <li>yjbxfpje2018s4 -> yjbxfpje2018s4</li>
     * <li>yjzfje2018s4 -> yjzfje2018s4</li>
     * <li>yjbxfpje2019 -> yjbxfpje2019</li>
     * <li>yjzfje2019 -> yjzfje2019</li>
     * <li>yjbxfpje2020 -> yjbxfpje2020</li>
     * <li>yjzfje2020 -> yjzfje2020</li>
     * <li>zerenren -> zerenren</li>
     * <li>remark -> remark</li>
     * <li>startTimre -> startTimre</li>
     * <li>updatetTime -> updatetTime</li>
     * <li>admin -> admin</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) {
            return null;
        }
        switch (fieldName) {
            case "id": return "id";
            case "deptId": return "deptId";
            case "contractNo": return "contractNo";
            case "secondParty": return "secondParty";
            case "startDate": return "startDate";
            case "endDate": return "endDate";
            case "contractName": return "contractName";
            case "conMoney": return "conMoney";
            case "conState": return "conState";
            case "conType": return "conType";
            case "conType1": return "conType1";
            case "isStock": return "isStock";
            case "wkpje": return "wkpje";
            case "wzcje": return "wzcje";
            case "yjbxfpje2018s4": return "yjbxfpje2018s4";
            case "yjzfje2018s4": return "yjzfje2018s4";
            case "yjbxfpje2019": return "yjbxfpje2019";
            case "yjzfje2019": return "yjzfje2019";
            case "yjbxfpje2020": return "yjbxfpje2020";
            case "yjzfje2020": return "yjzfje2020";
            case "zerenren": return "zerenren";
            case "remark": return "remark";
            case "startTimre": return "startTimre";
            case "updatetTime": return "updatetTime";
            case "admin": return "admin";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>deptId -> deptId</li>
     * <li>contractNo -> contractNo</li>
     * <li>secondParty -> secondParty</li>
     * <li>startDate -> startDate</li>
     * <li>endDate -> endDate</li>
     * <li>contractName -> contractName</li>
     * <li>conMoney -> conMoney</li>
     * <li>conState -> conState</li>
     * <li>conType -> conType</li>
     * <li>conType1 -> conType1</li>
     * <li>isStock -> isStock</li>
     * <li>wkpje -> wkpje</li>
     * <li>wzcje -> wzcje</li>
     * <li>yjbxfpje2018s4 -> yjbxfpje2018s4</li>
     * <li>yjzfje2018s4 -> yjzfje2018s4</li>
     * <li>yjbxfpje2019 -> yjbxfpje2019</li>
     * <li>yjzfje2019 -> yjzfje2019</li>
     * <li>yjbxfpje2020 -> yjbxfpje2020</li>
     * <li>yjzfje2020 -> yjzfje2020</li>
     * <li>zerenren -> zerenren</li>
     * <li>remark -> remark</li>
     * <li>startTimre -> startTimre</li>
     * <li>updatetTime -> updatetTime</li>
     * <li>admin -> admin</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) {
            return null;
        }
        switch (columnName) {
            case "id": return "id";
            case "deptId": return "deptId";
            case "contractNo": return "contractNo";
            case "secondParty": return "secondParty";
            case "startDate": return "startDate";
            case "endDate": return "endDate";
            case "contractName": return "contractName";
            case "conMoney": return "conMoney";
            case "conState": return "conState";
            case "conType": return "conType";
            case "conType1": return "conType1";
            case "isStock": return "isStock";
            case "wkpje": return "wkpje";
            case "wzcje": return "wzcje";
            case "yjbxfpje2018s4": return "yjbxfpje2018s4";
            case "yjzfje2018s4": return "yjzfje2018s4";
            case "yjbxfpje2019": return "yjbxfpje2019";
            case "yjzfje2019": return "yjzfje2019";
            case "yjbxfpje2020": return "yjbxfpje2020";
            case "yjzfje2020": return "yjzfje2020";
            case "zerenren": return "zerenren";
            case "remark": return "remark";
            case "startTimre": return "startTimre";
            case "updatetTime": return "updatetTime";
            case "admin": return "admin";
            default: return null;
        }
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
    public String getDeptId() {
        return this.deptId;
    }

    /**  **/
    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    /**  **/
    public String getContractNo() {
        return this.contractNo;
    }

    /**  **/
    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    /**  **/
    public String getSecondParty() {
        return this.secondParty;
    }

    /**  **/
    public void setSecondParty(String secondParty) {
        this.secondParty = secondParty;
    }

    /**  **/
    public String getStartDate() {
        return this.startDate;
    }

    /**  **/
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**  **/
    public String getEndDate() {
        return this.endDate;
    }

    /**  **/
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**  **/
    public String getContractName() {
        return this.contractName;
    }

    /**  **/
    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    /**  **/
    public String getConMoney() {
        return this.conMoney;
    }

    /**  **/
    public void setConMoney(String conMoney) {
        this.conMoney = conMoney;
    }

    /**  **/
    public String getConState() {
        return this.conState;
    }

    /**  **/
    public void setConState(String conState) {
        this.conState = conState;
    }

    /**  **/
    public String getConType() {
        return this.conType;
    }

    /**  **/
    public void setConType(String conType) {
        this.conType = conType;
    }

    /**  **/
    public String getConType1() {
        return this.conType1;
    }

    /**  **/
    public void setConType1(String conType1) {
        this.conType1 = conType1;
    }

    /**  **/
    public String getIsStock() {
        return this.isStock;
    }

    /**  **/
    public void setIsStock(String isStock) {
        this.isStock = isStock;
    }

    /**  **/
    public String getWkpje() {
        return this.wkpje;
    }

    /**  **/
    public void setWkpje(String wkpje) {
        this.wkpje = wkpje;
    }

    /**  **/
    public String getWzcje() {
        return this.wzcje;
    }

    /**  **/
    public void setWzcje(String wzcje) {
        this.wzcje = wzcje;
    }

    /**  **/
    public String getYjbxfpje2018s4() {
        return this.yjbxfpje2018s4;
    }

    /**  **/
    public void setYjbxfpje2018s4(String yjbxfpje2018s4) {
        this.yjbxfpje2018s4 = yjbxfpje2018s4;
    }

    /**  **/
    public String getYjzfje2018s4() {
        return this.yjzfje2018s4;
    }

    /**  **/
    public void setYjzfje2018s4(String yjzfje2018s4) {
        this.yjzfje2018s4 = yjzfje2018s4;
    }

    /**  **/
    public String getYjbxfpje2019() {
        return this.yjbxfpje2019;
    }

    /**  **/
    public void setYjbxfpje2019(String yjbxfpje2019) {
        this.yjbxfpje2019 = yjbxfpje2019;
    }

    /**  **/
    public String getYjzfje2019() {
        return this.yjzfje2019;
    }

    /**  **/
    public void setYjzfje2019(String yjzfje2019) {
        this.yjzfje2019 = yjzfje2019;
    }

    /**  **/
    public String getYjbxfpje2020() {
        return this.yjbxfpje2020;
    }

    /**  **/
    public void setYjbxfpje2020(String yjbxfpje2020) {
        this.yjbxfpje2020 = yjbxfpje2020;
    }

    /**  **/
    public String getYjzfje2020() {
        return this.yjzfje2020;
    }

    /**  **/
    public void setYjzfje2020(String yjzfje2020) {
        this.yjzfje2020 = yjzfje2020;
    }

    /**  **/
    public String getZerenren() {
        return this.zerenren;
    }

    /**  **/
    public void setZerenren(String zerenren) {
        this.zerenren = zerenren;
    }

    /**  **/
    public String getRemark() {
        return this.remark;
    }

    /**  **/
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**  **/
    public Date getStartTimre() {
        return this.startTimre;
    }

    /**  **/
    public void setStartTimre(Date startTimre) {
        this.startTimre = startTimre;
    }

    /**  **/
    public Date getUpdatetTime() {
        return this.updatetTime;
    }

    /**  **/
    public void setUpdatetTime(Date updatetTime) {
        this.updatetTime = updatetTime;
    }

    /**  **/
    public String getAdmin() {
        return this.admin;
    }

    /**  **/
    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getIslock() {
        return islock;
    }

    public void setIslock(String islock) {
        this.islock = islock;
    }

    public String getIfWillApprove() {
        return ifWillApprove;
    }

    public void setIfWillApprove(String ifWillApprove) {
        this.ifWillApprove = ifWillApprove;
    }

    public String getWillTime() {
        return willTime;
    }

    public void setWillTime(String willTime) {
        this.willTime = willTime;
    }

    public String getWillProjectName() {
        return willProjectName;
    }

    public void setWillProjectName(String willProjectName) {
        this.willProjectName = willProjectName;
    }

    public String getContractBelong() {
        return contractBelong;
    }

    public void setContractBelong(String contractBelong) {
        this.contractBelong = contractBelong;
    }

    public String getHasInvoice() {
        return hasInvoice;
    }

    public void setHasInvoice(String hasInvoice) {
        this.hasInvoice = hasInvoice;
    }

    public String getPrepaid() {
        return prepaid;
    }

    public void setPrepaid(String prepaid) {
        this.prepaid = prepaid;
    }

    public String getExpenseNov() {
        return expenseNov;
    }

    public void setExpenseNov(String expenseNov) {
        this.expenseNov = expenseNov;
    }

    public String getExpenditureNov() {
        return expenditureNov;
    }

    public void setExpenditureNov(String expenditureNov) {
        this.expenditureNov = expenditureNov;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }
}
