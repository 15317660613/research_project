package com.adc.da.statistics.controller;

import com.adc.da.budget.dao.BudgetEODao;
import com.adc.da.budget.entity.*;
import com.adc.da.budget.repository.ChildTaskRepository;
import com.adc.da.budget.repository.DailyRepository;
import com.adc.da.budget.repository.ProjectRepository;
import com.adc.da.budget.repository.TaskRepository;
import com.adc.da.budget.utils.CommonUtil;
import com.adc.da.statistics.dao.BusinessWorktimeEODao;
import com.adc.da.statistics.dao.ProjectWorktimeEODao;
import com.adc.da.statistics.dao.TaskWorktimeEODao;
import com.adc.da.statistics.entity.BusinessWorktimeEO;
import com.adc.da.statistics.entity.ProjectWorktimeEO;
import com.adc.da.statistics.entity.TaskWorktimeEO;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import lombok.extern.slf4j.Slf4j;
import org.dozer.util.IteratorUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

/**
 * describe:
 *
 * @author 李坤澔
 *     date 2019-06-18
 */
@Component
@Slf4j
public class TimerInputBusinessWorkTime {
    @Autowired
    private DailyRepository dailyRepository;

    @Autowired
    private BudgetEODao budgetEODao;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ChildTaskRepository childTaskRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectWorktimeEODao projectWorktimeEODao;

    @Autowired
    private BusinessWorktimeEODao businessWorktimeEODao;

    @Autowired
    private TaskWorktimeEODao taskWorktimeEODao;

    private boolean saveData;

    /**
     * 使用时将@Scheduled 注释取消即可
     */
    //@Scheduled(cron = "0/5 * * ? * *")
    @Scheduled(cron = "0 0 2 ? * *")
    public void start() {
        this.startTask(true);
    }

