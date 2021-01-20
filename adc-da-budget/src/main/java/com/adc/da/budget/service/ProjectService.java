package com.adc.da.budget.service;

import com.adc.da.budget.constant.Role;
import com.adc.da.budget.dao.BudgetEODao;
import com.adc.da.budget.dao.ContractPartnerEODao;
import com.adc.da.budget.dao.MyProjectContractInvoiceEODao;
import com.adc.da.budget.dao.OrgListDao;
import com.adc.da.budget.dao.UserEPDao;
import com.adc.da.budget.dao.UserProjectCustomDao;
import com.adc.da.budget.dto.PageDTO;
import com.adc.da.budget.entity.BudgetEO;
import com.adc.da.budget.entity.Business;
import com.adc.da.budget.entity.ChildrenTask;
import com.adc.da.budget.entity.Daily;
import com.adc.da.budget.entity.Project;
import com.adc.da.budget.entity.ProjectContractInvoiceEO;
import com.adc.da.budget.entity.Task;
import com.adc.da.budget.entity.UserProjectCustom;
import com.adc.da.budget.entity.UserWithProjects;
import com.adc.da.budget.enums.ProjectStatusEnums;
import com.adc.da.budget.repository.BusinessRepository;
import com.adc.da.budget.repository.ChildTaskRepository;
import com.adc.da.budget.repository.DailyRepository;
import com.adc.da.budget.repository.ProjectRepository;
import com.adc.da.budget.repository.TaskRepository;
import com.adc.da.budget.repository.UserWithProjectsRepository;
import com.adc.da.budget.utils.ArrayUtils;
import com.adc.da.budget.utils.BeanUtils;
import com.adc.da.budget.utils.CommonUtil;
import com.adc.da.budget.utils.DoubleUtils;
import com.adc.da.budget.vo.DashBoardVO;
import com.adc.da.budget.vo.ProjectMinVO;
import com.adc.da.budget.vo.ProjectVO;
import com.adc.da.login.util.UserUtils;
import com.adc.da.superadmin.service.SuperAdminService;
import com.adc.da.sys.dao.DicTypeEODao;
import com.adc.da.sys.dao.OrgEODao;
import com.adc.da.sys.dao.UserEODao;
import com.adc.da.sys.entity.DicTypeEO;
import com.adc.da.sys.entity.OrgEO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.sys.service.DicTypeEOService;
import com.adc.da.sys.service.OrgEOService;
import com.adc.da.sys.service.UserEOService;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.BeanMapper;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.aggregations.metrics.sum.SumBuilder;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import static com.adc.da.budget.constant.ProjectSearchField.BUDGET_ID;
import static com.adc.da.budget.constant.ProjectSearchField.CREATE_TIME;
import static com.adc.da.budget.constant.ProjectSearchField.DEL_FLAG;
import static com.adc.da.budget.constant.ProjectSearchField.FINISHED_STATUS;
import static com.adc.da.budget.constant.ProjectSearchField.NAME;
import static com.adc.da.budget.constant.ProjectSearchField.PROJECT_LEADER;
import static com.adc.da.budget.constant.ProjectSearchField.PROJECT_LEADER_ID;
import static com.adc.da.budget.constant.ProjectSearchField.PROJECT_TYPE;

/**
 * @Auther: ZHAIKAIXUAN
 * @Date: 2018/11/19 14:33
 * @Description:
 */
@Service
@Slf4j
public class ProjectService extends BaseService<Project, String> {

    private static final String LOGIN_EXPIRED = "登陆可能过期，请登录！";

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserEPDao userEPDao;

    @Autowired
    private UserEOService userEOService;

    @Autowired
    private OrgEODao orgEODao;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private BudgetEOService budgetEOService;

    @Autowired
    private OrgEOService orgEOService;

    @Autowired
    private ChildTaskRepository childTaskRepository;

    @Autowired
    private DailyRepository dailyRepository;

    @Autowired
    private UserWithProjectsRepository userWithProjectsRepository;

    @Autowired
    private BudgetEODao budgetEODao;

    @Autowired
    private ChildrenTaskService childrenTaskService;

    @Autowired
    private UserEODao userEODao;

    @Autowired
    private OrgListDao orgListDao;

    @Autowired
    private SuperAdminService superAdminService;

    @Autowired
    private UserProjectCustomService userProjectCustomService;

    @Autowired
    private UserProjectCustomDao userProjectCustomDao;

    @Autowired
    private DicTypeEODao dicTypeEODao;


    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private BeanMapper beanMapper ;

    @Autowired
    private MyProjectContractInvoiceEODao myProjectContractInvoiceEODao;

    @Autowired
    private ContractPartnerEODao contractPartnerEODao;

    @Autowired
    private DicTypeEOService dicTypeEOService;

    //记录更新的线程数
    AtomicInteger count=new AtomicInteger(0);

    //锁
    ReentrantLock reentrantLock=new ReentrantLock();

