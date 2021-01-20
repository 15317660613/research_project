package com.adc.da.budget.service;

import com.adc.da.budget.dao.OrgListDao;
import com.adc.da.budget.entity.*;
import com.adc.da.budget.repository.ChildTaskRepository;
import com.adc.da.budget.repository.ProjectRepository;
import com.adc.da.budget.repository.TaskRepository;
import com.adc.da.budget.vo.ReqStatisticsVO;
import com.adc.da.login.util.UserUtils;
import com.adc.da.sys.entity.OrgEO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.sys.service.UserEOService;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * add by liqiushi
 * 2019/03/15
 * 信息化系统 新饼状图各种维度工时统计
 */
@Service
public class PersonalWorkTimeService extends BaseService<Project, String> {
    private Logger logger = LoggerFactory.getLogger(PersonalWorkTimeService.class);

    @Autowired
    private UserEOService userEOService;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ChildTaskRepository childTaskRepository;

    @Autowired
    private OrgListDao orgDao;

    @Autowired
    private StaffScheduleService staffScheduleService;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BudgetEOService budgetEOService ;

    @Autowired
    private FinWorkTimeStatisticsService finWorkTimeStatisticsService;

    private final static  BigDecimal EIGHT = BigDecimal.valueOf(8);
    private final static  BigDecimal FOUR = BigDecimal.valueOf(4);

