package com.adc.da.crm.entity;

import com.adc.da.base.entity.BaseEntity;
import com.adc.da.crm.annotation.MatchField;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * <b>功能：</b>CONTRACT_APPROVAL ContractApprovalEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-12-04 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class ContractApprovalEO extends BaseEntity {

    private String id;
    @MatchField("区域经理")
    private String areaManagerId;
    @MatchField("申请日期")
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date applyData;
    @MatchField("申请部门")
    private String applyDeptid;
    @MatchField("业务部门")
    private String operateDeptid;
    @MatchField("合同编号")
    private String contractNo;
    @MatchField("合同名称")
    private String contractName;
    @MatchField("客户编号")
    private String customerNo;
    @MatchField("客户名称")
    private String customerName;
    @MatchField("项目经理")
    private String managerId;
    @MatchField("对接人")
    private String contactUser;
    @MatchField("合同类型")
    private String contractType;
    @MatchField("组别")
    private String part;
    @MatchField("签约乙方")
    private String partyB;
    @MatchField("所属平台")
    private String platform;
    @MatchField("所属板块")
    private String block;
    @MatchField("合同金额")
    private Long contractAmount;
    @MatchField("签订日期")
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date signData;
    @MatchField("生效日期")
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date beginData;
    @MatchField("终止日期")
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date endData;
    @MatchField("部长意见")
    private String deptView;
    @MatchField("主任意见")
    private String directorView;
    private String delFlag;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createdTime;
    private String createdUser;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime2;
    private String modifiedUser2;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>areaManagerId -> area_manager_id</li>
     * <li>applyData -> apply_data</li>
     * <li>applyDeptid -> apply_deptid</li>
     * <li>operateDeptid -> operate_deptid</li>
     * <li>contractNo -> contract_no</li>
     * <li>contractName -> contract_name</li>
     * <li>customerNo -> customer_no</li>
     * <li>customerName -> customer_name</li>
     * <li>managerId -> manager_id</li>
     * <li>contactUser -> contact_user</li>
     * <li>contractType -> contract_type</li>
     * <li>part -> part</li>
     * <li>partyB -> party_b</li>
     * <li>platform -> platform</li>
     * <li>block -> block</li>
     * <li>contractAmount -> contract_amount</li>
     * <li>signData -> sign_data</li>
     * <li>beginData -> begin_data</li>
     * <li>endData -> end_data</li>
     * <li>deptView -> dept_view</li>
     * <li>directorView -> director_view</li>
     * <li>delFlag -> del_flag</li>
     * <li>createdTime -> created_time</li>
     * <li>createdUser -> created_user</li>
     * <li>modifiedTime2 -> modified_time2</li>
     * <li>modifiedUser2 -> modified_user2</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) {return null;}
        switch (fieldName) {
            case "id": return "id";
            case "areaManagerId": return "area_manager_id";
            case "applyData": return "apply_data";
            case "applyDeptid": return "apply_deptid";
            case "operateDeptid": return "operate_deptid";
            case "contractNo": return "contract_no";
            case "contractName": return "contract_name";
            case "customerNo": return "customer_no";
            case "customerName": return "customer_name";
            case "managerId": return "manager_id";
            case "contactUser": return "contact_user";
            case "contractType": return "contract_type";
            case "part": return "part";
            case "partyB": return "party_b";
            case "platform": return "platform";
            case "block": return "block";
            case "contractAmount": return "contract_amount";
            case "signData": return "sign_data";
            case "beginData": return "begin_data";
            case "endData": return "end_data";
            case "deptView": return "dept_view";
            case "directorView": return "director_view";
            case "delFlag": return "del_flag";
            case "createdTime": return "created_time";
            case "createdUser": return "created_user";
            case "modifiedTime2": return "modified_time2";
            case "modifiedUser2": return "modified_user2";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>area_manager_id -> areaManagerId</li>
     * <li>apply_data -> applyData</li>
     * <li>apply_deptid -> applyDeptid</li>
     * <li>operate_deptid -> operateDeptid</li>
     * <li>contract_no -> contractNo</li>
     * <li>contract_name -> contractName</li>
     * <li>customer_no -> customerNo</li>
     * <li>customer_name -> customerName</li>
     * <li>manager_id -> managerId</li>
     * <li>contact_user -> contactUser</li>
     * <li>contract_type -> contractType</li>
     * <li>part -> part</li>
     * <li>party_b -> partyB</li>
     * <li>platform -> platform</li>
     * <li>block -> block</li>
     * <li>contract_amount -> contractAmount</li>
     * <li>sign_data -> signData</li>
     * <li>begin_data -> beginData</li>
     * <li>end_data -> endData</li>
     * <li>dept_view -> deptView</li>
     * <li>director_view -> directorView</li>
     * <li>del_flag -> delFlag</li>
     * <li>created_time -> createdTime</li>
     * <li>created_user -> createdUser</li>
     * <li>modified_time2 -> modifiedTime2</li>
     * <li>modified_user2 -> modifiedUser2</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) {return null;}
        switch (columnName) {
            case "id": return "id";
            case "area_manager_id": return "areaManagerId";
            case "apply_data": return "applyData";
            case "apply_deptid": return "applyDeptid";
            case "operate_deptid": return "operateDeptid";
            case "contract_no": return "contractNo";
            case "contract_name": return "contractName";
            case "customer_no": return "customerNo";
            case "customer_name": return "customerName";
            case "manager_id": return "managerId";
            case "contact_user": return "contactUser";
            case "contract_type": return "contractType";
            case "part": return "part";
            case "party_b": return "partyB";
            case "platform": return "platform";
            case "block": return "block";
            case "contract_amount": return "contractAmount";
            case "sign_data": return "signData";
            case "begin_data": return "beginData";
            case "end_data": return "endData";
            case "dept_view": return "deptView";
            case "director_view": return "directorView";
            case "del_flag": return "delFlag";
            case "created_time": return "createdTime";
            case "created_user": return "createdUser";
            case "modified_time2": return "modifiedTime2";
            case "modified_user2": return "modifiedUser2";
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
    public String getAreaManagerId() {
        return this.areaManagerId;
    }

    /**  **/
    public void setAreaManagerId(String areaManagerId) {
        this.areaManagerId = areaManagerId;
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
    public String getApplyDeptid() {
        return this.applyDeptid;
    }

    /**  **/
    public void setApplyDeptid(String applyDeptid) {
        this.applyDeptid = applyDeptid;
    }

    /**  **/
    public String getOperateDeptid() {
        return this.operateDeptid;
    }

    /**  **/
    public void setOperateDeptid(String operateDeptid) {
        this.operateDeptid = operateDeptid;
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
    public String getContractName() {
        return this.contractName;
    }

    /**  **/
    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    /**  **/
    public String getCustomerNo() {
        return this.customerNo;
    }

    /**  **/
    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    /**  **/
    public String getCustomerName() {
        return this.customerName;
    }

    /**  **/
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**  **/
    public String getManagerId() {
        return this.managerId;
    }

    /**  **/
    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    /**  **/
    public String getContactUser() {
        return this.contactUser;
    }

    /**  **/
    public void setContactUser(String contactUser) {
        this.contactUser = contactUser;
    }

    /**  **/
    public String getContractType() {
        return this.contractType;
    }

    /**  **/
    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    /**  **/
    public String getPart() {
        return this.part;
    }

    /**  **/
    public void setPart(String part) {
        this.part = part;
    }

    /**  **/
    public String getPartyB() {
        return this.partyB;
    }

    /**  **/
    public void setPartyB(String partyB) {
        this.partyB = partyB;
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
    public Long getContractAmount() {
        return this.contractAmount;
    }

    /**  **/
    public void setContractAmount(Long contractAmount) {
        this.contractAmount = contractAmount;
    }

    /**  **/
    public Date getSignData() {
        return this.signData;
    }

    /**  **/
    public void setSignData(Date signData) {
        this.signData = signData;
    }

    /**  **/
    public Date getBeginData() {
        return this.beginData;
    }

    /**  **/
    public void setBeginData(Date beginData) {
        this.beginData = beginData;
    }

    /**  **/
    public Date getEndData() {
        return this.endData;
    }

    /**  **/
    public void setEndData(Date endData) {
        this.endData = endData;
    }

    /**  **/
    public String getDeptView() {
        return this.deptView;
    }

    /**  **/
    public void setDeptView(String deptView) {
        this.deptView = deptView;
    }

    /**  **/
    public String getDirectorView() {
        return this.directorView;
    }

    /**  **/
    public void setDirectorView(String directorView) {
        this.directorView = directorView;
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
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
