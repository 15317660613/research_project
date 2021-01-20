package com.adc.da.research.project.poi.domain;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: yanyujie
 * @Date: 2020/11/27/13:37
 * @Description:
 */
public class CatarcTable {

    //总人数
    private String totalPeople;
    //正高级
    private String positive;
    //副高级
    private String deputy;
    //中级
    private String intermediate;
    //初级
    private String primary;
    //研究生
    private String postgraduate;
    //大学
    private String university;
    //大专
    private String junior;
    //博士
    private String PhD;
    //硕士
    private String degree;



    //资金支出 年份
    private String fundYear1;
    private String fundYear2;
    private String fundYear3;

    //资金总额
    private String sumFundSum;
    private String sumFundYear1;
    private String sumFundYear2;
    private String sumFundYear3;
    private String sumFundOther;

    //设备费
    private String equipmentSum;
    private String equipmentYear1;
    private String equipmentYear2;
    private String equipmentYear3;
    private String equipmentOther;

    //设备购置费
    private String purchaseSum;
    private String purchaseYear1;
    private String purchaseYear2;
    private String purchaseYear3;
    private String purchaseOther;

    //试制费
    private String trialSum;
    private String trialYear1;
    private String trialYear2;
    private String trialYear3;
    private String trialOther;

    //改造租赁费
    private String leaseSum;
    private String leaseYear1;
    private String leaseYear2;
    private String leaseYear3;
    private String leaseOther;

    //材料费
    private String materialSum;
    private String materialYear1;
    private String materialYear2;
    private String materialYear3;
    private String materialOther;

    //测试化验加工费
    private String assaySum;
    private String assayYear1;
    private String assayYear2;
    private String assayYear3;
    private String assayOther;

    //燃料动力费
    private String fuelSum;
    private String fuelYear1;
    private String fuelYear2;
    private String fuelYear3;
    private String fuelOther;

    //差旅费
    private String travelSum;
    private String travelYear1;
    private String travelYear2;
    private String travelYear3;
    private String travelOther;

    //会议费
    private String conferenceSum;
    private String conferenceYear1;
    private String conferenceYear2;
    private String conferenceYear3;
    private String conferenceOther;

    //国际合作交流费
    private String exchangeSum;
    private String exchangeYear1;
    private String exchangeYear2;
    private String exchangeYear3;
    private String exchangeOther;

    //出版/文献/信息传播/知识产权事务费
    private String serviceSum;
    private String serviceYear1;
    private String serviceYear2;
    private String serviceYear3;
    private String serviceOther;

    //软件购置费
    private String softwarSum;
    private String softwarYear1;
    private String softwarYear2;
    private String softwarYear3;
    private String softwarOther;

    //劳务费
    private String laborSum;
    private String laborYear1;
    private String laborYear2;
    private String laborYear3;
    private String laborOther;

    //专家咨询费
    private String advisorySum;
    private String advisoryYear1;
    private String advisoryYear2;
    private String advisoryYear3;
    private String advisoryOther;

    //外协费
    private String externalSum;
    private String externalYear1;
    private String externalYear2;
    private String externalYear3;
    private String externalOther;

    //管理费
    private String manSum;
    private String manYear1;
    private String manYear2;
    private String manYear3;
    private String manOther;

    //燃料动力费
    //水 单价
    private String wateUnitPrice;
    //水 数量
    private String wateCount;
    //水 合计
    private String wateTotal;

    //电 单价
    private String electricityPrice;
    //电 数量
    private String electricityCount;
    //电 合计
    private String electricityTotal;

    //汽油 单价
    private String gasPrice;
    //汽油 数量
    private String gasCount;
    //汽油 合计
    private String gasTotal;

    //柴油 单价
    private String dieselPrice;
    private String dieselCount;
    private String dieselTotal;


    //出版费 单价
    private String publicationUnit;
    //单价
    private String publicationPrice;
    //数量
    private String publicationCount;
    //合计
    private String publicationTotal;

    //资料费
    private String informationUnit;
    private String informationPrice;
    private String informationCount;
    private String informationTotal;

    //文献检索
    private String literatureUnit;
    private String literaturePrice;
    private String literatureCount;
    private String literatureTotal;

    //专业通信费
    private String professionalUnit;
    private String professionalPrice;
    private String professionalCount;
    private String professionalTotal;

    //专利申请及其他知识产权事务
    private String patentUnit;
    private String patentPrice;
    private String patentCount;
    private String patentTotal;


