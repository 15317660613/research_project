package com.adc.da.budget.entity;

import com.adc.da.base.entity.BaseEntity;
import com.adc.da.excel.annotation.Excel;
import com.adc.da.excel.annotation.ExcelVerify;
import com.fasterxml.jackson.annotation.JsonIgnore;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Date;

/**
 * <b>功能：</b>BUINESS_COMFIRM_CONTRACT BuinessComfirmContractEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-11-08 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class BuinessComfirmContractEO extends BaseEntity {
    private String id;
    @Excel(name = "上会时间", orderNum = "1")
    private String meetingDate;
    @Excel(name = "部门", orderNum = "2")
    private String deptId;
    @Excel(name = "项目名称", orderNum = "3")
    private String projectName;
    @Excel(name = "预算金额", orderNum = "4")
    private String proMoney;
    @Excel(name = "项目状态", orderNum = "5")
    private String proState;
//    @Excel(name = "合同签订与否", orderNum = "6")
    private String isContract;
//    @Excel(name = "项目类型1", orderNum = "7")
    private String proType1;
//    @Excel(name = "项目类型", orderNum = "8")
    private String proType;
//    @Excel(name = "库存商品2018年前是否处置（项目类型选择库存商品填写）", orderNum = "9", needMerge = true)
    private String isStock;
    @Excel(name = "合同编号", orderNum = "6")
    private String contractNo;
    @Excel(name = "乙方", orderNum = "7")
    private String secondParty;
    @Excel(name = "合同金额", orderNum = "8")
    private String proMoney2;
//    @Excel(name = "已报销发票金额", orderNum = "13")
    private String ybxfpje;
//    @Excel(name = "已支付项目金额", orderNum = "14")
    private String yzfxmje;
//    @Excel(name = "2018年10-12月报销发票金额", orderNum = "15")
    private String bxfpje2018s4;
//    @Excel(name = "2018年10-12月支付金额", orderNum = "16")
    private String zfje2018s4;
//    @Excel(name = "2019年预计报销发票金额", orderNum = "17")
    private String yjbxfpje2019;
//    @Excel(name = "2019年预计支付金额", orderNum = "18")
    private String yjzfje2019;
//    @Excel(name = "2020年预计报销发票金额", orderNum = "19")
    private String yjbxfpje2020;
//    @Excel(name = "2020年预计支付金额", orderNum = "20")
    private String yjzfje2020;
    @Excel(name = "责任人", orderNum = "12")
    private String zerenren;
    @Excel(name = "备注", orderNum = "13")
    private String remark;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date stratTime;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    private String admin;
    @Excel(name = "是否继续执行", orderNum = "9")
    private String ifContinueExecute;
    @Excel(name = "合同类型", orderNum = "10")
    private String contractType;
    @Excel(name = "合同签订时间", orderNum = "11")
    private String contractSigningTime;
    @Excel(name = "是否锁定", orderNum = "14")
    private String islock;
    private String processInstanceId;



    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>meetingDate -> meetingDate</li>
     * <li>deptId -> deptId</li>
     * <li>projectName -> projectName</li>
     * <li>proMoney -> proMoney</li>
     * <li>proState -> proState</li>
     * <li>isContract -> isContract</li>
     * <li>proType1 -> proType1</li>
     * <li>proType -> proType</li>
     * <li>isStock -> isStock</li>
     * <li>contractNo -> contractNo</li>
     * <li>secondParty -> secondParty</li>
     * <li>proMoney2 -> proMoney2</li>
     * <li>ybxfpje -> ybxfpje</li>
     * <li>yzfxmje -> yzfxmje</li>
     * <li>bxfpje2018s4 -> bxfpje2018s4</li>
     * <li>zfje2018s4 -> zfje2018s4</li>
     * <li>yjbxfpje2019 -> yjbxfpje2019</li>
     * <li>yjzfje2019 -> yjzfje2019</li>
     * <li>yjbxfpje2020 -> yjbxfpje2020</li>
     * <li>yjzfje2020 -> yjzfje2020</li>
     * <li>zerenren -> zerenren</li>
     * <li>remark -> remark</li>
     * <li>stratTime -> stratTime</li>
     * <li>updateTime -> updateTime</li>
     * <li>admin -> admin</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) {
            return null;
        }
        switch (fieldName) {
            case "id": return "id";
            case "meetingDate": return "meetingDate";
            case "deptId": return "deptId";
            case "projectName": return "projectName";
            case "proMoney": return "proMoney";
            case "proState": return "proState";
            case "isContract": return "isContract";
            case "proType1": return "proType1";
            case "proType": return "proType";
            case "isStock": return "isStock";
            case "contractNo": return "contractNo";
            case "secondParty": return "secondParty";
            case "proMoney2": return "proMoney2";
            case "ybxfpje": return "ybxfpje";
            case "yzfxmje": return "yzfxmje";
            case "bxfpje2018s4": return "bxfpje2018s4";
            case "zfje2018s4": return "zfje2018s4";
            case "yjbxfpje2019": return "yjbxfpje2019";
            case "yjzfje2019": return "yjzfje2019";
            case "yjbxfpje2020": return "yjbxfpje2020";
            case "yjzfje2020": return "yjzfje2020";
            case "zerenren": return "zerenren";
            case "remark": return "remark";
            case "stratTime": return "stratTime";
            case "updateTime": return "updateTime";
            case "admin": return "admin";
            case "ifContinueExecute": return "ifContinueExecute";
            case "contractType": return "contractType";
            case "contractSigningTime": return "contractSigningTime";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>meetingDate -> meetingDate</li>
     * <li>deptId -> deptId</li>
     * <li>projectName -> projectName</li>
     * <li>proMoney -> proMoney</li>
     * <li>proState -> proState</li>
     * <li>isContract -> isContract</li>
     * <li>proType1 -> proType1</li>
     * <li>proType -> proType</li>
     * <li>isStock -> isStock</li>
     * <li>contractNo -> contractNo</li>
     * <li>secondParty -> secondParty</li>
     * <li>proMoney2 -> proMoney2</li>
     * <li>ybxfpje -> ybxfpje</li>
     * <li>yzfxmje -> yzfxmje</li>
     * <li>bxfpje2018s4 -> bxfpje2018s4</li>
     * <li>zfje2018s4 -> zfje2018s4</li>
     * <li>yjbxfpje2019 -> yjbxfpje2019</li>
     * <li>yjzfje2019 -> yjzfje2019</li>
     * <li>yjbxfpje2020 -> yjbxfpje2020</li>
     * <li>yjzfje2020 -> yjzfje2020</li>
     * <li>zerenren -> zerenren</li>
     * <li>remark -> remark</li>
     * <li>stratTime -> stratTime</li>
     * <li>updateTime -> updateTime</li>
     * <li>admin -> admin</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) {
            return null;
        }
        switch (columnName) {
            case "id": return "id";
            case "meetingDate": return "meetingDate";
            case "deptId": return "deptId";
            case "projectName": return "projectName";
            case "proMoney": return "proMoney";
            case "proState": return "proState";
            case "isContract": return "isContract";
            case "proType1": return "proType1";
            case "proType": return "proType";
            case "isStock": return "isStock";
            case "contractNo": return "contractNo";
            case "secondParty": return "secondParty";
            case "proMoney2": return "proMoney2";
            case "ybxfpje": return "ybxfpje";
            case "yzfxmje": return "yzfxmje";
            case "bxfpje2018s4": return "bxfpje2018s4";
            case "zfje2018s4": return "zfje2018s4";
            case "yjbxfpje2019": return "yjbxfpje2019";
            case "yjzfje2019": return "yjzfje2019";
            case "yjbxfpje2020": return "yjbxfpje2020";
            case "yjzfje2020": return "yjzfje2020";
            case "zerenren": return "zerenren";
            case "remark": return "remark";
            case "stratTime": return "stratTime";
            case "updateTime": return "updateTime";
            case "admin": return "admin";
            case "ifContinueExecute": return "ifContinueExecute";
            case "contractType": return "contractType";
            case "contractSigningTime": return "contractSigningTime";
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
    public String getMeetingDate() {
        return this.meetingDate;
    }

    /**  **/
    public void setMeetingDate(String meetingDate) {
        this.meetingDate = meetingDate;
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
    public String getProjectName() {
        return this.projectName;
    }

    /**  **/
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /**  **/
    public String getProMoney() {
        return this.proMoney;
    }

    /**  **/
    public void setProMoney(String proMoney) {
        this.proMoney = proMoney;
    }

    /**  **/
    public String getProState() {
        return this.proState;
    }

    /**  **/
    public void setProState(String proState) {
        this.proState = proState;
    }

    /**  **/
    public String getIsContract() {
        return this.isContract;
    }

    /**  **/
    public void setIsContract(String isContract) {
        this.isContract = isContract;
    }

    /**  **/
    public String getProType1() {
        return this.proType1;
    }

    /**  **/
    public void setProType1(String proType1) {
        this.proType1 = proType1;
    }

    /**  **/
    public String getProType() {
        return this.proType;
    }

    /**  **/
    public void setProType(String proType) {
        this.proType = proType;
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
    public String getProMoney2() {
        return this.proMoney2;
    }

    /**  **/
    public void setProMoney2(String proMoney2) {
        this.proMoney2 = proMoney2;
    }

    /**  **/
    public String getYbxfpje() {
        return this.ybxfpje;
    }

    /**  **/
    public void setYbxfpje(String ybxfpje) {
        this.ybxfpje = ybxfpje;
    }

    /**  **/
    public String getYzfxmje() {
        return this.yzfxmje;
    }

    /**  **/
    public void setYzfxmje(String yzfxmje) {
        this.yzfxmje = yzfxmje;
    }

    /**  **/
    public String getBxfpje2018s4() {
        return this.bxfpje2018s4;
    }

    /**  **/
    public void setBxfpje2018s4(String bxfpje2018s4) {
        this.bxfpje2018s4 = bxfpje2018s4;
    }

    /**  **/
    public String getZfje2018s4() {
        return this.zfje2018s4;
    }

    /**  **/
    public void setZfje2018s4(String zfje2018s4) {
        this.zfje2018s4 = zfje2018s4;
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
    public Date getStratTime() {
        return this.stratTime;
    }

    /**  **/
    public void setStratTime(Date stratTime) {
        this.stratTime = stratTime;
    }

    /**  **/
    public Date getUpdateTime() {
        return this.updateTime;
    }

    /**  **/
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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

    public void setIslock(String isLock) {
        this.islock = isLock;
    }

    public String getIfContinueExecute() {
        return ifContinueExecute;
    }

    public void setIfContinueExecute(String ifContinueExecute) {
        this.ifContinueExecute = ifContinueExecute;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public String getContractSigningTime() {
        return contractSigningTime;
    }

    public void setContractSigningTime(String contractSigningTime) {
        this.contractSigningTime = contractSigningTime;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }
}