    public void rebuildUserWithProjectByUserId(String userId) {
        Set<String> allBudgetIdSet = new HashSet<>();
        Set<String> allProjectIdSet = new HashSet<>();
        Set<String> allTaskIdSet = new HashSet<>();
        Set<String> allChildTaskIdSet = new HashSet<>();
        // 先找自己是业务管理员 业务负责人 的业务 这部分，该用户可以看所有项目任务子任务
        List<BudgetEO> adminBudgetEOList = budgetEODao.selectByAdminId(userId);
        List<BudgetEO> pmBudgetEOList = budgetEODao.selectByPM(userId);
        List<String> manageBudgetIdList = getBudgetIdList(adminBudgetEOList);
        manageBudgetIdList.addAll(getBudgetIdList(pmBudgetEOList));
        List<String> manageProjectIdList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(manageBudgetIdList)) {
            List<Project> projectList = projectRepository.findByBudgetIdIn(new HashSet<>(manageBudgetIdList));
            manageProjectIdList.addAll(getProjectIdList(projectList));
        }
        List<Project> leaderProjectList = getProjectListByLeaderId(userId);
        manageProjectIdList.addAll(getProjectIdList(leaderProjectList));
        List<Project> adminProjectList = getProjectListByAdminId(userId);
        manageProjectIdList.addAll(getProjectIdList(adminProjectList));
        // 根据能看所有的权限 取 任务 子任务
        List<String> manageTaskIdList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(manageBudgetIdList)) {
            List<Task> budgetTaskList = taskRepository.findByBudgetIdIn(manageBudgetIdList);
            manageTaskIdList.addAll(getTaskIdList(budgetTaskList));
        }
        if (CollectionUtils.isNotEmpty(manageProjectIdList)) {
            List<Task> projectTaskList = taskRepository.findByProjectIdIn(manageProjectIdList);
            manageTaskIdList.addAll(getTaskIdList(projectTaskList));
        }
        List<String> manageChildTaskIdList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(manageTaskIdList)){
            List<ChildrenTask> manageChildrenTaskList = childTaskRepository.findByTaskIdIn(manageTaskIdList);
            manageChildTaskIdList.addAll(getChildTaskIdList(manageChildrenTaskList));
        }
        // 根据成员做in查询，在里面的，就关联上
        List<Project> memberProjectList = projectRepository.findByProjectMemberIdsInAndDelFlagNot(userId,true);
        if (CollectionUtils.isNotEmpty(memberProjectList)) {
            for (Project project : memberProjectList){
                if (StringUtils.isNotEmpty(project.getBudgetId())) {
                    allBudgetIdSet.add(project.getBudgetId());
                }
                allProjectIdSet.add(project.getId());
            }
        }
        List<Task> memberTaskList = taskRepository.findByMemberIdsInAndDelFlagNot(userId,true);
        if (CollectionUtils.isNotEmpty(memberTaskList)) {
            for (Task task : memberTaskList){
                if (StringUtils.isNotEmpty(task.getBudgetId())) {
                    allBudgetIdSet.add(task.getBudgetId());
                }
                if (StringUtils.isNotEmpty(task.getProjectId())) {
                    allProjectIdSet.add(task.getProjectId());
                }
                allTaskIdSet.add(task.getId());
            }
        }
        List<ChildrenTask> memChildrenTask = childTaskRepository.findByMemberIdsInAndDelFlagNot(userId,true);
        if (CollectionUtils.isNotEmpty(memChildrenTask)) {
            for (ChildrenTask childrenTask : memChildrenTask){
                if (StringUtils.isNotEmpty(childrenTask.getTaskId())) {
                    allTaskIdSet.add(childrenTask.getTaskId());
                }
                allChildTaskIdSet.add(childrenTask.getId());
            }
        }
        allBudgetIdSet.addAll(manageBudgetIdList);
        allProjectIdSet.addAll(manageProjectIdList);
        allTaskIdSet.addAll(manageTaskIdList);
        allChildTaskIdSet.addAll(manageChildTaskIdList);
        UserWithProjects userWithProjects = new UserWithProjects();
        userWithProjects.setUserId(userId);
        userWithProjects.setBusinessIds(allBudgetIdSet);
        userWithProjects.setProjectIds(allProjectIdSet);
        userWithProjects.setTaskIds(allTaskIdSet);
        userWithProjects.setChildTaskIds(allChildTaskIdSet);
        userWithProjectsRepository.save(userWithProjects);
    }

    public List<String> getTaskIdList(List<Task> taskList){
        List<String> taskIdList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(taskList)) {
            for (Task task : taskList) {
                taskIdList.add(task.getId());
            }
        }
        return taskIdList;
    }

    public List<String> getChildTaskIdList(List<ChildrenTask> childrenTaskList){
        List<String> childrenTaskIdList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(childrenTaskList)) {
            for (ChildrenTask childrenTask : childrenTaskList) {
                childrenTaskIdList.add(childrenTask.getId());
            }
        }
        return childrenTaskIdList;
    }


    public List<String> getBudgetIdList(List<BudgetEO> budgetEOList){
        List<String> budgetIdList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(budgetEOList)) {
            for (BudgetEO budgetEO : budgetEOList) {
                budgetIdList.add(budgetEO.getId());
            }
        }
        return budgetIdList;
    }

    public List<Project> getProjectListByLeaderId(String userId) {
        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .mustNot(QueryBuilders.termQuery(DEL_FLAG, true))
                .must(QueryBuilders.termQuery("projectLeaderId", userId));
        ArrayList<Project> projectList = Lists.newArrayList(projectRepository.search(queryBuilder));
        return projectList;
    }
    public List<Project> getProjectListByAdminId(String userId){
        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .mustNot(QueryBuilders.termQuery(DEL_FLAG, true))
                .must(QueryBuilders.termQuery("projectLeaderId",userId));
        ArrayList<Project> projectList = Lists.newArrayList(projectRepository.search(queryBuilder));
        return projectList;
    }


    //定义内部类来完成userWithProject更新SubTask
    class UserWithProjectWorkerForSubTask implements  Runnable{
        String empId;
        List<String> subTaskIdList;

        UserWithProjectWorkerForSubTask(String empId,List<String> subTaskIdList){
            this.empId=empId;
            this.subTaskIdList=subTaskIdList;
        }
        public void run(){
            UserWithProjects userWithProjects = userWithProjectsRepository.findOne(empId);
            if(userWithProjects!=null){
                List<String> subTaskIdsInUserWithProjects=new LinkedList<>(userWithProjects.getChildTaskIds());
                userWithProjects.setChildTaskIds(new HashSet<>(subTaskIdList));
                userWithProjectsRepository.save(userWithProjects);

                if(CollectionUtils.isNotEmpty(subTaskIdsInUserWithProjects)){
                    List<ChildrenTask> childrenTaskList=childTaskRepository.findByIdIn(subTaskIdsInUserWithProjects);
                    List<String> taskIdList=new LinkedList<>();
                    for(ChildrenTask childrenTask:childrenTaskList){
                        taskIdList.add(childrenTask.getTaskId());
                    }


                    List<Task> taskList=taskRepository.findByIdIn(taskIdList);
                    Set<String> businessIdSet=new HashSet<>();
                    Set<String> projectIdSet=new HashSet<>();
                    Set<String> taskIdSet=new HashSet<>();
                    for(Task task: taskList){
                        String taskId=task.getId();
                        String budgetId=task.getBudgetId();
                        String projectId=task.getProjectId();

                        if(StringUtils.isNotEmpty(taskId)){
                            taskIdSet.add(task.getId());
                        }
                        if(StringUtils.isNotEmpty(budgetId)){
                            businessIdSet.add(task.getBudgetId());
                        }

                        if(StringUtils.isNotEmpty(projectId)){
                            projectIdSet.add(task.getProjectId());
                        }

                    }

                    userWithProjects.setTaskIds(taskIdSet);
                    userWithProjects.setBusinessIds(businessIdSet);
                    userWithProjects.setProjectIds(projectIdSet);
                }

            }
            count.getAndDecrement();
        }
    }


    //定义内部类来完成userWithProject更新task
    class UserWithProjectWorkerForTask implements  Runnable{
        String empId;
        List<String> taskIdList;

        UserWithProjectWorkerForTask(String empId,List<String> taskIdList){
            this.empId=empId;
            this.taskIdList=taskIdList;
        }
        public void run(){
            UserWithProjects userWithProjects = userWithProjectsRepository.findOne(empId);
            if(userWithProjects!=null){
                List<String> taskIdsInUserWithProjects=new LinkedList<>(userWithProjects.getTaskIds());
                taskIdList.addAll(taskIdsInUserWithProjects);
                userWithProjects.setTaskIds(new HashSet<>(taskIdList));

                if(CollectionUtils.isNotEmpty(taskIdList)){
                    List<Task> taskList=taskRepository.findByIdIn(taskIdList);
                    Set<String> businessIdSet=new HashSet<>();
                    Set<String> projectIdSet=new HashSet<>();
                    for(Task task: taskList){
                        String budgetId=task.getBudgetId();
                        String projectId=task.getProjectId();

                        if(StringUtils.isNotEmpty(budgetId)){
                            businessIdSet.add(task.getBudgetId());
                        }

                        if(StringUtils.isNotEmpty(projectId)){
                            projectIdSet.add(task.getProjectId());
                        }

                    }

                    userWithProjects.setBusinessIds(businessIdSet);
                    userWithProjects.setProjectIds(projectIdSet);
                }


                userWithProjectsRepository.save(userWithProjects);
            }
            count.getAndDecrement();
        }
    }



    //定义内部类来完成userWithProject更新
    class UserWithProjectWorker implements  Runnable{
        String empId;
        List<String> projectIdList;

        UserWithProjectWorker(String empId,List<String> projectIdList){
            this.empId=empId;
            this.projectIdList=projectIdList;
        }
        public void run(){
            UserWithProjects userWithProjects = userWithProjectsRepository.findOne(empId);
            if(userWithProjects!=null){
                List<String> projectIdsInUserWithProjects=new LinkedList<>(userWithProjects.getProjectIds());
                projectIdList.addAll(projectIdsInUserWithProjects);
                userWithProjects.setProjectIds(new HashSet<>(projectIdList));
                if(CollectionUtils.isNotEmpty(projectIdList)){
                    List<Project> projectList=projectRepository.findByIdIn(projectIdList);
                    Set<String> businessIdSet=new HashSet<>();
                    for(Project project: projectList){
                        String budgetId=project.getBudgetId();
                        if(StringUtils.isNotEmpty(budgetId)){
                            businessIdSet.add(project.getBudgetId());
                        }
                    }
                    userWithProjects.setBusinessIds(businessIdSet);
                    userWithProjectsRepository.save(userWithProjects);
                }



            }
            count.getAndDecrement();
        }
    }




    BigDecimal TEN_THOUSAND = BigDecimal.valueOf(10000);

    public List<OrgEO> queryChildOrg(List<OrgEO> orgEOList) {
        List<OrgEO> allOrgList = orgListDao.listAllOrg();
        List<OrgEO> resultList = new ArrayList<>();
        resultList.addAll(orgEOList);
        HashMap<String, String> hashMap = new HashMap<>(); //该map用于去重，根据orgID
        for (OrgEO orgEO : orgEOList) {
            hashMap.put(orgEO.getId(), orgEO.getId());
        }

        for (OrgEO orgEO : allOrgList) {
            for (OrgEO o : orgEOList) {
                if (orgEO.getParentIds().contains(o.getId())) {
                    if (null == hashMap.get(orgEO.getId())) {
                        resultList.add(orgEO);
                        hashMap.put(orgEO.getId(), orgEO.getId());
                    }
                }
                if (o.getParentIds().contains(orgEO.getId())) {
                    if (null == hashMap.get(orgEO.getId())) {
                        resultList.add(orgEO);
                        hashMap.put(orgEO.getId(), orgEO.getId());
                    }
                }
            }
        }
        return resultList;
    }

    //倒序
    private List<OrgEO> sortOrgEOList(List<OrgEO> orgEOList) {

        Collections.sort(orgEOList, new Comparator<OrgEO>() {
            @Override
            public int compare(OrgEO o1, OrgEO o2) {
                return o1.getParentIds().length() - o2.getParentIds().length();
            }
        });
        return orgEOList;
    }

    public List<OrgEO> queryChildOrg(String userId) {
        List<OrgEO> allOrgList = orgListDao.listAllOrg();
        List<OrgEO> resultList = new ArrayList<>();
        UserEO userEO = userEODao.getUserWithRoles(userId);

        List<OrgEO> myOrgEOList = userEO.getOrgEOList();
        sortOrgEOList(myOrgEOList);

        //可能有多个同级别的根
        Set<String> rootOrgIdSet = new HashSet<>();
        Set<String> myOrgIdSet = new HashSet<>();
        for (OrgEO org : myOrgEOList) {
            myOrgIdSet.add(org.getId());
            rootOrgIdSet.add(org.getId());
        }

        // 先从字典找一圈
        List<DicTypeEO> dicTypeEOList = dicTypeEOService.queryByList("FAKE_BENBU");
        if(CollectionUtils.isNotEmpty(dicTypeEOList)) {
            for (DicTypeEO dicTypeEO : dicTypeEOList) {
                if (StringUtils.contains(dicTypeEO.getDicTypeName(), userId)) {
                    String orgId = dicTypeEO.getDicTypeCode();
                    myOrgIdSet.add(orgId);
                    rootOrgIdSet.add(orgId);
                }
            }
        }


        //主任和项目管理员查看中心所有员工
        if (rootOrgIdSet.contains("USW7ASDVED")) {
            // 如果用户有了该根，就能看到所有的子节点了 ，数据资源中心领导们的组织机构都没有数据资源中心机构
            rootOrgIdSet.add("MH8JQV5TSN");
            myOrgIdSet.add("MH8JQV5TSN");
        }
        if( SecurityUtils.getSubject().hasRole(Role.PROJECT_ADMIN)){
            myOrgIdSet.add("MH8JQV5TSN"); //项目管理员能看到所有的日报
        }
        rootOrgIdSet.add("USW7ASDVED");

        for (OrgEO orgEO : allOrgList) {
            if (rootOrgIdSet.contains(orgEO.getId())&&!resultList.contains(orgEO)) {
                resultList.add(orgEO);
            }
        }
        // 属于我的子部门
        for (OrgEO orgEO : allOrgList) {
            if (StringContainsSetOne(orgEO.getParentIds(), myOrgIdSet)) {
                if (rootOrgIdSet.add(orgEO.getId())&&!resultList.contains(orgEO)) {
                    resultList.add(orgEO);
                }
            }
        }
        // 属于我的父部门
        for (OrgEO orgEO : allOrgList) {
            if (ListOneParentIdsContainsOrgId(myOrgEOList, orgEO.getId())) {
                if (rootOrgIdSet.add(orgEO.getId())&&!resultList.contains(orgEO)) {
                    resultList.add(orgEO);
                }
            }
        }
        return resultList;
    }

    private boolean StringContainsSetOne(String parentIds, Set<String> stringSet) {
        for (String str : stringSet) {
            if (StringUtils.isNotEmpty(parentIds) && parentIds.contains(str)) {
                return true;
            }
        }
        return false;
    }

    private boolean ListOneParentIdsContainsOrgId(List<OrgEO> orgEOList, String orgId) {
        for (OrgEO orgEO : orgEOList) {
            if (StringUtils.isNotEmpty(orgEO.getParentIds()) && orgEO.getParentIds().contains(orgId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @Description: 根据id删除 可以是ids
     * @auther: ZHAIKAIXUAN
     * @params:
     * @return:
     * @date: 2018/11/19 14:44
     */
    public String deleteBatch(String ids) throws Exception {
        return deleteBatch(ids, true);
    }

    /**
     * @param ids
     * @param checkPermission 是否进行权限校验
     * @return
     * @throws Exception
     */
    public String deleteBatch(String ids, boolean checkPermission) throws Exception {
        if (StringUtils.isBlank(ids)) {
            throw new AdcDaBaseException("删除失败,请选择要删除的项目！");
        }
        boolean manager = false;
        Subject subject = SecurityUtils.getSubject();
        if (!checkPermission || (subject.hasRole(Role.SUPER_ADMIN) || subject.hasRole(Role.ZHU_REN)
                || subject.hasRole(Role.PROJECT_ADMIN))) {
            manager = true;
        }
        String msg = "删除成功！";
        String userId = UserUtils.getUserId();
        if (checkPermission && StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException(LOGIN_EXPIRED);
        }
        String[] idArray = ids.split(",");
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        for (String id : idArray) {
            boolQueryBuilder.should(QueryBuilders.termQuery("projectId", id));
            boolQueryBuilder.mustNot(QueryBuilders.termQuery(DEL_FLAG, true));
        }
        Iterator<Task> taskIterator = taskRepository.search(boolQueryBuilder).iterator();
        if (checkPermission && taskIterator.hasNext()) {
            throw new AdcDaBaseException("项目下存在任务，无法删除");
        }
        BudgetEO budgetEO;
        if (null != idArray && idArray.length != 0) {
            for (String id : idArray) {
                Project project = projectRepository.findOne(id);
                if (null != project) {
                    //只有超级管理员、主任、业务创建人、业务经理、可以修改项目
                    budgetEO = budgetEOService.selectByPrimaryKey(project.getBudgetId());
                    if (null != budgetEO) {
                        if (!manager && !StringUtils.equals(userId, budgetEO.getPm())
                                && !StringUtils.equals(userId, budgetEO.getCreateUserId())) {
                            throw new AdcDaBaseException("您无权删除项目" + project.getName());
                        }
                    }
                    project.setDelFlag(true);
                    //删除项目时，删除用户和项目的关联关系
                    deleteUserProject(project);
                    projectRepository.save(project);
                    //projectHistoryEOService.operateProjectHistory(project, "delete");
                }
            }
        }
        return msg;
    }

    /**
     * add by liqiushi 20190411
     * 删除项目时，删除用户和项目的关联关系
     * 判断这人删掉项目之后，在这个项目所属的业务下，是否还有别的项目，
     * 如果有的话简单去掉这个项目的关联关系就可以了，
     * 如果没有的话也去掉这个用户和业务的关联关系
     * 如果最后这个用户关联的业务，项目，任务，子任务都是空的话，执行删除而不是更新关联关系
     */
    public void deleteUserProject(Project project) throws NoSuchMethodException {
        Set<String> userIds = getUserIds(project);
        childrenTaskService.setUserWithProjectsData(userWithProjectsRepository,
                UserWithProjects.class.getMethod("getProjectIds"), userIds, false, project.getId());
        /**
         * 判断当前人是否是业务经理或者业务创建人
         * 只是成员需要判断当前业务下是否还有他的项目存在，如果没有移除当前业务在用户关联表
         */
        BudgetEO budgetEO = budgetEODao.selectByPrimaryKey(project.getBudgetId());
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery(BUDGET_ID, budgetEO.getId()));
        for (String userId : userIds) {
            //不是业务经理或业务创建人
            if (!StringUtils.equals(userId, project.getPm()) &&
                    !StringUtils.equals(userId, budgetEO.getCreateUserId())) {
                BoolQueryBuilder qb = QueryBuilders.boolQuery()
                        .should(QueryBuilders.termQuery("projectLeaderId", userId))
                        .should(QueryBuilders.termQuery("projectMemberIds", userId));
                queryBuilder.must(qb);
                ArrayList<Project> projects = Lists.newArrayList(projectRepository.search(queryBuilder));
                if (null == projects || projects.size() == 0) {
                    UserWithProjects userWithProjects = userWithProjectsRepository.findOne(userId);
                    userWithProjects.getBusinessIds().remove(budgetEO.getId());
                    userWithProjectsRepository.save(userWithProjects);
                }
            }
        }
    }

    /**
     * @Description: 修改
     * @auther: ZHAIKAIXUAN
     * @params:
     * @return:
     * @date: 2018/11/19 14:44
     */
    public Project update(ProjectVO projectVO) throws Exception {
        return update(projectVO, true);
    }

    /***
     * @author zyh 新增判断业务下面的项目名是否重复
     * @param projectVO
     */
    private void checkSameProjectName(ProjectVO projectVO) {
        String budgetId = projectVO.getBudgetId();
        if (StringUtils.isNotEmpty(budgetId)) {
            List<Project> projects = projectRepository.findByBudgetIdAndDelFlagNot(budgetId, true);
            if (StringUtils.isNotEmpty(projects)) {
                for (Project project : projects) {
                    if (StringUtils.equals(projectVO.getName(), project.getName()) && !StringUtils
                            .equals(projectVO.getId(), project.getId())) {
                        throw new AdcDaBaseException("该项目已存在！");
                    }
                    continue;
                }
            }
        }
    }
    public Project resetClaim(ProjectVO projectVO){
        Project project = beanMapper.map(projectVO,Project.class);
        return projectRepository.save(project);
    }

    public Project update(ProjectVO projectVO, boolean checkPermission) throws Exception {
        String userId = UserUtils.getUserId();
        if (checkPermission && StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException(LOGIN_EXPIRED);
        }
        //检查业务下是否可以重名(经营类可以重名)
        if(projectVO.getProjectType()!=0){
            checkSameProjectName(projectVO);
        }
        Subject subject = SecurityUtils.getSubject();
        Project projectOri = projectRepository.findOne(projectVO.getId());
        if (checkPermission && (projectOri == null || Boolean.TRUE.equals(projectOri.getDelFlag()))) {
            throw new AdcDaBaseException("项目不存在");
        }
        Project pro = projectRepository.findOne(projectVO.getId());
        BudgetEO budgetEO = budgetEOService.selectByPrimaryKey(projectOri.getBudgetId());

        if (StringUtils.isNotEmpty(budgetEO.getBusinessAdminId())) {
            projectOri.setBusinessManagerId(budgetEO.getBusinessAdminId());
            projectOri.setBusinessManagerName(budgetEO.getBusinessAdminName());
        }
        //只有超级管理员、主任、业务创建人、业务经理以及项目经理可以修改项目
        if (!StringUtils.equals(userId, projectOri.getProjectLeaderId())
                && !StringUtils.equals(userId, budgetEO.getPm())
                && !StringUtils.equals(userId, budgetEO.getCreateUserId())
                && !StringUtils.equals(userId, projectOri.getProjectAdminId()) //项目管理员可以编辑项目
                && !StringUtils.equals(userId, budgetEO.getBusinessAdminId())
                && !subject.hasRole(Role.SUPER_ADMIN)
                && !subject.hasRole(Role.ZHU_REN)
                && !subject.hasRole(Role.PROJECT_ADMIN)
                && checkPermission) {
            throw new AdcDaBaseException("您无权修改项目" + pro.getName());
        }
        Set<String> memberIdSet = new HashSet<>();
        HashMap<String,List<String>> DeptUserIdMap = new HashMap<>();
        List<Map<String,String>> deptInfoListMap = projectVO.getDeptInfoListMap();// 所选部门ID和类型 如类型=1 只选当前自身 类型=2 包含子部门
        for(Map<String,String> map :deptInfoListMap){
            Set<String> tempSet =new HashSet<>();
            int type =  Integer.parseInt(map.get("type").toString());
            String deptId =  map.get("deptId").toString();
            if(type == 1){
                List<String> deptList = new ArrayList<>();
                deptList.add(deptId);
                List<UserEO> list = userEOService.getAllUserEOsByOrgIds(deptList);//根据部门id查询成员

                for(UserEO userEO:list){
                    memberIdSet.add(userEO.getUsid());
                    tempSet.add(userEO.getUsid());
                }
                DeptUserIdMap.put(deptId,new ArrayList<>(tempSet));
                /// pro.setDeptIdUserIdList(DeptUserIdMap);
            }else if(type == 2){
                List<String> deptIdList = new ArrayList<>();
                //List<UserEO> list = userEOService.getAllUserEOsByOrgIds(deptIdList);//根据部门id查询成员
                List<UserEO> list=orgEOService.listUserEOByOrgId(deptId);
                for(UserEO userEO:list){
                    memberIdSet.add(userEO.getUsid());
                    tempSet.add(userEO.getUsid());
                }
                DeptUserIdMap.put(deptId,new ArrayList<>(tempSet));
                //pro.setDeptIdUserIdList(DeptUserIdMap);
            }
        }
        List<OrgEO> orgEOList = orgEODao.queryOrgAll();
        projectVO.setDeptIdUserIdList(DeptUserIdMap);
        String[] projectMemberIds = projectVO.getProjectMemberIds();
        if (CollectionUtils.isNotEmpty(memberIdSet)||CollectionUtils.isNotEmpty(projectMemberIds)) {
            Set<String> projectMemberIdSet = new HashSet<>(Arrays.asList(projectMemberIds));
            projectMemberIdSet.addAll(memberIdSet);
            if (StringUtils.isNotEmpty(projectVO.getProjectAdminId())) { //将项目管理员加入项目成员里面去
                projectMemberIdSet.add(projectVO.getProjectAdminId());
            }
            if (StringUtils.isNotEmpty(projectVO.getProjectLeaderId())) {
                projectMemberIdSet.add(projectVO.getProjectLeaderId());
            }
//            projectVO.setProjectMemberIds(projectMemberIdSet.toArray(new String[projectMemberIdSet.size()]));
            List<String> projectMemberIdList = new ArrayList<>(projectMemberIdSet);
            List<UserEO> userEOList = userEODao.getUserWithRolesByUserIds(projectMemberIdList);
            projectVO.setProjectMemberIds(CommonUtil.getUserIdArr(userEOList));
            projectVO.setMemberNames(CommonUtil.getUserNameArr(userEOList));
            projectVO.setProjectMemberNames(StringUtils.join(CommonUtil.getUserNameArr(userEOList), ','));
            projectVO.setMapList(CommonUtil.userIdUsnameMapKv(userEOList));
            projectVO.setUserIdDeptNameMapList(CommonUtil.userIdDeptNamesMapKv(userEOList, orgEOList));
        }
        //如果更换了项目经理，需要同步改项目下所有的任务，子任务 项目负责人字段
        if (!StringUtils.equals(projectVO.getProjectLeaderId(), projectOri.getProjectLeaderId())) {
            updateProjectLeaderInTaskAndChildTask(projectVO);
        }
        Project project = BeanUtils.map(projectVO, Project.class);
        project.setModifyTime(new Date());
        project.setProjectLeaderId(projectVO.getProjectLeaderId());
        project.setProjectStartTime(projectVO.getProjectStartTime());

        if (StringUtils.isNotEmpty(projectVO.getProjectLeaderId())) {
            UserEO userEO = userEODao.getUserWithRoles(projectVO.getProjectLeaderId());
            if (null != userEO) {
                project.setProjectLeader(userEO.getUsname());
                List<OrgEO> deptList = userEO.getOrgEOList();
                if (CollectionUtils.isNotEmpty(deptList)) {
                    projectOri.setDeptId(deptList.get(0).getId());
                }
            }
        }
        //更新项目时随之更新用户和任务的关联关系
        updateUserUserWithProjectInMember(projectVO, pro);
        //updateProjectAdmin(projectVO, pro, budgetEO);
        // 复制属性
        BeanUtils.copyPropertiesIgnoreNullValue(project, projectOri);

        //获取deptId
//        UserEO currentUser = userEOService.getUserWithRoles(projectOri.getProjectLeaderId());
//        List<OrgEO> deptList = currentUser.getOrgEOList();
//        if (CollectionUtils.isNotEmpty(deptList)) {
//            projectOri.setDeptId(deptList.get(0).getId());
//        }
        //解决将内部联系人选择自己，成员选择其他人。保存后，成员中未自动加入“内部联系人”的问题
//        Project newProject = updateProjectMember(projectOri);

        // 保存
        projectOri.setProjectMemberUpdatingTime(null);
        Project st = projectRepository.save(projectOri);
        //判断是否变更所属业务字段，如果变更了需要进行子级转移
        if (!StringUtils.equals(st.getBudgetId(), pro.getBudgetId())) {
            checkBelongBudgetId(projectVO.getBudgetId(), pro);
        }
        return st;
    }

//    private Project updateProjectMember(Project project) {
//        //获取项目负责人ID
//        String projectLeaderId = project.getProjectLeaderId();
//        String[] projectMemberIds = project.getProjectMemberIds();
//        String[] projectMemberNames = project.getMemberNames();
//        List<Map<String, String>> memberMapList = project.getMapList();
//        List<String> projectMemberIdList = new ArrayList(Arrays.asList(projectMemberIds));
//        List<String> projectMemberNameList = new ArrayList(Arrays.asList(projectMemberNames));
//        if (!projectMemberIdList.contains(projectLeaderId)) {
//            projectMemberIdList.add(0, projectLeaderId);
//            projectMemberNameList.add(0, project.getProjectLeader());
//            String[] newProjectMemberIds = projectMemberIdList.toArray(new String[projectMemberIdList.size()]);
//            String[] newProjectMemberNames = projectMemberNameList.toArray(new String[projectMemberNameList.size()]);
//            project.setProjectMemberIds(newProjectMemberIds);
//            project.setMemberNames(newProjectMemberNames);
//            project.setProjectMemberNames(StringUtils.join(newProjectMemberNames, ','));
//            Map<String, String> memberMap = new HashMap<>();
//            memberMap.put("id", projectLeaderId);
//            memberMap.put(NAME, project.getProjectLeader());
//            memberMapList.add(0, memberMap);
//            project.setMapList(memberMapList);
//        }
//        return project;
//    }

    public String changeFinshStatus(String projectId, String btnType, String btnType1) {
        try {
            //btnType：进行中，变成审批中，完成状态的改变走审批流
            Project project = projectRepository.findFirstByIdAndAndDelFlagNot(projectId, true);
            project.setFinishedStatus(btnType1);
            projectRepository.save(project);
            List<Task> taskList = taskRepository.findByProjectIdAndDelFlagNot(project.getId(), true);
            if (CollectionUtils.isNotEmpty(taskList)) {
                for (Task task : taskList) {
                    if (StringUtils.equals(task.getTaskStatus(), btnType)) {
                        task.setTaskStatus(btnType1);
                        taskRepository.save(task);
                    }
                    List<ChildrenTask> childrenTaskList = childTaskRepository
                            .findByTaskIdAndDelFlagNot(task.getId(), true);
                    if (CollectionUtils.isNotEmpty(childrenTaskList)) {
                        for (ChildrenTask childrenTask : childrenTaskList) {
                            if (StringUtils.equals(childrenTask.getStatus(), btnType)) {
                                childrenTask.setStatus(btnType1);
                                childTaskRepository.save(childrenTask);
                            }
                        }
                    }
                }
            }
            return project.getFinishedStatus();
        } catch (Exception e) {
            throw new AdcDaBaseException("更改项目状态失败");
        }
    }

    /**
     * 需要判断变更所属业务之后，之前的业务下项目成员是否还有参与的项目或任务
     * 如果没有需要清除变更前的业务ID，加上新的业务ID
     */
    private void checkBelongBudgetId(String newId, Project project) {
        BudgetEO budgetEO = budgetEODao.selectByPrimaryKey(newId);
        Project one = projectRepository.findOne(project.getId());
        one.setBusinessCreateUserId(budgetEO.getCreateUserId());
        one.setPm(budgetEO.getPm());
        one.setBudget(budgetEO.getProjectName());
        projectRepository.save(one);
        //判断项目成员和项目经理是否是之前的业务的业务经理或者业务创建人
        List<String> ids = Lists.newArrayList(project.getProjectMemberIds());
        ids.add(project.getProjectLeaderId());
//        if (StringUtils.isNotEmpty(budgetEO.getBusinessAdminId())) {
//            ids.add(budgetEO.getBusinessAdminId());
//        }
        for (String id : ids) {
            if (StringUtils.equals(id, project.getPm()) || StringUtils.equals(id, project.getBusinessCreateUserId())) {
                continue;
            }
            List<Project> projects = projectRepository
                    .findByBudgetIdAndProjectMemberIdsInAndDelFlagNot(project.getBudgetId(), id, Boolean.TRUE);
            List<Task> tasks = taskRepository
                    .findByBudgetIdAndMemberIdsInAndDelFlagNot(project.getBudgetId(), id, Boolean.TRUE);
            //当前人在之前的业务没有参与的项目或任务，在关联表清除业务ID并添加新的业务ID
            if (CollectionUtils.isEmpty(projects) && CollectionUtils.isEmpty(tasks)) {
                UserWithProjects userWithProjects = userWithProjectsRepository.findOne(id);
                //清除旧ID
                userWithProjects.getBusinessIds().remove(project.getBudgetId());
                //添加新ID
                userWithProjects.getBusinessIds().add(newId);
                userWithProjectsRepository.save(userWithProjects);
            }
        }
        //更新子级业务创建人以及业务经理字段
        List<Task> tasks = taskRepository.findByProjectIdAndDelFlagNot(project.getId(), Boolean.TRUE);
        if (CollectionUtils.isNotEmpty(tasks)) {
            for (Task task : tasks) {
                task.setBusinessCreateUserId(budgetEO.getCreateUserId());
                task.setPm(budgetEO.getPm());
                taskRepository.save(task);
                List<ChildrenTask> childrenTasks = childTaskRepository
                        .findByTaskIdEqualsAndDelFlagNot(task.getId(), Boolean.TRUE);
                if (CollectionUtils.isNotEmpty(childrenTasks)) {
                    for (ChildrenTask childrenTask : childrenTasks) {
                        childrenTask.setBusinessCreateUserId(budgetEO.getCreateUserId());
                        childrenTask.setPm(budgetEO.getPm());
                        childTaskRepository.save(childrenTasks);
                    }
                }
            }
        }

    }

    private void updateProjectLeaderInTaskAndChildTask(ProjectVO projectVO) {
        List<Task> tasks = taskRepository.findByProjectIdAndDelFlagNot(projectVO.getId(), Boolean.TRUE);
        if (CollectionUtils.isNotEmpty(tasks)) {
            for (Task task : tasks) {
                task.setProjectLeaderId(projectVO.getProjectLeaderId());
                List<ChildrenTask> childrenTasks = childTaskRepository
                        .findByTaskIdEqualsAndDelFlagNot(task.getId(), Boolean.TRUE);
                if (CollectionUtils.isNotEmpty(childrenTasks)) {
                    for (ChildrenTask childrenTask : childrenTasks) {
                        childrenTask.setProjectLeaderId(projectVO.getProjectLeaderId());
                        childTaskRepository.save(childrenTask);
                    }
                }
                taskRepository.save(task);
            }
        }
    }

    /**
     * add by liqiushi 20190411 更新项目时随之更新用户和任务的关联关系
     * project 是历史    formProject是新的
     */
    public void updateUserUserWithProjectInMember(ProjectVO formProject, Project project) throws Exception {
        //移除的成员ID
        //需要判断当前业务下是否还有参与的项目，任务，子任务，如果没有 删除业务数据
        List<String> delIds = ArrayUtils.compare(formProject.getProjectMemberIds(), project.getProjectMemberIds());
        List<Project> projectList = projectRepository.findByBudgetId(project.getBudgetId());
        List<Task> taskList = taskRepository.findByBudgetId(project.getBudgetId());
        BudgetEO budgetEO = budgetEODao.selectByPrimaryKey(project.getBudgetId());
        if (CollectionUtils.isNotEmpty(delIds)) {
            for (String deleteUserId : delIds) {
                boolean removeFlag = true;
                if (StringUtils.isEmpty(deleteUserId)) {
                    continue;
                }
                if (StringUtils.equals(deleteUserId, budgetEO.getPm()) || StringUtils.equals(deleteUserId, budgetEO.getCreateUserId())) {
                    removeFlag = false;
                }
                if (CollectionUtils.isNotEmpty(taskList)) {
                    for (Task task : taskList) {
                        if (CommonUtil.arrayContains(task.getMemberIds(),deleteUserId)>-1){
                            removeFlag = false; //如果当前项目的所在的业务下 任何一个包含 被移除的项目成员，那么不去移该人员业务关系
                            break;
                        }
                    }
                }
                if (CollectionUtils.isNotEmpty(projectList)) {
                    for (Project proj : projectList) {
                        if (CommonUtil.arrayContains(proj.getProjectMemberIds(),deleteUserId)>-1 && !StringUtils.equals(proj.getId(),project.getId())){
                            removeFlag = false; //如果当前项目的所在的业务下 任何一个包含 被移除的项目成员，那么不去移该人员业务关系
                            break;
                        }
                    }
                }
                UserWithProjects userWithProjects = userWithProjectsRepository.findOne(deleteUserId);
                if (null!=userWithProjects) {
                    userWithProjects.getProjectIds().remove(project.getId());
                    if (removeFlag) {
                        userWithProjects.getBusinessIds().remove(project.getBudgetId());
                    }
                    userWithProjectsRepository.save(userWithProjects);
                }
            }
        }
        //新增的成员ID
        String[] newIds = formProject.getProjectMemberIds();
        if (CollectionUtils.isNotEmpty(newIds)) {
            for (String newId : newIds) {
                if (StringUtils.isEmpty(newId)) {
                    continue;
                }
                UserWithProjects userWithProjects = userWithProjectsRepository.findOne(newId);
                //如果是新用户，新增数据到用户关联
                if (null == userWithProjects) {
                    userWithProjects = new UserWithProjects();
                    userWithProjects.setUserId(newId);
                }
                userWithProjects.getProjectIds().add(project.getId());
                userWithProjects.getBusinessIds().add(project.getBudgetId());
                userWithProjectsRepository.save(userWithProjects);
            }
        }

        //拿到当前项目所有的任务以及子任务
        List<Task> projectTaskList = taskRepository.findByProjectIdAndDelFlagNot(project.getId(), Boolean.TRUE);
        Set<String> taskIdSet = new HashSet<>();
        Set<String> allChildTaskIds = new HashSet<>();
        if (CollectionUtils.isNotEmpty(projectTaskList)) {
            for (Task task : projectTaskList) {
                taskIdSet.add(task.getId());
            }
        }
        taskIdSet.removeAll(Collections.singleton(null));
        List<ChildrenTask> childrenTaskList = new ArrayList<>();
        if(!taskIdSet.isEmpty()){
            childrenTaskList = childTaskRepository.findByTaskIdInAndDelFlagNot(taskIdSet,Boolean.TRUE);
        }
        if (CollectionUtils.isNotEmpty(childrenTaskList)) {
            for (ChildrenTask childrenTask : childrenTaskList) {
                allChildTaskIds.add(childrenTask.getId());
            }
        }

        //判断项目经理是否有变化 老的项目经理在成员移除的时候就被干掉了，这里面只需要处理新的
        if (!StringUtils.equals(project.getProjectLeaderId(), formProject.getProjectLeaderId())) {
            if (StringUtils.isNotEmpty(formProject.getProjectLeaderId())) {
                UserWithProjects userWithProjects = userWithProjectsRepository.findOne(formProject.getProjectLeaderId());
                if (null == userWithProjects) {
                    userWithProjects = new UserWithProjects();
                }

                userWithProjects.getChildTaskIds().addAll(allChildTaskIds);
                userWithProjects.getTaskIds().addAll(taskIdSet);
                userWithProjects.getBusinessIds().add(budgetEO.getId());
                userWithProjects.getProjectIds().add(formProject.getId());
                userWithProjectsRepository.save(userWithProjects);
            }
            //老的项目负责人不当前项目管理员 且 不是业务管理员 且不 业务负责人
            if (!StringUtils.equals(formProject.getProjectAdminId(),project.getProjectLeaderId())
                    && !StringUtils.equals(project.getProjectAdminId(),budgetEO.getPm())
                    && !StringUtils.equals(project.getProjectAdminId(),budgetEO.getBusinessAdminId())
            ) {
                if (StringUtils.isNotEmpty(project.getProjectLeaderId())) {
                    UserWithProjects userWithProjects = userWithProjectsRepository.findOne(project.getProjectLeaderId());
                    for (Task task : projectTaskList) {
                        if (CommonUtil.arrayContains(task.getMemberIds(), project.getProjectLeaderId()) < 0) {
                            userWithProjects.getTaskIds().remove(task.getId());
                        }
                    }
                    for (ChildrenTask childrenTask : childrenTaskList) {
                        if (CommonUtil.arrayContains(childrenTask.getMemberIds(), project.getProjectLeaderId()) < 0
                                && StringUtils.equals(childrenTask.getProjectId(), project.getId())) {
                            userWithProjects.getTaskIds().remove(childrenTask.getId());
                        }
                    }
                    userWithProjectsRepository.save(userWithProjects);
                }
            }
        }

        if (!StringUtils.equals(formProject.getProjectAdminId(), project.getProjectAdminId())) { //确实变更了
            if (StringUtils.isNotEmpty(formProject.getProjectAdminId())) {
                UserWithProjects userWithProjects = userWithProjectsRepository.findOne(formProject.getProjectAdminId());
                if (CollectionUtils.isNotEmpty(childrenTaskList)) {
                    for (ChildrenTask childrenTask : childrenTaskList) {
                        allChildTaskIds.add(childrenTask.getId());
                    }
                }
                if (null == userWithProjects) {
                    userWithProjects = new UserWithProjects();
                }

                userWithProjects.getChildTaskIds().addAll(allChildTaskIds);
                userWithProjects.getTaskIds().addAll(taskIdSet);
                userWithProjects.getBusinessIds().add(budgetEO.getId());
                userWithProjects.getProjectIds().add(formProject.getId());
                userWithProjectsRepository.save(userWithProjects);
            }
            //老的项目管理员不当前项目负责人 且 不是业务管理员 且不 业务负责人
            if (!StringUtils.equals(project.getProjectAdminId(),formProject.getProjectLeaderId())
                && !StringUtils.equals(project.getProjectAdminId(),budgetEO.getPm())
                && !StringUtils.equals(project.getProjectAdminId(),budgetEO.getBusinessAdminId())
            ) {
                if (StringUtils.isNotEmpty(project.getProjectAdminId())) {
                    UserWithProjects userWithProjects = userWithProjectsRepository.findOne(project.getProjectAdminId());
                    for (Task task : projectTaskList) {
                        if (CommonUtil.arrayContains(task.getMemberIds(), project.getProjectAdminId()) < 0) {
                            userWithProjects.getTaskIds().remove(task.getId());
                        }
                    }
                    for (ChildrenTask childrenTask : childrenTaskList) {
                        if (CommonUtil.arrayContains(childrenTask.getMemberIds(), project.getProjectAdminId()) < 0
                                && StringUtils.equals(childrenTask.getProjectId(), project.getId())) {
                            userWithProjects.getTaskIds().remove(childrenTask.getId());
                        }
                    }
                    userWithProjectsRepository.save(userWithProjects);
                }
            }
        }
    }

    public void updateProjectAdmin(ProjectVO newProject, Project oldProject, BudgetEO budgetEO) throws Exception {
        //判断项目管理员是否有变化
        if (!StringUtils.equals(newProject.getProjectAdminId(), oldProject.getProjectAdminId()) &&
                !StringUtils.equals(newProject.getProjectAdminId(), newProject.getProjectLeaderId())
                && StringUtils.isNotEmpty(newProject.getProjectAdminId())) {
            Set<String> removeTaskIdSet = new HashSet<>();
            Set<String> removeChildTaskIdSet = new HashSet<>();
            //拿到当前项目所有的任务以及子任务
            List<Task> tasks = taskRepository.findByProjectIdAndDelFlagNot(newProject.getId(), Boolean.TRUE);
            Set<String> taskIds = new HashSet<>();
            Set<String> allChildTaskIds = new HashSet<>();
            if (CollectionUtils.isNotEmpty(tasks)) {
                for (Task task : tasks) {
                    if (!StringUtils.equals(task.getApproveUserId(), oldProject.getProjectAdminId()) //不是负责人
                            && CommonUtil.arrayContains(task.getMemberIds(), oldProject.getProjectAdminId()) < 0
                            && !StringUtils.equals(oldProject.getProjectAdminId(), newProject.getProjectLeaderId())) { //不是成员
                        removeTaskIdSet.add(task.getId());
                    }
                    taskIds.add(task.getId());
                }
            }
            if (CollectionUtils.isNotEmpty(taskIds)) {
                List<ChildrenTask> childrenTasks = childTaskRepository
                        .findByTaskIdInAndDelFlagNot(taskIds, Boolean.TRUE);
                if (CollectionUtils.isNotEmpty(childrenTasks)) {
                    for (ChildrenTask childrenTask : childrenTasks) {
                        if (!StringUtils.equals(childrenTask.getApproveUserId(), oldProject.getProjectAdminId())
                                && CommonUtil.arrayContains(childrenTask.getMemberIds(), oldProject.getProjectAdminId())
                                < 0
                                && !StringUtils.equals(oldProject.getProjectAdminId(), newProject.getProjectLeaderId())) {
                            removeChildTaskIdSet.add(childrenTask.getId());
                        }
                        allChildTaskIds.add(childrenTask.getId());
                    }
                }
            }
            //老的项目管理员不是 业务管理员
            if (!StringUtils.equals(oldProject.getProjectAdminId(), newProject.getBusinessAdminId())) { //确实变更了
                UserWithProjects oldUserWithProjects = userWithProjectsRepository
                        .findOne(oldProject.getProjectAdminId());
                if (null != oldUserWithProjects) {
                    if (!StringUtils.equals(oldProject.getProjectAdminId(), budgetEO.getPm())
                            && CommonUtil.arrayContains(newProject.getProjectMemberIds(), oldProject.getProjectAdminId())
                            < 0
                            && !StringUtils.equals(oldProject.getProjectAdminId(), budgetEO.getBusinessAdminId())
                            && !StringUtils.equals(oldProject.getProjectAdminId(), newProject.getProjectLeaderId())) {
                        oldUserWithProjects.getBusinessIds().remove(newProject.getBudgetId());
                        oldUserWithProjects.getProjectIds().remove(newProject.getId());
                        oldUserWithProjects.getTaskIds().removeAll(taskIds);
                        oldUserWithProjects.getChildTaskIds().removeAll(allChildTaskIds);
                    } else {
                        oldUserWithProjects.getTaskIds().removeAll(removeTaskIdSet);
                        oldUserWithProjects.getChildTaskIds().removeAll(removeChildTaskIdSet);
                    }
                    userWithProjectsRepository.save(oldUserWithProjects);
                }
            }
            //处理新项目管理员
            UserWithProjects newUserWithProjects = userWithProjectsRepository
                    .findOne(newProject.getProjectAdminId());
            if (null == newUserWithProjects) {
                newUserWithProjects = new UserWithProjects();
                newUserWithProjects.setUserId(newProject.getProjectAdminId());
            }
            newUserWithProjects.getTaskIds().addAll(taskIds);
            newUserWithProjects.getChildTaskIds().addAll(allChildTaskIds);
            newUserWithProjects.getProjectIds().add(newProject.getId());
            newUserWithProjects.getBusinessIds().add(newProject.getBudgetId());
            userWithProjectsRepository.save(newUserWithProjects);
            if (StringUtils.isNotEmpty(newProject.getProjectAdminId())) {
                userProjectCustomService.upadteUserProjectCustomEOByProjectId(newProject.getProjectAdminId(),
                        newProject.getId(),
                        newProject.getBudgetId());
            }
        }
    }

    public void updateProjectAdmin(Project newProject, Project oldProject, BudgetEO budgetEO) {
        //判断项目管理员是否有变化
        if (!StringUtils.equals(newProject.getProjectAdminId(), oldProject.getProjectAdminId()) &&
                !StringUtils.equals(newProject.getProjectAdminId(), newProject.getProjectLeaderId())
                && StringUtils.isNotEmpty(newProject.getProjectAdminId())) {
            Set<String> removeTaskIdSet = new HashSet<>();
            Set<String> removeChildTaskIdSet = new HashSet<>();
            //拿到当前项目所有的任务以及子任务
            List<Task> tasks = taskRepository.findByProjectIdAndDelFlagNot(newProject.getId(), Boolean.TRUE);
            Set<String> taskIds = new HashSet<>();
            Set<String> allChildTaskIds = new HashSet<>();
            if (CollectionUtils.isNotEmpty(tasks)) {
                for (Task task : tasks) {
                    if (!StringUtils.equals(task.getApproveUserId(), oldProject.getProjectAdminId()) //不是负责人
                            && CommonUtil.arrayContains(task.getMemberIds(), oldProject.getProjectAdminId()) < 0) { //不是成员
                        removeTaskIdSet.add(task.getId());
                    }
                    taskIds.add(task.getId());
                }
            }
            if (CollectionUtils.isNotEmpty(taskIds)) {
                List<ChildrenTask> childrenTasks = childTaskRepository
                        .findByTaskIdInAndDelFlagNot(taskIds, Boolean.TRUE);
                if (CollectionUtils.isNotEmpty(childrenTasks)) {
                    for (ChildrenTask childrenTask : childrenTasks) {
                        if (!StringUtils.equals(childrenTask.getApproveUserId(), oldProject.getProjectAdminId())
                                && CommonUtil.arrayContains(childrenTask.getMemberIds(), oldProject.getProjectAdminId())
                                < 0) {
                            removeChildTaskIdSet.add(childrenTask.getId());
                        }
                        allChildTaskIds.add(childrenTask.getId());
                    }
                }
            }

            if (!StringUtils.equals(oldProject.getProjectAdminId(), newProject.getBusinessAdminId())) { //确实变更了
                UserWithProjects oldUserWithProjects = userWithProjectsRepository
                        .findOne(oldProject.getProjectAdminId());
                if (null != oldUserWithProjects) {
                    if (!StringUtils.equals(oldProject.getProjectAdminId(), budgetEO.getPm())
                            && CommonUtil.arrayContains(newProject.getProjectMemberIds(), oldProject.getProjectAdminId())
                            < 0
                            && !StringUtils.equals(oldProject.getProjectAdminId(), budgetEO.getBusinessAdminId())
                    ) {
                        oldUserWithProjects.getBusinessIds().remove(newProject.getBudgetId());
                        oldUserWithProjects.getProjectIds().remove(newProject.getId());
                        oldUserWithProjects.getTaskIds().removeAll(taskIds);
                        oldUserWithProjects.getChildTaskIds().removeAll(allChildTaskIds);
                    } else {
                        oldUserWithProjects.getTaskIds().removeAll(removeTaskIdSet);
                        oldUserWithProjects.getChildTaskIds().removeAll(removeChildTaskIdSet);
                    }
                    userWithProjectsRepository.save(oldUserWithProjects);
                }
                //处理新项目管理员
                if(StringUtils.isNotEmpty(newProject.getProjectAdminId())){
                    UserWithProjects newUserWithProjects = userWithProjectsRepository
                            .findOne(newProject.getProjectAdminId());
                    if (null == newUserWithProjects) {
                        newUserWithProjects = new UserWithProjects();
                        newUserWithProjects.setUserId(newProject.getProjectAdminId());
                    }
                    newUserWithProjects.getTaskIds().addAll(taskIds);
                    newUserWithProjects.getChildTaskIds().addAll(allChildTaskIds);
                    newUserWithProjects.getProjectIds().add(newProject.getId());
                    newUserWithProjects.getBusinessIds().add(newProject.getBudgetId());
                    userWithProjectsRepository.save(newUserWithProjects);
                }
            }
        }
    }

    private String getUserNameFromMapList(List<Map<String, String>> mapList, String userId) {
        for (Map<String, String> map : mapList) {
            String uid = map.get("id");
            if (StringUtils.isNotEmpty(uid) && StringUtils.equals(uid, userId)) {
                return map.get("name");
            }
        }
        return null;
    }

    public ResponseMessage<String> checkChangeMember(String projectId, String[] newMemberIdArr) {
        Project project = projectRepository.findById(projectId);
        String[] oldMemberIdArr = project.getProjectMemberIds();
        List<String> delUserList = ArrayUtils.compare(newMemberIdArr, oldMemberIdArr);
        Set<String> taskIdSet = new HashSet<>();
        List<Task> taskList = taskRepository.findByProjectIdAndDelFlagNot(projectId, true);
        if (CollectionUtils.isNotEmpty(delUserList)) {
            for (String userId : delUserList) {
                if (CollectionUtils.isNotEmpty(delUserList)) {
                    for (Task task : taskList) {
                        if (!StringUtils.equals(task.getTaskStatus(), "已完成")) {
                            List<Map<String, String>> userIdNameMapList = task.getMapsList();
                            String userName = getUserNameFromMapList(userIdNameMapList, userId);
                            if (StringUtils.isNotEmpty(userName)) {
                                return Result.error(userName + "在项目下已有任务，请先将其移除任务");
                            }
                        }
                        taskIdSet.add(task.getId());
                    }
                }
                if (CollectionUtils.isNotEmpty(taskIdSet)) {
                    List<ChildrenTask> childrenTaskList = childTaskRepository
                            .findByTaskIdInAndDelFlagNot(taskIdSet, true);
                    if (CollectionUtils.isNotEmpty(childrenTaskList)) {
                        for (ChildrenTask childrenTask : childrenTaskList) {
                            if (!StringUtils.equals(childrenTask.getStatus(), "已完成")) {
                                List<Map<String, String>> userIdNameMapList = childrenTask.getMapsList();
                                String userName = getUserNameFromMapList(userIdNameMapList, userId);
                                if (StringUtils.isNotEmpty(userName)) {
                                    return Result.error(userName + "在项目下已有子任务，请先将其移除任务");
                                }
                            }
                        }
                    }
                }
            }
        }
        return Result.success();
    }

    /**
     * @Description: 查找
     * @auther: ZHAIKAIXUAN
     * @params:
     * @return:
     * @date: 2018/11/19 14:45
     */
    public Project querySingle(String id) throws Exception {
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException(LOGIN_EXPIRED);
        }
        boolean manager = false;
        Project project = projectRepository.findByIdAndDelFlagNot(id, Boolean.TRUE);
        if (project != null && StringUtils.isNotEmpty(project.getBudgetId())) {
            BudgetEO budgetEO = budgetEOService.selectByPrimaryKey(project.getBudgetId());
            if (budgetEO != null) {
                project.setBudget(budgetEO.getProjectName());
                project.setBusinessAdminId(budgetEO.getBusinessAdminId());
                project.setBusinessAdminName(budgetEO.getBusinessAdminName());
            }
            if (StringUtils.isNotEmpty(project.getBusinessId())) {
                project.setBusiness(getBusinessName(project.getBusinessId()));
            }

            if (userId.equals(project.getProjectLeaderId()) || userId.equals(project.getPm())
                    || userId.equals(project.getBusinessCreateUserId()) || userId.equals(project.getProjectAdminId())
                    || userId.equals(project.getBusinessAdminId())) {
                manager = true;
            }
            // 超级管理员有编辑任意业务的权限
            if (superAdminService.isSuperAdmin()) {
                manager = true;
            }
            project.setManager(manager);
            //add by liqiushi 20190425 添加状态按钮校验
            if ((StringUtils.equals(userId, project.getProjectLeaderId()) || userId.equals(project.getProjectAdminId()))
                    && StringUtils.equals("进行中", project.getFinishedStatus())) {
                project.setBtnFlag("0");
            }
            //添加上级部长审批下一节点
            List<String> leaders = userEPDao.querySuperLeader(project.getDeptId());
            String approveUserIds = "";
            if (CollectionUtils.isNotEmpty(leaders)) {
                for (String userId1 : leaders) {
                    approveUserIds += userId1 + ",";
                }
                project.setApproveUserId(approveUserIds);
            }
            List<Task> taskList = taskRepository.findByProjectId(id);
            if (CollectionUtils.isNotEmpty(taskList)) {
                List<String> memberIdList = new ArrayList<>();
                for (Task task : taskList) {
                    if (StringUtils.equals(task.getTaskStatus(),ProjectStatusEnums.COMPLETE.getStatus())){
                        continue;
                    }
                    if (null!=task.getDelFlag()&&task.getDelFlag()){
                        continue;
                    }
                    if (CollectionUtils.isNotEmpty(task.getMemberIds())){
                        memberIdList.addAll(Arrays.asList(task.getMemberIds()));
                    }
                }
                project.setTaskMemberIdList(memberIdList);
            }

            return project;
        }
        return null;
    }

    public void deleteAll() {
        projectRepository.deleteAll();
    }

    public List<Project> queryAllNotOld(String[] property) {
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException(LOGIN_EXPIRED);
        }
        List<String> budgetEOIdList = budgetEODao.findAllBudgetIdByNameNotLikeAndPropertyArr("旧-%", property);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.mustNot(QueryBuilders.termQuery(DEL_FLAG, true));
        boolQueryBuilder.must(QueryBuilders.termsQuery(BUDGET_ID, budgetEOIdList));
        /*
         * 过滤未申领的项目
         */
        boolQueryBuilder.must(QueryBuilders.existsQuery(PROJECT_LEADER_ID));
        // boolQueryBuilder.must(QueryBuilders.termQuery("projectMemberIds",userId));
        ArrayList<Project> projects = Lists.newArrayList(projectRepository.search(boolQueryBuilder));
        // 将无用字段置为null，降低网络开销
        for (Project project : projects){
            project.setDeptIdUserIdList(null);
            project.setDeptInfoListMap(null);
            project.setUserIdDeptNameMapList(null);
        }
        //按中文首字母排序
        Collections.sort(projects);
        return projects;
    }

    public List<ProjectMinVO> queryAllNotOldNew(String[] property) {
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException(LOGIN_EXPIRED);
        }
        List<String> budgetEOIdList = budgetEODao.findAllBudgetIdByNameNotLikeAndPropertyArr("旧-%", property);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.mustNot(QueryBuilders.termQuery(DEL_FLAG, true));
        boolQueryBuilder.must(QueryBuilders.termsQuery(BUDGET_ID, budgetEOIdList));
        /*
         * 过滤未申领的项目
         */
        boolQueryBuilder.must(QueryBuilders.existsQuery(PROJECT_LEADER_ID));
        // boolQueryBuilder.must(QueryBuilders.termQuery("projectMemberIds",userId));
        ArrayList<Project> projects = Lists.newArrayList(projectRepository.search(boolQueryBuilder));
        List<ProjectMinVO> projectMinVOList = new ArrayList<>();
        for (Project project : projects){
            ProjectMinVO projectMinVO = new ProjectMinVO();
            BeanUtils.copyPropertiesIgnoreNullValue(project,projectMinVO);
            projectMinVOList.add(projectMinVO);
        }
        //按中文首字母排序
        Collections.sort(projectMinVOList);
        return projectMinVOList;
    }

    public List<Project> queryAll() {
        List<String> deptIds = getDeptIds();
        String userId = UserUtils.getUserId();
        Subject subject = SecurityUtils.getSubject();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("请登录！");
        }
        if (deptIds == null) {
            return Collections.emptyList();
        }
        boolQueryBuilder.mustNot(QueryBuilders.termQuery(DEL_FLAG, true));
        if (subject.hasRole(Role.BU_ZHANG)) {
            for (String deptId : deptIds) {
                boolQueryBuilder.should(QueryBuilders.termQuery("deptId", deptId));
            }
            ArrayList<Project> projects = Lists.newArrayList(projectRepository.search(boolQueryBuilder));
            setDataList(projects);
            return projects;
        }

        if (subject.hasRole(Role.SUPER_ADMIN) || subject.hasRole(Role.ADMIN) || subject.hasRole(Role.ZHU_REN)) {
            ArrayList<Project> projects = Lists.newArrayList(projectRepository.search(boolQueryBuilder));
            // 下面什么毛线写法
//            for (Project project : projects) {
////                project.setFinishedStatus(ProjectStatusEnums.EXECUTE.getStatus());
//                if (null != project.getBudgetId()) {
//                    BudgetEO budgetEO = budgetEOService.getDao().selectByPrimaryKey(project.getBudgetId());
//                    if (null != budgetEO) {
//                        project.setPm(budgetEO.getPm());
//                    }
//                    projectRepository.save(project);
//                }
//            }
            setDataList(projects);
            return projects;
        }

        //只显示自己参与和创建的项目
        BoolQueryBuilder bq;
        bq = QueryBuilders.boolQuery().should(QueryBuilders.termQuery("projectLeaderId", userId))
                .should(QueryBuilders.termQuery("projectMemberIds", userId));
        boolQueryBuilder.must(bq);
        List<Project> projectList = Lists.newArrayList(projectRepository.search(boolQueryBuilder));
        //TODO 优化这个方法的使用和命名
        setDataList(projectList);
        return projectList;
    }

    /**
     * 分页查询
     */
    public void refreshProjectInBusiness() {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        int i = 1 ;
        while (true) {
            Page<Project> queryPage = projectRepository.search(queryBuilder, new PageRequest(i, 10));
            List<Project> projectList = queryPage.getContent();
            if (projectList.size()<1){
                break;
            }
            for (Project project : projectList){
                project.setBusiness(getBusinessName(project.getBusinessId()));
            }
            projectRepository.save(projectList);
            i ++ ;
        }
    }



    /**
     * 分页查询
     */
    public PageDTO queryByPage(int page, int size, String budgetId) {
        List<String> deptList = getDeptIds();
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException(LOGIN_EXPIRED);
        }
        Subject subject = SecurityUtils.getSubject();
        List<Project> queryList = null;
        if (deptList == null) {
            return new PageDTO();
        }
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.mustNot(QueryBuilders.termQuery(DEL_FLAG, true));
        if (StringUtils.isNotEmpty(budgetId)) {
            queryBuilder.must(QueryBuilders.termQuery(BUDGET_ID, budgetId));
        }

        if (subject.hasRole("部长")) {
            for (String deptId : deptList) {
                queryBuilder.should(QueryBuilders.termQuery("deptId", deptId));
            }
            return getPageDTO(queryBuilder, page, size);
        }
        if (subject.hasRole("超级管理员") || subject.hasRole("管理员") || subject.hasRole("主任")) {
            Sort sort = new Sort(Sort.Direction.DESC, CREATE_TIME);
            Page<Project> queryPage = projectRepository.search(queryBuilder, new PageRequest(page - 1, size, sort));
            queryList = (queryPage == null || (queryList = queryPage.getContent()) == null) ? new ArrayList<Project>() :
                    queryList;
            long count = queryPage == null ? 0 : queryPage.getTotalElements();
            return new PageDTO(count, queryList, page, size);
        }

        //只显示自己参与和创建的项目
        BoolQueryBuilder bq;
        bq = QueryBuilders.boolQuery().should(QueryBuilders.termQuery("projectLeaderId", userId))
                .should(QueryBuilders.termQuery("projectMemberIds", userId));
        queryBuilder.must(bq);
        return getPageDTO(queryBuilder, page, size);
    }

    // 迁移到TaskService done by liuzixi 20190301
    public PageDTO queryTaskByProjectId(String projectId, Integer page, Integer size) {
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException(LOGIN_EXPIRED);
        }
        Project project = projectRepository.findById(projectId);

        BoolQueryBuilder builder = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("projectId", projectId))
                .mustNot(QueryBuilders.termQuery(DEL_FLAG, Boolean.TRUE));
        builder.mustNot(QueryBuilders.termsQuery("taskStatus", "已完成,审批中".split(",")));

        // 超级管理员权限
        if (!superAdminService.isSuperAdmin()
                && !userId.equals(project.getProjectLeaderId())
                && !userId.equals(project.getProjectAdminId())
                && !userId.equals(project.getBusinessAdminId())) {
            BoolQueryBuilder qb = QueryBuilders.boolQuery().should(QueryBuilders.termQuery("projectLeaderId", userId))
                    .should(QueryBuilders.termQuery("memberIds", userId))
                    .should(QueryBuilders.termQuery("pm", userId));
            builder.must(qb);
        }
        List<Task> taskList = Lists.newArrayList(taskRepository.search(builder));
        //过滤隐藏的任务
        List<UserProjectCustom> userProjectCustomList =
                userProjectCustomDao.findHideByStatusAndTypeAndUserId("0", "3", userId);
        if (StringUtils.isNotEmpty(userProjectCustomList) && StringUtils.isNotEmpty(taskList)) {
            List<Task> removeTaskList = new ArrayList<>();
            for (UserProjectCustom userProjectCustom : userProjectCustomList) {
                String hideTaskId = userProjectCustom.getTaskid();
                for (Task task : taskList) {
                    if (hideTaskId.equals(task.getId())) {
                        removeTaskList.add(task);
                        break;
                    }
                }
            }
            if (removeTaskList.size() > 0) {
                taskList.removeAll(removeTaskList);
            }
        }

        // 总的记录数
        int totalRecord = taskList.size();
        // 获取当前页的数据
        //分页的记录开始数和结束数
        int start = (page - 1) * size;
        int end = start + size;
        if (end > totalRecord) {
            end = totalRecord;
        }
        if (start > totalRecord) {
            start = totalRecord - size;
            end = totalRecord;
        }
        List<Task> tasks = taskList.subList(start, end);
        return new PageDTO(totalRecord, tasks, page, size);
    }

    /**
     * 根据所属业务查询当前用户的项目
     *
     * @return
     */
    public List<Project> queryByBudgetId(String budgetId) {
        List<String> deptIds = getDeptIds();
        if (deptIds == null) {
            return Collections.emptyList();
        }

        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery(BUDGET_ID, budgetId))
                .mustNot(QueryBuilders.termQuery(DEL_FLAG, true));
        List<Project> projectList = Lists.newArrayList(projectRepository.search(queryBuilder));
        setDataList(projectList);
        return projectList;
    }

    public List<Project> queryByBudgetId(String bussinessId, String projectName,
                                         String projectStatus, String projectManager) {

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        BoolQueryBuilder innerBoolQueryBuilder = QueryBuilders.boolQuery();

        boolQueryBuilder.must(QueryBuilders.termQuery(BUDGET_ID, bussinessId));
        boolQueryBuilder.mustNot(QueryBuilders.termQuery(DEL_FLAG, true));

        if (null != projectManager && projectManager.length() > 0) {
            innerBoolQueryBuilder.should(QueryBuilders.wildcardQuery(PROJECT_LEADER, "*" + projectManager + "*"));
        }
        if (null != projectName && projectName.length() > 0) {
            innerBoolQueryBuilder.should(QueryBuilders.wildcardQuery(NAME, "*" + projectName + "*"));
        }
        if (null != projectStatus && projectStatus.length() > 0) {
            innerBoolQueryBuilder.should(QueryBuilders.wildcardQuery(FINISHED_STATUS, "*" + projectStatus + "*"));
        }
        boolQueryBuilder.must(innerBoolQueryBuilder);

        List<Project> projectList = Lists.newArrayList(projectRepository.search(boolQueryBuilder));

        setDataList(projectList);
        return projectList;
    }

    /**
     * 先进行es查询，如果es查询不到进行数据字典查询
     * 因为 旧的信息存在ES里，科研模块存在数据字典中
     *
     * @param id
     * @return
     */
    public String getBusinessName(String id) {
        if (StringUtils.isNotEmpty(id)) {
            Business business = businessRepository.findOne(id);
            if (business != null) {
                return business.getName();
            } else {
                DicTypeEO dic = dicTypeEODao.getDicTypeEOByCode(id);
                if (null != dic) {
                    return dic.getDicTypeName();
                }
            }
        }
        return null;
    }

    public void setDataList(List<Project> dataList) {
        List<String> budgetIds = new ArrayList<>();
        for (Project project : dataList) {
            if (StringUtils.isNotEmpty(project.getBudgetId())) {
                budgetIds.add(project.getBudgetId());
            }
        }

        List<List<String>> lists = CommonUtil.splitList(budgetIds, 512);
        List<BudgetEO> budgetEOList = new ArrayList<>();
        for (List<String> ids : lists) {
            budgetEOList.addAll(budgetEOService.selectByPrimaryKeys(ids));
        }

        for (Project project : dataList) {
            boolean needUpdate = false;
            for (BudgetEO budgetEO : budgetEOList) {
                if (budgetEO.getId().equals(project.getBudgetId())) {
                    project.setBudget(budgetEO.getProjectName());
                    if (StringUtils.isEmpty(project.getPm())) {
                        needUpdate = true;
                        project.setPm(budgetEO.getPm());
                    }
                    if (StringUtils.isEmpty(project.getBusiness()) && StringUtils.isNotEmpty(project.getBusinessId())) {
                        needUpdate = true;
                        project.setBusiness(getBusinessName(project.getBusinessId()));
                    }
                }
            }
            if (needUpdate){
                projectRepository.save(project);
            }
        }
    }

    //TODO 修改getCurrentUserDeptIds
    public List<String> getDeptIds() {
        List<String> depIds = new ArrayList<>();
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException(LOGIN_EXPIRED);
        }
        if (StringUtils.isNotEmpty(userId)) {
            UserEO userEO = userEOService.getUserWithRoles(userId);
            Subject subject = SecurityUtils.getSubject();
            if (subject.hasRole(Role.SUPER_ADMIN) || subject.hasRole(Role.ADMIN) || subject.hasRole(Role.ZHU_REN)) {
                return depIds;
            }
            if (CollectionUtils.isEmpty(userEO.getOrgEOList())) {
                return Collections.emptyList();
            }
            List<OrgEO> orgList = userEO.getOrgEOList();

            if (CollectionUtils.isNotEmpty(orgList)) {
                for (OrgEO tepOrgEO : orgList) {
                    depIds.add(tepOrgEO.getId());
                }

                return depIds;
            }
        }
        return Collections.emptyList();
    }

    /**
     * 判断当前用户和项目负责人以及项目成员是否是同一部门
     *
     * @param personIds
     * @param userId
     * @return
     */
    public Boolean checkDept(String[] personIds, String userId) {
        String cusUserDeptId = getDeptId(userId);
        if (CollectionUtils.isEmpty(personIds)) {
            throw new IllegalArgumentException("人员不能为空！");
        }
        for (String perId : personIds) {
            String perDeptId = getDeptId(perId);
            if (null == cusUserDeptId || !cusUserDeptId.equals(perDeptId)) {
                throw new AdcDaBaseException("不能选择不同部门的人员！");
            }
        }
        return true;
    }

    public String getDeptId(String userId) {
        if (StringUtils.isNotEmpty(userId)) {
            UserEO userEO = userEOService.getUserWithRoles(userId);
            if (userEO.getOrgEOList() == null && userEO.getOrgEOList().size() <= 0) {
                return null;
            }
            List<OrgEO> orgList = userEO.getOrgEOList();
            OrgEO orgEO = orgList.get(orgList.size() - 1);
            return orgEO.getId();
        }
        return null;
    }

    public List<OrgEO> queryOrgByADC() {
        List<OrgEO> orgEOList = orgEOService.listOrgEOByOrgName("");
        List<OrgEO> adcOrgList = new ArrayList<>();
        for (OrgEO orgEO : orgEOList) {
            if ("MH8JQV5TSN".equals(orgEO.getId())) {
                adcOrgList = handelOrg(orgEOList, "MH8JQV5TSN");
                orgEO.setParentId("0");
                adcOrgList.add(orgEO);
            }
        }
        return adcOrgList;
    }

    public List<OrgEO> queryOrgByADCown1() {
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException(LOGIN_EXPIRED);
        }

        UserEO userEO = userEODao.getUserWithRoles(userId);
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException(LOGIN_EXPIRED);
        }
        List<OrgEO> orgEOList = userEO.getOrgEOList();

        return queryChildOrg(orgEOList);
    }

    public List<OrgEO> newQueryOrgByADCown1() {
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException(LOGIN_EXPIRED);
        }
        return this.queryChildOrg(userId);
    }

    public List<OrgEO> queryOrgByADCown() {
        String userId = UserUtils.getUserId();
        UserEO userWithRoles = userEOService.getUserWithRoles(userId);
        List<OrgEO> adcOwnOrgList = userWithRoles.getOrgEOList();
        List<OrgEO> adcOrgList = new ArrayList<>();
        for (OrgEO org : adcOwnOrgList) {
            String s = org.getId();
            //主任查看中心所有员工
            if (SecurityUtils.getSubject().hasRole(Role.ZHU_REN)) {
                s = "MH8JQV5TSN";
            }
            List<OrgEO> orgEOList = orgEOService.listOrgEOByOrgName("");

            List<String> upIds = null;
            for (OrgEO orgEO : orgEOList) {
                if (s.equals(orgEO.getId())) {
                    String parentIds = orgEO.getParentIds();
                    if (!StringUtils.isEmpty(parentIds)) {
                        String[] split = parentIds.split("0,CLEYQP8C27,");
                        if (1 == split.length) {
                            upIds = Arrays.asList(split[0].split(","));
                        } else {
                            upIds = Arrays.asList(split[1].split(","));
                        }
                    }
                    if (upIds == null) {
                        upIds = new ArrayList<>();
                        upIds.add(orgEO.getParentId());
                    }

                    adcOrgList = handelOrg(orgEOList, s);
                    if (!adcOrgList.contains(orgEO)) {
                        adcOrgList.add(orgEO);
                    }
                }
            }
            //寻找上级
            for (OrgEO orgEO : orgEOList) {
                if (upIds != null && upIds.contains(orgEO.getId())) {
                    orgEO.setRemarks("-1");
                    if (!adcOrgList.contains(orgEO)) {
                        adcOrgList.add(orgEO);
                    }
                }
            }
        }
        return adcOrgList;
    }

    private List<OrgEO> handelOrg(List<OrgEO> orgEOList, String orgId) {
        List<OrgEO> adcOrgList = new ArrayList<>();
        for (OrgEO orgEO : orgEOList) {
            if (orgEO.getParentId() != null && orgEO.getParentId().equals(orgId)) {
                List<OrgEO> orgEOS = handelOrg(orgEOList, orgEO.getId());
                if (!adcOrgList.contains(orgEO) && !adcOrgList.contains(orgEOS)) {
                    adcOrgList.add(orgEO);
                    adcOrgList.addAll(orgEOS);
                }
            }
        }

        return adcOrgList;

    }

    private PageDTO getPageDTO(QueryBuilder queryBuilder, int page, int size) {
        List<Project> queryList;
        Sort sort = new Sort(Sort.Direction.DESC, CREATE_TIME);
        Page<Project> queryPage = projectRepository.search(queryBuilder, new PageRequest(page - 1, size, sort));

        if (queryPage == null || (queryPage.getContent()) == null) {
            queryList = new ArrayList<>();
        } else {
            queryList = queryPage.getContent();
        }

        setDataList(queryList);
        long count = (queryPage == null) ? 0 : queryPage.getTotalElements();
        return new PageDTO(count, queryList, page, size);
    }

    public PageDTO getMyProject(int page, int pageSize, String userId) {
        List<String> budgetIdList = budgetEODao.findAllBudgetIdByNameNotLike("旧-%");
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .mustNot(QueryBuilders.termQuery(DEL_FLAG, true))
                .must(QueryBuilders.termsQuery(BUDGET_ID,budgetIdList))
                .must( QueryBuilders.boolQuery().should(QueryBuilders.termQuery("projectLeaderId", userId))
                                                 .should(QueryBuilders.termQuery("projectMemberIds", userId))
                );
        return getPageDTO(queryBuilder, page, pageSize);
    }

    public List<UserEO> getMemberInfo(String projectId) throws Exception {
        List<UserEO> userEOList = new ArrayList<>();
        Project project = projectRepository.findOne(projectId);
        if (project == null || Boolean.TRUE.equals(project.getDelFlag())) {
            throw new IllegalArgumentException();
        }
        //加入负责人
        UserEO projectLeaderUserEO = userEOService.selectByPrimaryKey(project.getProjectLeaderId());
        userEOList.add(projectLeaderUserEO);

        //加入成员
        for (String userId : project.getProjectMemberIds()) {
            userEOList.add(userEOService.selectByPrimaryKey(userId));
        }

        return userEOList;
    }

    public BigDecimal getWorkTime(String projectId) {
        double workTime = 0.0;
        String[] taskIdArray;
        int size;
        //1.根据项目查询所有的任务
        List<Task> tasks = taskRepository.findByProjectIdAndDelFlagNot(projectId, true);
        for (Task task : tasks) {
            //2.根据任务查询所有的子任务
            List<ChildrenTask> childrenTasks = childTaskRepository.findByTaskIdEqualsAndDelFlagNot(task.getId(), true);
            // add by liqiushi 20190313 没有子任务时工时平均分到任务上
            if (CollectionUtils.isEmpty(childrenTasks)) {
                //没有子任务，根据任务查询所有包含的日报
                List<Daily> dailies = dailyRepository.findByTaskIdArrayInAndDelFlagNot(task.getId(), true);
                for (Daily oneDay : dailies) {
                    taskIdArray = oneDay.getTaskIdArray();
                    size = taskIdArray.length;
                    //每条日报4小时，均分
                    workTime += 4 / (double) size;
                }
            } else {
                //有子任务时，将日报时间均分到任务和子任务上
                //3.根据子任务去查询所有包含的日报
                for (ChildrenTask childrenTask : childrenTasks) {
                    List<Daily> dailies = dailyRepository.findByTaskIdArrayInAndDelFlagNot(childrenTask.getId(), true);
                    //4.根据每条日报的工时数（4h）/日报选中的子任务数
                    if (CollectionUtils.isNotEmpty(dailies)) {
                        for (Daily daily : dailies) {
                            taskIdArray = daily.getTaskIdArray();
                            size = taskIdArray.length;
                            workTime += 4 / (double) size;
                        }
                    }
                }
            }
        }
        //5.累加得到项目的工时数（保留两位小数）
        return BigDecimal.valueOf(workTime).setScale(2, RoundingMode.UP);
    }

    private Set<String> getUserIds(Project project) {
        Set<String> userIds = new HashSet<>();
        userIds.add(project.getProjectLeaderId());
        userIds.addAll(Lists.newArrayList(project.getProjectMemberIds()));
        BudgetEO budgetEO = budgetEODao.selectByPrimaryKey(project.getBudgetId());
        if (budgetEO != null) {
            userIds.add(budgetEO.getCreateUserId());
            userIds.add(budgetEO.getPm());
        }
        return userIds;
    }

    public void updateProjectMembers(Integer projectType) {
        List<Project> rows = projectRepository.findByProjectType(projectType);
        for (Project project : rows) {
            addMemberNames4Project(project);
        }
        projectRepository.save(rows);
    }

    private void addMemberNames4Project(Project project) {
        if (CollectionUtils.isEmpty(project.getMemberNames())) {
            return;
        }
        String projectMemberNamesStr = StringUtils.join(project.getMemberNames(), ",");
        project.setProjectMemberNames(projectMemberNamesStr);
    }

    // 根据所属业务及项目负责人查找Project
    public List<Project> findByBudgetIdAndProjectLeaderIdAndDelFlagNot(String budgetId, String projectLeaderId) {
        return projectRepository.findByBudgetIdAndProjectLeaderIdAndDelFlagNot(budgetId, projectLeaderId, true);
    }


    public int  projectOwnerStatistics(int projectYear) {
        // 传0 下层已经做了处理 会同等作传null
        int allPartnerNum = projectOwnerStatisticsNew(0);
        if (StringUtils.isNotEmpty(projectYear)) {
            int previousPartnerNum = projectOwnerStatisticsNew(projectYear);
            return allPartnerNum - previousPartnerNum;
        }
        return allPartnerNum;
    }

    /**
     * @author Wei Jinjin
     * @date 2020-07-08
     */
    public int projectOwnerStatisticsNew(Integer projectYear) {
        List<String> skipList = Arrays.asList("1111111111","20190304","20181105","20160504",
                "20160809","20180305","20180801","20181105","20190203","20190304",
                "201800195","220200001","320200001","320200002","320200003","320200005","1111212",
                "20160504","9999999","180887","20160809","20160703","180663","1111212",
                "220200001","AS20200003","AS20200001","asd22312","6106NF0043N19","RC20160213",
                "RC20160411","CGHT201908004","CGHT201908005","2019000000000000","TJZCHT20190169",
                "TJHT20190828","TJKDK20190925004","CS20200165","HT10000001","0320200001","CS20200396");
        Set<String> allSet = new HashSet<>();

        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                .must(QueryBuilders.matchQuery("projectType", 0))
                .mustNot(QueryBuilders.matchQuery("delFlag", true))
                .must(QueryBuilders.existsQuery("budgetDomainId"))
                .mustNot(QueryBuilders.matchQuery("budgetDomainId", ""))
                .must(QueryBuilders.existsQuery("projectOwner"))
                .mustNot(QueryBuilders.matchQuery("projectOwner", ""))
                .must(QueryBuilders.existsQuery("contractNo"))
                .mustNot(QueryBuilders.matchQuery("contractNo", ""))
                .mustNot(QueryBuilders.matchQuery("projectOwner", "南方区"));
        for (String str : skipList) {
            boolQuery.mustNot(QueryBuilders.matchQuery("contractNo", str));
        }
        if (null != projectYear && projectYear != 0 ) {
            boolQuery.must(QueryBuilders.rangeQuery("projectYear").lt(projectYear));
        }
        boolQuery.filter(QueryBuilders.regexpQuery("contractNo", ".{7,}"));
        queryBuilder.withQuery(boolQuery);

        Page<Project> page = projectRepository.search(queryBuilder.build().getQuery(), new PageRequest(0, 10000));
        Iterator it = page.iterator();
        while(it.hasNext()) {
            Project project = (Project) it.next();
            allSet.add(StringUtils.trim(project.getProjectOwner()));
        }
        List<String> partnerNameList = contractPartnerEODao.queryAllPartnerName();
        allSet.addAll(partnerNameList);
        return allSet.size();
    }


    /**
     * @author Wei Jinjin
     * @date 2020-07-08
     */
    public void projectOwnerStatisticsNew(DashBoardVO dashBoardVO) {
        List<String> skipList = Arrays.asList("1111111111","20190304","20181105","20160504",
                "20160809","20180305","20180801","20181105","20190203","20190304",
                "201800195","220200001","320200001","320200002","320200003","320200005","1111212",
                "20160504","9999999","180887","20160809","20160703","180663","1111212",
                "220200001","AS20200003","AS20200001","asd22312","6106NF0043N19","RC20160213",
                "RC20160411","CGHT201908004","CGHT201908005","2019000000000000","TJZCHT20190169",
                "TJHT20190828","TJKDK20190925004","CS20200165","HT10000001","0320200001","CS20200396");
        Set<String> allSet = new HashSet<>();
        Set<String> oldSet = new HashSet<>();

        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                .must(QueryBuilders.matchQuery("projectType", 0))
                .mustNot(QueryBuilders.matchQuery("delFlag", true))
                .must(QueryBuilders.existsQuery("budgetDomainId"))
                .mustNot(QueryBuilders.matchQuery("budgetDomainId", ""))
                .must(QueryBuilders.existsQuery("projectOwner"))
                .mustNot(QueryBuilders.matchQuery("projectOwner", ""))
                .must(QueryBuilders.existsQuery("contractNo"))
                .mustNot(QueryBuilders.matchQuery("contractNo", ""))
                .mustNot(QueryBuilders.matchQuery("projectOwner", "南方区"));
        for (String str : skipList) {
            boolQuery.mustNot(QueryBuilders.matchQuery("contractNo", str));
        }
//        if (StringUtils.isNotEmpty(projectYear)) {
//            boolQuery.must(QueryBuilders.rangeQuery("projectYear").lt(projectYear));
//        }
        boolQuery.filter(QueryBuilders.regexpQuery("contractNo", ".{7,}"));
        queryBuilder.withQuery(boolQuery);

        //        int allPartnerNum = projectOwnerStatisticsNew(null);
//        int previousPartnerNum = projectOwnerStatisticsNew(projectYear);
//        dashBoardVO.setCurrentYearIncreaseProjectOwnerNum(allPartnerNum - previousPartnerNum);
        Page<Project> page = projectRepository.search(queryBuilder.build().getQuery(), new PageRequest(0, 10000));
        Iterator it = page.iterator();
        while(it.hasNext()) {
            Project project = (Project) it.next();
            allSet.add(StringUtils.trim(project.getProjectOwner()));
            if (project.getProjectYear() < dashBoardVO.getYear()){
                oldSet.add(StringUtils.trim(project.getProjectOwner()));
            }
        }
        List<String> partnerNameList = contractPartnerEODao.queryAllPartnerName();
        allSet.addAll(partnerNameList);
        oldSet.addAll(partnerNameList);
//        int allPartnerNum = allSet.size();
//        int previousPartnerNum = allSet.size() - oldSet.size();
        dashBoardVO.setProjectOwnerNum(allSet.size());
        dashBoardVO.setCurrentYearIncreaseProjectOwnerNum(allSet.size() - oldSet.size());
    }

    public DashBoardVO newProjectOwnerStatistics(Integer projectYear) {
        DashBoardVO dashBoardVO = new DashBoardVO();
        int allPartnerNum = projectOwnerStatisticsNew(0);
        int previousPartnerNum = projectOwnerStatisticsNew(projectYear);
        dashBoardVO.setCurrentYearIncreaseProjectOwnerNum(allPartnerNum - previousPartnerNum);
        dashBoardVO.setProjectOwnerNum(allPartnerNum);
        return dashBoardVO;
    }
    public DashBoardVO newProjectOwnerStatistics(DashBoardVO dashBoardVO) {
//        int allPartnerNum = projectOwnerStatisticsNew(null);
//        int previousPartnerNum = projectOwnerStatisticsNew(projectYear);
        projectOwnerStatisticsNew(dashBoardVO);
//        dashBoardVO.setCurrentYearIncreaseProjectOwnerNum(allPartnerNum - previousPartnerNum);
//        dashBoardVO.setProjectOwnerNum(allPartnerNum);
        return dashBoardVO;
    }




    /**
     * 合作企业总数统计
     * @return
     */