    /**
     * add by liqiushi 20190314
     * 以个人+项目为维度统计工时
     *
     * @param paramType 是前段传来的统计工时的单位
     */
    public List<Map<String, Double>> getWorkTimeByPersonInBudget(String paramType, String userId) {
        List<Map<String, Double>> resultList = new ArrayList<>();
        List<Long> times = getTimeWithStyle(paramType);
        if (StringUtils.isEmpty(userId)) {
            userId = UserUtils.getUserId();
        }
        try {
            //查询用户所有项目
//            List<Daily> dailyList = finWorkTimeStatisticsService
//                    .getStart2EndDailyList(userId, times.get(0), times.get(1),"");
            //结束日期改为当前日期
            List<Daily> dailyList = finWorkTimeStatisticsService
                    .getStart2EndDailyList(userId, times.get(0), new Date().getTime(), "");
            if (CollectionUtils.isNotEmpty(dailyList)) {
                Map<String, BigDecimal> budgetTimeMap = new HashMap<>();
                Map<String, BigDecimal> projectTimeMap = new HashMap<>();
                Map<String, BigDecimal> taskTimeMap = new HashMap<>();
                Set<String> taskIdSet = new HashSet<>();
                Set<String> projectIdSet = new HashSet<>();
                Map<String,String> budgetIdAndNameMap = new HashMap<>();



                for (Daily daily : dailyList) { //ES中 日报里的任务和子任务都存在taskIdArray中！
                    if (CollectionUtils.isNotEmpty(daily.getTaskIdArray())) {
                        BigDecimal arrayLength = BigDecimal.valueOf(daily.getTaskIdArray().length);
                        BigDecimal workCostTime = FOUR.divide(arrayLength, 8, RoundingMode.UP);
                        if (null != daily.getWorkCostTime()) {
                            workCostTime = BigDecimal.valueOf(daily.getWorkCostTime());
                        }
                        BigDecimal resDayTime = workCostTime.divide(EIGHT, 8, RoundingMode.UP);
                        for (String taskId : daily.getTaskIdArray()) {
                            BigDecimal tempTime = taskTimeMap.get(taskId);
                            if (null == tempTime) {
                                taskTimeMap.put(taskId, resDayTime);
                            }else {
                                taskTimeMap.put(taskId,resDayTime.add(tempTime));
                            }
                            taskIdSet.add(taskId);
                        }
                    }
                }
                //task 分为 业务下的task  项目下的task   子任务
                List<ChildrenTask> childrenTaskList = childTaskRepository.findByTaskIdIn(taskIdSet);
                //进行第一次向上汇总 将子任务的工时合计到任务
                for(ChildrenTask childrenTask : childrenTaskList){
                    BigDecimal childTaskDayTime = taskTimeMap.get(childrenTask.getId()); //把子任务工时找到
                    BigDecimal taskDayTime = taskTimeMap.get(childrenTask.getTaskId()); //把子任务的父任务工时找到
                    if(null == taskDayTime && null != childTaskDayTime) {
                        taskTimeMap.put(childrenTask.getTaskId(), childTaskDayTime); //如果父任务没有工时
                    }else if(null != taskDayTime && null != childTaskDayTime) {
                        taskTimeMap.put(childrenTask.getTaskId(), taskDayTime.add(childTaskDayTime)); //如果父任务有工时，那么加上子任务的
                    }
                    taskTimeMap.remove(childrenTask.getId()); //对map进行瘦身
                    taskIdSet.remove(childrenTask.getId()); //将子任务剔除，剩下的就是业务任务 和 项目任务了
                }
                List<Task> taskList = taskRepository.findByIdIn(taskIdSet);
                for(Task task : taskList){
                    if (StringUtils.isNotEmpty(task.getBudgetId())){//业务任务

                        budgetIdAndNameMap.put(task.getBudgetId(),task.getBudgetName());

                        BigDecimal taskDayTime = taskTimeMap.get(task.getId()); //把该任务的工时找到
                        BigDecimal budgetDayTime = budgetTimeMap.get(task.getBudgetId()); //把业务总工时找到

                        if(null == budgetDayTime) {
                            budgetTimeMap.put(task.getBudgetId(), taskDayTime); //如果业务总工时没有
                        }else {
                            budgetTimeMap.put(task.getBudgetId(), taskDayTime.add(budgetDayTime)); //如果业务总工时有工时，那么加任务的
                        }
                    }else if(StringUtils.isNotEmpty(task.getProjectId())){
                        projectIdSet.add(task.getProjectId());
                        BigDecimal taskDayTime = taskTimeMap.get(task.getId()); //把该任务的工时找到
                        BigDecimal projectDayTime = projectTimeMap.get(task.getProjectId()); //把项目总工时找到
                        if(null == projectDayTime) {
                            projectTimeMap.put(task.getProjectId(), taskDayTime); //如果业务总工时没有
                        }else {
                            projectTimeMap.put(task.getProjectId(), taskDayTime.add(projectDayTime)); //如果项目总工时有工时，那么加任务的
                        }

                    }
                    taskIdSet.remove(task.getId());
                    taskTimeMap.remove(task.getId());
                }
                if(CollectionUtils.isNotEmpty(projectIdSet)){
                    List<Project> projectList = projectRepository.findByIdIn(projectIdSet);
                    for (Project project : projectList){
                        if (StringUtils.isEmpty(project.getBudgetId())){
                            continue;
                        }
                        budgetIdAndNameMap.put(project.getBudgetId(),project.getBudget());

                        BigDecimal budgetDayTime = budgetTimeMap.get(project.getBudgetId()); //把业务总工时找到
                        BigDecimal projectDayTime = projectTimeMap.get(project.getId()); //把项目总工时找到
                        if(null == budgetDayTime && null != projectDayTime) {
                            budgetTimeMap.put(project.getBudgetId(), projectDayTime); //如果业务总工时没有
                        }else if(null != budgetDayTime && null != projectDayTime){
                            budgetTimeMap.put(project.getBudgetId(), budgetDayTime.add(projectDayTime)); //如果项目总工时有工时，那么加任务的
                        }
                    }
                }
                Map<String,Double> temMap = new HashMap<>();
                for (Map.Entry<String, String> entry : budgetIdAndNameMap.entrySet()) {
                    String budgetId = entry.getKey();
                    if (StringUtils.isEmpty(budgetId)){
                        continue;
                    }
                    String budgetName = budgetIdAndNameMap.get(budgetId);
                    if (StringUtils.isEmpty(budgetName)&&StringUtils.isNotEmpty(budgetId)) {
                        BudgetEO budgetEO = budgetEOService.selectByPrimaryKey(budgetId);
                        if (null == budgetEO){
                            continue;
                        }
                        budgetName = budgetEO.getProjectName();
                    }
                    BigDecimal tempDayTime = budgetTimeMap.get(budgetId);
                    if (null != tempDayTime ) {
                        temMap.put(budgetName, tempDayTime.setScale(2,RoundingMode.UP).doubleValue());
                    }
                }
                resultList.add(temMap);
            }//没有日报也给返回空list
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new AdcDaBaseException("任务信息查询异常，请联系系统管理员");
        }
        return resultList;
    }



