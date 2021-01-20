package com.adc.da.budget.service;

import com.adc.da.budget.dao.BudgetEODao;
import com.adc.da.budget.dao.CustomerWorkDateDao;
import com.adc.da.budget.entity.*;
import com.adc.da.budget.repository.*;
import com.adc.da.budget.vo.ReqStatisticsVO;
import com.adc.da.budget.vo.StatisticsVO;
import com.adc.da.budget.vo.UserProjectWorkTimeVO;
import com.adc.da.login.util.UserUtils;
import com.adc.da.sys.dao.OrgEODao;
import com.adc.da.sys.entity.OrgEO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.sys.service.UserEOService;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.DateUtils;
import com.adc.da.util.utils.StringUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

import static com.adc.da.budget.constant.ProjectSearchField.BUDGET_ID;
import static com.adc.da.budget.constant.ProjectSearchField.DEL_FLAG;

/**
 * @description: 财务系统工时统计
 * @author: qichunxu
 * @create: 2019-03-21 10:57
 **/
@Service
@Slf4j
public class FinWorkTimeStatisticsService {

    @Autowired
    private DailyRepository dailyRepository;

    @Autowired
    private OrgStatisticsRepository orgStatisticsRepository;

    @Autowired
    private BudgetEODao budgetEODao;

    @Autowired
    private UserEOService userEOService;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ChildTaskRepository childTaskRepository;

    /**
     * add by liqiushi 20190321
     * 统计各本部工时占比
     * <p>
     * 出现过两次前一天的旧数据没删干净导致报错的情况，重跑一下定时就好了，下面写成list实现一定程度的容错
     */
    @Autowired
    private OrgEODao orgEODao;

    BigDecimal DAY_WORK_TIME = new BigDecimal(8);

    public FinOrgStatisticsEO getHQWorkTime(ReqStatisticsVO reqStatisticsVO) {
        if (null == reqStatisticsVO) {
            return new FinOrgStatisticsEO();
        }

        String orgIds = reqStatisticsVO.getOrgId();
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
            orgIdSearch = reqStatisticsVO.getOrgId();
        }
        OrgEO orgEOTypeCheck = orgEODao.getOrgEOById(orgIdSearch);

        String orgRole;
        String orgType;
        if (orgEOTypeCheck.getName().contains("中心")) {
            orgRole = "数据中心领导";
            orgType = "本部";
        } else if (orgEOTypeCheck.getName().contains("本部")) {
            orgRole = "本部长";
            orgType = "部门";
        } else {
            orgRole = "部长";
            orgType = "项目组";
        }

        List<FinOrgStatisticsEO> finOrgStatisticsEO = orgStatisticsRepository
            .findBySuperOrgIdAndType(orgIdSearch, reqStatisticsVO.getTimeType());

