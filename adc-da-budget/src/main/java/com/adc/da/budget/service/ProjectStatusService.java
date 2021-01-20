package com.adc.da.budget.service;

import com.adc.da.budget.entity.*;
import com.adc.da.budget.enums.ProjectStatusEnums;
import com.adc.da.budget.repository.*;
import com.adc.da.budget.vo.*;
import com.adc.da.login.util.UserUtils;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.sys.service.UserEOService;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.DateUtils;
import com.adc.da.util.utils.GsonUtil;
import com.adc.da.util.utils.StringUtils;
import com.google.common.collect.Lists;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * @Auther: ZHAIKAIXUAN
 * @Date: 2018/11/22 10:58
 * @Description: 项目状况分析
 */
@Service
public class ProjectStatusService {
    @Autowired
    private DailyRepository dailyRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ChildTaskRepository childTaskRepository;
    @Autowired
    private UserEOService userEOService;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ExpensesIncurredRepository expensesIncurredRepository;




    private static final String DEL_FLAG = "delFlag";
    private static final String PROJECT_ID = "projectId";


    /**
     * @Description: 获取项目对应的任务
     * @auther: ZHAIKAIXUAN
     * @params:
     * @return:
     * @date: 2018/11/26 14:49
     */
    @Deprecated
    public List<Map<String, Object>> taskStatics(String id) throws Exception {
        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery(PROJECT_ID, id))
                .must(QueryBuilders.termQuery("taskStatus", "已完成"))
                .mustNot(QueryBuilders.termQuery(DEL_FLAG, true));
        List<Task> tasks = Lists.newArrayList(taskRepository.search(queryBuilder));
        Set<Integer> set = new HashSet<>();
        Project project = projectRepository.findOne(id);
        for (Task task : tasks) {

            Date projectStartTime = project.getProjectStartTime();
            Date finishedActualTime = task.getFinishedActualTime();
            if (null != finishedActualTime && null != projectStartTime) {
                int week = (int) week(projectStartTime, finishedActualTime);
                set.add(week);
            }

        }
        // 项目中的所有的日报
        QueryBuilder dailyQueryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery(PROJECT_ID, id))
                .mustNot(QueryBuilders.termQuery(DEL_FLAG, true));
        List<Daily> dailies = Lists.newArrayList(dailyRepository.search(dailyQueryBuilder));
        for (Daily daily : dailies) {
            if (null != daily.getDailyCreateTime() && null != project.getProjectStartTime()) {
                int week = (int) week(project.getProjectStartTime(), daily.getDailyCreateTime());
                set.add(week);
            }

        }

        return taskStatusWorkTime(set, dailies, tasks, project.getProjectStartTime());
    }

    /**
     * @Description: 获取项目对应的任务
     * @auther: ZHAIKAIXUAN
     * @params:
     * @return:
     * @date: 2018/11/26 14:49
     */
    public List<TaskFinishVO> taskFinishStatistic(String id) {
        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .mustNot(QueryBuilders.termQuery(DEL_FLAG, true))
                .must(QueryBuilders.termQuery(PROJECT_ID, id))
                .must(QueryBuilders.termQuery("taskStatus", "已完成"));
        List<Task> tasks = Lists.newArrayList(taskRepository.search(queryBuilder));
        // 项目中的所有的日报
        QueryBuilder dailyQueryBuilder = QueryBuilders.boolQuery()
                .mustNot(QueryBuilders.termQuery(DEL_FLAG, true))
                .must(QueryBuilders.termQuery(PROJECT_ID, id));
        List<Daily> dailies = Lists.newArrayList(dailyRepository.search(dailyQueryBuilder));

        Project project = projectRepository.findOne(id);
        // 计算项目的周数量
        List<TaskFinishVO> taskFinishVOS = new ArrayList<>();
        long weekNumber = (System.currentTimeMillis() - project.getProjectStartTime().getTime()) / 3600 / 1000 / 24 / 7;
        if (weekNumber <= 0) {
            return taskFinishVOS;
        }
        for (int i = 0; i < weekNumber; i++) {
            TaskFinishVO taskFinishVO = new TaskFinishVO();
            taskFinishVO.setWeek(i);
            // 获取工时
            taskFinishVO.setWorkTime(0);
            taskFinishVO.setTaskNum(0);
            taskFinishVOS.add(taskFinishVO);
        }
        for (Task task : tasks) {
            // 没有完成时间
            if (task.getFinishedActualTime() == null) {continue;}
            // 完成时间大于今天
            if (task.getFinishedActualTime().getTime() > System.currentTimeMillis()) {continue;}
            int weekNum = (int) ((task.getFinishedActualTime().getTime() - project.getProjectStartTime().getTime()) / 3600 / 1000 / 24 / 7);
            if (weekNum >= weekNumber) {weekNum--;}
            if (weekNum <= 1) {weekNum = 1;}
            TaskFinishVO taskFinishVO = taskFinishVOS.get(weekNum - 1);
            taskFinishVO.setTaskNum(taskFinishVO.getTaskNum() + 1);
        }
        for (Daily daily : dailies) {
            // 没有日报创建时间
            if (daily.getDailyCreateTime() == null) {continue;}
            // 日报创建时间比项目开始时间还早
            if (daily.getDailyCreateTime().getTime() < project.getProjectStartTime().getTime()) {continue;}
            int weekNum = (int) ((daily.getDailyCreateTime().getTime() - project.getProjectStartTime().getTime()) / 3600 / 1000 / 24 / 7);
            if (weekNum >= weekNumber) {
                weekNum--;
            }
            if (weekNum <= 1) {
                weekNum = 1;
            }
            TaskFinishVO taskFinishVO = taskFinishVOS.get(weekNum - 1);
            // FIXME 没有判断工作的任务是否是在当前项目中
            float[] workTimes = daily.getWorktimeArray();
            float workTime = 0;
            for (int i = 0; i < workTimes.length; i++) {
                workTime += workTimes[i];
            }
            taskFinishVO.setWorkTime(taskFinishVO.getWorkTime() + workTime);
        }
        return taskFinishVOS;
    }

    /**
     * @Description: 整理任务状态和工时投入数据格式
     * @auther: ZHAIKAIXUAN
     * @params:
     * @return:
     * @date: 2018/11/26 15:07
     */
    public List<Map<String, Object>> taskStatusWorkTime(Set<Integer> set, List<Daily> dailies, List<Task> tasks, Date projectCreateTime) {
        List<Map<String, Object>> maps = new ArrayList<>();
        Map<Integer, Integer> taskMap = new HashMap<>();
        Map<Integer, Float> workMap = new HashMap<>();
        for (Daily daily : dailies) {
            if (null != daily.getDailyCreateTime() && null != daily.getWorktimeArray() && null != projectCreateTime) {
                int week = (int) week(projectCreateTime, daily.getDailyCreateTime());
                if (workMap.containsKey(week)) {
                    workMap.put(week, workMap.get(week) + arraySum(daily.getWorktimeArray()));
                } else {
                    workMap.put(week, arraySum(daily.getWorktimeArray()));
                }
            }

        }

        for (Task task : tasks) {
            if (null != task.getFinishedActualTime() && null != projectCreateTime) {
                int week = (int) week(projectCreateTime, task.getFinishedActualTime());
                if (taskMap.containsKey(week)) {
                    taskMap.put(week, taskMap.get(week) + 1);
                } else {
                    taskMap.put(week, 1);
                }
            }

        }

        for (Integer week : set) {
            Map<String, Object> mapEle = new HashMap<>();
            mapEle.put("week", week);
            if (taskMap.get(week) == null) {
                mapEle.put("taskNum", 0);
            } else {
                mapEle.put("taskNum", taskMap.get(week));
            }

            if (workMap.get(week) == null) {
                mapEle.put("workTime", 0);
            } else {
                mapEle.put("workTime", workMap.get(week));
            }
            maps.add(mapEle);
        }
        return maps;
    }


    /**
     * @Description: 人员工时数统计
     * @auther: ZHAIKAIXUAN
     * @params:
     * @return:
     * @date: 2018/11/22 12:46
     */
    public List<Map<String, Object>> queryWorkTimeStatus(String id) throws Exception {
        List<Map<String, Object>> sortingList = new ArrayList<>();
        //获取项目详情
        Project project = projectRepository.findOne(id);
        if (project == null) {
            throw new IllegalArgumentException();
        }
        //获取项目任务信息
        BoolQueryBuilder boolQueryBuilder = boolQuery().must(termQuery(PROJECT_ID, project.getId()))
                .mustNot(QueryBuilders.termQuery(DEL_FLAG, true));
        BoolQueryBuilder childrenBoolQueryBuilder = boolQuery()
                .mustNot(QueryBuilders.termQuery(DEL_FLAG, true));
        BoolQueryBuilder dailyBoolQueryBuilder = boolQuery()
                .mustNot(QueryBuilders.termQuery(DEL_FLAG, true));

        Iterator<Task> iterator = taskRepository.search(boolQueryBuilder).iterator();
        while (iterator.hasNext()) {
            Task task = iterator.next();
            childrenBoolQueryBuilder.should(QueryBuilders.termQuery("taskId", task.getId()));
        }
        //所有子任务信息
        Iterator<ChildrenTask> childrenTaskIterator = childTaskRepository.search(childrenBoolQueryBuilder).iterator();
        while (childrenTaskIterator.hasNext()) {
            ChildrenTask childrenTask = childrenTaskIterator.next();
            dailyBoolQueryBuilder.should(QueryBuilders.termQuery("taskIdArray", childrenTask.getId()));
        }
        //所有日报信息
        List<Daily> dailyList = Lists.newArrayList(dailyRepository.search(dailyBoolQueryBuilder));
        Map<Integer, Float> map = new HashMap();
        //工时数计算 每条0.5天 == 4个工时
        for (Daily daily : dailyList) {
            // 获取项目的实际开始时间
            if (null != daily.getDailyCreateTime()) {
                if (null == project.getProjectStartTime()) {
                    return null;
                }
                //计算日报为第几周
                int week = (int) week(project.getProjectStartTime(), daily.getDailyCreateTime());
                if (map.containsKey(week)) {
                    map.put(week, map.get(week)+ 4);
                } else {
                    map.put(week, 4f);
                }
            }

        }
        // 拍成有序的工时数
        Object[] objects = new ArrayList<Integer>(map.keySet()).toArray();
        List<Object> sorting = numPlace(objects);

        for (Object sortingEle : sorting) {
            Map<String, Object> mapIndex = new HashMap<>();
            mapIndex.put("week", (Integer) sortingEle);
            mapIndex.put("workTime", map.get(sortingEle));
            sortingList.add(mapIndex);
        }
        // 修改工时结构

        return sortingList;


    }

    public ProjectWorkTimeChartVO queryWorkTime(String projectId) {
        //获取项目详情
        Project project = projectRepository.findOne(projectId);
        if (project == null || Boolean.TRUE.equals(project.getDelFlag())) {
            throw new IllegalArgumentException();
        }
        Date projectStartTime = project.getProjectStartTime();
        SimpleDateFormat yMD = new SimpleDateFormat("yyyy-MM-dd");
        if (null == projectStartTime){
            projectStartTime = new Date();
        }
        String projectStart = yMD.format(projectStartTime);
        String today = yMD.format(new Date());
        if (today.equals(projectStart)) {
            BoolQueryBuilder taskQueryBuilder = boolQuery().must(termQuery(PROJECT_ID, projectId))
                    .mustNot(QueryBuilders.termQuery(DEL_FLAG, true));
            Iterator<Task> taskIterator = taskRepository.search(taskQueryBuilder).iterator();
            BoolQueryBuilder childrenBoolQueryBuilder = boolQuery();
            while (taskIterator.hasNext()) {
                Task task = taskIterator.next();
                childrenBoolQueryBuilder.should(QueryBuilders.termQuery("taskId", task.getId()));
            }
            //当前项目下所有子任务
//            ArrayList<ChildrenTask> childrenTasks = Lists.newArrayList(childTaskRepository.search(childrenBoolQueryBuilder));
//            Map<String, Integer> completeWorkTimeMap = getCompleteWorkTime(childrenTasks, project.getProjectMemberIds());
//            Map<String, Integer> laveWorkTimeMap = getlaveWorkTime(childrenTasks, project.getProjectMemberIds());
            //所有员工完成的工时
            int completeWorkTime1 = 0;
            //所有员工正在进行的工时
            int executeWorkTime1 = 0;
            //所有工时
            int allWorkTime1 = 0;
            //剩余小时数
            int laveWorkTime1 = 0;
//            for (String id : completeWorkTimeMap.keySet()) {
//                completeWorkTime1 += completeWorkTimeMap.get(id);
//            }
//            for (String id : laveWorkTimeMap.keySet()) {
//                executeWorkTime1 += laveWorkTimeMap.get(id);
//            }
            Map<Integer, List<MemberStatusVO>> map = new HashMap<>();
//            for (String id : projectMemberIds) {
//                Map<String, Integer> m = new HashMap<>();
//                Integer complete = completeWorkTimeMap.get(id);
//                Integer lave = laveWorkTimeMap.get(id);
//                m.put("已完成", complete == null ? 0 : complete);
//                m.put("剩余", lave == null ? 0 : lave);
//                UserEO userEO = userEOService.getUserWithRoles(id);
//                memberWorkTimeMap.put(userEO.getUsname(), m);
//            }
            Map<String, Map<String, Integer>> memberWorkTimeMap = getlaveWorkTime(projectId);
            String time = "";
            List<String> list1 = new ArrayList<>();
            List<Integer> list2 = new ArrayList<>();
            List<Integer> list3 = new ArrayList<>();
            List<Integer> list4 = new ArrayList<>();

            for (int i = 1; i < 29; i++) {
                time = "2019/02/" + i;
                list1.add(time);
            }
            String[] time1 = new String[list1.size()];
            list1.toArray(time1);
            Integer[] completeWorkTime = new Integer[list2.size()];
            list2.toArray(completeWorkTime);
            Integer[] executeWorkTime = new Integer[list3.size()];
            list3.toArray(executeWorkTime);
            Integer[] laveWorkTime = new Integer[list4.size()];
            list4.toArray(laveWorkTime);
            List<MemberStatusVO> memberStatus = getMemberStatus(projectId);
            map.put(0, memberStatus);
            ProjectAchievementsChartVO projectAchievementsChartVO = new ProjectAchievementsChartVO();
            List<Double[]> bugDensity = new ArrayList<>();
            Map<String, Integer[]> financeChart = new HashMap<>();
            ProjectWorkTimeChartVO projectWorkTimeChartVO = new ProjectWorkTimeChartVO(projectId, time1, completeWorkTime, executeWorkTime, laveWorkTime, memberWorkTimeMap,
                    projectAchievementsChartVO, bugDensity, financeChart, map, true);
            return projectWorkTimeChartVO;

        }
        String data0 = "{\"id\":\"\",\"time\":[\"2019/02/1\",\"2019/02/2\",\"2019/02/3\",\"2019/02/4\",\"2019/02/5\",\"2019/02/6\",\"2019/02/7\",\"2019/02/8\",\"2019/02/9\",\"2019/02/10\",\"2019/02/11\",\"2019/02/12\",\"2019/02/13\",\"2019/02/14\",\"2019/02/15\",\"2019/02/16\",\"2019/02/17\",\"2019/02/18\",\"2019/02/19\",\"2019/02/20\",\"2019/02/21\",\"2019/02/22\",\"2019/02/23\",\"2019/02/24\",\"2019/02/25\",\"2019/02/26\",\"2019/02/27\",\"2019/02/28\"],\"completeWorkTime\":[8,76,94,57,32,36,53,91,15,91],\"executeWorkTime\":[332,107,459,172,269,377,409,365,50,382],\"laveWorkTime\":[183,45,618,264,728,747,488,268,125,739],\"memberWorkTimeMap\":{\"李宏伟\":{\"已完成\":24,\"剩余\":8},\"王洪洋\":{\"已完成\":20,\"剩余\":9},\"陈小珂\":{\"已完成\":10,\"剩余\":7},\"刘宏伟\":{\"已完成\":24,\"剩余\":9},\"邓程鹏\":{\"已完成\":6,\"剩余\":13},\"谢卉瑜\":{\"已完成\":7,\"剩余\":8},\"温泉\":{\"已完成\":12,\"剩余\":6},\"李冰阳\":{\"已完成\":11,\"剩余\":13}},\"projectAchievementsChartVO\":{\"names\":[\"谢卉瑜\",\"王洪洋\",\"刘宏伟\",\"李冰阳\",\"陈小珂\",\"李宏伟\",\"邓程鹏\",\"温泉\"],\"firstQuarter\":[69,79,61,78,85,66,82,80],\"secondQuarter\":[87,71,74,82,86,89,63,65],\"thirdQuarter\":[78,60,75,74,88,61,65,72],\"fourthQuarter\":[80,80,85,60,81,79,65,63],\"aveQuarter\":[78,72,73,73,85,73,68,70]},\"bugDensity\":[[25.0,80.0,0.8],[45.0,125.0,0.5],[60.0,80.0,0.35],[85.0,110.0,0.15]],\"financeChart\":{\"income\":[585,333,783,459,792,632,828,621,783,360,640,248],\"profit\":[105,157,175,443,408,192,572,133,575,315,310,176],\"expenses\":[480,176,608,416,384,540,256,488,208,245,630,372]},\"memberStatusVO\":{\"0\":[{\"type\":0,\"name\":\"谢卉瑜\",\"position\":\"\",\"taskNum\":24,\"completeTaskNum\":9,\"mark\":\"37%\",\"statistics\":\"9/24\"},{\"type\":0,\"name\":\"王洪洋\",\"position\":\"项目管理\",\"taskNum\":31,\"completeTaskNum\":6,\"mark\":\"19%\",\"statistics\":\"6/31\"},{\"type\":0,\"name\":\"刘宏伟\",\"position\":\"项目经理\",\"taskNum\":24,\"completeTaskNum\":15,\"mark\":\"62%\",\"statistics\":\"15/24\"},{\"type\":0,\"name\":\"李冰阳\",\"position\":\"UI设计师\",\"taskNum\":35,\"completeTaskNum\":7,\"mark\":\"20%\",\"statistics\":\"7/35\"},{\"type\":0,\"name\":\"陈小珂\",\"position\":\"测试工程师\",\"taskNum\":17,\"completeTaskNum\":16,\"mark\":\"94%\",\"statistics\":\"16/17\"},{\"type\":0,\"name\":\"李宏伟\",\"position\":\"开发工程师\",\"taskNum\":27,\"completeTaskNum\":14,\"mark\":\"51%\",\"statistics\":\"14/27\"},{\"type\":0,\"name\":\"邓程鹏\",\"position\":\"产品经理\",\"taskNum\":42,\"completeTaskNum\":8,\"mark\":\"19%\",\"statistics\":\"8/42\"},{\"type\":0,\"name\":\"温泉\",\"position\":\"前端工程师\",\"taskNum\":32,\"completeTaskNum\":6,\"mark\":\"18%\",\"statistics\":\"6/32\"}],\"1\":[{\"type\":1,\"name\":\"谢卉瑜\",\"position\":\"前端工程师\",\"taskNum\":43,\"completeTaskNum\":14,\"mark\":\"32%\",\"statistics\":\"14/43\"},{\"type\":1,\"name\":\"王洪洋\",\"position\":\"项目管理\",\"taskNum\":32,\"completeTaskNum\":13,\"mark\":\"40%\",\"statistics\":\"13/32\"},{\"type\":1,\"name\":\"刘宏伟\",\"position\":\"项目经理\",\"taskNum\":25,\"completeTaskNum\":15,\"mark\":\"60%\",\"statistics\":\"15/25\"},{\"type\":1,\"name\":\"李冰阳\",\"position\":\"UI设计师\",\"taskNum\":44,\"completeTaskNum\":18,\"mark\":\"40%\",\"statistics\":\"18/44\"},{\"type\":1,\"name\":\"陈小珂\",\"position\":\"测试工程师\",\"taskNum\":19,\"completeTaskNum\":10,\"mark\":\"52%\",\"statistics\":\"10/19\"},{\"type\":1,\"name\":\"李宏伟\",\"position\":\"开发工程师\",\"taskNum\":36,\"completeTaskNum\":8,\"mark\":\"22%\",\"statistics\":\"8/36\"},{\"type\":1,\"name\":\"邓程鹏\",\"position\":\"产品经理\",\"taskNum\":25,\"completeTaskNum\":12,\"mark\":\"48%\",\"statistics\":\"12/25\"},{\"type\":1,\"name\":\"温泉\",\"position\":\"前端工程师\",\"taskNum\":41,\"completeTaskNum\":17,\"mark\":\"41%\",\"statistics\":\"17/41\"}],\"2\":[{\"type\":2,\"name\":\"谢卉瑜\",\"position\":\"前端工程师\",\"taskNum\":42,\"completeTaskNum\":19,\"mark\":\"45%\",\"statistics\":\"19/42\"},{\"type\":2,\"name\":\"王洪洋\",\"position\":\"项目管理\",\"taskNum\":39,\"completeTaskNum\":6,\"mark\":\"15%\",\"statistics\":\"6/39\"},{\"type\":2,\"name\":\"刘宏伟\",\"position\":\"项目经理\",\"taskNum\":22,\"completeTaskNum\":5,\"mark\":\"22%\",\"statistics\":\"5/22\"},{\"type\":2,\"name\":\"李冰阳\",\"position\":\"UI设计师\",\"taskNum\":28,\"completeTaskNum\":5,\"mark\":\"17%\",\"statistics\":\"5/28\"},{\"type\":2,\"name\":\"陈小珂\",\"position\":\"测试工程师\",\"taskNum\":29,\"completeTaskNum\":18,\"mark\":\"62%\",\"statistics\":\"18/29\"},{\"type\":2,\"name\":\"李宏伟\",\"position\":\"开发工程师\",\"taskNum\":32,\"completeTaskNum\":14,\"mark\":\"43%\",\"statistics\":\"14/32\"},{\"type\":2,\"name\":\"邓程鹏\",\"position\":\"产品经理\",\"taskNum\":25,\"completeTaskNum\":5,\"mark\":\"20%\",\"statistics\":\"5/25\"},{\"type\":2,\"name\":\"温泉\",\"position\":\"前端工程师\",\"taskNum\":24,\"completeTaskNum\":13,\"mark\":\"54%\",\"statistics\":\"13/24\"}],\"3\":[{\"type\":3,\"name\":\"谢卉瑜\",\"position\":\"前端工程师\",\"taskNum\":28,\"completeTaskNum\":16,\"mark\":\"57%\",\"statistics\":\"16/28\"},{\"type\":3,\"name\":\"王洪洋\",\"position\":\"项目管理\",\"taskNum\":43,\"completeTaskNum\":19,\"mark\":\"44%\",\"statistics\":\"19/43\"},{\"type\":3,\"name\":\"刘宏伟\",\"position\":\"项目经理\",\"taskNum\":24,\"completeTaskNum\":18,\"mark\":\"75%\",\"statistics\":\"18/24\"},{\"type\":3,\"name\":\"李冰阳\",\"position\":\"UI设计师\",\"taskNum\":41,\"completeTaskNum\":19,\"mark\":\"46%\",\"statistics\":\"19/41\"},{\"type\":3,\"name\":\"陈小珂\",\"position\":\"测试工程师\",\"taskNum\":34,\"completeTaskNum\":11,\"mark\":\"32%\",\"statistics\":\"11/34\"},{\"type\":3,\"name\":\"李宏伟\",\"position\":\"开发工程师\",\"taskNum\":42,\"completeTaskNum\":19,\"mark\":\"45%\",\"statistics\":\"19/42\"},{\"type\":3,\"name\":\"邓程鹏\",\"position\":\"产品经理\",\"taskNum\":26,\"completeTaskNum\":6,\"mark\":\"23%\",\"statistics\":\"6/26\"},{\"type\":3,\"name\":\"温泉\",\"position\":\"前端工程师\",\"taskNum\":22,\"completeTaskNum\":18,\"mark\":\"81%\",\"statistics\":\"18/22\"}],\"4\":[{\"type\":4,\"name\":\"谢卉瑜\",\"position\":\"前端工程师\",\"taskNum\":27,\"completeTaskNum\":19,\"mark\":\"70%\",\"statistics\":\"19/27\"},{\"type\":4,\"name\":\"王洪洋\",\"position\":\"项目管理\",\"taskNum\":27,\"completeTaskNum\":17,\"mark\":\"62%\",\"statistics\":\"17/27\"},{\"type\":4,\"name\":\"刘宏伟\",\"position\":\"项目经理\",\"taskNum\":40,\"completeTaskNum\":17,\"mark\":\"43%\",\"statistics\":\"17/40\"},{\"type\":4,\"name\":\"李冰阳\",\"position\":\"UI设计师\",\"taskNum\":30,\"completeTaskNum\":15,\"mark\":\"50%\",\"statistics\":\"15/30\"},{\"type\":4,\"name\":\"陈小珂\",\"position\":\"测试工程师\",\"taskNum\":20,\"completeTaskNum\":11,\"mark\":\"55%\",\"statistics\":\"11/20\"},{\"type\":4,\"name\":\"李宏伟\",\"position\":\"开发工程师\",\"taskNum\":25,\"completeTaskNum\":19,\"mark\":\"76%\",\"statistics\":\"19/25\"},{\"type\":4,\"name\":\"邓程鹏\",\"position\":\"产品经理\",\"taskNum\":33,\"completeTaskNum\":19,\"mark\":\"57%\",\"statistics\":\"19/33\"},{\"type\":4,\"name\":\"温泉\",\"position\":\"前端工程师\",\"taskNum\":40,\"completeTaskNum\":6,\"mark\":\"15%\",\"statistics\":\"6/40\"}],\"5\":[{\"type\":5,\"name\":\"谢卉瑜\",\"position\":\"前端工程师\",\"taskNum\":20,\"completeTaskNum\":14,\"mark\":\"70%\",\"statistics\":\"14/20\"},{\"type\":5,\"name\":\"王洪洋\",\"position\":\"项目管理\",\"taskNum\":38,\"completeTaskNum\":14,\"mark\":\"36%\",\"statistics\":\"14/38\"},{\"type\":5,\"name\":\"刘宏伟\",\"position\":\"项目经理\",\"taskNum\":23,\"completeTaskNum\":14,\"mark\":\"60%\",\"statistics\":\"14/23\"},{\"type\":5,\"name\":\"李冰阳\",\"position\":\"UI设计师\",\"taskNum\":44,\"completeTaskNum\":5,\"mark\":\"11%\",\"statistics\":\"5/44\"},{\"type\":5,\"name\":\"陈小珂\",\"position\":\"测试工程师\",\"taskNum\":37,\"completeTaskNum\":17,\"mark\":\"45%\",\"statistics\":\"17/37\"},{\"type\":5,\"name\":\"李宏伟\",\"position\":\"开发工程师\",\"taskNum\":35,\"completeTaskNum\":6,\"mark\":\"17%\",\"statistics\":\"6/35\"},{\"type\":5,\"name\":\"邓程鹏\",\"position\":\"产品经理\",\"taskNum\":25,\"completeTaskNum\":6,\"mark\":\"24%\",\"statistics\":\"6/25\"},{\"type\":5,\"name\":\"温泉\",\"position\":\"前端工程师\",\"taskNum\":24,\"completeTaskNum\":19,\"mark\":\"79%\",\"statistics\":\"19/24\"}],\"6\":[{\"type\":6,\"name\":\"谢卉瑜\",\"position\":\"前端工程师\",\"taskNum\":27,\"completeTaskNum\":15,\"mark\":\"55%\",\"statistics\":\"15/27\"},{\"type\":6,\"name\":\"王洪洋\",\"position\":\"项目管理\",\"taskNum\":44,\"completeTaskNum\":19,\"mark\":\"43%\",\"statistics\":\"19/44\"},{\"type\":6,\"name\":\"刘宏伟\",\"position\":\"项目经理\",\"taskNum\":43,\"completeTaskNum\":7,\"mark\":\"16%\",\"statistics\":\"7/43\"},{\"type\":6,\"name\":\"李冰阳\",\"position\":\"UI设计师\",\"taskNum\":23,\"completeTaskNum\":16,\"mark\":\"69%\",\"statistics\":\"16/23\"},{\"type\":6,\"name\":\"陈小珂\",\"position\":\"测试工程师\",\"taskNum\":27,\"completeTaskNum\":9,\"mark\":\"33%\",\"statistics\":\"9/27\"},{\"type\":6,\"name\":\"李宏伟\",\"position\":\"开发工程师\",\"taskNum\":21,\"completeTaskNum\":7,\"mark\":\"33%\",\"statistics\":\"7/21\"},{\"type\":6,\"name\":\"邓程鹏\",\"position\":\"产品经理\",\"taskNum\":24,\"completeTaskNum\":12,\"mark\":\"50%\",\"statistics\":\"12/24\"},{\"type\":6,\"name\":\"温泉\",\"position\":\"前端工程师\",\"taskNum\":30,\"completeTaskNum\":9,\"mark\":\"30%\",\"statistics\":\"9/30\"}]},\"isNew\":\"false\"}\n";
        String data1 = "{\"id\":\"\",\"time\":[\"2019/02/1\",\"2019/02/2\",\"2019/02/3\",\"2019/02/4\",\"2019/02/5\",\"2019/02/6\",\"2019/02/7\",\"2019/02/8\",\"2019/02/9\",\"2019/02/10\",\"2019/02/11\",\"2019/02/12\",\"2019/02/13\",\"2019/02/14\",\"2019/02/15\",\"2019/02/16\",\"2019/02/17\",\"2019/02/18\",\"2019/02/19\",\"2019/02/20\",\"2019/02/21\",\"2019/02/22\",\"2019/02/23\",\"2019/02/24\",\"2019/02/25\",\"2019/02/26\",\"2019/02/27\",\"2019/02/28\"],\"completeWorkTime\":[23,39,29,34,67,44,80,79,89,19],\"executeWorkTime\":[308,483,109,100,347,149,182,41,436,150],\"laveWorkTime\":[553,569,518,621,386,862,710,75,531,66],\"memberWorkTimeMap\":{\"赵帅\":{\"已完成\":15,\"剩余\":6},\"任虹雨\":{\"已完成\":21,\"剩余\":13},\"何绍清\":{\"已完成\":21,\"剩余\":9},\"陈小珂\":{\"已完成\":19,\"剩余\":13},\"宋忠让\":{\"已完成\":7,\"剩余\":7},\"孙锌\":{\"已完成\":20,\"剩余\":9},\"邓程鹏\":{\"已完成\":18,\"剩余\":10},\"孙一焱\":{\"已完成\":16,\"剩余\":14}},\"projectAchievementsChartVO\":{\"names\":[\"宋忠让\",\"孙一焱\",\"邓程鹏\",\"何绍清\",\"任虹雨\",\"赵帅\",\"陈小珂\",\"孙锌\"],\"firstQuarter\":[84,70,80,64,77,89,83,68],\"secondQuarter\":[75,76,70,84,67,69,68,83],\"thirdQuarter\":[82,80,67,68,64,71,70,63],\"fourthQuarter\":[61,66,68,77,73,85,75,69],\"aveQuarter\":[75,73,71,73,70,78,74,70]},\"bugDensity\":[[25.0,80.0,0.8],[45.0,125.0,0.5],[60.0,80.0,0.35],[85.0,110.0,0.15]],\"financeChart\":{\"income\":[738,873,693,423,873,693,459,368,336,666,456,792],\"profit\":[594,641,229,165,225,621,207,344,318,410,226,558],\"expenses\":[144,232,464,408,648,372,352,324,218,256,450,234]},\"memberStatusVO\":{\"0\":[{\"type\":0,\"name\":\"宋忠让\",\"position\":\"\",\"taskNum\":41,\"completeTaskNum\":10,\"mark\":\"24%\",\"statistics\":\"10/41\"},{\"type\":0,\"name\":\"孙一焱\",\"position\":\"项目管理\",\"taskNum\":26,\"completeTaskNum\":15,\"mark\":\"57%\",\"statistics\":\"15/26\"},{\"type\":0,\"name\":\"邓程鹏\",\"position\":\"项目经理\",\"taskNum\":19,\"completeTaskNum\":18,\"mark\":\"94%\",\"statistics\":\"18/19\"},{\"type\":0,\"name\":\"何绍清\",\"position\":\"UI设计师\",\"taskNum\":20,\"completeTaskNum\":17,\"mark\":\"85%\",\"statistics\":\"17/20\"},{\"type\":0,\"name\":\"任虹雨\",\"position\":\"测试工程师\",\"taskNum\":19,\"completeTaskNum\":13,\"mark\":\"68%\",\"statistics\":\"13/19\"},{\"type\":0,\"name\":\"赵帅\",\"position\":\"开发工程师\",\"taskNum\":19,\"completeTaskNum\":7,\"mark\":\"36%\",\"statistics\":\"7/19\"},{\"type\":0,\"name\":\"陈小珂\",\"position\":\"产品经理\",\"taskNum\":31,\"completeTaskNum\":17,\"mark\":\"54%\",\"statistics\":\"17/31\"},{\"type\":0,\"name\":\"孙锌\",\"position\":\"前端工程师\",\"taskNum\":36,\"completeTaskNum\":5,\"mark\":\"13%\",\"statistics\":\"5/36\"}],\"1\":[{\"type\":1,\"name\":\"宋忠让\",\"position\":\"前端工程师\",\"taskNum\":25,\"completeTaskNum\":11,\"mark\":\"44%\",\"statistics\":\"11/25\"},{\"type\":1,\"name\":\"孙一焱\",\"position\":\"项目管理\",\"taskNum\":33,\"completeTaskNum\":15,\"mark\":\"45%\",\"statistics\":\"15/33\"},{\"type\":1,\"name\":\"邓程鹏\",\"position\":\"项目经理\",\"taskNum\":18,\"completeTaskNum\":9,\"mark\":\"50%\",\"statistics\":\"9/18\"},{\"type\":1,\"name\":\"何绍清\",\"position\":\"UI设计师\",\"taskNum\":22,\"completeTaskNum\":17,\"mark\":\"77%\",\"statistics\":\"17/22\"},{\"type\":1,\"name\":\"任虹雨\",\"position\":\"测试工程师\",\"taskNum\":35,\"completeTaskNum\":14,\"mark\":\"40%\",\"statistics\":\"14/35\"},{\"type\":1,\"name\":\"赵帅\",\"position\":\"开发工程师\",\"taskNum\":15,\"completeTaskNum\":15,\"mark\":\"100%\",\"statistics\":\"15/15\"},{\"type\":1,\"name\":\"陈小珂\",\"position\":\"产品经理\",\"taskNum\":34,\"completeTaskNum\":10,\"mark\":\"29%\",\"statistics\":\"10/34\"},{\"type\":1,\"name\":\"孙锌\",\"position\":\"前端工程师\",\"taskNum\":26,\"completeTaskNum\":18,\"mark\":\"69%\",\"statistics\":\"18/26\"}],\"2\":[{\"type\":2,\"name\":\"宋忠让\",\"position\":\"前端工程师\",\"taskNum\":28,\"completeTaskNum\":9,\"mark\":\"32%\",\"statistics\":\"9/28\"},{\"type\":2,\"name\":\"孙一焱\",\"position\":\"项目管理\",\"taskNum\":25,\"completeTaskNum\":5,\"mark\":\"20%\",\"statistics\":\"5/25\"},{\"type\":2,\"name\":\"邓程鹏\",\"position\":\"项目经理\",\"taskNum\":37,\"completeTaskNum\":18,\"mark\":\"48%\",\"statistics\":\"18/37\"},{\"type\":2,\"name\":\"何绍清\",\"position\":\"UI设计师\",\"taskNum\":34,\"completeTaskNum\":7,\"mark\":\"20%\",\"statistics\":\"7/34\"},{\"type\":2,\"name\":\"任虹雨\",\"position\":\"测试工程师\",\"taskNum\":24,\"completeTaskNum\":13,\"mark\":\"54%\",\"statistics\":\"13/24\"},{\"type\":2,\"name\":\"赵帅\",\"position\":\"开发工程师\",\"taskNum\":18,\"completeTaskNum\":18,\"mark\":\"100%\",\"statistics\":\"18/18\"},{\"type\":2,\"name\":\"陈小珂\",\"position\":\"产品经理\",\"taskNum\":23,\"completeTaskNum\":9,\"mark\":\"39%\",\"statistics\":\"9/23\"},{\"type\":2,\"name\":\"孙锌\",\"position\":\"前端工程师\",\"taskNum\":25,\"completeTaskNum\":17,\"mark\":\"68%\",\"statistics\":\"17/25\"}],\"3\":[{\"type\":3,\"name\":\"宋忠让\",\"position\":\"前端工程师\",\"taskNum\":35,\"completeTaskNum\":19,\"mark\":\"54%\",\"statistics\":\"19/35\"},{\"type\":3,\"name\":\"孙一焱\",\"position\":\"项目管理\",\"taskNum\":20,\"completeTaskNum\":17,\"mark\":\"85%\",\"statistics\":\"17/20\"},{\"type\":3,\"name\":\"邓程鹏\",\"position\":\"项目经理\",\"taskNum\":20,\"completeTaskNum\":18,\"mark\":\"90%\",\"statistics\":\"18/20\"},{\"type\":3,\"name\":\"何绍清\",\"position\":\"UI设计师\",\"taskNum\":22,\"completeTaskNum\":12,\"mark\":\"54%\",\"statistics\":\"12/22\"},{\"type\":3,\"name\":\"任虹雨\",\"position\":\"测试工程师\",\"taskNum\":15,\"completeTaskNum\":6,\"mark\":\"40%\",\"statistics\":\"6/15\"},{\"type\":3,\"name\":\"赵帅\",\"position\":\"开发工程师\",\"taskNum\":18,\"completeTaskNum\":19,\"mark\":\"105%\",\"statistics\":\"19/18\"},{\"type\":3,\"name\":\"陈小珂\",\"position\":\"产品经理\",\"taskNum\":39,\"completeTaskNum\":6,\"mark\":\"15%\",\"statistics\":\"6/39\"},{\"type\":3,\"name\":\"孙锌\",\"position\":\"前端工程师\",\"taskNum\":38,\"completeTaskNum\":6,\"mark\":\"15%\",\"statistics\":\"6/38\"}],\"4\":[{\"type\":4,\"name\":\"宋忠让\",\"position\":\"前端工程师\",\"taskNum\":21,\"completeTaskNum\":8,\"mark\":\"38%\",\"statistics\":\"8/21\"},{\"type\":4,\"name\":\"孙一焱\",\"position\":\"项目管理\",\"taskNum\":40,\"completeTaskNum\":15,\"mark\":\"37%\",\"statistics\":\"15/40\"},{\"type\":4,\"name\":\"邓程鹏\",\"position\":\"项目经理\",\"taskNum\":34,\"completeTaskNum\":18,\"mark\":\"52%\",\"statistics\":\"18/34\"},{\"type\":4,\"name\":\"何绍清\",\"position\":\"UI设计师\",\"taskNum\":36,\"completeTaskNum\":17,\"mark\":\"47%\",\"statistics\":\"17/36\"},{\"type\":4,\"name\":\"任虹雨\",\"position\":\"测试工程师\",\"taskNum\":16,\"completeTaskNum\":9,\"mark\":\"56%\",\"statistics\":\"9/16\"},{\"type\":4,\"name\":\"赵帅\",\"position\":\"开发工程师\",\"taskNum\":28,\"completeTaskNum\":6,\"mark\":\"21%\",\"statistics\":\"6/28\"},{\"type\":4,\"name\":\"陈小珂\",\"position\":\"产品经理\",\"taskNum\":18,\"completeTaskNum\":15,\"mark\":\"83%\",\"statistics\":\"15/18\"},{\"type\":4,\"name\":\"孙锌\",\"position\":\"前端工程师\",\"taskNum\":21,\"completeTaskNum\":13,\"mark\":\"61%\",\"statistics\":\"13/21\"}],\"5\":[{\"type\":5,\"name\":\"宋忠让\",\"position\":\"前端工程师\",\"taskNum\":17,\"completeTaskNum\":18,\"mark\":\"105%\",\"statistics\":\"18/17\"},{\"type\":5,\"name\":\"孙一焱\",\"position\":\"项目管理\",\"taskNum\":21,\"completeTaskNum\":14,\"mark\":\"66%\",\"statistics\":\"14/21\"},{\"type\":5,\"name\":\"邓程鹏\",\"position\":\"项目经理\",\"taskNum\":31,\"completeTaskNum\":13,\"mark\":\"41%\",\"statistics\":\"13/31\"},{\"type\":5,\"name\":\"何绍清\",\"position\":\"UI设计师\",\"taskNum\":31,\"completeTaskNum\":16,\"mark\":\"51%\",\"statistics\":\"16/31\"},{\"type\":5,\"name\":\"任虹雨\",\"position\":\"测试工程师\",\"taskNum\":19,\"completeTaskNum\":5,\"mark\":\"26%\",\"statistics\":\"5/19\"},{\"type\":5,\"name\":\"赵帅\",\"position\":\"开发工程师\",\"taskNum\":42,\"completeTaskNum\":7,\"mark\":\"16%\",\"statistics\":\"7/42\"},{\"type\":5,\"name\":\"陈小珂\",\"position\":\"产品经理\",\"taskNum\":37,\"completeTaskNum\":16,\"mark\":\"43%\",\"statistics\":\"16/37\"},{\"type\":5,\"name\":\"孙锌\",\"position\":\"前端工程师\",\"taskNum\":23,\"completeTaskNum\":10,\"mark\":\"43%\",\"statistics\":\"10/23\"}],\"6\":[{\"type\":6,\"name\":\"宋忠让\",\"position\":\"前端工程师\",\"taskNum\":19,\"completeTaskNum\":13,\"mark\":\"68%\",\"statistics\":\"13/19\"},{\"type\":6,\"name\":\"孙一焱\",\"position\":\"项目管理\",\"taskNum\":23,\"completeTaskNum\":10,\"mark\":\"43%\",\"statistics\":\"10/23\"},{\"type\":6,\"name\":\"邓程鹏\",\"position\":\"项目经理\",\"taskNum\":35,\"completeTaskNum\":9,\"mark\":\"25%\",\"statistics\":\"9/35\"},{\"type\":6,\"name\":\"何绍清\",\"position\":\"UI设计师\",\"taskNum\":30,\"completeTaskNum\":14,\"mark\":\"46%\",\"statistics\":\"14/30\"},{\"type\":6,\"name\":\"任虹雨\",\"position\":\"测试工程师\",\"taskNum\":18,\"completeTaskNum\":12,\"mark\":\"66%\",\"statistics\":\"12/18\"},{\"type\":6,\"name\":\"赵帅\",\"position\":\"开发工程师\",\"taskNum\":38,\"completeTaskNum\":10,\"mark\":\"26%\",\"statistics\":\"10/38\"},{\"type\":6,\"name\":\"陈小珂\",\"position\":\"产品经理\",\"taskNum\":43,\"completeTaskNum\":10,\"mark\":\"23%\",\"statistics\":\"10/43\"},{\"type\":6,\"name\":\"孙锌\",\"position\":\"前端工程师\",\"taskNum\":23,\"completeTaskNum\":12,\"mark\":\"52%\",\"statistics\":\"12/23\"}]},\"isNew\":\"false\"}\n";
        String data2 = "{\"id\":\"\",\"time\":[\"2019/02/1\",\"2019/02/2\",\"2019/02/3\",\"2019/02/4\",\"2019/02/5\",\"2019/02/6\",\"2019/02/7\",\"2019/02/8\",\"2019/02/9\",\"2019/02/10\",\"2019/02/11\",\"2019/02/12\",\"2019/02/13\",\"2019/02/14\",\"2019/02/15\",\"2019/02/16\",\"2019/02/17\",\"2019/02/18\",\"2019/02/19\",\"2019/02/20\",\"2019/02/21\",\"2019/02/22\",\"2019/02/23\",\"2019/02/24\",\"2019/02/25\",\"2019/02/26\",\"2019/02/27\",\"2019/02/28\"],\"completeWorkTime\":[52,88,1,35,14,22,19,59,58,31],\"executeWorkTime\":[442,272,459,445,195,429,349,243,240,165],\"laveWorkTime\":[199,884,806,718,227,666,67,531,796,475],\"memberWorkTimeMap\":{\"林盛海\":{\"已完成\":22,\"剩余\":14},\"王磊\":{\"已完成\":20,\"剩余\":11},\"康凯\":{\"已完成\":7,\"剩余\":12},\"何绍清\":{\"已完成\":14,\"剩余\":8},\"李瑞秋\":{\"已完成\":6,\"剩余\":8},\"任攀\":{\"已完成\":7,\"剩余\":11},\"温泉\":{\"已完成\":13,\"剩余\":9},\"吴建伟\":{\"已完成\":24,\"剩余\":5}},\"projectAchievementsChartVO\":{\"names\":[\"王磊\",\"何绍清\",\"林盛海\",\"温泉\",\"李瑞秋\",\"康凯\",\"任攀\",\"吴建伟\"],\"firstQuarter\":[78,76,68,76,72,81,65,69],\"secondQuarter\":[87,72,63,73,70,65,75,71],\"thirdQuarter\":[70,62,67,89,85,75,67,86],\"fourthQuarter\":[80,60,66,88,89,73,65,81],\"aveQuarter\":[78,67,66,81,79,73,68,76]},\"bugDensity\":[[25.0,80.0,0.8],[45.0,125.0,0.5],[60.0,80.0,0.35],[85.0,110.0,0.15]],\"financeChart\":{\"income\":[664,774,272,648,280,711,765,846,648,520,765,464],\"profit\":[358,294,164,405,55,131,445,230,198,169,133,259],\"expenses\":[306,480,168,243,225,680,320,616,540,351,632,405]},\"memberStatusVO\":{\"0\":[{\"type\":0,\"name\":\"王磊\",\"position\":\"\",\"taskNum\":40,\"completeTaskNum\":8,\"mark\":\"20%\",\"statistics\":\"8/40\"},{\"type\":0,\"name\":\"何绍清\",\"position\":\"项目管理\",\"taskNum\":38,\"completeTaskNum\":12,\"mark\":\"31%\",\"statistics\":\"12/38\"},{\"type\":0,\"name\":\"林盛海\",\"position\":\"项目经理\",\"taskNum\":36,\"completeTaskNum\":8,\"mark\":\"22%\",\"statistics\":\"8/36\"},{\"type\":0,\"name\":\"温泉\",\"position\":\"UI设计师\",\"taskNum\":41,\"completeTaskNum\":11,\"mark\":\"26%\",\"statistics\":\"11/41\"},{\"type\":0,\"name\":\"李瑞秋\",\"position\":\"测试工程师\",\"taskNum\":26,\"completeTaskNum\":19,\"mark\":\"73%\",\"statistics\":\"19/26\"},{\"type\":0,\"name\":\"康凯\",\"position\":\"开发工程师\",\"taskNum\":20,\"completeTaskNum\":6,\"mark\":\"30%\",\"statistics\":\"6/20\"},{\"type\":0,\"name\":\"任攀\",\"position\":\"产品经理\",\"taskNum\":18,\"completeTaskNum\":5,\"mark\":\"27%\",\"statistics\":\"5/18\"},{\"type\":0,\"name\":\"吴建伟\",\"position\":\"前端工程师\",\"taskNum\":34,\"completeTaskNum\":13,\"mark\":\"38%\",\"statistics\":\"13/34\"}],\"1\":[{\"type\":1,\"name\":\"王磊\",\"position\":\"前端工程师\",\"taskNum\":28,\"completeTaskNum\":17,\"mark\":\"60%\",\"statistics\":\"17/28\"},{\"type\":1,\"name\":\"何绍清\",\"position\":\"项目管理\",\"taskNum\":19,\"completeTaskNum\":6,\"mark\":\"31%\",\"statistics\":\"6/19\"},{\"type\":1,\"name\":\"林盛海\",\"position\":\"项目经理\",\"taskNum\":37,\"completeTaskNum\":11,\"mark\":\"29%\",\"statistics\":\"11/37\"},{\"type\":1,\"name\":\"温泉\",\"position\":\"UI设计师\",\"taskNum\":33,\"completeTaskNum\":18,\"mark\":\"54%\",\"statistics\":\"18/33\"},{\"type\":1,\"name\":\"李瑞秋\",\"position\":\"测试工程师\",\"taskNum\":42,\"completeTaskNum\":17,\"mark\":\"40%\",\"statistics\":\"17/42\"},{\"type\":1,\"name\":\"康凯\",\"position\":\"开发工程师\",\"taskNum\":44,\"completeTaskNum\":10,\"mark\":\"22%\",\"statistics\":\"10/44\"},{\"type\":1,\"name\":\"任攀\",\"position\":\"产品经理\",\"taskNum\":44,\"completeTaskNum\":5,\"mark\":\"11%\",\"statistics\":\"5/44\"},{\"type\":1,\"name\":\"吴建伟\",\"position\":\"前端工程师\",\"taskNum\":35,\"completeTaskNum\":12,\"mark\":\"34%\",\"statistics\":\"12/35\"}],\"2\":[{\"type\":2,\"name\":\"王磊\",\"position\":\"前端工程师\",\"taskNum\":16,\"completeTaskNum\":11,\"mark\":\"68%\",\"statistics\":\"11/16\"},{\"type\":2,\"name\":\"何绍清\",\"position\":\"项目管理\",\"taskNum\":18,\"completeTaskNum\":13,\"mark\":\"72%\",\"statistics\":\"13/18\"},{\"type\":2,\"name\":\"林盛海\",\"position\":\"项目经理\",\"taskNum\":16,\"completeTaskNum\":11,\"mark\":\"68%\",\"statistics\":\"11/16\"},{\"type\":2,\"name\":\"温泉\",\"position\":\"UI设计师\",\"taskNum\":21,\"completeTaskNum\":10,\"mark\":\"47%\",\"statistics\":\"10/21\"},{\"type\":2,\"name\":\"李瑞秋\",\"position\":\"测试工程师\",\"taskNum\":35,\"completeTaskNum\":14,\"mark\":\"40%\",\"statistics\":\"14/35\"},{\"type\":2,\"name\":\"康凯\",\"position\":\"开发工程师\",\"taskNum\":39,\"completeTaskNum\":10,\"mark\":\"25%\",\"statistics\":\"10/39\"},{\"type\":2,\"name\":\"任攀\",\"position\":\"产品经理\",\"taskNum\":44,\"completeTaskNum\":12,\"mark\":\"27%\",\"statistics\":\"12/44\"},{\"type\":2,\"name\":\"吴建伟\",\"position\":\"前端工程师\",\"taskNum\":32,\"completeTaskNum\":5,\"mark\":\"15%\",\"statistics\":\"5/32\"}],\"3\":[{\"type\":3,\"name\":\"王磊\",\"position\":\"前端工程师\",\"taskNum\":32,\"completeTaskNum\":13,\"mark\":\"40%\",\"statistics\":\"13/32\"},{\"type\":3,\"name\":\"何绍清\",\"position\":\"项目管理\",\"taskNum\":21,\"completeTaskNum\":9,\"mark\":\"42%\",\"statistics\":\"9/21\"},{\"type\":3,\"name\":\"林盛海\",\"position\":\"项目经理\",\"taskNum\":22,\"completeTaskNum\":11,\"mark\":\"50%\",\"statistics\":\"11/22\"},{\"type\":3,\"name\":\"温泉\",\"position\":\"UI设计师\",\"taskNum\":17,\"completeTaskNum\":16,\"mark\":\"94%\",\"statistics\":\"16/17\"},{\"type\":3,\"name\":\"李瑞秋\",\"position\":\"测试工程师\",\"taskNum\":39,\"completeTaskNum\":19,\"mark\":\"48%\",\"statistics\":\"19/39\"},{\"type\":3,\"name\":\"康凯\",\"position\":\"开发工程师\",\"taskNum\":31,\"completeTaskNum\":18,\"mark\":\"58%\",\"statistics\":\"18/31\"},{\"type\":3,\"name\":\"任攀\",\"position\":\"产品经理\",\"taskNum\":17,\"completeTaskNum\":7,\"mark\":\"41%\",\"statistics\":\"7/17\"},{\"type\":3,\"name\":\"吴建伟\",\"position\":\"前端工程师\",\"taskNum\":41,\"completeTaskNum\":15,\"mark\":\"36%\",\"statistics\":\"15/41\"}],\"4\":[{\"type\":4,\"name\":\"王磊\",\"position\":\"前端工程师\",\"taskNum\":20,\"completeTaskNum\":10,\"mark\":\"50%\",\"statistics\":\"10/20\"},{\"type\":4,\"name\":\"何绍清\",\"position\":\"项目管理\",\"taskNum\":41,\"completeTaskNum\":12,\"mark\":\"29%\",\"statistics\":\"12/41\"},{\"type\":4,\"name\":\"林盛海\",\"position\":\"项目经理\",\"taskNum\":41,\"completeTaskNum\":8,\"mark\":\"19%\",\"statistics\":\"8/41\"},{\"type\":4,\"name\":\"温泉\",\"position\":\"UI设计师\",\"taskNum\":19,\"completeTaskNum\":10,\"mark\":\"52%\",\"statistics\":\"10/19\"},{\"type\":4,\"name\":\"李瑞秋\",\"position\":\"测试工程师\",\"taskNum\":26,\"completeTaskNum\":7,\"mark\":\"26%\",\"statistics\":\"7/26\"},{\"type\":4,\"name\":\"康凯\",\"position\":\"开发工程师\",\"taskNum\":31,\"completeTaskNum\":19,\"mark\":\"61%\",\"statistics\":\"19/31\"},{\"type\":4,\"name\":\"任攀\",\"position\":\"产品经理\",\"taskNum\":15,\"completeTaskNum\":12,\"mark\":\"80%\",\"statistics\":\"12/15\"},{\"type\":4,\"name\":\"吴建伟\",\"position\":\"前端工程师\",\"taskNum\":31,\"completeTaskNum\":6,\"mark\":\"19%\",\"statistics\":\"6/31\"}],\"5\":[{\"type\":5,\"name\":\"王磊\",\"position\":\"前端工程师\",\"taskNum\":37,\"completeTaskNum\":12,\"mark\":\"32%\",\"statistics\":\"12/37\"},{\"type\":5,\"name\":\"何绍清\",\"position\":\"项目管理\",\"taskNum\":38,\"completeTaskNum\":11,\"mark\":\"28%\",\"statistics\":\"11/38\"},{\"type\":5,\"name\":\"林盛海\",\"position\":\"项目经理\",\"taskNum\":24,\"completeTaskNum\":12,\"mark\":\"50%\",\"statistics\":\"12/24\"},{\"type\":5,\"name\":\"温泉\",\"position\":\"UI设计师\",\"taskNum\":19,\"completeTaskNum\":5,\"mark\":\"26%\",\"statistics\":\"5/19\"},{\"type\":5,\"name\":\"李瑞秋\",\"position\":\"测试工程师\",\"taskNum\":33,\"completeTaskNum\":17,\"mark\":\"51%\",\"statistics\":\"17/33\"},{\"type\":5,\"name\":\"康凯\",\"position\":\"开发工程师\",\"taskNum\":44,\"completeTaskNum\":19,\"mark\":\"43%\",\"statistics\":\"19/44\"},{\"type\":5,\"name\":\"任攀\",\"position\":\"产品经理\",\"taskNum\":30,\"completeTaskNum\":10,\"mark\":\"33%\",\"statistics\":\"10/30\"},{\"type\":5,\"name\":\"吴建伟\",\"position\":\"前端工程师\",\"taskNum\":44,\"completeTaskNum\":12,\"mark\":\"27%\",\"statistics\":\"12/44\"}],\"6\":[{\"type\":6,\"name\":\"王磊\",\"position\":\"前端工程师\",\"taskNum\":36,\"completeTaskNum\":8,\"mark\":\"22%\",\"statistics\":\"8/36\"},{\"type\":6,\"name\":\"何绍清\",\"position\":\"项目管理\",\"taskNum\":31,\"completeTaskNum\":10,\"mark\":\"32%\",\"statistics\":\"10/31\"},{\"type\":6,\"name\":\"林盛海\",\"position\":\"项目经理\",\"taskNum\":43,\"completeTaskNum\":7,\"mark\":\"16%\",\"statistics\":\"7/43\"},{\"type\":6,\"name\":\"温泉\",\"position\":\"UI设计师\",\"taskNum\":20,\"completeTaskNum\":9,\"mark\":\"45%\",\"statistics\":\"9/20\"},{\"type\":6,\"name\":\"李瑞秋\",\"position\":\"测试工程师\",\"taskNum\":17,\"completeTaskNum\":9,\"mark\":\"52%\",\"statistics\":\"9/17\"},{\"type\":6,\"name\":\"康凯\",\"position\":\"开发工程师\",\"taskNum\":42,\"completeTaskNum\":9,\"mark\":\"21%\",\"statistics\":\"9/42\"},{\"type\":6,\"name\":\"任攀\",\"position\":\"产品经理\",\"taskNum\":43,\"completeTaskNum\":15,\"mark\":\"34%\",\"statistics\":\"15/43\"},{\"type\":6,\"name\":\"吴建伟\",\"position\":\"前端工程师\",\"taskNum\":15,\"completeTaskNum\":10,\"mark\":\"66%\",\"statistics\":\"10/15\"}]},\"isNew\":\"false\"}\n";
        String data3 = "{\"id\":\"\",\"time\":[\"2019/02/1\",\"2019/02/2\",\"2019/02/3\",\"2019/02/4\",\"2019/02/5\",\"2019/02/6\",\"2019/02/7\",\"2019/02/8\",\"2019/02/9\",\"2019/02/10\",\"2019/02/11\",\"2019/02/12\",\"2019/02/13\",\"2019/02/14\",\"2019/02/15\",\"2019/02/16\",\"2019/02/17\",\"2019/02/18\",\"2019/02/19\",\"2019/02/20\",\"2019/02/21\",\"2019/02/22\",\"2019/02/23\",\"2019/02/24\",\"2019/02/25\",\"2019/02/26\",\"2019/02/27\",\"2019/02/28\"],\"completeWorkTime\":[96,83,19,12,4,26,98,65,59,25],\"executeWorkTime\":[315,235,282,312,321,412,190,113,193,91],\"laveWorkTime\":[557,1,335,162,413,774,36,784,614,20],\"memberWorkTimeMap\":{\"高鑫\":{\"已完成\":19,\"剩余\":14},\"张凯明\":{\"已完成\":11,\"剩余\":14},\"赵帅\":{\"已完成\":7,\"剩余\":12},\"刘勇\":{\"已完成\":15,\"剩余\":10},\"张丹阳\":{\"已完成\":6,\"剩余\":11},\"闻雅\":{\"已完成\":13,\"剩余\":6},\"郭维明\":{\"已完成\":10,\"剩余\":10},\"藏丹丹\":{\"已完成\":8,\"剩余\":6}},\"projectAchievementsChartVO\":{\"names\":[\"刘勇\",\"高鑫\",\"闻雅\",\"赵帅\",\"张丹阳\",\"张凯明\",\"藏丹丹\",\"郭维明\"],\"firstQuarter\":[64,86,69,83,62,73,65,72],\"secondQuarter\":[78,88,64,72,83,73,76,61],\"thirdQuarter\":[83,75,82,70,61,73,79,72],\"fourthQuarter\":[86,79,63,65,70,86,79,61],\"aveQuarter\":[77,82,69,72,69,76,74,66]},\"bugDensity\":[[25.0,80.0,0.8],[45.0,125.0,0.5],[60.0,80.0,0.35],[85.0,110.0,0.15]],\"financeChart\":{\"income\":[297,729,384,760,837,756,800,134,900,828,384,621],\"profit\":[165,481,141,176,141,420,476,277,188,652,249,205],\"expenses\":[232,248,243,684,696,336,324,227,712,176,135,416]},\"memberStatusVO\":{\"0\":[{\"type\":0,\"name\":\"刘勇\",\"position\":\"\",\"taskNum\":22,\"completeTaskNum\":17,\"mark\":\"77%\",\"statistics\":\"17/22\"},{\"type\":0,\"name\":\"高鑫\",\"position\":\"项目管理\",\"taskNum\":24,\"completeTaskNum\":5,\"mark\":\"20%\",\"statistics\":\"5/24\"},{\"type\":0,\"name\":\"闻雅\",\"position\":\"项目经理\",\"taskNum\":24,\"completeTaskNum\":19,\"mark\":\"79%\",\"statistics\":\"19/24\"},{\"type\":0,\"name\":\"赵帅\",\"position\":\"UI设计师\",\"taskNum\":34,\"completeTaskNum\":7,\"mark\":\"20%\",\"statistics\":\"7/34\"},{\"type\":0,\"name\":\"张丹阳\",\"position\":\"测试工程师\",\"taskNum\":34,\"completeTaskNum\":12,\"mark\":\"35%\",\"statistics\":\"12/34\"},{\"type\":0,\"name\":\"张凯明\",\"position\":\"开发工程师\",\"taskNum\":28,\"completeTaskNum\":11,\"mark\":\"39%\",\"statistics\":\"11/28\"},{\"type\":0,\"name\":\"藏丹丹\",\"position\":\"产品经理\",\"taskNum\":43,\"completeTaskNum\":6,\"mark\":\"13%\",\"statistics\":\"6/43\"},{\"type\":0,\"name\":\"郭维明\",\"position\":\"前端工程师\",\"taskNum\":43,\"completeTaskNum\":15,\"mark\":\"34%\",\"statistics\":\"15/43\"}],\"1\":[{\"type\":1,\"name\":\"刘勇\",\"position\":\"前端工程师\",\"taskNum\":21,\"completeTaskNum\":11,\"mark\":\"52%\",\"statistics\":\"11/21\"},{\"type\":1,\"name\":\"高鑫\",\"position\":\"项目管理\",\"taskNum\":40,\"completeTaskNum\":12,\"mark\":\"30%\",\"statistics\":\"12/40\"},{\"type\":1,\"name\":\"闻雅\",\"position\":\"项目经理\",\"taskNum\":36,\"completeTaskNum\":7,\"mark\":\"19%\",\"statistics\":\"7/36\"},{\"type\":1,\"name\":\"赵帅\",\"position\":\"UI设计师\",\"taskNum\":40,\"completeTaskNum\":14,\"mark\":\"35%\",\"statistics\":\"14/40\"},{\"type\":1,\"name\":\"张丹阳\",\"position\":\"测试工程师\",\"taskNum\":31,\"completeTaskNum\":11,\"mark\":\"35%\",\"statistics\":\"11/31\"},{\"type\":1,\"name\":\"张凯明\",\"position\":\"开发工程师\",\"taskNum\":34,\"completeTaskNum\":11,\"mark\":\"32%\",\"statistics\":\"11/34\"},{\"type\":1,\"name\":\"藏丹丹\",\"position\":\"产品经理\",\"taskNum\":18,\"completeTaskNum\":8,\"mark\":\"44%\",\"statistics\":\"8/18\"},{\"type\":1,\"name\":\"郭维明\",\"position\":\"前端工程师\",\"taskNum\":29,\"completeTaskNum\":15,\"mark\":\"51%\",\"statistics\":\"15/29\"}],\"2\":[{\"type\":2,\"name\":\"刘勇\",\"position\":\"前端工程师\",\"taskNum\":29,\"completeTaskNum\":10,\"mark\":\"34%\",\"statistics\":\"10/29\"},{\"type\":2,\"name\":\"高鑫\",\"position\":\"项目管理\",\"taskNum\":24,\"completeTaskNum\":17,\"mark\":\"70%\",\"statistics\":\"17/24\"},{\"type\":2,\"name\":\"闻雅\",\"position\":\"项目经理\",\"taskNum\":16,\"completeTaskNum\":12,\"mark\":\"75%\",\"statistics\":\"12/16\"},{\"type\":2,\"name\":\"赵帅\",\"position\":\"UI设计师\",\"taskNum\":43,\"completeTaskNum\":18,\"mark\":\"41%\",\"statistics\":\"18/43\"},{\"type\":2,\"name\":\"张丹阳\",\"position\":\"测试工程师\",\"taskNum\":37,\"completeTaskNum\":10,\"mark\":\"27%\",\"statistics\":\"10/37\"},{\"type\":2,\"name\":\"张凯明\",\"position\":\"开发工程师\",\"taskNum\":32,\"completeTaskNum\":15,\"mark\":\"46%\",\"statistics\":\"15/32\"},{\"type\":2,\"name\":\"藏丹丹\",\"position\":\"产品经理\",\"taskNum\":34,\"completeTaskNum\":15,\"mark\":\"44%\",\"statistics\":\"15/34\"},{\"type\":2,\"name\":\"郭维明\",\"position\":\"前端工程师\",\"taskNum\":43,\"completeTaskNum\":13,\"mark\":\"30%\",\"statistics\":\"13/43\"}],\"3\":[{\"type\":3,\"name\":\"刘勇\",\"position\":\"前端工程师\",\"taskNum\":36,\"completeTaskNum\":8,\"mark\":\"22%\",\"statistics\":\"8/36\"},{\"type\":3,\"name\":\"高鑫\",\"position\":\"项目管理\",\"taskNum\":21,\"completeTaskNum\":17,\"mark\":\"80%\",\"statistics\":\"17/21\"},{\"type\":3,\"name\":\"闻雅\",\"position\":\"项目经理\",\"taskNum\":27,\"completeTaskNum\":17,\"mark\":\"62%\",\"statistics\":\"17/27\"},{\"type\":3,\"name\":\"赵帅\",\"position\":\"UI设计师\",\"taskNum\":19,\"completeTaskNum\":18,\"mark\":\"94%\",\"statistics\":\"18/19\"},{\"type\":3,\"name\":\"张丹阳\",\"position\":\"测试工程师\",\"taskNum\":38,\"completeTaskNum\":19,\"mark\":\"50%\",\"statistics\":\"19/38\"},{\"type\":3,\"name\":\"张凯明\",\"position\":\"开发工程师\",\"taskNum\":33,\"completeTaskNum\":13,\"mark\":\"39%\",\"statistics\":\"13/33\"},{\"type\":3,\"name\":\"藏丹丹\",\"position\":\"产品经理\",\"taskNum\":29,\"completeTaskNum\":17,\"mark\":\"58%\",\"statistics\":\"17/29\"},{\"type\":3,\"name\":\"郭维明\",\"position\":\"前端工程师\",\"taskNum\":34,\"completeTaskNum\":6,\"mark\":\"17%\",\"statistics\":\"6/34\"}],\"4\":[{\"type\":4,\"name\":\"刘勇\",\"position\":\"前端工程师\",\"taskNum\":15,\"completeTaskNum\":5,\"mark\":\"33%\",\"statistics\":\"5/15\"},{\"type\":4,\"name\":\"高鑫\",\"position\":\"项目管理\",\"taskNum\":41,\"completeTaskNum\":16,\"mark\":\"39%\",\"statistics\":\"16/41\"},{\"type\":4,\"name\":\"闻雅\",\"position\":\"项目经理\",\"taskNum\":29,\"completeTaskNum\":9,\"mark\":\"31%\",\"statistics\":\"9/29\"},{\"type\":4,\"name\":\"赵帅\",\"position\":\"UI设计师\",\"taskNum\":33,\"completeTaskNum\":15,\"mark\":\"45%\",\"statistics\":\"15/33\"},{\"type\":4,\"name\":\"张丹阳\",\"position\":\"测试工程师\",\"taskNum\":41,\"completeTaskNum\":6,\"mark\":\"14%\",\"statistics\":\"6/41\"},{\"type\":4,\"name\":\"张凯明\",\"position\":\"开发工程师\",\"taskNum\":24,\"completeTaskNum\":12,\"mark\":\"50%\",\"statistics\":\"12/24\"},{\"type\":4,\"name\":\"藏丹丹\",\"position\":\"产品经理\",\"taskNum\":34,\"completeTaskNum\":12,\"mark\":\"35%\",\"statistics\":\"12/34\"},{\"type\":4,\"name\":\"郭维明\",\"position\":\"前端工程师\",\"taskNum\":17,\"completeTaskNum\":17,\"mark\":\"100%\",\"statistics\":\"17/17\"}],\"5\":[{\"type\":5,\"name\":\"刘勇\",\"position\":\"前端工程师\",\"taskNum\":23,\"completeTaskNum\":12,\"mark\":\"52%\",\"statistics\":\"12/23\"},{\"type\":5,\"name\":\"高鑫\",\"position\":\"项目管理\",\"taskNum\":35,\"completeTaskNum\":17,\"mark\":\"48%\",\"statistics\":\"17/35\"},{\"type\":5,\"name\":\"闻雅\",\"position\":\"项目经理\",\"taskNum\":21,\"completeTaskNum\":8,\"mark\":\"38%\",\"statistics\":\"8/21\"},{\"type\":5,\"name\":\"赵帅\",\"position\":\"UI设计师\",\"taskNum\":28,\"completeTaskNum\":14,\"mark\":\"50%\",\"statistics\":\"14/28\"},{\"type\":5,\"name\":\"张丹阳\",\"position\":\"测试工程师\",\"taskNum\":28,\"completeTaskNum\":14,\"mark\":\"50%\",\"statistics\":\"14/28\"},{\"type\":5,\"name\":\"张凯明\",\"position\":\"开发工程师\",\"taskNum\":19,\"completeTaskNum\":6,\"mark\":\"31%\",\"statistics\":\"6/19\"},{\"type\":5,\"name\":\"藏丹丹\",\"position\":\"产品经理\",\"taskNum\":29,\"completeTaskNum\":13,\"mark\":\"44%\",\"statistics\":\"13/29\"},{\"type\":5,\"name\":\"郭维明\",\"position\":\"前端工程师\",\"taskNum\":26,\"completeTaskNum\":12,\"mark\":\"46%\",\"statistics\":\"12/26\"}],\"6\":[{\"type\":6,\"name\":\"刘勇\",\"position\":\"前端工程师\",\"taskNum\":34,\"completeTaskNum\":15,\"mark\":\"44%\",\"statistics\":\"15/34\"},{\"type\":6,\"name\":\"高鑫\",\"position\":\"项目管理\",\"taskNum\":15,\"completeTaskNum\":15,\"mark\":\"100%\",\"statistics\":\"15/15\"},{\"type\":6,\"name\":\"闻雅\",\"position\":\"项目经理\",\"taskNum\":24,\"completeTaskNum\":7,\"mark\":\"29%\",\"statistics\":\"7/24\"},{\"type\":6,\"name\":\"赵帅\",\"position\":\"UI设计师\",\"taskNum\":20,\"completeTaskNum\":18,\"mark\":\"90%\",\"statistics\":\"18/20\"},{\"type\":6,\"name\":\"张丹阳\",\"position\":\"测试工程师\",\"taskNum\":15,\"completeTaskNum\":12,\"mark\":\"80%\",\"statistics\":\"12/15\"},{\"type\":6,\"name\":\"张凯明\",\"position\":\"开发工程师\",\"taskNum\":40,\"completeTaskNum\":6,\"mark\":\"15%\",\"statistics\":\"6/40\"},{\"type\":6,\"name\":\"藏丹丹\",\"position\":\"产品经理\",\"taskNum\":34,\"completeTaskNum\":15,\"mark\":\"44%\",\"statistics\":\"15/34\"},{\"type\":6,\"name\":\"郭维明\",\"position\":\"前端工程师\",\"taskNum\":30,\"completeTaskNum\":5,\"mark\":\"16%\",\"statistics\":\"5/30\"}]},\"isNew\":\"true\",\"isNew\":\"false\"}\n";
        String data4 = "{\"id\":\"\",\"time\":[\"2019/02/1\",\"2019/02/2\",\"2019/02/3\",\"2019/02/4\",\"2019/02/5\",\"2019/02/6\",\"2019/02/7\",\"2019/02/8\",\"2019/02/9\",\"2019/02/10\",\"2019/02/11\",\"2019/02/12\",\"2019/02/13\",\"2019/02/14\",\"2019/02/15\",\"2019/02/16\",\"2019/02/17\",\"2019/02/18\",\"2019/02/19\",\"2019/02/20\",\"2019/02/21\",\"2019/02/22\",\"2019/02/23\",\"2019/02/24\",\"2019/02/25\",\"2019/02/26\",\"2019/02/27\",\"2019/02/28\"],\"completeWorkTime\":[65,35,35,92,23,76,63,14,44,26],\"executeWorkTime\":[415,336,201,397,137,483,498,1,327,479],\"laveWorkTime\":[458,66,505,304,399,89,449,741,78,395],\"memberWorkTimeMap\":{\"赵子豪\":{\"已完成\":7,\"剩余\":5},\"赵晓晨\":{\"已完成\":5,\"剩余\":7},\"李康\":{\"已完成\":14,\"剩余\":14},\"张妍\":{\"已完成\":16,\"剩余\":12},\"葛悦凯\":{\"已完成\":9,\"剩余\":13},\"熊迪\":{\"已完成\":15,\"剩余\":5},\"夏琳\":{\"已完成\":10,\"剩余\":7},\"孙天泽\":{\"已完成\":13,\"剩余\":12}},\"projectAchievementsChartVO\":{\"names\":[\"夏琳\",\"赵子豪\",\"孙天泽\",\"赵晓晨\",\"葛悦凯\",\"熊迪\",\"张妍\",\"李康\"],\"firstQuarter\":[64,66,76,60,83,81,81,68],\"secondQuarter\":[84,65,87,84,66,61,62,86],\"thirdQuarter\":[67,64,60,73,60,74,60,69],\"fourthQuarter\":[88,69,68,80,85,65,80,88],\"aveQuarter\":[75,66,72,74,73,70,70,77]},\"bugDensity\":[[25.0,80.0,0.8],[45.0,125.0,0.5],[60.0,80.0,0.35],[85.0,110.0,0.15]],\"financeChart\":{\"income\":[696,496,512,261,612,512,810,864,729,765,315,680],\"profit\":[210,298,296,109,176,224,130,152,185,245,123,653],\"expenses\":[486,198,216,152,536,288,680,712,624,520,192,227]},\"memberStatusVO\":{\"0\":[{\"type\":0,\"name\":\"夏琳\",\"position\":\"\",\"taskNum\":33,\"completeTaskNum\":17,\"mark\":\"51%\",\"statistics\":\"17/33\"},{\"type\":0,\"name\":\"赵子豪\",\"position\":\"项目管理\",\"taskNum\":30,\"completeTaskNum\":10,\"mark\":\"33%\",\"statistics\":\"10/30\"},{\"type\":0,\"name\":\"孙天泽\",\"position\":\"项目经理\",\"taskNum\":20,\"completeTaskNum\":6,\"mark\":\"30%\",\"statistics\":\"6/20\"},{\"type\":0,\"name\":\"赵晓晨\",\"position\":\"UI设计师\",\"taskNum\":26,\"completeTaskNum\":7,\"mark\":\"26%\",\"statistics\":\"7/26\"},{\"type\":0,\"name\":\"葛悦凯\",\"position\":\"测试工程师\",\"taskNum\":17,\"completeTaskNum\":17,\"mark\":\"100%\",\"statistics\":\"17/17\"},{\"type\":0,\"name\":\"熊迪\",\"position\":\"开发工程师\",\"taskNum\":25,\"completeTaskNum\":5,\"mark\":\"20%\",\"statistics\":\"5/25\"},{\"type\":0,\"name\":\"张妍\",\"position\":\"产品经理\",\"taskNum\":33,\"completeTaskNum\":12,\"mark\":\"36%\",\"statistics\":\"12/33\"},{\"type\":0,\"name\":\"李康\",\"position\":\"前端工程师\",\"taskNum\":42,\"completeTaskNum\":19,\"mark\":\"45%\",\"statistics\":\"19/42\"}],\"1\":[{\"type\":1,\"name\":\"夏琳\",\"position\":\"前端工程师\",\"taskNum\":19,\"completeTaskNum\":15,\"mark\":\"78%\",\"statistics\":\"15/19\"},{\"type\":1,\"name\":\"赵子豪\",\"position\":\"项目管理\",\"taskNum\":38,\"completeTaskNum\":16,\"mark\":\"42%\",\"statistics\":\"16/38\"},{\"type\":1,\"name\":\"孙天泽\",\"position\":\"项目经理\",\"taskNum\":35,\"completeTaskNum\":10,\"mark\":\"28%\",\"statistics\":\"10/35\"},{\"type\":1,\"name\":\"赵晓晨\",\"position\":\"UI设计师\",\"taskNum\":43,\"completeTaskNum\":17,\"mark\":\"39%\",\"statistics\":\"17/43\"},{\"type\":1,\"name\":\"葛悦凯\",\"position\":\"测试工程师\",\"taskNum\":43,\"completeTaskNum\":9,\"mark\":\"20%\",\"statistics\":\"9/43\"},{\"type\":1,\"name\":\"熊迪\",\"position\":\"开发工程师\",\"taskNum\":26,\"completeTaskNum\":14,\"mark\":\"53%\",\"statistics\":\"14/26\"},{\"type\":1,\"name\":\"张妍\",\"position\":\"产品经理\",\"taskNum\":44,\"completeTaskNum\":13,\"mark\":\"29%\",\"statistics\":\"13/44\"},{\"type\":1,\"name\":\"李康\",\"position\":\"前端工程师\",\"taskNum\":42,\"completeTaskNum\":12,\"mark\":\"28%\",\"statistics\":\"12/42\"}],\"2\":[{\"type\":2,\"name\":\"夏琳\",\"position\":\"前端工程师\",\"taskNum\":31,\"completeTaskNum\":5,\"mark\":\"16%\",\"statistics\":\"5/31\"},{\"type\":2,\"name\":\"赵子豪\",\"position\":\"项目管理\",\"taskNum\":17,\"completeTaskNum\":16,\"mark\":\"94%\",\"statistics\":\"16/17\"},{\"type\":2,\"name\":\"孙天泽\",\"position\":\"项目经理\",\"taskNum\":41,\"completeTaskNum\":14,\"mark\":\"34%\",\"statistics\":\"14/41\"},{\"type\":2,\"name\":\"赵晓晨\",\"position\":\"UI设计师\",\"taskNum\":43,\"completeTaskNum\":18,\"mark\":\"41%\",\"statistics\":\"18/43\"},{\"type\":2,\"name\":\"葛悦凯\",\"position\":\"测试工程师\",\"taskNum\":33,\"completeTaskNum\":7,\"mark\":\"21%\",\"statistics\":\"7/33\"},{\"type\":2,\"name\":\"熊迪\",\"position\":\"开发工程师\",\"taskNum\":24,\"completeTaskNum\":16,\"mark\":\"66%\",\"statistics\":\"16/24\"},{\"type\":2,\"name\":\"张妍\",\"position\":\"产品经理\",\"taskNum\":23,\"completeTaskNum\":6,\"mark\":\"26%\",\"statistics\":\"6/23\"},{\"type\":2,\"name\":\"李康\",\"position\":\"前端工程师\",\"taskNum\":17,\"completeTaskNum\":9,\"mark\":\"52%\",\"statistics\":\"9/17\"}],\"3\":[{\"type\":3,\"name\":\"夏琳\",\"position\":\"前端工程师\",\"taskNum\":22,\"completeTaskNum\":9,\"mark\":\"40%\",\"statistics\":\"9/22\"},{\"type\":3,\"name\":\"赵子豪\",\"position\":\"项目管理\",\"taskNum\":32,\"completeTaskNum\":9,\"mark\":\"28%\",\"statistics\":\"9/32\"},{\"type\":3,\"name\":\"孙天泽\",\"position\":\"项目经理\",\"taskNum\":20,\"completeTaskNum\":11,\"mark\":\"55%\",\"statistics\":\"11/20\"},{\"type\":3,\"name\":\"赵晓晨\",\"position\":\"UI设计师\",\"taskNum\":31,\"completeTaskNum\":11,\"mark\":\"35%\",\"statistics\":\"11/31\"},{\"type\":3,\"name\":\"葛悦凯\",\"position\":\"测试工程师\",\"taskNum\":20,\"completeTaskNum\":14,\"mark\":\"70%\",\"statistics\":\"14/20\"},{\"type\":3,\"name\":\"熊迪\",\"position\":\"开发工程师\",\"taskNum\":24,\"completeTaskNum\":6,\"mark\":\"25%\",\"statistics\":\"6/24\"},{\"type\":3,\"name\":\"张妍\",\"position\":\"产品经理\",\"taskNum\":39,\"completeTaskNum\":11,\"mark\":\"28%\",\"statistics\":\"11/39\"},{\"type\":3,\"name\":\"李康\",\"position\":\"前端工程师\",\"taskNum\":24,\"completeTaskNum\":10,\"mark\":\"41%\",\"statistics\":\"10/24\"}],\"4\":[{\"type\":4,\"name\":\"夏琳\",\"position\":\"前端工程师\",\"taskNum\":24,\"completeTaskNum\":9,\"mark\":\"37%\",\"statistics\":\"9/24\"},{\"type\":4,\"name\":\"赵子豪\",\"position\":\"项目管理\",\"taskNum\":15,\"completeTaskNum\":8,\"mark\":\"53%\",\"statistics\":\"8/15\"},{\"type\":4,\"name\":\"孙天泽\",\"position\":\"项目经理\",\"taskNum\":20,\"completeTaskNum\":13,\"mark\":\"65%\",\"statistics\":\"13/20\"},{\"type\":4,\"name\":\"赵晓晨\",\"position\":\"UI设计师\",\"taskNum\":31,\"completeTaskNum\":11,\"mark\":\"35%\",\"statistics\":\"11/31\"},{\"type\":4,\"name\":\"葛悦凯\",\"position\":\"测试工程师\",\"taskNum\":36,\"completeTaskNum\":5,\"mark\":\"13%\",\"statistics\":\"5/36\"},{\"type\":4,\"name\":\"熊迪\",\"position\":\"开发工程师\",\"taskNum\":39,\"completeTaskNum\":14,\"mark\":\"35%\",\"statistics\":\"14/39\"},{\"type\":4,\"name\":\"张妍\",\"position\":\"产品经理\",\"taskNum\":15,\"completeTaskNum\":9,\"mark\":\"60%\",\"statistics\":\"9/15\"},{\"type\":4,\"name\":\"李康\",\"position\":\"前端工程师\",\"taskNum\":31,\"completeTaskNum\":7,\"mark\":\"22%\",\"statistics\":\"7/31\"}],\"5\":[{\"type\":5,\"name\":\"夏琳\",\"position\":\"前端工程师\",\"taskNum\":39,\"completeTaskNum\":19,\"mark\":\"48%\",\"statistics\":\"19/39\"},{\"type\":5,\"name\":\"赵子豪\",\"position\":\"项目管理\",\"taskNum\":31,\"completeTaskNum\":12,\"mark\":\"38%\",\"statistics\":\"12/31\"},{\"type\":5,\"name\":\"孙天泽\",\"position\":\"项目经理\",\"taskNum\":34,\"completeTaskNum\":14,\"mark\":\"41%\",\"statistics\":\"14/34\"},{\"type\":5,\"name\":\"赵晓晨\",\"position\":\"UI设计师\",\"taskNum\":44,\"completeTaskNum\":12,\"mark\":\"27%\",\"statistics\":\"12/44\"},{\"type\":5,\"name\":\"葛悦凯\",\"position\":\"测试工程师\",\"taskNum\":42,\"completeTaskNum\":15,\"mark\":\"35%\",\"statistics\":\"15/42\"},{\"type\":5,\"name\":\"熊迪\",\"position\":\"开发工程师\",\"taskNum\":25,\"completeTaskNum\":18,\"mark\":\"72%\",\"statistics\":\"18/25\"},{\"type\":5,\"name\":\"张妍\",\"position\":\"产品经理\",\"taskNum\":24,\"completeTaskNum\":8,\"mark\":\"33%\",\"statistics\":\"8/24\"},{\"type\":5,\"name\":\"李康\",\"position\":\"前端工程师\",\"taskNum\":32,\"completeTaskNum\":15,\"mark\":\"46%\",\"statistics\":\"15/32\"}],\"6\":[{\"type\":6,\"name\":\"夏琳\",\"position\":\"前端工程师\",\"taskNum\":16,\"completeTaskNum\":15,\"mark\":\"93%\",\"statistics\":\"15/16\"},{\"type\":6,\"name\":\"赵子豪\",\"position\":\"项目管理\",\"taskNum\":30,\"completeTaskNum\":9,\"mark\":\"30%\",\"statistics\":\"9/30\"},{\"type\":6,\"name\":\"孙天泽\",\"position\":\"项目经理\",\"taskNum\":39,\"completeTaskNum\":8,\"mark\":\"20%\",\"statistics\":\"8/39\"},{\"type\":6,\"name\":\"赵晓晨\",\"position\":\"UI设计师\",\"taskNum\":25,\"completeTaskNum\":17,\"mark\":\"68%\",\"statistics\":\"17/25\"},{\"type\":6,\"name\":\"葛悦凯\",\"position\":\"测试工程师\",\"taskNum\":20,\"completeTaskNum\":16,\"mark\":\"80%\",\"statistics\":\"16/20\"},{\"type\":6,\"name\":\"熊迪\",\"position\":\"开发工程师\",\"taskNum\":34,\"completeTaskNum\":17,\"mark\":\"50%\",\"statistics\":\"17/34\"},{\"type\":6,\"name\":\"张妍\",\"position\":\"产品经理\",\"taskNum\":30,\"completeTaskNum\":17,\"mark\":\"56%\",\"statistics\":\"17/30\"},{\"type\":6,\"name\":\"李康\",\"position\":\"前端工程师\",\"taskNum\":27,\"completeTaskNum\":5,\"mark\":\"18%\",\"statistics\":\"5/27\"}]},\"isNew\":\"false\"}\n";
        String data5 = "{\"id\":\"\",\"time\":[\"2019/02/1\",\"2019/02/2\",\"2019/02/3\",\"2019/02/4\",\"2019/02/5\",\"2019/02/6\",\"2019/02/7\",\"2019/02/8\",\"2019/02/9\",\"2019/02/10\",\"2019/02/11\",\"2019/02/12\",\"2019/02/13\",\"2019/02/14\",\"2019/02/15\",\"2019/02/16\",\"2019/02/17\",\"2019/02/18\",\"2019/02/19\",\"2019/02/20\",\"2019/02/21\",\"2019/02/22\",\"2019/02/23\",\"2019/02/24\",\"2019/02/25\",\"2019/02/26\",\"2019/02/27\",\"2019/02/28\"],\"completeWorkTime\":[85,99,66,8,32,22,58,92,28,27],\"executeWorkTime\":[383,297,410,314,499,265,6,496,292,294],\"laveWorkTime\":[484,21,463,151,258,284,129,733,843,583],\"memberWorkTimeMap\":{\"李康\":{\"已完成\":21,\"剩余\":8},\"李岩\":{\"已完成\":21,\"剩余\":13},\"邓程鹏\":{\"已完成\":23,\"剩余\":7},\"马超\":{\"已完成\":16,\"剩余\":7},\"赵甲\":{\"已完成\":20,\"剩余\":8},\"温泉\":{\"已完成\":18,\"剩余\":8},\"夏琳\":{\"已完成\":5,\"剩余\":12},\"代雅娇\":{\"已完成\":10,\"剩余\":13}},\"projectAchievementsChartVO\":{\"names\":[\"邓程鹏\",\"温泉\",\"夏琳\",\"李康\",\"代雅娇\",\"赵甲\",\"李岩\",\"马超\"],\"firstQuarter\":[83,63,63,80,69,86,76,65],\"secondQuarter\":[84,89,67,66,87,73,65,64],\"thirdQuarter\":[84,70,82,81,87,71,73,74],\"fourthQuarter\":[79,65,67,83,64,64,83,87],\"aveQuarter\":[82,71,69,77,76,73,74,72]},\"bugDensity\":[[25.0,80.0,0.8],[45.0,125.0,0.5],[60.0,80.0,0.35],[85.0,110.0,0.15]],\"financeChart\":{\"income\":[744,396,702,504,792,900,729,688,864,744,882,756],\"profit\":[600,128,182,158,608,196,233,517,504,155,354,284],\"expenses\":[144,368,520,396,184,704,496,171,360,639,528,472]},\"memberStatusVO\":{\"0\":[{\"type\":0,\"name\":\"邓程鹏\",\"position\":\"\",\"taskNum\":22,\"completeTaskNum\":8,\"mark\":\"36%\",\"statistics\":\"8/22\"},{\"type\":0,\"name\":\"温泉\",\"position\":\"项目管理\",\"taskNum\":33,\"completeTaskNum\":14,\"mark\":\"42%\",\"statistics\":\"14/33\"},{\"type\":0,\"name\":\"夏琳\",\"position\":\"项目经理\",\"taskNum\":15,\"completeTaskNum\":14,\"mark\":\"93%\",\"statistics\":\"14/15\"},{\"type\":0,\"name\":\"李康\",\"position\":\"UI设计师\",\"taskNum\":34,\"completeTaskNum\":13,\"mark\":\"38%\",\"statistics\":\"13/34\"},{\"type\":0,\"name\":\"代雅娇\",\"position\":\"测试工程师\",\"taskNum\":23,\"completeTaskNum\":11,\"mark\":\"47%\",\"statistics\":\"11/23\"},{\"type\":0,\"name\":\"赵甲\",\"position\":\"开发工程师\",\"taskNum\":43,\"completeTaskNum\":5,\"mark\":\"11%\",\"statistics\":\"5/43\"},{\"type\":0,\"name\":\"李岩\",\"position\":\"产品经理\",\"taskNum\":26,\"completeTaskNum\":17,\"mark\":\"65%\",\"statistics\":\"17/26\"},{\"type\":0,\"name\":\"马超\",\"position\":\"前端工程师\",\"taskNum\":35,\"completeTaskNum\":9,\"mark\":\"25%\",\"statistics\":\"9/35\"}],\"1\":[{\"type\":1,\"name\":\"邓程鹏\",\"position\":\"前端工程师\",\"taskNum\":36,\"completeTaskNum\":18,\"mark\":\"50%\",\"statistics\":\"18/36\"},{\"type\":1,\"name\":\"温泉\",\"position\":\"项目管理\",\"taskNum\":35,\"completeTaskNum\":11,\"mark\":\"31%\",\"statistics\":\"11/35\"},{\"type\":1,\"name\":\"夏琳\",\"position\":\"项目经理\",\"taskNum\":17,\"completeTaskNum\":6,\"mark\":\"35%\",\"statistics\":\"6/17\"},{\"type\":1,\"name\":\"李康\",\"position\":\"UI设计师\",\"taskNum\":23,\"completeTaskNum\":15,\"mark\":\"65%\",\"statistics\":\"15/23\"},{\"type\":1,\"name\":\"代雅娇\",\"position\":\"测试工程师\",\"taskNum\":17,\"completeTaskNum\":15,\"mark\":\"88%\",\"statistics\":\"15/17\"},{\"type\":1,\"name\":\"赵甲\",\"position\":\"开发工程师\",\"taskNum\":42,\"completeTaskNum\":5,\"mark\":\"11%\",\"statistics\":\"5/42\"},{\"type\":1,\"name\":\"李岩\",\"position\":\"产品经理\",\"taskNum\":20,\"completeTaskNum\":6,\"mark\":\"30%\",\"statistics\":\"6/20\"},{\"type\":1,\"name\":\"马超\",\"position\":\"前端工程师\",\"taskNum\":44,\"completeTaskNum\":9,\"mark\":\"20%\",\"statistics\":\"9/44\"}],\"2\":[{\"type\":2,\"name\":\"邓程鹏\",\"position\":\"前端工程师\",\"taskNum\":30,\"completeTaskNum\":18,\"mark\":\"60%\",\"statistics\":\"18/30\"},{\"type\":2,\"name\":\"温泉\",\"position\":\"项目管理\",\"taskNum\":35,\"completeTaskNum\":18,\"mark\":\"51%\",\"statistics\":\"18/35\"},{\"type\":2,\"name\":\"夏琳\",\"position\":\"项目经理\",\"taskNum\":24,\"completeTaskNum\":10,\"mark\":\"41%\",\"statistics\":\"10/24\"},{\"type\":2,\"name\":\"李康\",\"position\":\"UI设计师\",\"taskNum\":41,\"completeTaskNum\":6,\"mark\":\"14%\",\"statistics\":\"6/41\"},{\"type\":2,\"name\":\"代雅娇\",\"position\":\"测试工程师\",\"taskNum\":39,\"completeTaskNum\":18,\"mark\":\"46%\",\"statistics\":\"18/39\"},{\"type\":2,\"name\":\"赵甲\",\"position\":\"开发工程师\",\"taskNum\":44,\"completeTaskNum\":19,\"mark\":\"43%\",\"statistics\":\"19/44\"},{\"type\":2,\"name\":\"李岩\",\"position\":\"产品经理\",\"taskNum\":29,\"completeTaskNum\":5,\"mark\":\"17%\",\"statistics\":\"5/29\"},{\"type\":2,\"name\":\"马超\",\"position\":\"前端工程师\",\"taskNum\":19,\"completeTaskNum\":18,\"mark\":\"94%\",\"statistics\":\"18/19\"}],\"3\":[{\"type\":3,\"name\":\"邓程鹏\",\"position\":\"前端工程师\",\"taskNum\":30,\"completeTaskNum\":11,\"mark\":\"36%\",\"statistics\":\"11/30\"},{\"type\":3,\"name\":\"温泉\",\"position\":\"项目管理\",\"taskNum\":16,\"completeTaskNum\":12,\"mark\":\"75%\",\"statistics\":\"12/16\"},{\"type\":3,\"name\":\"夏琳\",\"position\":\"项目经理\",\"taskNum\":18,\"completeTaskNum\":13,\"mark\":\"72%\",\"statistics\":\"13/18\"},{\"type\":3,\"name\":\"李康\",\"position\":\"UI设计师\",\"taskNum\":17,\"completeTaskNum\":8,\"mark\":\"47%\",\"statistics\":\"8/17\"},{\"type\":3,\"name\":\"代雅娇\",\"position\":\"测试工程师\",\"taskNum\":33,\"completeTaskNum\":11,\"mark\":\"33%\",\"statistics\":\"11/33\"},{\"type\":3,\"name\":\"赵甲\",\"position\":\"开发工程师\",\"taskNum\":33,\"completeTaskNum\":18,\"mark\":\"54%\",\"statistics\":\"18/33\"},{\"type\":3,\"name\":\"李岩\",\"position\":\"产品经理\",\"taskNum\":37,\"completeTaskNum\":7,\"mark\":\"18%\",\"statistics\":\"7/37\"},{\"type\":3,\"name\":\"马超\",\"position\":\"前端工程师\",\"taskNum\":15,\"completeTaskNum\":8,\"mark\":\"53%\",\"statistics\":\"8/15\"}],\"4\":[{\"type\":4,\"name\":\"邓程鹏\",\"position\":\"前端工程师\",\"taskNum\":20,\"completeTaskNum\":17,\"mark\":\"85%\",\"statistics\":\"17/20\"},{\"type\":4,\"name\":\"温泉\",\"position\":\"项目管理\",\"taskNum\":18,\"completeTaskNum\":9,\"mark\":\"50%\",\"statistics\":\"9/18\"},{\"type\":4,\"name\":\"夏琳\",\"position\":\"项目经理\",\"taskNum\":43,\"completeTaskNum\":18,\"mark\":\"41%\",\"statistics\":\"18/43\"},{\"type\":4,\"name\":\"李康\",\"position\":\"UI设计师\",\"taskNum\":19,\"completeTaskNum\":15,\"mark\":\"78%\",\"statistics\":\"15/19\"},{\"type\":4,\"name\":\"代雅娇\",\"position\":\"测试工程师\",\"taskNum\":20,\"completeTaskNum\":5,\"mark\":\"25%\",\"statistics\":\"5/20\"},{\"type\":4,\"name\":\"赵甲\",\"position\":\"开发工程师\",\"taskNum\":18,\"completeTaskNum\":6,\"mark\":\"33%\",\"statistics\":\"6/18\"},{\"type\":4,\"name\":\"李岩\",\"position\":\"产品经理\",\"taskNum\":44,\"completeTaskNum\":15,\"mark\":\"34%\",\"statistics\":\"15/44\"},{\"type\":4,\"name\":\"马超\",\"position\":\"前端工程师\",\"taskNum\":34,\"completeTaskNum\":5,\"mark\":\"14%\",\"statistics\":\"5/34\"}],\"5\":[{\"type\":5,\"name\":\"邓程鹏\",\"position\":\"前端工程师\",\"taskNum\":43,\"completeTaskNum\":7,\"mark\":\"16%\",\"statistics\":\"7/43\"},{\"type\":5,\"name\":\"温泉\",\"position\":\"项目管理\",\"taskNum\":20,\"completeTaskNum\":11,\"mark\":\"55%\",\"statistics\":\"11/20\"},{\"type\":5,\"name\":\"夏琳\",\"position\":\"项目经理\",\"taskNum\":20,\"completeTaskNum\":19,\"mark\":\"95%\",\"statistics\":\"19/20\"},{\"type\":5,\"name\":\"李康\",\"position\":\"UI设计师\",\"taskNum\":16,\"completeTaskNum\":11,\"mark\":\"68%\",\"statistics\":\"11/16\"},{\"type\":5,\"name\":\"代雅娇\",\"position\":\"测试工程师\",\"taskNum\":19,\"completeTaskNum\":18,\"mark\":\"94%\",\"statistics\":\"18/19\"},{\"type\":5,\"name\":\"赵甲\",\"position\":\"开发工程师\",\"taskNum\":15,\"completeTaskNum\":12,\"mark\":\"80%\",\"statistics\":\"12/15\"},{\"type\":5,\"name\":\"李岩\",\"position\":\"产品经理\",\"taskNum\":37,\"completeTaskNum\":8,\"mark\":\"21%\",\"statistics\":\"8/37\"},{\"type\":5,\"name\":\"马超\",\"position\":\"前端工程师\",\"taskNum\":33,\"completeTaskNum\":14,\"mark\":\"42%\",\"statistics\":\"14/33\"}],\"6\":[{\"type\":6,\"name\":\"邓程鹏\",\"position\":\"前端工程师\",\"taskNum\":37,\"completeTaskNum\":10,\"mark\":\"27%\",\"statistics\":\"10/37\"},{\"type\":6,\"name\":\"温泉\",\"position\":\"项目管理\",\"taskNum\":17,\"completeTaskNum\":9,\"mark\":\"52%\",\"statistics\":\"9/17\"},{\"type\":6,\"name\":\"夏琳\",\"position\":\"项目经理\",\"taskNum\":37,\"completeTaskNum\":14,\"mark\":\"37%\",\"statistics\":\"14/37\"},{\"type\":6,\"name\":\"李康\",\"position\":\"UI设计师\",\"taskNum\":17,\"completeTaskNum\":6,\"mark\":\"35%\",\"statistics\":\"6/17\"},{\"type\":6,\"name\":\"代雅娇\",\"position\":\"测试工程师\",\"taskNum\":42,\"completeTaskNum\":9,\"mark\":\"21%\",\"statistics\":\"9/42\"},{\"type\":6,\"name\":\"赵甲\",\"position\":\"开发工程师\",\"taskNum\":42,\"completeTaskNum\":8,\"mark\":\"19%\",\"statistics\":\"8/42\"},{\"type\":6,\"name\":\"李岩\",\"position\":\"产品经理\",\"taskNum\":31,\"completeTaskNum\":12,\"mark\":\"38%\",\"statistics\":\"12/31\"},{\"type\":6,\"name\":\"马超\",\"position\":\"前端工程师\",\"taskNum\":20,\"completeTaskNum\":12,\"mark\":\"60%\",\"statistics\":\"12/20\"}]},\"isNew\":\"false\"}\n";
        String data6 = "{\"id\":\"\",\"time\":[\"2019/02/1\",\"2019/02/2\",\"2019/02/3\",\"2019/02/4\",\"2019/02/5\",\"2019/02/6\",\"2019/02/7\",\"2019/02/8\",\"2019/02/9\",\"2019/02/10\",\"2019/02/11\",\"2019/02/12\",\"2019/02/13\",\"2019/02/14\",\"2019/02/15\",\"2019/02/16\",\"2019/02/17\",\"2019/02/18\",\"2019/02/19\",\"2019/02/20\",\"2019/02/21\",\"2019/02/22\",\"2019/02/23\",\"2019/02/24\",\"2019/02/25\",\"2019/02/26\",\"2019/02/27\",\"2019/02/28\"],\"completeWorkTime\":[0,66,92,39,37,33,5,46,25,6],\"executeWorkTime\":[426,94,145,451,434,359,391,214,173,178],\"laveWorkTime\":[855,278,678,708,728,623,586,416,644,575],\"memberWorkTimeMap\":{\"孟庆磊\":{\"已完成\":5,\"剩余\":13},\"周博林\":{\"已完成\":22,\"剩余\":9},\"刘勇\":{\"已完成\":23,\"剩余\":11},\"孙二鑫\":{\"已完成\":15,\"剩余\":7},\"孙国宁\":{\"已完成\":17,\"剩余\":9},\"温泉\":{\"已完成\":20,\"剩余\":13},\"李冰阳\":{\"已完成\":14,\"剩余\":8},\"代雅娇\":{\"已完成\":10,\"剩余\":6}},\"projectAchievementsChartVO\":{\"names\":[\"李冰阳\",\"周博林\",\"刘勇\",\"孟庆磊\",\"代雅娇\",\"孙国宁\",\"温泉\",\"孙二鑫\"],\"firstQuarter\":[73,83,77,70,64,75,82,74],\"secondQuarter\":[64,70,62,77,62,80,72,60],\"thirdQuarter\":[83,69,70,73,73,73,62,72],\"fourthQuarter\":[78,70,86,88,69,68,60,87],\"aveQuarter\":[74,73,73,77,67,74,69,73]},\"bugDensity\":[[25.0,80.0,0.8],[45.0,125.0,0.5],[60.0,80.0,0.35],[85.0,110.0,0.15]],\"financeChart\":{\"income\":[280,128,396,783,576,513,495,531,810,900,408,760],\"profit\":[226,120,356,711,208,193,487,307,136,884,174,274],\"expenses\":[254,388,140,172,368,320,328,224,704,316,234,486]},\"memberStatusVO\":{\"0\":[{\"type\":0,\"name\":\"李冰阳\",\"position\":\"\",\"taskNum\":30,\"completeTaskNum\":15,\"mark\":\"50%\",\"statistics\":\"15/30\"},{\"type\":0,\"name\":\"周博林\",\"position\":\"项目管理\",\"taskNum\":20,\"completeTaskNum\":19,\"mark\":\"95%\",\"statistics\":\"19/20\"},{\"type\":0,\"name\":\"刘勇\",\"position\":\"项目经理\",\"taskNum\":19,\"completeTaskNum\":14,\"mark\":\"73%\",\"statistics\":\"14/19\"},{\"type\":0,\"name\":\"孟庆磊\",\"position\":\"UI设计师\",\"taskNum\":33,\"completeTaskNum\":9,\"mark\":\"27%\",\"statistics\":\"9/33\"},{\"type\":0,\"name\":\"代雅娇\",\"position\":\"测试工程师\",\"taskNum\":16,\"completeTaskNum\":16,\"mark\":\"100%\",\"statistics\":\"16/16\"},{\"type\":0,\"name\":\"孙国宁\",\"position\":\"开发工程师\",\"taskNum\":16,\"completeTaskNum\":11,\"mark\":\"68%\",\"statistics\":\"11/16\"},{\"type\":0,\"name\":\"温泉\",\"position\":\"产品经理\",\"taskNum\":26,\"completeTaskNum\":13,\"mark\":\"50%\",\"statistics\":\"13/26\"},{\"type\":0,\"name\":\"孙二鑫\",\"position\":\"前端工程师\",\"taskNum\":38,\"completeTaskNum\":13,\"mark\":\"34%\",\"statistics\":\"13/38\"}],\"1\":[{\"type\":1,\"name\":\"李冰阳\",\"position\":\"前端工程师\",\"taskNum\":33,\"completeTaskNum\":5,\"mark\":\"15%\",\"statistics\":\"5/33\"},{\"type\":1,\"name\":\"周博林\",\"position\":\"项目管理\",\"taskNum\":35,\"completeTaskNum\":7,\"mark\":\"20%\",\"statistics\":\"7/35\"},{\"type\":1,\"name\":\"刘勇\",\"position\":\"项目经理\",\"taskNum\":42,\"completeTaskNum\":10,\"mark\":\"23%\",\"statistics\":\"10/42\"},{\"type\":1,\"name\":\"孟庆磊\",\"position\":\"UI设计师\",\"taskNum\":39,\"completeTaskNum\":9,\"mark\":\"23%\",\"statistics\":\"9/39\"},{\"type\":1,\"name\":\"代雅娇\",\"position\":\"测试工程师\",\"taskNum\":42,\"completeTaskNum\":11,\"mark\":\"26%\",\"statistics\":\"11/42\"},{\"type\":1,\"name\":\"孙国宁\",\"position\":\"开发工程师\",\"taskNum\":37,\"completeTaskNum\":5,\"mark\":\"13%\",\"statistics\":\"5/37\"},{\"type\":1,\"name\":\"温泉\",\"position\":\"产品经理\",\"taskNum\":37,\"completeTaskNum\":6,\"mark\":\"16%\",\"statistics\":\"6/37\"},{\"type\":1,\"name\":\"孙二鑫\",\"position\":\"前端工程师\",\"taskNum\":39,\"completeTaskNum\":10,\"mark\":\"25%\",\"statistics\":\"10/39\"}],\"2\":[{\"type\":2,\"name\":\"李冰阳\",\"position\":\"前端工程师\",\"taskNum\":22,\"completeTaskNum\":7,\"mark\":\"31%\",\"statistics\":\"7/22\"},{\"type\":2,\"name\":\"周博林\",\"position\":\"项目管理\",\"taskNum\":17,\"completeTaskNum\":9,\"mark\":\"52%\",\"statistics\":\"9/17\"},{\"type\":2,\"name\":\"刘勇\",\"position\":\"项目经理\",\"taskNum\":30,\"completeTaskNum\":7,\"mark\":\"23%\",\"statistics\":\"7/30\"},{\"type\":2,\"name\":\"孟庆磊\",\"position\":\"UI设计师\",\"taskNum\":28,\"completeTaskNum\":14,\"mark\":\"50%\",\"statistics\":\"14/28\"},{\"type\":2,\"name\":\"代雅娇\",\"position\":\"测试工程师\",\"taskNum\":33,\"completeTaskNum\":12,\"mark\":\"36%\",\"statistics\":\"12/33\"},{\"type\":2,\"name\":\"孙国宁\",\"position\":\"开发工程师\",\"taskNum\":30,\"completeTaskNum\":16,\"mark\":\"53%\",\"statistics\":\"16/30\"},{\"type\":2,\"name\":\"温泉\",\"position\":\"产品经理\",\"taskNum\":24,\"completeTaskNum\":9,\"mark\":\"37%\",\"statistics\":\"9/24\"},{\"type\":2,\"name\":\"孙二鑫\",\"position\":\"前端工程师\",\"taskNum\":31,\"completeTaskNum\":5,\"mark\":\"16%\",\"statistics\":\"5/31\"}],\"3\":[{\"type\":3,\"name\":\"李冰阳\",\"position\":\"前端工程师\",\"taskNum\":25,\"completeTaskNum\":14,\"mark\":\"56%\",\"statistics\":\"14/25\"},{\"type\":3,\"name\":\"周博林\",\"position\":\"项目管理\",\"taskNum\":21,\"completeTaskNum\":6,\"mark\":\"28%\",\"statistics\":\"6/21\"},{\"type\":3,\"name\":\"刘勇\",\"position\":\"项目经理\",\"taskNum\":45,\"completeTaskNum\":19,\"mark\":\"42%\",\"statistics\":\"19/45\"},{\"type\":3,\"name\":\"孟庆磊\",\"position\":\"UI设计师\",\"taskNum\":52,\"completeTaskNum\":19,\"mark\":\"36%\",\"statistics\":\"19/52\"},{\"type\":3,\"name\":\"代雅娇\",\"position\":\"测试工程师\",\"taskNum\":43,\"completeTaskNum\":9,\"mark\":\"20%\",\"statistics\":\"9/43\"},{\"type\":3,\"name\":\"孙国宁\",\"position\":\"开发工程师\",\"taskNum\":36,\"completeTaskNum\":8,\"mark\":\"22%\",\"statistics\":\"8/36\"},{\"type\":3,\"name\":\"温泉\",\"position\":\"产品经理\",\"taskNum\":42,\"completeTaskNum\":8,\"mark\":\"19%\",\"statistics\":\"8/42\"},{\"type\":3,\"name\":\"孙二鑫\",\"position\":\"前端工程师\",\"taskNum\":18,\"completeTaskNum\":8,\"mark\":\"44%\",\"statistics\":\"8/18\"}],\"4\":[{\"type\":4,\"name\":\"李冰阳\",\"position\":\"前端工程师\",\"taskNum\":29,\"completeTaskNum\":14,\"mark\":\"48%\",\"statistics\":\"14/29\"},{\"type\":4,\"name\":\"周博林\",\"position\":\"项目管理\",\"taskNum\":15,\"completeTaskNum\":12,\"mark\":\"80%\",\"statistics\":\"12/15\"},{\"type\":4,\"name\":\"刘勇\",\"position\":\"项目经理\",\"taskNum\":22,\"completeTaskNum\":11,\"mark\":\"50%\",\"statistics\":\"11/22\"},{\"type\":4,\"name\":\"孟庆磊\",\"position\":\"UI设计师\",\"taskNum\":42,\"completeTaskNum\":8,\"mark\":\"19%\",\"statistics\":\"8/42\"},{\"type\":4,\"name\":\"代雅娇\",\"position\":\"测试工程师\",\"taskNum\":36,\"completeTaskNum\":6,\"mark\":\"16%\",\"statistics\":\"6/36\"},{\"type\":4,\"name\":\"孙国宁\",\"position\":\"开发工程师\",\"taskNum\":25,\"completeTaskNum\":6,\"mark\":\"24%\",\"statistics\":\"6/25\"},{\"type\":4,\"name\":\"温泉\",\"position\":\"产品经理\",\"taskNum\":40,\"completeTaskNum\":6,\"mark\":\"15%\",\"statistics\":\"6/40\"},{\"type\":4,\"name\":\"孙二鑫\",\"position\":\"前端工程师\",\"taskNum\":32,\"completeTaskNum\":13,\"mark\":\"40%\",\"statistics\":\"13/32\"}],\"5\":[{\"type\":5,\"name\":\"李冰阳\",\"position\":\"前端工程师\",\"taskNum\":42,\"completeTaskNum\":19,\"mark\":\"45%\",\"statistics\":\"19/42\"},{\"type\":5,\"name\":\"周博林\",\"position\":\"项目管理\",\"taskNum\":20,\"completeTaskNum\":8,\"mark\":\"40%\",\"statistics\":\"8/20\"},{\"type\":5,\"name\":\"刘勇\",\"position\":\"项目经理\",\"taskNum\":22,\"completeTaskNum\":11,\"mark\":\"50%\",\"statistics\":\"11/22\"},{\"type\":5,\"name\":\"孟庆磊\",\"position\":\"UI设计师\",\"taskNum\":16,\"completeTaskNum\":8,\"mark\":\"50%\",\"statistics\":\"8/16\"},{\"type\":5,\"name\":\"代雅娇\",\"position\":\"测试工程师\",\"taskNum\":25,\"completeTaskNum\":18,\"mark\":\"72%\",\"statistics\":\"18/25\"},{\"type\":5,\"name\":\"孙国宁\",\"position\":\"开发工程师\",\"taskNum\":20,\"completeTaskNum\":14,\"mark\":\"70%\",\"statistics\":\"14/20\"},{\"type\":5,\"name\":\"温泉\",\"position\":\"产品经理\",\"taskNum\":34,\"completeTaskNum\":8,\"mark\":\"23%\",\"statistics\":\"8/34\"},{\"type\":5,\"name\":\"孙二鑫\",\"position\":\"前端工程师\",\"taskNum\":40,\"completeTaskNum\":12,\"mark\":\"30%\",\"statistics\":\"12/40\"}],\"6\":[{\"type\":6,\"name\":\"李冰阳\",\"position\":\"前端工程师\",\"taskNum\":17,\"completeTaskNum\":15,\"mark\":\"88%\",\"statistics\":\"15/17\"},{\"type\":6,\"name\":\"周博林\",\"position\":\"项目管理\",\"taskNum\":18,\"completeTaskNum\":15,\"mark\":\"83%\",\"statistics\":\"15/18\"},{\"type\":6,\"name\":\"刘勇\",\"position\":\"项目经理\",\"taskNum\":44,\"completeTaskNum\":7,\"mark\":\"15%\",\"statistics\":\"7/44\"},{\"type\":6,\"name\":\"孟庆磊\",\"position\":\"UI设计师\",\"taskNum\":31,\"completeTaskNum\":8,\"mark\":\"25%\",\"statistics\":\"8/31\"},{\"type\":6,\"name\":\"代雅娇\",\"position\":\"测试工程师\",\"taskNum\":35,\"completeTaskNum\":14,\"mark\":\"40%\",\"statistics\":\"14/35\"},{\"type\":6,\"name\":\"孙国宁\",\"position\":\"开发工程师\",\"taskNum\":34,\"completeTaskNum\":15,\"mark\":\"44%\",\"statistics\":\"15/34\"},{\"type\":6,\"name\":\"温泉\",\"position\":\"产品经理\",\"taskNum\":31,\"completeTaskNum\":19,\"mark\":\"61%\",\"statistics\":\"19/31\"},{\"type\":6,\"name\":\"孙二鑫\",\"position\":\"前端工程师\",\"taskNum\":42,\"completeTaskNum\":5,\"mark\":\"11%\",\"statistics\":\"5/42\"}]},\"isNew\":\"false\"}\n";
        String data7 = "{\"id\":\"\",\"time\":[\"2019/02/1\",\"2019/02/2\",\"2019/02/3\",\"2019/02/4\",\"2019/02/5\",\"2019/02/6\",\"2019/02/7\",\"2019/02/8\",\"2019/02/9\",\"2019/02/10\",\"2019/02/11\",\"2019/02/12\",\"2019/02/13\",\"2019/02/14\",\"2019/02/15\",\"2019/02/16\",\"2019/02/17\",\"2019/02/18\",\"2019/02/19\",\"2019/02/20\",\"2019/02/21\",\"2019/02/22\",\"2019/02/23\",\"2019/02/24\",\"2019/02/25\",\"2019/02/26\",\"2019/02/27\",\"2019/02/28\"],\"completeWorkTime\":[78,52,48,13,91,79,0,96,95,10],\"executeWorkTime\":[181,199,397,213,56,104,12,200,271,118],\"laveWorkTime\":[880,315,673,656,47,841,570,811,391,181],\"memberWorkTimeMap\":{\"孟庆磊\":{\"已完成\":22,\"剩余\":10},\"周博林\":{\"已完成\":23,\"剩余\":7},\"王洪洋\":{\"已完成\":16,\"剩余\":11},\"何绍清\":{\"已完成\":18,\"剩余\":10},\"宋忠让\":{\"已完成\":21,\"剩余\":11},\"孟鑫\":{\"已完成\":6,\"剩余\":7},\"闻雅\":{\"已完成\":20,\"剩余\":6},\"任攀\":{\"已完成\":11,\"剩余\":10}},\"projectAchievementsChartVO\":{\"names\":[\"王洪洋\",\"何绍清\",\"闻雅\",\"宋忠让\",\"孟鑫\",\"任攀\",\"周博林\",\"孟庆磊\"],\"firstQuarter\":[83,70,69,71,65,66,70,89],\"secondQuarter\":[66,83,67,77,76,66,67,88],\"thirdQuarter\":[74,67,64,78,80,60,69,81],\"fourthQuarter\":[62,88,67,76,66,60,65,73],\"aveQuarter\":[71,77,66,75,71,63,67,82]},\"bugDensity\":[[25.0,80.0,0.8],[45.0,125.0,0.5],[60.0,80.0,0.35],[85.0,110.0,0.15]],\"financeChart\":{\"income\":[216,468,568,765,440,648,576,784,369,744,468,819],\"profit\":[136,414,559,269,260,552,160,352,161,474,100,371],\"expenses\":[180,424,429,496,180,196,416,432,208,270,368,448]},\"memberStatusVO\":{\"0\":[{\"type\":0,\"name\":\"王洪洋\",\"position\":\"\",\"taskNum\":17,\"completeTaskNum\":14,\"mark\":\"82%\",\"statistics\":\"14/17\"},{\"type\":0,\"name\":\"何绍清\",\"position\":\"项目管理\",\"taskNum\":20,\"completeTaskNum\":11,\"mark\":\"55%\",\"statistics\":\"11/20\"},{\"type\":0,\"name\":\"闻雅\",\"position\":\"项目经理\",\"taskNum\":30,\"completeTaskNum\":17,\"mark\":\"57%\",\"statistics\":\"17/30\"},{\"type\":0,\"name\":\"宋忠让\",\"position\":\"UI设计师\",\"taskNum\":44,\"completeTaskNum\":9,\"mark\":\"20%\",\"statistics\":\"9/44\"},{\"type\":0,\"name\":\"孟鑫\",\"position\":\"测试工程师\",\"taskNum\":37,\"completeTaskNum\":9,\"mark\":\"24%\",\"statistics\":\"9/37\"},{\"type\":0,\"name\":\"任攀\",\"position\":\"开发工程师\",\"taskNum\":30,\"completeTaskNum\":14,\"mark\":\"46%\",\"statistics\":\"14/30\"},{\"type\":0,\"name\":\"周博林\",\"position\":\"产品经理\",\"taskNum\":24,\"completeTaskNum\":16,\"mark\":\"66%\",\"statistics\":\"16/24\"},{\"type\":0,\"name\":\"孟庆磊\",\"position\":\"前端工程师\",\"taskNum\":38,\"completeTaskNum\":13,\"mark\":\"34%\",\"statistics\":\"13/38\"}],\"1\":[{\"type\":1,\"name\":\"王洪洋\",\"position\":\"前端工程师\",\"taskNum\":31,\"completeTaskNum\":17,\"mark\":\"54%\",\"statistics\":\"17/31\"},{\"type\":1,\"name\":\"何绍清\",\"position\":\"项目管理\",\"taskNum\":28,\"completeTaskNum\":6,\"mark\":\"21%\",\"statistics\":\"6/28\"},{\"type\":1,\"name\":\"闻雅\",\"position\":\"项目经理\",\"taskNum\":35,\"completeTaskNum\":8,\"mark\":\"22%\",\"statistics\":\"8/35\"},{\"type\":1,\"name\":\"宋忠让\",\"position\":\"UI设计师\",\"taskNum\":26,\"completeTaskNum\":14,\"mark\":\"53%\",\"statistics\":\"14/26\"},{\"type\":1,\"name\":\"孟鑫\",\"position\":\"测试工程师\",\"taskNum\":24,\"completeTaskNum\":12,\"mark\":\"50%\",\"statistics\":\"12/24\"},{\"type\":1,\"name\":\"任攀\",\"position\":\"开发工程师\",\"taskNum\":37,\"completeTaskNum\":9,\"mark\":\"24%\",\"statistics\":\"9/37\"},{\"type\":1,\"name\":\"周博林\",\"position\":\"产品经理\",\"taskNum\":24,\"completeTaskNum\":13,\"mark\":\"54%\",\"statistics\":\"13/24\"},{\"type\":1,\"name\":\"孟庆磊\",\"position\":\"前端工程师\",\"taskNum\":42,\"completeTaskNum\":14,\"mark\":\"33%\",\"statistics\":\"14/42\"}],\"2\":[{\"type\":2,\"name\":\"王洪洋\",\"position\":\"前端工程师\",\"taskNum\":32,\"completeTaskNum\":6,\"mark\":\"18%\",\"statistics\":\"6/32\"},{\"type\":2,\"name\":\"何绍清\",\"position\":\"项目管理\",\"taskNum\":29,\"completeTaskNum\":9,\"mark\":\"31%\",\"statistics\":\"9/29\"},{\"type\":2,\"name\":\"闻雅\",\"position\":\"项目经理\",\"taskNum\":31,\"completeTaskNum\":19,\"mark\":\"61%\",\"statistics\":\"19/31\"},{\"type\":2,\"name\":\"宋忠让\",\"position\":\"UI设计师\",\"taskNum\":36,\"completeTaskNum\":14,\"mark\":\"38%\",\"statistics\":\"14/36\"},{\"type\":2,\"name\":\"孟鑫\",\"position\":\"测试工程师\",\"taskNum\":20,\"completeTaskNum\":5,\"mark\":\"25%\",\"statistics\":\"5/20\"},{\"type\":2,\"name\":\"任攀\",\"position\":\"开发工程师\",\"taskNum\":35,\"completeTaskNum\":18,\"mark\":\"51%\",\"statistics\":\"18/35\"},{\"type\":2,\"name\":\"周博林\",\"position\":\"产品经理\",\"taskNum\":24,\"completeTaskNum\":11,\"mark\":\"45%\",\"statistics\":\"11/24\"},{\"type\":2,\"name\":\"孟庆磊\",\"position\":\"前端工程师\",\"taskNum\":15,\"completeTaskNum\":7,\"mark\":\"46%\",\"statistics\":\"7/15\"}],\"3\":[{\"type\":3,\"name\":\"王洪洋\",\"position\":\"前端工程师\",\"taskNum\":34,\"completeTaskNum\":13,\"mark\":\"38%\",\"statistics\":\"13/34\"},{\"type\":3,\"name\":\"何绍清\",\"position\":\"项目管理\",\"taskNum\":35,\"completeTaskNum\":14,\"mark\":\"40%\",\"statistics\":\"14/35\"},{\"type\":3,\"name\":\"闻雅\",\"position\":\"项目经理\",\"taskNum\":39,\"completeTaskNum\":19,\"mark\":\"48%\",\"statistics\":\"19/39\"},{\"type\":3,\"name\":\"宋忠让\",\"position\":\"UI设计师\",\"taskNum\":34,\"completeTaskNum\":16,\"mark\":\"47%\",\"statistics\":\"16/34\"},{\"type\":3,\"name\":\"孟鑫\",\"position\":\"测试工程师\",\"taskNum\":37,\"completeTaskNum\":8,\"mark\":\"21%\",\"statistics\":\"8/37\"},{\"type\":3,\"name\":\"任攀\",\"position\":\"开发工程师\",\"taskNum\":20,\"completeTaskNum\":12,\"mark\":\"60%\",\"statistics\":\"12/20\"},{\"type\":3,\"name\":\"周博林\",\"position\":\"产品经理\",\"taskNum\":16,\"completeTaskNum\":16,\"mark\":\"100%\",\"statistics\":\"16/16\"},{\"type\":3,\"name\":\"孟庆磊\",\"position\":\"前端工程师\",\"taskNum\":33,\"completeTaskNum\":14,\"mark\":\"42%\",\"statistics\":\"14/33\"}],\"4\":[{\"type\":4,\"name\":\"王洪洋\",\"position\":\"前端工程师\",\"taskNum\":26,\"completeTaskNum\":12,\"mark\":\"46%\",\"statistics\":\"12/26\"},{\"type\":4,\"name\":\"何绍清\",\"position\":\"项目管理\",\"taskNum\":41,\"completeTaskNum\":7,\"mark\":\"17%\",\"statistics\":\"7/41\"},{\"type\":4,\"name\":\"闻雅\",\"position\":\"项目经理\",\"taskNum\":32,\"completeTaskNum\":11,\"mark\":\"34%\",\"statistics\":\"11/32\"},{\"type\":4,\"name\":\"宋忠让\",\"position\":\"UI设计师\",\"taskNum\":32,\"completeTaskNum\":13,\"mark\":\"40%\",\"statistics\":\"13/32\"},{\"type\":4,\"name\":\"孟鑫\",\"position\":\"测试工程师\",\"taskNum\":32,\"completeTaskNum\":15,\"mark\":\"46%\",\"statistics\":\"15/32\"},{\"type\":4,\"name\":\"任攀\",\"position\":\"开发工程师\",\"taskNum\":44,\"completeTaskNum\":19,\"mark\":\"43%\",\"statistics\":\"19/44\"},{\"type\":4,\"name\":\"周博林\",\"position\":\"产品经理\",\"taskNum\":39,\"completeTaskNum\":17,\"mark\":\"43%\",\"statistics\":\"17/39\"},{\"type\":4,\"name\":\"孟庆磊\",\"position\":\"前端工程师\",\"taskNum\":20,\"completeTaskNum\":9,\"mark\":\"45%\",\"statistics\":\"9/20\"}],\"5\":[{\"type\":5,\"name\":\"王洪洋\",\"position\":\"前端工程师\",\"taskNum\":25,\"completeTaskNum\":17,\"mark\":\"68%\",\"statistics\":\"17/25\"},{\"type\":5,\"name\":\"何绍清\",\"position\":\"项目管理\",\"taskNum\":28,\"completeTaskNum\":12,\"mark\":\"42%\",\"statistics\":\"12/28\"},{\"type\":5,\"name\":\"闻雅\",\"position\":\"项目经理\",\"taskNum\":15,\"completeTaskNum\":8,\"mark\":\"53%\",\"statistics\":\"8/15\"},{\"type\":5,\"name\":\"宋忠让\",\"position\":\"UI设计师\",\"taskNum\":30,\"completeTaskNum\":14,\"mark\":\"46%\",\"statistics\":\"14/30\"},{\"type\":5,\"name\":\"孟鑫\",\"position\":\"测试工程师\",\"taskNum\":28,\"completeTaskNum\":5,\"mark\":\"17%\",\"statistics\":\"5/28\"},{\"type\":5,\"name\":\"任攀\",\"position\":\"开发工程师\",\"taskNum\":28,\"completeTaskNum\":6,\"mark\":\"21%\",\"statistics\":\"6/28\"},{\"type\":5,\"name\":\"周博林\",\"position\":\"产品经理\",\"taskNum\":19,\"completeTaskNum\":18,\"mark\":\"94%\",\"statistics\":\"18/19\"},{\"type\":5,\"name\":\"孟庆磊\",\"position\":\"前端工程师\",\"taskNum\":33,\"completeTaskNum\":13,\"mark\":\"39%\",\"statistics\":\"13/33\"}],\"6\":[{\"type\":6,\"name\":\"王洪洋\",\"position\":\"前端工程师\",\"taskNum\":33,\"completeTaskNum\":16,\"mark\":\"48%\",\"statistics\":\"16/33\"},{\"type\":6,\"name\":\"何绍清\",\"position\":\"项目管理\",\"taskNum\":22,\"completeTaskNum\":5,\"mark\":\"22%\",\"statistics\":\"5/22\"},{\"type\":6,\"name\":\"闻雅\",\"position\":\"项目经理\",\"taskNum\":22,\"completeTaskNum\":16,\"mark\":\"72%\",\"statistics\":\"16/22\"},{\"type\":6,\"name\":\"宋忠让\",\"position\":\"UI设计师\",\"taskNum\":37,\"completeTaskNum\":19,\"mark\":\"51%\",\"statistics\":\"19/37\"},{\"type\":6,\"name\":\"孟鑫\",\"position\":\"测试工程师\",\"taskNum\":33,\"completeTaskNum\":6,\"mark\":\"18%\",\"statistics\":\"6/33\"},{\"type\":6,\"name\":\"任攀\",\"position\":\"开发工程师\",\"taskNum\":19,\"completeTaskNum\":5,\"mark\":\"26%\",\"statistics\":\"5/19\"},{\"type\":6,\"name\":\"周博林\",\"position\":\"产品经理\",\"taskNum\":25,\"completeTaskNum\":17,\"mark\":\"68%\",\"statistics\":\"17/25\"},{\"type\":6,\"name\":\"孟庆磊\",\"position\":\"前端工程师\",\"taskNum\":23,\"completeTaskNum\":11,\"mark\":\"47%\",\"statistics\":\"11/23\"}]},\"isNew\":\"false\"}\n";
        String data8 = "{\"id\":\"\",\"time\":[\"2019/02/1\",\"2019/02/2\",\"2019/02/3\",\"2019/02/4\",\"2019/02/5\",\"2019/02/6\",\"2019/02/7\",\"2019/02/8\",\"2019/02/9\",\"2019/02/10\",\"2019/02/11\",\"2019/02/12\",\"2019/02/13\",\"2019/02/14\",\"2019/02/15\",\"2019/02/16\",\"2019/02/17\",\"2019/02/18\",\"2019/02/19\",\"2019/02/20\",\"2019/02/21\",\"2019/02/22\",\"2019/02/23\",\"2019/02/24\",\"2019/02/25\",\"2019/02/26\",\"2019/02/27\",\"2019/02/28\"],\"completeWorkTime\":[55,31,28,29,38,69,66,25,82,77],\"executeWorkTime\":[67,121,109,150,300,43,488,196,491,223],\"laveWorkTime\":[344,528,239,768,376,820,346,703,369,858],\"memberWorkTimeMap\":{\"荣庆川\":{\"已完成\":17,\"剩余\":13},\"张鹏\":{\"已完成\":11,\"剩余\":13},\"康凯\":{\"已完成\":19,\"剩余\":12},\"翟洋\":{\"已完成\":11,\"剩余\":9},\"李慧\":{\"已完成\":12,\"剩余\":10},\"曹一哲\":{\"已完成\":20,\"剩余\":14},\"欧培培\":{\"已完成\":17,\"剩余\":8},\"张世俊\":{\"已完成\":12,\"剩余\":13}},\"projectAchievementsChartVO\":{\"names\":[\"李慧\",\"欧培培\",\"曹一哲\",\"荣庆川\",\"张鹏\",\"张世俊\",\"康凯\",\"翟洋\"],\"firstQuarter\":[68,72,62,60,78,77,75,60],\"secondQuarter\":[80,71,69,79,83,68,61,68],\"thirdQuarter\":[71,65,62,73,63,84,63,64],\"fourthQuarter\":[64,62,75,87,71,63,83,71],\"aveQuarter\":[70,67,67,74,73,73,70,65]},\"bugDensity\":[[25.0,80.0,0.8],[45.0,125.0,0.5],[60.0,80.0,0.35],[85.0,110.0,0.15]],\"financeChart\":{\"income\":[234,396,513,536,408,592,666,549,801,702,712,736],\"profit\":[154,360,190,186,228,151,282,117,177,166,514,196],\"expenses\":[280,336,504,450,180,441,384,432,624,536,198,540]},\"memberStatusVO\":{\"0\":[{\"type\":0,\"name\":\"李慧\",\"position\":\"\",\"taskNum\":43,\"completeTaskNum\":6,\"mark\":\"13%\",\"statistics\":\"6/43\"},{\"type\":0,\"name\":\"欧培培\",\"position\":\"项目管理\",\"taskNum\":32,\"completeTaskNum\":19,\"mark\":\"59%\",\"statistics\":\"19/32\"},{\"type\":0,\"name\":\"曹一哲\",\"position\":\"项目经理\",\"taskNum\":19,\"completeTaskNum\":16,\"mark\":\"84%\",\"statistics\":\"16/19\"},{\"type\":0,\"name\":\"荣庆川\",\"position\":\"UI设计师\",\"taskNum\":30,\"completeTaskNum\":16,\"mark\":\"53%\",\"statistics\":\"16/30\"},{\"type\":0,\"name\":\"张鹏\",\"position\":\"测试工程师\",\"taskNum\":38,\"completeTaskNum\":18,\"mark\":\"47%\",\"statistics\":\"18/38\"},{\"type\":0,\"name\":\"张世俊\",\"position\":\"开发工程师\",\"taskNum\":15,\"completeTaskNum\":9,\"mark\":\"60%\",\"statistics\":\"9/15\"},{\"type\":0,\"name\":\"康凯\",\"position\":\"产品经理\",\"taskNum\":28,\"completeTaskNum\":9,\"mark\":\"32%\",\"statistics\":\"9/28\"},{\"type\":0,\"name\":\"翟洋\",\"position\":\"前端工程师\",\"taskNum\":23,\"completeTaskNum\":18,\"mark\":\"78%\",\"statistics\":\"18/23\"}],\"1\":[{\"type\":1,\"name\":\"李慧\",\"position\":\"前端工程师\",\"taskNum\":30,\"completeTaskNum\":17,\"mark\":\"56%\",\"statistics\":\"17/30\"},{\"type\":1,\"name\":\"欧培培\",\"position\":\"项目管理\",\"taskNum\":29,\"completeTaskNum\":14,\"mark\":\"48%\",\"statistics\":\"14/29\"},{\"type\":1,\"name\":\"曹一哲\",\"position\":\"项目经理\",\"taskNum\":34,\"completeTaskNum\":11,\"mark\":\"32%\",\"statistics\":\"11/34\"},{\"type\":1,\"name\":\"荣庆川\",\"position\":\"UI设计师\",\"taskNum\":17,\"completeTaskNum\":10,\"mark\":\"58%\",\"statistics\":\"10/17\"},{\"type\":1,\"name\":\"张鹏\",\"position\":\"测试工程师\",\"taskNum\":41,\"completeTaskNum\":10,\"mark\":\"24%\",\"statistics\":\"10/41\"},{\"type\":1,\"name\":\"张世俊\",\"position\":\"开发工程师\",\"taskNum\":18,\"completeTaskNum\":12,\"mark\":\"66%\",\"statistics\":\"12/18\"},{\"type\":1,\"name\":\"康凯\",\"position\":\"产品经理\",\"taskNum\":34,\"completeTaskNum\":17,\"mark\":\"50%\",\"statistics\":\"17/34\"},{\"type\":1,\"name\":\"翟洋\",\"position\":\"前端工程师\",\"taskNum\":40,\"completeTaskNum\":19,\"mark\":\"47%\",\"statistics\":\"19/40\"}],\"2\":[{\"type\":2,\"name\":\"李慧\",\"position\":\"前端工程师\",\"taskNum\":26,\"completeTaskNum\":8,\"mark\":\"30%\",\"statistics\":\"8/26\"},{\"type\":2,\"name\":\"欧培培\",\"position\":\"项目管理\",\"taskNum\":41,\"completeTaskNum\":15,\"mark\":\"36%\",\"statistics\":\"15/41\"},{\"type\":2,\"name\":\"曹一哲\",\"position\":\"项目经理\",\"taskNum\":17,\"completeTaskNum\":6,\"mark\":\"35%\",\"statistics\":\"6/17\"},{\"type\":2,\"name\":\"荣庆川\",\"position\":\"UI设计师\",\"taskNum\":40,\"completeTaskNum\":10,\"mark\":\"25%\",\"statistics\":\"10/40\"},{\"type\":2,\"name\":\"张鹏\",\"position\":\"测试工程师\",\"taskNum\":24,\"completeTaskNum\":10,\"mark\":\"41%\",\"statistics\":\"10/24\"},{\"type\":2,\"name\":\"张世俊\",\"position\":\"开发工程师\",\"taskNum\":33,\"completeTaskNum\":9,\"mark\":\"27%\",\"statistics\":\"9/33\"},{\"type\":2,\"name\":\"康凯\",\"position\":\"产品经理\",\"taskNum\":23,\"completeTaskNum\":15,\"mark\":\"65%\",\"statistics\":\"15/23\"},{\"type\":2,\"name\":\"翟洋\",\"position\":\"前端工程师\",\"taskNum\":42,\"completeTaskNum\":16,\"mark\":\"38%\",\"statistics\":\"16/42\"}],\"3\":[{\"type\":3,\"name\":\"李慧\",\"position\":\"前端工程师\",\"taskNum\":20,\"completeTaskNum\":8,\"mark\":\"40%\",\"statistics\":\"8/20\"},{\"type\":3,\"name\":\"欧培培\",\"position\":\"项目管理\",\"taskNum\":24,\"completeTaskNum\":13,\"mark\":\"54%\",\"statistics\":\"13/24\"},{\"type\":3,\"name\":\"曹一哲\",\"position\":\"项目经理\",\"taskNum\":16,\"completeTaskNum\":5,\"mark\":\"31%\",\"statistics\":\"5/16\"},{\"type\":3,\"name\":\"荣庆川\",\"position\":\"UI设计师\",\"taskNum\":34,\"completeTaskNum\":17,\"mark\":\"50%\",\"statistics\":\"17/34\"},{\"type\":3,\"name\":\"张鹏\",\"position\":\"测试工程师\",\"taskNum\":23,\"completeTaskNum\":8,\"mark\":\"34%\",\"statistics\":\"8/23\"},{\"type\":3,\"name\":\"张世俊\",\"position\":\"开发工程师\",\"taskNum\":30,\"completeTaskNum\":9,\"mark\":\"30%\",\"statistics\":\"9/30\"},{\"type\":3,\"name\":\"康凯\",\"position\":\"产品经理\",\"taskNum\":22,\"completeTaskNum\":18,\"mark\":\"81%\",\"statistics\":\"18/22\"},{\"type\":3,\"name\":\"翟洋\",\"position\":\"前端工程师\",\"taskNum\":29,\"completeTaskNum\":13,\"mark\":\"44%\",\"statistics\":\"13/29\"}],\"4\":[{\"type\":4,\"name\":\"李慧\",\"position\":\"前端工程师\",\"taskNum\":30,\"completeTaskNum\":9,\"mark\":\"30%\",\"statistics\":\"9/30\"},{\"type\":4,\"name\":\"欧培培\",\"position\":\"项目管理\",\"taskNum\":28,\"completeTaskNum\":15,\"mark\":\"53%\",\"statistics\":\"15/28\"},{\"type\":4,\"name\":\"曹一哲\",\"position\":\"项目经理\",\"taskNum\":40,\"completeTaskNum\":10,\"mark\":\"25%\",\"statistics\":\"10/40\"},{\"type\":4,\"name\":\"荣庆川\",\"position\":\"UI设计师\",\"taskNum\":30,\"completeTaskNum\":9,\"mark\":\"30%\",\"statistics\":\"9/30\"},{\"type\":4,\"name\":\"张鹏\",\"position\":\"测试工程师\",\"taskNum\":29,\"completeTaskNum\":13,\"mark\":\"44%\",\"statistics\":\"13/29\"},{\"type\":4,\"name\":\"张世俊\",\"position\":\"开发工程师\",\"taskNum\":34,\"completeTaskNum\":16,\"mark\":\"47%\",\"statistics\":\"16/34\"},{\"type\":4,\"name\":\"康凯\",\"position\":\"产品经理\",\"taskNum\":36,\"completeTaskNum\":8,\"mark\":\"22%\",\"statistics\":\"8/36\"},{\"type\":4,\"name\":\"翟洋\",\"position\":\"前端工程师\",\"taskNum\":19,\"completeTaskNum\":6,\"mark\":\"31%\",\"statistics\":\"6/19\"}],\"5\":[{\"type\":5,\"name\":\"李慧\",\"position\":\"前端工程师\",\"taskNum\":38,\"completeTaskNum\":11,\"mark\":\"28%\",\"statistics\":\"11/38\"},{\"type\":5,\"name\":\"欧培培\",\"position\":\"项目管理\",\"taskNum\":36,\"completeTaskNum\":14,\"mark\":\"38%\",\"statistics\":\"14/36\"},{\"type\":5,\"name\":\"曹一哲\",\"position\":\"项目经理\",\"taskNum\":25,\"completeTaskNum\":19,\"mark\":\"76%\",\"statistics\":\"19/25\"},{\"type\":5,\"name\":\"荣庆川\",\"position\":\"UI设计师\",\"taskNum\":32,\"completeTaskNum\":19,\"mark\":\"59%\",\"statistics\":\"19/32\"},{\"type\":5,\"name\":\"张鹏\",\"position\":\"测试工程师\",\"taskNum\":40,\"completeTaskNum\":19,\"mark\":\"48%\",\"statistics\":\"19/40\"},{\"type\":5,\"name\":\"张世俊\",\"position\":\"开发工程师\",\"taskNum\":34,\"completeTaskNum\":15,\"mark\":\"44%\",\"statistics\":\"15/34\"},{\"type\":5,\"name\":\"康凯\",\"position\":\"产品经理\",\"taskNum\":38,\"completeTaskNum\":12,\"mark\":\"31%\",\"statistics\":\"12/38\"},{\"type\":5,\"name\":\"翟洋\",\"position\":\"前端工程师\",\"taskNum\":33,\"completeTaskNum\":17,\"mark\":\"51%\",\"statistics\":\"17/33\"}],\"6\":[{\"type\":6,\"name\":\"李慧\",\"position\":\"前端工程师\",\"taskNum\":33,\"completeTaskNum\":18,\"mark\":\"54%\",\"statistics\":\"18/33\"},{\"type\":6,\"name\":\"欧培培\",\"position\":\"项目管理\",\"taskNum\":43,\"completeTaskNum\":13,\"mark\":\"30%\",\"statistics\":\"13/43\"},{\"type\":6,\"name\":\"曹一哲\",\"position\":\"项目经理\",\"taskNum\":31,\"completeTaskNum\":11,\"mark\":\"35%\",\"statistics\":\"11/31\"},{\"type\":6,\"name\":\"荣庆川\",\"position\":\"UI设计师\",\"taskNum\":42,\"completeTaskNum\":6,\"mark\":\"14%\",\"statistics\":\"6/42\"},{\"type\":6,\"name\":\"张鹏\",\"position\":\"测试工程师\",\"taskNum\":38,\"completeTaskNum\":7,\"mark\":\"18%\",\"statistics\":\"7/38\"},{\"type\":6,\"name\":\"张世俊\",\"position\":\"开发工程师\",\"taskNum\":37,\"completeTaskNum\":7,\"mark\":\"18%\",\"statistics\":\"7/37\"},{\"type\":6,\"name\":\"康凯\",\"position\":\"产品经理\",\"taskNum\":29,\"completeTaskNum\":5,\"mark\":\"17%\",\"statistics\":\"5/29\"},{\"type\":6,\"name\":\"翟洋\",\"position\":\"前端工程师\",\"taskNum\":17,\"completeTaskNum\":12,\"mark\":\"70%\",\"statistics\":\"12/17\"}]},\"isNew\":\"false\"}\n";
        String data9 = "{\"id\":\"\",\"time\":[\"2019/02/1\",\"2019/02/2\",\"2019/02/3\",\"2019/02/4\",\"2019/02/5\",\"2019/02/6\",\"2019/02/7\",\"2019/02/8\",\"2019/02/9\",\"2019/02/10\",\"2019/02/11\",\"2019/02/12\",\"2019/02/13\",\"2019/02/14\",\"2019/02/15\",\"2019/02/16\",\"2019/02/17\",\"2019/02/18\",\"2019/02/19\",\"2019/02/20\",\"2019/02/21\",\"2019/02/22\",\"2019/02/23\",\"2019/02/24\",\"2019/02/25\",\"2019/02/26\",\"2019/02/27\",\"2019/02/28\"],\"completeWorkTime\":[30,11,91,4,9,18,56,28,40,66],\"executeWorkTime\":[111,331,300,296,6,83,92,17,26,57],\"laveWorkTime\":[189,176,645,442,340,840,830,795,623,655],\"memberWorkTimeMap\":{\"郝树新\":{\"已完成\":5,\"剩余\":14},\"孟庆磊\":{\"已完成\":10,\"剩余\":14},\"张妍\":{\"已完成\":18,\"剩余\":11},\"贺怡\":{\"已完成\":15,\"剩余\":5},\"郭维明\":{\"已完成\":11,\"剩余\":7},\"马乃锋\":{\"已完成\":23,\"剩余\":5},\"马超\":{\"已完成\":20,\"剩余\":14},\"汪道乐\":{\"已完成\":17,\"剩余\":14}},\"projectAchievementsChartVO\":{\"names\":[\"汪道乐\",\"张妍\",\"马超\",\"马乃锋\",\"孟庆磊\",\"郭维明\",\"郝树新\",\"贺怡\"],\"firstQuarter\":[68,74,70,72,68,63,87,77],\"secondQuarter\":[75,78,74,76,61,70,72,66],\"thirdQuarter\":[78,86,70,67,64,64,72,83],\"fourthQuarter\":[73,70,89,71,75,86,60,77],\"aveQuarter\":[73,77,75,71,67,70,72,75]},\"bugDensity\":[[25.0,80.0,0.8],[45.0,125.0,0.5],[60.0,80.0,0.35],[85.0,110.0,0.15]],\"financeChart\":{\"income\":[639,405,760,648,855,594,585,396,594,160,819,480],\"profit\":[487,177,283,531,479,266,181,284,114,188,187,354],\"expenses\":[152,328,477,117,376,328,504,312,480,272,632,126]},\"memberStatusVO\":{\"0\":[{\"type\":0,\"name\":\"汪道乐\",\"position\":\"\",\"taskNum\":23,\"completeTaskNum\":9,\"mark\":\"39%\",\"statistics\":\"9/23\"},{\"type\":0,\"name\":\"张妍\",\"position\":\"项目管理\",\"taskNum\":30,\"completeTaskNum\":5,\"mark\":\"16%\",\"statistics\":\"5/30\"},{\"type\":0,\"name\":\"马超\",\"position\":\"项目经理\",\"taskNum\":38,\"completeTaskNum\":14,\"mark\":\"36%\",\"statistics\":\"14/38\"},{\"type\":0,\"name\":\"马乃锋\",\"position\":\"UI设计师\",\"taskNum\":18,\"completeTaskNum\":7,\"mark\":\"38%\",\"statistics\":\"7/18\"},{\"type\":0,\"name\":\"孟庆磊\",\"position\":\"测试工程师\",\"taskNum\":43,\"completeTaskNum\":9,\"mark\":\"20%\",\"statistics\":\"9/43\"},{\"type\":0,\"name\":\"郭维明\",\"position\":\"开发工程师\",\"taskNum\":42,\"completeTaskNum\":10,\"mark\":\"23%\",\"statistics\":\"10/42\"},{\"type\":0,\"name\":\"郝树新\",\"position\":\"产品经理\",\"taskNum\":41,\"completeTaskNum\":11,\"mark\":\"26%\",\"statistics\":\"11/41\"},{\"type\":0,\"name\":\"贺怡\",\"position\":\"前端工程师\",\"taskNum\":15,\"completeTaskNum\":11,\"mark\":\"73%\",\"statistics\":\"11/15\"}],\"1\":[{\"type\":1,\"name\":\"汪道乐\",\"position\":\"前端工程师\",\"taskNum\":25,\"completeTaskNum\":5,\"mark\":\"20%\",\"statistics\":\"5/25\"},{\"type\":1,\"name\":\"张妍\",\"position\":\"项目管理\",\"taskNum\":30,\"completeTaskNum\":17,\"mark\":\"56%\",\"statistics\":\"17/30\"},{\"type\":1,\"name\":\"马超\",\"position\":\"项目经理\",\"taskNum\":19,\"completeTaskNum\":19,\"mark\":\"100%\",\"statistics\":\"19/19\"},{\"type\":1,\"name\":\"马乃锋\",\"position\":\"UI设计师\",\"taskNum\":24,\"completeTaskNum\":7,\"mark\":\"29%\",\"statistics\":\"7/24\"},{\"type\":1,\"name\":\"孟庆磊\",\"position\":\"测试工程师\",\"taskNum\":44,\"completeTaskNum\":16,\"mark\":\"36%\",\"statistics\":\"16/44\"},{\"type\":1,\"name\":\"郭维明\",\"position\":\"开发工程师\",\"taskNum\":25,\"completeTaskNum\":10,\"mark\":\"40%\",\"statistics\":\"10/25\"},{\"type\":1,\"name\":\"郝树新\",\"position\":\"产品经理\",\"taskNum\":40,\"completeTaskNum\":18,\"mark\":\"45%\",\"statistics\":\"18/40\"},{\"type\":1,\"name\":\"贺怡\",\"position\":\"前端工程师\",\"taskNum\":22,\"completeTaskNum\":19,\"mark\":\"86%\",\"statistics\":\"19/22\"}],\"2\":[{\"type\":2,\"name\":\"汪道乐\",\"position\":\"前端工程师\",\"taskNum\":30,\"completeTaskNum\":5,\"mark\":\"16%\",\"statistics\":\"5/30\"},{\"type\":2,\"name\":\"张妍\",\"position\":\"项目管理\",\"taskNum\":19,\"completeTaskNum\":19,\"mark\":\"100%\",\"statistics\":\"19/19\"},{\"type\":2,\"name\":\"马超\",\"position\":\"项目经理\",\"taskNum\":30,\"completeTaskNum\":12,\"mark\":\"40%\",\"statistics\":\"12/30\"},{\"type\":2,\"name\":\"马乃锋\",\"position\":\"UI设计师\",\"taskNum\":25,\"completeTaskNum\":16,\"mark\":\"64%\",\"statistics\":\"16/25\"},{\"type\":2,\"name\":\"孟庆磊\",\"position\":\"测试工程师\",\"taskNum\":41,\"completeTaskNum\":11,\"mark\":\"26%\",\"statistics\":\"11/41\"},{\"type\":2,\"name\":\"郭维明\",\"position\":\"开发工程师\",\"taskNum\":32,\"completeTaskNum\":9,\"mark\":\"28%\",\"statistics\":\"9/32\"},{\"type\":2,\"name\":\"郝树新\",\"position\":\"产品经理\",\"taskNum\":33,\"completeTaskNum\":9,\"mark\":\"27%\",\"statistics\":\"9/33\"},{\"type\":2,\"name\":\"贺怡\",\"position\":\"前端工程师\",\"taskNum\":22,\"completeTaskNum\":15,\"mark\":\"68%\",\"statistics\":\"15/22\"}],\"3\":[{\"type\":3,\"name\":\"汪道乐\",\"position\":\"前端工程师\",\"taskNum\":22,\"completeTaskNum\":8,\"mark\":\"36%\",\"statistics\":\"8/22\"},{\"type\":3,\"name\":\"张妍\",\"position\":\"项目管理\",\"taskNum\":32,\"completeTaskNum\":10,\"mark\":\"31%\",\"statistics\":\"10/32\"},{\"type\":3,\"name\":\"马超\",\"position\":\"项目经理\",\"taskNum\":23,\"completeTaskNum\":12,\"mark\":\"52%\",\"statistics\":\"12/23\"},{\"type\":3,\"name\":\"马乃锋\",\"position\":\"UI设计师\",\"taskNum\":34,\"completeTaskNum\":14,\"mark\":\"41%\",\"statistics\":\"14/34\"},{\"type\":3,\"name\":\"孟庆磊\",\"position\":\"测试工程师\",\"taskNum\":44,\"completeTaskNum\":7,\"mark\":\"15%\",\"statistics\":\"7/44\"},{\"type\":3,\"name\":\"郭维明\",\"position\":\"开发工程师\",\"taskNum\":17,\"completeTaskNum\":7,\"mark\":\"41%\",\"statistics\":\"7/17\"},{\"type\":3,\"name\":\"郝树新\",\"position\":\"产品经理\",\"taskNum\":33,\"completeTaskNum\":15,\"mark\":\"45%\",\"statistics\":\"15/33\"},{\"type\":3,\"name\":\"贺怡\",\"position\":\"前端工程师\",\"taskNum\":25,\"completeTaskNum\":18,\"mark\":\"72%\",\"statistics\":\"18/25\"}],\"4\":[{\"type\":4,\"name\":\"汪道乐\",\"position\":\"前端工程师\",\"taskNum\":39,\"completeTaskNum\":13,\"mark\":\"33%\",\"statistics\":\"13/39\"},{\"type\":4,\"name\":\"张妍\",\"position\":\"项目管理\",\"taskNum\":36,\"completeTaskNum\":11,\"mark\":\"30%\",\"statistics\":\"11/36\"},{\"type\":4,\"name\":\"马超\",\"position\":\"项目经理\",\"taskNum\":26,\"completeTaskNum\":14,\"mark\":\"53%\",\"statistics\":\"14/26\"},{\"type\":4,\"name\":\"马乃锋\",\"position\":\"UI设计师\",\"taskNum\":26,\"completeTaskNum\":11,\"mark\":\"42%\",\"statistics\":\"11/26\"},{\"type\":4,\"name\":\"孟庆磊\",\"position\":\"测试工程师\",\"taskNum\":39,\"completeTaskNum\":11,\"mark\":\"28%\",\"statistics\":\"11/39\"},{\"type\":4,\"name\":\"郭维明\",\"position\":\"开发工程师\",\"taskNum\":43,\"completeTaskNum\":7,\"mark\":\"16%\",\"statistics\":\"7/43\"},{\"type\":4,\"name\":\"郝树新\",\"position\":\"产品经理\",\"taskNum\":16,\"completeTaskNum\":8,\"mark\":\"50%\",\"statistics\":\"8/16\"},{\"type\":4,\"name\":\"贺怡\",\"position\":\"前端工程师\",\"taskNum\":24,\"completeTaskNum\":13,\"mark\":\"54%\",\"statistics\":\"13/24\"}],\"5\":[{\"type\":5,\"name\":\"汪道乐\",\"position\":\"前端工程师\",\"taskNum\":43,\"completeTaskNum\":11,\"mark\":\"25%\",\"statistics\":\"11/43\"},{\"type\":5,\"name\":\"张妍\",\"position\":\"项目管理\",\"taskNum\":36,\"completeTaskNum\":6,\"mark\":\"16%\",\"statistics\":\"6/36\"},{\"type\":5,\"name\":\"马超\",\"position\":\"项目经理\",\"taskNum\":29,\"completeTaskNum\":9,\"mark\":\"31%\",\"statistics\":\"9/29\"},{\"type\":5,\"name\":\"马乃锋\",\"position\":\"UI设计师\",\"taskNum\":38,\"completeTaskNum\":16,\"mark\":\"42%\",\"statistics\":\"16/38\"},{\"type\":5,\"name\":\"孟庆磊\",\"position\":\"测试工程师\",\"taskNum\":18,\"completeTaskNum\":19,\"mark\":\"105%\",\"statistics\":\"19/18\"},{\"type\":5,\"name\":\"郭维明\",\"position\":\"开发工程师\",\"taskNum\":19,\"completeTaskNum\":19,\"mark\":\"100%\",\"statistics\":\"19/19\"},{\"type\":5,\"name\":\"郝树新\",\"position\":\"产品经理\",\"taskNum\":37,\"completeTaskNum\":15,\"mark\":\"40%\",\"statistics\":\"15/37\"},{\"type\":5,\"name\":\"贺怡\",\"position\":\"前端工程师\",\"taskNum\":33,\"completeTaskNum\":10,\"mark\":\"30%\",\"statistics\":\"10/33\"}],\"6\":[{\"type\":6,\"name\":\"汪道乐\",\"position\":\"前端工程师\",\"taskNum\":38,\"completeTaskNum\":6,\"mark\":\"15%\",\"statistics\":\"6/38\"},{\"type\":6,\"name\":\"张妍\",\"position\":\"项目管理\",\"taskNum\":37,\"completeTaskNum\":8,\"mark\":\"21%\",\"statistics\":\"8/37\"},{\"type\":6,\"name\":\"马超\",\"position\":\"项目经理\",\"taskNum\":44,\"completeTaskNum\":5,\"mark\":\"11%\",\"statistics\":\"5/44\"},{\"type\":6,\"name\":\"马乃锋\",\"position\":\"UI设计师\",\"taskNum\":43,\"completeTaskNum\":11,\"mark\":\"25%\",\"statistics\":\"11/43\"},{\"type\":6,\"name\":\"孟庆磊\",\"position\":\"测试工程师\",\"taskNum\":16,\"completeTaskNum\":17,\"mark\":\"106%\",\"statistics\":\"17/16\"},{\"type\":6,\"name\":\"郭维明\",\"position\":\"开发工程师\",\"taskNum\":23,\"completeTaskNum\":6,\"mark\":\"26%\",\"statistics\":\"6/23\"},{\"type\":6,\"name\":\"郝树新\",\"position\":\"产品经理\",\"taskNum\":16,\"completeTaskNum\":15,\"mark\":\"93%\",\"statistics\":\"15/16\"},{\"type\":6,\"name\":\"贺怡\",\"position\":\"前端工程师\",\"taskNum\":28,\"completeTaskNum\":11,\"mark\":\"39%\",\"statistics\":\"11/28\"}]},\"isNew\":\"false\"}\n";
        String data = "";
        String ascii = Ascii(projectId);
        switch (ascii) {

            case "0":
                data = data0;
                break;
            case "1":
                data = data1;
                break;
            case "2":
                data = data2;
                break;
            case "3":
                data = data3;
                break;
            case "4":
                data = data4;
                break;
            case "5":
                data = data5;
                break;
            case "6":
                data = data6;
                break;
            case "7":
                data = data7;
                break;
            case "8":
                data = data8;
                break;
            case "9":
                data = data9;
                break;
            default:
        }
        ProjectWorkTimeChartVO projectWorkTimeChartVO = GsonUtil.fromJson(data, ProjectWorkTimeChartVO.class);
        projectWorkTimeChartVO.setId(projectId);
        return projectWorkTimeChartVO;
        //return new ProjectWorkTimeChartVO();
    }

    public String Ascii(String string) {
        StringBuilder sb = new StringBuilder();
        char[] ch = string.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            sb.append(Integer.valueOf(ch[i]));// 加空格
        }
        return sb.toString().substring(sb.toString().length() - 2, sb.toString().length() - 1);
    }


    private Map<String, Map<String, Integer>> getlaveWorkTime(String projectId) {
        Map<String, Map<String, Integer>> memberWorkTimeMap = new HashMap<>();
        Project project = projectRepository.findOne(projectId);
        if (null == project || Boolean.TRUE.equals(project.getDelFlag())) {
            return memberWorkTimeMap;
        }
        String[] projectMemberIds = project.getProjectMemberIds();
        BoolQueryBuilder builder = boolQuery().must(termQuery(PROJECT_ID, projectId))
                .mustNot(QueryBuilders.termQuery(DEL_FLAG, true));
        for (String id : projectMemberIds) {
            UserEO userEO = userEOService.getUserWithRoles(id);
            int count = taskRepository.countByProjectIdEqualsAndMemberIdsIn(projectId, id);
            builder.must(QueryBuilders.termQuery("memberIds", id))
                    .must(QueryBuilders.termQuery("taskStatus", ProjectStatusEnums.COMPLETE.getStatus()));
            int completeTaskNum = Lists.newArrayList(taskRepository.search(builder)).size();
            int lave = count - completeTaskNum;
            if (completeTaskNum > 45) {
                completeTaskNum = 45;
            }
            if (lave > 45) {
                lave = 45;
            }
            Map<String, Integer> m = new HashMap<>();
            m.put("已完成", completeTaskNum == 0 ? 0 : completeTaskNum * 4);
            m.put("剩余", lave < 0 ? 0 : lave * 4);
            memberWorkTimeMap.put(userEO.getUsname(), m);
        }
        return memberWorkTimeMap;
    }


    //数组求和
    private float arraySum(float[] workTimes) {
        float sum = 0;
        if (workTimes == null) {return sum;}
        for (Float workTime : workTimes) {
            sum = sum + workTime;
        }
        return sum;
    }
    //

    /**
     * @Description: 获取项目总任务书，完成任务数，参与人员，项目总支出
     * @auther: ZHAIKAIXUAN
     * @params:
     * @return:
     * @date: 2018/11/22 13:46
     */
    public ProjectSmaryNum querysmaryNum(String id) throws Exception {
        if (id == null || id.trim().isEmpty()) {return null;}
        // 任务总数
        QueryBuilder dailyQueriBuilder = QueryBuilders.boolQuery()
                .mustNot(QueryBuilders.termQuery(DEL_FLAG, true))
                .must(QueryBuilders.termQuery(PROJECT_ID, id));
        List<Task> taskTotal = Lists.newArrayList(taskRepository.search(dailyQueriBuilder));
        // 完成的任务数
        QueryBuilder dailyFinishedQueriBuilder = QueryBuilders.boolQuery()
                .mustNot(QueryBuilders.termQuery(DEL_FLAG, true))
                .must(QueryBuilders.termQuery(PROJECT_ID, id))
                .must(QueryBuilders.termQuery("taskStatus", "已完成"));
        List<Task> taskFinished = Lists.newArrayList(taskRepository.search(dailyFinishedQueriBuilder));

        // 项目参入人员
        Project project = projectRepository.findOne(id);
        int length = 0;
        double expensestotalAmount = 0;
        if (null != project && !Boolean.TRUE.equals(project.getDelFlag())) {

            if (null != project.getProjectMemberIds()) {
                length = project.getProjectMemberIds().length;
            }
            QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                    .mustNot(QueryBuilders.termQuery(DEL_FLAG, true))
                    .must(QueryBuilders.termQuery("parentProjectId", id));
            List<ExpensesIncurred> expensesIncurreds =
                    Lists.newArrayList(expensesIncurredRepository.search(queryBuilder));
            expensestotalAmount = abtainSum(expensesIncurreds);
        }
        ProjectSmaryNum projectSmaryNum = new ProjectSmaryNum(taskTotal.size(), length, taskFinished.size(), expensestotalAmount);
        return projectSmaryNum;
    }


    //传入List将对应的值求和
    public double abtainSum(List<ExpensesIncurred> objects) {
        double sum = 0;
        if (objects == null) {return sum;}
        for (ExpensesIncurred expensesIncurred : objects) {
            sum = sum + (expensesIncurred.getExpensesAmount() == null ? 0.0 : expensesIncurred.getExpensesAmount());
        }
        return sum;
    }


    //    两个日期差的时间数
    public long week(Date startTime, Date endTime) {
        if (startTime == null || endTime == null) {return 0;}
        return (endTime.getTime() - startTime.getTime()) / (1000 * 3600 * 24 * 7);
    }

    //    两个日期差的天数
    public int diffDays(Date startTime, Date endTime) {
        if (startTime == null || endTime == null) {return 0;}
        long l = Math.abs(endTime.getTime() - startTime.getTime()) / (1000 * 3600 * 24);
        return (int) l;
    }

    // 页面展示信息接口
    public ShowProjectAnalyzeVO showProjectAnalyzeVO(String id) throws Exception {
        ProjectSmaryNum smaryNum = querysmaryNum(id);
        List<TaskFinishVO> taskStatus = taskFinishStatistic(id);
        List<Map<String, Object>> workTimes = queryWorkTimeStatus(id);
        List<ProjectStatusFinishedVO> finishedStatus = queryFinishedStatus(id);
        Project project = projectRepository.findOne(id);
        boolean isProjectLeader = false;
        if (null != project && UserUtils.getUserId().equals(project.getProjectLeaderId())) {
            isProjectLeader = true;
        }
        return new ShowProjectAnalyzeVO(smaryNum, taskStatus, workTimes, finishedStatus, isProjectLeader);
    }

    private List<Object> numPlace(Object[] array) {
        int temp;
        for (int i = 0; i < array.length - 1; i++) {
            for (int j = 0; j < array.length - i - 1; j++) {
                if ((Integer) array[j + 1] < (Integer) array[j]) {
                    temp = (Integer) array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
        return Arrays.asList(array);
    }


    // 根据Id获取项目
    public Project project(String id) {
        if (id == null || id.trim().isEmpty()) {return null;}
        List<Project> projects = Lists.newArrayList(projectRepository.findAll());
        for (Project project : projects) {
            if (project.getId().equals(id)) {
                return project;
            }
        }
        return null;
    }

    /**
     * 对List<Map>进行排序</>
     */
    public List<Map<String, Object>> mapListSort(List<Map<String, Object>> mapsList) {
        // 获取按照顺序的week周
        List<Integer> weekNum = new ArrayList<>();
        for (Map<String, Object> entryList : mapsList) {
            Integer week = (Integer) entryList.get("week");
            weekNum.add(week);
        }
        // 对List排序
        Collections.sort(weekNum);
        // 很久周存入新的List<Map>
        List<Map<String, Object>> maps = new ArrayList<>();
        for (Integer week : weekNum) {
            for (Map<String, Object> map : mapsList) {
                if (map.get("week") == week) {
                    maps.add(map);
                }
            }
        }
        return maps;
    }


    /**
     * 根据日报ID判断完成状态
     *
     * @param dailyId
     * @return
     */
    @Transactional
    public String dailyStatusUpdate(String dailyId) {
        //修改日报信息
        Daily daily = dailyRepository.findOne(dailyId);
        if (null == daily || Boolean.TRUE.equals(daily.getDelFlag())) {
            return "不存在该日报！";
        }
        daily.setFinishedStatus("已完成");
        dailyRepository.save(daily);
        return   "修改状态成功！";
    }

    /***
     * 查询项目任务完成度列表
     * @param projectId
     * @return
     */
    public List<ProjectStatusFinishedVO> queryFinishedStatus(String projectId) {
        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery(PROJECT_ID, projectId))
                .mustNot(QueryBuilders.termQuery(DEL_FLAG, true));
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder().withQuery(queryBuilder);
        FieldSortBuilder sortBuilder = SortBuilders.fieldSort("planStartTime").order(SortOrder.ASC);
        nativeSearchQueryBuilder.withSort(sortBuilder);
        SearchQuery searchQuery = nativeSearchQueryBuilder.build();

        List<Task> tasks = Lists.newArrayList(taskRepository.search(searchQuery));
        List<ProjectStatusFinishedVO> projectStatusFinishedVOS = new ArrayList<>();

        for (Task task : tasks) {

            List<ChildrenTask> childrenTasks = childTaskRepository.findByTaskIdEquals(task.getId());
            int finishedCount = 0;
            int taskCount = childrenTasks.size();
            double finishedStatus = 0;
            List<Integer> schedule = new ArrayList<>();
            for (ChildrenTask childrenTask : childrenTasks) {
                int days = diffDays(childrenTask.getPlanStartTime(), childrenTask.getPlanEndTime());
                schedule.add(days);
                if (("已完成").equals(childrenTask.getStatus())) {
                    finishedCount++;
                }
            }
            if (taskCount > 0) {
                finishedStatus = (double) finishedCount / taskCount;
            }
            String[] mark = new String[2];
            mark[0] = DateUtils.dateToString(task.getPlanStartTime(), DateUtils.YYYY_MM_DD_EN);
            mark[1] = (int) finishedStatus + "%";
            List<String[]> markWrap = new ArrayList<>();
            markWrap.add(mark);
            ProjectStatusFinishedVO projectStatusFinishedVO = new ProjectStatusFinishedVO();
            projectStatusFinishedVO.setId(task.getId());
            projectStatusFinishedVO.setName(task.getName());
            projectStatusFinishedVO.setStart(task.getPlanStartTime());
            projectStatusFinishedVO.setMark(markWrap);
            projectStatusFinishedVO.setSchedule(schedule);
            projectStatusFinishedVOS.add(projectStatusFinishedVO);
        }
        return projectStatusFinishedVOS;
    }

    /***
     * 当前用户的任务完成状态
     * @return
     */
    public List<ProjectStatusFinishedVO> queryFinishedStatusByUserId()  {
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        BoolQueryBuilder queryBuilder = boolQuery().must(termQuery("memberIds", userId))
                                                   .mustNot(termQuery(DEL_FLAG, Boolean.TRUE));
        List<Task> tasks = Lists.newArrayList(taskRepository.search(queryBuilder));
        List<ProjectStatusFinishedVO> projectStatusFinishedVOS = new ArrayList<>();
        for (Task task : tasks) {
            List<ChildrenTask> childrenTasks = childTaskRepository.findByTaskIdEqualsAndDelFlagNot(task.getId(),Boolean.TRUE);
            List<Integer> schedule = new ArrayList<>();
            for (ChildrenTask childrenTask : childrenTasks) {
                int days = diffDays(childrenTask.getPlanStartTime(), childrenTask.getPlanEndTime());
                schedule.add(days);
            }
            ProjectStatusFinishedVO projectStatusFinishedVO = new ProjectStatusFinishedVO();
            projectStatusFinishedVO.setId(task.getId());
            projectStatusFinishedVO.setName(task.getName());
            projectStatusFinishedVO.setStart(task.getPlanStartTime());
            projectStatusFinishedVO.setEnd(task.getPlanEndTime());
            projectStatusFinishedVO.setSchedule(schedule);
            projectStatusFinishedVO.setPriority(task.getPriority());
            if (task.getProjectId() != null) {
                projectStatusFinishedVO.setProjectName(task.getProjectName());
            } else {
                projectStatusFinishedVO.setProjectName(task.getBudgetName());
            }
            projectStatusFinishedVOS.add(projectStatusFinishedVO);
        }
        int size = projectStatusFinishedVOS.size();
        List<ProjectStatusFinishedVO> resultList = projectStatusFinishedVOS.subList(0,size>5?5:size);
        return resultList;
    }

    public Map<String, Integer[]> getFinanceChart(String projectId) {
        Map<String, Integer[]> map = new HashMap<>();
        Integer[] incomeArray = new Integer[12];
        Integer[] expensesArray = new Integer[12];
        Integer[] profitArray = new Integer[12];
        int temp;
        for (int i = 0; i < 12; i++) {
            incomeArray[i] = (int) (Math.random() * 100 + 1) * 9;
            expensesArray[i] = (int) (Math.random() * 100 + 1) * 8;
            if (incomeArray[i].equals(expensesArray[i])) {
                incomeArray[i] = incomeArray[i] + 87;
            }
            if (incomeArray[i] < expensesArray[i]) {
                temp = incomeArray[i];
                incomeArray[i] = expensesArray[i];
                expensesArray[i] = temp;
            }
            profitArray[i] = incomeArray[i] - expensesArray[i];
        }
        map.put("income", incomeArray);
        map.put("expenses", expensesArray);
        map.put("profit", profitArray);
        return map;
    }

    public List<MemberStatusVO> getMemberStatus(String projectId) {
        List<MemberStatusVO> list = new ArrayList<>();
        Project project = projectRepository.findOne(projectId);
        if (null == project || Boolean.TRUE.equals(project.getDelFlag())) {
            return list;
        }
        String[] projectMemberIds = project.getProjectMemberIds();
        BoolQueryBuilder builder = boolQuery().must(termQuery(PROJECT_ID, projectId))
                .mustNot(QueryBuilders.termQuery(DEL_FLAG, true));
        for (String id : projectMemberIds) {
            UserEO userEO = userEOService.getUserWithRoles(id);
            String usname = userEO.getUsname();
            String position = userEO.getContactAddress();
            int taskNum = taskRepository.countByProjectIdEqualsAndMemberIdsIn(projectId, id);
            builder.must(QueryBuilders.termQuery("memberIds", id))
                    .must(QueryBuilders.termQuery("taskStatus", ProjectStatusEnums.COMPLETE.getStatus()));
            int completeTaskNum = Lists.newArrayList(taskRepository.search(builder)).size();
            Double d = (double) completeTaskNum / taskNum;
            String mark = (int) (d * 100) + "%";
            String statistics = completeTaskNum + "/" + taskNum;
            list.add(new MemberStatusVO(0, usname, position, taskNum, completeTaskNum, mark,statistics));
        }
        return list;
    }

    public List<Object[]> queryTotle(String projectId) {
        //获取项目详情
        Project project = projectRepository.findOne(projectId);
        if (project == null || Boolean.TRUE.equals(project.getDelFlag())) {
            throw new IllegalArgumentException();
        }
        int yanshouDate = 15 + new Random().nextInt(15);
        //获取开始时间
        Date start = project.getProjectStartTime();
        int index = 0;
        //
        List<Object[]> list = new ArrayList<Object[]>();
        for (int i = 0; i < 7; i++) {
            Object[] obj = new Object[3];
            if (i < 4) {
                obj[0] = addDate(start, i + index);
                obj[1] = addDate(start, i + index + 1);
                if (i == 3) {
                    obj[2] = true;
                } else {
                    obj[2] = false;
                }
            }
            if (i == 4) {
                obj[0] = addDate(start, i + index);
                obj[1] = "--";
                obj[2] = false;
            }
            if (i == 5) {
                obj[0] = addDate(start, yanshouDate + i + index);
                obj[1] = "--";
                obj[2] = false;
            }
            if (i == 6) {
                obj[0] = addDate(start, yanshouDate + i + index);
                obj[1] = "--";
                obj[2] = false;
            }
            list.add(obj);
            index++;
        }
        return list;
    }

    public String addDate(Date start, int addNum) {
        Calendar c = Calendar.getInstance();
        c.setTime(start);
        if (addNum > 0) {
            c.add(Calendar.DAY_OF_MONTH, addNum);
        }
        return new SimpleDateFormat("yyyy.MM.dd").format(c.getTime());
    }
}