    /**
     * 工时统计
     *
     * @param saveData 是否将结果存入数据库，true为存入，false为只计算，不存入数据库
     * @return
     * @author Lee Kwanho 李坤澔
     *     date 2019-07-17
     **/
    public void startTask(boolean saveData) {

        this.saveData = saveData;
        /*
         *  所有业务
         * todo 未来需要对业务按照年划分
         */
        List<BudgetEO> budgetEOList = budgetEODao.findAllBudget();
        /*
         *  所有项目
         * todo 未来需要对项目进行划分
         */
        List<Project> projectList = IteratorUtils.toList(projectRepository.findAll().iterator());

        /*
         * 所有任务
         * todo 未来需要对任务进行划分
         */
        List<Task> taskList = IteratorUtils.toList(taskRepository.findAll().iterator());
        /*
         * 所有子任务
         * todo 子任务目前是全数据，后续可能需要改进
         */
        List<ChildrenTask> childrenTaskList = IteratorUtils.toList(childTaskRepository.findAll().iterator());

        Map<String, Integer> childrenTasksListMap = initIdMap(childrenTaskList);
        Map<String, Integer> budgetEOListMap = initIdMapBudget(budgetEOList);


        /*用于projectId回显budgetId */
        Map<String, String> trProjectBudget = new HashMap<>();
        Map<String, Integer> projectListMap = new HashMap<>();
        initIdMapProject(projectList, projectListMap, trProjectBudget);


        /* 用于id搜索 */
        Map<String, Integer> taskListMap = new HashMap<>();
        /*用于id回显project*/
        Map<String, String> trTaskProject = new HashMap<>();
        /*用于taskId回显budgetId，仅限业务任务*/
        Map<String, String> trTaskBudget = new HashMap<>();
        /*
         * 对taskListMap trTaskProject trTaskBudget进行初始化
         */
        initIdMapTask(taskList, taskListMap, trTaskProject, trTaskBudget);


        /*
         *  1天的长度
         */
        long oneDay = 86400000L;
        /*
         * 30天
         */
        long thirtyDay = 2592000000L;

        /* 2019-01-01  1546272000000L */
        long startLong = 1546272000000L;

        Map<String, BigDecimal> taskWorkTimeMap = new HashMap<>();
        Calendar calendar = Calendar.getInstance();

        /*删除   */
        deleteAllRecords();
        /* 统计次数超过10年 强制停止 */
        for (int i = 0; i < 3650; i++) {

            //加一天
            long endLong = startLong + oneDay;

            /*
             *  设置跳出 startLong >  当前时间 +30天
             */
            if (startLong > calendar.getTimeInMillis() + thirtyDay) {
                log.debug("end long is {}", endLong);
                break;
            }

            try {
                Iterable<Daily> dailyIterable = getDailyList(null, startLong, endLong);

                List<TaskWorktimeEO> taskWorkTimeList = new ArrayList<>(58);
                List<ProjectWorktimeEO> projectWorkTimeEOList = new ArrayList<>(58);
                List<BusinessWorktimeEO> businessWorkTimeEOList = new ArrayList<>(58);

                if (dailyIterable.iterator().hasNext()) {

                    String deptId = "";
                    for (Daily daily : dailyIterable) {
                        if (daily.getDailyField() == 10) { // 10 为实习生创建的日报
                            continue;
                        }
//                        if ("".equals(deptId)) {
                        if (StringUtils.isEmpty(deptId)) {
                            deptId = daily.getDeptId(); // 这里是老部门得id,可能部门改制之后，当时得部门压根就不存在了，导致工时仍被记录统计在老部门下
                        } else if (!deptId.equals(daily.getDeptId())) {
                            //不同部门
                            addTaskWorkList(
                                startLong, deptId,
                                taskWorkTimeList, taskWorkTimeMap,
                                childrenTaskList, childrenTasksListMap,
                                taskList, taskListMap);

                            deptId = daily.getDeptId();
                            taskWorkTimeMap.clear();
                        }
                        calculateWorkTime(daily, taskWorkTimeMap);
                    }

                    addTaskWorkList(
                        startLong, deptId,
                        taskWorkTimeList, taskWorkTimeMap,
                        childrenTaskList, childrenTasksListMap,
                        taskList, taskListMap);
                    taskWorkTimeMap.clear();
                }

                /*对ST_Task_WorkTime表进行存储,需要先存储再进行后续操作，否则 taskWorkTimeList 将被覆盖*/
                saveTaskWorkTime(taskWorkTimeList);

                /*
                 *  进行project,budget拆分，会变更 taskWorkTimeList的数据
                 *  更新projectWorkTimeEOList，businessWorkTimeEOList
                 */
                calculateProjectWorkTime(
                    taskWorkTimeList,
                    projectWorkTimeEOList,
                    startLong,
                    projectListMap,
                    projectList,
                    trTaskBudget,
                    trTaskProject,
                    trProjectBudget,
                    businessWorkTimeEOList,
                    budgetEOListMap,
                    budgetEOList);

                // 对ST_Project_WorkTime表进行存储
                saveProjectWorkTime(projectWorkTimeEOList);

                // 对ST_Business_WorkTime表进行存储
                saveBusinessWorkTime(businessWorkTimeEOList);

                //            2019-03-01 1551369600000L

                log.warn("now Day {} ,timeStamp {}", i, startLong);
                startLong = endLong;
            } catch (Exception e) {

                log.warn("error  Day {} ,timeStamp {}", i, startLong);
                log.error("timer error :", e);
                startLong = endLong;

            }

        }

    }

    /**
     * 删除数据库记录
     */
    private void deleteAllRecords() {

        if (saveData) {
            /*
             * 删除记录
             */
            try {
                businessWorktimeEODao.deleteAll();
                projectWorktimeEODao.deleteAll();
                taskWorktimeEODao.deleteAll();

            } catch (Exception e) {
                log.error("TimerInputBusinessWorkTime delete table false :  ", e);
                throw new AdcDaBaseException("TimerInputBusinessWorkTime delete table false ");
            }
        }
    }

