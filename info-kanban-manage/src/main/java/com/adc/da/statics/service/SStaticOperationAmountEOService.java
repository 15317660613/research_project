package com.adc.da.statics.service;

import cn.hutool.core.bean.BeanUtil;
import com.adc.da.budget.entity.Project;
import com.adc.da.budget.repository.ProjectRepository;
import com.adc.da.dashboard.dao.ProvinceAreaEODao;
import com.adc.da.dashboard.entity.ProvinceAreaEO;
import com.adc.da.dashboard.vo.*;
import com.adc.da.industymeeting.dao.BudgetManagementInfoEODao;
import com.adc.da.industymeeting.dao.ReceivableIncomeEODao;
import com.adc.da.industymeeting.dao.ReceivableIncomeFiledEODao;
import com.adc.da.industymeeting.entity.BudgetManagementInfoEO;
import com.adc.da.login.util.UserUtils;
import com.adc.da.processform.dao.ProjectContractInvoiceEODao;
import com.adc.da.processform.entity.ProjectContractInvoiceEO;

import com.adc.da.statics.vo.*;
import com.adc.da.sys.dao.OrgEODao;
import com.adc.da.sys.entity.OrgEO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import org.joda.time.DateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.statics.dao.SStaticOperationAmountEODao;
import com.adc.da.statics.entity.SStaticOperationAmountEO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static java.math.BigDecimal.ZERO;


