package com.adc.da.research.service;


import com.adc.da.budget.vo.DashBoardVO;
import com.adc.da.research.dao.ResProArriveFeeEODao;
import com.adc.da.research.dao.ResProFeeDetailEODao;
import com.adc.da.research.dao.ResearchProjectEODao;
import com.adc.da.research.entity.ResProArriveFeeEO;
import com.adc.da.research.entity.ResProFeeDetailEO;
import com.adc.da.research.entity.ResearchProjectEO;
import com.adc.da.research.page.ResProArriveFeeEOPage;
import com.adc.da.research.page.ResearchProjectEOPage;
import com.adc.da.research.vo.DashBoardBarVO;
import com.adc.da.research.vo.HeaderVO;
import com.adc.da.research.vo.ResearchProjectSortVO;
import com.adc.da.sys.dao.DicTypeEODao;
import com.adc.da.sys.entity.DicTypeEO;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import io.swagger.annotations.Api;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service("reportService")
public class ReportService {


    @Autowired
    private ResearchProjectEODao researchProjectEODao ;

    @Autowired
    private ResProFeeDetailEODao resProFeeDetailEODao;

    @Autowired
    private ResProArriveFeeEODao resProArriveFeeEODao;
    @Autowired
    private DicTypeEODao dicTypeEODao;



    public List<String> getProjectStatusDicTypeNameList(String type){
        List<DicTypeEO> dicTypeEOList = dicTypeEODao.queryByList("RES_DB_PRO_STATUS");
        List<String> dicTypeNameList = new ArrayList<>();
        for (DicTypeEO dicTypeEO : dicTypeEOList){
            String dicTypeCode = dicTypeEO.getDicTypeCode();
            if (StringUtils.isEmpty(dicTypeCode)){
                continue;
            }
            if (StringUtils.contains(dicTypeCode,type)){
                dicTypeNameList.add(dicTypeEO.getDicTypeName());
            }
        }

        return dicTypeNameList;
    }

    public List<String> getProjectLevelDicTypeNameList(String type){
        List<DicTypeEO> dicTypeEOList = dicTypeEODao.queryByList("RES_DB_PRO_LEVEL");
        List<String> dicTypeLevelList = new ArrayList<>();
        for (DicTypeEO dicTypeEO : dicTypeEOList){
            String dicTypeCode = dicTypeEO.getDicTypeCode();
            if (StringUtils.isEmpty(dicTypeCode)){
                continue;
            }
            if (StringUtils.contains(dicTypeCode,type)){
                dicTypeLevelList.add(dicTypeEO.getDicTypeName());
            }
        }

        return dicTypeLevelList;
    }


    /**
     *
     * 计算看板的四个总额
     *
     */
    public HeaderVO getHeaderVO(){
        DateTime dateTime = new DateTime();
        int year = dateTime.getYear();
        HeaderVO headerVO = new HeaderVO();
        ResearchProjectEOPage researchProjectEOPage = new ResearchProjectEOPage();
        researchProjectEOPage.setYear(String.valueOf(year));
        List<ResearchProjectEO> researchProjectEOList = researchProjectEODao.queryByList(researchProjectEOPage);
        Set<String> projectNoSet = new HashSet<>();
        BigDecimal allFunds = BigDecimal.ZERO;
        BigDecimal nationFunds = BigDecimal.ZERO; //getAllocateFunds
        BigDecimal selfFunds = BigDecimal.ZERO;
        List<String> levelList = getProjectLevelDicTypeNameList("_1");
        List<String> statusList = getProjectStatusDicTypeNameList("_1");
        for (ResearchProjectEO researchProjectEO : researchProjectEOList){
            if (!levelList.contains(researchProjectEO.getProjectLevelDicName())){
                continue;
            }
            if (!statusList.contains(researchProjectEO.getProjectStatusDicName())){
                continue;
            }
            if (!projectNoSet.contains(researchProjectEO.getProjectNo())){
                projectNoSet.add(researchProjectEO.getProjectNo());
            }
            if (null != researchProjectEO.getAllFunds()){
                allFunds = allFunds.add(researchProjectEO.getAllFunds());
            }

            if (null != researchProjectEO.getAllocateFunds()){
                nationFunds = nationFunds.add(researchProjectEO.getAllocateFunds());
            }

            if (null != researchProjectEO.getRaiseFunds()){
                selfFunds = selfFunds.add(researchProjectEO.getRaiseFunds());
            }
        }
        headerVO.setAllFunds(allFunds.setScale(0,BigDecimal.ROUND_HALF_UP).intValue());
        headerVO.setNationFunds(nationFunds.setScale(0,BigDecimal.ROUND_HALF_UP).intValue());
        headerVO.setSelfFunds(selfFunds.setScale(0,BigDecimal.ROUND_HALF_UP).intValue());
        headerVO.setProjectNum(projectNoSet.size());

        return headerVO;
    }

