package com.adc.da.budget.service;

import com.adc.da.budget.constant.Role;
import com.adc.da.budget.dao.BudgetEODao;
import com.adc.da.budget.dao.TaskResultEODao;
import com.adc.da.budget.dao.UserEPDao;
import com.adc.da.budget.dao.UserProjectCustomDao;
import com.adc.da.budget.dto.PageDTO;
import com.adc.da.budget.entity.*;
import com.adc.da.budget.enums.ProjectStatusEnums;
import com.adc.da.budget.page.ProjectEOPage;
import com.adc.da.budget.repository.*;
import com.adc.da.budget.utils.CommonUtil;
import com.adc.da.budget.vo.BudgetVO;
import com.adc.da.budget.vo.TreeTaskVO;
import com.adc.da.excel.poi.excel.ExcelExportUtil;
import com.adc.da.excel.poi.excel.entity.ExportParams;
import com.adc.da.file.service.FileEOService;
import com.adc.da.login.util.UserUtils;
import com.adc.da.superadmin.service.SuperAdminService;
import com.adc.da.sys.dao.OrgEODao;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.sys.service.UserEOService;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.BeanMapper;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import org.elasticsearch.action.search.*;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.apache.http.HttpHost;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * @Auther: ZHAIKAIXUAN
 * @Date: 2018/11/19 14:33
 * @Description:
 */
@Service
@Slf4j
public class TaskService extends BaseService<Task, String> {



    private Logger logger = LoggerFactory.getLogger(TaskService.class);

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private DailyRepository dailyRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserEPDao userEPDao;

    @Autowired
    private BeanMapper beanMapper;

    @Autowired
    private ChildTaskRepository childTaskRepository;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private BudgetEOService budgetEOService;

    @Autowired
    private ChildrenTaskService childrenTaskService;

    @Autowired
    private TaskPriorityRepository taskPriorityRepository;

    @Autowired
    private UserWithProjectsRepository userWithProjectsRepository;

    @Autowired
    private BudgetEODao budgetEODao;

    @Autowired
    private TaskHistoryEOService taskHistoryEOService;

    @Autowired
    private UserEOService userEOService;

    @Autowired
    private SuperAdminService superAdminService;

    @Autowired
    private MyService myService;

    @Autowired
    private TaskResultEODao taskResultEODao;

    @Autowired
    private OrgEODao orgEODao ;

    @Autowired
    private TaskResultEOService taskResultEOService;

    @Autowired
    private FileEOService fileEOService;

    @Autowired
    private UserProjectCustomDao userProjectCustomDao;

    private static final String DEL_FLAG = "delFlag";

    public String getAllProjectIdsInEs()
    {
        String url="http://192.168.7.124:9200/financial_prd/project/_search";
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            out = new PrintWriter(conn.getOutputStream());
            String param="{\n" +
                    "  \"query\": {\n" +
                    "  \t\"match_all\": {}\n" +
                    "  },\n" +
                    "  \"fields\": [\"id\",\"projectType\",\"delFlag\"],\n" +
                    "   \"size\": 5000,\n" +
                    "  \"from\": 0\n" +
                    "}";
            out.print(param);

            out.flush();

            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            System.out.println(result);

            JsonObject returnData = new JsonParser().parse(result).getAsJsonObject();
            JsonObject hits=returnData.getAsJsonObject("hits");
            JsonArray hits2=hits.getAsJsonArray("hits");
            StringBuffer sb=new StringBuffer();
            int projectIdCount=0;

           Iterator<JsonElement> iterator= hits2.iterator();
           while(iterator.hasNext())
           {
               JsonElement element=iterator.next();
               JsonObject obj=element.getAsJsonObject();

               JsonObject obj2=obj.getAsJsonObject("fields");
               JsonArray arrId=obj2.getAsJsonArray("id");
               JsonArray arrProjectType=obj2.getAsJsonArray("projectType");
               JsonArray delFlag=obj2.getAsJsonArray("delFlag");


               JsonElement eleId=arrId.get(0);
               JsonElement eleProjectType=arrProjectType.get(0);

               if((eleProjectType.getAsString().equals("0")||eleProjectType.getAsString().equals("2"))&&delFlag==null) {
                   sb.append("'");
                   sb.append(eleId.getAsString());
                   sb.append("'");
                   sb.append(",");
                   projectIdCount++;
                   if (projectIdCount % 900 == 0) {
                       sb.append("\r\n");
                   }
               }
           }


           String ids=sb.toString();
           if(ids.endsWith(","))
           {
               ids=ids.substring(0,ids.length()-1);
           }
           System.out.println("***********************************************");
           System.out.println("*************************************total count"+projectIdCount);
            System.out.println(ids);
            System.out.println("***********************************************");
           return ids;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }

      return "error";
    }

    /**
     * @Description: 根据id删除 可以是ids
     * @auther: ZHAIKAIXUAN
     * @params:
     * @return:
     * @date: 2018/11/19 14:44
     */
    public String deleteBatch(String ids) throws Exception {
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        Subject subject = SecurityUtils.getSubject();
        if (StringUtils.isBlank(ids)) {
            throw new AdcDaBaseException("删除失败，请选择删除的任务");
        }
        boolean mananger = false;
        if (subject.hasRole(Role.ZHU_REN) || subject.hasRole(Role.SUPER_ADMIN) || subject.hasRole(Role.PROJECT_ADMIN)) {
            mananger = true;
        }
        String[] idArray = ids.split(",");
        if (!CollectionUtils.isEmpty(idArray)) {
            Project project = new Project();
            BudgetEO budgetEO;
            Task task;
            for (String id : idArray) {
                task = taskRepository.findOne(id);
                //只有超级管理员、主任、业务经理、业务创建人、项目经理、任务创建人可以删除任务
                //项目任务
                if (StringUtils.isBlank(task.getBudgetId())) {
                    project = projectRepository.findOne(task.getProjectId());
                    budgetEO = budgetEOService.selectByPrimaryKey(project.getBudgetId());

                } else {
                    budgetEO = budgetEOService.selectByPrimaryKey(task.getBudgetId());

                }
                if (!mananger && !StringUtils.equals(userId, budgetEO.getCreateUserId())
                    && !StringUtils.equals(userId, budgetEO.getPm())
                    && !StringUtils.equals(userId, budgetEO.getBusinessAdminId())
                    && !StringUtils.equals(userId, project.getProjectLeaderId())
                    && !StringUtils.equals(userId, project.getProjectAdminId())
                    && !StringUtils.equals(userId, task.getApproveUserId())
                ) {
                    throw new AdcDaBaseException("您无权删除任务：" + task.getName());
                }
                BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("taskId", id))
                                                             .mustNot(QueryBuilders.termQuery(DEL_FLAG, true));
                Iterable<ChildrenTask> iterable = childTaskRepository.search(queryBuilder);
                if (iterable.iterator().hasNext()) {
                    throw new AdcDaBaseException("当前任务有所属子任务！");
                }
                BoolQueryBuilder boolQueryBuilder2 = QueryBuilders.boolQuery();
                boolQueryBuilder2.should(QueryBuilders.termQuery("taskIdArray", id))
                                 .mustNot(QueryBuilders.termQuery(DEL_FLAG, true));
                Iterator<Daily> taskIterator = dailyRepository.search(boolQueryBuilder2).iterator();
                if (taskIterator.hasNext()) {
                    throw new AdcDaBaseException("任务下存在日报，无法删除");
                }
            }
        }
        String msg = "删除成功！";
        try {
            for (String id : idArray) {
                Task task = taskRepository.findOne(id);
                task.setDelFlag(true);
                //删除任务时同时删除任务优先级数据
                taskPriorityRepository.delete(task.getId());
                taskRepository.save(task);
                //删除任务同时删除用户业务树关联中的任务ID  需要判断如果是业务任务  当前业务下是否还有参与的任务，如果没有 删除业务数据
                Set<String> userIds = getUserIds(task);
                childrenTaskService.setUserWithProjectsData(userWithProjectsRepository,
                    UserWithProjects.class.getMethod("getTaskIds"), userIds, false, task.getId());
                /**
                 * 如果是业务任务，需要判断当前人是否是业务经理或者业务创建人
                 * 只是成员需要判断当前业务下是否还有他的任务存在，如果没有移除当前业务在用户关联表
                 */
                if (StringUtils.isEmpty(task.getProjectId())) {
                    BudgetEO budgetEO = budgetEODao.selectByPrimaryKey(task.getBudgetId());
                    String[] memberIds = task.getMemberIds();
                    for (String memberId : memberIds) {
                        if (!StringUtils.equals(memberId, budgetEO.getPm()) &&
                            !StringUtils.equals(memberId, budgetEO.getCreateUserId())) {
                            List<Task> tasks = taskRepository
                                .findByBudgetIdAndMemberIdsInAndDelFlagNot(budgetEO.getId(), memberId, Boolean.TRUE);
                            if (CollectionUtils.isEmpty(tasks)) {
                                UserWithProjects userWithProjects = userWithProjectsRepository.findOne(memberId);
                                userWithProjects.getBusinessIds().remove(budgetEO.getId());
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.info(e.getMessage());
            throw new AdcDaBaseException("删除失败!");
        }
        return msg;
    }

    /**
     * @Description: 查找
     * @auther: ZHAIKAIXUAN
     * @params:
     * @return:
     * @date: 2018/11/19 14:45
     */
    public Task querySingle(String id) throws Exception {
        Task task = taskRepository.findByIdAndDelFlagNot(id, Boolean.TRUE);
        if (null == task) {
            throw new AdcDaBaseException("任务不存在!");
        }
        if (!StringUtils.isEmpty(task.getProjectId())) {
            /*
             * 项目任务
             */
            Project projectVO = projectService.querySingle(task.getProjectId());
            task.setManager(projectVO.getManager());
            String projectName = getProjectName(task.getProjectId());
            task.setProjectName(projectName);
            //add by liqiushi 20190425 添加状态按钮校验 项目任务由项目经理点击按钮,业务任务由业务经理或者是业务创建人点击按钮
        } else {
            /*业务任务 */
            BudgetVO budget = budgetEOService.findOne(task.getBudgetId());
            task.setBudgetName(budget.getProjectName());
            task.setManager(budget.getManager());
        }

        //追加任务负责人的任务编辑权限
        if (!task.getManager()) {
            String userId = UserUtils.getUserId();
            task.setManager(Objects.equals(userId, task.getApproveUserId()));
        }

        //任务组成员也可以进行任务的完成操作
        if (StringUtils.equals("进行中", task.getTaskStatus())
            && StringUtils.isEmpty(task.getBtnFlag())) {
            task.setBtnFlag("0");
        }
        //拼人员组织机构, 目前未使用

        List<TaskResultEO> taskResultEOList = taskResultEODao.selectByTaskId(task.getId());
        List<TaskResultEO> resTaskResultEOList = new ArrayList<>();
        for (TaskResultEO taskResultEO : taskResultEOList){
            if (StringUtils.isNotEmpty(taskResultEO.getResultName())) {
                resTaskResultEOList.add(taskResultEO);
            }
        }
        task.setTaskResultEOList(resTaskResultEOList);

        List<ChildrenTask> childrenTaskList = childTaskRepository.findByTaskId(id);
        //没有子任务 返回false
        if (CollectionUtils.isNotEmpty(childrenTaskList)){
            List<String> childTaskMemberIdList = new ArrayList<>();
            for (ChildrenTask childrenTask : childrenTaskList) {
                if (StringUtils.equals(childrenTask.getStatus(),ProjectStatusEnums.COMPLETE.getStatus())){
                    continue;
                }
                if (null!=childrenTask.getDelFlag()&&childrenTask.getDelFlag()){
                    continue;
                }
                if (CollectionUtils.isNotEmpty(childrenTask.getMemberIds())){
                    childTaskMemberIdList.addAll(Arrays.asList(childrenTask.getMemberIds()));
                }
            }
            task.setChildTaskMemberIdList(childTaskMemberIdList);
        }


        return task;
    }

    public String refreshProject(){

        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.mustNot(QueryBuilders.termQuery(DEL_FLAG, true));
        queryBuilder.must(QueryBuilders.termQuery("name","商务"));

        List<Task> taskList = Lists.newArrayList(taskRepository.search(queryBuilder));
        Set<String> projectIdSet = new HashSet<>();
        Map<String,String> projectIdTaskApproveUserIdMap = new HashMap<>();
        for (Task task : taskList){
            if (StringUtils.isNotEmpty(task.getProjectId())&&StringUtils.isNotEmpty(task.getApproveUserId())) {
                projectIdTaskApproveUserIdMap.put(task.getProjectId(), task.getApproveUserId());
                projectIdSet.add(task.getProjectId());
            }else {
                logger.error(task.getId()+"的 projectId或者approveUserId不存在！" );
            }
        }

        List<Project> projectList = projectRepository.findByIdIn(projectIdSet);
        for (Project project : projectList){
           List<String> projectMemberIdList = new ArrayList<>(Arrays.asList(project.getProjectMemberIds()));
           String businessTaskApproveUserId = projectIdTaskApproveUserIdMap.get(project.getId());
           if (StringUtils.isNotEmpty(businessTaskApproveUserId)){
               projectMemberIdList.add(businessTaskApproveUserId);
               List<UserEO> userEOList = userEOService.getDao().getUserWithRolesByUserIds(projectMemberIdList);
               project.setMemberNames(CommonUtil.getUserNameArr(userEOList));
               project.setMapList(CommonUtil.userIdUsnameMapKv(userEOList));
               project.setUserIdDeptNameMapList(CommonUtil.userIdDeptNamesMapKv(userEOList,orgEODao));
               project.setProjectMemberIds(CommonUtil.getUserIdArr(userEOList));
               project.setProjectMemberNames(StringUtils.join(CommonUtil.getUserNameList(userEOList),','));
           }
        }
        logger.info("刷新完成！");
        projectRepository.save(projectList);

        return "刷新完成！";

    }

    // 导出
    public Workbook excelExport(ExportParams params, Integer type) {
        List<Task> oneList = queryAll(type);
        if (CollectionUtils.isEmpty(oneList)) {
            throw new AdcDaBaseException("没有导出数据！");
        }

        for (Task task : oneList) {
            String names = StringUtils.join(task.getMemberNames(), "，");
            Project one = projectRepository.findOne(task.getProjectId());
            if (null != one
                && !Boolean.TRUE.equals(one.getDelFlag())
                && StringUtils.isNotEmpty(one.getBudget())) {
                task.setBudgetName(one.getBudget());
            }
            task.setMemberNameString(names);
        }
        return ExcelExportUtil.exportExcel(params, Task.class, oneList);
    }

    public void deleteAll() {
        taskRepository.deleteAll();
    }

    public List<Task> queryAll(Integer type) {
        //获取当前用户部门
        List<String> deptList = projectService.getDeptIds();
        if (deptList == null) {
            throw new IllegalArgumentException();
        }
        //当前用户的id
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("请登录！");
        }

        //如果当前用户是部长可以看见本部门所有任务
        Subject subject = SecurityUtils.getSubject();
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.mustNot(QueryBuilders.termQuery(DEL_FLAG, true));
        BoolQueryBuilder qb = QueryBuilders.boolQuery();
        if (type != null && type == 0) {
            queryBuilder.must(QueryBuilders.existsQuery("budgetId"));
        } else {
            queryBuilder.must(QueryBuilders.existsQuery("projectId"));
            qb.should(QueryBuilders.termQuery("projectLeaderId", userId));
        }
        if (subject.hasRole(Role.SUPER_ADMIN) || subject.hasRole(Role.ADMIN) || subject.hasRole(Role.ZHU_REN)) {
            ArrayList<Task> tasks = Lists.newArrayList(taskRepository.search(queryBuilder));
            for (Task task : tasks) {
                if (null != task.getProjectId()) {
                    Project one = projectRepository.findOne(task.getProjectId());
                    if (null != one) {
                        task.setPm(one.getPm());
                        BudgetEO budgetEO = budgetEOService.getDao().selectByPrimaryKey(one.getBudgetId());
                        if (null != budgetEO) {
                            task.setPm(budgetEO.getPm());
                            task.setBusinessCreateUserId(budgetEO.getCreateUserId());
                        }
                    }
                    task.setTaskStatus(ProjectStatusEnums.EXECUTE.getStatus());
                    taskRepository.save(task);
                } else {
                    BudgetEO budgetEO = budgetEOService.getDao().selectByPrimaryKey(task.getBudgetId());
                    if (null != budgetEO) {
                        task.setPm(budgetEO.getPm());
                        task.setTaskStatus(ProjectStatusEnums.EXECUTE.getStatus());
                        task.setBusinessCreateUserId(budgetEO.getCreateUserId());
                    }
                    taskRepository.save(task);
                }
            }
            return tasks;
        }
        if (subject.hasRole(Role.BU_ZHANG)) {
            for (String deptId : deptList) {
                queryBuilder.must(QueryBuilders.termQuery("deptId", deptId));
            }
            //当前部门下的所有任务任务
            ArrayList<Task> taskList = Lists.newArrayList(taskRepository.search(queryBuilder));
            if (CollectionUtils.isEmpty(taskList)) {
                return new ArrayList<>();
            }
            setProjectName(taskList);
            return taskList;
        }
        qb.should(QueryBuilders.termQuery("memberIds", userId))
          .should(QueryBuilders.termQuery("createUserId", userId));
        queryBuilder.must(qb);
        List<Task> resultList = Lists.newArrayList(taskRepository.search(queryBuilder));
        if (null != resultList) {
            setProjectName(resultList);
        }
        return resultList;
    }

    // 任务查询页

    /**
     * 根据任务ID查询所属子任务
     *
     * @param taskId
     * @return
     */
    public PageDTO findChildrenTasksByTaskId(Integer page, Integer size, String taskId) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                                                     .mustNot(QueryBuilders.termQuery(DEL_FLAG, true))
                                                     .must(QueryBuilders.matchQuery("taskId", taskId));
        Page<ChildrenTask> childrenTaskPage = childTaskRepository.search(queryBuilder, new PageRequest(page - 1, size));
        List<ChildrenTask> childrenTaskList = childrenTaskPage.getContent();
        long totalElements = childrenTaskPage.getTotalElements();
        return new PageDTO(totalElements, childrenTaskList, page, size);
    }

    /**
     * 分页查询
     */
    public PageDTO queryByPage(int page, int size, String projectId, String budgetId, Integer type) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.mustNot(QueryBuilders.termQuery(DEL_FLAG, true));
        if (StringUtils.isNotEmpty(projectId)) {
            queryBuilder.must(QueryBuilders.termQuery("projectId", projectId));
        }
        //根据业务ID查询所属任务
        if (StringUtils.isNotEmpty(budgetId)) {
            queryBuilder.must(QueryBuilders.termQuery("budgetId", budgetId));
        }
        return getPageDTO(queryBuilder, page, size);
//        //获取当前用户部门
    }

    /**
     * 条件分页查询
     */
    @Deprecated
    public PageDTO queryByPage(int page, int size, String projectID) {

        List<Task> queryList = null;
        QueryBuilder query = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("projectId", projectID));
        long count = taskRepository.countByProjectIdEquals(projectID);
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        Page<Task> queryPage = elasticsearchRepository.search(query, new PageRequest(page - 1, size, sort));
        queryList =
            (queryPage == null || (queryList = queryPage.getContent()) == null) ? new ArrayList<Task>() : queryList;
        setProjectName(queryList);
        return new PageDTO(count, queryList, page, size);
    }

    public List<TreeTaskVO> getTaskTree(String userId, String keyword) throws Exception {
        //当前用户Id
        if (userId == null) {
            userId = UserUtils.getUserId();
        }
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("请登录！");
        }

        //获取所有当前用户的任务
        List<TreeTaskVO> treeTaskVOList = setTaskTreeData(userId);
        //return getHideFilter(getKeywordFilter(treeTaskVOList, keyword),userId);
        return getKeywordFilter(treeTaskVOList, keyword);
    }

    /**
     * 当前登录用户是否有任务
     */
    public Boolean haveTask() throws Exception {
        //当前登录用户Id
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("请登录！");
        }
        QueryBuilder queryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("memberIds", userId))
                                                 .mustNot(QueryBuilders.termQuery(DEL_FLAG, Boolean.TRUE));
        List<Task> tasks = Lists.newArrayList(taskRepository.search(queryBuilder));
        return tasks.size() > 0;
    }

    /**
     * 日报填写，左侧的业务树
     *
     * @param userId
     * @return
     * @throws Exception
     */
    private List<TreeTaskVO> setTaskTreeData(String userId) throws Exception {
        List<TreeTaskVO> treeTaskVOList = new ArrayList<>();
        List<Task> taskList = taskRepository
            .findByMemberIdsInAndTaskStatusAndDelFlagNot(userId, ProjectStatusEnums.EXECUTE.getStatus(), Boolean.TRUE);
        ArrayList<Task> tasks = new ArrayList<>(taskList);

        QueryBuilder queryBuilder = null;
        if (CollectionUtils.isNotEmpty(tasks)) {

            //把隐藏的task去掉
            List<UserProjectCustom> userProjectCustomList =
                    userProjectCustomDao.findHideByStatusAndTypeAndUserId("0","3",userId);
           if(StringUtils.isNotEmpty(userProjectCustomList)){
               List<Task> removeTaskList = new ArrayList<>();
               for (UserProjectCustom userProjectCustom : userProjectCustomList){
                   String hideTaskId = userProjectCustom.getTaskid();
                   for (Task task : tasks) {
                       if(hideTaskId.equals(task.getId())){
                           removeTaskList.add(task);
                           break;
                       }
                   }
               }
               if(removeTaskList.size()>0){
                   tasks = new ArrayList<>(tasks);
                   tasks.removeAll(removeTaskList);
               }
           }


            HashSet<String> budgetIds = new HashSet<>();
            HashSet<String> projectIds = new HashSet<>();
            List<BudgetEO> budgetEOS = new ArrayList<>();
            List<Project> projects = new ArrayList<>();
            for (Task task : tasks) {
                if (StringUtils.isNotEmpty(task.getBudgetId())) {
                    budgetIds.add(task.getBudgetId());
                }
                if (StringUtils.isNotEmpty(task.getProjectId())) {
                    projectIds.add(task.getProjectId());
                }
            }
            if (!budgetIds.isEmpty()) {
                budgetEOS = budgetEOService.selectByPrimaryKeys(new ArrayList<>(budgetIds));
            }
            if (!projectIds.isEmpty()) {
                projects = projectRepository.findByIdInAndDelFlagNot(new ArrayList<>(projectIds),true);
            }
            for (int i = tasks.size() - 1; i >= 0; i--) {
                Task task = tasks.get(i);
                if (StringUtils.isNotEmpty(task.getBudgetId())) {
                    BudgetEO budgetEO = null;
                    for (BudgetEO budgetEO1 : budgetEOS) {
                        if (budgetEO1.getId().equals(task.getBudgetId())) {
                            budgetEO = budgetEO1;
                            break;
                        }
                    }
                    if (null != budgetEO) {
                        if (budgetEO.getProjectName().contains("旧-")) {
                            tasks.remove(i);
                        } else {
                            //业务任务
                            String approveUserId;
                            if (StringUtils.isNotEmpty(task.getApproveUserId())) {
                                approveUserId = task.getApproveUserId();
                            } else {
                                approveUserId = budgetEO.getPm();
                            }
                            TreeTaskVO taskTreeTaskVO = new TreeTaskVO(
                                task.getId(),
                                task.getId(),
                                task.getName(),
                                3,
                                budgetEO.getId(),
                                approveUserId,
                                task.getBudgetId(),
                                task.getProjectId()
                            );
                            TreeTaskVO budgetTreeTaskVO = new TreeTaskVO(
                                budgetEO.getId(),
                                budgetEO.getId(),
                                budgetEO.getProjectName(),
                                1,
                                budgetEO.getId(),
                                null, null, null
                            );
                            if (!treeTaskVOList.contains(budgetTreeTaskVO)) {
                                treeTaskVOList.add(budgetTreeTaskVO);
                            }
                            treeTaskVOList.add(taskTreeTaskVO);
                        }
                    }
                } else {
                    if (StringUtils.isNotEmpty(task.getProjectId())) {
                        Project project = null;
                        for (Project project1 : projects) {
                            if (project1.getId().equals(task.getProjectId())) {
                                project = project1;
                                break;
                            }
                        }
                        if (null != project && StringUtils.isNotEmpty(project.getBudgetId())) {
                            BudgetEO budgetEO = budgetEOService.selectByPrimaryKey(project.getBudgetId());
                            if (null == budgetEO || budgetEO.getProjectName().contains("旧-")) {
                                tasks.remove(i);
                            } else {
                                String approveUserId;
                                if (StringUtils.isNotEmpty(task.getApproveUserId())) {
                                    approveUserId = task.getApproveUserId();
                                } else {
                                    approveUserId = project.getProjectLeaderId();
                                }
                                //项目任务
                                TreeTaskVO taskTreeTaskVO = new TreeTaskVO(
                                    task.getId(),
                                    task.getId(),
                                    task.getName(),
                                    3,
                                    project.getId(),
                                    approveUserId,
                                    task.getBudgetId(),
                                    task.getProjectId()
                                );
                                TreeTaskVO projectTreeTaskVO = new TreeTaskVO(
                                    project.getId(),
                                    project.getId(),
                                    project.getName(),
                                    2,
                                    budgetEO.getId(),
                                    project.getProjectLeaderId(),
                                    task.getBudgetId(),
                                    task.getProjectId()

                                );
                                TreeTaskVO budgetTreeTaskVO = new TreeTaskVO(
                                    budgetEO.getId(),
                                    budgetEO.getId(),
                                    budgetEO.getProjectName(),
                                    1,
                                    budgetEO.getId(), null, null, null
                                );
                                if (!treeTaskVOList.contains(budgetTreeTaskVO)) {
                                    treeTaskVOList.add(budgetTreeTaskVO);
                                }
                                if (!treeTaskVOList.contains(projectTreeTaskVO)) {
                                    treeTaskVOList.add(projectTreeTaskVO);
                                }
                                treeTaskVOList.add(taskTreeTaskVO);
                            }
                        } else {
                            tasks.remove(i);
                        }
                    }
                }
            }
        }
        for (Task task : tasks) {
            /*queryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("taskId", task.getId()))
                                        .must(QueryBuilders.termQuery("personId", userId))
                                        .mustNot(QueryBuilders.termQuery(DEL_FLAG, Boolean.TRUE));*/
            queryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("taskId", task.getId()))
                                        .must(QueryBuilders.fuzzyQuery("memberIds", userId))
                                        .mustNot(QueryBuilders.termQuery(DEL_FLAG, Boolean.TRUE));
            ArrayList<ChildrenTask> childrenTasks = Lists.newArrayList(childTaskRepository.search(queryBuilder));
            if (CollectionUtils.isNotEmpty(childrenTasks)) {


                //过滤隐藏子任务
                List<UserProjectCustom> userProjectCustomList =
                        userProjectCustomDao.findHideByStatusAndTypeAndUserId("0","4",userId);
                if(StringUtils.isNotEmpty(userProjectCustomList)){
                    List<ChildrenTask> removeChildTaskList = new ArrayList<>();
                    for (UserProjectCustom userProjectCustom : userProjectCustomList){
                        String hideChildTaskId = userProjectCustom.getChildtaskid();
                        for (ChildrenTask childrenTask : childrenTasks){
                            if(hideChildTaskId.equals(childrenTask.getId())){
                                removeChildTaskList.add(childrenTask);
                                break;
                            }
                        }
                    }
                    if(removeChildTaskList.size()>0){
                        childrenTasks = new ArrayList<>(childrenTasks);
                        childrenTasks.removeAll(removeChildTaskList);
                    }
                }



                for (ChildrenTask childrenTask : childrenTasks) {
                    //子任务是否完成
                    if (ProjectStatusEnums.COMPLETE.getStatus().equals(childrenTask.getStatus())) {
                        continue;
                    }
                    String approveUserId;
                    if (!StringUtils.isEmpty(task.getApproveUserId())) {
                        approveUserId = task.getApproveUserId();
                    } else {
                        approveUserId = task.getProjectLeaderId();
                    }
                    TreeTaskVO childrenTreeTaskVO = new TreeTaskVO(
                        childrenTask.getId(),
                        childrenTask.getId(),
                        childrenTask.getChildTaskName(),
                        4,
                        task.getId(),
                        approveUserId,
                        task.getBudgetId(),
                        task.getProjectId()
                    );
                    treeTaskVOList.add(childrenTreeTaskVO);
                }
            }
        }
        //按中文首字母排序
        Collections.sort(treeTaskVOList);

        return treeTaskVOList;
    }

    /**
     * 关键词筛选
     *
     * @param list
     * @param keyword
     * @return
     * @author liuzixi
     *     date 2019-03-01
     */
    private List<TreeTaskVO> getKeywordFilter(List<TreeTaskVO> list, String keyword) {
        if (StringUtils.isBlank(keyword)) {
            return list;
        }
        Set<TreeTaskVO> newList = new HashSet<>();

        int i = 0;
        int len = list.size();
        for (i = 0; i < len; i++) {
            TreeTaskVO listTreeVO = list.get(i);
            if (listTreeVO.getName().contains(keyword)) {
                int type = listTreeVO.getType();
                newList.add(listTreeVO);
                if (type == 1) {
                    for (TreeTaskVO treeVO : list) {
                        if (StringUtils.equals(listTreeVO.getId(), treeVO.getParentId())) {
                            //添加项目与业务任务
                            newList.add(treeVO);
                            for (TreeTaskVO treeVO1 : list) {
                                if (StringUtils.equals(treeVO.getId(), treeVO1.getParentId())) {
                                    //添加项目任务与业务任务子任务
                                    newList.add(treeVO1);
                                    for (TreeTaskVO treeVO2 : list) {
                                        if (StringUtils.equals(treeVO1.getId(), treeVO2.getParentId())) {
                                            //添加项目任务子任务
                                            newList.add(treeVO2);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (type == 2) {
                    for (TreeTaskVO treeVO : list) {
                        if (StringUtils.equals(listTreeVO.getParentId(), treeVO.getId())) {
                            //添加业务
                            newList.add(treeVO);
                        }
                        if (StringUtils.equals(listTreeVO.getId(), treeVO.getParentId())) {
                            //添加项目任务
                            newList.add(treeVO);
                            for (TreeTaskVO treeVO1 : list) {
                                if (StringUtils.equals(treeVO.getId(), treeVO1.getParentId())) {
                                    //添加项目任务子任务
                                    newList.add(treeVO1);
                                }
                            }
                        }
                    }
                }
                if (type == 3) {
                    for (TreeTaskVO treeVO : list) {
                        if (StringUtils.equals(listTreeVO.getParentId(), treeVO.getId())) {
                            //添加项目或者是业务
                            newList.add(treeVO);
                            for (TreeTaskVO treeVO1 : list) {
                                if (StringUtils.equals(treeVO.getParentId(), treeVO1.getId())) {
                                    //添加业务
                                    newList.add(treeVO1);
                                }
                            }
                        }
                        if (StringUtils.equals(listTreeVO.getId(), treeVO.getParentId())) {
                            //添加项目任务子任务或者业务任务子任务
                            newList.add(treeVO);
                            for (TreeTaskVO treeVO1 : list) {
                                if (StringUtils.equals(treeVO.getId(), treeVO1.getParentId())) {
                                    //添加项目任务子任务
                                    newList.add(treeVO1);
                                }
                            }
                        }
                    }
                }
                if (type == 4) {
                    for (TreeTaskVO treeVO : list) {
                        if (StringUtils.equals(listTreeVO.getParentId(), treeVO.getId())) {
                            //父任务
                            newList.add(treeVO);
                            for (TreeTaskVO treeVO1 : list) {
                                if (StringUtils.equals(treeVO.getParentId(), treeVO1.getId())) {
                                    //添加业务或者项目
                                    newList.add(treeVO1);
                                    for (TreeTaskVO treeVO2 : list) {
                                        if (StringUtils.equals(treeVO1.getParentId(), treeVO2.getId())) {
                                            newList.add(treeVO2);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return new ArrayList<>(newList);

    }




    private List<TreeTaskVO> getHideFilter(List<TreeTaskVO> list, String userId) {
        List<TreeTaskVO> removeList = new ArrayList<>();
        List<UserProjectCustom> userProjectCustoms = userProjectCustomDao.findByStatus("0",userId);
        if(StringUtils.isNotEmpty(userProjectCustoms)){
            for (UserProjectCustom userProjectCustom:userProjectCustoms){
                String publicId = "";
                String parentPublicId = "";
                String  treeType = userProjectCustom.getType()+"";//1业务2 项目 3任务 4子任务
                if (StringUtils.equals(treeType,"1")){
                    //业务
                    publicId = userProjectCustom.getBusinessid();
                    parentPublicId = userProjectCustom.getBusinessid();
                }else if (StringUtils.equals(treeType,"2")){
                    //项目
                    publicId = userProjectCustom.getProjectid();
                    parentPublicId = userProjectCustom.getBusinessid();
                }else if (StringUtils.equals(treeType,"3")){
                    //任务
                    publicId = userProjectCustom.getTaskid();
                    parentPublicId = userProjectCustom.getProjectid();
                    if(StringUtils.isEmpty(parentPublicId)){
                        parentPublicId = userProjectCustom.getBusinessid();
                    }
                }else if (StringUtils.equals(treeType,"4")){
                    //子任务
                    publicId = userProjectCustom.getChildtaskid();
                    parentPublicId = userProjectCustom.getTaskid();
                }

                for (TreeTaskVO taskVO : list){
                    String id = taskVO.getId();
                    String parentId = taskVO.getParentId();
                    if(id.equals(publicId) && parentId.equals(parentPublicId)){
                        removeList.add (taskVO);
                        break;
                    }
                }
            }
        }

        if(removeList.size()>0){
            list.removeAll(removeList);

        }
        return list;

    }

    /**
     * 任务list加入项目名称
     *
     * @param taskList
     */
    private void setProjectName(List<Task> taskList) {
        for (Task task : taskList) {
            if (task.getProjectId() != null) {
                String projectName = getProjectName(task.getProjectId());
                task.setProjectName(projectName);
            } else {
                BudgetEO budgetEO = null;
                try {
                    budgetEO = budgetEOService.selectByPrimaryKey(task.getBudgetId());
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
                if (budgetEO != null) {
                    task.setBudgetName(budgetEO.getProjectName());
                }
            }

        }
    }

    /**
     * 获取项目名称
     *
     * @param projectId
     * @return
     */
    private String getProjectName(String projectId) {
        Project project = projectRepository.findOne(projectId);
        if (project == null || Boolean.TRUE.equals(project.getDelFlag())) {
            return "";
        }
        return project.getName();
    }

    /**
     * 查询当前用户拥有的项目
     *
     * @return
     */
    @SuppressWarnings("all")
    public List<Project> currentUserProject() {
        String userId = UserUtils.getUserId();
        List<Project> resultList = new ArrayList<>();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("请登录！");
        }
        //已隐藏的所有个人项目
        List<UserProjectCustom> userProjectCustomList =
                userProjectCustomDao.findHideByStatusAndTypeAndUserId("0","2",userId);
        // 超级管理员能够在任意项目下创建任务，不受权限影响
        if (superAdminService.isSuperAdmin()) {
            List<String> budgetIdList = budgetEODao.findAllBudgetIdByNameNotLike("旧-%");
            Iterable<Project> projectIterable = projectRepository.findByBudgetIdInAndDelFlagNot(budgetIdList, true);
            //return Lists.newArrayList(projectIterable);
            //过滤隐藏的项目信息;
            //                            break;
            List<Project> projectList = Lists.newArrayList(projectIterable);
            if(StringUtils.isNotEmpty(projectList)){
                List<Project> removeList = new ArrayList<>();
                for (UserProjectCustom userProjectCustom : userProjectCustomList){
                    String hideProjectId = userProjectCustom.getProjectid();
                    for (Project project : projectList){
                        if (hideProjectId.equals(project.getId())){
                            removeList.add(project);
                        }
                    }
                }
                if(removeList.size()>0){
                    projectList.removeAll(removeList);
                }
            }
            return projectList;
        }
        UserWithProjects userWithProjects = userWithProjectsRepository.findOne(userId);
        if (null != userWithProjects) {
            Set<String> projectIds = userWithProjects.getProjectIds();
            projectIds.removeAll(Collections.singleton(null));
            if (CollectionUtils.isNotEmpty(projectIds)) {

                ///过滤隐藏的项目id
                List<String> removeProjectIdList = new ArrayList<>();
                for (UserProjectCustom userProjectCustom : userProjectCustomList){
                    String hideProjectId = userProjectCustom.getProjectid();
                    for (String projectId : projectIds){
                        if (hideProjectId.equals(projectId)){
                            removeProjectIdList.add(projectId);
                            break;
                        }
                    }
                }
                if(removeProjectIdList.size()>0){
                    projectIds.removeAll(removeProjectIdList);
                }

                for (String projectId : projectIds) {
                    Project project = projectRepository.findOne(projectId);
                    //不能看到已完成和审批中的項目
                    if (null != project && StringUtils
                        .equals(project.getFinishedStatus(), ProjectStatusEnums.EXECUTE.getStatus())) {
                        //不添加旧业务下的项目
                        String budgetId = project.getBudgetId();
                        try {
                            BudgetEO budgetEO = budgetEOService.selectByPrimaryKey(budgetId);
                            if (null != budgetEO && !budgetEO.getProjectName().contains("旧-")) {
                                resultList.add(project);
                            }
                        } catch (Exception e) {
                            logger.warn("currentUserProject 异常 {}", e.getMessage(), e);
                        }
                    }
                }
            }
            return resultList;
        }
        return null;
    }




    private PageDTO getProjectPageDTO(QueryBuilder queryBuilder, int page, int size) {
        List<Project> queryList = null;
        Sort sort = new Sort(Sort.Direction.DESC, "budgetId");

        Page<Project> queryPage =projectRepository.search(queryBuilder, new PageRequest(page - 1, size, sort));

        queryList =
                (queryPage == null || (queryList = queryPage.getContent()) == null) ? new ArrayList<>() : queryList;
        long count = queryPage == null ? 0 : queryPage.getTotalElements();

        return new PageDTO(count, queryList, page, size);
    }

    /**
     * 按页查询当前用户拥有的项目2
     * @param pageSize 每页的项目数量
     * @param pageIndex 页索引
     * @return
     */
    @SuppressWarnings("all")
    public PageDTO currentUserProjectByPage(int pageSize,int pageIndex,String name) {

        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("请登录！");
        }
        //invalid page size or page index, then return null;
        if (pageSize <= 0 || pageIndex < 1) {
            return new PageDTO();
        }
        //已隐藏的所有个人项目
        List<UserProjectCustom> userProjectCustomList =
                userProjectCustomDao.findHideByStatusAndTypeAndUserId("0", "2", userId);
        List<String> hideProjectIds = new LinkedList<>();

        //add hidden project id into hideProjectIds
        for (UserProjectCustom userProjectCustom : userProjectCustomList) {
            String hideProjectId = userProjectCustom.getProjectid();
            hideProjectIds.add(userProjectCustom.getProjectid());
        }

        //define a BoolQueryuBuilder which is used to query project in Elasticsearch
        BoolQueryBuilder projectQueryBuilder = QueryBuilders.boolQuery();
        //name query string judgement
        if(name!=null&&!name.trim().equals("")) {
            projectQueryBuilder .must(QueryBuilders.wildcardQuery("name", "*" + name + "*"));
        }

        // 超级管理员能够在任意项目下创建任务，不受权限影响
        if (superAdminService.isSuperAdmin()) {
            List<String> budgetIdList = budgetEODao.findAllBudgetIdByNameNotLike("旧-%");
            //delete flag must not be null
            projectQueryBuilder .mustNot(QueryBuilders.termQuery(DEL_FLAG, true))
            //budget id limitation
            .should(QueryBuilders.termsQuery("budgetId", budgetIdList))
            //must not be in the hide project ids
             .mustNot(QueryBuilders.termsQuery("id", hideProjectIds));
            PageDTO pageDto = getProjectPageDTO(projectQueryBuilder, pageIndex, pageSize);
            return pageDto;
         }
        //get projects of common user
        UserWithProjects userWithProjects = userWithProjectsRepository.findOne(userId);
        //common user who has project(s)
        if (null != userWithProjects) {
            Set<String> projectIds = userWithProjects.getProjectIds();
            //save the project ids which are not hidden
            Set<String> projectIds2 = new HashSet<>();
            projectIds.removeAll(Collections.singleton(null));
            for (String projectId : projectIds) {
                Project project = projectRepository.findOne(projectId);
                //不能看到已完成和审批中的項目
                if (null != project && StringUtils
                        .equals(project.getFinishedStatus(), ProjectStatusEnums.EXECUTE.getStatus())) {
                    //不添加旧业务下的项目
                    String budgetId = project.getBudgetId();
                    try {
                        BudgetEO budgetEO = budgetEOService.selectByPrimaryKey(budgetId);
                        if (null != budgetEO && !budgetEO.getProjectName().contains("旧-")) {
                            projectIds2.add(projectId);
                        }
                    } catch (Exception e) {
                        logger.warn("currentUserProject 异常 {}", e.getMessage(), e);
                    }
                }
            }
            //assure the project status is ongoing
            projectQueryBuilder.must(QueryBuilders.termQuery("finishedStatus", "进行中"))
                    //assure the delete flag is false
                    .mustNot(QueryBuilders.termQuery(DEL_FLAG, true))
                    .must(QueryBuilders.termsQuery("id", projectIds2))
                    //assure project ids are not in hidden project id
                    .mustNot(QueryBuilders.termsQuery("id", hideProjectIds));
            PageDTO pageDto = getProjectPageDTO(projectQueryBuilder, pageIndex, pageSize);
            return pageDto;
        }
        //common user whose does not have project
        return new PageDTO();
    }

    private PageDTO getPageDTO(QueryBuilder queryBuilder, int page, int size) {
        List<Task> queryList = null;
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        Page<Task> queryPage = taskRepository.search(queryBuilder, new PageRequest(page - 1, size, sort));
        queryList =
            (queryPage == null || (queryList = queryPage.getContent()) == null) ? new ArrayList<Task>() : queryList;
        long count = queryPage == null ? 0 : queryPage.getTotalElements();
        //过滤隐藏的任务
        List<UserProjectCustom> userProjectCustomList =
                userProjectCustomDao.findHideByStatusAndTypeAndUserId("0","3",UserUtils.getUserId());
        if (StringUtils.isNotEmpty(userProjectCustomList) && StringUtils.isNotEmpty(queryList)){
            List<Task> removeTaskList = new ArrayList<>();
            for (UserProjectCustom userProjectCustom : userProjectCustomList){
                String hideTaskId = userProjectCustom.getTaskid();
                for (Task task : queryList){
                    if(hideTaskId.equals(task.getId())){
                        removeTaskList.add(task);
                        break;
                    }
                }
            }
            if(removeTaskList.size()>0){
                queryList = new ArrayList<>(queryList);
                queryList.removeAll(removeTaskList);
            }
            count = queryList.size();
        }

        return new PageDTO(count, queryList, page, size);
    }

    public List<Task> getMyTask() {
        List<Task> taskList = new ArrayList<>();
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        String deptId = projectService.getDeptId(userId);
        Subject subject = SecurityUtils.getSubject();
        if (subject.hasRole(Role.SUPER_ADMIN) || subject.hasRole(Role.ADMIN) || subject.hasRole(Role.ZHU_REN)) {
            Iterable<Task> allTask = taskRepository.findAll();
            Iterator<Task> allTaskIterator = allTask.iterator();
            while (allTaskIterator.hasNext()) {
                Task task = allTaskIterator.next();
                if (null != task && !Boolean.TRUE.equals(task.getDelFlag())) {
                    taskList.add(task);
                }
            }
            return taskList;
        }

        if (subject.hasRole(Role.BU_ZHANG)) {
            BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery().should(QueryBuilders.termQuery("deptId", deptId))
                                                         .mustNot(QueryBuilders.termQuery(DEL_FLAG, true));
            return Lists.newArrayList(taskRepository.search(queryBuilder));
        }
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                                                     .should(QueryBuilders.termQuery("projectLeaderId", userId))
                                                     .mustNot(QueryBuilders.termQuery(DEL_FLAG, true))
                                                     .should(QueryBuilders.termQuery("memberIds", userId))
                                                     .should(QueryBuilders.termQuery("businessCreateUserId", userId))
                                                     .should(QueryBuilders.termQuery("pm", userId));
        return Lists.newArrayList(taskRepository.search(queryBuilder));
    }

    /**
     * 临时方法 用来设置用户的项目关联ID列表
     */
    public String setUserWithProjectsData() {
        userWithProjectsRepository.deleteAll();
        List<String> ids = userEPDao.queryAllUsid();
        for (String userId : ids) {
            UserWithProjects userWithProjects = new UserWithProjects();
            userWithProjects.setUserId(userId);
            //查询出当前用户是业务经理或者创建人的所有业务
            List<BudgetEO> budgetEOList = budgetEODao.selectByCreateUserId(userId);
            Set<String> childTaskIds = new HashSet<>();
            Set<String> taskIds = new HashSet<>();
            Set<String> projectIds = new HashSet<>();
            Set<String> businessIds = new HashSet<>();
            if (CollectionUtils.isNotEmpty(budgetEOList)) {
                for (BudgetEO budgetEO : budgetEOList) {
                    businessIds.add(budgetEO.getId());
                }
            }
            //查询当前用户参与或者负责的项目
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            boolQueryBuilder.mustNot(QueryBuilders.termQuery(DEL_FLAG, true));
            BoolQueryBuilder bq;
            bq = QueryBuilders.boolQuery().should(QueryBuilders.termQuery("projectLeaderId", userId))
                              .should(QueryBuilders.termQuery("projectMemberIds", userId))
                              .should(QueryBuilders.termQuery("pm", userId));
            boolQueryBuilder.must(bq);
            List<Project> projectList = Lists.newArrayList(projectRepository.search(boolQueryBuilder));
            if (CollectionUtils.isNotEmpty(projectList)) {
                for (Project project : projectList) {
                    if (StringUtils.isEmpty(project.getBudgetId())) {
                        log.warn(
                            "======================报NULL的业务ID：" + project.getBudgetId() + "=========================");
                        continue;
                    }
                    BudgetEO budget = budgetEODao.selectByPrimaryKey(project.getBudgetId());
                    if (null == budget) {
                        log.warn(
                            "======================不存在的业务ID：" + project.getBudgetId() + "=========================");
                        continue;
                    }
                    projectIds.add(project.getId());
                    businessIds.add(budget.getId());
                }
            }
            //查询出当前用户所有的任务
            BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
            queryBuilder.mustNot(QueryBuilders.termQuery(DEL_FLAG, true));
            BoolQueryBuilder qb = QueryBuilders.boolQuery();
            qb.should(QueryBuilders.termQuery("memberIds", userId))
              .should(QueryBuilders.termQuery("pm", userId))
              .should(QueryBuilders.termQuery("projectLeaderId", userId))
              .should(QueryBuilders.termQuery("createUserId", userId));
            queryBuilder.must(qb);
            ArrayList<Task> tasks = Lists.newArrayList(taskRepository.search(queryBuilder));
            if (CollectionUtils.isNotEmpty(tasks)) {
                for (Task task : tasks) {
                    //业务任务
                    if (StringUtils.isEmpty(task.getProjectId())) {
                        BudgetEO budgetEO = budgetEODao.selectByPrimaryKey(task.getBudgetId());
                        if (null == budgetEO) {
                            log.warn(
                                "-----------==========不存在的业务ID：" + task.getBudgetId() + "=========================");
                            continue;
                        }
                        taskIds.add(task.getId());
                        businessIds.add(budgetEO.getId());
                        //项目任务
                    } else {
                        Project project = projectRepository.findOne(task.getProjectId());
                        if (null == project) {
                            log.warn(
                                "-----------==========不存在的项目ID：" + task.getProjectId() + "=========================");
                            continue;
                        }
                        if (!businessIds.contains(project.getBudgetId())) {
                            log.warn(
                                "------------------不存在的业务ID：" + project.getBudgetId() + "------------------------");
                            continue;
                        }
                        taskIds.add(task.getId());
                    }
                }
            }
            BoolQueryBuilder childTaskQueryBuilder = QueryBuilders.boolQuery();
            childTaskQueryBuilder.mustNot(QueryBuilders.termQuery(DEL_FLAG, true));
            BoolQueryBuilder childTaskQb = QueryBuilders.boolQuery();
            childTaskQb.should(QueryBuilders.termQuery("projectLeaderId", userId))
                       .should(QueryBuilders.termQuery("personId", userId))
                       .should(QueryBuilders.termQuery("pm", userId));
            childTaskQueryBuilder.must(childTaskQb);
            List<ChildrenTask> childrenTasks = Lists.newArrayList(childTaskRepository.search(childTaskQueryBuilder));
            if (CollectionUtils.isNotEmpty(childrenTasks)) {
                for (ChildrenTask childrenTask : childrenTasks) {
                    Task task = taskRepository.findOne(childrenTask.getTaskId());
                    if (!taskIds.contains(task.getId())) {
                        log.warn("------------------不存在的任务ID：" + task.getId() + "------------------------");
                        continue;
                    }
                    childTaskIds.add(childrenTask.getId());
                }
            }
            userWithProjects.setBusinessIds(businessIds);
            userWithProjects.setProjectIds(projectIds);
            userWithProjects.setTaskIds(taskIds);
            userWithProjects.setChildTaskIds(childTaskIds);
            userWithProjectsRepository.save(userWithProjects);
        }
        return "ok";
    }

    /**
     * 获取任务关联的任务创建人，项目经理，业务经理，业务创建人
     *
     * @param task
     * @return
     */
    public Set<String> getUserIds(Task task) {
        Set<String> userIds = new HashSet<>();
        userIds.addAll(Lists.newArrayList(task.getMemberIds()));
        userIds.add(task.getCreateUserId());
        BudgetEO budgetEO;
        if (StringUtils.isEmpty(task.getBudgetId())) {
            Project project = projectRepository.findOne(task.getProjectId());
            budgetEO = budgetEODao.selectByPrimaryKey(project.getBudgetId());
            userIds.add(project.getProjectLeaderId());
            userIds.add(budgetEO.getPm());
            userIds.add(budgetEO.getCreateUserId());
            userIds.add(budgetEO.getBusinessAdminId());
            userIds.add(project.getProjectAdminId());

            task.setProjectAdminId(project.getProjectAdminId());
            task.setProjectAdminName(project.getProjectAdminName());
            task.setBusinessAdminId(budgetEO.getBusinessAdminId());
            task.setBusinessAdminName(budgetEO.getBusinessAdminName());

        } else {
            budgetEO = budgetEODao.selectByPrimaryKey(task.getBudgetId());
            userIds.add(budgetEO.getPm());
            userIds.add(budgetEO.getCreateUserId());
            userIds.add(budgetEO.getBusinessAdminId());
            task.setBusinessAdminId(budgetEO.getBusinessAdminId());
            task.setBusinessAdminName(budgetEO.getBusinessAdminName());
        }
        userIds.removeAll(Collections.singleton(null));
        return userIds;
    }

    public String changeTaskStatus(String taskId, String btnType) {
        Task task = taskRepository.findByIdAndDelFlagNot(taskId, true);
        List<ChildrenTask> childrenTaskList = childTaskRepository.findByTaskIdAndDelFlagNot(taskId, true);
        if (StringUtils.equals(btnType, "进行中")) {
            task.setTaskStatus(ProjectStatusEnums.COMPLETE.getStatus());
            taskRepository.save(task);
        }
        if (CollectionUtils.isNotEmpty(childrenTaskList)) {
            for (ChildrenTask childrenTask : childrenTaskList) {
                childrenTask.setStatus(ProjectStatusEnums.COMPLETE.getStatus());
                childTaskRepository.save(childrenTask);
            }
        }
        return "更改状态成功";
    }

    public List<Task> getTasksByBudgetId(String id) {
        return taskRepository.findByBudgetIdAndDelFlagNot(id, Boolean.TRUE);

    }

    public int queryTaskStatusById(String taskId) {
        return childTaskRepository.countByTaskIdEqualsAndStatusEqualsAndDelFlagNot(
            taskId,
            ProjectStatusEnums.EXECUTE.getStatus(),
            Boolean.TRUE);
    }

    public boolean existTask(String id) {
        Task task = taskRepository.findByIdAndDelFlagNot(id, Boolean.TRUE);
        if (null == task) {
            return false;
        }
        return true;
    }

    public boolean checkUserInChildTask(String taskId, String userId) {
        List<ChildrenTask> childrenTaskList = childTaskRepository.findByTaskId(taskId);
        //没有子任务 返回false
        if (CollectionUtils.isEmpty(childrenTaskList)){
            return false;
        }
        for (ChildrenTask childrenTask : childrenTaskList){
            //如果存在，立即返回true
            if (CommonUtil.arrayContains(childrenTask.getMemberIds(),userId)>-1){
                return true;
            }
        }
        return false;
    }

    public void moveChildTask2Task(String sourceTaskId, String targetTaskId) {
//Task sourceTask = taskRepository.findById(sourceTaskId);
        Task targetTask = taskRepository.findById(targetTaskId);
        ChildrenTask childrenTask = childTaskRepository.findById(sourceTaskId);
        childrenTask.setBudgetId(targetTask.getBudgetId());
        childrenTask.setBudgetName(targetTask.getBudgetName());
        childrenTask.setProjectId(targetTask.getProjectId());
        childrenTask.setProjectName(targetTask.getProjectName());
        childrenTask.setTaskId(targetTaskId);
        childrenTask.setDeptId(targetTask.getDeptId());
        childTaskRepository.save(childrenTask);

    }

    public void moveTask2Project(String taskId, String targetProjectId) {
        Task task = taskRepository.findById(taskId);
        Project project = projectRepository.findById(targetProjectId);
        task.setPm(project.getPm());
        task.setProjectName(project.getName());
        task.setProjectName1(project.getName());
        task.setProjectId(project.getId());
        taskRepository.save(task);
    }
}