    //预期交付物
    private String deliverables;
    private String indicators;

    public String getDeliverables() {
        return deliverables;
    }

    public void setDeliverables(String deliverables) {
        this.deliverables = deliverables;
    }

    public String getIndicators() {
        return indicators;
    }

    public void setIndicators(String indicators) {
        this.indicators = indicators;
    }


    public String getPublicationUnit() {
        return publicationUnit;
    }

    public void setPublicationUnit(String publicationUnit) {
        this.publicationUnit = publicationUnit;
    }

    public String getPublicationPrice() {
        return publicationPrice;
    }

    public void setPublicationPrice(String publicationPrice) {
        this.publicationPrice = publicationPrice;
    }

    public String getPublicationCount() {
        return publicationCount;
    }

    public void setPublicationCount(String publicationCount) {
        this.publicationCount = publicationCount;
    }

    public String getPublicationTotal() {
        return publicationTotal;
    }

    public void setPublicationTotal(String publicationTotal) {
        this.publicationTotal = publicationTotal;
    }

    public String getInformationUnit() {
        return informationUnit;
    }

    public void setInformationUnit(String informationUnit) {
        this.informationUnit = informationUnit;
    }

    public String getInformationPrice() {
        return informationPrice;
    }

    public void setInformationPrice(String informationPrice) {
        this.informationPrice = informationPrice;
    }

    public String getInformationCount() {
        return informationCount;
    }

    public void setInformationCount(String informationCount) {
        this.informationCount = informationCount;
    }

    public String getInformationTotal() {
        return informationTotal;
    }

    public void setInformationTotal(String informationTotal) {
        this.informationTotal = informationTotal;
    }

    public String getLiteratureUnit() {
        return literatureUnit;
    }

    public void setLiteratureUnit(String literatureUnit) {
        this.literatureUnit = literatureUnit;
    }

    public String getLiteraturePrice() {
        return literaturePrice;
    }

    public void setLiteraturePrice(String literaturePrice) {
        this.literaturePrice = literaturePrice;
    }

    public String getLiteratureCount() {
        return literatureCount;
    }

    public void setLiteratureCount(String literatureCount) {
        this.literatureCount = literatureCount;
    }

    public String getLiteratureTotal() {
        return literatureTotal;
    }

    public void setLiteratureTotal(String literatureTotal) {
        this.literatureTotal = literatureTotal;
    }

    public String getProfessionalUnit() {
        return professionalUnit;
    }

    public void setProfessionalUnit(String professionalUnit) {
        this.professionalUnit = professionalUnit;
    }

    public String getProfessionalPrice() {
        return professionalPrice;
    }

    public void setProfessionalPrice(String professionalPrice) {
        this.professionalPrice = professionalPrice;
    }

    public String getProfessionalCount() {
        return professionalCount;
    }

    public void setProfessionalCount(String professionalCount) {
        this.professionalCount = professionalCount;
    }

    public String getProfessionalTotal() {
        return professionalTotal;
    }

    public void setProfessionalTotal(String professionalTotal) {
        this.professionalTotal = professionalTotal;
    }

    public String getPatentUnit() {
        return patentUnit;
    }

    public void setPatentUnit(String patentUnit) {
        this.patentUnit = patentUnit;
    }

    public String getPatentPrice() {
        return patentPrice;
    }

    public void setPatentPrice(String patentPrice) {
        this.patentPrice = patentPrice;
    }

    public String getPatentCount() {
        return patentCount;
    }

    public void setPatentCount(String patentCount) {
        this.patentCount = patentCount;
    }

    public String getPatentTotal() {
        return patentTotal;
    }

    public void setPatentTotal(String patentTotal) {
        this.patentTotal = patentTotal;
    }

    public String getWateUnitPrice() {
        return wateUnitPrice;
    }

    public void setWateUnitPrice(String wateUnitPrice) {
        this.wateUnitPrice = wateUnitPrice;
    }

    public String getWateCount() {
        return wateCount;
    }

    public void setWateCount(String wateCount) {
        this.wateCount = wateCount;
    }

    public String getWateTotal() {
        return wateTotal;
    }

    public void setWateTotal(String wateTotal) {
        this.wateTotal = wateTotal;
    }

    public String getElectricityPrice() {
        return electricityPrice;
    }

    public void setElectricityPrice(String electricityPrice) {
        this.electricityPrice = electricityPrice;
    }