    /**
     * 更新 TaskWorktimeEO 中的信息，补全businessId，projectId
     *
     * @param base
     * @param trTaskBudget
     * @param trTaskProject
     * @param trProjectBudget
     */
    private void updateChildTaskRecord(TaskWorktimeEO base, Map<String, String> trTaskBudget,
        Map<String, String> trTaskProject, Map<String, String> trProjectBudget) {
        /*处理子任务的记录*/
        String taskId = base.getTaskId();
        if (trTaskProject.containsKey(taskId)) {
            String projectId = trTaskProject.get(taskId);
            base.setProjectId(projectId);
            if (trProjectBudget.containsKey(projectId)) {
                base.setBusinessId(trProjectBudget.get(projectId));
            } else {
                log.warn("projectId {} not found  BudgetId ,which TaskId is {}", projectId, taskId);
            }
        } else if (trTaskBudget.containsKey(taskId)) {
            base.setBusinessId(trTaskBudget.get(taskId));
        } else {
            log.warn("Task {} not found projectId and BudgetId ", taskId);
        }

    }

    /**
     * 组装ST_Project_WorkTime 数据
     *
     * @param taskWorkTimeList
     * @param projectWorkTimeEOList
     * @param startLong
     * @param projectListMap
     * @param projectList
     * @param trTaskBudget
     * @param trTaskProject
     * @param trProjectBudget
     */
    private void calculateProjectWorkTime(List<TaskWorktimeEO> taskWorkTimeList,
        List<ProjectWorktimeEO> projectWorkTimeEOList,
        long startLong, Map<String, Integer> projectListMap, List<Project> projectList,
        Map<String, String> trTaskBudget, Map<String, String> trTaskProject, Map<String, String> trProjectBudget,
        List<BusinessWorktimeEO> businessWorktimeEOList,
        Map<String, Integer> budgetEOListMap, List<BudgetEO> budgetEOList) {

        Map<String, BigDecimal> projectWorkTimeMap = new HashMap<>();
        Map<String, BigDecimal> budgetTimeMap = new HashMap<>();

        String deptId = "";

        for (TaskWorktimeEO base : taskWorkTimeList) {

            /*更新子任务信息*/
            updateChildTaskRecord(base, trTaskBudget, trTaskProject, trProjectBudget);

            String projectId = base.getProjectId();

            if ("".equals(deptId)) {
                deptId = base.getDepartmentId();
            }

            if (!deptId.equals(base.getDepartmentId())) {
                //不同部门
                addProjectWorkList(startLong, deptId,
                    projectWorkTimeEOList, projectWorkTimeMap,
                    projectList, projectListMap);
                projectWorkTimeMap.clear();

                //不同部门，进行计算
                addBusinessWorkList(startLong, deptId,
                    businessWorktimeEOList, budgetTimeMap,
                    budgetEOList, budgetEOListMap);
                budgetTimeMap.clear();

                deptId = base.getDepartmentId();
            }

            /*
             * 对project数据进行计算
             */
            if (StringUtils.isNotEmpty(projectId)) {
                BigDecimal sum = base.getWorkTimeLocal();
                if (projectWorkTimeMap.containsKey(projectId)) {
                    BigDecimal workTimeBase = projectWorkTimeMap.get(projectId);
                    sum = sum.add(workTimeBase);
                }
                projectWorkTimeMap.put(projectId, sum);

            }

            String budgetId = base.getBusinessId();

            /*
             * 对business数据进行计算
             */
            if (StringUtils.isNotEmpty(budgetId)) {
                BigDecimal sum = base.getWorkTimeLocal();
                if (budgetTimeMap.containsKey(budgetId)) {
                    BigDecimal workTimeBase = budgetTimeMap.get(budgetId);
                    sum = sum.add(workTimeBase);
                }
                budgetTimeMap.put(budgetId, sum);
            }
        }
        addProjectWorkList(
            startLong, deptId,
            projectWorkTimeEOList, projectWorkTimeMap,
            projectList, projectListMap
        );
        projectWorkTimeMap.clear();

        addBusinessWorkList(
            startLong, deptId,
            businessWorktimeEOList, budgetTimeMap,
            budgetEOList, budgetEOListMap
        );
        budgetTimeMap.clear();

    }

