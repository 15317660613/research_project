package com.adc.da.research.project.poi.domain;

import com.adc.da.research.project.poi.table.ResearchContentData;

/**
 *
 * @Auther: yanyujie
 * @Date: 2020/11/27/9:25
 * @Description:
 */
public class CatarcData extends CatarcTable {

    //项目编号
    private String pCode;

    //项目名称
    private String pName;

    //中心科研经费预算
    private String centreBudget;

    //项目负责人
    private String principal;

    //所属部门
    private String department;

    //联系人电话
    private String tel;

    //手机
    private String phone;

    //电子邮箱
    private String email;

    //起止时间
    private String startEndTime;

    //性别
    private String sex;

    //学历
    private String education;

    //职称
    private String level;

    //是否留学
    private String abroad;

    //专业
    private String profession;

    private ResearchContentData researchContent;

    private ResearchContentData researchTarget;

    private ResearchContentData progress;

    private ResearchContentData deliverable;

    private ResearchContentData userTable;

    private ResearchContentData purchase;

    private ResearchContentData production;

    private ResearchContentData retrofit;

    private ResearchContentData material;

    private ResearchContentData assay;

    private ResearchContentData travel;

    private ResearchContentData conference;

    private ResearchContentData exchange;
    private ResearchContentData software;

    private String materialSum;

    private String assaySum;

    private String pSum;

    private String purchaseSum;

    private String productionSum;

    private String retrofitSum;
    //专利
    private String patent;

    //实用新型
    private String practical;

    //外观专利
    private String exterior;

    //目标刊物
    private String publication;

    //篇数
    private String actual;

    //软件
    private String soft;

    //其他
    private String other;

    //来源合计
    private String sumSource;
    //申请中心预算
    private String center;
    //部门自筹
    private String selfBudget;
    //备注
    private String sumSRemarks;
    private String cBRemarks;

    private String fuelSum;
    private String travelSum;
    private String conferenceSum;
    private String exchangeSum;

    private String service;

    private String softwareSum;

    public ResearchContentData getSoftware() {
        return software;
    }

    public void setSoftware(ResearchContentData software) {
        this.software = software;
    }

    public String getSoftwareSum() {
        return softwareSum;
    }