//    public int projectOwnerStatistics(Integer projectYear) {
//       /* BoolQueryBuilder bq;
//        bq = QueryBuilders.boolQuery().should(QueryBuilders.termQuery("projectLeaderId", userId))
//                .should(QueryBuilders.termQuery("projectMemberIds", userId));
//        boolQueryBuilder.must(bq);
//        List<Project> projectList = Lists.newArrayList(projectRepository.search(boolQueryBuilder));*/
//
//
//        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//        boolQueryBuilder.mustNot(QueryBuilders.termQuery(DEL_FLAG, true));
//        boolQueryBuilder.must(QueryBuilders.termQuery(PROJECT_TYPE,0));
//        if (StringUtils.isNotEmpty(projectYear)){
//            boolQueryBuilder.must(QueryBuilders.termQuery("projectYear",projectYear));
//        }
//
//        String[] property = new String[1];
//        property[0] = "0";
//        List<BudgetEO> budgetEOList = budgetEODao.findAllBudgetNameNotLike("旧-%", property);
//        //按中文首字母排序
//        Collections.sort(budgetEOList);
//
//        List<String> budgetIds = new ArrayList<>();
//        for (BudgetEO budgetEO : budgetEOList) {
//            budgetIds.add(budgetEO.getId());
//        }
//        boolQueryBuilder.must(QueryBuilders.termsQuery(BUDGET_ID, budgetIds));
//
//
//        boolQueryBuilder.mustNot(
//                QueryBuilders.boolQuery()
//                        .should(QueryBuilders.termQuery(CONTRACT_NO,""))
//                        .should(QueryBuilders.boolQuery().mustNot(QueryBuilders.existsQuery(CONTRACT_NO))));
//
//
//
//        //boolQueryBuilder.must(boolQueryBuilder.mustNot(QueryBuilders.termQuery(CONTRACT_NO,"")).should(boolQueryBuilder.must(QueryBuilders.existsQuery(CONTRACT_NO))));
//
//        //boolQueryBuilder.mustNot(QueryBuilders.termQuery(CONTRACT_NO,""));
//
//
//        ArrayList<Project> projects = Lists.newArrayList(projectRepository.search(boolQueryBuilder));
//
//
//        //List<Project> projects = this.queryAllNotOld(property);
//
//
//
//
//        //新增企业统计
//        if (StringUtils.isNotEmpty(projectYear)){
//            //过滤不是新增的数据
//            return this.newAddProjectOwnerNum(projects,projectYear);
//        }
//
//        Set<String> projectOwner = new HashSet<>();
//        if (projects.size()>0){
//            for (Project project : projects){
//                projectOwner.add(project.getProjectOwner());
//            }
//            projectOwner.remove(null);
//            projectOwner.remove("");
//        }
//        System.out.println(projectOwner);
//        int total = projectOwner.size();
//        return total;
//    }

    private Integer newAddProjectOwnerNum(List<Project> projects,Integer projectYear){
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        Set<String> projectOwners = new HashSet<>();
        for (Project project : projects){
            projectOwners.add(project.getProjectOwner());
        }
        boolQueryBuilder.must(QueryBuilders.termsQuery("projectOwners", projectOwners));
        boolQueryBuilder.mustNot(QueryBuilders.termQuery("projectYear",projectYear));
        ArrayList<Project> projectsOtherYear = Lists.newArrayList(projectRepository.search(boolQueryBuilder));
        Set<String> projectOwnersOtherYear = new HashSet<>();
        if (projectOwnersOtherYear.size()>0){
            for (Project project : projectsOtherYear){
                projectOwnersOtherYear.add(project.getProjectOwner());
            }
        }
        projectOwners.removeAll(projectOwnersOtherYear);
        projectOwners.remove(null);
        projectOwners.remove("");
        return projectOwners.size();
    }

    /**
     * 获取经营类项目的合同金额
     * @param projectOwner
     * @return
     */
    public Float getTotalContractAmount (String projectOwner){
        List<Project> projects = getAllProjectLists(projectOwner);
        Float totalContractAmount = 0f;
        for (Project project : projects){
            totalContractAmount += project.getContractAmount();
        }
        return DoubleUtils.getDivideTenThousandScale2(BigDecimal.valueOf(totalContractAmount)).floatValue();
    }
    public Map<String, Float> getTotalContractAmountMap (){
        List<Project> projects = getAllProjectLists(null);
        Map<String, Float> projectOwnerContractAmountMap = new HashMap<>();
        for (Project project : projects){
            if (StringUtils.isEmpty(project.getProjectOwner())){
                continue;
            }

            Float totalContractAmount = projectOwnerContractAmountMap.get(project.getProjectOwner());
            if (null == totalContractAmount){
                totalContractAmount = 0f ;
            }
            totalContractAmount += project.getContractAmount();
            projectOwnerContractAmountMap.put(project.getProjectOwner(),totalContractAmount);
        }
        return projectOwnerContractAmountMap;
    }

    public Float getTotalContractAmount (){
        Map<String,Float> projectOwnerContractAmountMap = getProjectOwnerContractAmountMap();
        BigDecimal totalContractAmount = new BigDecimal(0);
        for (Map.Entry<String,Float> entry : projectOwnerContractAmountMap.entrySet()){
            totalContractAmount = totalContractAmount.add(BigDecimal.valueOf(entry.getValue()==null?0:entry.getValue()));
        }
        return totalContractAmount.floatValue();
    }


    //根据条件查询所有经营类项目
    public List<Project> getAllProjectLists (String projectOwner){
        String[] property = new String[1];
        property[0] = "0";
        List<BudgetEO> budgetEOList = budgetEODao.findAllBudgetNameNotLike("旧-%", property);
        //按中文首字母排序
        Collections.sort(budgetEOList);
        List<String> budgetIds = new ArrayList<>();
        for (BudgetEO budgetEO : budgetEOList) {
            budgetIds.add(budgetEO.getId());
        }

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.termsQuery(BUDGET_ID, budgetIds));
        boolQueryBuilder.must(QueryBuilders.existsQuery(PROJECT_LEADER_ID));
        boolQueryBuilder.mustNot(QueryBuilders.termQuery(DEL_FLAG, true));
        Integer projectYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        boolQueryBuilder.must(QueryBuilders.termQuery("projectYear",projectYear));
        boolQueryBuilder.must(QueryBuilders.termQuery(PROJECT_TYPE,0));
        if(StringUtils.isNotEmpty(projectOwner)){
            boolQueryBuilder.must(QueryBuilders.termQuery("projectOwner",projectOwner));
        }
        ArrayList<Project> projects = Lists.newArrayList(projectRepository.search(boolQueryBuilder));
        return  projects;
    }

    //根据业务方分组查询其合同金额并且倒序
    public  List<Project> getProjectContractAmount(){

        String[] property = new String[1];
        property[0] = "0";
        List<BudgetEO> budgetEOList = budgetEODao.findAllBudgetNameNotLike("旧-%", property);
        //按中文首字母排序
        Collections.sort(budgetEOList);
        List<String> budgetIds = new ArrayList<>();
        for (BudgetEO budgetEO : budgetEOList) {
            budgetIds.add(budgetEO.getId());
        }

        Integer projectYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));

        //创建查询工具类，然后按照条件查询，可以分开也可以合在一起写
        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .mustNot(QueryBuilders.termQuery(DEL_FLAG, true))
                .must(QueryBuilders.termQuery("projectYear",projectYear))
                .must(QueryBuilders.termQuery(PROJECT_TYPE,0))
                .must(QueryBuilders.termsQuery(BUDGET_ID, budgetIds));

        //构建查询
        NativeSearchQueryBuilder nativeSearchQueryBuilder =
                new NativeSearchQueryBuilder().withQuery(queryBuilder);
        // 不查询任何结果
        nativeSearchQueryBuilder.withSourceFilter(new FetchSourceFilter(new String[] { "" }, null));

        /**
         *创建聚合函数：本例子是以 projectOwner公司名称分组然后计算其contractAmount合同金额总和并且按照合同金额倒序排序
         * 相当于SQL语句 select projectOwner,sum(contractAmount) as contractAmount from peoject group by projectOwner order by contractAmount desc
         */
        TermsBuilder projectOwnerAgg =
                AggregationBuilders.terms("projectOwner").field("projectOwner")
                        .order(Terms.Order.aggregation("contractAmount", false));

        SumBuilder contractAmountAgg = AggregationBuilders.sum("contractAmount").field("contractAmount");

        projectOwnerAgg.subAggregation(contractAmountAgg);

        nativeSearchQueryBuilder.addAggregation(projectOwnerAgg);
        SearchQuery searchQuery = nativeSearchQueryBuilder.build();



        /**
         * ③  通过elasticSearch模板elasticsearchTemplate.query()方法查询,获得聚合(常用)
         */