    /**
     * 核算工时，Task维度，同部门的合并
     *
     * @param daily
     * @param taskWorkTimeMap
     */
    private void calculateWorkTime(Daily daily, Map<String, BigDecimal> taskWorkTimeMap) {
        List<String> taskArray = Arrays.asList(daily.getTaskIdArray());
        BigDecimal taskArraySize = BigDecimal.valueOf(taskArray.size());

        if (null == daily.getWorkCostTime()) {
            /*
             * 旧的日报，需要进行切割工时
             */
            for (String taskId : taskArray) {

                BigDecimal pointFive = BigDecimal.valueOf(0.5);

                BigDecimal sum = pointFive.divide(taskArraySize, 5, BigDecimal.ROUND_HALF_EVEN);
                if (taskWorkTimeMap.containsKey(taskId)) {
                    BigDecimal workTimeBase = taskWorkTimeMap.get(taskId);
                    sum = sum.add(workTimeBase);
                }
                taskWorkTimeMap.put(taskId, sum);

            }
        } else if (1 == daily.getApproveState()) {
            /*
             *  新的日报，直接用工作 时长/8小时 计算人天
             */
            BigDecimal dailyWorkTime = BigDecimal.valueOf(daily.getWorkCostTime());
            for (String taskId : taskArray) {
                BigDecimal eight = BigDecimal.valueOf(8);
                BigDecimal sum = dailyWorkTime.divide(eight, 5, BigDecimal.ROUND_HALF_EVEN);

                if (taskWorkTimeMap.containsKey(taskId)) {
                    BigDecimal workTimeBase = taskWorkTimeMap.get(taskId);
                    sum = sum.add(workTimeBase);
                }
                taskWorkTimeMap.put(taskId, sum);
            }
        }

    }

    /**
     * 将当前日的Task存入数据库
     *
     * @param taskWorkTimeList
     */
    private int saveTaskWorkTime(List<TaskWorktimeEO> taskWorkTimeList) {
        if (CollectionUtils.isNotEmpty(taskWorkTimeList) && saveData) {
            if (taskWorkTimeList.size() > 400) {
                List<List<TaskWorktimeEO>> splitList = CommonUtil.splitList(taskWorkTimeList, 400);
                int sum = 0;
                for (List<TaskWorktimeEO> saveList : splitList) {
                    sum += taskWorktimeEODao.insertSelectiveAll(saveList);
                }
                return sum;
            } else {
                return taskWorktimeEODao.insertSelectiveAll(taskWorkTimeList);
            }

        }
        return -1;
    }

    /**
     * 存当日ST_project_WorkTime
     *
     * @param taskWorkTimeList
     * @return
     */
    private int saveProjectWorkTime(List<ProjectWorktimeEO> taskWorkTimeList) {

        if (CollectionUtils.isNotEmpty(taskWorkTimeList) && saveData) {
            if (taskWorkTimeList.size() > 400) {
                List<List<ProjectWorktimeEO>> splitList = CommonUtil.splitList(taskWorkTimeList, 400);
                int sum = 0;
                for (List<ProjectWorktimeEO> saveList : splitList) {
                    sum += projectWorktimeEODao.insertSelectiveAll(saveList);
                }
                return sum;
            } else {
                return projectWorktimeEODao.insertSelectiveAll(taskWorkTimeList);

            }

        }
        return -1;
    }

    /**
     * 存当日ST_Business_WorkTime
     *
     * @param taskWorkTimeList
     * @return
     */
    private int saveBusinessWorkTime(List<BusinessWorktimeEO> taskWorkTimeList) {
        if (CollectionUtils.isNotEmpty(taskWorkTimeList) && saveData) {
            if (taskWorkTimeList.size() > 400) {
                List<List<BusinessWorktimeEO>> splitList = CommonUtil.splitList(taskWorkTimeList, 400);
                int sum = 0;
                for (List<BusinessWorktimeEO> saveList : splitList) {
                    sum += businessWorktimeEODao.insertSelectiveAll(saveList);
                }
                return sum;
            } else {
                return businessWorktimeEODao.insertSelectiveAll(taskWorkTimeList);

            }

        }
        return -1;
    }