    public void setSoftwareSum(String softwareSum) {
        this.softwareSum = softwareSum;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public ResearchContentData getExchange() {
        return exchange;
    }

    public void setExchange(ResearchContentData exchange) {
        this.exchange = exchange;
    }

    public String getExchangeSum() {
        return exchangeSum;
    }

    public void setExchangeSum(String exchangeSum) {
        this.exchangeSum = exchangeSum;
    }

    public ResearchContentData getTravel() {
        return travel;
    }

    public void setTravel(ResearchContentData travel) {
        this.travel = travel;
    }

    public ResearchContentData getConference() {
        return conference;
    }

    public void setConference(ResearchContentData conference) {
        this.conference = conference;
    }

    public String getTravelSum() {
        return travelSum;
    }

    public void setTravelSum(String travelSum) {
        this.travelSum = travelSum;
    }

    public String getConferenceSum() {
        return conferenceSum;
    }

    public void setConferenceSum(String conferenceSum) {
        this.conferenceSum = conferenceSum;
    }

    public String getFuelSum() {
        return fuelSum;
    }

    public void setFuelSum(String fuelSum) {
        this.fuelSum = fuelSum;
    }

    public String getProductionSum() {
        return productionSum;
    }

    public void setProductionSum(String productionSum) {
        this.productionSum = productionSum;
    }

    public String getRetrofitSum() {
        return retrofitSum;
    }

    public void setRetrofitSum(String retrofitSum) {
        this.retrofitSum = retrofitSum;
    }

    public String getpCode() {
        return pCode;
    }

    public void setpCode(String pCode) {
        this.pCode = pCode;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getCentreBudget() {
        return centreBudget;
    }

    public void setCentreBudget(String centreBudget) {
        this.centreBudget = centreBudget;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStartEndTime() {
        return startEndTime;
    }

    public void setStartEndTime(String startEndTime) {
        this.startEndTime = startEndTime;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getAbroad() {
        return abroad;
    }

    public void setAbroad(String abroad) {
        this.abroad = abroad;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public ResearchContentData getResearchContent() {
        return researchContent;
    }

    public void setResearchContent(ResearchContentData researchContent) {
        this.researchContent = researchContent;
    }

    public ResearchContentData getResearchTarget() {
        return researchTarget;
    }

    public void setResearchTarget(ResearchContentData researchTarget) {
        this.researchTarget = researchTarget;
    }

    public ResearchContentData getProgress() {
        return progress;
    }

    public void setProgress(ResearchContentData progress) {
        this.progress = progress;
    }

    public ResearchContentData getDeliverable() {
        return deliverable;
    }

    public void setDeliverable(ResearchContentData deliverable) {
        this.deliverable = deliverable;
    }

    public ResearchContentData getUserTable() {
        return userTable;
    }

    public void setUserTable(ResearchContentData userTable) {
        this.userTable = userTable;
    }

    public String getPatent() {
        return patent;
    }

    public void setPatent(String patent) {
        this.patent = patent;
    }

    public String getPractical() {
        return practical;
    }

    public void setPractical(String practical) {
        this.practical = practical;
    }

    public String getExterior() {
        return exterior;
    }

    public void setExterior(String exterior) {
        this.exterior = exterior;
    }

    public String getPublication() {
        return publication;
    }

    public void setPublication(String publication) {
        this.publication = publication;
    }

    public String getActual() {
        return actual;
    }

    public void setActual(String actual) {
        this.actual = actual;
    }

    public String getSoft() {
        return soft;
    }

    public void setSoft(String soft) {
        this.soft = soft;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getSumSource() {
        return sumSource;
    }

    public void setSumSource(String sumSource) {
        this.sumSource = sumSource;
    }

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }

    public String getSelfBudget() {
        return selfBudget;
    }

    public void setSelfBudget(String selfBudget) {
        this.selfBudget = selfBudget;
    }

    public String getSumSRemarks() {
        return sumSRemarks;
    }

    public void setSumSRemarks(String sumSRemarks) {
        this.sumSRemarks = sumSRemarks;
    }

    public String getcBRemarks() {
        return cBRemarks;
    }

    public void setcBRemarks(String cBRemarks) {
        this.cBRemarks = cBRemarks;
    }

    public ResearchContentData getPurchase() {
        return purchase;
    }

    public void setPurchase(ResearchContentData purchase) {
        this.purchase = purchase;
    }

    public ResearchContentData getProduction() {
        return production;
    }

    public void setProduction(ResearchContentData production) {
        this.production = production;
    }

    public ResearchContentData getRetrofit() {
        return retrofit;
    }

    public void setRetrofit(ResearchContentData retrofit) {
        this.retrofit = retrofit;
    }

    public String getpSum() {
        return pSum;
    }

    public void setpSum(String pSum) {
        this.pSum = pSum;
    }

    public String getPurchaseSum() {
        return purchaseSum;
    }

    public void setPurchaseSum(String purchaseSum) {
        this.purchaseSum = purchaseSum;
    }

    public ResearchContentData getMaterial() {
        return material;
    }

    public void setMaterial(ResearchContentData material) {
        this.material = material;
    }


    public String getMaterialSum() {
        return materialSum;
    }

    public void setMaterialSum(String materialSum) {
        this.materialSum = materialSum;
    }

    public ResearchContentData getAssay() {
        return assay;
    }

    public void setAssay(ResearchContentData assay) {
        this.assay = assay;
    }

    public String getAssaySum() {
        return assaySum;
    }

    public void setAssaySum(String assaySum) {
        this.assaySum = assaySum;
    }
}