    public String getElectricityCount() {
        return electricityCount;
    }

    public void setElectricityCount(String electricityCount) {
        this.electricityCount = electricityCount;
    }

    public String getElectricityTotal() {
        return electricityTotal;
    }

    public void setElectricityTotal(String electricityTotal) {
        this.electricityTotal = electricityTotal;
    }

    public String getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(String gasPrice) {
        this.gasPrice = gasPrice;
    }

    public String getGasCount() {
        return gasCount;
    }

    public void setGasCount(String gasCount) {
        this.gasCount = gasCount;
    }

    public String getGasTotal() {
        return gasTotal;
    }

    public void setGasTotal(String gasTotal) {
        this.gasTotal = gasTotal;
    }

    public String getDieselPrice() {
        return dieselPrice;
    }

    public void setDieselPrice(String dieselPrice) {
        this.dieselPrice = dieselPrice;
    }

    public String getDieselCount() {
        return dieselCount;
    }

    public void setDieselCount(String dieselCount) {
        this.dieselCount = dieselCount;
    }

    public String getDieselTotal() {
        return dieselTotal;
    }

    public void setDieselTotal(String dieselTotal) {
        this.dieselTotal = dieselTotal;
    }

    public String getFundYear1() {
        return fundYear1;
    }

    public void setFundYear1(String fundYear1) {
        this.fundYear1 = fundYear1;
    }

    public String getFundYear2() {
        return fundYear2;
    }

    public void setFundYear2(String fundYear2) {
        this.fundYear2 = fundYear2;
    }

    public String getFundYear3() {
        return fundYear3;
    }

    public void setFundYear3(String fundYear3) {
        this.fundYear3 = fundYear3;
    }

    public String getSumFundSum() {
        return sumFundSum;
    }

    public void setSumFundSum(String sumFundSum) {
        this.sumFundSum = sumFundSum;
    }

    public String getSumFundYear1() {
        return sumFundYear1;
    }

    public void setSumFundYear1(String sumFundYear1) {
        this.sumFundYear1 = sumFundYear1;
    }

    public String getSumFundYear2() {
        return sumFundYear2;
    }

    public void setSumFundYear2(String sumFundYear2) {
        this.sumFundYear2 = sumFundYear2;
    }

    public String getSumFundYear3() {
        return sumFundYear3;
    }

    public void setSumFundYear3(String sumFundYear3) {
        this.sumFundYear3 = sumFundYear3;
    }

    public String getSumFundOther() {
        return sumFundOther;
    }

    public void setSumFundOther(String sumFundOther) {
        this.sumFundOther = sumFundOther;
    }

    public String getEquipmentSum() {
        return equipmentSum;
    }

    public void setEquipmentSum(String equipmentSum) {
        this.equipmentSum = equipmentSum;
    }

    public String getEquipmentYear1() {
        return equipmentYear1;
    }

    public void setEquipmentYear1(String equipmentYear1) {
        this.equipmentYear1 = equipmentYear1;
    }

    public String getEquipmentYear2() {
        return equipmentYear2;
    }

    public void setEquipmentYear2(String equipmentYear2) {
        this.equipmentYear2 = equipmentYear2;
    }

    public String getEquipmentYear3() {
        return equipmentYear3;
    }

    public void setEquipmentYear3(String equipmentYear3) {
        this.equipmentYear3 = equipmentYear3;
    }

    public String getEquipmentOther() {
        return equipmentOther;
    }

    public void setEquipmentOther(String equipmentOther) {
        this.equipmentOther = equipmentOther;
    }

    public String getPurchaseSum() {
        return purchaseSum;
    }

    public void setPurchaseSum(String purchaseSum) {
        this.purchaseSum = purchaseSum;
    }

    public String getPurchaseYear1() {
        return purchaseYear1;
    }

    public void setPurchaseYear1(String purchaseYear1) {
        this.purchaseYear1 = purchaseYear1;
    }

    public String getPurchaseYear2() {
        return purchaseYear2;
    }

    public void setPurchaseYear2(String purchaseYear2) {
        this.purchaseYear2 = purchaseYear2;
    }

    public String getPurchaseYear3() {
        return purchaseYear3;
    }

    public String getMaterialSum() {
        return materialSum;
    }

    public void setMaterialSum(String materialSum) {
        this.materialSum = materialSum;
    }

    public String getAssaySum() {
        return assaySum;
    }

