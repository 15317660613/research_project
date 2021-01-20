package com.adc.da.budget.schedule;

import com.adc.da.budget.dao.UserEPDao;
import com.adc.da.budget.entity.ChildrenTask;
import com.adc.da.budget.entity.Daily;
import com.adc.da.budget.entity.Project;
import com.adc.da.budget.entity.Task;
import com.adc.da.budget.repository.ChildTaskRepository;
import com.adc.da.budget.repository.ProjectRepository;
import com.adc.da.budget.repository.TaskRepository;
import com.adc.da.budget.service.ChildrenTaskService;
import com.adc.da.budget.service.ProjectService;
import com.adc.da.budget.service.TaskInsertUpdateService;
import com.adc.da.budget.service.TaskService;
import com.adc.da.budget.vo.ChildrenTaskVO;
import com.adc.da.budget.vo.ProjectVO;
import com.adc.da.budget.vo.TaskVO;
import com.adc.da.login.util.UserUtils;
import com.adc.da.sys.dao.UserEODao;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.MapUtils;
import com.adc.da.util.utils.StringUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.aggregations.metrics.sum.SumBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.sql.SQLOutput;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.BeanUtils;

import static com.adc.da.budget.constant.ProjectSearchField.DEL_FLAG;
import static com.adc.da.word.policy.RenderPolicy.logger;

@Service("projectScheduleServiceForProjectMembers")
@Slf4j
public class ProjectScheduleServiceForProjectMembers {
    @Autowired
    private UserEPDao dao;
    @Autowired
    private UserEODao userEODao;
    @Autowired
    private ChildTaskRepository childTaskRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private TaskInsertUpdateService taskInsertUpdateService;

    @Autowired
    private ChildrenTaskService childrenTaskService;

    @Autowired
    private UserEPDao userEPDao;

    private ExecutorService executorService;

    //初始化线程池用来完成定时任务
    //@PostConstruct
    private void initThreadPool(){
        executorService=new ThreadPoolExecutor(3,3,5000, TimeUnit.SECONDS,new LinkedBlockingDeque<>(3));
        log.info("[初始化线程池，用来定时更新project成员]");
    }





    //定时任务，向线程池提交任务，对project,Task,childtask进行 成员更新
    //@Scheduled(cron = "*/60 * * * * ?")
    //@Scheduled(cron = "0 0 2 ? * *")
    public void updateProjectMembers(){
        executorService.submit(() ->{ updateProjectMembersForSubTask();});
        executorService.submit(() ->{ updateProjectMembersForTask();});
        executorService.submit(() ->{ updateProjectMembersForProject();});
        //updateProjectMembersForSubTask();
        //updateProjectMembersForTask();
        //updateProjectMembersForProject();
    }

    public void projectMemberUpdatingFunction() {
        //更新子task
        updateProjectMembersForSubTask();
        //更新task
        updateProjectMembersForTask();
        //更新project
         updateProjectMembersForProject();

    }



    //更新子task
    public void updateProjectMembersForSubTask() {
        while (true) {
            List<ChildrenTask> tasks=getTopOutdatedSubTaskFromES(100);
            //没有找到需要更新的project,跳出while循环，任务结束
            if (CollectionUtils.isEmpty(tasks)) {
                log.info("[没有子任务需要更新项目成员]");
                break;
            } else {
                //遍历需要更新的task
                System.out.println("[有子任务需要更新项目成员!]");
                for (ChildrenTask task: tasks) {
                    log.info("开始更新： "+task.getId()+","+task.getChildTaskName());
                    //是否需要更新的标志
                    boolean updatingNeededFlag=false;
                    //获取project Id
                    String projectId = task.getProjectId();
                    //获取部门列表
                    List<Map<String, String>> projectDeptInfoListMap = task.getDeptInfoListMap();
                    //遍历每一个部门
                    for (Map<String, String> projectDeptInfoMap : projectDeptInfoListMap) {
                        //获取部门ID
                        String deptId = projectDeptInfoMap.get("deptId");
                        List<String> orgEmpListInDB=getEmployeeListByDeptId(deptId);
                        //从ES中查出的部门的人员
                        List<String> orgEmpListInES = task.getDeptIdUserIdList().get(deptId);
                        //task.getDeptIdUserIdList().get(deptId).clear();
                        //task.getDeptIdUserIdList().put(deptId,orgEmpListInDB);
                        //project 人员发生了变化，
                        if(!isListEqual(orgEmpListInDB,orgEmpListInES)){
                            updatingNeededFlag=true;
                            break;
                        }
                    }

                    //project 人员需要更新
                    if(updatingNeededFlag==true){
                        task.setProjectMemberUpdatingTime(new Date());
                        ChildrenTaskVO childrenTaskVO=new ChildrenTaskVO();
                        BeanUtils.copyProperties(task,childrenTaskVO);
                        try {
                            childrenTaskService.updateWithoutShiroAuthentication(childrenTaskVO);
                        }
                        catch (Exception ex){
                            childrenTaskService.setProjectMemberUpdatingTime(task.getId(),new Date());
                            log.info(ex.getMessage());
                        }
                    }
                    //写入project 成员更新时间
                    else{
                        childrenTaskService.setProjectMemberUpdatingTime(task.getId(),new Date());

                    }
                    log.info("结束更新： "+task.getId()+","+task.getChildTaskName());
                }
            }
        }

    }