    /**
     * 科研经费月度使用情况一览
     */
    public DashBoardBarVO getDataByType(int type){
        DateTime dateTime = new DateTime();
        int currentYear = dateTime.getYear();
        dateTime = dateTime.minusMonths(11);
        int lastYear = dateTime.getYear();
        int month = dateTime.getMonthOfYear();
        List<String> levelList = getProjectLevelDicTypeNameList("_1");
        List<String> statusList = getProjectStatusDicTypeNameList("_1");
        DashBoardBarVO dashBoardVO = new DashBoardBarVO();
        List<String> indexList = dashBoardVO.getXIndexList();
        List<Integer> allocateDataList = dashBoardVO.getBudgetDataList();
        List<Integer> useDataList = dashBoardVO.getUseDataList();
        ResearchProjectEOPage researchProjectEOPage = new ResearchProjectEOPage();
        researchProjectEOPage.setYear(String.valueOf(currentYear));
        List<ResearchProjectEO> researchProjectEOList = researchProjectEODao.queryByList(researchProjectEOPage);
        Set<String> projectIdSet = new HashSet<>();
        BigDecimal selfFunds = BigDecimal.ZERO;
        for (ResearchProjectEO researchProjectEO : researchProjectEOList) {
            if (!levelList.contains(researchProjectEO.getProjectLevelDicName())){
                continue;
            }
            if (!statusList.contains(researchProjectEO.getProjectStatusDicName())){
                continue;
            }
            if (!projectIdSet.contains(researchProjectEO.getResearchProjectId())) {
                projectIdSet.add(researchProjectEO.getResearchProjectId());
            }
            if (null != researchProjectEO.getRaiseFunds()){
                selfFunds = selfFunds.add(researchProjectEO.getRaiseFunds());
            }
        }
        if (CollectionUtils.isEmpty(projectIdSet)){
            return dashBoardVO;
        }

        List<ResProFeeDetailEO> resProFeeDetailEOList = resProFeeDetailEODao.queryByResearchProjectIdList(new ArrayList<String>(projectIdSet));
        List<ResProArriveFeeEO> resProArriveFeeEOList = resProArriveFeeEODao.queryByYearEqAndMonthGteAndResearchProjectIdList(lastYear,month, new ArrayList<String>(projectIdSet));
        resProArriveFeeEOList.addAll(resProArriveFeeEODao.queryByYearGteAndResearchProjectIdList(currentYear, new ArrayList<String>(projectIdSet)));
        int m = month;
        for (int y = lastYear ; y <= currentYear; y ++ ){
            for (; (m  <= 12 && y < currentYear) || (m  < month && y == currentYear); m ++ ){
                BigDecimal useFunds = BigDecimal.ZERO;
                BigDecimal allocateFunds = BigDecimal.ZERO;
                String index = y + "/" + m;
                if (m < 10) {
                    index = y + "/0" + m ;
                }


                for (ResProFeeDetailEO resProFeeDetailEO : resProFeeDetailEOList){
                    if (type == 1){
                        if (!StringUtils.equals(resProFeeDetailEO.getFundsType(),"国拨费用（使用）")){
                            continue;
                        }
                        if (resProFeeDetailEO.getFundsYear() == y && resProFeeDetailEO.getFundsMonth() == m ){
                            useFunds = useFunds.add(doSumResProFeeDetailEO(resProFeeDetailEO));
                        }
                    }else if (type == 2){
                        if (!StringUtils.equals(resProFeeDetailEO.getFundsType(),"自筹费用（使用）")){
                            continue;
                        }
                        if (resProFeeDetailEO.getFundsYear() == y && resProFeeDetailEO.getFundsMonth() == m ){
                            useFunds = useFunds.add(doSumResProFeeDetailEO(resProFeeDetailEO));
                        }
                    }else if (type == 3){
                        if (!StringUtils.equals(resProFeeDetailEO.getFundsType(),"国拨费用（使用）")&&!StringUtils.equals(resProFeeDetailEO.getFundsType(),"自筹费用（使用）")){
                            continue;
                        }
                        if (resProFeeDetailEO.getFundsYear() == y && resProFeeDetailEO.getFundsMonth() == m) {
                            useFunds = useFunds.add(doSumResProFeeDetailEO(resProFeeDetailEO));
                        }
                    }

                }

                if (type == 1) {
                    for (ResProArriveFeeEO resProArriveFeeEO : resProArriveFeeEOList) {
                        if (y == resProArriveFeeEO.getArriveYear() &&  m == resProArriveFeeEO.getArriveMonth() && null != resProArriveFeeEO.getArriveFee()) {
                            allocateFunds = allocateFunds.add(resProArriveFeeEO.getArriveFee());
                        }
                    }
                }else if (type ==2){
                    allocateFunds = selfFunds;
                }else if (type == 3){
                    for (ResProArriveFeeEO resProArriveFeeEO : resProArriveFeeEOList) {
                        if (y == resProArriveFeeEO.getArriveYear() &&  m  == resProArriveFeeEO.getArriveMonth() && null != resProArriveFeeEO.getArriveFee()) {
                            allocateFunds = allocateFunds.add(resProArriveFeeEO.getArriveFee());
                        }
                    }
                    allocateFunds = allocateFunds.add(selfFunds);

                }
                indexList.add(index);
                allocateDataList.add(allocateFunds.setScale(0,BigDecimal.ROUND_HALF_UP).intValue());
                useDataList.add(useFunds.setScale(0,BigDecimal.ROUND_HALF_UP).intValue());
                if(m + 1 == 13){
                    m = 1;
                    break;
                }
            }
        }
        return dashBoardVO;
    }