    public void setAssaySum(String assaySum) {
        this.assaySum = assaySum;
    }

    public String getFuelSum() {
        return fuelSum;
    }

    public void setFuelSum(String fuelSum) {
        this.fuelSum = fuelSum;
    }

    public String getTravelSum() {
        return travelSum;
    }

    public void setTravelSum(String travelSum) {
        this.travelSum = travelSum;
    }

    public void setPurchaseYear3(String purchaseYear3) {
        this.purchaseYear3 = purchaseYear3;
    }

    public String getPurchaseOther() {
        return purchaseOther;
    }

    public void setPurchaseOther(String purchaseOther) {
        this.purchaseOther = purchaseOther;
    }

    public String getTrialSum() {
        return trialSum;
    }

    public void setTrialSum(String trialSum) {
        this.trialSum = trialSum;
    }

    public String getTrialYear1() {
        return trialYear1;
    }

    public void setTrialYear1(String trialYear1) {
        this.trialYear1 = trialYear1;
    }

    public String getTrialYear2() {
        return trialYear2;
    }

    public void setTrialYear2(String trialYear2) {
        this.trialYear2 = trialYear2;
    }

    public String getTrialYear3() {
        return trialYear3;
    }

    public void setTrialYear3(String trialYear3) {
        this.trialYear3 = trialYear3;
    }

    public String getTrialOther() {
        return trialOther;
    }

    public void setTrialOther(String trialOther) {
        this.trialOther = trialOther;
    }

    public String getLeaseSum() {
        return leaseSum;
    }

    public void setLeaseSum(String leaseSum) {
        this.leaseSum = leaseSum;
    }

    public String getLeaseYear1() {
        return leaseYear1;
    }

    public void setLeaseYear1(String leaseYear1) {
        this.leaseYear1 = leaseYear1;
    }

    public String getLeaseYear2() {
        return leaseYear2;
    }

    public void setLeaseYear2(String leaseYear2) {
        this.leaseYear2 = leaseYear2;
    }

    public String getLeaseYear3() {
        return leaseYear3;
    }

    public void setLeaseYear3(String leaseYear3) {
        this.leaseYear3 = leaseYear3;
    }

    public String getLeaseOther() {
        return leaseOther;
    }

    public void setLeaseOther(String leaseOther) {
        this.leaseOther = leaseOther;
    }

    public String getT71() {
        return materialSum;
    }

    public void setT71(String materialSum) {
        this.materialSum = materialSum;
    }

    public String getMaterialYear1() {
        return materialYear1;
    }

    public void setMaterialYear1(String materialYear1) {
        this.materialYear1 = materialYear1;
    }

    public String getMaterialYear2() {
        return materialYear2;
    }

    public void setMaterialYear2(String materialYear2) {
        this.materialYear2 = materialYear2;
    }

    public String getMaterialYear3() {
        return materialYear3;
    }

    public void setMaterialYear3(String materialYear3) {
        this.materialYear3 = materialYear3;
    }

    public String getMaterialOther() {
        return materialOther;
    }

    public void setMaterialOther(String materialOther) {
        this.materialOther = materialOther;
    }

    public String getT81() {
        return assaySum;
    }

    public void setT81(String assaySum) {
        this.assaySum = assaySum;
    }

    public String getAssayYear1() {
        return assayYear1;
    }

    public void setAssayYear1(String assayYear1) {
        this.assayYear1 = assayYear1;
    }

    public String getAssayYear2() {
        return assayYear2;
    }

    public void setAssayYear2(String assayYear2) {
        this.assayYear2 = assayYear2;
    }

    public String getAssayYear3() {
        return assayYear3;
    }

    public void setAssayYear3(String assayYear3) {
        this.assayYear3 = assayYear3;
    }

    public String getAssayOther() {
        return assayOther;
    }

    public void setAssayOther(String assayOther) {
        this.assayOther = assayOther;
    }

    public String getT91() {
        return fuelSum;
    }

    public void setT91(String fuelSum) {
        this.fuelSum = fuelSum;
    }

    public String getFuelYear1() {
        return fuelYear1;
    }

    public void setFuelYear1(String fuelYear1) {
        this.fuelYear1 = fuelYear1;
    }

    public String getFuelYear2() {
        return fuelYear2;
    }

    public void setFuelYear2(String fuelYear2) {
        this.fuelYear2 = fuelYear2;
    }

    public String getFuelYear3() {
        return fuelYear3;
    }