    //更新task
    public void updateProjectMembersForTask() {
        while (true) {
            List<Task> tasks=getTopOutdatedTaskFromES(100);
            //没有找到需要更新的project,跳出while循环，任务结束
            if (CollectionUtils.isEmpty(tasks)) {
                break;
            } else {
                log.info("【有task需要更新】");

                //遍历需要更新的task
                for (Task task: tasks) {
                    //是否需要更新的标志
                    boolean updatingNeededFlag=false;
                    //获取project Id
                    String projectId = task.getProjectId();
                    //获取部门列表
                    List<Map<String, String>> projectDeptInfoListMap = task.getDeptInfoListMap();
                    //遍历每一个部门
                    for (Map<String, String> projectDeptInfoMap : projectDeptInfoListMap) {
                        //获取部门ID
                        String deptId = projectDeptInfoMap.get("deptId");
                        List<String> orgEmpListInDB=getEmployeeListByDeptId(deptId);
                        //从ES中查出的部门的人员
                        List<String> orgEmpListInES = task.getDeptIdUserIdList().get(deptId);
                        if(!isListEqual(orgEmpListInDB,orgEmpListInES)){
                            updatingNeededFlag=true;
                            break;
                        }
                    }
                    //project 人员需要更新
                    if(updatingNeededFlag==true){
                        task.setProjectMemberUpdatingTime(new Date());
                        TaskVO taskVO=new TaskVO();

                        com.adc.da.budget.utils.BeanUtils.copyPropertiesIgnoreNullValue(task,taskVO);
                        try {
                            taskInsertUpdateService.updateWithoutShiroAuthentication(taskVO);
                        }
                        catch (Exception ex){
                            taskInsertUpdateService.setProjectMemberUpdatingTime(task.getId(),new Date());
                            log.info(ex.getMessage());
                        }
                    }
                    //写入project 成员更新时间
                    else{
                        taskInsertUpdateService.setProjectMemberUpdatingTime(task.getId(),new Date());
                    }

                }
            }
        }
    }

    //更新project
    public void updateProjectMembersForProject() {
        while (true) {
            List<Project> projects = getTopOutdatedProjectFromES(100);
            //没有找到需要更新的project,跳出while循环，任务结束
            if (CollectionUtils.isEmpty(projects)) {
                log.info("[项目成员不需要更新]");
                break;
            } else {
                log.info("[如下项目需要更新成员!]");
                for(Project project: projects)
                {
                    log.info(project.getName());
                }
                //遍历需要更新的project
                for (Project project : projects) {
                    //是否需要更新的标志
                    boolean updatingNeededFlag=false;
                    log.info(project.getId());
                    //获取project Id
                    String projectId = project.getId();
                    //获取部门列表
                    List<Map<String, String>> projectDeptInfoListMap = project.getDeptInfoListMap();
                    //遍历每一个部门
                    for (Map<String, String> projectDeptInfoMap : projectDeptInfoListMap) {
                        //获取部门ID
                        String deptId = projectDeptInfoMap.get("deptId");
                        List<String> orgEmpListInDB=getEmployeeListByDeptId(deptId);
                        //从ES中查出的部门的人员
                        List<String> orgEmpListInES = project.getDeptIdUserIdList().get(deptId);
                        //project.getDeptIdUserIdList().get(deptId).clear();
                        //project.getDeptIdUserIdList().put(deptId,orgEmpListInDB);
                        //project 人员发生了变化，
                        if(!isListEqual(orgEmpListInDB,orgEmpListInES)){
                            updatingNeededFlag=true;
                            break;
                        }
                    }

                    //project 人员需要更新
                    if(updatingNeededFlag==true||project.getProjectMemberUpdatingTime()==null){
                        project.setProjectMemberUpdatingTime(new Date());
                        ProjectVO projectVO=new ProjectVO();
                        BeanUtils.copyProperties(project,projectVO);
                        try {
                            projectService.updateWithoutShiroAuthentication(projectVO);
                        }catch (Exception ex){
                            updateProjectMemberDate(projectId,new Date());
                            log.info(ex.getMessage());
                        }
                    }else{
                        updateProjectMemberDate(projectId,new Date());
                    }
                }
            }
        }


    }

