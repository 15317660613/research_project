package com.adc.da.research.project.vo;

import com.adc.da.base.entity.BaseEntity;

import java.util.Date;

/**
 * <b>功能：</b>RS_RESEARCH_DETAIL_HISTORY ResearchDetailHistoryEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-12-05 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class ResearchDetailHistoryVO extends BaseEntity {

    private String id;
    private String researchProjectId;
    private String detailType;
    private String name;
    private Long count;
    private String unit;
    private Double unitPrice;
    private Integer sort;
    private String extInfo1;
    private String extInfo2;
    private String extInfo3;
    private String extInfo4;
    private String extInfo5;
    private String extInfo6;
    private String model;
    private String function;
    private String entrustedUnit;
    private Integer peopleNum;
    private Integer dayNum;
    private String exchangePlace;
    private String participateHost;
    private Double totalPrice;
    private Integer delFlag;
    private String createUserId;
    private String createUserName;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id_</li>
     * <li>researchProjectId -> research_project_id_</li>
     * <li>detailType -> detail_type_</li>
     * <li>name -> name_</li>
     * <li>count -> count_</li>
     * <li>unit -> unit_</li>
     * <li>unitPrice -> unit_price_</li>
     * <li>sort -> sort_</li>
     * <li>extInfo1 -> ext_info_1_</li>
     * <li>extInfo2 -> ext_info_2_</li>
     * <li>extInfo3 -> ext_info_3_</li>
     * <li>extInfo4 -> ext_info_4_</li>
     * <li>extInfo5 -> ext_info_5_</li>
     * <li>extInfo6 -> ext_info_6_</li>
     * <li>model -> model_</li>
     * <li>function -> function_</li>
     * <li>entrustedUnit -> entrusted_unit_</li>
     * <li>peopleNum -> people_num_</li>
     * <li>dayNum -> day_num_</li>
     * <li>exchangePlace -> exchange_place_</li>
     * <li>participateHost -> participate_host_</li>
     * <li>totalPrice -> total_price_</li>
     * <li>delFlag -> del_flag</li>
     * <li>createUserId -> create_user_id</li>
     * <li>createUserName -> create_user_name</li>
     * <li>createTime -> create_time</li>
     * <li>modifyTime -> modify_time</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) { return null; }
        switch (fieldName) {
            case "id": return "id_";
            case "researchProjectId": return "research_project_id_";
            case "detailType": return "detail_type_";
            case "name": return "name_";
            case "count": return "count_";
            case "unit": return "unit_";
            case "unitPrice": return "unit_price_";
            case "sort": return "sort_";
            case "extInfo1": return "ext_info_1_";
            case "extInfo2": return "ext_info_2_";
            case "extInfo3": return "ext_info_3_";
            case "extInfo4": return "ext_info_4_";
            case "extInfo5": return "ext_info_5_";
            case "extInfo6": return "ext_info_6_";
            case "model": return "model_";
            case "function": return "function_";
            case "entrustedUnit": return "entrusted_unit_";
            case "peopleNum": return "people_num_";
            case "dayNum": return "day_num_";
            case "exchangePlace": return "exchange_place_";
            case "participateHost": return "participate_host_";
            case "totalPrice": return "total_price_";
            case "delFlag": return "del_flag";
            case "createUserId": return "create_user_id";
            case "createUserName": return "create_user_name";
            case "createTime": return "create_time";
            case "modifyTime": return "modify_time";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id_ -> id</li>
     * <li>research_project_id_ -> researchProjectId</li>
     * <li>detail_type_ -> detailType</li>
     * <li>name_ -> name</li>
     * <li>count_ -> count</li>
     * <li>unit_ -> unit</li>
     * <li>unit_price_ -> unitPrice</li>
     * <li>sort_ -> sort</li>
     * <li>ext_info_1_ -> extInfo1</li>
     * <li>ext_info_2_ -> extInfo2</li>
     * <li>ext_info_3_ -> extInfo3</li>
     * <li>ext_info_4_ -> extInfo4</li>
     * <li>ext_info_5_ -> extInfo5</li>
     * <li>ext_info_6_ -> extInfo6</li>
     * <li>model_ -> model</li>
     * <li>function_ -> function</li>
     * <li>entrusted_unit_ -> entrustedUnit</li>
     * <li>people_num_ -> peopleNum</li>
     * <li>day_num_ -> dayNum</li>
     * <li>exchange_place_ -> exchangePlace</li>
     * <li>participate_host_ -> participateHost</li>
     * <li>total_price_ -> totalPrice</li>
     * <li>del_flag -> delFlag</li>
     * <li>create_user_id -> createUserId</li>
     * <li>create_user_name -> createUserName</li>
     * <li>create_time -> createTime</li>
     * <li>modify_time -> modifyTime</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) { return null; }
        switch (columnName) {
            case "id_": return "id";
            case "research_project_id_": return "researchProjectId";
            case "detail_type_": return "detailType";
            case "name_": return "name";
            case "count_": return "count";
            case "unit_": return "unit";
            case "unit_price_": return "unitPrice";
            case "sort_": return "sort";
            case "ext_info_1_": return "extInfo1";
            case "ext_info_2_": return "extInfo2";
            case "ext_info_3_": return "extInfo3";
            case "ext_info_4_": return "extInfo4";
            case "ext_info_5_": return "extInfo5";
            case "ext_info_6_": return "extInfo6";
            case "model_": return "model";
            case "function_": return "function";
            case "entrusted_unit_": return "entrustedUnit";
            case "people_num_": return "peopleNum";
            case "day_num_": return "dayNum";
            case "exchange_place_": return "exchangePlace";
            case "participate_host_": return "participateHost";
            case "total_price_": return "totalPrice";
            case "del_flag": return "delFlag";
            case "create_user_id": return "createUserId";
            case "create_user_name": return "createUserName";
            case "create_time": return "createTime";
            case "modify_time": return "modifyTime";
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
    public String getResearchProjectId() {
        return this.researchProjectId;
    }

    /**  **/
    public void setResearchProjectId(String researchProjectId) {
        this.researchProjectId = researchProjectId;
    }

    /**  **/
    public String getDetailType() {
        return this.detailType;
    }

    /**  **/
    public void setDetailType(String detailType) {
        this.detailType = detailType;
    }

    /**  **/
    public String getName() {
        return this.name;
    }

    /**  **/
    public void setName(String name) {
        this.name = name;
    }

    /**  **/
    public Long getCount() {
        return this.count;
    }

    /**  **/
    public void setCount(Long count) {
        this.count = count;
    }

    /**  **/
    public String getUnit() {
        return this.unit;
    }

    /**  **/
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**  **/
    public Double getUnitPrice() {
        return this.unitPrice;
    }

    /**  **/
    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    /**  **/
    public Integer getSort() {
        return this.sort;
    }

    /**  **/
    public void setSort(Integer sort) {
        this.sort = sort;
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

    /**  **/
    public String getExtInfo5() {
        return this.extInfo5;
    }

    /**  **/
    public void setExtInfo5(String extInfo5) {
        this.extInfo5 = extInfo5;
    }

    /**  **/
    public String getExtInfo6() {
        return this.extInfo6;
    }

    /**  **/
    public void setExtInfo6(String extInfo6) {
        this.extInfo6 = extInfo6;
    }

    /**  **/
    public String getModel() {
        return this.model;
    }

    /**  **/
    public void setModel(String model) {
        this.model = model;
    }

    /**  **/
    public String getFunction() {
        return this.function;
    }

    /**  **/
    public void setFunction(String function) {
        this.function = function;
    }

    /**  **/
    public String getEntrustedUnit() {
        return this.entrustedUnit;
    }

    /**  **/
    public void setEntrustedUnit(String entrustedUnit) {
        this.entrustedUnit = entrustedUnit;
    }

    /**  **/
    public Integer getPeopleNum() {
        return this.peopleNum;
    }

    /**  **/
    public void setPeopleNum(Integer peopleNum) {
        this.peopleNum = peopleNum;
    }

    /**  **/
    public Integer getDayNum() {
        return this.dayNum;
    }

    /**  **/
    public void setDayNum(Integer dayNum) {
        this.dayNum = dayNum;
    }

    /**  **/
    public String getExchangePlace() {
        return this.exchangePlace;
    }

    /**  **/
    public void setExchangePlace(String exchangePlace) {
        this.exchangePlace = exchangePlace;
    }

    /**  **/
    public String getParticipateHost() {
        return this.participateHost;
    }

    /**  **/
    public void setParticipateHost(String participateHost) {
        this.participateHost = participateHost;
    }

    /**  **/
    public Double getTotalPrice() {
        return this.totalPrice;
    }

    /**  **/
    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    /**  **/
    public Integer getDelFlag() {
        return this.delFlag;
    }

    /**  **/
    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
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
    public Date getModifyTime() {
        return this.modifyTime;
    }

    /**  **/
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

}