    /**
     * 科研经费拨付详情/科研经费使用详情
     * @param
     * @return
     */
    public DashBoardBarVO projectAllocateDetail(int type){
        DashBoardBarVO dashBoardBarVO = new DashBoardBarVO();
        DateTime dateTime = new DateTime();
        int currentYear = dateTime.getYear();
        List<String> levelList = getProjectLevelDicTypeNameList("_1");
        List<String> statusList = getProjectStatusDicTypeNameList("_1");

        DashBoardBarVO dashBoardVO = new DashBoardBarVO();
        List<String> indexList = dashBoardVO.getXIndexList();
        List<Integer> allocateDataList = dashBoardVO.getBudgetDataList();
        ResearchProjectEOPage researchProjectEOPage = new ResearchProjectEOPage();
        researchProjectEOPage.setYear(String.valueOf(currentYear));
        List<ResearchProjectEO> researchProjectEOList = researchProjectEODao.queryByList(researchProjectEOPage);
        Set<String> projectIdSet = new HashSet<>();
        BigDecimal selfFunds = BigDecimal.ZERO;
        BigDecimal nationFunds = BigDecimal.ZERO;
        BigDecimal allFunds =BigDecimal.ZERO;
        for (ResearchProjectEO researchProjectEO : researchProjectEOList) {
            if (!levelList.contains(researchProjectEO.getProjectLevelDicName())){
                continue;
            }
            if (!statusList.contains(researchProjectEO.getProjectStatusDicName())){
                continue;
            }
            if (!projectIdSet.contains(researchProjectEO.getResearchProjectId())) {
                projectIdSet.add(researchProjectEO.getResearchProjectId());
            }
            if (null != researchProjectEO.getRaiseFunds()){
                selfFunds = selfFunds.add(researchProjectEO.getRaiseFunds());
            }
            if (null != researchProjectEO.getAllocateFunds()){
                nationFunds = nationFunds.add(researchProjectEO.getAllocateFunds());
            }

            if (null != researchProjectEO.getAllFunds()){
                allFunds = allFunds.add(researchProjectEO.getAllFunds());
            }
        }
        if (CollectionUtils.isEmpty(projectIdSet)){
            return dashBoardBarVO;
        }
        //科研经费拨付详情
        BigDecimal currentArriveFee = resProArriveFeeEODao.sumArriveFeeByYearAndResearchProjectIdList(currentYear,new ArrayList<String>(projectIdSet));
        currentArriveFee = currentArriveFee == null?BigDecimal.ZERO:currentArriveFee;
        BigDecimal lastArriveFee = resProArriveFeeEODao.sumArriveFeeByYearAndResearchProjectIdList(currentYear-1,new ArrayList<String>(projectIdSet));
        lastArriveFee = lastArriveFee == null?BigDecimal.ZERO:lastArriveFee;
        BigDecimal formerArriveFee = resProArriveFeeEODao.sumArriveFeeByYearAndResearchProjectIdList(currentYear-2,new ArrayList<String>(projectIdSet));
        formerArriveFee = formerArriveFee == null?BigDecimal.ZERO:formerArriveFee;
        BigDecimal allArriveFee = resProArriveFeeEODao.sumArriveFeeByResearchProjectIdList(new ArrayList<String>(projectIdSet));
        allArriveFee = allArriveFee == null?BigDecimal.ZERO:allArriveFee;
        //BigDecimal nationFunds = researchProjectEODao.sumNationFundsByResearchProjectIdList(new ArrayList<String>(projectIdSet));
        nationFunds = nationFunds == null?BigDecimal.ZERO:nationFunds;

        //科研经费使用详情
        List<ResProFeeDetailEO> resProFeeDetailEOList = resProFeeDetailEODao.queryByResearchProjectIdList(new ArrayList<String>(projectIdSet));
        BigDecimal currentSelfFunds = BigDecimal.ZERO;
        BigDecimal lastSelfFunds = BigDecimal.ZERO;
        BigDecimal formerSelfFunds =BigDecimal.ZERO;

        for (ResProFeeDetailEO resProFeeDetailEO : resProFeeDetailEOList){
            if (!StringUtils.equals(resProFeeDetailEO.getFundsType(),"自筹费用（使用）")){
                continue;
            }
            if (currentYear == resProFeeDetailEO.getFundsYear()){
                currentSelfFunds =currentSelfFunds.add(doSumResProFeeDetailEO(resProFeeDetailEO));
            }
            if (currentYear-1 == resProFeeDetailEO.getFundsYear()){
                lastSelfFunds =lastSelfFunds.add(doSumResProFeeDetailEO(resProFeeDetailEO));
            }
            if (currentYear-2 == resProFeeDetailEO.getFundsYear()){
                formerSelfFunds =formerSelfFunds.add(doSumResProFeeDetailEO(resProFeeDetailEO));
            }
        }
        if (type == 1) {
            //科研经费拨付详情
            indexList.add(currentYear-2+"使用金额");
            allocateDataList.add(formerArriveFee.setScale(0,BigDecimal.ROUND_HALF_UP).intValue());
            indexList.add(currentYear-1+"使用金额");
            allocateDataList.add(lastArriveFee.setScale(0,BigDecimal.ROUND_HALF_UP).intValue());
            indexList.add(currentYear+"使用金额");
            allocateDataList.add(currentArriveFee.setScale(0,BigDecimal.ROUND_HALF_UP).intValue());
            indexList.add("已到账金额");
            allocateDataList.add(allArriveFee.setScale(0,BigDecimal.ROUND_HALF_UP).intValue());
            indexList.add("国拨总额");
            allocateDataList.add(nationFunds.setScale(0,BigDecimal.ROUND_HALF_UP).intValue());
        }
        if (type == 2) {
            indexList.add(currentYear-2+"使用金额");
            allocateDataList.add(formerSelfFunds.setScale(0,BigDecimal.ROUND_HALF_UP).intValue());
            indexList.add(currentYear-1+"使用金额");
            allocateDataList.add(lastSelfFunds.setScale(0,BigDecimal.ROUND_HALF_UP).intValue());
            indexList.add(currentYear+"使用金额");
            allocateDataList.add(currentSelfFunds.setScale(0,BigDecimal.ROUND_HALF_UP).intValue());
            indexList.add("已到账金额");
            allocateDataList.add(selfFunds.setScale(0,BigDecimal.ROUND_HALF_UP).intValue());
            indexList.add("自筹总额");
            allocateDataList.add(selfFunds.setScale(0,BigDecimal.ROUND_HALF_UP).intValue());
        }

        if (type == 3) {
            indexList.add(currentYear-2+"使用金额");
            allocateDataList.add(formerSelfFunds.add(formerArriveFee).setScale(0,BigDecimal.ROUND_HALF_UP).intValue());
            indexList.add(currentYear-1+"使用金额");
            allocateDataList.add(lastSelfFunds.add(lastArriveFee).setScale(0,BigDecimal.ROUND_HALF_UP).intValue());
            indexList.add(currentYear+"使用金额");
            allocateDataList.add(currentSelfFunds.add(currentArriveFee).setScale(0,BigDecimal.ROUND_HALF_UP).intValue());
            indexList.add("已到账金额");
            allocateDataList.add(allArriveFee.add(selfFunds).setScale(0,BigDecimal.ROUND_HALF_UP).intValue());
            indexList.add("经费总额");
            allocateDataList.add(allFunds.setScale(0,BigDecimal.ROUND_HALF_UP).intValue());
        }

        dashBoardBarVO.setXIndexList(indexList);
        dashBoardBarVO.setUseDataList(allocateDataList);

        return dashBoardBarVO;

    }