    public void setFuelYear3(String fuelYear3) {
        this.fuelYear3 = fuelYear3;
    }

    public String getFuelOther() {
        return fuelOther;
    }

    public void setFuelOther(String fuelOther) {
        this.fuelOther = fuelOther;
    }

    public String getTa1() {
        return travelSum;
    }

    public void setTa1(String travelSum) {
        this.travelSum = travelSum;
    }

    public String getTravelYear1() {
        return travelYear1;
    }

    public void setTravelYear1(String travelYear1) {
        this.travelYear1 = travelYear1;
    }

    public String getTravelYear2() {
        return travelYear2;
    }

    public void setTravelYear2(String travelYear2) {
        this.travelYear2 = travelYear2;
    }

    public String getTravelYear3() {
        return travelYear3;
    }

    public void setTravelYear3(String travelYear3) {
        this.travelYear3 = travelYear3;
    }

    public String getTravelOther() {
        return travelOther;
    }

    public void setTravelOther(String travelOther) {
        this.travelOther = travelOther;
    }

    public String getTb1() {
        return conferenceSum;
    }

    public void setTb1(String conferenceSum) {
        this.conferenceSum = conferenceSum;
    }

    public String getConferenceYear1() {
        return conferenceYear1;
    }

    public void setConferenceYear1(String conferenceYear1) {
        this.conferenceYear1 = conferenceYear1;
    }

    public String getConferenceYear2() {
        return conferenceYear2;
    }

    public void setConferenceYear2(String conferenceYear2) {
        this.conferenceYear2 = conferenceYear2;
    }

    public String getConferenceYear3() {
        return conferenceYear3;
    }

    public void setConferenceYear3(String conferenceYear3) {
        this.conferenceYear3 = conferenceYear3;
    }

    public String getConferenceOther() {
        return conferenceOther;
    }

    public void setConferenceOther(String conferenceOther) {
        this.conferenceOther = conferenceOther;
    }

    public String getTc1() {
        return exchangeSum;
    }

    public void setTc1(String exchangeSum) {
        this.exchangeSum = exchangeSum;
    }

    public String getExchangeYear1() {
        return exchangeYear1;
    }

    public void setExchangeYear1(String exchangeYear1) {
        this.exchangeYear1 = exchangeYear1;
    }

    public String getExchangeYear2() {
        return exchangeYear2;
    }

    public void setExchangeYear2(String exchangeYear2) {
        this.exchangeYear2 = exchangeYear2;
    }

    public String getExchangeYear3() {
        return exchangeYear3;
    }

    public void setExchangeYear3(String exchangeYear3) {
        this.exchangeYear3 = exchangeYear3;
    }

    public String getExchangeOther() {
        return exchangeOther;
    }

    public void setExchangeOther(String exchangeOther) {
        this.exchangeOther = exchangeOther;
    }

    public String getServiceSum() {
        return serviceSum;
    }

    public void setServiceSum(String serviceSum) {
        this.serviceSum = serviceSum;
    }

    public String getServiceYear1() {
        return serviceYear1;
    }

    public void setServiceYear1(String serviceYear1) {
        this.serviceYear1 = serviceYear1;
    }

    public String getServiceYear2() {
        return serviceYear2;
    }

    public void setServiceYear2(String serviceYear2) {
        this.serviceYear2 = serviceYear2;
    }

    public String getServiceYear3() {
        return serviceYear3;
    }

    public void setServiceYear3(String serviceYear3) {
        this.serviceYear3 = serviceYear3;
    }

    public String getServiceOther() {
        return serviceOther;
    }

    public void setServiceOther(String serviceOther) {
        this.serviceOther = serviceOther;
    }

    public String getSoftwarSum() {
        return softwarSum;
    }

    public void setSoftwarSum(String softwarSum) {
        this.softwarSum = softwarSum;
    }

    public String getSoftwarYear1() {
        return softwarYear1;
    }

    public void setSoftwarYear1(String softwarYear1) {
        this.softwarYear1 = softwarYear1;
    }

    public String getSoftwarYear2() {
        return softwarYear2;
    }

    public void setSoftwarYear2(String softwarYear2) {
        this.softwarYear2 = softwarYear2;
    }

    public String getSoftwarYear3() {
        return softwarYear3;
    }

    public void setSoftwarYear3(String softwarYear3) {
        this.softwarYear3 = softwarYear3;
    }

