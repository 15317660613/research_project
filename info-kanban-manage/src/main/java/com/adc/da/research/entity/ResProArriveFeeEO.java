package com.adc.da.research.entity;

import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import com.adc.da.base.entity.BaseEntity;
import cn.afterturn.easypoi.excel.annotation.Excel;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <b>功能：</b>DB_RES_PRO_ARRIVE_FEE ResProArriveFeeEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-07-20 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class ResProArriveFeeEO extends BaseEntity implements IExcelModel, IExcelDataModel {

    private String errorMsg;

    private int rowNum;
    @Override
    public int getRowNum() {
        return this.rowNum;
    }

    @Override
    public void setRowNum(int i) {
        this.rowNum = i;
    }

    @Override
    public String getErrorMsg() {
        return this.errorMsg;
    }

    @Override
    public void setErrorMsg(String s) {
        this.errorMsg = s;
    }
    private String id;
    @Excel(name = "科研项目ID",orderNum = "1")
    @Length(min=1, max=20,message = "科研项目ID长度不能为空且小于20字符")
    private String researchProjectId;

    @Excel(name = "科研项目编号",orderNum = "2")
    @Length(min=1, max=20,message = "科研项目编号长度不能为空且小于20字符")
    private String projectNo;

    @Excel(name = "年份",orderNum = "3")
    @Digits(integer=12, fraction=0,message = "年份为四位整数")
    @NotNull(message = "年份为四位整数不能为空")
    private Long arriveYear;

    @Excel(name = "月份",orderNum = "4")
    @Max(value=12,message = "月份小于等于12")
    @Min(value=1,message = "月份大于等于1")
    @NotNull(message = "月份为不能为空")
    private Long arriveMonth;
    @Excel(name = "到账金额（万元）",orderNum = "5")
    @Digits(integer=12, fraction=6,message = "到账金额整数位长度小于12位，且小数位小于6位")
    private BigDecimal arriveFee;

    private String projectName ;

    private String updateUserId;
    private String updateUserName;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    private String createUserId;
    private String createUserName;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private String extInfo1;
    private String extInfo2;
    private String extInfo3;
    private String extInfo4;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>researchProjectId -> research_project_id</li>
     * <li>projectNo -> project_no</li>
     * <li>arriveYear -> arrive_year</li>
     * <li>arriveMonth -> arrive_month</li>
     * <li>arriveFee -> arrive_fee</li>
     * <li>updateUserId -> update_user_id</li>
     * <li>updateUserName -> update_user_name</li>
     * <li>updateTime -> update_time</li>
     * <li>createUserId -> create_user_id</li>
     * <li>createUserName -> create_user_name</li>
     * <li>createTime -> create_time</li>
     * <li>extInfo1 -> ext_info1</li>
     * <li>extInfo2 -> ext_info2</li>
     * <li>extInfo3 -> ext_info3</li>
     * <li>extInfo4 -> ext_info4</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) { return null; }
        switch (fieldName) {
            case "id": return "id";
            case "researchProjectId": return "research_project_id";
            case "projectNo": return "project_no";
            case "arriveYear": return "arrive_year";
            case "arriveMonth": return "arrive_month";
            case "arriveFee": return "arrive_fee";
            case "updateUserId": return "update_user_id";
            case "updateUserName": return "update_user_name";
            case "updateTime": return "update_time";
            case "createUserId": return "create_user_id";
            case "createUserName": return "create_user_name";
            case "createTime": return "create_time";
            case "extInfo1": return "ext_info1";
            case "extInfo2": return "ext_info2";
            case "extInfo3": return "ext_info3";
            case "extInfo4": return "ext_info4";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>research_project_id -> researchProjectId</li>
     * <li>project_no -> projectNo</li>
     * <li>arrive_year -> arriveYear</li>
     * <li>arrive_month -> arriveMonth</li>
     * <li>arrive_fee -> arriveFee</li>
     * <li>update_user_id -> updateUserId</li>
     * <li>update_user_name -> updateUserName</li>
     * <li>update_time -> updateTime</li>
     * <li>create_user_id -> createUserId</li>
     * <li>create_user_name -> createUserName</li>
     * <li>create_time -> createTime</li>
     * <li>ext_info1 -> extInfo1</li>
     * <li>ext_info2 -> extInfo2</li>
     * <li>ext_info3 -> extInfo3</li>
     * <li>ext_info4 -> extInfo4</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) { return null; }
        switch (columnName) {
            case "id": return "id";
            case "research_project_id": return "researchProjectId";
            case "project_no": return "projectNo";
            case "arrive_year": return "arriveYear";
            case "arrive_month": return "arriveMonth";
            case "arrive_fee": return "arriveFee";
            case "update_user_id": return "updateUserId";
            case "update_user_name": return "updateUserName";
            case "update_time": return "updateTime";
            case "create_user_id": return "createUserId";
            case "create_user_name": return "createUserName";
            case "create_time": return "createTime";
            case "ext_info1": return "extInfo1";
            case "ext_info2": return "extInfo2";
            case "ext_info3": return "extInfo3";
            case "ext_info4": return "extInfo4";
            default: return null;
        }
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
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
    public String getResearchProjectId() {
        return this.researchProjectId;
    }

    /**  **/
    public void setResearchProjectId(String researchProjectId) {
        this.researchProjectId = researchProjectId;
    }

    /**  **/
    public String getProjectNo() {
        return this.projectNo;
    }

    /**  **/
    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }

    /**  **/
    public Long getArriveYear() {
        return this.arriveYear;
    }

    /**  **/
    public void setArriveYear(Long arriveYear) {
        this.arriveYear = arriveYear;
    }

    /**  **/
    public Long getArriveMonth() {
        return this.arriveMonth;
    }

    /**  **/
    public void setArriveMonth(Long arriveMonth) {
        this.arriveMonth = arriveMonth;
    }

    /**  **/
    public BigDecimal getArriveFee() {
        return this.arriveFee;
    }

    /**  **/
    public void setArriveFee(BigDecimal arriveFee) {
        this.arriveFee = arriveFee;
    }

    /**  **/
    public String getUpdateUserId() {
        return this.updateUserId;
    }

    /**  **/
    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }

    /**  **/
    public String getUpdateUserName() {
        return this.updateUserName;
    }

    /**  **/
    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
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
    public String getCreateUserId() {
        return this.createUserId;
    }

    /**  **/
    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    /**  **/
    public String getCreateUserName() {
        return this.createUserName;
    }

    /**  **/
    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    /**  **/
    public Date getCreateTime() {
        return this.createTime;
    }

    /**  **/
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**  **/
    public String getExtInfo1() {
        return this.extInfo1;
    }

    /**  **/
    public void setExtInfo1(String extInfo1) {
        this.extInfo1 = extInfo1;
    }

    /**  **/
    public String getExtInfo2() {
        return this.extInfo2;
    }

    /**  **/
    public void setExtInfo2(String extInfo2) {
        this.extInfo2 = extInfo2;
    }

    /**  **/
    public String getExtInfo3() {
        return this.extInfo3;
    }

    /**  **/
    public void setExtInfo3(String extInfo3) {
        this.extInfo3 = extInfo3;
    }

    /**  **/
    public String getExtInfo4() {
        return this.extInfo4;
    }

    /**  **/
    public void setExtInfo4(String extInfo4) {
        this.extInfo4 = extInfo4;
    }


}