    /**
     *
     * 科研经费使用详情
     * @return
     */
    public List<Map<String,Object>> getFundsUseDetail(int type){
        List<Map<String,Object>> mapList = new ArrayList<>();
        DateTime dateTime = new DateTime();
        int currentYear = dateTime.getYear();

        List<String> levelList = getProjectLevelDicTypeNameList("_1");
        List<String> statusList = getProjectStatusDicTypeNameList("_1");

        ResearchProjectEOPage researchProjectEOPage = new ResearchProjectEOPage();
        researchProjectEOPage.setYear(String.valueOf(currentYear));
        List<ResearchProjectEO> researchProjectEOList = researchProjectEODao.queryByList(researchProjectEOPage);
        Set<String> projectIdSet = new HashSet<>();
        for (ResearchProjectEO researchProjectEO : researchProjectEOList) {
            if (!levelList.contains(researchProjectEO.getProjectLevelDicName())){
                continue;
            }
            if (!statusList.contains(researchProjectEO.getProjectStatusDicName())){
                continue;
            }
            if (!projectIdSet.contains(researchProjectEO.getResearchProjectId())) {
                projectIdSet.add(researchProjectEO.getResearchProjectId());
            }
        }
        if (CollectionUtils.isEmpty(projectIdSet)){
            return mapList;
        }
        List<ResProFeeDetailEO> resProFeeDetailEOList = resProFeeDetailEODao.sumByResearchProjectIdListGroupByFundsType(new ArrayList<String>(projectIdSet));
        ResProFeeDetailEO resultEO = new ResProFeeDetailEO();
        for (ResProFeeDetailEO resProFeeDetailEO : resProFeeDetailEOList){
            if (type == 1){
                if (StringUtils.equals(resProFeeDetailEO.getFundsType(),"国拨费用（预算）")){
                    addData2Map(resProFeeDetailEO,mapList);
                }
            }else if (type == 2){
                if (StringUtils.equals(resProFeeDetailEO.getFundsType(),"自筹费用（预算）")){
                    addData2Map(resProFeeDetailEO,mapList);
                }
            }else if (type == 3){
                ResProFeeDetailEO tmp = null;
                if (StringUtils.equals(resProFeeDetailEO.getFundsType(),"国拨费用（预算）")){
                    tmp = resProFeeDetailEO;
                }
                if (StringUtils.equals(resProFeeDetailEO.getFundsType(),"自筹费用（预算）")){
                    tmp = resProFeeDetailEO;
                }
                if (null == tmp){
                    continue;
                }


                if (null != tmp.getLaborFee()) {
                    resultEO.setLaborFee(doAdd(resultEO.getLaborFee(), tmp.getLaborFee()));
                }
                if (null != tmp.getInternationExchangeFee()) {
                    resultEO.setInternationExchangeFee(doAdd(resultEO.getInternationExchangeFee(), tmp.getInternationExchangeFee()));
                }
                if (null != tmp.getMeetingFee()) {
                    resultEO.setMeetingFee(doAdd(resultEO.getMeetingFee(), tmp.getMeetingFee()));
                }
                if (null != tmp.getTravelFee()) {
                    resultEO.setTravelFee(doAdd(resultEO.getTravelFee(), tmp.getTravelFee()));
                }
                if (null != tmp.getFuelPowerFee()) {
                    resultEO.setFuelPowerFee(doAdd(resultEO.getFuelPowerFee(), tmp.getFuelPowerFee()));
                }
                if (null != tmp.getEquipPurchaseFee()) {
                    resultEO.setEquipPurchaseFee(doAdd(resultEO.getEquipPurchaseFee(), tmp.getEquipPurchaseFee()));
                }
                if (null != tmp.getEquipRentFee()) {
                    resultEO.setEquipRentFee(doAdd(resultEO.getEquipRentFee(), tmp.getEquipRentFee()));
                }
                if (null != tmp.getEquipTestCreateFee()) {
                    resultEO.setEquipTestCreateFee(doAdd(resultEO.getEquipTestCreateFee(), tmp.getEquipTestCreateFee()));
                }

                if (null != resProFeeDetailEO.getMaterialFee()) {
                    resultEO.setMaterialFee(doAdd(resultEO.getMaterialFee(), tmp.getMaterialFee()));
                }
                if (null != resProFeeDetailEO.getTestProcessFee()) {
                    resultEO.setTestProcessFee(doAdd(resultEO.getTestProcessFee(), tmp.getTestProcessFee()));
                }
                if (null != resProFeeDetailEO.getExpertConsultFee()) {
                    resultEO.setExpertConsultFee(doAdd(resultEO.getExpertConsultFee(), tmp.getExpertConsultFee()));
                }
                if (null != resProFeeDetailEO.getExternalAssistFee()) {
                    resultEO.setExternalAssistFee(doAdd(resultEO.getExternalAssistFee(), tmp.getExternalAssistFee()));
                }
                if (null != resProFeeDetailEO.getPersonFee()) {
                    resultEO.setPersonFee(doAdd(resultEO.getPersonFee(), tmp.getPersonFee()));
                }
                if (null != resProFeeDetailEO.getManageFee()) {
                    resultEO.setManageFee(doAdd(resultEO.getManageFee(), tmp.getManageFee()));
                }
                if (null != resProFeeDetailEO.getkSoftFee()) {
                    resultEO.setkSoftFee(doAdd(resultEO.getkSoftFee(), tmp.getkSoftFee()));
                }
                if (null != resProFeeDetailEO.getkOtherFee()) {
                    resultEO.setkOtherFee(doAdd(resultEO.getkOtherFee(), tmp.getkOtherFee()));
                }
                if (null != resProFeeDetailEO.getOtherFee()) {
                    resultEO.setOtherFee(doAdd(resultEO.getOtherFee(), tmp.getOtherFee()));
                }
            }
        }
        if (type == 3){
            addData2Map(resultEO,mapList);
        }
        return mapList;
    }

    /**
     *各部门科研经费详情
     * @param type
     * @return
     */