    //判断两个List<String>是否相同
    public  boolean isListEqual(List<String> list1, List<String> list2) {
        if(list1==null&&list2!=null||list2==null&&list1!=null){
            return false;
        }
        if (list1 == list2) {
            return true;
        }
        if ((list1 == null && list2 != null && list2.size() == 0)
                || (list2 == null && list1 != null && list1.size() == 0)) {
            return true;
        }
        if (list1.size() != list2.size()) {
            return false;
        }
        if (!list1.containsAll(list2)) {
            return false;
        }
        return true;
    }

    //通过id查询project
    public Project getProjectById(String id) {
        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                // .mustNot(QueryBuilders.termQuery(DEL_FLAG, true))
                .must(QueryBuilders.termQuery("id", id));
        List<Project> projects = Lists.newArrayList(projectRepository.search(queryBuilder));
        if (CollectionUtils.isNotEmpty(projects)) {
            return projects.get(0);
        } else {
            return null;
        }
    }

    //通过 project Id 查询 user map<usid,usname>
    public Map<String, String> getProjectMemberMapById(String id) {
        return dao.getProjectMemberMapById(id);
    }

    //通过deptId查询人员id list
    public List<String> getEmployeeListByDeptId(String deptId) {
        List<String> deptList = new LinkedList<>();
        deptList.add(deptId);
        return userEODao.getAllUserIdsByOrgIds(deptList);
    }

    //查询所有project的member
    public Map<String, List<String>> queryAllProjectMembers() {
        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .mustNot(QueryBuilders.termQuery(DEL_FLAG, true));
        List<Project> projects = Lists.newArrayList(projectRepository.search(queryBuilder));

        for (Project project : projects) {
            log.info(project.getId());
            if (project.getProjectMemberIds() != null) {
                String[] projectIds = project.getProjectMemberIds();
                for (String projectId : projectIds) {
                    log.info(StringUtils.join(projectIds,","));
                }

            }
        }
        return null;

    }