    /**
     * 组装数据
     *
     * @param startLong
     * @param deptId
     * @param taskWorkTimeList
     * @param childrenTaskList
     * @param taskWorkTimeMap
     * @param childrenTasksListMap
     */
    private void addTaskWorkList(long startLong, String deptId,
        List<TaskWorktimeEO> taskWorkTimeList, Map<String, BigDecimal> taskWorkTimeMap,
        List<ChildrenTask> childrenTaskList, Map<String, Integer> childrenTasksListMap,
        List<Task> taskList, Map<String, Integer> taskListMap) {
        for (Map.Entry<String, BigDecimal> entry : taskWorkTimeMap.entrySet()) {
            String id = entry.getKey();
            BigDecimal value = entry.getValue();
            TaskWorktimeEO resultEO = initTaskWorkTimeEO(startLong, deptId);

            /*用于存储*/
            resultEO.setWorktime(value.floatValue());
            /*用于计算*/
            resultEO.setWorkTimeLocal(value);
            /*
             * 子任务
             */
            if (childrenTasksListMap.containsKey(id)) {
                resultEO.setParentTaskId(id);
                resultEO.setTaskId(childrenTaskList.get(childrenTasksListMap.get(id)).getTaskId());

            } else {
                resultEO.setTaskId(id);

                Task task = taskList.get(taskListMap.get(id));

                if (StringUtils.isNotEmpty(task.getBudgetId())) {
                    resultEO.setBusinessId(task.getBudgetId());
                } else {
                    resultEO.setProjectId(task.getProjectId());

                }
            }
            taskWorkTimeList.add(resultEO);
        }

    }

    private void addProjectWorkList(long startLong, String deptId,
        List<ProjectWorktimeEO> projectWorktimeList,
        Map<String, BigDecimal> projectWorkTimeMap,
        List<Project> projectList,
        Map<String, Integer> projectListMap) {

        for (Map.Entry<String, BigDecimal> entry : projectWorkTimeMap.entrySet()) {
            String id = entry.getKey();
            BigDecimal value = entry.getValue();
            ProjectWorktimeEO resultEO = initTaskWorkTimeEO(startLong, deptId);

            resultEO.setWorktime(value.floatValue());

            Project project = projectList.get(projectListMap.get(id));

            if (null != project) {
                resultEO.setProjectId(project.getId());
                resultEO.setBusinessId(project.getBudgetId());
            } else {
                log.warn("projectList.get(projectListMap.get(id)) null id is {}", id);
            }

            projectWorktimeList.add(resultEO);
        }

        log.debug("time {}", startLong);

    }

    private void addBusinessWorkList(long startLong, String deptId,
        List<BusinessWorktimeEO> projectWorktimeList,
        Map<String, BigDecimal> projectWorkTimeMap,
        List<BudgetEO> projectList,
        Map<String, Integer> projectListMap) {

        for (Map.Entry<String, BigDecimal> entry : projectWorkTimeMap.entrySet()) {
            String id = entry.getKey();
            BigDecimal value = entry.getValue();
            BusinessWorktimeEO resultEO = initTaskWorkTimeEO(startLong, deptId);

            resultEO.setWorktime(value.floatValue());

            Integer projectIndex = projectListMap.get(id);
            if (projectIndex == null) {
                /*
                 *  es数据和Oracle不同步，导致Oracle中查询不到对应的Budget，跳过处理，同时记录日志
                 */

                log.warn("projectListMap.get(id) == null  , Budget id is {}", id);
                continue;
            }
            BudgetEO project = projectList.get(projectIndex);

            if (null != project) {
                resultEO.setBusinessId(project.getId());
            } else {
                log.warn("projectList.get(projectListMap.get(id)) null id is {}", id);
            }

            projectWorktimeList.add(resultEO);
        }

        log.debug("time {}", startLong);

    }