    public DashBoardBarVO getOrgFundsDetail(int type ){
        DashBoardBarVO dashBoardBarVO = new DashBoardBarVO();
        List<String> indexList = dashBoardBarVO.getXIndexList();
        List<Integer> budgetDataList = dashBoardBarVO.getBudgetDataList();
        List<Integer> useDataList = dashBoardBarVO.getUseDataList();

        DateTime dateTime = new DateTime();
        int currentYear = dateTime.getYear();

//        List<String> levelList = Arrays.asList("国家级","省部级");
//        List<String> statusList = Arrays.asList("中标未公示","公示未签任务书","在研（未建本）","在研（已建本）");
        List<String> levelList = getProjectLevelDicTypeNameList("_1");
        List<String> statusList = getProjectStatusDicTypeNameList("_1");

        ResearchProjectEOPage researchProjectEOPage = new ResearchProjectEOPage();
        researchProjectEOPage.setYear(String.valueOf(currentYear));
        List<ResearchProjectEO> researchProjectEOList = researchProjectEODao.queryByList(researchProjectEOPage);
        Map<String,List<String>> orgNameResProIdListMap = new HashMap<>();

        Set<String> orgNameSet = new HashSet<>();
        for (ResearchProjectEO researchProjectEO : researchProjectEOList) {
            if (!levelList.contains(researchProjectEO.getProjectLevelDicName())){
                continue;
            }
            if (!statusList.contains(researchProjectEO.getProjectStatusDicName())){
                continue;
            }
            if (StringUtils.isNotEmpty(researchProjectEO.getChargeOrgName())) {
                orgNameSet.add(researchProjectEO.getChargeOrgName());
                List<String> proResIdList = orgNameResProIdListMap.get(researchProjectEO.getChargeOrgName());
                if (CollectionUtils.isEmpty(proResIdList)){
                    proResIdList = new ArrayList<>();
                }
                if (!proResIdList.contains(researchProjectEO.getResearchProjectId())) {
                    proResIdList.add(researchProjectEO.getResearchProjectId());
                }
                orgNameResProIdListMap.put(researchProjectEO.getChargeOrgName(),proResIdList);
            }
        }

        List<ResearchProjectSortVO> tempSortDataList = new ArrayList<>();
        for (String orgName : orgNameSet){
            BigDecimal useFunds = BigDecimal.ZERO;
            BigDecimal allFunds = BigDecimal.ZERO;
            List<String> proResIdList = orgNameResProIdListMap.get(orgName);
            List<ResProFeeDetailEO> resProFeeDetailEOList = resProFeeDetailEODao.sumByResearchProjectIdListGroupByFundsType(proResIdList);
            for (ResProFeeDetailEO resProFeeDetailEO : resProFeeDetailEOList){
                if (type == 1) {
                    if (StringUtils.equals(resProFeeDetailEO.getFundsType(), "国拨费用（使用）")) {
                        useFunds = useFunds.add(doSumResProFeeDetailEO(resProFeeDetailEO));
                    }

                }else if (type == 2){
                    if (StringUtils.equals(resProFeeDetailEO.getFundsType(), "自筹费用（使用）")) {
                        useFunds = useFunds.add(doSumResProFeeDetailEO(resProFeeDetailEO));
                    }

                }else if (type ==3){
                    useFunds = useFunds.add(doSumResProFeeDetailEO(resProFeeDetailEO));
                }
            }
            if (type == 1){
                allFunds = researchProjectEODao.sumNationFundsByResearchProjectIdList(proResIdList);
            }else if(type == 2){
                allFunds = researchProjectEODao.sumSelfFundsByResearchProjectIdList(proResIdList);
            }else if (type == 3){
                //allFunds= researchProjectEODao.sumAllFundsByResearchProjectIdList(proResIdList);
                BigDecimal a1 = researchProjectEODao.sumNationFundsByResearchProjectIdList(proResIdList);
                BigDecimal a2  = researchProjectEODao.sumSelfFundsByResearchProjectIdList(proResIdList);
                allFunds = doAdd(a1,a2);
            }
            ResearchProjectSortVO tmp = new ResearchProjectSortVO();
            tmp.setOrgName(orgName);
            int all = allFunds==null?0:allFunds.setScale(0,BigDecimal.ROUND_HALF_UP).intValue();
            tmp.setAllFunds(all);
            int use = useFunds==null?0:useFunds.setScale(0,BigDecimal.ROUND_HALF_UP).intValue();
            tmp.setUseFunds(use);
            tmp.setUnUseFunds(all-use);
            tempSortDataList.add(tmp);
        }
        Collections.sort(tempSortDataList);

        for (ResearchProjectSortVO vo : tempSortDataList){
            indexList.add(vo.getOrgName());
            budgetDataList.add(vo.getUnUseFunds());
            useDataList.add(vo.getUseFunds());
        }
        dashBoardBarVO.setXIndexList(indexList);
        dashBoardBarVO.setUseDataList(useDataList);
        dashBoardBarVO.setBudgetDataList(budgetDataList);
        return dashBoardBarVO;
    }

    /**
     * 各部门科研经费申请分布
     * @param
     * @param
     */
    public List<List<Object>> getOrgFundsDistribution(int type){
        List<List<Object>> resultList= new ArrayList<>();
        DateTime dateTime = new DateTime();
        int currentYear = dateTime.getYear();

//        List<String> levelList = Arrays.asList("国家级","省部级");
//        List<String> statusList = Arrays.asList("中标未公示","公示未签任务书","在研（未建本）","在研（已建本）");
        List<String> levelList = getProjectLevelDicTypeNameList("_1");
        List<String> statusList = getProjectStatusDicTypeNameList("_1");

        ResearchProjectEOPage researchProjectEOPage = new ResearchProjectEOPage();
        researchProjectEOPage.setYear(String.valueOf(currentYear));
        List<ResearchProjectEO> researchProjectEOList = researchProjectEODao.queryByList(researchProjectEOPage);
        Map<String,List<String>> orgNameProIdListMap = new HashMap<>();
        Map<String,List<String>> orgNameResProNoListMap = new HashMap<>();

        Set<String> orgNameSet = new HashSet<>();
        for (ResearchProjectEO researchProjectEO : researchProjectEOList) {
            if (!levelList.contains(researchProjectEO.getProjectLevelDicName())){
                continue;
            }
            if (!statusList.contains(researchProjectEO.getProjectStatusDicName())){
                continue;
            }
            if (StringUtils.isNotEmpty(researchProjectEO.getChargeOrgName())) {
                orgNameSet.add(researchProjectEO.getChargeOrgName());
                List<String> proIdList = orgNameProIdListMap.get(researchProjectEO.getChargeOrgName());
                if (CollectionUtils.isEmpty(proIdList)){
                    proIdList = new ArrayList<>();
                }
                if (!proIdList.contains(researchProjectEO.getId())) {
                    proIdList.add(researchProjectEO.getId());
                }
                orgNameProIdListMap.put(researchProjectEO.getChargeOrgName(),proIdList);


                List<String> proResNoList = orgNameResProNoListMap.get(researchProjectEO.getChargeOrgName());
                if (CollectionUtils.isEmpty(proResNoList)){
                    proResNoList = new ArrayList<>();
                }
                if (!proResNoList.contains(researchProjectEO.getProjectNo())) {
                    proResNoList.add(researchProjectEO.getProjectNo());
                }
                orgNameResProNoListMap.put(researchProjectEO.getChargeOrgName(),proResNoList);
            }
        }

        for (String orgName : orgNameSet){
            List<String> proIdList = orgNameProIdListMap.get(orgName);
//            List<ResProFeeDetailEO> resProFeeDetailEOList = resProFeeDetailEODao.sumByResearchProjectIdListGroupByFundsType(proResIdList);
            List<String> proNoList = orgNameResProNoListMap.get(orgName);
            Set<String> proNOSet = new HashSet<>(proNoList);

//            for (ResProFeeDetailEO sresProFeeDetailEO : resProFeeDetailEOList){
                if (type == 1) {
//                    if (StringUtils.equals(resProFeeDetailEO.getFundsType(), "国拨费用（使用）")) {
                        BigDecimal nationFunds = researchProjectEODao.sumNationFundsByIdList(proIdList);
                        List<Object> list = new ArrayList<>();
                        list.add(proNOSet.size());
                        list.add(nationFunds.setScale(0,BigDecimal.ROUND_HALF_UP).intValue());
                        list.add(orgName);
                        resultList.add(list);
//                        break;
//                    }
                }else if (type == 2){
//                    if (StringUtils.equals(resProFeeDetailEO.getFundsType(), "自筹费用（使用）")) {
                        BigDecimal selfFunds = researchProjectEODao.sumSelfFundsByIdList(proIdList);
                        List<Object> list = new ArrayList<>();
                        list.add(proNOSet.size());
                        list.add(selfFunds.setScale(0,BigDecimal.ROUND_HALF_UP).intValue());
                        list.add(orgName);
                        resultList.add(list);
//                        break;
//                    }
                }else if (type ==3){
                    BigDecimal allFunds = researchProjectEODao.sumAllFundsByIdList(proIdList);
                    List<Object> list = new ArrayList<>();
                    list.add(proNOSet.size());
                    list.add(allFunds.setScale(0,BigDecimal.ROUND_HALF_UP).intValue());
                    list.add(orgName);
                    resultList.add(list);
//                    break;
                }
//            }
        }
        return resultList;
    }