//        Aggregations aggregations = elasticsearchTemplate.query(searchQuery, new ResultsExtractor<Aggregations>() {
//            @Override
//            public Aggregations extract(SearchResponse searchResponse) {
//                return searchResponse.getAggregations();
//            }
//        });
        AggregatedPage<Project> aggPage = (AggregatedPage<Project>) this.projectRepository.search(searchQuery);
        StringTerms agg = (StringTerms) aggPage.getAggregation("projectOwner");
        List<Terms.Bucket> buckets = agg.getBuckets();
        List<Project> projects = new ArrayList<>();
        for (Terms.Bucket bucket : buckets) {
            Project project = new Project();
            // 、获取桶中的key，即名称  获取桶中的文档数
            //System.out.println(bucket.getKeyAsString() + "，有" + bucket.getDocCount());

            // 获取子聚合结果：
            Sum sum = (Sum) bucket.getAggregations().asMap().get("contractAmount");
            //System.out.println("总数：" + sum.getValue());
            project.setProjectOwner(bucket.getKeyAsString());
            BigDecimal obj = BigDecimal.valueOf(sum.getValue())
                    .divide(BigDecimal.valueOf(10000)).setScale(2, RoundingMode.HALF_UP);

            float contractAmount = obj.floatValue();


            project.setContractAmount(contractAmount);
            project.setContractAmountStr(obj.toString());
            projects.add(project);

        }

        /**
         * 对查询结果处理
         */
        //转换成map集合
