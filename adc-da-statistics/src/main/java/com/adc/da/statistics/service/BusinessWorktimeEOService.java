package com.adc.da.statistics.service;

import com.adc.da.base.service.BaseService;
import com.adc.da.budget.dao.OrgListDao;
import com.adc.da.budget.entity.AllBusinessStatistics;
import com.adc.da.budget.entity.FinOrgStatisticsEO;
import com.adc.da.budget.entity.OrgWithLevelEO;
import com.adc.da.budget.entity.Statistics;
import com.adc.da.budget.entity.StatisticsEntity;
import com.adc.da.budget.timertask.TimeType;
import com.adc.da.login.util.UserUtils;
import com.adc.da.statistics.dao.BusinessWorktimeEODao;
import com.adc.da.statistics.entity.BusinessWorktimeEO;
import com.adc.da.sys.dao.OrgEODao;
import com.adc.da.sys.dao.UserEODao;
import com.adc.da.sys.entity.DicTypeEO;
import com.adc.da.sys.entity.OrgEO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.sys.service.DicTypeEOService;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.DateUtils;
import com.adc.da.util.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <br>
 * <b>功能：</b>ST_BUSINESS_WORKTIME BusinessWorktimeEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-06-18 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("businessWorktimeEOService")
@Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class BusinessWorktimeEOService extends BaseService<BusinessWorktimeEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(BusinessWorktimeEOService.class);

    @Autowired
    private BusinessWorktimeEODao dao;

    @Autowired
    private OrgListDao orgListDao;

    @Autowired
    private OrgEODao orgEODao;
    @Autowired
    private UserEODao userEODao ;
    @Autowired
    private DicTypeEOService dicTypeEOService;

    public BusinessWorktimeEODao getDao() {
        return dao;
    }

    public List<BusinessWorktimeEO> getManDayByMonth(String id, String year) {
        return dao.getManDayByMonth(id, year);
    }

    /**
     * 回显工时信息 ， 入参为 时间为String
     * @param orgIds
     * @param beginTime
     * @param endTime
     * @return
     * @see #getOrgWorkTime(String, Date, Date,String)
     */
    public FinOrgStatisticsEO getOrgWorkTime(String orgIds, String beginTime, String endTime,String requestType) {
        Date startTime = null ;
        Date finishTime = null ;

        if (StringUtils.equals("year",beginTime)){
            startTime = DateUtils.getCurrentYearStartTime();
            finishTime=DateUtils.getCurrentYearEndTime();

        }else if (StringUtils.equals("season",beginTime)){
            startTime = DateUtils.getCurrentSeasonStartTime();
            finishTime =DateUtils.getCurrentSeasonEndTime();

        }else if (StringUtils.equals("month",beginTime)){
            startTime = DateUtils.getTimesMonthStartTime();
            finishTime =DateUtils.getTimesMonthEndTime();

        }else if (StringUtils.equals("week",beginTime)){
            startTime = DateUtils.getTimesWeekStartTime();
            finishTime =DateUtils.getTimesWeekEndTime();

        }else {
             startTime = DateUtils.stringToDate(beginTime, DateUtils.YYYY_MM_DD_EN);
             finishTime = DateUtils.stringToDate(endTime, DateUtils.YYYY_MM_DD_EN);
        }

        return getOrgWorkTime(orgIds, startTime, finishTime,requestType);
    }





    /**
     * 工时统计页面，以及首页饼图
     *
     * @param orgIds
     * @param startTime
     * @param finishTime
     * @return
     */
    public FinOrgStatisticsEO getOrgWorkTime(String orgIds, Date startTime, Date finishTime,String requestType) {
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        FinOrgStatisticsEO finOrgStatisticsEO = new FinOrgStatisticsEO();
        String orgIdSearch;
        //判断多组织情况
        if (StringUtils.isNotEmpty(orgIds) && orgIds.indexOf(',') != -1) {
            String[] orgId = orgIds.split(",");
            OrgEO orgEO = orgEODao.getOrgEOById(orgId[0]);
            if (null != orgEO && orgEO.getParentIds().contains(orgId[1])) {
                orgIdSearch = orgId[1];
            } else {
                orgIdSearch = orgId[0];
            }
        } else {
            orgIdSearch = orgIds;
        }

        boolean fake_benbu = false;
        List<String> fakeOrgIdList = new ArrayList<>();
        // 先从字典找一圈
        List<DicTypeEO> dicTypeEOList = dicTypeEOService.queryByList("FAKE_BENBU");
        if(CollectionUtils.isNotEmpty(dicTypeEOList)) {
            for (DicTypeEO dicTypeEO : dicTypeEOList) {
                if (StringUtils.contains(dicTypeEO.getDicTypeName(), userId)) {
                    String orgId = dicTypeEO.getDicTypeCode();
                    fakeOrgIdList.add(orgId);
                    fake_benbu = true;
                }
            }
        }

        OrgEO orgEO = orgEODao.getOrgEOById(orgIdSearch);
        String orgRole = "";
        String orgType = "";
        double allWorkTime = 0.0;
        List<StatisticsEntity> statisticsEntityList = new ArrayList<>();
        //判断是否是数据中心直属部门
        List<OrgEO> adcOrgEO = orgEODao.getOrgEOByOrgCode("1018");
        if (StringUtils.equals(orgEO.getId(), adcOrgEO.get(0).getId())||(fake_benbu && StringUtils.equals(requestType,"bigDept"))) { // 新增 用于处理兼任其他本部领导的问题
            orgRole = "数据中心领导";
            orgType = "本部";
            List<OrgEO> orgEOList = new ArrayList<>();
            //获取数据中心下本部ID
            if (!fake_benbu) {
                orgEOList = orgListDao.queryByParentId(orgEO.getId());
            }
            if (CollectionUtils.isNotEmpty(fakeOrgIdList)) {
                List<OrgEO> fakeOrgEOList = orgListDao.selectOrgEOByIdList(fakeOrgIdList);
                orgEOList.addAll(fakeOrgEOList);
                orgEOList.add(orgEO);
            }
            if (CollectionUtils.isNotEmpty(orgEOList)) {
                allWorkTime = setStatisticsEntityList(statisticsEntityList, orgEOList, startTime, finishTime);
            }
        } else if (!StringUtils.equals(orgEO.getParentId(), adcOrgEO.get(0).getId())) {
            orgRole = "部长";
            orgType = "项目组";
            List<OrgEO> orgEOList = orgListDao.queryByParentId(orgEO.getId());
            if (CollectionUtils.isNotEmpty(orgEOList)) {
                allWorkTime = setStatisticsEntityList(statisticsEntityList, orgEOList, startTime, finishTime);
            } else {
                orgEOList.add(orgEO);
                allWorkTime = setStatisticsEntityList(statisticsEntityList, orgEOList, startTime, finishTime);
            }
        } else if (StringUtils.equals(orgEO.getParentId(), adcOrgEO.get(0).getId())) {
            orgRole = "本部长";
            orgType = "部门";
            List<OrgEO> orgEOList = orgListDao.queryByParentId(orgEO.getId());
//            if (!orgEO.getName().contains("本部")) {
            if (!StringUtils.equals(orgEO.getLayer(),"3")||StringUtils.equals(orgEO.getOrgCode(),"101801")) {  // 列出所有部门，如果当前部门不是本部，如果就把当前部门扔进去
                orgEOList.add(orgEO);
            }
            if (CollectionUtils.isNotEmpty(orgEOList)) {
                allWorkTime = setStatisticsEntityList(statisticsEntityList, orgEOList, startTime, finishTime);
            }
        }

        finOrgStatisticsEO.setRole(orgRole);
        finOrgStatisticsEO.setOrgType(orgType);
        StatisticsEntity statistics = dao.getWorkTimeByOrgIds(orgListDao.getAllOrgIdByPid(orgIdSearch), startTime, finishTime).get(0);
        if (null != statistics && CollectionUtils.isNotEmpty(statisticsEntityList)) {
            if ("项目组".equals(orgType)) {
                if (statistics.getWorkTime() > allWorkTime) {
                    StatisticsEntity other = new StatisticsEntity();
                    other.setOrgName("其他");
                    other.setWorkTime(statistics.getWorkTime() - allWorkTime);
                    other.setPerAveInput("--");
                    statisticsEntityList.add(other);
                    allWorkTime = statistics.getWorkTime();
                }
            } else if ("部门".equals(orgType)) {
                if (statistics.getWorkTime() > allWorkTime) {
                    allWorkTime = statistics.getWorkTime();
                }
            }
        }
        BigDecimal bg = BigDecimal.valueOf(allWorkTime).setScale(2, RoundingMode.HALF_EVEN);
        finOrgStatisticsEO.setAllWorkTime(bg.doubleValue());
        for (StatisticsEntity statisticsEntity : statisticsEntityList) {
            BigDecimal allWorkTime1 = BigDecimal.valueOf(allWorkTime);
            BigDecimal workTime = BigDecimal.valueOf(statisticsEntity.getWorkTime());
            int r = allWorkTime1.compareTo(BigDecimal.ZERO);
            if (r != 0) {
                BigDecimal percent = workTime.divide(allWorkTime1, 4, BigDecimal.ROUND_HALF_EVEN);
                statisticsEntity.setPercent(String.valueOf(percent.multiply(BigDecimal.valueOf(100))));
            }
        }
        finOrgStatisticsEO.setOrgList(statisticsEntityList);
        return finOrgStatisticsEO;
    }

    public Double setStatisticsEntityList(List<StatisticsEntity> statisticsEntityList,
        List<OrgEO> orgEOList, Date startTime, Date finishTime) {
        Double allWorkTime = 0.0D;
        if (CollectionUtils.isNotEmpty(orgEOList)) {
            for (OrgEO orgEO1 : orgEOList) {
                List<String> childOrgIds = orgListDao.getAllOrgIdByPid(orgEO1.getId());
                if (CollectionUtils.isNotEmpty(childOrgIds)) {
                    List<UserEO> userEOList = userEODao.getAllUserEOsByOrgIdsNotTrainee(childOrgIds);
                    StatisticsEntity statisticsEntity = new StatisticsEntity();
                    List<StatisticsEntity> statisticsEntities = dao.getWorkTimeByOrgIds(childOrgIds, startTime, finishTime);
                    if (CollectionUtils.isNotEmpty(statisticsEntities) && null != statisticsEntities.get(0)) {
                        allWorkTime = allWorkTime + statisticsEntities.get(0).getWorkTime();
                        statisticsEntity.setWorkTime(statisticsEntities.get(0).getWorkTime());
                        statisticsEntity.setOrgId(orgEO1.getId());
                        statisticsEntity.setOrgName(orgEO1.getName());
                        if (CollectionUtils.isNotEmpty(userEOList)){
                            BigDecimal memberCount = new BigDecimal(userEOList.size());
                            statisticsEntity.setMemberCount(memberCount.intValue());
                            BigDecimal workTime = BigDecimal.valueOf(statisticsEntities.get(0).getWorkTime());
                            String  result =  workTime.divide(memberCount,2,RoundingMode.HALF_UP).toString();
                            statisticsEntity.setPerAveInput(result);
                        }else {
                            statisticsEntity.setPerAveInput("--"); // 人均投入工时
                        }
                        statisticsEntityList.add(statisticsEntity);
                    }
                }
            }
        }
        return allWorkTime;
    }