    public String getSoftwarOther() {
        return softwarOther;
    }

    public void setSoftwarOther(String softwarOther) {
        this.softwarOther = softwarOther;
    }

    public String getLaborSum() {
        return laborSum;
    }

    public void setLaborSum(String laborSum) {
        this.laborSum = laborSum;
    }

    public String getLaborYear1() {
        return laborYear1;
    }

    public void setLaborYear1(String laborYear1) {
        this.laborYear1 = laborYear1;
    }

    public String getLaborYear2() {
        return laborYear2;
    }

    public void setLaborYear2(String laborYear2) {
        this.laborYear2 = laborYear2;
    }

    public String getLaborYear3() {
        return laborYear3;
    }

    public void setLaborYear3(String laborYear3) {
        this.laborYear3 = laborYear3;
    }

    public String getLaborOther() {
        return laborOther;
    }

    public void setLaborOther(String laborOther) {
        this.laborOther = laborOther;
    }

    public String getAdvisorySum() {
        return advisorySum;
    }

    public void setAdvisorySum(String advisorySum) {
        this.advisorySum = advisorySum;
    }

    public String getAdvisoryYear1() {
        return advisoryYear1;
    }

    public void setAdvisoryYear1(String advisoryYear1) {
        this.advisoryYear1 = advisoryYear1;
    }

    public String getAdvisoryYear2() {
        return advisoryYear2;
    }

    public void setAdvisoryYear2(String advisoryYear2) {
        this.advisoryYear2 = advisoryYear2;
    }

    public String getAdvisoryYear3() {
        return advisoryYear3;
    }

    public void setAdvisoryYear3(String advisoryYear3) {
        this.advisoryYear3 = advisoryYear3;
    }

    public String getAdvisoryOther() {
        return advisoryOther;
    }

    public void setAdvisoryOther(String advisoryOther) {
        this.advisoryOther = advisoryOther;
    }

    public String getExternalSum() {
        return externalSum;
    }

    public void setExternalSum(String externalSum) {
        this.externalSum = externalSum;
    }

    public String getExternalYear1() {
        return externalYear1;
    }

    public void setExternalYear1(String externalYear1) {
        this.externalYear1 = externalYear1;
    }

    public String getExternalYear2() {
        return externalYear2;
    }

    public void setExternalYear2(String externalYear2) {
        this.externalYear2 = externalYear2;
    }

    public String getExternalYear3() {
        return externalYear3;
    }

    public void setExternalYear3(String externalYear3) {
        this.externalYear3 = externalYear3;
    }

    public String getExternalOther() {
        return externalOther;
    }

    public void setExternalOther(String externalOther) {
        this.externalOther = externalOther;
    }

    public String getManSum() {
        return manSum;
    }

    public void setManSum(String manSum) {
        this.manSum = manSum;
    }

    public String getManYear1() {
        return manYear1;
    }

    public void setManYear1(String manYear1) {
        this.manYear1 = manYear1;
    }

    public String getManYear2() {
        return manYear2;
    }

    public void setManYear2(String manYear2) {
        this.manYear2 = manYear2;
    }

    public String getManYear3() {
        return manYear3;
    }

    public void setManYear3(String manYear3) {
        this.manYear3 = manYear3;
    }

    public String getManOther() {
        return manOther;
    }

    public void setManOther(String manOther) {
        this.manOther = manOther;
    }

    public String getTotalPeople() {
        return totalPeople;
    }

    public void setTotalPeople(String totalPeople) {
        this.totalPeople = totalPeople;
    }

    public String getPositive() {
        return positive;
    }

    public void setPositive(String positive) {
        this.positive = positive;
    }

    public String getDeputy() {
        return deputy;
    }

    public void setDeputy(String deputy) {
        this.deputy = deputy;
    }

    public String getIntermediate() {
        return intermediate;
    }

    public void setIntermediate(String intermediate) {
        this.intermediate = intermediate;
    }

    public String getPrimary() {
        return primary;
    }

    public void setPrimary(String primary) {
        this.primary = primary;
    }

    public String getPostgraduate() {
        return postgraduate;
    }

    public void setPostgraduate(String postgraduate) {
        this.postgraduate = postgraduate;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getJunior() {
        return junior;
    }

    public void setJunior(String junior) {
        this.junior = junior;
    }

    public String getPhD() {
        return PhD;
    }

    public void setPhD(String phD) {
        this.PhD = phD;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

}