//        Map<String, Aggregation> aggregationMap = aggregations.asMap();
//        //获得对应的聚合函数的聚合子类，该聚合子类也是个map集合,里面的value就是桶Bucket，我们要获得Bucket
//        StringTerms stringTerms = (StringTerms)aggregationMap.get("projectOwner_key");
//        //获得所有的桶
//        List<Terms.Bucket> buckets = stringTerms.getBuckets();

       /* 一 、使用Iterator遍历桶
        //将集合转换成迭代器遍历桶,当然如果你不删除buckets中的元素，直接foreach遍历就可以了
       Iterator<Terms.Bucket> iterator = buckets.iterator();
        List<String> projectOwnerList = new ArrayList<>();
        while (iterator.hasNext()){
        //bucket桶也是一个map对象，我们取它的key值就可以了
            String projectOwner = iterator.next().getKeyAsString();
            //根据projectOwner去结果中查询即可对应的文档，添加存储数据的集合
            projectOwnerList.add(projectOwner);
        }*/

        //② 使用 foreach遍历就可以了
//       List<Project> projects = new ArrayList<>();
//       for (Terms.Bucket bucket : buckets){
//           Project project = new Project();
//           String projectOwner = bucket.getKeyAsString();
//           //得到所有子聚合
//           Map<String, Aggregation> contractAmountSumbuilder = bucket.getAggregations().asMap();
//           //sum值获取方法 如果是avg，那么这里就是Avg格式
//           Sum contractAmounts = (Sum)contractAmountSumbuilder.get("contractAmount_key");
//           double contractAmount = BigDecimal.valueOf(contractAmounts.getValue()).divide(BigDecimal.valueOf(10000)).setScale(2, RoundingMode.HALF_UP)
//                   .doubleValue();
//
//
//           project.setProjectOwner(projectOwner);
//           project.setContractAmount((float) contractAmount);
//           projects.add(project);
//
//       }

        return  projects;
    }

    //根据业务方分组查询其合同金额并且倒序
    public  List<Project> getPadProjectContractAmount(){

        String[] property = new String[1];
        property[0] = "0";
        List<BudgetEO> budgetEOList = budgetEODao.findAllBudgetNameNotLike("旧-%", property);
        //按中文首字母排序
        Collections.sort(budgetEOList);
        List<String> budgetIds = new ArrayList<>();
        for (BudgetEO budgetEO : budgetEOList) {
            budgetIds.add(budgetEO.getId());
        }
        Integer projectYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        //创建查询工具类，然后按照条件查询，可以分开也可以合在一起写
        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .mustNot(QueryBuilders.termQuery(DEL_FLAG, true))
                .must(QueryBuilders.termQuery("projectYear",projectYear))
                .must(QueryBuilders.termQuery(PROJECT_TYPE,0))
                .must(QueryBuilders.termsQuery(BUDGET_ID, budgetIds))
                .mustNot(QueryBuilders.termsQuery("projectOwner",Arrays.asList("中国汽车技术研究中心有限公司","工业和信息化部计算机与微电子发展研究中心（中国软件评测中心）")));
        //构建查询
        NativeSearchQueryBuilder nativeSearchQueryBuilder =
                new NativeSearchQueryBuilder().withQuery(queryBuilder);
        // 不查询任何结果
        nativeSearchQueryBuilder.withSourceFilter(new FetchSourceFilter(new String[] { "" }, null));

        /**
         *创建聚合函数：本例子是以 projectOwner公司名称分组然后计算其contractAmount合同金额总和并且按照合同金额倒序排序
         * 相当于SQL语句 select projectOwner,sum(contractAmount) as contractAmount from peoject group by projectOwner order by contractAmount desc
         */
        TermsBuilder projectOwnerAgg =
                AggregationBuilders.terms("projectOwner").field("projectOwner")
                        .order(Terms.Order.aggregation("contractAmount", false));
        SumBuilder contractAmountAgg = AggregationBuilders.sum("contractAmount").field("contractAmount");
        projectOwnerAgg.subAggregation(contractAmountAgg);
        nativeSearchQueryBuilder.addAggregation(projectOwnerAgg);
        SearchQuery searchQuery = nativeSearchQueryBuilder.build();
//        });
        AggregatedPage<Project> aggPage = (AggregatedPage<Project>) this.projectRepository.search(searchQuery);
        StringTerms agg = (StringTerms) aggPage.getAggregation("projectOwner");
        List<Terms.Bucket> buckets = agg.getBuckets();
        List<Project> projects = new ArrayList<>();
        for (Terms.Bucket bucket : buckets) {
            Project project = new Project();
            // 、获取桶中的key，即名称  获取桶中的文档数
            // 获取子聚合结果：
            Sum sum = (Sum) bucket.getAggregations().asMap().get("contractAmount");
            project.setProjectOwner(bucket.getKeyAsString());
            BigDecimal obj = BigDecimal.valueOf(sum.getValue())
                    .divide(BigDecimal.valueOf(10000)).setScale(0, RoundingMode.HALF_UP);
            float contractAmount = obj.floatValue();
            project.setContractAmount(contractAmount);
            project.setContractAmountStr(obj.toString());
            projects.add(project);

        }
        return  projects;
    }
    //根据业务方分组查询其合同金额并且倒序
    public  Map<String,Float> getProjectOwnerContractAmountMap() {

        String[] property = new String[1];
        property[0] = "0";
        List<BudgetEO> budgetEOList = budgetEODao.findAllBudgetNameNotLike("旧-%", property);
        //按中文首字母排序
        Collections.sort(budgetEOList);
        List<String> budgetIds = new ArrayList<>();
        for (BudgetEO budgetEO : budgetEOList) {
            budgetIds.add(budgetEO.getId());
        }

        Integer projectYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));

        //创建查询工具类，然后按照条件查询，可以分开也可以合在一起写
        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .mustNot(QueryBuilders.termQuery(DEL_FLAG, true))
                .must(QueryBuilders.termQuery("projectYear", projectYear))
                .must(QueryBuilders.termQuery(PROJECT_TYPE, 0))
                .must(QueryBuilders.termsQuery(BUDGET_ID, budgetIds));

        //构建查询
        NativeSearchQueryBuilder nativeSearchQueryBuilder =
                new NativeSearchQueryBuilder().withQuery(queryBuilder);
        // 不查询任何结果
        nativeSearchQueryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{""}, null));

        /**
         *创建聚合函数：本例子是以 projectOwner公司名称分组然后计算其contractAmount合同金额总和并且按照合同金额倒序排序
         * 相当于SQL语句 select projectOwner,sum(contractAmount) as contractAmount from peoject group by projectOwner order by contractAmount desc
         */
        TermsBuilder projectOwnerAgg =
                AggregationBuilders.terms("projectOwner").field("projectOwner")
                        .order(Terms.Order.aggregation("contractAmount", false));

        SumBuilder contractAmountAgg = AggregationBuilders.sum("contractAmount").field("contractAmount");
        projectOwnerAgg.subAggregation(contractAmountAgg).size(1000);
        nativeSearchQueryBuilder.addAggregation(projectOwnerAgg);
        SearchQuery searchQuery = nativeSearchQueryBuilder.build();


        /**
         * ③  通过elasticSearch模板elasticsearchTemplate.query()方法查询,获得聚合(常用)
         */
        AggregatedPage<Project> aggPage = (AggregatedPage<Project>) this.projectRepository.search(searchQuery);
        StringTerms agg = (StringTerms) aggPage.getAggregation("projectOwner");
        List<Terms.Bucket> buckets = agg.getBuckets();
        Map<String, Float> projectOwnerContractAmountMap = new HashMap<>();
        for (Terms.Bucket bucket : buckets) {
            // 、获取桶中的key，即名称  获取桶中的文档数
            // 获取子聚合结果：
            Sum sum = (Sum) bucket.getAggregations().asMap().get("contractAmount");
            BigDecimal obj = BigDecimal.valueOf(sum.getValue())
                    .divide(BigDecimal.valueOf(10000)).setScale(2, RoundingMode.HALF_UP);

            float contractAmount = obj.floatValue();
            projectOwnerContractAmountMap.put(bucket.getKeyAsString(), contractAmount);

        }
        return projectOwnerContractAmountMap;
    }


    /**
     * 各公司经营情况统计 （按照当前年份）
     * @return
     * @throws Exception
     */
    public List<Map<String,Object>> getPartBOperationStatistics() throws Exception{
        List<Map<String,Object>> result = new ArrayList<>();
        String[] property = new String[1];
        property[0] = "0";
        List<BudgetEO> budgetEOList = budgetEODao.findAllBudgetNameNotLike("旧-%", property);
        //按中文首字母排序
        Collections.sort(budgetEOList);
        List<String> budgetIds = new ArrayList<>();
        for (BudgetEO budgetEO : budgetEOList) {
            budgetIds.add(budgetEO.getId());
        }

        Integer projectYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));

        //创建查询工具类，然后按照条件查询，可以分开也可以合在一起写
        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .mustNot(QueryBuilders.termQuery(DEL_FLAG, true))
                .must(QueryBuilders.termQuery("projectYear",projectYear))
                .must(QueryBuilders.termQuery(PROJECT_TYPE,0))
                .must(QueryBuilders.termsQuery(BUDGET_ID, budgetIds));

        //构建查询
        NativeSearchQueryBuilder nativeSearchQueryBuilder =
                new NativeSearchQueryBuilder().withQuery(queryBuilder);


        /**
         *创建聚合函数：本例子是以 projectOwner公司名称分组然后计算其contractAmount合同金额总和并且按照合同金额倒序排序
         * 相当于SQL语句 select projectOwner,sum(contractAmount) as contractAmount from peoject group by projectOwner order by contractAmount desc
         */
        TermsBuilder projectOwnerAgg =
                AggregationBuilders.terms("partyB").field("partyB").
                        order(Terms.Order.aggregation("contractAmount",false));
        TermsBuilder contractNoAgg = AggregationBuilders.terms("contractNo").field("contractNo");
        SumBuilder contractAmountAgg = AggregationBuilders.sum("contractAmount").field("contractAmount");
        nativeSearchQueryBuilder.addAggregation(projectOwnerAgg.subAggregation(contractNoAgg).subAggregation(contractAmountAgg));
        SearchQuery searchQuery = nativeSearchQueryBuilder.build();



        /**
         * ③  通过elasticSearch模板elasticsearchTemplate.query()方法查询,获得聚合(常用)
         */
        Aggregations aggregations = elasticsearchTemplate.query(searchQuery, new ResultsExtractor<Aggregations>() {
            @Override
            public Aggregations extract(SearchResponse searchResponse) {
                return searchResponse.getAggregations();
            }
        });

        /**
         * 对查询结果处理
         */
        //转换成map集合
        Map<String, Aggregation> aggregationMap = aggregations.asMap();
        //获得对应的聚合函数的聚合子类，该聚合子类也是个map集合,里面的value就是桶Bucket，我们要获得Bucket
        Terms stringTerms = (Terms)aggregationMap.get("partyB");
        //获得所有的桶
        List<Terms.Bucket> buckets = stringTerms.getBuckets();

       /* 一 、使用Iterator遍历桶
        //将集合转换成迭代器遍历桶,当然如果你不删除buckets中的元素，直接foreach遍历就可以了
       Iterator<Terms.Bucket> iterator = buckets.iterator();
        List<String> projectOwnerList = new ArrayList<>();
        while (iterator.hasNext()){
        //bucket桶也是一个map对象，我们取它的key值就可以了
            String projectOwner = iterator.next().getKeyAsString();
            //根据projectOwner去结果中查询即可对应的文档，添加存储数据的集合
            projectOwnerList.add(projectOwner);
        }*/

        if (buckets.size()>0){
            //② 使用 foreach遍历就可以了
            String invoiceDateStart = projectYear + "-01-01 00:00:00";
            String invoiceDateEnd = projectYear + "-12-31 23:59:59";
            for (Terms.Bucket bucket : buckets){
                Map<String,Object> map = new HashMap<>();
                String projectOwner = bucket.getKeyAsString();
                //得到所有子聚合
                Map<String, Aggregation> contractAmountSumbuilder = bucket.getAggregations().asMap();
                //sum值获取方法 如果是avg，那么这里就是Avg格式
                Sum contractAmounts = (Sum)contractAmountSumbuilder.get("contractAmount");
                double contractAmount = contractAmounts.getValue();
                BigDecimal contractAmoutB = BigDecimal.valueOf(contractAmount);
                //获取合同编号
                Terms contractNoTerm = (Terms) contractAmountSumbuilder.get("contractNo");
                List<Terms.Bucket> contractNoTermBuckets = contractNoTerm.getBuckets();
                String contractNO = contractNoTermBuckets.get(0).getKeyAsString();
                //根据合同编号查询开票额度
//               BigDecimal invoiceAmount = new BigDecimal(0);
//               ProjectContractInvoiceEO projectContractInvoiceEO = myProjectContractInvoiceEODao.
//                       queryCurrentYearInvoiceAmountByContractId(contractNO, invoiceDateStart, invoiceDateEnd);

//               if(StringUtils.isNotEmpty(projectContractInvoiceEO)){
//                   invoiceAmount = projectContractInvoiceEO.getInvoiceAmount();
//               }
                QueryBuilder qb = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("partyB", projectOwner));
                List<Project> projectList = Lists.newArrayList(projectRepository.search(qb));
                List<String> projectIdList = getProjectIdList(projectList);
                BigDecimal resInvoiceAmount;
                if (CollectionUtils.isNotEmpty(projectIdList)) {
                    Double invoiceAmount = myProjectContractInvoiceEODao
                            .queryCurrentYearInvoiceAmountByProjectIdList(projectIdList, invoiceDateStart, invoiceDateEnd);
                    if (null == invoiceAmount) {
                        invoiceAmount = 0.0D;
                    }
                    resInvoiceAmount = new BigDecimal(invoiceAmount).setScale(2, RoundingMode.HALF_UP);
                } else {
                    resInvoiceAmount = new BigDecimal(0);
                }

                map.put("projectOwner",projectOwner);//公司名称
                map.put("contractNO",contractNO);//合同编号
                map.put("contractAmount",contractAmoutB);//合同额
                map.put("invoiceAmount", resInvoiceAmount);//开票额
                result.add(map);
            }
        }
        return result;
    }

    /**
     * 各公司经营情况统计 （按照当前年份）验证聚合是否出错
     *
     * @return
     * @throws Exception
     */
    public  List<Map<String, Object>> getPartBOperationStatistic() throws Exception {
        List<Map<String, Object>> result = new ArrayList<>();

        Map<String, BigDecimal> partBContractAmountMap = new HashMap<>();
        Set<String> partyBSet = new HashSet<>();
        Map<String, Set<String>> partBProjectIdSetMap = new HashMap<>();
        DateTime thisDateTime = new DateTime();
        Integer projectYear = thisDateTime.getYear();

        List<Project> projectList = projectRepository.findByProjectTypeAndDelFlagNot(0, true);
        for (Project project : projectList) {
            String  partB = project.getPartyB();
            if (StringUtils.contains(partB,"天津卡达")) {
                partB = "中汽数据（天津）有限公司";
            }
            if (StringUtils.contains(partB,"北京卡达")) {
                partB = "中汽数据有限公司";
            }
            //---------------处理金额的保存--------------------
            if(projectYear.intValue() == project.getProjectYear()) {
                BigDecimal contractAmount = BigDecimal.valueOf(0);
                if (StringUtils.isNotEmpty(project.getContractAmountStr())) {
                    contractAmount = new BigDecimal(project.getContractAmountStr());
                } else {
                    contractAmount = new BigDecimal(project.getContractAmount());
                }
                BigDecimal tempContractAmount = partBContractAmountMap.get(partB);
                if (null != tempContractAmount) {
                    contractAmount = tempContractAmount.add(contractAmount);
                }
                partBContractAmountMap.put(partB, contractAmount);
            }
            //-----------乙方的保存--------------
            Set<String> projectIdSet = partBProjectIdSetMap.get(partB);
            if (StringUtils.isNotEmpty(partB) && CollectionUtils.isEmpty(projectIdSet)) {
                projectIdSet = new HashSet<>();
                partyBSet.add(partB);
            }else if (StringUtils.isEmpty(partB)){
                continue;
            }
            projectIdSet.add(project.getId());
            partBProjectIdSetMap.put(partB, projectIdSet);
        }
        List<ProjectContractInvoiceEO> projectContractInvoiceEOList = myProjectContractInvoiceEODao.queryAll();
        for (String partyB : partyBSet) {
            Map<String, Object> resMap = new HashMap<>();
            Set<String> projectIdSet = partBProjectIdSetMap.get(partyB);
            BigDecimal contractAmoutB = BigDecimal.valueOf(0);
            if (CollectionUtils.isEmpty(projectIdSet)) {
                continue;
            }
            List<String> projectIdList = new ArrayList<>(projectIdSet);
            BigDecimal resInvoiceAmount = BigDecimal.ZERO;
            if (CollectionUtils.isNotEmpty(projectIdList)) {
                for (ProjectContractInvoiceEO projectContractInvoiceEO :projectContractInvoiceEOList) {
                    if (null != projectContractInvoiceEO.getInvoiceDate()) {
                        DateTime dateTime = new DateTime(projectContractInvoiceEO.getInvoiceDate());
                        if (projectIdSet.contains(projectContractInvoiceEO.getProjectId()) && dateTime.getYear()==projectYear.intValue()) {
                            resInvoiceAmount = resInvoiceAmount.add(sumProjectContractInvoiceEOAmount(projectContractInvoiceEO));
                        }
                    }
                }
            }
            if (null != partBContractAmountMap.get(partyB)) {
                contractAmoutB = partBContractAmountMap.get(partyB);
            }
            resMap.put("partyB", partyB);//公司名称
            resMap.put("contractAmount", contractAmoutB);//合同额
            resMap.put("invoiceAmount", resInvoiceAmount);//开票额
            result.add(resMap);
        }
        return result;
    }

    public BigDecimal sumProjectContractInvoiceEOAmount(ProjectContractInvoiceEO projectContractInvoiceEO) {
        BigDecimal amount = BigDecimal.ZERO;
        if (null != projectContractInvoiceEO.getActualInvoiceAmount()){
            amount = amount.add(projectContractInvoiceEO.getActualInvoiceAmount());
        }

        if (null != projectContractInvoiceEO.getChangeInvoiceAmount()){
            amount = amount.add(projectContractInvoiceEO.getChangeInvoiceAmount());
        }

        if (null != projectContractInvoiceEO.getBackInvoiceAmount()){
            amount = amount.subtract(projectContractInvoiceEO.getBackInvoiceAmount());
        }

        return amount;
    }



    List<String> getProjectIdList(List<Project> projectList) {
        List<String> projectIdList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(projectList)) {
            for (Project project : projectList) {
                projectIdList.add(project.getId());
            }
        }
        return projectIdList;
    }

    public Project updateWithoutShiroAuthentication(ProjectVO projectVO) throws Exception {
        Project projectOri = projectRepository.findOne(projectVO.getId());
        Project pro = projectRepository.findOne(projectVO.getId());
        Set<String> memberIdSet = new HashSet<>();
        HashMap<String,List<String>> DeptUserIdMap = new HashMap<>();
        List<Map<String,String>> deptInfoListMap = projectVO.getDeptInfoListMap();// 所选部门ID和类型 如类型=1 只选当前自身 类型=2 包含子部门
        for(Map<String,String> map :deptInfoListMap){
            int type =  Integer.parseInt(map.get("type").toString());
            String deptId =  map.get("deptId").toString();
            Set<String> tempSet = new HashSet<>();
            if(type == 1){
                List<String> deptList = new ArrayList<>();
                deptList.add(deptId);
                List<UserEO> list = userEOService.getAllUserEOsByOrgIds(deptList);//根据部门id查询成员
                for(UserEO userEO:list){
                    tempSet.add(userEO.getUsid());
                    memberIdSet.add(userEO.getUsid());
                }
                DeptUserIdMap.put(deptId,new ArrayList<>(tempSet));
            }else if(type == 2){
                List<UserEO> list=orgEOService.listUserEOByOrgId(deptId);
                for(UserEO userEO:list){
                    memberIdSet.add(userEO.getUsid());
                    tempSet.add(userEO.getUsid());
                }
                DeptUserIdMap.put(deptId,new ArrayList<>(tempSet));
            }
        }
        projectVO.setDeptIdUserIdList(DeptUserIdMap);
        String[] projectMemberIds = projectVO.getProjectMemberIds();
        List<OrgEO> orgEOList = orgEODao.queryOrgAll();
        if (CollectionUtils.isNotEmpty(projectMemberIds)) {
            Set<String> projectMemberIdSet = new HashSet<>(Arrays.asList(projectMemberIds));
            projectMemberIdSet.addAll(memberIdSet);
            if (StringUtils.isNotEmpty(projectVO.getProjectAdminId())) { //将项目管理员加入项目成员里面去
                projectMemberIdSet.add(projectVO.getProjectAdminId());
            }
            if (StringUtils.isNotEmpty(projectVO.getProjectLeaderId())) {
                projectMemberIdSet.add(projectVO.getProjectLeaderId());
            }
            List<UserEO> userEOList = userEODao.getUserWithRolesByUserIds(new ArrayList<String>(projectMemberIdSet));
            String[] projectMemberNameArr = new String[userEOList.size()];
            for (int i = 0; i < userEOList.size(); i++) {
                UserEO userEO = userEOList.get(i);
                projectMemberNameArr[i] = userEO.getUsname();
                memberIdSet.add(userEO.getUsid());
            }
            projectVO.setMemberNames(projectMemberNameArr);
            projectVO.setProjectMemberIds(memberIdSet.toArray(new String[memberIdSet.size()]));
            projectVO.setProjectMemberNames(StringUtils.join(projectMemberNameArr, ','));
            projectVO.setMapList(CommonUtil.userIdUsnameMapKv(userEOList));
            projectVO.setUserIdDeptNameMapList(CommonUtil.userIdDeptNamesMapKv(userEOList, orgEOList));
        }
        Project project = BeanUtils.map(projectVO, Project.class);
        project.setModifyTime(new Date());
//        BudgetEO budgetEO = budgetEODao.selectByPrimaryKey(projectVO.getId());
        //更新项目时随之更新用户和任务的关联关系
        updateUserUserWithProjectInMember(projectVO, pro);
//        updateProjectAdmin(projectVO, pro, budgetEO);
        // 复制属性
        BeanUtils.copyPropertiesIgnoreNullValue(project, projectOri);
        //解决将内部联系人选择自己，成员选择其他人。保存后，成员中未自动加入“内部联系人”的问题
//        Project newProject = updateProjectMember(projectOri);
        // 保存
        Project st = projectRepository.save(projectOri);
        return st;
    }

    public Map<String,List<String>> getAllEmpIdProjectMap(){
        List<String> empIdList=userEPDao.getAllEmpIds();
        Map<String,List<String>> empIdProjectIdsMapList=new HashMap<>();
        for(String empId: empIdList){
            empIdProjectIdsMapList.put(empId,new LinkedList<>());
        }
        return empIdProjectIdsMapList;

    }


    public Map<String,List<String>> getAllEmpIdTaskMap(){
        List<String> empIdList=userEPDao.getAllEmpIds();
        Map<String,List<String>> empIdTaskIdsMapList=new HashMap<>();
        for(String empId: empIdList){
            empIdTaskIdsMapList.put(empId,new LinkedList<>());
        }
        return empIdTaskIdsMapList;

    }

    public Map<String,List<String>> getAllEmpIdSubTaskMap(){
        List<String> empIdList=userEPDao.getAllEmpIds();
        Map<String,List<String>> empIdSubTaskIdsMapList=new HashMap<>();
        for(String empId: empIdList){
            empIdSubTaskIdsMapList.put(empId,new LinkedList<>());
        }
        return empIdSubTaskIdsMapList;

    }




    public Map<String,List<String>> getEmpIdSubTaskIdListMap(){
        Iterable<ChildrenTask> childrenTasks = childTaskRepository.findAll();
        List<ChildrenTask> childrenTaskList = Lists.newArrayList(childrenTasks);

        //定义Map 保存 用户所参与的子任务id
        Map<String, List<String>> empIdChildrenTaskIdListMap = getAllEmpIdTaskMap();

        //遍历所有的ChildrenTask
        for (ChildrenTask childrenTask : childrenTaskList) {
            if (StringUtils.isEmpty(childrenTask.getId())) continue;

            //获取某个子任务的成员
            String[] memberIds = childrenTask.getMemberIds();
            if (memberIds != null) {
                //把项目id添加到Map中用户id的value list
                for (String empId : memberIds) {
                    List<String> childrenTaskIdList = empIdChildrenTaskIdListMap.get(empId);
                    if (childrenTaskIdList != null) {
                        childrenTaskIdList.add(childrenTask.getId());
                    }
                }
            }
        }
        return empIdChildrenTaskIdListMap;
    }

    public Map<String,List<String>> getEmpIdTaskIdListMap(){
        Iterable<Task> tasks = taskRepository.findAll();
        List<Task> taskList = Lists.newArrayList(tasks);

        //定义Map 保存 用户所参与的任务id
        Map<String, List<String>> empIdTaskIdListMap = getAllEmpIdTaskMap();

        //遍历所有的Task
        for (Task task : taskList) {
            if (StringUtils.isEmpty(task.getId())) continue;

            //获取某个任务的成员
            String[] memberIds = task.getMemberIds();
            if (memberIds != null) {
                //把项目id添加到Map中用户id的value list
                for (String empId : memberIds) {
                    List<String> taskIdList = empIdTaskIdListMap.get(empId);
                    if (taskIdList != null) {
                        taskIdList.add(task.getId());
                    }
                }
            }
        }
        return empIdTaskIdListMap;
    }


    public Map<String,List<String>> getEmpIdProjectIdListMap(){
        Iterable<Project> projects = projectRepository.findAll();
        List<Project> projectList = Lists.newArrayList(projects);

        //定义Map 保存 用户所参与的任务id
        Map<String, List<String>> empIdProjectIdsListMap = getAllEmpIdProjectMap();

        //遍历所有的project
        for (Project project: projectList) {
            if (StringUtils.isEmpty(project.getId())) continue;

            //获取某个project的成员
            String[] memberIds = project.getProjectMemberIds();
            if (memberIds != null) {
                //把项目id添加到Map中用户id的value list
                for (String empId : memberIds) {
                    List<String> taskIdList = empIdProjectIdsListMap.get(empId);
                    if (taskIdList != null) {
                        taskIdList.add(project.getId());
                    }
                }
            }
        }
        return empIdProjectIdsListMap;
    }





    /**
     * 处理userWithProjects重构

     */
    public String userWithProjectsRebuilding()
    {
        //【1】 根据员工信息去构建userWithProjext
        ThreadPoolExecutor executorService=new ThreadPoolExecutor(8,8,5000, TimeUnit.SECONDS,new LinkedBlockingDeque<>(5000));
        Map<String,List<String>> empIdSubTaskIdsListMap=getEmpIdSubTaskIdListMap();
        Map<String,List<String>> empIdTaskIdsListMap=getEmpIdTaskIdListMap();
        Map<String,List<String>> empIdProjectIdsListMap=getEmpIdProjectIdListMap();

        Set<String> empIdSet = empIdSubTaskIdsListMap.keySet();

        //开始更新userWithProject的childTaskIds
        count.set(empIdSet.size());
        for (String empId : empIdSet) {
            executorService.execute(new UserWithProjectWorkerForSubTask(empId, empIdSubTaskIdsListMap.get(empId)));
        }
        //仍有未执行完的线程，等待
        while (count.get() > 0) {
            try {
                Thread.sleep(200);
            } catch (Exception ex) {

            }
        }

        //开始更新userWithProject的taskIds
        count.set(empIdSet.size());
        for (String empId : empIdSet) {
            executorService.execute(new UserWithProjectWorkerForTask(empId, empIdTaskIdsListMap.get(empId)));
        }
        //仍有未执行完的线程，等待
        while (count.get() > 0) {
            try {
                Thread.sleep(200);
            } catch (Exception ex) {

            }
        }


        //开始更新userWithProject的projectIds
        count.set(empIdSet.size());
        for (String empId : empIdSet) {
            executorService.execute(new UserWithProjectWorker(empId, empIdProjectIdsListMap.get(empId)));
        }
        //仍有未执行完的线程，等待
        while (count.get() > 0) {
            try {
                Thread.sleep(200);
            } catch (Exception ex) {

            }
        }


        count.set(0);
        executorService.shutdown();



        //【2】在ChildTask中查出businessAdminId不空的项，把child task id 添加到admin对应的userWithProject childTaskIds
        List<ChildrenTask> childrenTaskList=Lists.newArrayList(childTaskRepository.findAll());
        Map<String,String> businessAdminMap0=new HashMap<>();
        for(ChildrenTask childrenTask: childrenTaskList){
            if(StringUtils.isNotEmpty(childrenTask.getBusinessAdminId())){
                businessAdminMap0.put(childrenTask.getBusinessAdminId(),childrenTask.getId());
            }

        }
        //遍历业务管理员，给业务管理员添加子任务
        List<UserWithProjects> userWithProjectsList0= userWithProjectsRepository.findByUserIdIn(businessAdminMap0.keySet());
        for(UserWithProjects userWithProjects: userWithProjectsList0){
            userWithProjects.getChildTaskIds().add(businessAdminMap0.get(userWithProjects.getUserId()));
        }
        userWithProjectsRepository.save(userWithProjectsList0);




        //【3】在Task中查出businessAdminId或projectAdminId不空的项，把task id 添加到admin对应的userWithProject taskIds
        List<Task> taskList=Lists.newArrayList(taskRepository.findAll());
        Map<String,String> businessAdminMap=new HashMap<>();
        Map<String,String> projectAdminMap=new HashMap<>();
        for(Task task: taskList){
            if(StringUtils.isNotEmpty(task.getBusinessAdminId())){
                businessAdminMap.put(task.getBusinessAdminId(),task.getId());
            }
            if(StringUtils.isNotEmpty(task.getProjectAdminId())){
                projectAdminMap.put(task.getProjectAdminId(),task.getId());
            }
        }
        //遍历业务管理员，给业务管理员添加任务
        List<UserWithProjects> userWithProjectsList= userWithProjectsRepository.findByUserIdIn(businessAdminMap.keySet());
        for(UserWithProjects userWithProjects: userWithProjectsList){
            userWithProjects.getTaskIds().add(businessAdminMap.get(userWithProjects.getUserId()));
        }
        userWithProjectsRepository.save(userWithProjectsList);
        //遍历项目管理员，给项目管理员添加任务
        userWithProjectsList= userWithProjectsRepository.findByUserIdIn(projectAdminMap.keySet());
        for(UserWithProjects userWithProjects: userWithProjectsList){
            userWithProjects.getTaskIds().add(projectAdminMap.get(userWithProjects.getUserId()));
        }
        userWithProjectsRepository.save(userWithProjectsList);





        //【4】在project中查出businessAdminId或projectAdminId不空的项，把project id 添加到admin对应的userWithProject projectIds
        List<Project> projectList=Lists.newArrayList(projectRepository.findAll());
        Map<String,String> businessAdminMap2=new HashMap<>();
        Map<String,String> projectAdminMap2=new HashMap<>();
        for(Project project: projectList){
            if(StringUtils.isNotEmpty(project.getBusinessAdminId())){
                businessAdminMap2.put(project.getBusinessAdminId(),project.getId());
            }
            if(StringUtils.isNotEmpty(project.getProjectAdminId())){
                projectAdminMap2.put(project.getProjectAdminId(),project.getId());
            }
        }
        //遍历业务管理员，给业务管理员添加项目
        List<UserWithProjects> userWithProjectsList2= userWithProjectsRepository.findByUserIdIn(businessAdminMap2.keySet());
        for(UserWithProjects userWithProjects: userWithProjectsList2){
            userWithProjects.getProjectIds().add(businessAdminMap2.get(userWithProjects.getUserId()));
        }
        userWithProjectsRepository.save(userWithProjectsList2);
        //遍历项目管理员，给业务管理员添加项目
        userWithProjectsList2= userWithProjectsRepository.findByUserIdIn(projectAdminMap2.keySet());
        for(UserWithProjects userWithProjects: userWithProjectsList2){
            userWithProjects.getProjectIds().add(projectAdminMap2.get(userWithProjects.getUserId()));
        }
        userWithProjectsRepository.save(userWithProjectsList2);

        return "userWithProjects重构完成";



    }



    /**
     * 处理type project内部的数据一致性问题

     */
    public String updateProjectForConsistency()
    {
        List<Project> projectList=Lists.newArrayList(projectRepository.findAll());
        List<String> empIdList=userEPDao.getAllEmpIds();
        List<OrgEO> orgEOList = orgEODao.queryOrgAll();


        //开始遍历所有项目
        for (Project project : projectList){
            String projectLeaderId=project.getProjectLeaderId();
            String projectAdminId=project.getProjectAdminId();
            String businessAdminId=project.getBusinessAdminId();

            //遍历员工id,将无效的id过滤掉
            Set<String> projectMemberIdSet = new HashSet<>();
            if (CollectionUtils.isNotEmpty(project.getProjectMemberIds())){
                for(String empId: project.getProjectMemberIds()){
                    if(empIdList.indexOf(empId)>=0){
                        projectMemberIdSet.add(empId);
                    }

                }
            }

            //定义deptIdUserIdListMap
            HashMap<String,List<String>> deptIdUserIdListMap=new HashMap<>();

            //获取部门信息,将所选部门的人员加入到人员id列表
            List<Map<String, String>> deptInfoListMap = project.getDeptInfoListMap();
            //如果部门不空
            if(StringUtils.isNotEmpty(deptInfoListMap)){
                //遍历部门开始
                for(Map<String,String> dept: deptInfoListMap){
                    String type=dept.get("type");
                    String deptId=dept.get("id");
                    List<String>  deptUserIdList=new LinkedList<>();

                    //包含子部门
                    if(type.equals("2")){
                        //List<UserEO> list = userEOService.getAllUserEOsByOrgIds(deptIdList);//根据部门id查询成员
                        List<UserEO> userEOList=orgEOService.listUserEOByOrgId(deptId);
                        if(StringUtils.isNotEmpty(userEOList)){
                            for(UserEO userEO:userEOList){
                                projectMemberIdSet.add(userEO.getUsid());
                                deptUserIdList.add(userEO.getUsid());
                            }
                        }
                        //修改上边定义的depIdUserIdList，后边会使用
                        deptIdUserIdListMap.put(deptId,deptUserIdList);
                    }
                    //不包含子部门
                    if(type.equals("1")){
                        List<String> deptIdList=new LinkedList<>();
                        deptIdList.add(deptId);
                        List<UserEO> userEOList = userEOService.getAllUserEOsByOrgIds(deptIdList);
                        if(StringUtils.isNotEmpty(userEOList)){
                            for(UserEO userEO:userEOList){
                                projectMemberIdSet.add(userEO.getUsid());
                                deptUserIdList.add(userEO.getUsid());
                            }
                        }
                        //修改上边定义的depIdUserIdList，后边会使用
                        deptIdUserIdListMap.put(deptId,deptUserIdList);
                    }
                }//遍历部门结束

            }//如果部门不空

            //将管理人员加入到项目人员
            projectMemberIdSet.add(projectLeaderId);
            projectMemberIdSet.add(projectAdminId);
            projectMemberIdSet.add(businessAdminId);



            //如果人员为空
            if (CollectionUtils.isEmpty(projectMemberIdSet)){
                project.setProjectMemberIds(null);
                project.setMapList(null);
                project.setProjectMemberNames(null);
                project.setMemberNames(null);
                project.setUserIdDeptNameMapList(null);
                project.setDeptIdUserIdList(null);
            }

            //如果人员不空
            else{

                List<UserEO> userEOS = userEODao.getUserWithRolesByUserIdsNotDeleted(new ArrayList<>(projectMemberIdSet));
                //mapList
                List<Map<String, String>> mapList=CommonUtil.userIdUsnameMapKv(userEOS);
                //memberNames
                String [] userNameArr = CommonUtil.getUserNameArr(userEOS);
                //projectMemberNames
                String projectMemberNames=StringUtils.join(userNameArr,",");
                //userIdDeptNameMapList
                List<Map<String,String>> userIdDeptNameMapList=CommonUtil.userIdDeptNamesMapKv(userEOS,orgEOList);
                project.setProjectMemberIds(projectMemberIdSet.toArray(new String[projectMemberIdSet.size()]));
                project.setMapList(mapList);
                project.setMemberNames(userNameArr);
                project.setProjectMemberNames(projectMemberNames);
                project.setUserIdDeptNameMapList(userIdDeptNameMapList);
                project.setDeptIdUserIdList(deptIdUserIdListMap);

            }

        }
        if (CollectionUtils.isNotEmpty(projectList)) {
            projectRepository.save(projectList);
        }//结束处理项目
        return "project刷新完成";
    }



}