    /**
     * 重点课题排名
     * @param type
     * @return
     */

    public DashBoardBarVO getTop5(int type) {
        DashBoardBarVO dashBoardBarVO = new DashBoardBarVO();
        DateTime dateTime = new DateTime();
        int currentYear = dateTime.getYear();
//        List<String> levelList = Arrays.asList("国家级", "省部级");
//        List<String> statusList = Arrays.asList("中标未公示","公示未签任务书","在研（未建本）","在研（已建本）");
        List<String> levelList = getProjectLevelDicTypeNameList("_1");
        List<String> statusList = getProjectStatusDicTypeNameList("_1");
        DashBoardBarVO dashBoardVO = new DashBoardBarVO();
        List<String> indexList = dashBoardVO.getXIndexList();
        List<Integer> dataList = dashBoardBarVO.getUseDataList();
        ResearchProjectEOPage researchProjectEOPage = new ResearchProjectEOPage();
        researchProjectEOPage.setYear(String.valueOf(currentYear));
        List<ResearchProjectEO> researchProjectEOList = researchProjectEODao.queryByList(researchProjectEOPage);
        List<String> projectIdList = new ArrayList<>();
        for (ResearchProjectEO researchProjectEO : researchProjectEOList) {
            if (!levelList.contains(researchProjectEO.getProjectLevelDicName())){
                continue;
            }
            if (!statusList.contains(researchProjectEO.getProjectStatusDicName())){
                continue;
            }
            if (!projectIdList.contains(researchProjectEO.getId())) {
                projectIdList.add(researchProjectEO.getId());
            }
        }
        if(CollectionUtils.isEmpty(projectIdList)){
            return dashBoardBarVO;
        }
        List<ResearchProjectEO> projectEOList = researchProjectEODao.queryByIdList(projectIdList);
        Map<String,String> proNOAndNameMap = getProNOAndNameMap( projectEOList);

        if (type == 1){
            List<ResearchProjectEO> researchProjectEOS = researchProjectEODao.queryNationFundsTop5(projectIdList);
            if (CollectionUtils.isEmpty(researchProjectEOS)){
                return dashBoardBarVO;
            }
            for (int i = 0 ; i < researchProjectEOS.size() && i < 5 ; i ++ ){
                ResearchProjectEO researchProjectEO  = researchProjectEOS.get(i);
//                indexList.add(researchProjectEO.getProjectName());
                indexList.add(proNOAndNameMap.get(researchProjectEO.getProjectNo()));
                dataList.add(researchProjectEO.getAllocateFunds()==null?0:researchProjectEO.getAllocateFunds().setScale(0,BigDecimal.ROUND_HALF_UP).intValue());
            }
        }else if (type == 2){
            List<ResearchProjectEO> researchProjectEOS = researchProjectEODao.querySelfFundsTop5(projectIdList);
            if (CollectionUtils.isEmpty(researchProjectEOS)){
                return dashBoardBarVO;
            }
            for (int i = 0 ; i < researchProjectEOS.size() && i < 5 ; i ++ ){
                ResearchProjectEO researchProjectEO  = researchProjectEOS.get(i);
//                indexList.add(researchProjectEO.getProjectName());
                indexList.add(proNOAndNameMap.get(researchProjectEO.getProjectNo()));
                dataList.add(researchProjectEO.getRaiseFunds()==null?0:researchProjectEO.getRaiseFunds().setScale(0,BigDecimal.ROUND_HALF_UP).intValue());
            }

        }else if (type == 3){

            List<ResearchProjectEO> researchProjectEOS = researchProjectEODao.queryAllFundsTop5(projectIdList);
            if (CollectionUtils.isEmpty(researchProjectEOS)){
                return dashBoardBarVO;
            }
            for (int i = 0 ; i < researchProjectEOS.size() && i < 5 ; i ++ ){
                ResearchProjectEO researchProjectEO  = researchProjectEOS.get(i);
                indexList.add(proNOAndNameMap.get(researchProjectEO.getProjectNo()));
                dataList.add(researchProjectEO.getAllFunds()==null?0:researchProjectEO.getAllFunds().setScale(0,BigDecimal.ROUND_HALF_UP).intValue());
            }
        }
        dashBoardBarVO.setXIndexList(indexList);
        dashBoardBarVO.setUseDataList(dataList);
        return dashBoardBarVO;
    }

    private Map<String,String> getProNOAndNameMap( List<ResearchProjectEO> projectEOList){
        HashMap<String,String> map = new HashMap<>();
        for (ResearchProjectEO researchProjectEO : projectEOList){
            map.put(researchProjectEO.getProjectNo(),researchProjectEO.getProjectName());
        }
        return map;
    }