        if (CollectionUtils.isNotEmpty(finOrgStatisticsEO)) {

            finOrgStatisticsEO.get(0).setRole(orgRole);
            finOrgStatisticsEO.get(0).setOrgType(orgType);

            if (!orgEOTypeCheck.getName().contains("中心")) {
                //拿到父级的上级ID
                FinOrgStatisticsEO currentOrgStatisticsEO = finOrgStatisticsEO.get(0);
                String parentId = currentOrgStatisticsEO.getParentDeptId();
                //不包含组的人员工时
                double other;
                //根据父级的上级ID去查询出父级的工时信息  减去组的工时，拿到没有组的人员工时
                FinOrgStatisticsEO statisticsEOS = orgStatisticsRepository
                    .findBySuperOrgIdAndType(parentId, reqStatisticsVO.getTimeType()).get(0);

                List<StatisticsEntity> orgList = statisticsEOS.getOrgList();
                if (CollectionUtils.isNotEmpty(orgList)) {
                    for (StatisticsEntity org : orgList) {
                        //父级
                        if (!org.getWorkTime().equals(currentOrgStatisticsEO.getAllWorkTime())
                            && StringUtils.equals(reqStatisticsVO.getOrgId(), org.getOrgId())) {

                            other = org.getWorkTime() - currentOrgStatisticsEO.getAllWorkTime();
                            StatisticsEntity entity = new StatisticsEntity(other, "", "其他");
                            currentOrgStatisticsEO.getOrgList().add(entity);
                            currentOrgStatisticsEO.setAllWorkTime(other + currentOrgStatisticsEO.getAllWorkTime());
                            for (int i = 0; i < currentOrgStatisticsEO.getOrgList().size(); i++) {
                                BigDecimal allTime = BigDecimal.valueOf(currentOrgStatisticsEO.getAllWorkTime());
                                BigDecimal orgTime = BigDecimal
                                    .valueOf(currentOrgStatisticsEO.getOrgList().get(i).getWorkTime());
                                BigDecimal percentMath = orgTime.divide(allTime, 4, BigDecimal.ROUND_HALF_EVEN)
                                                                .multiply(BigDecimal.valueOf(100));
                                DecimalFormat becimal = new DecimalFormat("0.00");
                                //百分比保留两位小数
                                String percent = becimal.format(percentMath);
                                currentOrgStatisticsEO.getOrgList().get(i).setPercent(percent);
                            }
                        }
                    }

                }

                return currentOrgStatisticsEO;
            } else {
                return finOrgStatisticsEO.get(0);
            }
        } else {
            FinOrgStatisticsEO res = new FinOrgStatisticsEO();
            res.setRole(orgRole);
            res.setOrgType(orgType);
            return res;
        }
    }

    //今年上个月第一天
    public static Date getLastMonthStartDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getMinimum(Calendar.DATE));
        // 时
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        // 分
        calendar.set(Calendar.MINUTE, 0);
        // 秒
        calendar.set(Calendar.SECOND, 0);
        // 毫秒
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    //今年上个月最后一天
    public static Date getLastMonthEndDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getMinimum(Calendar.DATE));
        // 时
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        // 分
        calendar.set(Calendar.MINUTE, 0);
        // 秒
        calendar.set(Calendar.SECOND, 0);
        // 毫秒
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public Map<String, Double> setWorkTimeMap(List<Daily> dailies) {
        String[] taskIdArray;
        Map<String, Double> map = new HashMap<>();
        ChildrenTask childrenTask;
        Task task;
        BigDecimal four = BigDecimal.valueOf(4.0);
        for (Daily daily : dailies) {
            taskIdArray = daily.getTaskIdArray();
            BigDecimal num = BigDecimal.valueOf(taskIdArray.length);
            BigDecimal workTime = four.divide(num, 4, BigDecimal.ROUND_HALF_EVEN);

            for (String id : taskIdArray) {
                childrenTask = childTaskRepository.findByIdAndDelFlagNot(id, Boolean.TRUE);
                //任务ID
                if (null == childrenTask) {
                    task = taskRepository.findByIdAndDelFlagNot(id, Boolean.TRUE);
                    if (null == task) {
                        continue;
                    }
                    setAllBusinessMap(map, task, workTime.doubleValue());
                    //子任务ID
                } else {
                    task = taskRepository.findByIdAndDelFlagNot(childrenTask.getTaskId(), Boolean.TRUE);
                    if (null == task) {
                        continue;
                    }
                    log.debug("任务ID：" + task.getId() + "项目ID：" + task.getProjectId());
                    setAllBusinessMap(map, task, workTime.doubleValue());
                }
            }
        }
        return map;
    }

    private void setAllBusinessMap(Map<String, Double> map, Task task, Double workTime) {
        Double wDouble;
        if (StringUtils.isBlank(task.getProjectId())) {
            if (!map.containsKey(task.getBudgetId())) {
                map.put(task.getBudgetId(), 0.0);
            }
            wDouble = map.get(task.getBudgetId());
            wDouble += workTime;
            map.put(task.getBudgetId(), wDouble);
            //项目任务
        } else {
            Project project = projectRepository.findByIdAndDelFlagNot(task.getProjectId(), Boolean.TRUE);
            if (null != project) {
                if (!map.containsKey(project.getBudgetId())) {
                    map.put(project.getBudgetId(), 0.0);
                }
                wDouble = map.get(project.getBudgetId());
                wDouble += workTime;
                map.put(project.getBudgetId(), wDouble);
            }
        }
    }

    private void setNewWorkTimeMap(List<Daily> newDailies, Map<String, Double> map) {
        String taskId;

        ChildrenTask childrenTask;
        Task task;

        for (Daily daily : newDailies) {
            taskId = daily.getTaskIdArray()[0];

            BigDecimal workTime = BigDecimal.valueOf(daily.getWorkCostTime());
            childrenTask = childTaskRepository.findByIdAndDelFlagNot(taskId, Boolean.TRUE);
            //任务ID
            if (null == childrenTask) {
                task = taskRepository.findByIdAndDelFlagNot(taskId, Boolean.TRUE);
                if (null == task) {
                    continue;
                }
                setAllBusinessMap(map, task, workTime.doubleValue());
                //子任务ID
            } else {
                task = taskRepository.findByIdAndDelFlagNot(childrenTask.getTaskId(), Boolean.TRUE);
                if (null == task) {
                    continue;
                }
                log.debug("任务ID：" + task.getId() + "项目ID：" + task.getProjectId());
                setAllBusinessMap(map, task, workTime.doubleValue());
            }
        }
    }

    /**
     * @Description: 抽取通用方法，返回统计业务id 时间区间投入的工时 Map
     * @Param:
     * @Return:
     * @Author: 丁强
     * @Date: 9:05
     **/
    public Map<String, Double> getBudgetNameWorkTimeMapByUserIdAndTimeRange(String userId, String paramType) {
        Date startTime = null;
        Date finishTime = new Date();

        if (StringUtils.equals("year", paramType)) {
            startTime = DateUtils.getCurrentYearStartTime();

        } else if (StringUtils.equals("season", paramType)) {
            startTime = DateUtils.getCurrentSeasonStartTime();

        } else if (StringUtils.equals("month", paramType)) {
            startTime = DateUtils.getTimesMonthStartTime();

        } else if (StringUtils.equals("week", paramType)) {
            startTime = DateUtils.getTimesWeekStartTime();

        }
        Map<String, Double> map = new HashMap<>();
        //历史数据中的日报
        List<Daily> dailies = this.getStart2EndDailyList(userId, startTime.getTime(), finishTime.getTime(), "0");
        //workCostTime有值的新日报
        List<Daily> newDailies = this.getStart2EndDailyList(userId, startTime.getTime(), finishTime.getTime(), "1");

        if (CollectionUtils.isEmpty(dailies) && CollectionUtils.isEmpty(newDailies)) {
            return map;
        }
        //遍历日报信息统计业务工时
        map = setWorkTimeMap(dailies);
        setNewWorkTimeMap(newDailies, map);
        Set<String> budgetIdSet = map.keySet();
        List<BudgetEO> budgetEOList = budgetEODao.selectByPrimaryKeys(new ArrayList<>(budgetIdSet));
        Map<String, Double> budgetNameWorkTimeMap = new HashMap<>();
        for (Map.Entry<String, Double> entry : map.entrySet()) {
            for (BudgetEO budgetEO : budgetEOList) {
                if (StringUtils.equals(budgetEO.getId(), entry.getKey())) {
                    budgetNameWorkTimeMap.put(budgetEO.getProjectName(), culWorkTimeHourToDay(entry.getValue()));
                }
            }
        }

        return budgetNameWorkTimeMap;
    }

    public Double culWorkTimeHourToDay(Double hour) {
        BigDecimal bigDecimal = new BigDecimal(hour);

        return bigDecimal.divide(DAY_WORK_TIME, 2, RoundingMode.HALF_UP).doubleValue();

    }


    /**
     * 根据用户查询四个维度业务工时信息
     *
     * @param userId
     * @return
     */
    public List<Map<String, Object>> getUserWorkTimeDiy(String userId, String beginTime, String endTime) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        List<StatisticsVO> list = new ArrayList<>();
        if (StringUtils.isBlank(userId)) {
            userId = UserUtils.getUserId();
        }
        Date startTime = null;
        Date finishTime = new Date();


        if (StringUtils.equals("year", beginTime)) {
            startTime = DateUtils.getCurrentYearStartTime();
            //finishTime = DateUtils.getCurrentYearEndTime();

        } else if (StringUtils.equals("season", beginTime)) {
            startTime = DateUtils.getCurrentSeasonStartTime();
            //finishTime = DateUtils.getCurrentSeasonEndTime();

        } else if (StringUtils.equals("month", beginTime)) {
            startTime = DateUtils.getTimesMonthStartTime();
            //finishTime = DateUtils.getTimesMonthEndTime();

        } else if (StringUtils.equals("week", beginTime)) {
            startTime = DateUtils.getTimesWeekStartTime();
            //finishTime = DateUtils.getTimesWeekEndTime();

        } else {
            startTime = DateUtils.stringToDate(beginTime, DateUtils.YYYY_MM_DD_EN);
            finishTime = DateUtils.stringToDate(endTime, DateUtils.YYYY_MM_DD_EN);
        }

        //历史数据中的日报
        List<Daily> dailies = this.getStart2EndDailyList(userId, startTime.getTime(), finishTime.getTime(), "0");
        //workCostTime有值的新日报
        List<Daily> newDailies = this.getStart2EndDailyList(userId, startTime.getTime(), finishTime.getTime(), "1");

        if (CollectionUtils.isEmpty(dailies) && CollectionUtils.isEmpty(newDailies)) {
            return resultList;
        }
        //遍历日报信息统计业务工时
        Map<String, Double> map = setWorkTimeMap(dailies);

        setNewWorkTimeMap(newDailies, map);

        if (CollectionUtils.isNotEmpty(map)) {
            Map<String, Object> statMap = new HashMap<>(16);
            Map<String, Object> allMap = new HashMap<>();
            BudgetEO budgetEO;
            BigDecimal workTime;
            double allManDay = 0.0;
            BigDecimal manDay = new BigDecimal(8);
            for (String id : map.keySet()) {
                workTime = BigDecimal.valueOf(map.get(id));
                budgetEO = budgetEODao.selectByPrimaryKey(id);
                BigDecimal bigDecimal = workTime.divide(manDay, 2, BigDecimal.ROUND_HALF_EVEN);
                //   (two, 2, BigDecimal.ROUND_HALF_UP)

                allManDay += bigDecimal.doubleValue();
                list.add(new StatisticsVO(
                    budgetEO.getId(),
                    budgetEO.getProjectName(),
                    map.get(id),
                    bigDecimal.setScale(2, RoundingMode.HALF_UP).doubleValue()));
            }
            //设置工时占比
            BigDecimal allTime = BigDecimal.valueOf(allManDay);
            BigDecimal multiply;
            for (StatisticsVO statisticsVO : list) {
                BigDecimal decimal = BigDecimal.valueOf(statisticsVO.getManDay());
                multiply = decimal.divide(allTime, 4, BigDecimal.ROUND_HALF_EVEN).multiply(BigDecimal.valueOf(100));

                statisticsVO.setPercentage(multiply.setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue());
            }
            Collections.sort(list);

            Double allWorkTime = BigDecimal.valueOf(allManDay).setScale(2, RoundingMode.HALF_UP).doubleValue();

            allMap.put("allWorkTime", allWorkTime);
            statMap.put("business", list);
            resultList.add(statMap);
            resultList.add(allMap);

            /*
             * 追加工时利用率
             * @author Lee Kwanho 李坤澔
             * date 2019-06-14
             **/

            resultList.add(this.utilizationRate(startTime, finishTime, allWorkTime));

        }
        return resultList;
    }

    public List<UserProjectWorkTimeVO> findUserWorkTimeByProjectId(String projectId) {
        List<UserProjectWorkTimeVO> userProjectWorkTimeVOList = new ArrayList<>();

        Project project = projectRepository.findById(projectId);
        String[] projectMemberIds = project.getProjectMemberIds();
        List<Daily> dailyList = null;
        if(StringUtils.isEmpty(projectMemberIds)){
            return userProjectWorkTimeVOList;
        }
        if (CollectionUtils.isNotEmpty(projectMemberIds)) {
            dailyList = getAllDailyByProjectId(projectId, projectMemberIds);
        }

        for (String userId : projectMemberIds) {

            UserProjectWorkTimeVO userProjectWorkTimeVO = new UserProjectWorkTimeVO();
            //查询职称
            try {
                UserEO userEO = userEOService.selectByPrimaryKey(userId);
                userProjectWorkTimeVO.setUserName(userEO.getUsname());
                userProjectWorkTimeVO.setWorkLevel(userEO.getContactAddress());
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            Float workTime = 0.0f;
            if (CollectionUtils.isNotEmpty(dailyList)) {
                for (Daily daily : dailyList) {
                    if (daily.getCreateUserId().equals(userId)) {
                        if (StringUtils.isNotEmpty(daily.getTimeSlot())) {
                            workTime += 4.0f / daily.getTaskIdArray().length;
                        } else {
                            workTime += daily.getWorkCostTime();
                        }
                    }
                }
            }
            userProjectWorkTimeVO.setProjectId(projectId);
            userProjectWorkTimeVO.setProjectName(project.getName());
            userProjectWorkTimeVO.setUserId(userId);
            userProjectWorkTimeVO.setWorkTime(workTime);
            userProjectWorkTimeVOList.add(userProjectWorkTimeVO);
        }
        return userProjectWorkTimeVOList;
    }

    /**
     * Author 丁强
     * Descri 根据项目id 查询上个月所有成员在该项目所有任务及子任务填写的所有日报
     *
     * @param projectId        项目id
     * @param projectMemberIds 项目组成员数组
     * @return
     */
    private List<Daily> getAllDailyByProjectId(String projectId, String[] projectMemberIds) {
        List<Daily> resultDailyList = new ArrayList<>();
        //1.构造时间区间
        Date lastMonthStartDay = getLastMonthStartDay();
        Date lastMonthEndDay = getLastMonthEndDay();
        //2.构造查询条件，按日报时间查询，且是未删除，已审批的、所有成员
        BoolQueryBuilder dailyQueryBuilder = QueryBuilders.boolQuery();
        dailyQueryBuilder.must(QueryBuilders.rangeQuery("dailyCreateTime")
                                            .from(lastMonthStartDay.getTime())
                                            .to(lastMonthEndDay.getTime())
                                            .includeLower(true)
                                            .includeUpper(true));
        dailyQueryBuilder.mustNot(QueryBuilders.termQuery(DEL_FLAG, Boolean.TRUE));
        dailyQueryBuilder.must(QueryBuilders.termsQuery("createUserId", projectMemberIds));
        dailyQueryBuilder.must(QueryBuilders.termQuery("approveState", 1));
        //3.从ES 查询
        Iterable<Daily> dailyIterable = dailyRepository.search(dailyQueryBuilder);
        //4.根据项目Id 查找任务 task
        List<Task> taskList = taskRepository.findByProjectIdAndDelFlagNot(projectId, true);
        if (CollectionUtils.isEmpty(taskList)) {
            return resultDailyList;
        }
        List<String> taskIdList = new ArrayList<>();
        for (Task task : taskList) {
            taskIdList.add(task.getId());
        }
        //5.查找所有子任务 ChildrenTask
        Iterable<ChildrenTask> childrenTaskIterable = childTaskRepository.findByTaskIdIn(taskIdList);
        //6.将将所有数据项目的任务及子任务的日报装填进List 里面
        if (!dailyIterable.iterator().hasNext()) {
            return resultDailyList;
        }
        for (Daily daily : dailyIterable) {
            for (String taskId : taskIdList) {
                if (Arrays.asList(daily.getTaskIdArray()).contains(taskId)) {
                    resultDailyList.add(daily);
                }
            }
            if (childrenTaskIterable.iterator().hasNext()) {
                for (ChildrenTask childrenTask : childrenTaskIterable) {
                    if (Arrays.asList(daily.getTaskIdArray()).contains(childrenTask.getId())) {
                        resultDailyList.add(daily);
                    }
                }
            }
        }
        //7.返回
        return resultDailyList;
    }

    public List<UserProjectWorkTimeVO> findUserWorkTimeByBudgetId(String budgetId) {
        List<UserProjectWorkTimeVO> userProjectWorkTimeVOList = new ArrayList<>();
        Set<String> memberUserIdSet = new HashSet<>();
        BudgetEO budgetEO = budgetEODao.selectByPrimaryKey(budgetId);

        if (null == budgetEO) {
            throw new AdcDaBaseException("id 为 " + budgetId + " 的业务不存在!");
        }

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        boolQueryBuilder.must(QueryBuilders.termQuery(BUDGET_ID, budgetId));
        boolQueryBuilder.mustNot(QueryBuilders.termQuery(DEL_FLAG, true));

        Iterable<Project> projectIterable = projectRepository.search(boolQueryBuilder);

        List<String> projectIdList = new ArrayList<>();
        for (Project project : projectIterable) {
            if (CollectionUtils.isNotEmpty(project.getProjectMemberIds())) {
                Collections.addAll(memberUserIdSet, project.getProjectMemberIds());
            }
            projectIdList.add(project.getId());
        }
        List<Task> taskListProject = taskRepository.findByProjectIdInAndDelFlagNot(projectIdList, true);
        List<Task> taskListBudget = taskRepository.findByBudgetIdAndDelFlagNot(budgetId, true);
        Set<String> taskIdSet = new HashSet<>();

        if (CollectionUtils.isNotEmpty(taskListProject)) {
            for (Task task : taskListProject) {

                if (CollectionUtils.isNotEmpty(task.getMemberIds())) {
                    Collections.addAll(memberUserIdSet, task.getMemberIds());
                }

                taskIdSet.add(task.getId());
            }
        }
        if (CollectionUtils.isNotEmpty(taskListBudget)) {
            for (Task task : taskListBudget) {

                if (CollectionUtils.isNotEmpty(task.getMemberIds())) {
                    Collections.addAll(memberUserIdSet, task.getMemberIds());
                }

                taskIdSet.add(task.getId());
            }
        }
        List<ChildrenTask> childrenTaskList = childTaskRepository.findByTaskIdInAndDelFlagNot(taskIdSet, true);

        if (CollectionUtils.isNotEmpty(childrenTaskList)) {
            for (ChildrenTask childTask : childrenTaskList) {
                if (StringUtils.isNotEmpty(childTask.getPersonId())) {
                    memberUserIdSet.add(childTask.getPersonId());
                }
                if (CollectionUtils.isNotEmpty(childTask.getMemberIds())){
                    memberUserIdSet.addAll(Arrays.asList(childTask.getMemberIds()));
                }
                taskIdSet.add(childTask.getId());
            }
        }

        List<Daily> dailyList = dailyRepository.findByTaskIdArrayInAndDelFlagNot(taskIdSet, true);

        for (String userId : memberUserIdSet) {

            UserProjectWorkTimeVO userProjectWorkTimeVO = new UserProjectWorkTimeVO();
            //查询职称
            try {
                UserEO userEO = userEOService.selectByPrimaryKey(userId);
                userProjectWorkTimeVO.setUserName(userEO.getUsname());
                userProjectWorkTimeVO.setWorkLevel(userEO.getContactAddress());
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            Float workTime = 0.0f;
            if (CollectionUtils.isNotEmpty(dailyList)) {
                for (Daily daily : dailyList) {
                    if (daily.getCreateUserId().equals(userId)) {
                        if (StringUtils.isNotEmpty(daily.getTimeSlot())) {
                            workTime += 4.0f / daily.getTaskIdArray().length;
                        } else {
                            workTime += daily.getWorkCostTime();
                        }
                    }
                }
            }
            userProjectWorkTimeVO.setProjectId(budgetId);
            userProjectWorkTimeVO.setProjectName(budgetEO.getProjectName());
            userProjectWorkTimeVO.setUserId(userId);
            userProjectWorkTimeVO.setWorkTime(workTime);
            userProjectWorkTimeVOList.add(userProjectWorkTimeVO);
        }
        return userProjectWorkTimeVOList;
    }

    /**
     * 查询工作日
     */
    @Autowired
    private CustomerWorkDateDao customerWorkDateDao;

    /**
     * 设置工时利用率
     *
     * @param startDate
     * @return
     */
    private Map<String, Object> utilizationRate(Date startDate, Date endDate, Double workTime) {
        Map<String, Object> utilizationRate = new HashMap<>();
        int workDay = customerWorkDateDao.countWorkDate(startDate, endDate);

        BigDecimal workDayB = BigDecimal.valueOf(workDay);
        BigDecimal workTimeB = BigDecimal.valueOf(workTime);
        BigDecimal bigDecimal = workTimeB.divide(workDayB, 5, BigDecimal.ROUND_HALF_EVEN);
        utilizationRate.put("utilizationRate", bigDecimal);
        return utilizationRate;
    }

    public List<Daily> getStart2EndDailyList(String userId, Long start, Long end, String flag) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                                                     .must(QueryBuilders.matchQuery("createUserId", userId))
                                                     .mustNot(QueryBuilders.termQuery(DEL_FLAG, Boolean.TRUE));
        BoolQueryBuilder timeBuilder = QueryBuilders.boolQuery();

        timeBuilder.must(
            QueryBuilders.rangeQuery("dailyCreateTime")
                         .from(start)
                         .to(end)
                         .includeLower(true)
                         .includeUpper(true)
        );

        if (StringUtils.equals("1", flag)) {
            timeBuilder.must(QueryBuilders.existsQuery("workCostTime"));
        }
        if (StringUtils.equals("0", flag)) {
            timeBuilder.mustNot(QueryBuilders.existsQuery("workCostTime"));
        }
        queryBuilder.must(timeBuilder);
        queryBuilder.must(
            QueryBuilders.termQuery("approveState", 1)
        );
        return Lists.newArrayList(dailyRepository.search(queryBuilder));
    }

}