    private void initIdMapTask(List<Task> taskList,
        Map<String, Integer> taskListMap,
        Map<String, String> trTaskProject,
        Map<String, String> trTaskBudget) {

        int index = 0;
        for (Task task : taskList) {
            taskListMap.put(task.getId(), index);
            index++;

            String projectId = task.getProjectId();
            if (StringUtils.isNotEmpty(projectId)) {
                trTaskProject.put(task.getId(), task.getProjectId());

            }
            String budgetId = task.getBudgetId();
            if (StringUtils.isNotEmpty(budgetId)) {
                trTaskBudget.put(task.getId(), task.getBudgetId());

            }
        }

    }

    /**
     * Project
     *
     * @param taskList
     * @return
     */
    private Map<String, Integer> initIdMap(List<ChildrenTask> taskList) {
        Map<String, Integer> resultMap = new HashMap<>();

        int index = 0;
        for (ChildrenTask task : taskList) {
            resultMap.put(task.getId(), index);
            index++;
        }

        return resultMap;
    }

    private void initIdMapProject(List<Project> taskList, Map<String, Integer> projectListMap,
        Map<String, String> trProjectBudget) {

        int index = 0;
        for (Project task : taskList) {
            String budgetId = task.getBudgetId();
            if (StringUtils.isNotEmpty(budgetId)) {
                trProjectBudget.put(task.getId(), budgetId);
            }
            projectListMap.put(task.getId(), index);
            index++;
        }

    }

    private Map<String, Integer> initIdMapBudget(List<BudgetEO> taskList) {
        Map<String, Integer> resultMap = new HashMap<>();

        int index = 0;
        for (BudgetEO task : taskList) {
            resultMap.put(task.getId(), index);
            index++;
        }

        return resultMap;
    }

    /**
     * 初始化保存入数据库的实体类，三个实体类军用这个进行初始化
     *
     * @param startLong
     * @param deptId
     * @return
     */
    private TaskWorktimeEO initTaskWorkTimeEO(long startLong, String deptId) {
        TaskWorktimeEO taskWorktimeEO = new TaskWorktimeEO();
        Date date = new Date(startLong);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;

        taskWorktimeEO.setDepartmentId(deptId);
        taskWorktimeEO.setYear(String.valueOf(year));
        taskWorktimeEO.setMonth(String.valueOf(month));
        taskWorktimeEO.setDailyTime(date);
        taskWorktimeEO.setId(UUID.randomUUID10());
        return taskWorktimeEO;
    }

    /**
     * 进行es查询
     * ExistsQueryBuilder qb = QueryBuilders.existsQuery("dailyCreateTime")
     * QueryBuilders.rangeQuery("dailyCreateTime")
     * .from(start)
     * .to(end)
     * .includeLower(true)
     * .includeUpper(false))
     *
     * @return
     * @author Lee Kwanho 李坤澔
     *     date 2019-06-01
     **/
    private Iterable<Daily> getDailyList(String deptId, long start, long end) {
        // 所有未删除日报
        BoolQueryBuilder builder = QueryBuilders.boolQuery().mustNot(QueryBuilders.termQuery("delFlag", true));
        // 是否要筛选部门
        if (StringUtils.isNotBlank(deptId)) {
            builder.should(QueryBuilders.termQuery("deptId", deptId));
        }

        builder.must(
            QueryBuilders.rangeQuery("dailyCreateTime")
                         .from(start)
                         .to(end)
                         //包含下界
                         .includeLower(true)
                         //不包含上界
                         .includeUpper(false));

        Sort sort = new Sort(Sort.Direction.DESC, "deptId");

        /*
         * 每天日报总量不可 超过3000条，返回结果集根据deptId排序
         */
        return dailyRepository.search(builder, new PageRequest(0, 3000, sort));
    }

}