    /**
     * 科研经费历史趋势
     * @param type
     * @return
     */
    public DashBoardBarVO getFundsHistoryTrend(int type) {
        DashBoardBarVO dashBoardBarVO = new DashBoardBarVO();
        DateTime dateTime = new DateTime();
        int currentYear = dateTime.getYear();
        List<String> levelList = getProjectLevelDicTypeNameList("_1");
        List<String> statusList = getProjectStatusDicTypeNameList("_1");
        statusList.addAll(getProjectStatusDicTypeNameList("_2"));
//        List<String> levelList = Arrays.asList("国家级", "省部级");
//        List<String> statusList = Arrays.asList("中标未公示","公示未签任务书","在研（未建本）","在研（已建本）","结项未答辩","已结项");
        DashBoardBarVO dashBoardVO = new DashBoardBarVO();
        List<String> indexList = dashBoardVO.getXIndexList();
        List<Integer> dataList = dashBoardBarVO.getUseDataList();
        ResearchProjectEOPage researchProjectEOPage = new ResearchProjectEOPage();
        researchProjectEOPage.setYearOperator(">=");
        researchProjectEOPage.setYear(String.valueOf(currentYear-4));
        List<ResearchProjectEO> researchProjectEOList = researchProjectEODao.queryByList(researchProjectEOPage);

        for (int y = currentYear-4 ; y <= currentYear; y++) {
            BigDecimal funds = BigDecimal.ZERO;
            for (ResearchProjectEO researchProjectEO : researchProjectEOList) {
                if (!levelList.contains(researchProjectEO.getProjectLevelDicName())) {
                    continue;
                }
                if (!statusList.contains(researchProjectEO.getProjectStatusDicName())) {
                    continue;
                }
                if (!StringUtils.equals(String.valueOf(y),researchProjectEO.getYear())) {
                    continue;
                }
                if (type == 1) {
                    funds = funds.add(researchProjectEO.getAllocateFunds());
                } else if (type == 2) {
                    funds = funds.add(researchProjectEO.getRaiseFunds());
                } else if (type == 3) {
                    funds = funds.add(researchProjectEO.getAllFunds());
                }

            }
            indexList.add(String.valueOf(y));
            dataList.add(funds.setScale(0,BigDecimal.ROUND_HALF_UP).intValue());
        }
        dashBoardBarVO.setXIndexList(indexList);
        dashBoardBarVO.setUseDataList(dataList);
        return dashBoardBarVO;
    }

    /**
     * 课题预警
     */
    public void alter(){

    }



    /**
     *课题申请数量历史趋势
     * @param type
     */
    public  DashBoardBarVO getProjectHistoryTrend(int type) {
        DashBoardBarVO dashBoardBarVO = new DashBoardBarVO();
        DateTime dateTime = new DateTime();
        int currentYear = dateTime.getYear();
//        List<String> levelList = Arrays.asList("国家级", "省部级");
//        List<String> statusList = Arrays.asList("中标未公示", "公示未签任务书", "在研（未建本）", "在研（已建本）", "结项未答辩", "已结项");
        List<String> levelList = getProjectLevelDicTypeNameList("_1");
        List<String> statusList = getProjectStatusDicTypeNameList("_1");
        statusList.addAll(getProjectStatusDicTypeNameList("_2"));
        DashBoardBarVO dashBoardVO = new DashBoardBarVO();
        List<String> indexList = dashBoardVO.getXIndexList();
        List<Integer> dataList = dashBoardBarVO.getUseDataList();
        ResearchProjectEOPage researchProjectEOPage = new ResearchProjectEOPage();
        researchProjectEOPage.setYearOperator(">=");
        researchProjectEOPage.setYear(String.valueOf(currentYear - 4));
        List<ResearchProjectEO> yearProjectEOList = researchProjectEODao.queryByList(researchProjectEOPage);



        for (int y = currentYear-4 ; y <= currentYear; y++) {
            Set<String> proNOSet = new HashSet<>();
            int projectCount = 0 ;
            for (ResearchProjectEO researchProjectEO : yearProjectEOList) {
                if (!levelList.contains(researchProjectEO.getProjectLevelDicName())) {
                    continue;
                }
                if (!statusList.contains(researchProjectEO.getProjectStatusDicName())) {
                    continue;
                }
                if (!StringUtils.equals(String.valueOf(y),researchProjectEO.getYear())) {
                    continue;
                }
                if (proNOSet.add(researchProjectEO.getProjectNo())) {
                    projectCount++;
                }
            }
            indexList.add(String.valueOf(y));
            dataList.add(projectCount);
        }

        dashBoardBarVO.setXIndexList(indexList);
        dashBoardBarVO.setUseDataList(dataList);
        return dashBoardBarVO;
    }



    public Map<String,Object> createMap(String name,BigDecimal value){
        if (value == null){
            value =BigDecimal.ZERO;
        }
        Map<String,Object> hashMap = new HashMap<>();
        hashMap.put("name",name);
        hashMap.put("value",value.setScale(0,BigDecimal.ROUND_HALF_UP).intValue());
        return hashMap;
    }