//    /**
//     * code code by dingqiang
//     * @param statisticsEntityList
//     * @param orgEOList
//     * @param startTime
//     * @param finishTime
//     * @return
//     */
//
//    public Double newSetStatisticsEntityList(List<StatisticsEntity> statisticsEntityList,
//            List<OrgEO> orgEOList, Date startTime, Date finishTime) {
//        Double allWorkTime = 0.0D;
//        if (CollectionUtils.isNotEmpty(orgEOList)) {
//            for (OrgEO orgEO1 : orgEOList) {
//                List<String> childOrgIds = orgListDao.getAllOrgIdByPid(orgEO1.getId());
//                if (CollectionUtils.isNotEmpty(childOrgIds)) {
//                    StatisticsEntity statisticsEntity = new StatisticsEntity();
//                    List<StatisticsEntity> statisticsEntities = dao.getWorkTimeByOrgIds(childOrgIds, startTime, finishTime);
//                    if (CollectionUtils.isNotEmpty(statisticsEntities) && null != statisticsEntities.get(0)) {
//                        allWorkTime = allWorkTime + statisticsEntities.get(0).getWorkTime();
//                        statisticsEntity.setWorkTime(statisticsEntities.get(0).getWorkTime());
//                        statisticsEntity.setOrgId(orgEO1.getId());
//                        statisticsEntity.setOrgName(orgEO1.getName());
//                        statisticsEntityList.add(statisticsEntity);
//                    }
//                }
//            }
//        }
//        return allWorkTime;
//    }



    public AllBusinessStatistics getBusWorkTime(String orgIds, String beginTime, String endTime) {
        Date startTime = null ;
        Date finishTime = null ;

        if (StringUtils.equals("year",beginTime)){
            startTime = DateUtils.getCurrentYearStartTime();
            finishTime=DateUtils.getCurrentYearEndTime();

        }else if (StringUtils.equals("season",beginTime)){
            startTime = DateUtils.getCurrentSeasonStartTime();
            finishTime =DateUtils.getCurrentSeasonEndTime();

        }else if (StringUtils.equals("month",beginTime)){
            startTime = DateUtils.getTimesMonthStartTime();
            finishTime =DateUtils.getTimesMonthEndTime();

        }else if (StringUtils.equals("week",beginTime)){
            startTime = DateUtils.getTimesWeekStartTime();
            finishTime =DateUtils.getTimesWeekEndTime();

        }else {
            startTime = DateUtils.stringToDate(beginTime, DateUtils.YYYY_MM_DD_EN);
            finishTime = DateUtils.stringToDate(endTime, DateUtils.YYYY_MM_DD_EN);
        }


        AllBusinessStatistics allBusinessStatistics = new AllBusinessStatistics();
        String orgIdSearch;
        //判断多组织情况
        if (StringUtils.isNotEmpty(orgIds) && orgIds.indexOf(',') != -1) {
            String[] orgId = orgIds.split(",");
            OrgEO orgEO = orgEODao.getOrgEOById(orgId[0]);
            if (null != orgEO && orgEO.getParentIds().contains(orgId[1])) {
                orgIdSearch = orgId[1];
            } else {
                orgIdSearch = orgId[0];
            }
        } else {
            orgIdSearch = orgIds;
        }

        double allWorkTime;
        List<Statistics> statisticsEntityList = new ArrayList<>();
        allWorkTime = setBusStatisticsEntityList(statisticsEntityList, orgIdSearch, startTime, finishTime);

        allBusinessStatistics.setBusinessAllWorkTime(allWorkTime);
        Collections.sort(statisticsEntityList);
        allBusinessStatistics.setBusinessStatistics(statisticsEntityList);
        return allBusinessStatistics;
    }

    public Double setBusStatisticsEntityList(List<Statistics> statisticsEntityList,
        String orgIdSearch, Date startTime, Date finishTime) {



        Double allWorkTime = 0.0;
        if (StringUtils.isNotEmpty(orgIdSearch)) {
            List<String> childOrgIds = orgListDao.getAllOrgIdByPid(orgIdSearch);
            if (CollectionUtils.isNotEmpty(childOrgIds)) {
                List<Statistics> statisticsList = dao.getBusWorkTimeByOrgIds(childOrgIds, startTime, finishTime);
                for (Statistics s : statisticsList) {
                    allWorkTime += s.getWorkTime();
                    statisticsEntityList.add(s);
                }
            }
        }
        return allWorkTime;
    }

    public List<Map<String, Double>> getWorkTimeByHQNew(String paramType) {
        List<Map<String, Double>> resultList = new ArrayList<>();
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }

        Date startDate;

        Calendar calendar = Calendar.getInstance();
        /*
         * 获取当前月   ，默认January是0
         */
        int currentMonth = calendar.get(Calendar.MONTH);

        /*
         * 设置时分秒
         */
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date endDate = calendar.getTime();
        switch (paramType) {
            case TimeType.YEAR:
                calendar.set(Calendar.DAY_OF_YEAR, 1);
                break;
            case TimeType.SEASON:
                /*
                 *  currentMonth 从0 开始计算
                 */
                if (currentMonth < 3) {
                    calendar.set(Calendar.MONTH, 0);
                } else if (currentMonth < 6) {
                    calendar.set(Calendar.MONTH, 3);
                } else if (currentMonth < 9) {
                    calendar.set(Calendar.MONTH, 6);
                } else if (currentMonth < 12) {
                    calendar.set(Calendar.MONTH, 9);
                } else {
                    throw new AdcDaBaseException("BusinessWorktimeEOService Month error ");
                }
                calendar.set(Calendar.DATE, 1);

                break;
            case TimeType.MONTH:
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                // 设置为1号,当前日期既为本月第一天
                break;

            case TimeType.WEEK:
                // 周一为 2
                calendar.set(Calendar.DAY_OF_WEEK, 2);
                break;
            default:
                throw new AdcDaBaseException("BusinessWorktimeEOService paramType error ");
        }
        startDate = calendar.getTime();
        List<OrgWithLevelEO> userOrgList = orgListDao.getUserOrgWhitLeafAndLev(userId);
        String orgId;
        if (CollectionUtils.isNotEmpty(userOrgList)) {
            orgId = userOrgList.get(userOrgList.size() - 1).getId();
        } else {
            throw new AdcDaBaseException("BusinessWorktimeEOService org is null");
        }

        List<StatisticsEntity> orgList = getOrgWorkTime(orgId, startDate, endDate,"").getOrgList();
        // 先从字典找一圈
        List<DicTypeEO> dicTypeEOList = dicTypeEOService.queryByList("FAKE_BENBU");
        if(CollectionUtils.isNotEmpty(dicTypeEOList)) {
            for (DicTypeEO dicTypeEO : dicTypeEOList) {
                if (StringUtils.contains(dicTypeEO.getDicTypeName(), userId)) {
                    List<StatisticsEntity>  fakeStatisticsEntities = getOrgWorkTime(dicTypeEO.getDicTypeCode(), startDate, endDate,"").getOrgList();
                    if (CollectionUtils.isNotEmpty(fakeStatisticsEntities)){
                        orgList.addAll(fakeStatisticsEntities);
                    }
                }
            }
        }


        Map<String, Double> resMap = new HashMap<>();

        if (CollectionUtils.isNotEmpty(orgList)) {
            for (StatisticsEntity temp : orgList) {
                resMap.put(temp.getOrgName(), temp.getWorkTime());
            }
        }
        resultList.add(resMap);
        return resultList;
    }
}