    //查询100条需要更新的project信息
    public List<Project> getTopOutdatedProjectFromES(int size) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str1 = "2020-1-1 0:0:0";
        Date time1 = null, time2 = null;
        try {
            time1 = sdf.parse(str1);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.MINUTE, -120);
            time2=calendar.getTime();

        } catch (Exception ex) {
            log.info(ex.getMessage());
        }

        BoolQueryBuilder bqr1=QueryBuilders.boolQuery()
                .must(QueryBuilders.existsQuery("projectMemberUpdatingTime"))
                .must(QueryBuilders.rangeQuery("projectMemberUpdatingTime").from(time1.getTime()).to(time2.getTime()));

        BoolQueryBuilder bqr2=QueryBuilders.boolQuery()
                .mustNot(QueryBuilders.existsQuery("projectMemberUpdatingTime"));


        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.should(bqr1);
        boolQueryBuilder.should(bqr2);


        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .mustNot(QueryBuilders.termQuery(DEL_FLAG, true))
                .must(QueryBuilders.existsQuery("deptInfoListMap"))
                .must(boolQueryBuilder);


        PageRequest page = new PageRequest(0, size);
        List<Project> projects = Lists.newArrayList(projectRepository.search(queryBuilder, page));
        return projects;

    }

    //查询100条需要更新的Task信息
    public List<Task> getTopOutdatedTaskFromES(int size) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str1 = "2020-1-1 0:0:0";
        Date time1 = null, time2 = null;
        try {
            time1 = sdf.parse(str1);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.MINUTE, -120);
            time2=calendar.getTime();

        } catch (Exception ex) {
            log.info(ex.getMessage());
        }

        BoolQueryBuilder bqr1=QueryBuilders.boolQuery()
                .must(QueryBuilders.existsQuery("projectMemberUpdatingTime"))
                .must(QueryBuilders.rangeQuery("projectMemberUpdatingTime").from(time1.getTime()).to(time2.getTime()));

        BoolQueryBuilder bqr2=QueryBuilders.boolQuery()
                .mustNot(QueryBuilders.existsQuery("projectMemberUpdatingTime"));

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.should(bqr1);
        boolQueryBuilder.should(bqr2);

        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .mustNot(QueryBuilders.termQuery(DEL_FLAG, true))
                .must(QueryBuilders.existsQuery("deptInfoListMap"))
               // .must(QueryBuilders.termQuery("id","8d3cf2fd-27a4-4c1a-80ff-61f628aaacfb"))

                .must(boolQueryBuilder)
                ;

        PageRequest page = new PageRequest(0, size);
        List<Task> tasks = Lists.newArrayList(taskRepository.search(queryBuilder, page));
        return tasks;

    }

    //查询100条需要更新的SubTask信息
    public List<ChildrenTask> getTopOutdatedSubTaskFromES(int size) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str1 = "2020-1-1 0:0:0";
        Date time1 = null, time2 = null;
        try {
            time1 = sdf.parse(str1);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.MINUTE, -120);
            time2=calendar.getTime();

        } catch (Exception ex) {
            log.info(ex.getMessage());
        }

        BoolQueryBuilder bqr1=QueryBuilders.boolQuery()
                .must(QueryBuilders.existsQuery("projectMemberUpdatingTime"))
                .must(QueryBuilders.rangeQuery("projectMemberUpdatingTime").from(time1.getTime()).to(time2.getTime()));

        BoolQueryBuilder bqr2=QueryBuilders.boolQuery()
                .mustNot(QueryBuilders.existsQuery("projectMemberUpdatingTime"));

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.should(bqr1);
        boolQueryBuilder.should(bqr2);

        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .mustNot(QueryBuilders.termQuery(DEL_FLAG, true))
               // .must(QueryBuilders.termQuery("id","acb5e850-7d5f-4fdf-bc54-d50416d67849"))
                .must(QueryBuilders.existsQuery("deptInfoListMap"))
                .must(boolQueryBuilder)
                ;

        PageRequest page = new PageRequest(0, size);
        List<ChildrenTask> tasks = Lists.newArrayList(childTaskRepository.search(queryBuilder, page));
        return tasks;

    }


    //更新项目组成员的update时间
    public void updateProjectMemberDate(String projectId, Date date) {
        Project project = getProjectById(projectId);
        if (project == null) return;
        project.setProjectMemberUpdatingTime(date);
        projectRepository.save(project);
    }



    //根据project id 更新 project member
    public void updateProjectMembersById(String projectId, String[] projectMemberIds) {
        Project project = getProjectById(projectId);
        if (project == null) return;

        String projectMemberNames = "";
        ArrayList<String> memberNamesList = new ArrayList<String>();
        List<Map<String, String>> mapList = new LinkedList<>();

        for (String projectMemberId : projectMemberIds) {
            Map<String, String> projectMemberMap = getProjectMemberMapById(projectMemberId);
            if (projectMemberMap != null) {
                mapList.add(projectMemberMap);
            }
            String memberName = projectMemberMap.get("USNAME").toString();
            if (StringUtils.isNotEmpty(memberName)) {
                memberNamesList.add(memberName);
            }

        }

        String[] memberNames = memberNamesList.toArray(new String[0]);

        for (int i = 0; i < projectMemberNames.length(); i++) {
            if (StringUtils.isNotEmpty(memberNames[i])) {
                projectMemberNames += memberNames[i];
                projectMemberNames += ",";
            }
        }
        if (projectMemberNames.endsWith(",")) {
            projectMemberNames = projectMemberNames.substring(0, projectMemberNames.length() - 1);
        }

        project.setProjectMemberNames(projectMemberNames);
        project.setMemberNames(memberNames);
        project.setMapList(mapList);
        project.setProjectMemberIds(projectMemberIds);

        projectRepository.save(project);

    }





}