    /**
     * add by liqiushi 20190314
     * 以个人+项目为维度统计工时
     *
     * @param paramType 是前段传来的统计工时的单位
     */
    public List<Map<String, Double>> getWorkTimeByPerson(String paramType, String userId) {
        List<Map<String, Double>> resultList = new ArrayList<>();
        List<Long> times = getTimeWithStyle(paramType);
        if (StringUtils.isEmpty(userId)) {
            userId = UserUtils.getUserId();
        }
        try {
            //查询用户所有项目
//            List<Daily> dailyList = finWorkTimeStatisticsService
//                    .getStart2EndDailyList(userId, times.get(0), times.get(1),"");
            //结束日期改为当前日期
            List<Daily> dailyList = finWorkTimeStatisticsService
                    .getStart2EndDailyList(userId, times.get(0), new Date().getTime(), "");

            if (CollectionUtils.isNotEmpty(dailyList)) {
                Map<String, Double> projectTime = new HashMap<>();
                for (Daily daily : dailyList) { //ES中 日报里的任务和子任务都存在taskIdArray中！
                    if (CollectionUtils.isNotEmpty(daily.getTaskIdArray())) {
                        Double evenTime = (double) 4 / daily.getTaskIdArray().length;
                        if(StringUtils.isNotEmpty(daily.getWorkCostTime())){
                            evenTime = Double.valueOf(daily.getWorkCostTime());
                        }
                        for (String childTaskId : daily.getTaskIdArray()) {
                            Task superTask = taskRepository.findByIdAndDelFlagNot(childTaskId, true);
                            if (superTask != null) {
                                //任务
                                String projectName = superTask.getProjectName();
                                if (StringUtils.isEmpty(projectName)) {
                                    //还有任务直接对应业务的情况，不能取业务 先给任务的名
                                    continue;
                                }
                                if (projectTime.containsKey(projectName)) {
                                    double workTimePlus = projectTime.get(projectName) + evenTime;
                                    projectTime.put(projectName, workTimePlus);
                                } else {
                                    projectTime.put(projectName, evenTime);
                                }
                            } else {
                                //子任务
                                ChildrenTask childrenTask = childTaskRepository
                                        .findByIdAndDelFlagNot(childTaskId, true);
                                if (childrenTask != null) {
                                    Task superTask1 = taskRepository
                                            .findByIdAndDelFlagNot(childrenTask.getTaskId(), true);
                                    if (superTask1 != null) {
                                        String projectName = superTask1.getProjectName();
                                        if (StringUtils.isEmpty(projectName)) {
                                            //还有任务直接对应业务的情况，不统计
                                            continue;
                                        }
                                        if (projectTime.containsKey(projectName)) {
                                            double workTimePlus = projectTime.get(projectName) + evenTime;
                                            projectTime.put(projectName, workTimePlus);
                                        } else {
                                            projectTime.put(projectName, evenTime);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                //转换成人日
                projectTime = removeMapEmptyValue(projectTime);
                Map<String, Double> map = new HashMap<>();
                if (CollectionUtils.isNotEmpty(projectTime)) {
                    BigDecimal eight = BigDecimal.valueOf(8);
                    for (String key : projectTime.keySet()) {
                        Double aDouble = projectTime.get(key);
                        BigDecimal manDay = BigDecimal.valueOf(aDouble);
                        map.put(key, manDay.divide(eight, 2, RoundingMode.UP).doubleValue());
                    }
                    resultList.add(map);
                }
            }//没有日报也给返回空list
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new AdcDaBaseException("任务信息查询异常，请联系系统管理员");
        }
        return resultList;
    }



    /**
     * 以大组为维度统计工时。
     * 如果有小组的话，统计各小组花费工时占比
     * 如果没有小组的话，统计大组中的每个人话费工时的占比
     */
    public List<Map<String, Double>> getWorkTimeByGroup(String paramType) {
        List<Map<String, Double>> resultList = new ArrayList<>();
        Map<String, Double> resultMap = new HashMap<>();
        List<Long> times = getTimeWithStyle(paramType);
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        try {
            //需求确认：登录此功能的用户都是大组的组长，所以没有使用递归查询
            List<StatisticsEntity> bigGroups = orgDao.querygGroupByUserId(userId);
            if (CollectionUtils.isNotEmpty(bigGroups)) {
                List<StatisticsEntity> childGroups;
                for (StatisticsEntity orgBean : bigGroups) {
                    childGroups = orgDao.queryAllChildGroups(orgBean.getId());
                    if (CollectionUtils.isNotEmpty(childGroups)) {
                        for (StatisticsEntity childGroup : childGroups) {
                            String childGroupId = childGroup.getId();
                            Double workTime = 0.0;
                            List<String> groupEmpIds = orgDao.queryEmpByOrgId(childGroupId);
                            if (CollectionUtils.isNotEmpty(groupEmpIds)) {
                                for (String empId : groupEmpIds) {
                                    //从ES中查出员工对应的日报工时
                                    List<Daily> dailyList
                                        = finWorkTimeStatisticsService
                                        .getStart2EndDailyList(empId, times.get(0), times.get(1),"");
                                    if (CollectionUtils.isNotEmpty(dailyList)) {
                                        workTime += dailyList.size() * 4;
                                    }
                                }
                            }
                            workTime = Math.ceil(workTime / 8);
                            resultMap.put(orgDao.queryOrgName(childGroupId), workTime);
                        }
                    } else {
                        //added by qichunxu 20190326 没有小组，统计每个项目的工时
                        String orgId = orgBean.getOrgId();
                        //查询这机构下的所有员工
                        List<String> empIds = orgDao.queryEmpByGropId(orgId);
                        List<Daily> dailyList = new ArrayList<>();
                        for (String id : empIds) {
                            List<Daily> dailies = staffScheduleService
                                .getDailyList(id, new Date(times.get(0)), new Date(times.get(1)));
                            dailyList.addAll(dailies);
                        }
                        resultMap = getProjectWorkTime(dailyList);
                    }
                }
                if (CollectionUtils.isNotEmpty(resultMap)) {
                    resultList.add(resultMap);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new AdcDaBaseException("任务信息查询异常，请联系系统管理员");
        }
        //登录用户名下没有大组时 返回空集合
        return resultList;
    }

    /**
     * @Description: 获取项目对应工时信息
     * @Param: dailies
     * @return: Map<String, Integer>
     * @Author: qichunxu
     * @Date: 2019/3/26
     */
    private Map<String, Double> getProjectWorkTime(List<Daily> dailies) {
        BigDecimal four = BigDecimal.valueOf(4.0);
        String[] idArray;
        BigDecimal workTime;
        BigDecimal num;
        Map<String, Double> map = new HashMap<>(16);
        Task task;
        String projectId;
        for (Daily daily : dailies) {
            idArray = daily.getTaskIdArray();
            num = BigDecimal.valueOf(idArray.length);
            workTime = four.divide(num, 2, BigDecimal.ROUND_FLOOR);
            for (String id : idArray) {
                ChildrenTask childrenTask = childTaskRepository.findByIdAndDelFlagNot(id, Boolean.TRUE);
                //任务ID
                if (null == childrenTask) {
                    task = taskRepository.findByIdAndDelFlagNot(id, Boolean.TRUE);
                    if (null == task || StringUtils.isBlank(projectId = task.getProjectId())) {
                        continue;
                    }
                    setMap(map, projectId, workTime.doubleValue());
                } else {
                    task = taskRepository.findByIdAndDelFlagNot(childrenTask.getTaskId(), Boolean.TRUE);
                    if (null == task || StringUtils.isBlank(projectId = task.getProjectId())) {
                        continue;
                    }
                    setMap(map, projectId, workTime.doubleValue());
                }
            }
        }
        //组装数据
        Map<String, Double> resultMap = new HashMap<>(16);
        if (null != map) {
            Project project;
            BigDecimal eight = BigDecimal.valueOf(8);
            for (String id : map.keySet()) {
                project = projectRepository.findByIdAndDelFlagNot(id, Boolean.TRUE);
                if (null != project) {
                    Double pDouble = map.get(id);
                    BigDecimal manDay = BigDecimal.valueOf(pDouble);
                    resultMap.put(project.getName(), manDay.divide(eight, 2, RoundingMode.UP).doubleValue());
                }
            }
        }
        return resultMap;
    }

    private void setMap(Map<String, Double> map, String projectId, Double workTime) {
        if (!map.containsKey(projectId)) {
            map.put(projectId, 0.00);
        }
        Double pDouble = map.get(projectId);
        map.put(projectId, pDouble + workTime);
    }

    private FinOrgStatisticsEO getHQWorkTime(String userId, String paramType) {

        UserEO userEO = userEOService.getUserWithRoles(userId);
        StringBuilder orgIds = new StringBuilder();
        List<OrgEO> orgList = userEO.getOrgEOList();
        for (OrgEO orgEO : orgList) {
            orgIds.append(orgEO.getId()).append(',');
        }

        ReqStatisticsVO reqStatisticsVO = new ReqStatisticsVO();
        reqStatisticsVO.setTimeType(paramType);
        reqStatisticsVO.setOrgId(orgIds.substring(0, orgIds.length() - 1));

        return finWorkTimeStatisticsService.getHQWorkTime(reqStatisticsVO);

    }

    public List<Map<String, Double>> getWorkTimeByHQNew(String paramType) {
        List<Map<String, Double>> resultList = new ArrayList<>();
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }

        List<StatisticsEntity> orgList = getHQWorkTime(userId, paramType).getOrgList();
        Map<String, Double> resMap = new HashMap<>();

        if (CollectionUtils.isNotEmpty(orgList)) {
            for (StatisticsEntity temp : orgList) {
                resMap.put(temp.getOrgName(), temp.getWorkTime());
            }
        }
        resultList.add(resMap);
        return resultList;
    }

    /**
     * 统计部门下各个大组的工时
     * 登录用户就是部长，直接取下属机构为大组的进行统计
     */
    public List<Map<String, Double>> getWorkTimeByDep(String paramType, String orgType) {
        List<Map<String, Double>> resultList = new ArrayList<>();
        Map<String, Integer> resultMap = new HashMap<>();
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        List<Long> times = getTimeWithStyle(paramType);
        //根据用户查出对应机构
        List<StatisticsEntity> orgList = orgDao.queryOrgByUserId(userId);
        if (CollectionUtils.isNotEmpty(orgList)) {
            for (StatisticsEntity orgBean : orgList) {
                if (!orgBean.getOrgName().contains("组")) { //取用户对应的部级机构
                    //根据本部机构查出下属各部
                    List<StatisticsEntity> bigGroups = orgDao.queryChildGroup(orgBean.getId());
                    if (CollectionUtils.isNotEmpty(bigGroups)) {
                        for (StatisticsEntity bigGroup : bigGroups) {
                            //查出大组下所有员工
                            List<String> empIds = orgDao.queryEmpByOrgId(bigGroup.getId());
                            int dailyNum = 0;
                            for (String empId : empIds) {
                                List<Daily> dailyList
                                    = finWorkTimeStatisticsService
                                    .getStart2EndDailyList(empId, times.get(0), times.get(1),"");
                                if (CollectionUtils.isNotEmpty(dailyList)) {
                                    dailyNum += dailyList.size();
                                }
                            }
                            int workTime = 4 * dailyNum;
                            resultMap.put(bigGroup.getOrgName(), workTime);
                        }
                    }
                }//不统计本部长在别的机构兼职
            }
            Map<String, Double> map = new HashMap<>();
            if (CollectionUtils.isNotEmpty(resultMap)) {
                BigDecimal eight = BigDecimal.valueOf(8);
                for (String key : resultMap.keySet()) {
                    Double aDouble = (double) resultMap.get(key);
                    BigDecimal manDay = BigDecimal.valueOf(aDouble);
                    map.put(key, manDay.divide(eight, 2, RoundingMode.UP).doubleValue());
                }
                resultList.add(map);
            }
        }
        return resultList;
    }

    public List<Long> getTimeWithStyle(String paramType) {
        List<Long> times = new ArrayList<>();
        List<Date> dateList = getTimeWithStyleDate(paramType);

        for (Date date : dateList) {
            times.add(date.getTime());
        }
        return times;
    }

    public List<Date> getTimeWithStyleDate(String paramType) {
        List<Date> times = new ArrayList<>();
        Date startTime;
        Date endTime;
        if ("month".equals(paramType)) {
            startTime = getTimesMonthmorning();
            endTime = getTimesMonthnight();
        } else if ("season".equals(paramType)) {
            startTime = getCurrentQuarterStartTime();
            endTime = getCurrentQuarterEndTime();
        } else if ("year".equals(paramType)) {
            startTime = getCurrentYearStartTime();
            endTime = getCurrentYearEndTime();
        } else {
            startTime = getTimesWeekmorning();
            endTime = getTimesWeeknight();
        }
        times.add(startTime);
        times.add(endTime);
        return times;
    }

    // 获得本周一日期
    public Date getTimesWeekmorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        /*Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayofweek == 1) {
            dayofweek += 7;
        }
        cal.add(Calendar.DATE, 2 - dayofweek);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);*/
        return cal.getTime();
    }

    // 获得本周日日期
    public Date getTimesWeeknight() {
        /*Calendar cal = Calendar.getInstance();
        cal.setTime(getTimesWeekmorning());
        cal.add(Calendar.DAY_OF_WEEK, 7);

        Instant instant = cal.getTime().toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        // atZone()方法返回在指定时区从此Instant生成的ZonedDateTime

        LocalDate lastWeekEndlocal = instant.atZone(zoneId).toLocalDate();
        LocalDateTime localDateTime = lastWeekEndlocal.atTime(23, 59, 59);
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of("Asia/Shanghai"));

        return  Date.from(zonedDateTime.toInstant());*/
        Calendar cal = Calendar.getInstance();
        cal.setTime(getTimesWeekmorning());
        cal.add(Calendar.DAY_OF_WEEK, 6);
        cal.getTime();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        return cal.getTime();
    }

    // 获得本月第一天0点时间
    public Date getTimesMonthmorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    // 获得本月最后一天24点时间
    public Date getTimesMonthnight() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    //季度第一天
    public Date getCurrentQuarterStartTime_SB() {
        Calendar c = Calendar.getInstance();
        int currentMonth = c.get(Calendar.MONTH) + 1;
        SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
        Date now = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 3) { c.set(Calendar.MONTH, 0); } else if (currentMonth >= 4 &&
                currentMonth <= 6) { c.set(Calendar.MONTH, 3); } else if (currentMonth >= 7 &&
                currentMonth <= 9) { c.set(Calendar.MONTH, 4); } else if (
                currentMonth >= 10 && currentMonth <= 12) { c.set(Calendar.MONTH, 9); }
            c.set(Calendar.DATE, 1);
            now = longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

    //季度第一天
    public Date getCurrentQuarterStartTime() {
        Calendar c = Calendar.getInstance();
        int currentMonth = c.get(Calendar.MONTH) + 1;
        SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
        Date now = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 3) {
                c.set(Calendar.MONTH, 0);
            } else if (currentMonth >= 4 && currentMonth <= 6) {
                c.set(Calendar.MONTH, 3);
            } else if (currentMonth >= 7 &&currentMonth <= 9) {
                c.set(Calendar.MONTH, 6);
            } else if (currentMonth >= 10 && currentMonth <= 12) { c.set(Calendar.MONTH, 9);
            }
            c.set(Calendar.DATE, 1);
            now = longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }



    //当前季度的结束时间
    public Date getCurrentQuarterEndTime() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getCurrentQuarterStartTime());
        cal.add(Calendar.MONTH, 3);
        return cal.getTime();
    }

    //当前季度的结束时间
    public Date getCurrentTime() {
        Calendar cal = Calendar.getInstance();
        return cal.getTime();
    }


    //今年第一天
    public Date getCurrentYearStartTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_YEAR, cal.getActualMinimum(Calendar.YEAR));
        return cal.getTime();
    }

    //今年最后一天
    public Date getCurrentYearEndTime() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getCurrentYearStartTime());
        cal.add(Calendar.YEAR, 1);
        return cal.getTime();
    }

    /**
     * 获取某个日期开始时间
     */
    public static Timestamp getDayStartTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        if (null != d) { calendar.setTime(d); }
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Timestamp(calendar.getTimeInMillis());

    }

    public static Map<String, Double> removeMapEmptyValue(Map<String, Double> paramMap) {
        Set<String> set = paramMap.keySet();
        Iterator<String> it = set.iterator();
        while (it.hasNext()) {
            String key = it.next();
            if (paramMap.get(key) == null || paramMap.get(key) == 0) {
                paramMap.remove(key);
                set = paramMap.keySet();
                it = set.iterator();
            }
        }
        return paramMap;
    }

}