    public Map<String,Object> createMap(String name,BigDecimal value,BigDecimal all){
        if (value == null){
            value =BigDecimal.ZERO;
        }
        Map<String,Object> hashMap = new HashMap<>();
        hashMap.put("name",name);
        hashMap.put("value",value.setScale(0,BigDecimal.ROUND_HALF_UP).intValue());
        hashMap.put("percent",value.divide(all,2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)).intValue());
        return hashMap;
    }


    public void addData2Map(ResProFeeDetailEO resProFeeDetailEO,List<Map<String,Object>> mapList){
        BigDecimal sum  = doSumResProFeeDetailEO(resProFeeDetailEO);
        if (resProFeeDetailEO == null){
            mapList.add(createMap("暂无数据",BigDecimal.ZERO,sum));
        }
        if (null != resProFeeDetailEO.getLaborFee()) {
            mapList.add(createMap("劳务费", resProFeeDetailEO.getLaborFee(),sum));
        }
        if (null != resProFeeDetailEO.getInternationExchangeFee()) {
            mapList.add(createMap("交流费", resProFeeDetailEO.getInternationExchangeFee(),sum));
        }
        if (null != resProFeeDetailEO.getMeetingFee()) {
            mapList.add(createMap("会议费", resProFeeDetailEO.getMeetingFee(),sum));
        }
        if (null != resProFeeDetailEO.getTravelFee()) {
            mapList.add(createMap("差旅费", resProFeeDetailEO.getTravelFee(),sum));
        }
        if (null != resProFeeDetailEO.getFuelPowerFee()) {
            mapList.add(createMap("燃料动力费", resProFeeDetailEO.getFuelPowerFee(),sum));
        }
        BigDecimal a1 = resProFeeDetailEO.getkSoftFee()==null?BigDecimal.ZERO:resProFeeDetailEO.getkSoftFee();
        BigDecimal a2 = resProFeeDetailEO.getkOtherFee()==null?BigDecimal.ZERO:resProFeeDetailEO.getkOtherFee();
        BigDecimal res = a1.add(a2);
        if(res.compareTo(BigDecimal.ZERO)>0) {
            mapList.add(createMap("出版/文献/信息传播/知识产权事务费", a1.add(a2),sum));
        }

        if (null != resProFeeDetailEO.getMaterialFee()) {
            mapList.add(createMap("材料费", resProFeeDetailEO.getMaterialFee(),sum));
        }
        if (null != resProFeeDetailEO.getTestProcessFee()) {
            mapList.add(createMap("加工费", resProFeeDetailEO.getTestProcessFee(),sum));
        }
        if (null != resProFeeDetailEO.getExpertConsultFee()) {
            mapList.add(createMap("专家咨询费", resProFeeDetailEO.getExpertConsultFee(),sum));
        }
        if (null != resProFeeDetailEO.getExternalAssistFee()) {
            mapList.add(createMap("外协费", resProFeeDetailEO.getExternalAssistFee(),sum));
        }
        if (null != resProFeeDetailEO.getPersonFee()) {
            mapList.add(createMap("人员费", resProFeeDetailEO.getPersonFee(),sum));
        }
        if (null != resProFeeDetailEO.getManageFee()) {
            mapList.add(createMap("管理费", resProFeeDetailEO.getManageFee(),sum));
        }
        BigDecimal b1 = resProFeeDetailEO.getEquipPurchaseFee()==null?BigDecimal.ZERO:resProFeeDetailEO.getEquipPurchaseFee();
        BigDecimal b2 = resProFeeDetailEO.getEquipRentFee()==null?BigDecimal.ZERO:resProFeeDetailEO.getEquipRentFee();
        BigDecimal b3 = resProFeeDetailEO.getEquipTestCreateFee()==null?BigDecimal.ZERO:resProFeeDetailEO.getEquipTestCreateFee();
        BigDecimal result = b1.add(b2).add(b3);
        if(result.compareTo(BigDecimal.ZERO)>0) {
            mapList.add(createMap("设备费", result,sum));
        }


        if (null != resProFeeDetailEO.getOtherFee()) {
            mapList.add(createMap("其他费用", resProFeeDetailEO.getOtherFee(),sum));
        }

    }

    public BigDecimal doAdd(BigDecimal b1 , BigDecimal b2){
        if (null == b1){
            b1 = BigDecimal.ZERO;
        }
        if (null == b2){
            b2 = BigDecimal.ZERO;
        }
        return b1.add(b2);
    }



    private BigDecimal doSumResProFeeDetailEO(ResProFeeDetailEO resProFeeDetailEO){
        BigDecimal funds = BigDecimal.ZERO;
        if (null != resProFeeDetailEO.getEquipPurchaseFee()){
            funds = funds.add(resProFeeDetailEO.getEquipPurchaseFee());
        }
        if (null != resProFeeDetailEO.getEquipRentFee()){
            funds = funds.add(resProFeeDetailEO.getEquipRentFee());
        }
        if (null != resProFeeDetailEO.getEquipTestCreateFee()){
            funds = funds.add(resProFeeDetailEO.getEquipTestCreateFee());
        }
        if (null != resProFeeDetailEO.getMaterialFee()){
            funds = funds.add(resProFeeDetailEO.getMaterialFee());
        }
        if (null != resProFeeDetailEO.getTestProcessFee()){
            funds = funds.add(resProFeeDetailEO.getTestProcessFee());
        }
        if (null != resProFeeDetailEO.getFuelPowerFee()){
            funds = funds.add(resProFeeDetailEO.getFuelPowerFee());
        }
        if (null != resProFeeDetailEO.getTravelFee()){
            funds = funds.add(resProFeeDetailEO.getTravelFee());
        }
        if (null != resProFeeDetailEO.getMeetingFee()){
            funds = funds.add(resProFeeDetailEO.getMeetingFee());
        }
        if (null != resProFeeDetailEO.getInternationExchangeFee()){
            funds = funds.add(resProFeeDetailEO.getInternationExchangeFee());
        }
        if (null != resProFeeDetailEO.getkOtherFee()){
            funds = funds.add(resProFeeDetailEO.getkOtherFee());
        }
        if (null != resProFeeDetailEO.getkSoftFee()){
            funds = funds.add(resProFeeDetailEO.getkSoftFee());
        }
        if (null != resProFeeDetailEO.getLaborFee()){
            funds = funds.add(resProFeeDetailEO.getLaborFee());
        }
        if (null != resProFeeDetailEO.getExpertConsultFee()){
            funds = funds.add(resProFeeDetailEO.getExpertConsultFee());
        }
        if (null != resProFeeDetailEO.getExternalAssistFee()){
            funds = funds.add(resProFeeDetailEO.getExternalAssistFee());
        }
        if (null != resProFeeDetailEO.getPersonFee()){
            funds = funds.add(resProFeeDetailEO.getPersonFee());
        }
        if (null != resProFeeDetailEO.getManageFee()){
            funds = funds.add(resProFeeDetailEO.getManageFee());
        }
        if (null != resProFeeDetailEO.getOtherFee()){
            funds = funds.add(resProFeeDetailEO.getOtherFee());
        }

        return funds;
    }



    public static void main(String[] args) {
//        DateTime dateTime = new DateTime();
//        dateTime = dateTime.minusMonths(11);
//        int year = dateTime.getYear();
//        int month = dateTime.getMonthOfYear();
//        System.out.println(year+"--"+month);

        Set<String> hashSet = new HashSet<>();
        String s = "2";
        String s1 = new String("1")+"2";;
        String s2 = new String("12");
        String s3 = new String("12");
        hashSet.add(s1);
        hashSet.add(s2);
        hashSet.add(s3);
        System.out.println(hashSet.size());
    }



}