/**
 *
 * <br>
 * <b>功能：</b>S_STATIC_OPERATION_AMOUNT SStaticOperationAmountEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-09-04 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("sStaticOperationAmountEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class SStaticOperationAmountEOService extends BaseService<SStaticOperationAmountEO, String> {


    @Autowired
    private SStaticOperationAmountEODao dao;

    @Autowired
    ProjectContractInvoiceEODao projectContractInvoiceEODao;

    @Autowired
    ReceivableIncomeEODao receivableIncomeEODao;

    @Autowired
    ReceivableIncomeFiledEODao receivableIncomeFiledEODao;

    @Autowired
    OrgEODao orgEODao;

    @Autowired
    ProvinceAreaEODao provinceAreaEODao;

    @Autowired
    BudgetManagementInfoEODao budgetManagementInfoEODao;

    BigDecimal TEN_THOUSAND = BigDecimal.valueOf(10000);


    public SStaticOperationAmountEODao getDao() {
        return dao;
    }


    public int insertSelective(SStaticOperationAmountEO t) throws Exception {
        UserEO userEO = UserUtils.getUser();
        if (null == userEO) {
            throw new AdcDaBaseException("登陆过期，请从新登陆！");
        }

        t.setId(UUID.randomUUID10());

        t.setCreateTime(new Date());
        t.setCreateuserId(userEO.getUsid());
        t.setCreateuserName(userEO.getUsname());
        t.setDelFlag(0);


        return this.getDao().insertSelective(t);
    }


    public int updateByPrimaryKeySelective(SStaticOperationAmountEO t) throws Exception {
        UserEO userEO = UserUtils.getUser();
        if (null == userEO) {
            throw new AdcDaBaseException("登陆过期，请从新登陆！");
        }
        t.setUpdateTime(new Date());
        t.setUpdaterId(userEO.getUsid());
        t.setUpdaterName(userEO.getUsname());

        return this.getDao().updateByPrimaryKeySelective(t);
    }


    public ContractDashBoardBodyVO getContractDashBoardBodyVO(int type) {
        DateTime dateTime = new DateTime();
        Integer currentYear = dateTime.getYear();
        Integer lastYear = dateTime.minusYears(1).getYear();
        Integer currentMonth = dateTime.getMonthOfYear();

        List<StaticOperationAmountVO> list = dao.queryDashBoardBody(new StaticOperationAmountQueryVO(currentYear, lastYear, currentMonth, type));
        ContractDashBoardBodyVO contractDashBoardBodyVO = new ContractDashBoardBodyVO();
        // contractDashBoardBodyVO.setCurrentTimeDataList(list.stream().map(StaticOperationAmountVO::getAmount).collect(Collectors.toList()));
        List<Double> currentTimeDataList = contractDashBoardBodyVO.getCurrentTimeDataList(); //当前时间数值
        List<Double> lastTimeDataList = contractDashBoardBodyVO.getLastTimeDataList(); //同期时间数值
        List<Double> rateList = contractDashBoardBodyVO.getRateList(); //同比变化率
        for(StaticOperationAmountVO amountVO : list){
            currentTimeDataList.add(amountVO.getAmount());
            lastTimeDataList.add(amountVO.getLastAmount());
            rateList.add(amountVO.getRate());
        }
        return contractDashBoardBodyVO;


    }


    public Double getScale2(Double amount) {
        return BigDecimal.valueOf(amount).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    public ContractDashBoardHeaderVO getContractDashBoardHeaderVO(){

        SStaticOperationAmountEO query = new SStaticOperationAmountEO();
        DateTime dateTime = new DateTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime.toDate());
        query.setYear(dateTime.getYear()+"");
        query.setMonth(dateTime.getMonthOfYear()+"");

        ContractDashBoardHeaderVO contractDashBoardHeaderVO = dao.queryDashboardTop(query);
        StaticOperationDashBoardHeaderVO vv = dao.queryDashboardTopVo(query);
        if(contractDashBoardHeaderVO == null){
            contractDashBoardHeaderVO = new ContractDashBoardHeaderVO();
        }
        return contractDashBoardHeaderVO;
    }

    public OrgEODashBoardVO getOrgChartDataVO(int type) {
        OrgEODashBoardVO orgEODashBoardVO = new OrgEODashBoardVO();
        Set<String> actureOrgNameSet = new LinkedHashSet<>();

        List<Double> currentTimeDataList = orgEODashBoardVO.getCurrentTimeDataList(); //当前时间数值
        List<Double> lastTimeDataList = orgEODashBoardVO.getLastTimeDataList(); //同期时间数值
        List<Double> rateList = orgEODashBoardVO.getRateList();

        DateTime dateTime = new DateTime();
        int currentYear = dateTime.getYear();
        int lastYear = dateTime.minusYears(1).getYear();


        List<StaticOperationAmountChartVO> list =dao.queryChartDataYear(new StaticOperationAmountQueryVO(currentYear, lastYear, type));
        if(CollectionUtils.isNotEmpty(list)){
            for(StaticOperationAmountChartVO vo : list){
                currentTimeDataList.add(vo.getAmount());
                lastTimeDataList.add(vo.getLastAmount());
                rateList.add(vo.getRate());
                actureOrgNameSet.add(vo.getDepartmentName());
            }
        }
        orgEODashBoardVO.setOrgNameSet(actureOrgNameSet);
        return orgEODashBoardVO;

    }

    public AreaDashBoardVO getMapDataArea(int type){
        List<ProvinceAreaEO> provinceAreaEOList = provinceAreaEODao.queryAllByList();

        AreaDashBoardVO areaDashBoardVO = new AreaDashBoardVO();
        /*Set<String> areaSet = areaDashBoardVO.getAreaSet();
        areaSet = getAreaSet(provinceAreaEOList);*/
        Set<String> areaSet = new LinkedHashSet();

        List<Double> currentTimeDataList = areaDashBoardVO.getCurrentTimeDataList(); //当前时间数值
        List<Double> lastTimeDataList = areaDashBoardVO.getLastTimeDataList(); //同期时间数值
        List<Double> rateList = areaDashBoardVO.getRateList();

        DateTime dateTime = new DateTime();
        Integer currentYear = dateTime.getYear();
        Integer lastYear = dateTime.minusYears(1).getYear();
        Integer currentMonth = dateTime.getMonthOfYear();
        areaDashBoardVO.setCurrentYear(currentYear);
        areaDashBoardVO.setLastYear(lastYear);

        SStaticOperationAmountEO query = new SStaticOperationAmountEO();
        query.setDelFlag(0);
        query.setAmountType(type);
        query.setYear(String.valueOf(currentYear));
        query.setMonth(String.valueOf(currentMonth));
        List<StaticOperationAmountVO> projects = dao.findByProjectAmountAreaStatics(query);

        query.setYear(String.valueOf(lastYear));
        query.setMonth(String.valueOf(currentMonth));
        List<StaticOperationAmountVO> projectsLastYear = dao.findByProjectAmountAreaStatics(query);
        if(CollectionUtils.isNotEmpty(projects)){
            for(int i=0; i<projects.size(); i++){
                StaticOperationAmountVO vo = projects.get(i);
                StaticOperationAmountVO voLastYear = projectsLastYear.get(i);
                /*if(i > projectsLastYear.size()-1){
                    lastTimeDataList.add((double) 0);
                }else{
                    currentTimeDataList.add(vo.getAmount());
                }*/
                currentTimeDataList.add(vo.getAmount());
                lastTimeDataList.add(voLastYear.getAmount());
                rateList.add(vo.getRate());
                areaSet.add(vo.getArea());
            }
        }

        areaDashBoardVO.setAreaSet(areaSet);
        return areaDashBoardVO;
    }

    public AreaDashBoardVO getMapDataProvince(int type){
        AreaDashBoardVO areaDashBoardVO = new AreaDashBoardVO();
        List<Map<String, Object>> provinceDataMapList = areaDashBoardVO.getMapData();
        List<Double> currentTimeDataList = areaDashBoardVO.getCurrentTimeDataList(); //当前时间数值
        List<Double> rateList = areaDashBoardVO.getRateList();

        DateTime dateTime = new DateTime();
        Integer currentYear = dateTime.getYear();
        Integer lastYear = dateTime.minusYears(1).getYear();
        Integer currentMonth = dateTime.getMonthOfYear();
        areaDashBoardVO.setCurrentYear(currentYear);
        areaDashBoardVO.setLastYear(lastYear);

        List<StaticOperationAmountVO> projects = dao.findByProjectAmountStatics(new StaticOperationAmountQueryVO(currentYear, lastYear, currentMonth, type));
        if(CollectionUtils.isNotEmpty(projects)){
            for(int i=0; i<projects.size(); i++){
                StaticOperationAmountVO vo = projects.get(i);
                currentTimeDataList.add(vo.getAmount());
                rateList.add(vo.getRate());
                Map<String, Object> map = new HashMap<>();
                map.put("name", vo.getProvince());
                map.put("value", getScale2(vo.getAmount()));
                provinceDataMapList.add(map);
            }
        }
        return areaDashBoardVO;
    }

    @Deprecated
    public AreaDashBoardVO getMapData(int type) {
        AreaDashBoardVO areaDashBoardVO = new AreaDashBoardVO();
        List<Map<String, Object>> provinceDataMapList = areaDashBoardVO.getMapData();
        List<Double> currentTimeDataList = areaDashBoardVO.getCurrentTimeDataList(); //当前时间数值
        List<Double> rateList = areaDashBoardVO.getRateList();

        DateTime dateTime = new DateTime();
        Integer currentYear = dateTime.getYear();
        Integer lastYear = dateTime.minusYears(1).getYear();
        Integer currentMonth = dateTime.getMonthOfYear();
        areaDashBoardVO.setCurrentYear(currentYear);
        areaDashBoardVO.setLastYear(lastYear);

        List<StaticOperationAmountVO> projects = dao.findByProjectAmountStatics(new StaticOperationAmountQueryVO(currentYear, lastYear, currentMonth, type));
        if(CollectionUtils.isNotEmpty(projects)){
            for(int i=0; i<projects.size(); i++){
                StaticOperationAmountVO vo = projects.get(i);
                /*if(i > projectsLastYear.size()-1){
                    lastTimeDataList.add((double) 0);
                }else{
                    currentTimeDataList.add(vo.getAmount());
                }*/
                currentTimeDataList.add(vo.getAmount());
                // lastTimeDataList.add(vo.getLastAmount());
                rateList.add(vo.getRate());
                Map<String, Object> map = new HashMap<>();
                map.put("name", vo.getProvince());
                map.put("value", getScale2(vo.getAmount()));
                provinceDataMapList.add(map);
            }
        }
        return areaDashBoardVO;


    }


    public List<OrgContractInvoiceVO> getChartDataList() {
        DateTime dateTime = new DateTime();
        List<StaticOperationOrgContractInvoiceVO> list = dao.queryDeptChartDataList(new StaticOperationAmountQueryVO(dateTime.getYear(), null, dateTime.getMonthOfYear(), 240));
        List<OrgContractInvoiceVO> result = new LinkedList<>();
        for(StaticOperationOrgContractInvoiceVO vo : list){
            result.add(OrgContractInvoiceVO.builder()
                    .orgId(vo.getOrgId()) //组织机构ID
                    .orgName(vo.getOrgName())//部门名称
                    .isBigOrg(vo.isBigOrg())
                    .monthContractAmount(getScale2(vo.getMonthContractAmount()))//月开票额
                    .monthIncreaseRate(null) //月增长
                    .allContractAmount(getScale2(vo.getAllContractAmount()))//累计（全年）开票额
                    .finishedRate(vo.getFinishedRate())//完成率
                    .build());
        }
        return result;

    }



    public List<StaticOperationCompanyVO> getCompanyBusinessVO() {
        SStaticOperationAmountEO query = new SStaticOperationAmountEO();
        DateTime dateTime = new DateTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime.toDate());
        query.setYear(dateTime.getYear()+"");


        List<StaticOperationCompanyVO> staticOperationCompanyVO = dao.queryCompanyBusiness(query);
        return staticOperationCompanyVO;
    }

    public List<StaticOperationCompanyVO> getCompanyContractVO() {
        SStaticOperationAmountEO query = new SStaticOperationAmountEO();
        DateTime dateTime = new DateTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime.toDate());
        query.setYear(dateTime.getYear()+"");

        List<StaticOperationCompanyVO> staticOperationCompanyVO = dao.queryCompanyContract(query);
        return staticOperationCompanyVO;
    }

    public List<StaticOperationCompanyVO> getSalesVolumeVO() {
        SStaticOperationAmountEO query = new SStaticOperationAmountEO();
        DateTime dateTime = new DateTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime.toDate());
        query.setYear(dateTime.getYear()+"");
        query.setMonth(dateTime.getMonthOfYear()+"");

        List<StaticOperationCompanyVO> staticOperationCompanyVO = dao.querySalesVolumeVO(query);
        return staticOperationCompanyVO;
    }

    public List<StaticOperationCompanyVO> getDepartmentVO() {
        SStaticOperationAmountEO query = new SStaticOperationAmountEO();
        DateTime dateTime = new DateTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime.toDate());
        query.setYear(dateTime.getYear()+"");

        List<StaticOperationCompanyVO> staticOperationCompanyVO = dao.queryDepartmentVO(query);
        return staticOperationCompanyVO;
    }


    public List<StaticOperationCompanyVO> getCompanyVO() {
        SStaticOperationAmountEO query = new SStaticOperationAmountEO();
        DateTime dateTime = new DateTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime.toDate());
        query.setYear(dateTime.getYear()+"");

        List<StaticOperationCompanyVO> staticOperationCompanyVO = dao.queryCompanyVO(query);
        return staticOperationCompanyVO;
    }

    public void logicDeleteByPrimaryKey(List<String> idList) {
        dao.logicDeleteByPrimaryKeys(idList);
    }



}
