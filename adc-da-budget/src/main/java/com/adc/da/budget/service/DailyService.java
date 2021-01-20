package com.adc.da.budget.service;

import com.adc.da.base.page.BasePage;
import com.adc.da.budget.constant.Role;
import com.adc.da.budget.dao.BranchDao;
import com.adc.da.budget.dao.OrgListDao;
import com.adc.da.budget.dao.UserEPDao;
import com.adc.da.budget.dto.DailyConditionDTO;
import com.adc.da.budget.dto.PageDTO;
import com.adc.da.budget.entity.*;
import com.adc.da.budget.enums.ProjectStatusEnums;
import com.adc.da.budget.page.DailyPage;
import com.adc.da.budget.repository.*;
import com.adc.da.budget.utils.ArrayUtils;
import com.adc.da.budget.utils.BeanUtils;
import com.adc.da.budget.utils.CommonUtil;
import com.adc.da.budget.vo.*;
import com.adc.da.crm.dao.UserDao;
import com.adc.da.crm.entity.CustomerRecordEO;
import com.adc.da.crm.page.CustomerRecordEOPage;
import com.adc.da.crm.service.CustomerRecordEOService;
import com.adc.da.excel.poi.excel.ExcelExportUtil;
import com.adc.da.excel.poi.excel.ExcelImportUtil;
import com.adc.da.excel.poi.excel.entity.ExportParams;
import com.adc.da.excel.poi.excel.entity.ImportParams;
import com.adc.da.excel.poi.excel.entity.params.ExcelExportEntity;
import com.adc.da.file.entity.FileEO;
import com.adc.da.file.service.FileEOService;
import com.adc.da.login.util.UserUtils;
import com.adc.da.sys.dao.OrgEODao;
import com.adc.da.sys.dao.UserEODao;
import com.adc.da.sys.entity.OrgEO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.sys.service.UserEOService;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.*;
import com.adc.da.util.utils.UUID;
import com.adc.da.workflow.dao.ActHistoryDao;
import com.adc.da.workflow.service.ActivitiHistoryDeleteService;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.elasticsearch.search.aggregations.metrics.cardinality.CardinalityBuilder;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.aggregations.metrics.sum.SumBuilder;
import org.elasticsearch.search.aggregations.metrics.tophits.TopHits;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


import static com.adc.da.budget.constant.ProjectSearchField.*;
import static com.adc.da.budget.constant.ProjectSearchField.BUDGET_ID;
import static com.adc.da.budget.constant.ProjectType.*;

import static com.adc.da.budget.constant.ProjectSearchField.CREATE_USER_ID;
import static com.adc.da.budget.constant.ProjectSearchField.DEL_FLAG;


@Service
public class DailyService extends BaseService<Daily, String> {

    /**
     * 表头
     */
    private static final String[] TITLES = {"本部", "部门", "人员", "工时（人天）"};
    @Autowired
    private DailyRepository dailyRepository;

    @Autowired
    private ChildTaskRepository childTaskRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BeanMapper beanMapper;

    @Autowired
    private UserEPDao userEPDao;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserEOService userEOService;

    @Autowired
    private BranchDao branchDao;

    @Autowired
    private CustomerRecordEOService customerRecordEOService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private OrgEODao orgEODao;

    @Autowired
    private OrgListDao orgListDao;

    @Autowired
    private BudgetEOService budgetEOService;

    @Autowired
    private TaskResultEOService taskResultEOService;

    @Autowired
    private FileEOService fileEOService;

    @Autowired
    private UserEODao userEODao;

    @Autowired
    private UserWithProjectsRepository userWithProjectsRepository;

    @Autowired
    private ActivitiHistoryDeleteService activitiHistoryDeleteService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private ActHistoryDao actHistoryDao;
    @Autowired
    private RuntimeService runtimeService;


    private Map<String,String> projectIdBusinessIdMap=new HashMap<>();
    private AtomicInteger numberOfBusinessAndProjects=new AtomicInteger(0);
    private ReentrantLock businessLock=new ReentrantLock();
    private ReentrantLock projectLock=new ReentrantLock();
    ReentrantReadWriteLock lock=new ReentrantReadWriteLock();
    ExecutorService executorService=new ThreadPoolExecutor(4,4,5000, TimeUnit.SECONDS,new LinkedBlockingDeque<>());
    ConcurrentHashMap<String,JsonObject> taskPool=new ConcurrentHashMap<>();

    class TaskWorker implements Runnable{
        List<JsonObject> empBusiness;

        //保存员工项目信息
        List<JsonObject> empProjects;

        String empId;
        String empName;

        String[] taskIdArray;
        TaskWorker(List<JsonObject> empBusiness,List<JsonObject> empProjects,String empId,String empName,String[] taskIdArray){
            this.empBusiness=empBusiness;
            this.empProjects=empProjects;
            this.empId=empId;
            this.empName=empName;
            this.taskIdArray=taskIdArray;
        }

        public void run(){
            for (int i = 0; i < taskIdArray.length; i++) {

                JsonObject obj = getTaskById(taskIdArray[i]);
                if(obj==null)continue;
                obj.addProperty("empId",empId);
                obj.addProperty("empName",empName);

                String businessOrProjectName=obj.get("name").getAsString();
                String objType=obj.get("type").getAsString();

                //如果是业务
                if (objType.equals("1")) {
                    businessLock.lock();
                    if(!jsonObjectExistsInJsonObjectList(empBusiness,obj)){
                        empBusiness.add(obj);
                    }
                    businessLock.unlock();

                }

                //如果是项目
                if (objType.equals("2")) {
                    projectLock.lock();
                    if(!jsonObjectExistsInJsonObjectList(empProjects,obj)){
                        empProjects.add(obj);
                    }
                    projectLock.unlock();
                }
            } //taskIdArray

        }


    }

    class ProjectWorker extends Thread{
        private List<Project> projectList;
        private List<Map<String, String>> allBusinessAndProjects;
        ProjectWorker(List<Project> projectList,List<Map<String, String>> allBusinessAndProjects,String threadName){
            this.projectList=projectList;
            this.allBusinessAndProjects=allBusinessAndProjects;
            setName(threadName);
        }
        @Override
        public void run(){
            logger.info("thread started........................"+this.getName());
            ReentrantReadWriteLock.ReadLock readLock=lock.readLock();
            ReentrantReadWriteLock.WriteLock writeLock=lock.writeLock();

            if(CollectionUtils.isEmpty(projectList))return;
            for(Project proj: projectList){
                if(proj==null)continue;
                if(proj.getName().startsWith("旧"))continue;
                Map<String,String > project=new HashMap<>();
                project.put("id",proj.getId());
                project.put("name",proj.getName());
                project.put("type","2");


                readLock.lock();
                if(mapExistsInMapList(allBusinessAndProjects,project)){
                    readLock.unlock();
                    continue;
                }
                readLock.unlock();

                writeLock.lock();
                int insertPosition=allBusinessAndProjects.size();
                //String businessId=getBusinessIdByProjectId(proj.getId());
                String businessId=projectIdBusinessIdMap.get(proj.getId());

                if(StringUtils.isEmpty(businessId)){
                    writeLock.unlock();
                    continue;
                }
                for(Map<String,String> map: allBusinessAndProjects){
                    if(map.get("type").equals("1")&&map.get("id").equals(businessId)){
                        insertPosition=allBusinessAndProjects.indexOf(map)+1;
                        break;
                    }
                }
                allBusinessAndProjects.add(insertPosition,project);
                numberOfBusinessAndProjects.getAndIncrement();
                writeLock.unlock();

            }
            logger.info("thread finished........................"+this.getName());

        }

    }

    private Logger logger = LoggerFactory.getLogger(DailyService.class);

    private static final SimpleDateFormat simpleDateFormatyMd = new SimpleDateFormat("yyyy-MM-dd");

    private void setDailyField(Daily daily) { //在创建的时候，判断创建人是不是实习生，是的话，日报参数就是10
        UserEO userEO = UserUtils.getUser();
        if (null != userEO && StringUtils.isNotEmpty(userEO.getExtInfo())) {
            int jobWeight = Integer.valueOf(userEO.getExtInfo());
            if (jobWeight == 10) { //职级权重为 10 的 是实习生
                daily.setDailyField(10);
            }
        }
    }

    // 批量审批（已阅）
    @Transactional
    public List<Daily> approveDailyBatch(List<String> dailyIdList) {
        if(CollectionUtils.isEmpty(dailyIdList)){
            return new ArrayList<>();
        }
        List<Daily> dailyList = dailyRepository.findAllByIdIn(dailyIdList);
        try {
            for (Daily daily : dailyList) {
                daily.setApproveState(1);
                daily.setModifyTime(new Date());
                if(StringUtils.isNotEmpty(daily.getTaskResultFileId())){
                    addTaskResult(daily);
                }
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    List<String> historicProcessInstanceIdList =  actHistoryDao.getHistoricProcessInstanceIdByBusinessKeyIn(dailyIdList);
                    for (String instanceId : historicProcessInstanceIdList){
                        runtimeService.deleteProcessInstance(instanceId,"批量审阅的日报，自动删除！");
                        historyService.deleteHistoricProcessInstance(instanceId);
                    }
                }
            }).start();
        }catch (Exception e){
            throw new AdcDaBaseException("批量审批失败，请检查或联系管理员！");
        }
        dailyRepository.save(dailyList);
        return dailyList;
    }
    private void addTaskResult(Daily daily) throws Exception{

        FileEO fileEO = fileEOService.selectByPrimaryKey(daily.getTaskResultFileId());
        daily.setResultFileName(fileEO.getFileName());
        if (CollectionUtils.isNotEmpty(daily.getTaskIdArray())) {
            for (String taskId:daily.getTaskIdArray()) {
                TaskResultEO taskResultEO = new TaskResultEO();
                taskResultEO.setId(UUID.randomUUID10());
                taskResultEO.setTaskId(taskId);
                taskResultEO.setResultType("日报成果物");
                taskResultEO.setCreateTime(daily.getModifyTime());
                taskResultEO.setResultName(daily.getResultFileName());
                taskResultEOService.insertSelective(taskResultEO);
                if (null != fileEO){
                    fileEO.setForeignId(taskResultEO.getId());
                    fileEOService.updateByPrimaryKeySelective(fileEO);
                }else {
                    throw new AdcDaBaseException("当前日报关联的文件未找到，请联系日报填写者或管理员！");
                }
            }
        }
    }


    public Object queryTask(String id){
        Task task = taskRepository.findById(id);
        if (null != task){
            return task;
        }
        ChildrenTask childrenTask = childTaskRepository.findById(id);
        if (null != childrenTask){
            return  childrenTask;
        }
        throw new AdcDaBaseException("没有查询到任务或子任务");
    }


    /**
     * 保存
     *
     * @param dailyVO
     */
    public Daily save(DailyVO dailyVO, String userId) {
        Daily daily = beanMapper.map(dailyVO, Daily.class);


        daily.setCreateUserId(userId);
        daily.setCreateTime(new Date());
        if (daily.getDailyCreateTime() == null) {
            daily.setDailyCreateTime(new Date());
        }
        setDailyField(daily);
        daily.setId(UUID.randomUUID());

        daily.setDeptId(getDepIdLevelHighest(userId));

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(daily.getDailyCreateTime());
        calendar1.set(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH),
                0, 0, 0);
        Date beginOfDate = calendar1.getTime();
        Date endOfDate = new Date(beginOfDate.getTime() + 86400000);

        //查询当前用户的今日日报条数
        Iterable<Daily> dailyIterable = dailyRepository
                .search(QueryBuilders.boolQuery()
                        .mustNot(QueryBuilders.termQuery(DEL_FLAG, true))
                        .must(QueryBuilders.matchQuery(CREATE_USER_ID, userId))
                        .must(QueryBuilders
                                .rangeQuery("dailyCreateTime")
                                .from(beginOfDate.getTime())
                                .to(endOfDate.getTime())
                                .includeLower(true)     // 包含下界
                                .includeUpper(false)      // 不包含上界
                        ));
        //是否超过2条
        Iterator<Daily> iterator = dailyIterable.iterator();
        int num = 0;
        while (iterator.hasNext()) {
            if (++num > 2) {
                throw new AdcDaBaseException("今日创建日报已达到上线");
            }
            //得到用户创建的日报
            Daily dailyOri = iterator.next();
            //当前日报是否有重复时段
            if (daily.getTimeSlot().equals(dailyOri.getTimeSlot())) {
                throw new AdcDaBaseException("日报时段重复");
            }

        }

        //子任务是否完成
        String[] taskIdArray = daily.getTaskIdArray();
        for (String id : taskIdArray) {
            ChildrenTask childrenTask = childTaskRepository.findOne(id);
            if (null == childrenTask || Boolean.TRUE.equals(childrenTask.getDelFlag())) {
                Task task = taskRepository.findOne(id);
                if (ProjectStatusEnums.COMPLETE.getStatus().equals(task.getTaskStatus())) {
                    throw new AdcDaBaseException(task.getName() + ProjectStatusEnums.COMPLETE.getStatus());
                }
            } else if (ProjectStatusEnums.COMPLETE.getStatus().equals(childrenTask.getStatus())) {
                throw new AdcDaBaseException(
                        childrenTask.getChildTaskName() + ProjectStatusEnums.COMPLETE.getStatus());
            }
        }

        UserEO userEO = UserUtils.getUser();
        daily.setCreateUserName(userEO.getUsname());
        //按照新需求 增加协作人日报
        if ( (daily.getApproveState()==1||daily.getApproveState()==2)&&CollectionUtils.isNotEmpty(daily.getChildrenDailyCreateUserIds())) {
            addAssistDaily(daily);
        }
        dailyRepository.save(daily);
        return daily;

    }

    public List<Daily> updateDailyState(List<DailyVO> dailyVOList) {
        List<Daily> dailyList = new ArrayList<>();
        for (DailyVO dailyVO : dailyVOList) {
            Daily daily = dailyRepository.findOne(dailyVO.getId());
            daily.setApproveState(dailyVO.getApproveState());
            daily.setModifyTime(new Date());
            dailyList.add(daily);
        }
        dailyRepository.save(dailyList);
        return dailyList;
    }

    public List<Daily> addAssistDaily(Daily daily){
        List<Daily> dailyList = new ArrayList<>();
        List<UserEO> userEOList = userEOService.getDao().getUserWithRolesByUserIds(Arrays.asList(daily.getChildrenDailyCreateUserIds()));
        ArrayList<Map<String, String>> userIdDeptNamesMapKv  = CommonUtil.userIdDeptNamesMapKv(userEOList,orgEODao);
        daily.setUserIdDeptNameMapList(userIdDeptNamesMapKv);
        for (UserEO userEO : userEOList){
            Daily childDaily = new Daily();
            BeanUtils.copyPropertiesIgnoreNullValue(daily,childDaily);
            //子日报不同的有 id 状态  创建人  有父id节点
            childDaily.setId(UUID.randomUUID10());
            childDaily.setDailyParentId(daily.getId());
            childDaily.setApproveState(6);
            childDaily.setCreateUserName(userEO.getUsname());
            childDaily.setCreateUserId(userEO.getUsid());
            childDaily.setDailyParentCreateUserId(daily.getCreateUserId());
            childDaily.setDailyParentCreateUserName(daily.getCreateUserName());
            childDaily.setModifyTime(null);
            childDaily.setUserIdDeptNameMapList(null);
            childDaily.setChildrenDailyCreateUserIds(null);
            childDaily.setChildrenDailyCreateUserNames(null);
            childDaily.setTaskResultFileId(null);
            childDaily.setResultFileName(null);
            setESField(childDaily);
            dailyList.add(childDaily);
        }
        dailyRepository.save(dailyList);
        return dailyList;

    }

//    private void updateAssistDaily(Daily newDaily ,Daily oldDaily){
//        List<String> addUserIds = ArrayUtils.compare(newDaily.getChildrenDailyCreateUserIds(),oldDaily.getChildrenDailyCreateUserIds());
//        if (CollectionUtils.isNotEmpty(addUserIds)){
//            da
//        }
//    }

    /**
     * 批量保存日报，同时进行审批校验
     *
     * @param dailyVOList
     * @param userId
     * @return
     */
    public List<Daily> saveList(List<DailyVO> dailyVOList, String userId) throws Exception {

        checkWorkCurrentList(dailyVOList);

        List<Daily> dailyList = new ArrayList<>();
        boolean leaderFlag = false;

        Subject subject = SecurityUtils.getSubject();
        if (subject.hasRole(Role.BEN_BU_ZHANG)
                || subject.hasRole(Role.BU_ZHANG)
                || subject.hasRole(Role.ZHU_REN)
        ) {
            leaderFlag = true;
        }

        String createUserName = null;
        try {
            //UserEO createUserEO = userEOService.selectByPrimaryKey(userId);
            UserEO createUserEO = UserUtils.getUser();
            createUserName = createUserEO.getUsname();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        List<Daily> addedDailyList = new ArrayList<>(); // 用于超时或者异常回滚
        List<String> addedTaskResultId = new ArrayList<>(); // 用于会滚开已插入的taskResult
        try {

            for (DailyVO dailyVO : dailyVOList) {
//                Daily oldDaily = null;
//                if (StringUtils.isNotEmpty(dailyVO.getId())) {
//                    oldDaily = dailyRepository.findOne(dailyVO.getId());
//                }
                Daily daily = new Daily();
                BeanUtils.copyPropertiesIgnoreNullValue(dailyVO, daily);
                daily.setCreateUserId(userId);
                daily.setCreateUserName(createUserName);
                setDailyField(daily);
                if (StringUtils.isEmpty(daily.getId())) {
                    String id = UUID.randomUUID10();
                    daily.setId(id);
                    dailyVO.setId(id);
                    daily.setCreateTime(new Date());
                } else if (daily.getApproveState() != 1) {
                    daily.setCreateTime(new Date());
                }

                daily.setDeptId(getDepIdLevelHighest(userId));

                String dateStr = simpleDateFormatyMd.format(daily.getDailyCreateTime());

                if (checkWorkTimeAll(daily, dailyVOList, userId)) {
                    throw new AdcDaBaseException(dateStr + " 日报要超过24小时了,不要太累哦！");
                }
                if (CollectionUtils.isEmpty(daily.getTaskIdArray())) {
                    throw new AdcDaBaseException(" 项目id为空，请联系管理员！");
                }
                String taskId = daily.getTaskIdArray()[0];
                //
                ChildrenTask childTask = childTaskRepository.findById(taskId);
                if (null != childTask) {
                    addProjectNameAndBudgetName(daily, childTask.getTaskId());
                } else {
                    addProjectNameAndBudgetName(daily, taskId);
                }

                /*
                 *  负责人标识 ，每条日报都是独立的
                 */
                boolean pmFlag = false;
                /*
                 *  进行项目负责人/业务负责人校验 ， 部长及以上跳过校验
                 */
                if (!leaderFlag) {
                    pmFlag = checkLeader(daily, userId);
                }


                /* 更新日报的审批人信息 */
                updateDailyApproveUserInfo(daily);

                //子任务是否完成
                String[] taskIdArray = daily.getTaskIdArray();
                for (String id : taskIdArray) {
                    ChildrenTask childrenTask = childTaskRepository.findOne(id);
                    if (null == childrenTask || Boolean.TRUE.equals(childrenTask.getDelFlag())) {
                        Task task = taskRepository.findOne(id);
                        if (ProjectStatusEnums.COMPLETE.getStatus().equals(task.getTaskStatus())) {
                            throw new AdcDaBaseException(task.getName() + ProjectStatusEnums.COMPLETE.getStatus());
                        }
                    } else {
                        if (ProjectStatusEnums.COMPLETE.getStatus().equals(childrenTask.getStatus())) {
                            throw new AdcDaBaseException(
                                    childrenTask.getChildTaskName() + ProjectStatusEnums.COMPLETE.getStatus());
                        }
                    }
                }
//            UserEO userEO = userEOService.getUserWithRoles(userId);
//            daily.setCreateUserName(userEO.getUsname());
                /*
                 * 部长及以上，或相关负责人不进行日报审批
                 */
                if ((daily.getApproveState() == 2 && leaderFlag)
                        || (daily.getApproveState() == 2 && pmFlag)) {
                    daily.setApproveState(1);
                    daily.setModifyTime(new Date());
                    // 同步日报附件 @see com.adc.da.budget.listener.UpdateDailyApproveState.addTaskResult()
                    if (StringUtils.isNotEmpty(daily.getTaskResultFileId())) {
                        FileEO fileEO = fileEOService.selectByPrimaryKey(daily.getTaskResultFileId());
                        TaskResultEO taskResultEO = new TaskResultEO();
                        taskResultEO.setId(UUID.randomUUID10());
                        taskResultEO.setTaskId(taskId);
                        taskResultEO.setResultType("日报成果物");
                        taskResultEO.setCreateTime(daily.getModifyTime());
                        daily.setResultFileName(fileEO.getFileName());
                        taskResultEO.setResultName(daily.getResultFileName());
                        taskResultEOService.insertSelective(taskResultEO);
                        if (null != fileEO) {
                            fileEO.setForeignId(taskResultEO.getId());
                            fileEOService.updateByPrimaryKeySelective(fileEO);
                        } else {
                            throw new AdcDaBaseException("当前日报关联的文件未找到，请联系日报填写者或管理员！");
                        }
                        addedTaskResultId.add(taskResultEO.getId());
                    }
                    //按照新需求 增加协作人日报
                    if ( (daily.getApproveState()==1||daily.getApproveState()==2)&&CollectionUtils.isNotEmpty(daily.getChildrenDailyCreateUserIds())) {
                        List<Daily> list =  addAssistDaily(daily);
                        addedDailyList.addAll(list);
                    }

                }

                dailyList.add(daily);
                setESField(daily);
                addedDailyList.add(daily);
                dailyRepository.save(daily);
            }
        }catch (Exception e){
            if (CollectionUtils.isNotEmpty(addedDailyList)) {
                dailyRepository.delete(addedDailyList);
            }
            if (CollectionUtils.isNotEmpty(addedTaskResultId)) {
                taskResultEOService.deleteByPrimaryKeys(addedTaskResultId);
            }
            throw e ;
        }

        return dailyList;
    }

    public List<Daily> findByDailyParentIdAndAndApproveStateNotEqual(String parentId , int approveState){

        return Lists.newArrayList(dailyRepository
                .search(QueryBuilders.boolQuery()
                        .mustNot(QueryBuilders.termQuery(DEL_FLAG, true))
                        .must(QueryBuilders.termQuery("dailyParentId", parentId))
                        .mustNot(QueryBuilders.termQuery("approveState", approveState))
                ));
    }

    /**
     * 进行当前用户是否是 任务/项目/业务负责人校验
     *
     * @param daily
     * @param userId
     * @return
     */
    private boolean checkLeader(Daily daily, String userId) {

        /*
         * 进行任务负责人校验
         */
        if (Objects.equals(daily.getApproveUserId(), userId)) {
            return true;
        }
        /*
         *  进行项目任务负责人判断
         */
        String budgetId = daily.getBudgetId();
        if (StringUtils.isNotEmpty(daily.getProjectId())) {
            Project project = projectRepository.findOne(daily.getProjectId());
            if (project != null) {
                if (Objects.equals(userId, project.getProjectLeaderId())) {
                    //当前用户是项目负责人
                    return true;

                } else {
                    //不是项目负责人，可能是业务负责人
                    budgetId = project.getBudgetId();
                }
            }
        }
        /*
         *  业务负责人校验
         */
        if (StringUtils.isNotEmpty(budgetId)) {
            try {
                BudgetEO budgetEO = budgetEOService.selectByPrimaryKey(budgetId);
                if (budgetEO != null && Objects.equals(userId, budgetEO.getPm())) {
                    return true;
                }
            } catch (Exception e) {
                throw new AdcDaBaseException(e.getMessage());
            }
        }
        /*
         *  不是 任务/项目/业务负责人
         */
        return false;
    }

    /**
     * 更新审批人信息，保存 和 流程中均有调用
     *
     * @param daily
     * @see #saveList
     * @see com.adc.da.budget.listener.UpdateDailyApproveState
     */
    public void updateDailyApproveUserInfo(Daily daily) {
        try {
            String leaderId;
            if (StringUtils.isNotEmpty(daily.getProjectId())) {
                Project project = projectRepository.findById(daily.getProjectId());
                leaderId = project.getProjectLeaderId();
            } else if (StringUtils.isNotEmpty(daily.getBudgetId())) {
                BudgetEO budgetEO = budgetEOService.selectByPrimaryKey(daily.getBudgetId());
                leaderId = budgetEO.getPm();
            } else {
                throw new AdcDaBaseException(" 项目id为空，请联系管理员！");
            }
            //追加任务负责人校验
            String approveUserId;
            String[] taskIdArray = daily.getTaskIdArray();
            if (taskIdArray != null && StringUtils.isNotEmpty(taskIdArray[0])) {
                String taskId = taskIdArray[0];
                //可能是下级任务
                ChildrenTask childrenTask = childTaskRepository.findOne(taskId);
                if (childrenTask != null) {
                    taskId = childrenTask.getTaskId();
                }
                //查询对应的任务信息
                Task task = taskRepository.findOne(taskId);
                if (task != null && StringUtils.isNotEmpty(task.getApproveUserId())) {
                    approveUserId = task.getApproveUserId();
                } else {
                    //子任务
                    approveUserId = leaderId;
                }
            } else {
                approveUserId = leaderId;
            }

            daily.setProjectLeaderId(leaderId);
            daily.setApproveUserId(approveUserId);
            UserEO userEO = userEOService.selectByPrimaryKey(approveUserId);
            daily.setApproveUserName(userEO.getUsname());

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private void addProjectNameAndBudgetName(Daily daily, String taskId) {

        try {

            Task task = taskRepository.findById(taskId);
            if (StringUtils.isNotEmpty(task.getProjectId())) {
                Project project = projectRepository.findById(task.getProjectId());
                daily.setProjectName(project.getName());
                BudgetEO budgetEO = budgetEOService.selectByPrimaryKey(project.getBudgetId());
                daily.setBudgetName(budgetEO.getProjectName());
                daily.setBudgetId(budgetEO.getId());
                daily.setProjectId(project.getId());
            } else {
                BudgetEO budgetEO = budgetEOService.selectByPrimaryKey(task.getBudgetId());
                daily.setBudgetName(budgetEO.getProjectName());
                daily.setBudgetId(budgetEO.getId());
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

    }

    private Boolean checkWorkCurrentList(List<DailyVO> dailyVOList) {
        for (DailyVO dailyVO1 : dailyVOList) {
            int hasTwoSlot = 0;
            double workTimeCost = 0;
            for (DailyVO dailyVO2 : dailyVOList) {
                if (this.isSameDay(dailyVO1.getDailyCreateTime(), dailyVO2.getDailyCreateTime())) {

                    if (null != dailyVO2.getTimeSlot()) {
                        hasTwoSlot++;
                        workTimeCost = workTimeCost + 4;
                    } else if (null != dailyVO2.getWorkCostTime()) {
                        workTimeCost = workTimeCost + dailyVO2.getWorkCostTime();
                    }
                }
            }
            if (hasTwoSlot > 2) {
                String dateStr = simpleDateFormatyMd.format(dailyVO1.getDailyCreateTime());
                throw new AdcDaBaseException(dateStr + " 日报上下午都安排满了,不要太累哦！");
            }

            //当前日报是否有重复时段
            float totalWorkTimeHours=Math.round(workTimeCost*10)/10.0f ;
            if (totalWorkTimeHours > 24) {

                String dateStr = simpleDateFormatyMd.format(dailyVO1.getDailyCreateTime());
                throw new AdcDaBaseException(dateStr + " 日报已经超过24小时了,不要太累哦！");
            }
        }
        return false;
    }

    private boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        boolean isSameYear = cal1.get(Calendar.YEAR) == cal2
                .get(Calendar.YEAR);
        boolean isSameMonth = isSameYear
                && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
        boolean isSameDate = isSameMonth
                && cal1.get(Calendar.DAY_OF_MONTH) == cal2
                .get(Calendar.DAY_OF_MONTH);

        return isSameDate;
    }

    private boolean checkWorkTimeAll(Daily daily, List<DailyVO> dailyVOList, String userId) {
        Set<String> dailyIdSet = new HashSet<>();

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(daily.getDailyCreateTime());
        calendar1.set(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH),
                0, 0, 0);
        Date beginOfDate = calendar1.getTime();
        Date endOfDate = new Date(beginOfDate.getTime() + 86400000);

        //查询当前用户的今日日报条数
        Iterable<Daily> dailyIterable = dailyRepository
                .search(QueryBuilders.boolQuery()
                        .mustNot(QueryBuilders.termQuery(DEL_FLAG, true))
                        .mustNot(QueryBuilders.matchQuery("id", daily.getId()))
                        .must(QueryBuilders.matchQuery(CREATE_USER_ID, userId))
                        .must(QueryBuilders
                                .rangeQuery("dailyCreateTime")
                                .from(beginOfDate.getTime())
                                .to(endOfDate.getTime())
                                .includeLower(true)     // 包含下界
                                .includeUpper(false)      // 不包含上界
                        ));

        //计算当前日期所有日报的时间总和
        Iterator<Daily> iterator = dailyIterable.iterator();
        float workTimeCost = 0.0f;
        int hasTwoSlot = 0;
        while (iterator.hasNext()) {

            //得到用户创建的日报
            Daily dailyOri = iterator.next();
            if (!StringUtils.equals(daily.getId(), dailyOri.getId()) && dailyIdSet.add(dailyOri.getId())) {
                if (null != dailyOri.getTimeSlot()) {
                    hasTwoSlot++;
                    workTimeCost = workTimeCost + 4;
                } else if (null != dailyOri.getWorkCostTime()) {
                    workTimeCost = workTimeCost + dailyOri.getWorkCostTime();
                }
            }
        }

        for (DailyVO dailyVO : dailyVOList) {
            if (StringUtils.equals(daily.getId(), dailyVO.getId()) || !dailyIdSet.add(dailyVO.getId()) || (
                    dailyVO.getDailyCreateTime().getTime() < beginOfDate.getTime()
                            || dailyVO.getDailyCreateTime().getTime() >= endOfDate.getTime())
            ) {
                continue;
            } else {
                if (null != dailyVO.getTimeSlot()) {
                    hasTwoSlot++;
                    workTimeCost = workTimeCost + 4;
                } else if (null != dailyVO.getWorkCostTime()) {
                    workTimeCost = workTimeCost + dailyVO.getWorkCostTime();
                }
            }

        }

        // 已至少填了上午和下午、晚上其中两个的，
        if (hasTwoSlot >= 2) {
            return true;
        }


        //当前日报是否有重复时段
        if (dailyIdSet.add(daily.getId())) {
            float totalWorkTimeHours=workTimeCost + daily.getWorkCostTime();
            totalWorkTimeHours=Math.round(totalWorkTimeHours*10)/10.0f ;
            if(totalWorkTimeHours > 24.0){
                return true;
            }

        } else {
            float totalWorkTimeHours=workTimeCost ;
            totalWorkTimeHours=Math.round(totalWorkTimeHours*10)/10.0f ;
            if(totalWorkTimeHours > 24.0){
                return true;
            }
        }
        return false;
    }

    private String getDepIdLevelHighest(String userId) {
        /*
         * 返回的结果集，为根据组织树降序排列(Oracle 递归函数的Level 字段)，若用户同属本部与部门，则第一条记录为部门的信息，根节点的Level为1
         */
        List<OrgWithLevelEO> orgEOList = orgListDao.getUserOrgWhitLeafAndLev(userId);
        if (CollectionUtils.isNotEmpty(orgEOList)) {
            return orgEOList.get(0).getId();
        } else {
            throw new AdcDaBaseException("日报-获取部门信息失败");

        }
    }

    /**
     * 修改
     *
     * @param dailyVO
     * @return
     */
    public Daily update(DailyVO dailyVO) {

        Daily dailyNew = beanMapper.map(dailyVO, Daily.class);
        dailyNew.setModifyTime(new Date());
        // 查询
        Daily dailyOld = dailyRepository.findOne(dailyNew.getId());

        /*
         * 更新组织机构信息
         */
        dailyNew.setDeptId(getDepIdLevelHighest(dailyOld.getCreateUserId()));

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(dailyNew.getDailyCreateTime());
        calendar1.set(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH),
                0, 0, 0);
        Date beginOfDate = calendar1.getTime();
        Date endOfDate = new Date(beginOfDate.getTime() + 86400000);
        //查询当前用户的今日日报条数
        Iterable<Daily> dailyIterable = dailyRepository.search(
                QueryBuilders.boolQuery()
                        .mustNot(QueryBuilders
                                .termQuery(DEL_FLAG, true))
                        .must(QueryBuilders.matchQuery(CREATE_USER_ID, dailyOld.getCreateUserId()))
                        .must(QueryBuilders.rangeQuery("dailyCreateTime")
                                .from(beginOfDate.getTime())
                                .to(endOfDate.getTime())
                                .includeLower(true)
                                // 包含下界,不包含上界
                                .includeUpper(false))
                        .mustNot(QueryBuilders.termQuery("id", dailyNew.getId())));
        //是否超过2条
        Iterator<Daily> iterator = dailyIterable.iterator();
        int num = 0;
        while (iterator.hasNext()) {
            if (++num > 2) {
                dailyRepository.save(dailyOld);
                throw new AdcDaBaseException("今日创建日报已达到上线");
            }
            //得到用户创建的日报
            Daily dailyOri = iterator.next();

            //当前日报是否有重复时段
            if (dailyNew.getTimeSlot().equals(dailyOri.getTimeSlot())) {
                throw new AdcDaBaseException("日报时段重复");
            }

        }

        //子任务是否完成
        String[] taskIdArray = dailyNew.getTaskIdArray();
        for (String id : taskIdArray) {
            ChildrenTask childrenTask = childTaskRepository.findOne(id);
            if (null == childrenTask || Boolean.TRUE.equals(childrenTask.getDelFlag())) {
                Task task = taskRepository.findOne(id);
                if (ProjectStatusEnums.COMPLETE.getStatus().equals(task.getTaskStatus())) {
                    throw new AdcDaBaseException(task.getName() + ProjectStatusEnums.COMPLETE.getStatus());
                }
            } else {
                if (ProjectStatusEnums.COMPLETE.getStatus().equals(childrenTask.getStatus())) {
                    throw new AdcDaBaseException(
                            childrenTask.getChildTaskName() + ProjectStatusEnums.COMPLETE.getStatus());
                }
            }
        }

        BeanUtils.copyPropertiesIgnoreNullValue(dailyNew, dailyOld);

        return dailyRepository.save(dailyOld);

    }

    /**
     * 更新全部记录
     *
     * @param dailyIterable
     * @return
     * @author Lee Kwanho 李坤澔
     *     date 2019-06-01
     **/
    public void saveAll(Iterable<Daily> dailyIterable) {
        List<Daily> dailyList = new ArrayList<>();

        for (Daily dailyNew : dailyIterable) {
            if (dailyNew.getApproveState() == null || dailyNew.getApproveState() == 0) {
                dailyNew.setApproveState(1);
                dailyList.add(dailyNew);
            }
        }
        dailyRepository.save(dailyList);

    }

    public void moveAll() {
        BoolQueryBuilder queryBuilder = new BoolQueryBuilder();
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        int size = 1000;
        int page = 1;
        while (true) {
            Page<Daily> queryPage = dailyRepository.search(queryBuilder, new PageRequest(page - 1, size, sort));
            page++;
            List<Daily> queryList =
                    (queryPage == null || (queryList = queryPage.getContent()) == null) ? new ArrayList<Daily>() :
                            queryList;
            if (CollectionUtils.isEmpty(queryList)) {
                logger.info("------------------------------>更新完毕");
                break;
            }
            setESField(queryList);

            dailyRepository.save(queryList);

        }

    }

    /**
     * 进行es查询
     *
     * @return
     * @author Lee Kwanho 李坤澔
     *     date 2019-06-01
     **/
    public Iterable<Daily> queryBuilds() {
        BoolQueryBuilder queryBuilder = new BoolQueryBuilder();

        /*
         * 拼接部门条件，不存在dailyCreateTime字段的记录
         */
        return dailyRepository.search(queryBuilder);

    }

    /**
     * 删除
     *
     * @param ids
     */
    public String delete(String ids) {
        if (StringUtils.isBlank(ids)) {
            throw new AdcDaBaseException("请选择要删除的日报！");
        }
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        String[] idArray = ids.split(",");
        for (int i = 0; i < idArray.length; i++) {
            Daily daily = dailyRepository.findOne(idArray[i]);
            //如果包含子日报
            if (CollectionUtils.isNotEmpty(daily.getChildrenDailyCreateUserIds())){
                List<Daily> childDailyList = findByDailyParentIdAndAndApproveStateNotEqual(daily.getId(), 6);
                if (CollectionUtils.isNotEmpty(childDailyList)) {
                    //哪些用户认领了
                    List<String> claimUserNameList = new ArrayList<>();
                    for (Daily d : childDailyList) {
                        claimUserNameList.add(d.getCreateUserName());
                    }
                    throw new AdcDaBaseException(daily.getTaskName()+"任务的日报" + StringUtils.join(claimUserNameList, "、") + "已确认，不可进行编辑/删除。");
                } else {
                    dailyRepository.deleteByDailyParentId(daily.getId());
                }
            }
            if (!StringUtils.equals(userId, daily.getCreateUserId())) {
                throw new AdcDaBaseException("只能删除自己的日报！");
            }
            if(BooleanUtils.isTrue(daily.getDelFlag())){ // 已经是true的就不再重新删除了，减少数据库访问
                continue;
            }
            daily.setDelFlag(true);
            dailyRepository.save(daily);
        }
        return "删除成功";
    }

    public void deleteAll() {
        dailyRepository.deleteAll();
    }

    /**
     * 查询全部
     *
     * @return
     */
    public List<Daily> findAll() {
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

        //如果当前用户是部长可以看见本部门所有任务
        Subject subject = SecurityUtils.getSubject();
        if (subject.hasRole(Role.SUPER_ADMIN) || subject.hasRole(Role.ADMIN) || subject.hasRole(Role.ZHU_REN)) {
            ArrayList<Daily> dailies = Lists.newArrayList(dailyRepository.findAllByDelFlagNot(true));
            for (Daily daily : dailies) {
                if (StringUtils.isBlank(daily.getCreateUserName())) {
                    String usname = userEOService.getUserWithRoles(daily.getCreateUserId()).getUsname();
                    daily.setCreateUserName(usname);
                }
                dailyRepository.save(daily);
            }
        }
        if (subject.hasRole(Role.BU_ZHANG)) {
            BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
            BoolQueryBuilder qb = QueryBuilders.boolQuery();
            for (String deptId : deptList) {
                qb.should(QueryBuilders.termQuery("deptId", deptId));
            }
            queryBuilder.must(qb);
            //匹配非删除的数据
            queryBuilder.mustNot(QueryBuilders.termQuery(DEL_FLAG, true));
            //当前部门下的所有任务任务
            return Lists.newArrayList(dailyRepository.search(queryBuilder));
        }

        //不是部长的情况
        List<String> projectIds = getCurrentUserProjectIds();
        DailyConditionDTO dailyConditionDTO = new DailyConditionDTO();
        dailyConditionDTO.setProjectIds(projectIds);
        Set<String> childrenTaskIds = getChildrenTaskIdsByCondition(dailyConditionDTO);
        //查询日报信息
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        query.should(QueryBuilders.termsQuery(CREATE_USER_ID, userId));
        //如果子任务id为空，则查询全部
        if (StringUtils.isNotEmpty(childrenTaskIds)) {
            query.should(QueryBuilders.termsQuery("taskIdArray", childrenTaskIds));
        }
        boolQueryBuilder.must(query);
        boolQueryBuilder.mustNot(QueryBuilders.termQuery(DEL_FLAG, true));
        return Lists.newArrayList(dailyRepository.search(query));
    }

    /**
     * 单个查询
     *
     * @param id
     * @return
     */
    public Daily findOne(String id) {
        return dailyRepository.findFirstByIdAndDelFlagNot(id, true);
    }

    /**
     * 导入
     */

    public void excelImport(InputStream is, ImportParams params) throws Exception {
        List<Daily> datas = ExcelImportUtil.importExcel(is, Daily.class, params);
        //导入
        for (Daily daily : datas) {
            //处理任务ID列表
            String[] taskIdArray = daily.getTaskIds().split("，");
            daily.setTaskIdArray(taskIdArray);
            String[] taskStatusArray = daily.getTaskStatus().split("，");
            daily.setTaskStatusArray(taskStatusArray);
            String[] workTimeArray = daily.getWorkTime().split("，");

            float[] workTimeArrayInteger = new float[workTimeArray.length];
            for (int i = 0; i < workTimeArray.length; i++) {
                if (workTimeArray[i] == null || StringUtils.isEmpty(workTimeArray[i])) {
                    continue;
                }
                workTimeArrayInteger[i] = Float.parseFloat(workTimeArray[i]);
            }
            daily.setWorktimeArray(workTimeArrayInteger);
            if (StringUtils.isEmpty(daily.getId())) {
                daily.setId(UUID.randomUUID());
            }
            if (StringUtils.isEmpty(daily.getCreateTime())) {
                daily.setCreateTime(new Date(System.currentTimeMillis()));
            }

            UserEPEntity userEPEntity = userEPDao.queryUserByName(daily.getCreateUserId());
            if (null != userEPEntity) {
                daily.setCreateUserId(userEPEntity.getUsid());
            }
            dailyRepository.save(daily);
        }
    }
    /**
     * 导出
     *
     * @param params
     * @return
     */
    public Workbook excelExport(ExportParams params) {
        List<Daily> dailies = findAll();
        Collections.sort(dailies, new Comparator<Daily>() {
            @Override
            public int compare(Daily o1, Daily o2) {
                return (int) (o1.getDailyCreateTime().getTime() - o2.getDailyCreateTime().getTime());

            }
        });
        for (Daily daily : dailies) {
            switch (daily.getDailyType()) {
                case "0":
                    daily.setDailyType("工作");
                    break;
                case "1":
                    daily.setDailyType("出差");
                    break;
                case "2":
                    daily.setDailyType("会议");
                    break;
                case "3":
                    daily.setDailyType("培训");
                    break;
                default:
                    daily.setDailyType("其他");
            }
        }
        return ExcelExportUtil.exportExcel(params, Daily.class, dailies);
    }

    /**
     * @Description: 查询子任务
     * @auther: ZHAIKAIXUAN
     * @params: 人员ID
     * @return:
     * @date: 2018/11/27 10:21
     */
    public List<Map<String, String>> queryChildrenDaily(String id, String projectId) {
        if ((null == id || StringUtils.isEmpty(id.trim()))
                || (null == projectId || StringUtils.isEmpty(projectId.trim()))) {
            return new ArrayList<>();
        }
        List<ChildrenTask> childrenTasks;

        List<Task> taskList = Lists.newArrayList(taskRepository.findAll());
        List<Task> tasks = new ArrayList<>();
        for (Task task : taskList) {
            if (projectId.equals(task.getProjectId())) {
                tasks.add(task);
            }
        }
        // 获取所有任务下的所有子任务
        childrenTasks = new ArrayList<>();
        for (Task task : tasks) {
            List<ChildrenTask> childrenTasksList = Lists.newArrayList(childTaskRepository.findAll());
            for (ChildrenTask childrenTask : childrenTasksList) {

                if (null != childrenTask.getTaskId() && childrenTask.getTaskId().equals(task.getId())) {
                    childrenTasks.add(childrenTask);
                }

            }
        }
        List<Map<String, String>> childrenMapList = new ArrayList<>();
        SimpleDateFormat yMDHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (ChildrenTask childrenTask : childrenTasks) {
            if ( (null != childrenTask.getPersonId() && childrenTask.getPersonId().equals(id)||
                    CommonUtil.arrayContains(childrenTask.getMemberIds(),id) > -1)
                    && ("未完成").equals(childrenTask.getStatus())) {
                Map<String, String> map = new HashMap<>();
                // 根据任务ID获`取任务
                Task task = new Task();
                Project project = new Project();
                if (childrenTask.getTaskId() != null) {
                    task = taskRepository.findOne(childrenTask.getTaskId());
                    // 根据项目Id获取项目
                    if (null == task || task.getProjectId() == null || !projectId.equals(task.getProjectId())) {
                        continue;
                    }

                    if (null != task.getProjectId()) {
                        project = projectRepository.findOne(task.getProjectId());
                    }

                }

                map.put("Id", childrenTask.getId());
                map.put("childrenName", childrenTask.getChildTaskName());
                map.put("planStart", yMDHMS.format(childrenTask.getPlanStartTime()));
                map.put("planEnd", yMDHMS.format(childrenTask.getPlanEndTime()));
                map.put("taskName", task.getName());

                map.put("projectName", project.getName());
                childrenMapList.add(map);
            }

        }

        return childrenMapList;
    }

    /**
     * 分页查询
     */
    public PageDTO newQueryByPage(int page, int size) {
        //当前用户的id
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        Subject subject = SecurityUtils.getSubject();
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.mustNot(QueryBuilders.termQuery(DEL_FLAG, true))
                .must(QueryBuilders.existsQuery("workCostTime"));

        if (subject.hasRole(Role.SUPER_ADMIN) || subject.hasRole(Role.ADMIN)) {
            //管理员，日报看到所有记录
            return getPageDTO(queryBuilder, page, size);
        } else if (subject.hasRole(Role.BEN_BU_ZHANG) || subject.hasRole(Role.BU_ZHANG)
                || subject.hasRole(Role.ZHU_REN)) {
            //本部长，部长能看到部门及下属组的，主任能看到中心级别及下属
            BoolQueryBuilder qb = QueryBuilders.boolQuery();
            List<String> orgIds = getOrgIds(userId);

            for (String deptId : orgIds) {
                qb.should(QueryBuilders.termQuery("deptId", deptId));
            }
            queryBuilder.must(qb);
            return getPageDTO(queryBuilder, page, size);
        } else if (subject.hasRole(Role.ZU_ZHANG) || subject.hasRole(Role.PROJECT_SENIOR_MANAGER)) {

            return newSetDailyData(userId, page, size);
        }
        queryBuilder.must(QueryBuilders.matchQuery(CREATE_USER_ID, userId));
        return getPageDTO(queryBuilder, page, size);
    }

    @Override
    public PageDTO queryByPage(int page, int size) {
        //当前用户的id
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        Subject subject = SecurityUtils.getSubject();
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.mustNot(QueryBuilders.termQuery(DEL_FLAG, true));

        if (subject.hasRole(Role.SUPER_ADMIN) || subject.hasRole(Role.ADMIN)) {
            //管理员，日报看到所有记录
            return getPageDTO(queryBuilder, page, size);
        } else if (subject.hasRole(Role.BEN_BU_ZHANG) || subject.hasRole(Role.BU_ZHANG)
                || subject.hasRole(Role.ZHU_REN)) {
            //本部长，部长能看到部门及下属组的，主任能看到中心级别及下属
            BoolQueryBuilder innerQueryBuilder = QueryBuilders.boolQuery();
            List<String> orgIds = getOrgIds(userId);
            innerQueryBuilder.should(QueryBuilders.termsQuery("deptId", orgIds));
            queryBuilder.must(innerQueryBuilder);
            return getPageDTO(queryBuilder, page, size);
        } else if (subject.hasRole(Role.ZU_ZHANG) || subject.hasRole(Role.PROJECT_SENIOR_MANAGER)) {

            return setDailyData(userId, page, size);
        }
        queryBuilder.must(QueryBuilders.matchQuery(CREATE_USER_ID, userId));
        return getPageDTO(queryBuilder, page, size);
    }

    public PageDTO queryLoginUserDailyByPage(int page, int size) {
        //当前用户的id
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.mustNot(QueryBuilders.termQuery(DEL_FLAG, true));

        queryBuilder.must(QueryBuilders.termQuery(CREATE_USER_ID, userId));
        queryBuilder.must(QueryBuilders.existsQuery("workCostTime"));
        return getPageDTO(queryBuilder, page, size);
    }

    /**
     * 分页查询
     */
    public PageDTO queryByPage4Tips(DailyPage dailyPage) {
        //当前用户的id
       String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.mustNot(QueryBuilders.termQuery(DEL_FLAG, true));
        int[] state = {1, 2};
        if (null != dailyPage.getApproveState()) {
            state = new int[]{dailyPage.getApproveState()};
        }
        queryBuilder.must(QueryBuilders.termsQuery("approveState", state))
            .must(QueryBuilders.existsQuery("workCostTime"));

        if (!StringUtils.isEmpty(dailyPage.getCreateUserName())) {
            queryBuilder.must(
                    QueryBuilders.wildcardQuery("createUserNameES", "*" + dailyPage.getCreateUserName() + "*")
            );
        }

        if (!StringUtils.isEmpty(dailyPage.getDailyCreateTime())) {
            queryBuilder.must(
                    QueryBuilders.rangeQuery("dailyCreateTime").from(dailyPage.getDailyCreateTime().getTime())
                            .to(dailyPage.getDailyCreateTime().getTime() + 1000 * 3600 * 24)
            );
        }

        if (!StringUtils.isEmpty(dailyPage.getBudgetName())) {
            queryBuilder.must(
                    QueryBuilders.wildcardQuery("budgetNameES", "*" + dailyPage.getBudgetName() + "*")
            );

        }

        if (!StringUtils.isEmpty(dailyPage.getProjectName())) {
            queryBuilder.must(
                    QueryBuilders.wildcardQuery("projectNameES", "*" + dailyPage.getProjectName() + "*")
            );

        }

        if (!StringUtils.isEmpty(dailyPage.getTaskName())) {
            queryBuilder.must(
                    QueryBuilders.wildcardQuery("taskNameES", "*" + dailyPage.getTaskName() + "*")
            );
        }
        if (null != dailyPage.getQueryStartDate() && null != dailyPage.getQueryEndDate()) {
            queryBuilder.must(
                    QueryBuilders.rangeQuery("modifyTime")
                            .from(dailyPage.getQueryStartDate().getTime())
                            .to(dailyPage.getQueryEndDate().getTime() + 3600 * 24 * 1000)
                            .includeLower(true)
                            .includeUpper(true)
            );

        }

        if (null != dailyPage.getApproveState()) {
            queryBuilder.must(QueryBuilders.termQuery("approveState", dailyPage.getApproveState()));

        }

        if (!StringUtils.isEmpty(dailyPage.getTimeSlot())) {
            queryBuilder.must(QueryBuilders.termQuery("timeSlot", dailyPage.getTimeSlot()));

        }
        if (!StringUtils.isEmpty(dailyPage.getWorkDescription())) {
            queryBuilder
                    .must(QueryBuilders.wildcardQuery("workDescription", "*" + dailyPage.getWorkDescription() + "*"));

        }
        if (!StringUtils.isEmpty(dailyPage.getDailyType())) {
            queryBuilder.must(QueryBuilders.termQuery("dailyType", dailyPage.getDailyType()));

        }

        queryBuilder.must(QueryBuilders.matchQuery("approveUserId", userId));
        return getPageDTO(queryBuilder, dailyPage.getPage(), dailyPage.getPageSize());
    }
    /**
     * 分页查询
     */
    public PageDTO queryByPage4TipsByType(DailyPage dailyPage) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.mustNot(QueryBuilders.termQuery(DEL_FLAG, true));
        queryBuilder.must(QueryBuilders.existsQuery("approveUserId"));
        int[] state = { 1 };
        queryBuilder.must(QueryBuilders.termsQuery("approveState", state));

        List<Project> projectList = new ArrayList<>() ;
        List<Task> taskList =new ArrayList<>() ;
        List<ChildrenTask> childrenTaskList = new ArrayList<>() ;
        Set<String> projectIdSet = new HashSet<>();
        Set<String> taskIdSet =  new HashSet<>();

        if (StringUtils.isNotEmpty(dailyPage.getBudgetId())) { //如果是根据业务查询或导出 要查出 业务的项目 业务任务
            projectList = projectRepository.findByBudgetId(dailyPage.getBudgetId());
            taskList = new ArrayList<>(taskRepository.findByBudgetId(dailyPage.getBudgetId()));
        }

        if (StringUtils.isNotEmpty(dailyPage.getProjectId())) { //如果是根据项目 要查出 项目任务
            taskList = new ArrayList<>(taskRepository.findByProjectId(dailyPage.getProjectId()));
        }

        if (StringUtils.isNotEmpty(dailyPage.getTaskId())) {//如果是根据任务 要查出子任务
            childrenTaskList = new ArrayList<>(childTaskRepository.findByTaskId(dailyPage.getTaskId()));
            taskIdSet.add(dailyPage.getTaskId());
        }

        for (Project project : projectList){
            projectIdSet.add(project.getId());
        }

        if(CollectionUtils.isNotEmpty(projectIdSet)){
            if (CollectionUtils.isNotEmpty(taskList)){
                List<Task> tempList = taskRepository.findByProjectIdInAndDelFlagNot(projectIdSet,true);
                taskList.addAll(new ArrayList<>(tempList));
            }else {
                taskList = taskRepository.findByProjectIdInAndDelFlagNot(projectIdSet,true) ;
            }
        }
        for (Task task : taskList){
            taskIdSet.add(task.getId());
        }

        if(CollectionUtils.isNotEmpty(taskIdSet)){
            if (CollectionUtils.isNotEmpty(childrenTaskList)){
                List<ChildrenTask> tempList = childTaskRepository.findByTaskIdInAndDelFlagNot(taskIdSet,true);
                childrenTaskList.addAll(new ArrayList<>(tempList));
            }else {
                childrenTaskList = childTaskRepository.findByTaskIdInAndDelFlagNot(taskIdSet,true) ;
            }
        }

        for (ChildrenTask childrenTask : childrenTaskList){
            taskIdSet.add(childrenTask.getId());
        }


        //查询的全集落脚点是 所有的任务子任务id
        if (CollectionUtils.isNotEmpty(taskIdSet)){
            queryBuilder.must(
                    QueryBuilders.termsQuery("taskIdArray", taskIdSet)
            );
        } else {
            //防止查找失效，在没有任务子任务的项目下 会导致没有条件,查出了所有的日报
            taskIdSet.add("-----");
            queryBuilder.must(
                    QueryBuilders.termsQuery("taskIdArray", taskIdSet)
            );
        }



        if (!StringUtils.isEmpty(dailyPage.getCreateUserName())) {
            queryBuilder.must(
                    QueryBuilders.wildcardQuery("createUserNameES", "*" + dailyPage.getCreateUserName() + "*")
            );
        }


        if (!StringUtils.isEmpty(dailyPage.getBudgetName())) {
            queryBuilder.must(
                    QueryBuilders.wildcardQuery("budgetNameES", "*" + dailyPage.getBudgetName() + "*")
            );
        }


        if (!StringUtils.isEmpty(dailyPage.getProjectName())) {
            queryBuilder.must(
                    QueryBuilders.wildcardQuery("projectNameES", "*" + dailyPage.getProjectName() + "*")
            );

        }

        if (!StringUtils.isEmpty(dailyPage.getTaskName())) {
            queryBuilder.must(
                    QueryBuilders.wildcardQuery("taskNameES", "*" + dailyPage.getTaskName() + "*")
            );
        }

        if (null != dailyPage.getQueryStartDate() && null != dailyPage.getQueryEndDate()) {
            queryBuilder.must(
                    QueryBuilders.rangeQuery("dailyCreateTime")
                            .from(dailyPage.getQueryStartDate().getTime())
                            .to(dailyPage.getQueryEndDate().getTime() + 3600 * 24 * 1000)
                            .includeLower(true)
                            .includeUpper(true)
            );

        }
        PageDTO pageDTO = getPageDTO(queryBuilder, dailyPage.getPage(), dailyPage.getPageSize());
//        List<Daily> dailyList = new ArrayList<>(pageDTO.getDataList());
//        if (CollectionUtils.isNotEmpty(dailyList)) {
//            Collections.sort(dailyList, new Comparator<Daily>() {
//                public int compare(Daily o1, Daily o2) {
//                    Date a = o1.getDailyCreateTime();
//                    Date b = o2.getDailyCreateTime();
//                    if (a.compareTo(b) < 0) {
//                        return 1;
//                    } else if (a.compareTo(b) == 0) {
//                        return 0;
//                    } else
//                        return -1;
//                }
//            });
//        }
//        pageDTO.setDataList(dailyList);
        return pageDTO;
    }

    public Workbook exportExcelByPage4TipsByType(DailyPage dailyPage,ExportParams params) {
        dailyPage.setPage(1);
        dailyPage.setPageSize(10000-1);
        List<Daily> dailyList = queryByPage4TipsByType(dailyPage).getDataList();
        List<DailyExportVO> dailyExportVOList = beanMapper.mapList(dailyList, DailyExportVO.class);
        return ExcelExportUtil.exportExcel(params, DailyExportVO.class, dailyExportVOList);

    }



    /**
     * 获取组织
     *
     * @param userId 用户id
     * @return 组织id list
     * @author Lee Kwanho 李坤澔
     *     date 2019-05-29
     */
    private List<String> getOrgIds(String userId) {
        List<String> orgIds = new ArrayList<>();
        /*
         * 原则上只用取最大的部门id即可，但是防止出现跨部门兼任的情况需要对每个组织id进行循环
         */
        List<OrgEO> orgEOList = userEOService.getUserWithRoles(userId).getOrgEOList();

        for (OrgEO orgEO : orgEOList) {
            //获取org本身及子节点
            orgIds.addAll(orgListDao.getOrgAndSubOrgIds(orgEO.getId()));
        }
        /*
         * 利用TreeSet对orgIds去重
         */
        TreeSet<String> treeSet = new TreeSet<>(orgIds);

        return new ArrayList<>(treeSet);
    }

    private PageDTO newSetDailyData(String userId, int page, int size) {
        UserEO userWithRoles = userEOService.getUserWithRoles(userId);
        List<OrgEO> orgEOList = userWithRoles.getOrgEOList();
        List<String> ids = new ArrayList<>();
        String ordId = "";
        for (OrgEO orgEO : orgEOList) {
            if (orgEO.getName().contains("组")) {
                ordId = orgEO.getId();
            }
        }
        List<OrgEO> orgEOByPid = orgEODao.getOrgEOByPid(ordId);
        if (CollectionUtils.isNotEmpty(orgEOByPid)) {
            for (OrgEO orgEO : orgEOByPid) {
                ids.add(orgEO.getId());
            }
        }
        ids.add(ordId);
        List<UserEO> userEOS = userDao.selectByOrgIdAndUsName(ids, null);

        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

        List<String> userIdList = new ArrayList<>();
        for (UserEO userEO : userEOS) {
            if (!userEO.getUsid().equals(userId)) {
                userIdList.add(userEO.getUsid());
            }
        }
        queryBuilder.should(
                QueryBuilders.boolQuery().must(QueryBuilders.termsQuery(CREATE_USER_ID, userIdList))
                        .must(QueryBuilders.termQuery("approveState", 1))
                        .mustNot(QueryBuilders.termQuery(DEL_FLAG, Boolean.TRUE))
        );

        queryBuilder.should(
                QueryBuilders.boolQuery().must(QueryBuilders.termQuery(CREATE_USER_ID, userId))
                        .must(QueryBuilders.existsQuery("workCostTime"))
        );

        return getPageDTO(queryBuilder, page, size);

    }

    private PageDTO setDailyData(String userId, int page, int size) {
        UserEO userWithRoles = userEOService.getUserWithRoles(userId);
        List<OrgEO> orgEOList = userWithRoles.getOrgEOList();
        List<String> ids = new ArrayList<>();
        String ordId = "";
        for (OrgEO orgEO : orgEOList) {
            if (orgEO.getName().contains("组")) {
                ordId = orgEO.getId();
            }
        }
        List<OrgEO> orgEOByPid = orgEODao.getOrgEOByPid(ordId);
        if (CollectionUtils.isNotEmpty(orgEOByPid)) {
            for (OrgEO orgEO : orgEOByPid) {
                ids.add(orgEO.getId());
            }
        }
        ids.add(ordId);
        List<UserEO> userEOS = userDao.selectByOrgIdAndUsName(ids, null);

        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

        List<String> userIdList = new ArrayList<>();
        for (UserEO userEO : userEOS) {
            if (!userEO.getUsid().equals(userId)) {
                userIdList.add(userEO.getUsid());
            }
        }
        queryBuilder.should(
                QueryBuilders.boolQuery().must(QueryBuilders.termsQuery(CREATE_USER_ID, userIdList))
                        .must(QueryBuilders.termQuery("approveState", 1))
                        .mustNot(QueryBuilders.termQuery(DEL_FLAG, Boolean.TRUE))
        );

        queryBuilder.should(
                QueryBuilders.boolQuery().must(QueryBuilders.termQuery(CREATE_USER_ID, userId))
                        .mustNot(QueryBuilders.termQuery(DEL_FLAG, Boolean.TRUE))
        );

        return getPageDTO(queryBuilder, page, size);

    }

    /**
     * @Description: 任务完成状态 （修改某个任务的完成状态）
     * @auther: ZHAIKAIXUAN
     * @params:
     * @return:
     * @date: 2018/11/26 13:51
     */
    public void taskStatusUpdate(Task task) {
        if (null == task) {
            throw new IllegalArgumentException("任务不能为空！");
        }
        //保存新的任务状态
        taskRepository.save(task);
        // 判断是否要开启项目状态的修改
        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .mustNot(QueryBuilders.termQuery(DEL_FLAG, true))
                .must(QueryBuilders.termQuery("parentProjectId", task.getProjectId()));
        List<Task> tasks = Lists.newArrayList(taskRepository.search(queryBuilder));

        String finishedStatus = ProjectStatusEnums.COMPLETE.getStatus();
        for (Task taskEle : tasks) {
            if (taskEle.getTaskStatus().equals(ProjectStatusEnums.COMPLETE.getStatus())) {
                finishedStatus = ProjectStatusEnums.EXECUTE.getStatus();
                break;
            }
        }
        // 获取项目
        Project project = projectRepository.findOne(task.getProjectId());
        if (null == project) {
            return;
        }
        if (!project.getFinishedStatus().equals(finishedStatus)) {
            project.setFinishedStatus(finishedStatus);
        }

        // 插入新的项目状态
        projectRepository.save(project);
    }

    /**
     * @Description: 子任务的完成状态的修改
     * @auther: ZHAIKAIXUAN
     * @params:
     * @return:
     * @date: 2018/11/26 14:25
     */
    public void childrenTask(ChildrenTask childrenTask) {
        if (null == childrenTask) {
            throw new AdcDaBaseException("子任务不能为空！");
        }
        // 确定任务的状态
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("taskId", childrenTask.getTaskId()));
        List<ChildrenTask> childrenTasks = Lists.newArrayList(childTaskRepository.search(queryBuilder));
        // 判断子任务的完成状态去确定任务的状态
        String status = ProjectStatusEnums.COMPLETE.getStatus();
        for (ChildrenTask childrenTaskEle : childrenTasks) {
            if (childrenTaskEle.getStatus().equals(ProjectStatusEnums.EXECUTE.getStatus())) {
                status = ProjectStatusEnums.EXECUTE.getStatus();
                break;
            }
        }
        // 确定是否要修改任务的状态
        Task task = taskRepository.findOne(childrenTask.getTaskId());
        // 获取下面的所有的子任务
        List<ChildrenTask> childrenTaskList = childTaskRepository.findByTaskIdEquals(childrenTask.getTaskId());
        //任务的完成状态
        String taskStatus = ProjectStatusEnums.COMPLETE.getStatus();
        for (ChildrenTask childTk : childrenTaskList) {
            if (childTk.getStatus().equals(ProjectStatusEnums.COMPLETE.getStatus())) {
                taskStatus = ProjectStatusEnums.EXECUTE.getStatus();
                break;
            }
        }
        if (!taskStatus.equals(task.getTaskStatus())) {
            task.setTaskStatus(status);
            if (taskStatus.equals(ProjectStatusEnums.COMPLETE.getStatus())) {
                task.setFinishedActualTime(new Date());
            }
            taskStatusUpdate(task);
        }
    }

    /**
     * 根据条件查询日报
     * created by chenhaidong 2019/1/17
     *
     * @return
     */
    public PageDTO queryByConditionPage(DailyConditionDTO dailyConditionDTO) {
        //查询子任务id列表
        Set<String> childrenTaskIds = getChildrenTaskIdsByCondition(dailyConditionDTO);
        Integer page = dailyConditionDTO.getPage();
        Integer size = dailyConditionDTO.getSize();
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }

        List<Daily> dailyList = null;
        //查询日报信息
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        query.should(QueryBuilders.termsQuery(CREATE_USER_ID, userId));
        //如果子任务id为空，则查询全部
        if (CollectionUtils.isNotEmpty(childrenTaskIds)) {
            query.should(QueryBuilders.termsQuery("taskIdArray", childrenTaskIds));
        }
        queryBuilder.must(query);
        queryBuilder.mustNot(QueryBuilders.termQuery(DEL_FLAG, true));
        //按创建时间倒序排序
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        //根据筛选条件和分页条件查询日报列表
        Page<Daily> queryPage = dailyRepository.search(
                queryBuilder,
                new PageRequest(page == null || page < 1 ? 0 : page - 1, size == null || size < 1 ? 10 : size, sort));
        dailyList =
                (queryPage == null || (dailyList = queryPage.getContent()) == null) ? new ArrayList<Daily>() : dailyList;
        return new PageDTO(queryPage == null ? 0 : queryPage.getTotalElements(), dailyList, page, size);
    }

    /**
     * 根据条件获取子任务id列表
     *
     * @param dailyConditionDTO
     * @return
     */
    public Set<String> getChildrenTaskIdsByCondition(DailyConditionDTO dailyConditionDTO) {
        if (dailyConditionDTO == null) {
            throw new AdcDaBaseException("筛选条件不能为空！");
        }
        List<String> idList = null;
        //业务id集合
        Set<String> budgetIds = new HashSet<>(
                (idList = dailyConditionDTO.getBudgetIds()) == null ? new ArrayList<String>() : idList);
        //项目id集合
        Set<String> projectIds = new HashSet<>(
                (idList = dailyConditionDTO.getProjectIds()) == null ? new ArrayList<String>() : idList);
        //任务id集合
        Set<String> taskIds = new HashSet<>(
                (idList = dailyConditionDTO.getTaskIds()) == null ? new ArrayList<String>() : idList);
        //子任务id集合
        Set<String> childrenTaskIds = new HashSet<>(
                (idList = dailyConditionDTO.getChildrenTaskIds()) == null ? new ArrayList<String>() : idList);
        try {
            //如果是某个业务下，则查询任务列表和项目列表
            getIdInfoAndFillSet(
                    projectIds,
                    budgetIds,
                    projectRepository,
                    projectRepository.getClass().getMethod("findByBudgetIdIn", Collection.class));
            getIdInfoAndFillSet(
                    taskIds,
                    budgetIds,
                    taskRepository,
                    taskRepository.getClass().getMethod("findByBudgetIdIn", Collection.class));
            //如果项目id存在
            getIdInfoAndFillSet(
                    taskIds,
                    projectIds,
                    taskRepository,
                    taskRepository.getClass().getMethod("findDistinctByProjectIdIn", Collection.class));
            // 如果任务id存在
            getIdInfoAndFillSet(
                    childrenTaskIds,
                    taskIds,
                    childTaskRepository,
                    childTaskRepository.getClass().getMethod("findByTaskIdIn", Collection.class));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AdcDaBaseException("系统出错");
        }
        return childrenTaskIds;
    }

    /**
     * 遍历list获取id并添加到set中
     *
     * @param list
     * @param set
     */
    public void fillIdToSet(List list, Set set)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //如果list不为空
        if (CollectionUtils.isNotEmpty(list)) {
            //获取getId方法
            Method getId = list.get(0).getClass().getMethod("getId");
            for (Object o : list) {
                //设置可访问
                getId.setAccessible(true);
                //调用getId方法获取id并添加到set中
                set.add(getId.invoke(o));
            }
        }
    }

    /**
     * 根据id列表查询下级信息列表，并将下级信息id添加至集合中
     *
     * @param fillSet      被添加的id集合
     * @param searchSet    需要查询的id集合
     * @param repository   需要查询的dao层
     * @param searchMethod 需要执行的查询方法
     */
    public void getIdInfoAndFillSet(Set<String> fillSet, Set<String> searchSet, Object repository, Method searchMethod)
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        //判断id是否存在
        if (CollectionUtils.isNotEmpty(searchSet)) {
            //根据id集合查询下级信息列表，并将下级信息id存入集合中
            fillIdToSet((List) searchMethod.invoke(repository, searchSet), fillSet);
        }
    }

    private List<String> getCurrentUserProjectIds() {
        List<Project> projects = taskService.currentUserProject();
        List<String> projectIds = new ArrayList<>();
        if (StringUtils.isNotEmpty(projects)) {
            for (Project project : projects) {
                projectIds.add(project.getId());
            }
        }
        return projectIds;
    }

    /**
     * 查询客户和分公司
     *
     * @return
     */
    public List<CustomerRecordEO> queryBranch() throws Exception {
        BasePage page = new CustomerRecordEOPage();
        page.setPage(1);
        page.setPageSize(1000);
        List<CustomerRecordEO> customerRecordEOList = customerRecordEOService.queryCRMListByPage(page);
        List<BranchEO> branchEOList = branchDao.queryAll();
        for (BranchEO branchEO : branchEOList) {
            CustomerRecordEO customerRecordEO = new CustomerRecordEO();
            customerRecordEO.setCustomerName(branchEO.getBranchName());
            customerRecordEO.setId(UUID.randomUUID());
            customerRecordEOList.add(customerRecordEO);
        }
        return customerRecordEOList;
    }

    public int countByUserId(String userId) {
        return dailyRepository.countByCreateUserIdAndDelFlagNot(userId, true);
    }

    private PageDTO getPageDTO(QueryBuilder queryBuilder, int page, int size) {
        List<Daily> queryList = null;
        //Sort sort = new Sort(Sort.Direction.DESC, "approveState", "createTime");

        Sort sort = new Sort(Sort.Direction.DESC, "approveState", "dailyCreateTime");

        Page<Daily> queryPage = dailyRepository.search(queryBuilder, new PageRequest(page - 1, size, sort));
        queryList =
                (queryPage == null || (queryList = queryPage.getContent()) == null) ? new ArrayList<Daily>() : queryList;
        long count = queryPage == null ? 0 : queryPage.getTotalElements();
        setCreateUserName(queryList);
        return new PageDTO(count, queryList, page, size);
    }

    /**
     * 解决旧数据字段用错的问题
     *
     * @param dailyList
     */
    public void updateCreateTime(List<Daily> dailyList) {
//        boolean leaderFlag = false;
//        Subject subject = SecurityUtils.getSubject();
//        if (subject.hasRole(Role.BEN_BU_ZHANG)
//            || subject.hasRole(Role.BU_ZHANG)
//            || subject.hasRole(Role.ZHU_REN)) {
//            leaderFlag = true;
//        }
        for (Daily daily : dailyList) {
            if (daily.getDailyCreateTime() == null) {
                daily.setDailyCreateTime(daily.getCreateTime());
            }
            if (daily.getCreateTime() == null) {
                daily.setCreateTime(daily.getDailyCreateTime());
            }
            if(StringUtils.isNotEmpty(daily.getProjectName())){
                String projectName = daily.getProjectName().replace("&amp;&amp;","&").replace("&amp;amp;","&");
                daily.setProjectName(projectName);
            }
            if(StringUtils.isNotEmpty(daily.getTaskName())){
                String taskName = daily.getTaskName().replace("&amp;&amp;","&").replace("&amp;amp;","&");
                daily.setTaskName(taskName);
            }
//            if (leaderFlag) {
//                daily.setApproveUserName("---");
//            }
            try {
                if(StringUtils.isNotEmpty(daily.getTaskResultFileId())) {
                    FileEO fileEO = fileEOService.selectByPrimaryKey(daily.getTaskResultFileId());
                    daily.setResultFileName(fileEO.getFileName() + "." + fileEO.getFileType());
                }
            } catch (Exception e){
                logger.error(e.getMessage());
            }
        }
    }

    /**
     * 大领导不想看到审批人
     *
     * @param dailyList
     */
    public void updateApproveUserName(List<Daily> dailyList, Set<String> userIdSet) {

        for (Daily daily : dailyList) {
            if (userIdSet.contains(daily.getCreateUserId())) {
                daily.setApproveUserName("---");
                daily.setApproveUserNameES("---");
            }

        }
    }




    private void setCreateUserName(List<Daily> list) {
        if (CollectionUtils.isNotEmpty(list)) {
            for (Daily daily : list) {
                if (StringUtils.isEmpty(daily.getCreateUserName())) {
                    String usname = userEOService.getUserWithRoles(daily.getCreateUserId()).getUsname();
                    daily.setCreateUserName(usname);
                }
                if (StringUtils.isEmpty(daily.getProjectName())) {
                    daily.setProjectName("");
                }
                //添加日报成果上传文件名   20200303   todo？ 文件名为空，文件id没考虑呀。。。
//                if (StringUtils.isEmpty(daily.getResultFileName())){
//                    try {
//                        FileEO fileEO = fileEOService.selectByPrimaryKey(daily.getTaskResultFileId());
//                        daily.setResultFileName(fileEO.getFileName()+"."+fileEO.getFileType());
//                    } catch (Exception e){
//                        e.printStackTrace();
//                    }
//                }
            }
        }
    }

    private void setESField(List<Daily> list) {
        if (CollectionUtils.isNotEmpty(list)) {
            for (Daily daily : list) {
                setESField(daily);
            }
        }
    }

    private void setESField(Daily daily) {
        if (StringUtils.isNotEmpty(daily.getApproveUserName())) {
            daily.setApproveUserNameES(daily.getApproveUserName());
        }
        if (StringUtils.isNotEmpty(daily.getCreateUserName())) {
            daily.setCreateUserNameES(daily.getCreateUserName());
        }
        if (StringUtils.isNotEmpty(daily.getBudgetName())) {
            daily.setBudgetNameES(daily.getBudgetName());
        }
        if (StringUtils.isNotEmpty(daily.getProjectName())) {
            daily.setProjectNameES(daily.getProjectName());
        }
        if (StringUtils.isNotEmpty(daily.getTaskName())) {
            daily.setTaskNameES(daily.getTaskName());
        }
    }

    public PageDTO queryByPageDelFlag(int page, int size) {
        //当前用户的id
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        Subject subject = SecurityUtils.getSubject();
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.termQuery(DEL_FLAG, true));
        if (subject.hasRole(Role.SUPER_ADMIN)) {
            return getPageDTO(queryBuilder, page, size);
        }

        throw new AdcDaBaseException("权限不足");
    }


    public  void refreshDaily(){
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.mustNot(QueryBuilders.termQuery(DEL_FLAG, true));
        queryBuilder.must(QueryBuilders.termQuery("taskResultFileId",""));
        Iterable<Daily> dailyIterable =  dailyRepository.search(queryBuilder);
        if(dailyIterable.iterator().hasNext()){
            for (Daily daily : dailyIterable){
                daily.setTaskResultFileId("");
            }
        }
        dailyRepository.save(dailyIterable);
    }


    public  void refreshDailyProjectId() {
        Map<String,Project> idAndProjectMap = new HashMap<>();
        Map<String,Task> idAndTaskMap = new HashMap<>();
        Map<String,ChildrenTask> idAndChildTaskMap = new HashMap<>();
        while (true) {
            BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
            queryBuilder.mustNot(QueryBuilders.termQuery(DEL_FLAG, true));
            queryBuilder.must(QueryBuilders.existsQuery("workCostTime"));
            queryBuilder.must(QueryBuilders.termQuery("budgetId",""));
            Iterable<Daily> dailyIterable = dailyRepository.search(queryBuilder, new PageRequest(0, 300));
            if (dailyIterable.iterator().hasNext()) {
                for (Daily daily : dailyIterable) {
                    refresh(daily,idAndProjectMap,idAndTaskMap,idAndChildTaskMap);
                }
                dailyRepository.save(dailyIterable);
            }else {
                break;
            }
        }
        while (true) {
            BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
            queryBuilder.mustNot(QueryBuilders.termQuery(DEL_FLAG, true));
            queryBuilder.must(QueryBuilders.existsQuery("workCostTime"));
            queryBuilder.mustNot(QueryBuilders.existsQuery("budgetName"));
            Iterable<Daily> dailyIterable = dailyRepository.search(queryBuilder, new PageRequest(0, 300));
            if (dailyIterable.iterator().hasNext()) {
                for (Daily daily : dailyIterable) {
                    refresh(daily,idAndProjectMap,idAndTaskMap,idAndChildTaskMap);
                }
                dailyRepository.save(dailyIterable);
            }else {
                break;
            }
        }
    }

    private void refresh(Daily daily,
                         Map<String,Project> idAndProjectMap ,
                         Map<String,Task> idAndTaskMap ,
                         Map<String,ChildrenTask> idAndChildTaskMap){
        String taskId = "";
        if (CollectionUtils.isNotEmpty(daily.getTaskIdArray())) {
            taskId = daily.getTaskIdArray()[0];
        }
        Task task = idAndTaskMap.get(taskId);
        if (null == task) {
            task = taskRepository.findById(taskId);
            idAndTaskMap.put(taskId,task);
        }
        if (task == null) {
            ChildrenTask childrenTask = idAndChildTaskMap.get(taskId);
            if (null == childrenTask) {
                childrenTask = childTaskRepository.findById(taskId);
                idAndChildTaskMap.put(taskId,childrenTask);
            }
            if (null != childrenTask && StringUtils.isNotEmpty(childrenTask.getTaskId())) {
                task = idAndTaskMap.get(taskId);
                if (null == task) {
                    task = taskRepository.findById(childrenTask.getTaskId());
                    idAndTaskMap.put(childrenTask.getTaskId(),task);
                }
            } else {
                return;
            }
        }
        if (null == task) {
            return;
        } else {
            if (StringUtils.isNotEmpty(task.getProjectId())) {
                daily.setProjectId(task.getProjectId());
                daily.setProjectName(task.getProjectName());
                daily.setProjectNameES(task.getProjectName());
            }
            if (StringUtils.isNotEmpty(task.getProjectId())) {
                Project project = idAndProjectMap.get(task.getProjectId());
                if (null == project) {
                    project = projectRepository.findById(task.getProjectId());
                    idAndProjectMap.put(task.getProjectId(),project);
                }
                if (null != project) {
                    if(StringUtils.isNotEmpty(project.getBudget())){
                        daily.setBudgetId(project.getBudgetId());
                        daily.setBudgetName(project.getBudget());
                        daily.setBudgetNameES(project.getBudget());
                    }else {
                        try {
                            BudgetEO budgetEO = budgetEOService.getDao().selectById(project.getBudgetId());
                            if (null!= budgetEO){
                                daily.setBudgetId(project.getBudgetId());
                                daily.setBudgetName(project.getBudget());
                                daily.setBudgetNameES(project.getBudget());
                                project.setBudget(budgetEO.getProjectName());
                                final Project newProject = project;
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        projectRepository.save(newProject);
                                    }
                                });
                            }
                        }catch (Exception e){
                            logger.error("日报数据刷新异常 project = "+project.toString() + "  ex = " + e.fillInStackTrace());
                        }
                    }
                }
            }
        }
    }


    //按部门顺序插入员工信息到 Map的List
    public void addEmployeeByDept( List<Map<String, Object>> resultEmployeeInfoMapList,Map<String,Object> anEmployee) {
        int i=0;
        if(resultEmployeeInfoMapList==null){
            resultEmployeeInfoMapList.add(anEmployee);
        }
        for (Map<String,Object> anExistingEmployee: resultEmployeeInfoMapList) {
            if (anExistingEmployee.get("PARENT_DEPT").toString().compareTo(anEmployee.get("PARENT_DEPT").toString())<0) {
                i++;
            }
            else if(anExistingEmployee.get("PARENT_DEPT").toString().compareTo(anEmployee.get("PARENT_DEPT").toString())==0) {
                if(anExistingEmployee.get("ORG_NAME").toString().compareTo(anEmployee.get("ORG_NAME").toString())<0)i++;
                else if(anExistingEmployee.get("ORG_NAME").toString().compareTo(anEmployee.get("ORG_NAME").toString())>0)break;
                else break;
            }
            else
            {
                break;
            }
        }
        resultEmployeeInfoMapList.add(i, anEmployee);
    }


    //按工时排序日报
    public void trimEmployeeInfoMapList( List<Map<String, Object>> employeeInfoMapList) {
        if(employeeInfoMapList==null)return;
        int listSize=employeeInfoMapList.size();
        for(int i=0;i<listSize;i++)
            for(int j=i+1;j<listSize;j++){
                String currentDept1 = employeeInfoMapList.get(i).get("ORG_NAME").toString();
                String parentDept1 = employeeInfoMapList.get(i).get("PARENT_DEPT").toString();
                String employeeName1=employeeInfoMapList.get(i).get("USNAME").toString();

                String currentDept2 = employeeInfoMapList.get(j).get("ORG_NAME").toString();
                String parentDept2 = employeeInfoMapList.get(j).get("PARENT_DEPT").toString();
                String employeeName2=employeeInfoMapList.get(j).get("USNAME").toString();


                float workCostTime1=Float.parseFloat(employeeInfoMapList.get(i).get("workCostTime").toString());
                float workCostTime2=Float.parseFloat(employeeInfoMapList.get(j).get("workCostTime").toString());

                if(currentDept1.equals(currentDept2)&&parentDept1.equals(parentDept2)&&workCostTime1<workCostTime2)
                {
                    String tmp=employeeName1;
                    employeeName1=employeeName2;
                    employeeName2=tmp;

                    float tmp2=workCostTime1;
                    workCostTime1=workCostTime2;
                    workCostTime2=tmp2;

                    employeeInfoMapList.get(i).put("USNAME",employeeName1);
                    employeeInfoMapList.get(i).put("workCostTime",workCostTime1);

                    employeeInfoMapList.get(j).put("USNAME",employeeName2);
                    employeeInfoMapList.get(j).put("workCostTime",workCostTime2);

                }

            }


    }


    //从数据库查询用户部门信息，返回Map 的List
    public List<Map<String,Object>> getEmployeeInfoMapList()
    {
        try {
            Set<String> idStore=new HashSet<>();
            List<Map<String, Object>> originalEmployeeInfoMapList = userEPDao.getEmployeeInfoMapList();
            List<Map<String, Object>> resultEmployeeInfoMapList = new LinkedList<>();
            if (CollectionUtils.isEmpty(originalEmployeeInfoMapList)) return null;
            int employeeSize = originalEmployeeInfoMapList.size();
            for (int i = 0; i < employeeSize; i++) {
                Map<String, Object> anEmployeeInfoMap = originalEmployeeInfoMapList.get(i);
                if (anEmployeeInfoMap != null) {
                    if (anEmployeeInfoMap.get("PARENT_IDS") == null) continue;
                    int layer = anEmployeeInfoMap.get("PARENT_IDS").toString().split(",").length;
                    if (layer < 3) continue;
                    String currentDept = "";
                    String parentDept = "";
                    switch (layer) {
                        case 6:
                            currentDept = anEmployeeInfoMap.get("ORG_NAME3").toString();
                            parentDept = anEmployeeInfoMap.get("ORG_NAME4").toString();
                            break;
                        case 5:
                            currentDept = anEmployeeInfoMap.get("ORG_NAME2").toString();
                            parentDept = anEmployeeInfoMap.get("ORG_NAME3").toString();
                            break;
                        case 4:
                            currentDept = anEmployeeInfoMap.get("ORG_NAME1").toString();
                            parentDept = anEmployeeInfoMap.get("ORG_NAME2").toString();
                            break;
                        case 3:
                            currentDept = anEmployeeInfoMap.get("ORG_NAME1").toString();
                            parentDept = anEmployeeInfoMap.get("ORG_NAME1").toString();
                            break;
                        default:
                            break;
                    }


                    String userid = anEmployeeInfoMap.get("USID").toString();
                    String username = anEmployeeInfoMap.get("USNAME").toString();

                    boolean addFlag=idStore.add(userid);
                    if(addFlag) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("ORG_NAME", currentDept);
                        map.put("PARENT_DEPT", parentDept);
                        map.put("USNAME", username);
                        map.put("USID", userid);
                        //resultEmployeeInfoMapList.add(map);
                        addEmployeeByDept(resultEmployeeInfoMapList,map);
                    }
                }
            }

            return resultEmployeeInfoMapList;
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return null;
        }
    }

    //创建返回Excel表头
    private Workbook getDailyStatisticTemplate() {
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("工时统计");
        Row titleRow = sheet.createRow(0); // 创建表头
        for (int i = 0, len = TITLES.length; i < len; i++) {
            Cell cell = titleRow.createCell(i);
            cell.setCellValue(TITLES[i]);
        }
        return workbook;
    }



    //给控制器返回日志统计的Excel
    public Workbook getDailyStatisticWorkbook(String startDate,String endDate) {
        Workbook workbook = getDailyStatisticTemplate();
        Sheet sheet = workbook.getSheetAt(0);
        sheet.setColumnWidth(0,40*256);
        sheet.setColumnWidth(1,40*256);
        sheet.setColumnWidth(2,20*256);
        sheet.setColumnWidth(3,20*256);
        List<Map<String,Object>> employeeInfoMapList = getEmployeeInfoMapList();
        Map<String,Float> dailyStatisticMap = queryAllUsersDailyByPage(startDate,endDate);
        Set<String> userIdsInEs=dailyStatisticMap.keySet();
        int rowIndex = 1;


        for(int i=0;i<employeeInfoMapList.size()&&i<100000;i++){
            Map<String,Object> employeeInfoMap=employeeInfoMapList.get(i);
            employeeInfoMap.put("workCostTime",0);
            String empId=employeeInfoMap.get("USID").toString();
            for(String empid:userIdsInEs) {
                if(empId.equals(empid)) {
                    employeeInfoMap.put("workCostTime",dailyStatisticMap.get(empid));
                    break;
                }
            }

        }

        trimEmployeeInfoMapList(employeeInfoMapList);
        try {
            for (int j = 0; j < employeeInfoMapList.size(); j++) {
                Row row = sheet.createRow(rowIndex);
                Cell cell1 = row.createCell(0);
                cell1.setCellValue(employeeInfoMapList.get(j).get("PARENT_DEPT").toString());
                Cell cell2 = row.createCell(1);
                cell2.setCellValue(employeeInfoMapList.get(j).get("ORG_NAME").toString());
                Cell cell3 = row.createCell(2);
                cell3.setCellValue(employeeInfoMapList.get(j).get("USNAME").toString());
                Cell cell4 = row.createCell(3);
                cell4.setCellValue(employeeInfoMapList.get(j).get("workCostTime").toString());
                rowIndex++;
            }
        }
        catch(Exception ex) {
            System.out.println(ex.getMessage());
        }

        return workbook;

    }


    //给控制器返回日志统计的Excel
    public List<DailyExportStasticsVO> getDailyStatisticWorkbookNew(String startDate,String endDate) {
        List<UserEO> userEOList = userEOService.queryUserEOList();
        List<OrgEO> allOrgEOList = orgEODao.queryOrgAll();
        Map<String,Float> dailyStatisticMap = queryAllUsersDailyByPage(startDate,endDate);
//        Set<String> userIdSet = dailyStatisticMap.keySet();
        List<DailyExportStasticsVO> dailyExportStasticsVOList = new ArrayList<>();
        for (UserEO userEO : userEOList){
            if (StringUtils.equals(userEO.getExtInfo(),"10")){
                continue;
            }
            DailyExportStasticsVO dailyExportStasticsVO = new DailyExportStasticsVO();
            dailyExportStasticsVO.setUserName(userEO.getUsname());
            List<OrgEO> orgEOList = userEO.getOrgEOList();
            for (OrgEO orgEO : orgEOList){
                if (StringUtils.equals(orgEO.getLayer(),"4")){
                    if (StringUtils.isNotEmpty(orgEO.getParentId())){
                        // 在内存里查
                        OrgEO bigOrgEO = getOrgEOById(allOrgEOList, orgEO.getParentId());
                        if (null != bigOrgEO){
                            dailyExportStasticsVO.setBigDeptName(bigOrgEO.getName());
                            dailyExportStasticsVO.setCompareString(bigOrgEO.getName());

                        }
                    }
                    if (StringUtils.isNotEmpty(orgEO.getName())) {
                        dailyExportStasticsVO.setDeptName(orgEO.getName());
                        // 构造一个用于比较的字段
                        String compareString = (StringUtils.isNotEmpty(dailyExportStasticsVO.getCompareString())?dailyExportStasticsVO.getCompareString():"")+orgEO.getName();
                        dailyExportStasticsVO.setCompareString(compareString);
                    }
                }
            }
            if (StringUtils.isEmpty(dailyExportStasticsVO.getDeptName())) { // 没有科室 肯定没有大部门 应该是本部领导 或者数据中心领导
                for (OrgEO orgEO : orgEOList) {
                    if (StringUtils.equals(orgEO.getLayer(), "3")) {
                        if (null != orgEO){
                            dailyExportStasticsVO.setBigDeptName(orgEO.getName());
                            dailyExportStasticsVO.setCompareString(orgEO.getName());
                        }
                    }
                }
            }
            // 到这一步，还没有大部门的 都不要了
            if (StringUtils.isEmpty(dailyExportStasticsVO.getBigDeptName())){
                continue;
            }
            Float workTime = dailyStatisticMap.get(userEO.getUsid());
            dailyExportStasticsVO.setWorkTime((null!=workTime?String.valueOf(workTime):"0"));
            dailyExportStasticsVOList.add(dailyExportStasticsVO);
        }
        Collections.sort(dailyExportStasticsVOList, new Comparator<DailyExportStasticsVO>() {
            @Override
            public int compare(DailyExportStasticsVO o1, DailyExportStasticsVO o2) {
                if (StringUtils.compare(o1.getCompareString(),o2.getCompareString()) == 0){
                    return Float.valueOf(o2.getWorkTime()).compareTo(Float.valueOf(o1.getWorkTime()));
                }
                return StringUtils.compare(o1.getCompareString(),o2.getCompareString());
            }
        });
        return dailyExportStasticsVOList;

    }

    public OrgEO getOrgEOById(List<OrgEO> orgEOList , String orgId){
        for (OrgEO orgEO : orgEOList){
            if (StringUtils.equals(orgId,orgEO.getId())){
                return orgEO;
            }
        }
        return null;
    }

    public UserEO getUserEOById(List<UserEO> userEOList , String userId){
        for (UserEO userEO : userEOList){
            if (StringUtils.equals(userId,userEO.getUsid())){
                return userEO;
            }
        }
        return null;
    }

    //从ES按日期查询指定条件的日报数据
    public Map<String,Float> queryAllUsersDailyByPage(String startDate,String endDate) {
        //当前用户的id
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }

        Date date1=null;
        Date date2=null;
        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startDate+" 0:0:0");
            date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endDate+" 23:59:59");
        }
        catch (Exception ex) {
            throw new AdcDaBaseException("请按正确的日期格式（yyyy-MM-dd)传递日期参数");
        }


        long startTimestamp = date1.getTime();
        long endTimestamp = date2.getTime();
        int[] approveStateArr = {1,2};
        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .mustNot(QueryBuilders.termQuery(DEL_FLAG, true))
                .must(QueryBuilders.rangeQuery("dailyCreateTime").from(startTimestamp).to(endTimestamp))
                .must(QueryBuilders.termsQuery("approveState", approveStateArr))
                .must(QueryBuilders.existsQuery("workCostTime"));

        NativeSearchQueryBuilder nativeSearchQueryBuilder =
                new NativeSearchQueryBuilder().withQuery(queryBuilder);
        // nativeSearchQueryBuilder.withSourceFilter(new FetchSourceFilter(new String[] { "" }, null));
        TermsBuilder dailyAgg =AggregationBuilders.terms("createUserId").field("createUserId")
                .order(Terms.Order.aggregation("workCostTime", false)).size(999999);

        SumBuilder dailySumAgg = AggregationBuilders.sum("workCostTime").field("workCostTime");
        dailyAgg.subAggregation(dailySumAgg);

        nativeSearchQueryBuilder.addAggregation(dailyAgg);
        SearchQuery searchQuery = nativeSearchQueryBuilder.build();

        AggregatedPage<Daily> aggPage = (AggregatedPage<Daily>) dailyRepository.search(searchQuery);
        StringTerms agg = (StringTerms) aggPage.getAggregation("createUserId");
        List<Terms.Bucket> buckets = agg.getBuckets();
        Map<String,Float> result=new HashMap<>();
        for (Terms.Bucket bucket : buckets) {
            Sum sum = (Sum) bucket.getAggregations().asMap().get("workCostTime");
            BigDecimal obj = BigDecimal.valueOf(sum.getValue());
            float workTimeCost = obj.floatValue();
            workTimeCost=Math.round(workTimeCost/8.0*100)/100.0f;
            String uid=bucket.getKeyAsString();
            result.put(uid,workTimeCost);
        }
        return  result;

    }

    //根据ID查询出ID 类型（项目、业务） 、名称、对应的业务（如果是项目)
    private JsonObject getTaskById(String id){
        JsonObject jsonObject=new JsonObject();
        Task task=taskRepository.findById(id);
        //该id属于业务
        if(task!=null&&task.getProjectId()==null){
            String businessId=task.getBudgetId();
            if(StringUtils.isNotEmpty(businessId)){
                BudgetVO budgetVO=budgetEOService.findOne(businessId);
                if(budgetVO!=null){
                    String businessName=budgetVO.getProjectName();
                    jsonObject.addProperty("type",1);
                    jsonObject.addProperty("name",businessName);
                    jsonObject.addProperty("id",businessId);
                    return jsonObject;
                }
            }
            return null;
        }
        //该id属于项目
        if(task!=null&&task.getProjectId()!=null){
            String projectName=task.getProjectName();
            String projectId=task.getProjectId();
            if(StringUtils.isNotEmpty(projectName)){
                jsonObject.addProperty("type",2);
                jsonObject.addProperty("name",projectName);
                jsonObject.addProperty("id",projectId);
                return jsonObject;
            }
        }

        //否则，属于子任务
        ChildrenTask childrenTask=childTaskRepository.findById(id);
        if(childrenTask!=null){
            //查找子任务对应的任务 ID
            String taskId=childrenTask.getTaskId();
            if(StringUtils.isNotEmpty(taskId)){
                Task taskOfSubTask=taskRepository.findById(taskId);
                if(taskOfSubTask!=null){
                    //Id属于业务
                    if(taskOfSubTask.getProjectId()==null){
                        String businessId=taskOfSubTask.getBudgetId();
                        if(StringUtils.isNotEmpty(businessId)){
                            BudgetVO budgetVO=budgetEOService.findOne(businessId);
                            if(budgetVO!=null){
                                String businessName=budgetVO.getProjectName();
                                jsonObject.addProperty("type",1);
                                jsonObject.addProperty("name",businessName);
                                jsonObject.addProperty("id",businessId);
                                return jsonObject;                         }
                        }
                    }

                    //Id属于项目
                    else{
                        String parentTaskName= taskOfSubTask.getProjectName();
                        String projectId=taskOfSubTask.getProjectId();
                        jsonObject.addProperty("type",2);
                        jsonObject.addProperty("name",parentTaskName);
                        jsonObject.addProperty("id",projectId);
                        return jsonObject;
                    }
                }
            }
        }
        return null;
    }



    //根据项目id查询业务id
    private String getBusinessIdByProjectId(String projectId){
        Project project= projectRepository.findById(projectId);
        if(project!=null){
            return project.getBudgetId();
        }
        return null;
    }

    //查询某个部门人员id列表
    public List<String> getMemberIdListOfDept(String deptId){
        List<UserEO> empList = orgEODao.listUserEOByOrgId(deptId);
        List<String> resultList=new LinkedList<>();
        for(UserEO userEO :empList){
            resultList.add(userEO.getUsid());
        }
        return resultList;
    }

    //查询软件开发部的人员id列表
    public List<String> getMemberIdListOfSoftwareDevelopmentDepartment(){
        String idOfSoftwareDevelopmentDepartment="MEGMW98MVJ";
        return getMemberIdListOfDept(idOfSoftwareDevelopmentDepartment);
    }


    //根据id和type判断一个map是否在map list
    public boolean mapExistsInMapList(List<Map<String,String>> list,Map<String,String> map){
        if(CollectionUtils.isEmpty(list))return false;
        if(map==null)return false;
        for(Map<String,String> item: list){
            String itemType=item.get("type").toString();
            String  mapType=map.get("type").toString();
            String itemId=item.get("id").toString();
            String mapId=map.get("id").toString();
            if(itemType.equals(mapType)&&itemId.equals(mapId)){
                return true;
            }
        }
        return false;
    }

    //根据id和empId判断JsonObject是否在 List<JsonObject>
    public boolean jsonObjectExistsInJsonObjectList(List<JsonObject> list,JsonObject jsonObject){
        if(CollectionUtils.isEmpty(list))return false;
        if(jsonObject==null)return false;
        for(JsonObject item: list){
            String id=item.get("id").getAsString();
            String  tempId=jsonObject.get("id").getAsString();

            String empId=item.get("empId").getAsString();
            String  tempEmpId=jsonObject.get("empId").getAsString();

            if(id.equals(tempId)&&empId.equals(tempEmpId)){
                return true;
            }
        }
        return false;
    }

    //根据员工Id集合，查询其参与的业务和项目*************************************************************
    public List<Map<String,String>> getBusinessAndProjectsByEmpIds(Set<String> empIds,String businName,String projName) {
        return getBusinessAndProjectsByEmpIdsPage(empIds,99999,1,businName,projName);
    }



    //根据员工Id集合，查询其参与的业务和项目*************************************************************
    public List<Map<String,String>> getBusinessAndProjectsByEmpIdsPage(Set<String> empIds, int pageSize,int pageNo,String businName,String projName) {
        List<UserWithProjects> userWithProjectsList = userWithProjectsRepository.findByUserIdIn(empIds);
        List<Map<String, String>> allBusinessAndProjects = new ArrayList<>();

        Set<String> allBusinessIds = new HashSet<>();
        Set<String> allProjectIds = new HashSet<>();

        //开始遍历userWithProjects
        for (UserWithProjects userWithProjects : userWithProjectsList) {
            Set<String> businessIds = userWithProjects.getBusinessIds();
            Set<String> projectIds = userWithProjects.getProjectIds();

            for (String businessId : businessIds) {
                if (StringUtils.isEmpty(businessId)) continue;
                allBusinessIds.add(businessId);
            }

            for (String projectId : projectIds) {
                if (StringUtils.isEmpty(projectId)) continue;
                allProjectIds.add(projectId);
            }
        }//遍历userWithProjects结束


        //如果没有输入项目名和业务名
        if (StringUtils.isEmpty(businName) && StringUtils.isEmpty(projName)) {
            List<String > businessIdList=new LinkedList<>(allBusinessIds);
            List<String>  projectIdList=new LinkedList<>(allProjectIds);

            List<BudgetEO> budgetEOList = budgetEOService.selectByPrimaryKeys(businessIdList);
            for(BudgetEO budgetEO : budgetEOList) {
                if (budgetEO == null) continue;
                if(budgetEO.getProjectName().startsWith("旧"))continue;
                String businessName = budgetEO.getProjectName();
                if(businessName.equals(businName))continue;
                String businessId = budgetEO.getId();
                Map<String, String> business = new HashMap<>();
                business.put("id", businessId);
                business.put("name", businessName);
                business.put("type", "1");
                if(!mapExistsInMapList(allBusinessAndProjects,business)){
                    allBusinessAndProjects.add(business);
                    numberOfBusinessAndProjects.getAndIncrement();
                }
            }

            List<Project> projectList = projectRepository.findByIdIn(allProjectIds);
            if(StringUtils.isNotEmpty(projectIdList)) {
                for(Project proj: projectList){
                    String projId=proj.getId();
                    String businId=proj.getBudgetId();
                    if(StringUtils.isEmpty(projId)||StringUtils.isEmpty(businId))continue;
                    projectIdBusinessIdMap.put(projId,businId);
                }
                int projectCount = projectList.size();
                List<Project> partOneList = projectList.subList(0, projectCount / 4);
                List<Project> partTwoList = projectList.subList((projectCount / 4) + 1, projectCount/2);
                List<Project> partThreeList = projectList.subList((projectCount / 2) + 1, projectCount);
                ProjectWorker projectWorker1 = new ProjectWorker(partOneList, allBusinessAndProjects,"projectWorker1");
                ProjectWorker projectWorker2 = new ProjectWorker(partTwoList, allBusinessAndProjects,"projectWorker2");
                ProjectWorker projectWorker3 = new ProjectWorker(partThreeList, allBusinessAndProjects,"projectWorker3");


                /*
                //executorService.execute(projectWorker1);
                //executorService.execute(projectWorker2);

                while(projectWorker1.isAlive()||projectWorker2.isAlive()){

                    logger.info("wait.........................................");
                    try{
                    Thread.sleep(200);}
                    catch (Exception ex){

                    }
                }
            }*/
                projectWorker1.start();
                projectWorker2.start();
                projectWorker3.start();

                try {
                    projectWorker1.join();
                    projectWorker2.join();
                    projectWorker3.join();
                } catch (Exception ex) {
                    logger.info(ex.getMessage());
                    return null;
                }
            }

            int startIndex=(pageNo-1)*pageSize;
            int endIndex=pageNo*pageSize;
            if(CollectionUtils.isEmpty(allBusinessAndProjects)){
                return null;
            }
            if(startIndex>=allBusinessAndProjects.size()){
                return null;
            }
            if(endIndex>allBusinessAndProjects.size()-1){
                endIndex=allBusinessAndProjects.size()-1;
            }

            return allBusinessAndProjects.subList(startIndex,endIndex);


            //*****
            //return null;
        }


        //如果输入了业务名，没输入项目名
        else if (StringUtils.isNotEmpty(businName) && StringUtils.isEmpty(projName)){
            List<String > businessIdList=new LinkedList<>(allBusinessIds);
            List<String>  projectIdList=new LinkedList<>(allProjectIds);

            List<BudgetEO> budgetEOList = budgetEOService.selectByPrimaryKeys(businessIdList);
            for(BudgetEO budgetEO : budgetEOList) {
                if (budgetEO == null) continue;
                if(budgetEO.getProjectName().startsWith("旧"))continue;
                String businessName = budgetEO.getProjectName();
                if(!businessName.equals(businName))continue;
                String businessId = budgetEO.getId();
                Map<String, String> business = new HashMap<>();
                business.put("id", businessId);
                business.put("name", businessName);
                business.put("type", "1");
                if(!mapExistsInMapList(allBusinessAndProjects,business)){
                    allBusinessAndProjects.add(business);
                    numberOfBusinessAndProjects.getAndIncrement();
                }
            }

            List<Project> projectList = projectRepository.findByIdIn(allProjectIds);
            if(StringUtils.isNotEmpty(projectIdList)) {
                for(Project proj: projectList){
                    String projId=proj.getId();
                    String businId=proj.getBudgetId();
                    if(StringUtils.isEmpty(projId)||StringUtils.isEmpty(businId))continue;
                    projectIdBusinessIdMap.put(projId,businId);
                }
                int projectCount = projectList.size();
                List<Project> partOneList = projectList.subList(0, projectCount / 4);
                List<Project> partTwoList = projectList.subList((projectCount / 4) + 1, projectCount/2);
                List<Project> partThreeList = projectList.subList((projectCount / 2) + 1, projectCount);
                ProjectWorker projectWorker1 = new ProjectWorker(partOneList, allBusinessAndProjects,"projectWorker1");
                ProjectWorker projectWorker2 = new ProjectWorker(partTwoList, allBusinessAndProjects,"projectWorker2");
                ProjectWorker projectWorker3 = new ProjectWorker(partThreeList, allBusinessAndProjects,"projectWorker3");


                projectWorker1.start();
                projectWorker2.start();
                projectWorker3.start();

                try {
                    projectWorker1.join();
                    projectWorker2.join();
                    projectWorker3.join();
                } catch (Exception ex) {
                    logger.info(ex.getMessage());
                    return null;
                }
            }

            int startIndex=(pageNo-1)*pageSize;
            int endIndex=pageNo*pageSize;
            if(CollectionUtils.isEmpty(allBusinessAndProjects)){
                return null;
            }
            if(startIndex>=allBusinessAndProjects.size()){
                return null;
            }
            if(endIndex>allBusinessAndProjects.size()-1){
                endIndex=allBusinessAndProjects.size()-1;
            }

            return allBusinessAndProjects.subList(startIndex,endIndex);


            //*****
            //return null;

        }

        //如果没输入业务名，输入了项目名
        else if (StringUtils.isEmpty(businName) && StringUtils.isNotEmpty(projName)){
            List<String > businessIdList=new LinkedList<>(allBusinessIds);
            List<String>  projectIdList=new LinkedList<>(allProjectIds);

            List<BudgetEO> budgetEOList = budgetEOService.selectByPrimaryKeys(businessIdList);
            for(BudgetEO budgetEO : budgetEOList) {
                if (budgetEO == null) continue;
                if(budgetEO.getProjectName().startsWith("旧"))continue;
                String businessName = budgetEO.getProjectName();
                if(businessName.equals(businName))continue;
                String businessId = budgetEO.getId();
                Map<String, String> business = new HashMap<>();
                business.put("id", businessId);
                business.put("name", businessName);
                business.put("type", "1");
                if(!mapExistsInMapList(allBusinessAndProjects,business)){
                    allBusinessAndProjects.add(business);
                    numberOfBusinessAndProjects.getAndIncrement();
                }
            }

            List<Project> projectList = projectRepository.findByIdIn(allProjectIds);
            if(StringUtils.isNotEmpty(projectIdList)) {
                for(Project proj: projectList){
                    String projId=proj.getId();
                    String businId=proj.getBudgetId();
                    String projectName=proj.getName();
                    if(StringUtils.isEmpty(projId)||StringUtils.isEmpty(businId)||!projectName.equals(projName))continue;
                    projectIdBusinessIdMap.put(projId,businId);
                }
                int projectCount = projectList.size();
                List<Project> partOneList = projectList.subList(0, projectCount / 4);
                List<Project> partTwoList = projectList.subList((projectCount / 4) + 1, projectCount/2);
                List<Project> partThreeList = projectList.subList((projectCount / 2) + 1, projectCount);
                ProjectWorker projectWorker1 = new ProjectWorker(partOneList, allBusinessAndProjects,"projectWorker1");
                ProjectWorker projectWorker2 = new ProjectWorker(partTwoList, allBusinessAndProjects,"projectWorker2");
                ProjectWorker projectWorker3 = new ProjectWorker(partThreeList, allBusinessAndProjects,"projectWorker3");



                projectWorker1.start();
                projectWorker2.start();
                projectWorker3.start();

                try {
                    projectWorker1.join();
                    projectWorker2.join();
                    projectWorker3.join();
                } catch (Exception ex) {
                    logger.info(ex.getMessage());
                    return null;
                }
            }

            int startIndex=(pageNo-1)*pageSize;
            int endIndex=pageNo*pageSize;
            if(CollectionUtils.isEmpty(allBusinessAndProjects)){
                return null;
            }
            if(startIndex>=allBusinessAndProjects.size()){
                return null;
            }
            if(endIndex>allBusinessAndProjects.size()-1){
                endIndex=allBusinessAndProjects.size()-1;
            }

            return allBusinessAndProjects.subList(startIndex,endIndex);


            //*****
            //return null;

        }

        //输入了业务名，也输入了项目名
        else {
            List<String > businessIdList=new LinkedList<>(allBusinessIds);
            List<String>  projectIdList=new LinkedList<>(allProjectIds);

            List<BudgetEO> budgetEOList = budgetEOService.selectByPrimaryKeys(businessIdList);
            for(BudgetEO budgetEO : budgetEOList) {
                if (budgetEO == null) continue;
                if(budgetEO.getProjectName().startsWith("旧"))continue;
                String businessName = budgetEO.getProjectName();
                if(!businessName.equals(businName))continue;
                String businessId = budgetEO.getId();
                Map<String, String> business = new HashMap<>();
                business.put("id", businessId);
                business.put("name", businessName);
                business.put("type", "1");
                if(!mapExistsInMapList(allBusinessAndProjects,business)){
                    allBusinessAndProjects.add(business);
                    numberOfBusinessAndProjects.getAndIncrement();
                }
            }

            List<Project> projectList = projectRepository.findByIdIn(allProjectIds);
            if(StringUtils.isNotEmpty(projectIdList)) {
                for(Project proj: projectList){
                    String projId=proj.getId();
                    String businId=proj.getBudgetId();
                    String projectName=proj.getName();
                    if(StringUtils.isEmpty(projId)||StringUtils.isEmpty(businId)||!projectName.equals(projName))continue;
                    projectIdBusinessIdMap.put(projId,businId);
                }
                int projectCount = projectList.size();
                List<Project> partOneList = projectList.subList(0, projectCount / 4);
                List<Project> partTwoList = projectList.subList((projectCount / 4) + 1, projectCount/2);
                List<Project> partThreeList = projectList.subList((projectCount / 2) + 1, projectCount);
                ProjectWorker projectWorker1 = new ProjectWorker(partOneList, allBusinessAndProjects,"projectWorker1");
                ProjectWorker projectWorker2 = new ProjectWorker(partTwoList, allBusinessAndProjects,"projectWorker2");
                ProjectWorker projectWorker3 = new ProjectWorker(partThreeList, allBusinessAndProjects,"projectWorker3");


                projectWorker1.start();
                projectWorker2.start();
                projectWorker3.start();

                try {
                    projectWorker1.join();
                    projectWorker2.join();
                    projectWorker3.join();
                } catch (Exception ex) {
                    logger.info(ex.getMessage());
                    return null;
                }
            }

            int startIndex=(pageNo-1)*pageSize;
            int endIndex=pageNo*pageSize;
            if(CollectionUtils.isEmpty(allBusinessAndProjects)){
                return null;
            }
            if(startIndex>=allBusinessAndProjects.size()){
                return null;
            }
            if(endIndex>allBusinessAndProjects.size()-1){
                endIndex=allBusinessAndProjects.size()-1;
            }

            return allBusinessAndProjects.subList(startIndex,endIndex);


            //*****
            //return null;

        }

    }




    public  Map<String,List<JsonObject>> queryUsersDailyNew(List<String> userIds,List<Map<String,String>> businessAndProjectsMap,String startDate,String endDate) {

        JsonObject jsonObject = new JsonObject();

        //定义业务名称列表
        List<String> businList=new LinkedList<>();
        //定义项目名称列表
        List<String> projList=new LinkedList<>();

        //遍历Map,填充业务名称列表和项目名称列表
        for(Map<String,String> businessOrProject : businessAndProjectsMap){
            if(businessOrProject.get("type").equals("1")){
                businList.add(businessOrProject.get("name"));
            } else {
                projList.add(businessOrProject.get("name"));
            }
        }


        Date date1 = null;
        Date date2 = null;

        if(StringUtils.isNotEmpty(startDate)&&StringUtils.isNotEmpty(endDate)) {
            try {
                date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startDate + " 0:0:0");
                date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endDate + " 23:59:59");
            } catch (Exception ex) {
                throw new AdcDaBaseException("请按正确的日期格式（yyyy-MM-dd)传递日期参数");
            }
        }

        else {
            date2=new Date();
            Calendar calendar=Calendar.getInstance();
            calendar.setTime(date2);


            calendar.add(Calendar.DATE,-180);
            date1=calendar.getTime();
        }

        int[] approveStateArr = {1, 2};
        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .mustNot(QueryBuilders.termQuery(DEL_FLAG, true))
                // .must(QueryBuilders.rangeQuery("dailyCreateTime").from(startTimestamp).to(endTimestamp))
                .must(QueryBuilders.termsQuery("approveState", approveStateArr))
                .must(QueryBuilders.existsQuery("workCostTime"))
                .must(QueryBuilders.termsQuery("createUserId",getMemberIdListOfSoftwareDevelopmentDepartment()))
                //.must(QueryBuilders.termsQuery("createUserId", userIds))
                ;
        //按时间条件进行查询过滤
        if(date1!=null&&date2!=null){

            long startTimestamp = date1.getTime();
            long endTimestamp = date2.getTime();
            ((BoolQueryBuilder) queryBuilder).must(QueryBuilders.rangeQuery("dailyCreateTime").from(startTimestamp).to(endTimestamp));
        }

        if(CollectionUtils.isNotEmpty(userIds)){
            ((BoolQueryBuilder) queryBuilder).must(QueryBuilders.termsQuery("createUserId",userIds));
        }

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        BoolQueryBuilder bqr1=QueryBuilders.boolQuery().must(QueryBuilders.termsQuery("projectName",projList));
        BoolQueryBuilder bqr2=QueryBuilders.boolQuery().must(QueryBuilders.termsQuery("budgetName",businList));
        boolQueryBuilder.should(bqr1);
        boolQueryBuilder.should(bqr2);


        //按业务和项目名称进行查询
        ((BoolQueryBuilder) queryBuilder).must(boolQueryBuilder);
        logger.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println(queryBuilder.toString());
        logger.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");



        //查出满足条件的日报
        Page<Daily> dailyList = dailyRepository.search(queryBuilder, new PageRequest(0, 99999));


        //定义返回结果
        Map<String,List<JsonObject>> result=new HashMap<>();

        //保存员工业务信息
        List<JsonObject> empBusiness = new LinkedList<>();

        //保存员工项目信息
        List<JsonObject> empProjects = new LinkedList<>();


        /*
        //遍历日报
        for (Daily daily : dailyList) {
            String empId = daily.getCreateUserId();
            String empName = daily.getCreateUserName();


            //遍历任务数组
            String[] taskIdArray = daily.getTaskIdArray();
            if (taskIdArray != null) {
                for (int i = 0; i < taskIdArray.length; i++) {

                    JsonObject obj = getTaskById(taskIdArray[i]);
                    obj.addProperty("empId",empId);
                    obj.addProperty("empName",empName);

                    String businessOrProjectName=obj.get("name").getAsString();
                    String objType=obj.get("type").getAsString();
                    //如果是业务
                    if (objType.equals("1")) {
                        if(!jsonObjectExistsInJsonObjectList(empBusiness,obj)){
                            empBusiness.add(obj);
                        }
                    }

                    //如果是项目
                    if (objType.equals("2")) {
                        if(!jsonObjectExistsInJsonObjectList(empProjects,obj)){
                            empProjects.add(obj);
                        }
                    }
                } //taskIdArray

            }
        }
        */

        ExecutorService executorService=new ThreadPoolExecutor(8,8,5000,TimeUnit.SECONDS,new LinkedBlockingDeque<>(10000));


        for(Daily daily: dailyList){
            String empId = daily.getCreateUserId();
            String empName = daily.getCreateUserName();

            //遍历任务数组
            String[] taskIdArray = daily.getTaskIdArray();

            executorService.execute(new TaskWorker(empBusiness,empProjects,empId,empName,taskIdArray));

        }

        ThreadPoolExecutor pool=(ThreadPoolExecutor)executorService;
        while(pool.getActiveCount()>0){
            try {
                Thread.sleep(100);
            }catch (Exception ex){

            }
        }

        executorService.shutdown();

        //List<Map<String,String>> userIdTaskId=new LinkedList<>();
        result.put("business",empBusiness);
        result.put("projects",empProjects);


        //返回结果
        return result;
    }

    //日报分布，给controller层调用(New)
    public String getDailyDistributionNew(List<String> userIds,List<String> deptIds,String startDate,String endDate,String businName,String projName,int pageSize,int pageNo) {
        Set<String> allUserIdSet=new HashSet<>();
        //如果不传入ID,则把软件开发部的员工加到查询
        if(CollectionUtils.isEmpty(userIds)){
            allUserIdSet.addAll(getMemberIdListOfSoftwareDevelopmentDepartment());
        }
        else {
            allUserIdSet.addAll(userIds);
        }
        //如果传入了部门Id,则把部门人员添加到查询
        if(CollectionUtils.isNotEmpty(deptIds)){
            for(String deptId: deptIds)
            {
                List<String> ids=getMemberIdListOfDept(deptId);
                allUserIdSet.addAll(ids);
            }
        }

        List<String> allUserIdList=new LinkedList<>(allUserIdSet);

        List<Map<String,String>> businessAndProjectsMap=getBusinessAndProjectsByEmpIdsPage(allUserIdSet,pageSize,pageNo,businName,projName);
        if(CollectionUtils.isEmpty(businessAndProjectsMap)){
            logger.info("没有找到业务和项目信息");
            throw new AdcDaBaseException("没有找到业务和项目信息");
        }

        Map<String,List<JsonObject>> tasks=queryUsersDailyNew(allUserIdList,businessAndProjectsMap,startDate,endDate);

        List<JsonObject> empBusiness=tasks.get("business");
        List<JsonObject> empProjects=tasks.get("projects");
        logger.info("*******************************************emp business*****************************  "+empBusiness.size());
        logger.info("*******************************************emp projects*****************************  "+empProjects.size());


        List<UserEO> userEOList=userEODao.getUserWithRolesByUserIds(allUserIdList);
        List<JsonObject> empIdEmpName=new LinkedList<>();
        for(UserEO userEO: userEOList){
            JsonObject emp=new JsonObject();
            emp.addProperty("empId",userEO.getUsid());
            emp.addProperty("empName",userEO.getUsname());
            empIdEmpName.add(emp);
        }

        //定义返回的JsonArray
        JsonArray jsonArray=new JsonArray();
        JsonArray headerArray=new JsonArray();
        JsonArray dataArray=new JsonArray();
        JsonObject countObject=new JsonObject();


        //表头
        String sequence=" { type: 'numbers', title: '序号', width: '60px' }";
        String title=" {field: 'name',  title: '业务或项目列表', width: '600px', status: 'project'  }";
        JsonObject sequenceObject=new JsonParser().parse(sequence).getAsJsonObject();
        headerArray.add(sequenceObject);
        JsonObject titleObject=new JsonParser().parse(title).getAsJsonObject();
        headerArray.add(titleObject);


        //表头，遍历员工列表
        for(JsonObject jsonObject:empIdEmpName){
            String empId=jsonObject.get("empId").getAsString();
            String empName=jsonObject.get("empName").getAsString();
            String empString="{        field: '" +
                    empId +
                    "',\n" +
                    "            title: '" +
                    empName +
                    "',\n" +
                    "            align: 'center',\n" +
                    "            width: '120px', \n" +
                    "            status:'people' }";
            JsonObject emp=new JsonParser().parse(empString).getAsJsonObject();
            headerArray.add(emp);
        }

        //遍历所有业务和项目
        for(Map<String,String> map: businessAndProjectsMap){
            String businessOrProjectId=map.get("id");
            String type=map.get("type");
            String name=map.get("name");

            String businessJsonString="";
            String projectJsonString="";

            //当前为业务
            if(type.equals("1")){
                JsonObject tempJsonObject=new JsonObject();
                tempJsonObject.addProperty("id",businessOrProjectId);
                tempJsonObject.addProperty("name",name);
                tempJsonObject.addProperty("parentId",0);
                tempJsonObject.addProperty("type",1);
                for(JsonObject empJsonObject: empIdEmpName){
                    String empIdTemp=empJsonObject.get("empId").getAsString();
                    //String propertyName=empIdTemp+"ProjectTaskStatus";
                    String propertyName=empIdTemp+"";
                    if(CollectionUtils.isEmpty(empBusiness)){
                        tempJsonObject.addProperty(propertyName,"null");
                    }

                    else
                    {
                        //定义标志判断用户是否在该业务
                        int flag=0;
                        String empId=empJsonObject.get("empId").getAsString();
                        for(JsonObject businessObject: empBusiness){
                            if(businessObject.get("id").getAsString().equals(businessOrProjectId)&&businessObject.get("empId").getAsString().equals(empId)){
                                flag=1;
                                break;
                            }
                        }

                        if(flag==1){
                            tempJsonObject.addProperty(propertyName,"1");
                        }
                        else{
                            tempJsonObject.addProperty(propertyName,"null");
                        }
                    }

                }


                //JsonObject  tempBusinessJsonObject=new JsonParser().parse(businessJsonString).getAsJsonObject();
                dataArray.add(tempJsonObject);
            }//当前为业务

            //当前为项目
            if(type.equals("2")){
                JsonObject tempJsonObject=new JsonObject();
                String businId=projectIdBusinessIdMap.get(businessOrProjectId);
                if(StringUtils.isEmpty(businId))continue;

                tempJsonObject.addProperty("id",businessOrProjectId);
                tempJsonObject.addProperty("name",name);
                tempJsonObject.addProperty("parentId",businId);
                tempJsonObject.addProperty("type",2);

                String empInfo="";
                //遍历所有人
                for(JsonObject empJsonObject: empIdEmpName){
                    String empIdTemp=empJsonObject.get("empId").getAsString();
                    //String propertyName=empIdTemp+"ProjectTaskStatus";
                    String propertyName=empIdTemp+"";
                    if(CollectionUtils.isEmpty(empProjects)){
                        tempJsonObject.addProperty(propertyName,"null");
                    }
                    //遍历empBusiness
                    else
                    {
                        //定义标志判断用户是否在该项目
                        int flag=0;
                        String empId=empJsonObject.get("empId").getAsString();
                        for(JsonObject projectObject: empProjects){
                            if(projectObject.get("id").getAsString().equals(businessOrProjectId)&&projectObject.get("empId").getAsString().equals(empId)){
                                flag=1;
                                break;
                            }
                        }

                        if(flag==1){
                            tempJsonObject.addProperty(propertyName,"2");
                        }
                        else{
                            tempJsonObject.addProperty(propertyName,"null");
                        }

                    }
                }//遍历所有人


                //JsonObject  tempProjectJsonObject=new JsonParser().parse(projectJsonString).getAsJsonObject();
                dataArray.add(tempJsonObject);
            }//当前为项目



        }

        countObject.addProperty("count",numberOfBusinessAndProjects.intValue());
        jsonArray.add(headerArray);
        jsonArray.add(dataArray);
        jsonArray.add(countObject);


        String result=jsonArray.toString();
        numberOfBusinessAndProjects.set(0);
        return result;

    }

    public  Map<String,List<JsonObject>> queryUsersDailyForExcelExport(List<String> userIds,List<Map<String,String>> businessAndProjectsMap,String startDate,String endDate) {

        if(StringUtils.isEmpty(businessAndProjectsMap))return null;
        JsonObject jsonObject = new JsonObject();


        Map<String,Set<String>> userTasks=new HashMap<>();
        for(String userId: userIds){
            userTasks.put(userId,new HashSet<>());
        }
        //定义业务名称列表
        List<String> businList=new LinkedList<>();
        //定义项目名称列表
        List<String> projList=new LinkedList<>();

        //遍历Map,填充业务名称列表和项目名称列表
        for(Map<String,String> businessOrProject : businessAndProjectsMap){
            if(businessOrProject.get("type").equals("1")){
                businList.add(businessOrProject.get("name"));
            } else {
                projList.add(businessOrProject.get("name"));
            }
        }


        Date date1 = null;
        Date date2 = null;

        if(StringUtils.isNotEmpty(startDate)&&StringUtils.isNotEmpty(endDate)) {
            try {
                date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startDate + " 0:0:0");
                date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endDate + " 23:59:59");
            } catch (Exception ex) {
                throw new AdcDaBaseException("请按正确的日期格式（yyyy-MM-dd)传递日期参数");
            }
        }

        else {
            date2=new Date();
            Calendar calendar=Calendar.getInstance();
            calendar.setTime(date2);


            calendar.add(Calendar.DATE,-180);
            date1=calendar.getTime();
        }

        int[] approveStateArr = {1, 2};
        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .mustNot(QueryBuilders.termQuery(DEL_FLAG, true))
                // .must(QueryBuilders.rangeQuery("dailyCreateTime").from(startTimestamp).to(endTimestamp))
                .must(QueryBuilders.termsQuery("approveState", approveStateArr))
                .must(QueryBuilders.existsQuery("workCostTime"))
                .must(QueryBuilders.termsQuery("createUserId",getMemberIdListOfSoftwareDevelopmentDepartment()))
                //.must(QueryBuilders.termsQuery("createUserId", userIds))
                ;
        //按时间条件进行查询过滤
        if(date1!=null&&date2!=null){

            long startTimestamp = date1.getTime();
            long endTimestamp = date2.getTime();
            ((BoolQueryBuilder) queryBuilder).must(QueryBuilders.rangeQuery("dailyCreateTime").from(startTimestamp).to(endTimestamp));
        }

        if(CollectionUtils.isNotEmpty(userIds)){
            ((BoolQueryBuilder) queryBuilder).must(QueryBuilders.termsQuery("createUserId",userIds));
        }

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        BoolQueryBuilder bqr1=QueryBuilders.boolQuery().must(QueryBuilders.termsQuery("projectName",projList));
        BoolQueryBuilder bqr2=QueryBuilders.boolQuery().must(QueryBuilders.termsQuery("budgetName",businList));
        boolQueryBuilder.should(bqr1);
        boolQueryBuilder.should(bqr2);


        //按业务和项目名称进行查询
        ((BoolQueryBuilder) queryBuilder).must(boolQueryBuilder);


        //查出满足条件的日报
        Page<Daily> dailyList = dailyRepository.search(queryBuilder, new PageRequest(0, 99999));


        //定义返回结果
        Map<String,List<JsonObject>> result=new HashMap<>();

        //保存员工业务信息
        List<JsonObject> empBusiness = new LinkedList<>();

        //保存员工项目信息
        List<JsonObject> empProjects = new LinkedList<>();


        //遍历日报
        for (Daily daily : dailyList) {
            String empId = daily.getCreateUserId();
            String empName = daily.getCreateUserName();


            //遍历任务数组
            String[] taskIdArray = daily.getTaskIdArray();
            if (taskIdArray != null) {
                for (int i = 0; i < taskIdArray.length; i++) {


                    JsonObject obj = getTaskById(taskIdArray[i]);
                    obj.addProperty("empId",empId);
                    obj.addProperty("empName",empName);

                    String businessOrProjectName=obj.get("name").getAsString();
                    String objType=obj.get("type").getAsString();
                    //如果是业务
                    if (objType.equals("1")) {
                        if(!jsonObjectExistsInJsonObjectList(empBusiness,obj)){
                            empBusiness.add(obj);
                        }
                    }

                    //如果是项目
                    if (objType.equals("2")) {
                        if(!jsonObjectExistsInJsonObjectList(empProjects,obj)){
                            empProjects.add(obj);
                        }
                    }
                } //taskIdArray

            }
        }

        //List<Map<String,String>> userIdTaskId=new LinkedList<>();
        result.put("business",empBusiness);
        result.put("projects",empProjects);


        //返回结果
        return result;
    }


    //日报分布导出，给controller层调用(New)
    public Workbook getDailyDistributionExcel(List<String> userIds,List<String> deptIds, String startDate, String endDate,String businName,String projName){

        Set<String> allUserIdSet=new HashSet<>();
        //如果不传入ID,则把软件开发部的员工加到查询
        if(CollectionUtils.isEmpty(userIds)){
            allUserIdSet.addAll(getMemberIdListOfSoftwareDevelopmentDepartment());
        }
        //如果传入了部门Id,则把部门人员添加到查询
        if(CollectionUtils.isNotEmpty(deptIds)){
            for(String deptId: deptIds)
            {
                List<String> ids=getMemberIdListOfDept(deptId);
                allUserIdSet.addAll(ids);
            }
        }

        List<String> allUserIdList=new LinkedList<>(allUserIdSet);

        List<Map<String,String>> businessAndProjectsMap=getBusinessAndProjectsByEmpIds(allUserIdSet,businName,projName);


        Map<String,List<JsonObject>> tasks=queryUsersDailyForExcelExport(allUserIdList,businessAndProjectsMap,startDate,endDate);
        List<JsonObject> empBusiness=tasks.get("business");
        List<JsonObject> empProjects=tasks.get("projects");

        List<UserEO> userEOList=userEODao.getUserWithRolesByUserIds(allUserIdList);
        List<JsonObject> empIdEmpName=new LinkedList<>();
        for(UserEO userEO: userEOList){
            JsonObject emp=new JsonObject();
            emp.addProperty("empId",userEO.getUsid());
            emp.addProperty("empName",userEO.getUsname());
            empIdEmpName.add(emp);
        }

        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("工时分布");
        sheet.setColumnWidth(1,45*256);
        Row titleRow = sheet.createRow(0);
        Cell cell1=titleRow.createCell(0);
        cell1.setCellValue("序号");
        Cell cell2=titleRow.createCell(1);
        cell2.setCellValue("业务和项目列表");
        int columnIndex=2;

        for(JsonObject jsonObject:empIdEmpName) {
            Cell tempCell=titleRow.createCell(columnIndex);
            String empId = jsonObject.get("empId").getAsString();
            String empName = jsonObject.get("empName").getAsString();
            tempCell.setCellValue(empName);
            columnIndex++;
        }

        int rowIndex=1;


        //遍历所有业务和项目
        for(Map<String,String> map: businessAndProjectsMap){
            columnIndex=0;
            String businessOrProjectId=map.get("id");
            String type=map.get("type");
            String name=map.get("name");
            Row aBusinessOrProject=sheet.createRow(rowIndex);
            Cell indexCell=aBusinessOrProject.createCell(columnIndex);
            indexCell.setCellValue(rowIndex);
            columnIndex++;
            Cell nameCell=aBusinessOrProject.createCell(columnIndex);
            nameCell.setCellValue(name);
            columnIndex++;


            //当前为业务
            if(type.equals("1")){
                //遍历所有人
                for(JsonObject empJsonObject: empIdEmpName){
                    String empIdTemp=empJsonObject.get("empId").getAsString();
                    //该用户没在该业务
                    if(CollectionUtils.isEmpty(empBusiness)){
                        Cell empCell=aBusinessOrProject.createCell(columnIndex);
                        empCell.setCellValue("");
                        columnIndex++;
                    }
                    //遍历empBusiness
                    else
                    {
                        //定义标志判断用户是否在该业务
                        int flag=0;
                        String empId=empJsonObject.get("empId").getAsString();
                        for(JsonObject businessObject: empBusiness){
                            if(businessObject.get("id").getAsString().equals(businessOrProjectId)&&businessObject.get("empId").getAsString().equals(empId)){
                                flag=1;
                                break;
                            }
                        }

                        if(flag==1){
                            Cell empCell=aBusinessOrProject.createCell(columnIndex);
                            empCell.setCellValue("■");

                            columnIndex++;
                        }

                        if(flag==0){
                            Cell empCell=aBusinessOrProject.createCell(columnIndex);
                            empCell.setCellValue("");
                            columnIndex++;
                        }

                    }
                }//遍历所有人

            }//当前为业务



            //当前为项目
            if(type.equals("2")){
                //遍历所有人
                for(JsonObject empJsonObject: empIdEmpName){
                    String empIdTemp=empJsonObject.get("empId").getAsString();
                    //该用户没在该项目
                    if(CollectionUtils.isEmpty(empProjects)){
                        Cell empCell=aBusinessOrProject.createCell(columnIndex);
                        empCell.setCellValue("");
                        columnIndex++;
                    }
                    //遍历empProjects
                    else
                    {
                        //定义标志判断用户是否在该项目
                        int flag=0;
                        String empId=empJsonObject.get("empId").getAsString();
                        for(JsonObject projectObject: empProjects){
                            if(projectObject.get("id").getAsString().equals(businessOrProjectId)&&projectObject.get("empId").getAsString().equals(empId)){
                                flag=1;
                                break;
                            }
                        }

                        if(flag==1){
                            Cell empCell=aBusinessOrProject.createCell(columnIndex);
                            empCell.setCellValue("●");

                            columnIndex++;
                        }

                        if(flag==0){
                            Cell empCell=aBusinessOrProject.createCell(columnIndex);
                            empCell.setCellValue("");
                            columnIndex++;
                        }
                    }
                }//遍历所有人

            }//当前为项目

            rowIndex++;
        }

        return workbook;
    }

    //日报分布，给controller层调用(New)
    public String getDailyDistributionNewTemp(List<String> userIds,List<String> deptIds,long startDate,long endDate,String businName,String projName,int pageSize,int pageNo) {
        if (CollectionUtils.isEmpty(userIds) && CollectionUtils.isEmpty(deptIds)){
            throw  new AdcDaBaseException("请选择人员！");
        }
        if (CollectionUtils.isNotEmpty(deptIds)) {
            if (CollectionUtils.isEmpty(userIds)){
                userIds = new ArrayList<>();
            }
            List<UserEO> userEOList = userEODao.getAllUserEOsByOrgIds(deptIds);
            for (UserEO userEO : userEOList){
                userIds.add(userEO.getUsid());
            }
        }

        List<UserWithProjects> userWithProjectsList = userWithProjectsRepository.findByUserIdIn(new HashSet<>(userIds));
        Set<String> budgetIdSet = new HashSet<>();
        Set<String> projectIdSet = new HashSet<>();
        Set<String> taskIdSet = new HashSet<>();
        Set<String> childTaskIdSet = new HashSet<>();
        for (UserWithProjects userWithProjects : userWithProjectsList){
            budgetIdSet.addAll(userWithProjects.getBusinessIds());
            projectIdSet.addAll(userWithProjects.getProjectIds());
//            taskIdSet.addAll(userWithProjects.getTaskIds());
//            childTaskIdSet.addAll(userWithProjects.getChildTaskIds());
        }
        budgetIdSet.removeAll(Collections.singleton(null));
        projectIdSet.removeAll(Collections.singleton(null));
//        taskIdSet.removeAll(Collections.singleton(null));
//        childTaskIdSet.removeAll(Collections.singleton(null));

        List<BudgetEO> allBudgetEOList = budgetEOService.findByIds(budgetIdSet);
        for (BudgetEO budgetEO : allBudgetEOList){
            if (!StringUtils.equals(budgetEO.getProperty(),"0")){
                allBudgetEOList.remove(budgetEO);
                budgetIdSet.remove(budgetEO.getId());
            }
        }
        List<Project> allProjectList = projectRepository.findByBudgetIdIn(budgetIdSet);
        for (Project project : allProjectList){
            if (project.getProjectType()!=0){
                allProjectList.remove(project);
                projectIdSet.remove(project.getId());
            }
        }
        List<Task> taskList1 = taskRepository.findByProjectIdIn(projectIdSet);
        List<Task> taskList2 = taskRepository.findByBudgetIdIn(budgetIdSet);
        taskList1.addAll(taskList2);
        for (Task task : taskList1){
            taskIdSet.add(task.getId());
        }
        List<ChildrenTask> childrenTaskList = childTaskRepository.findByTaskIdIn(taskIdSet);
        for (ChildrenTask childrenTask : childrenTaskList){
            childTaskIdSet.add(childrenTask.getId());
        }
        childTaskIdSet.addAll(taskIdSet);
        //创建查询工具类，然后按照条件查询，可以分开也可以合在一起写
        int[] approveStateArr = {1, 2};
        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .mustNot(QueryBuilders.termQuery(DEL_FLAG, true))
                .must(QueryBuilders.rangeQuery("dailyCreateTime").from(startDate).to(endDate))
                .must(QueryBuilders.termsQuery("approveState", approveStateArr))
                .must(QueryBuilders.existsQuery("workCostTime"))
                .must(QueryBuilders.termsQuery("createUserId",getMemberIdListOfSoftwareDevelopmentDepartment()));

        //构建查询
        NativeSearchQueryBuilder nativeSearchQueryBuilder =
                new NativeSearchQueryBuilder().withQuery(queryBuilder);
        // 不查询任何结果
        nativeSearchQueryBuilder.withSourceFilter(new FetchSourceFilter(new String[] { "" }, null));
        return null;
    }

    public String getDailyDistributionNewTem(List<String> userIds,List<String> deptIds,String startDate,String endDate,String businName,String projName,int pageSize,int pageNo) {
        DateTime dateTime = new DateTime();
        long endTime = dateTime.getMillis();
        long startTime = dateTime.minusDays(60).getMillis();
        if(StringUtils.isNotEmpty(startDate)&&StringUtils.isNotEmpty(endDate)) {
            try {
                startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startDate + " 0:0:0").getTime();
                endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endDate + " 23:59:59").getTime();
            } catch (Exception ex) {
                throw new AdcDaBaseException("请按正确的日期格式（yyyy-MM-dd)传递日期参数");
            }
        }
        //定义返回的JsonArray
        JsonArray jsonArray=new JsonArray();
        JsonArray headerArray=new JsonArray();
        JsonArray dataArray=new JsonArray();
        JsonObject countObject=new JsonObject();
        //表头
        String sequence=" { type: 'numbers', title: '序号', width: '60px' }";
        String title=" {field: 'name',  title: '业务或项目列表', width: '600px', status: 'project'  }";
        JsonObject sequenceObject=new JsonParser().parse(sequence).getAsJsonObject();
        headerArray.add(sequenceObject);
        JsonObject titleObject=new JsonParser().parse(title).getAsJsonObject();
        headerArray.add(titleObject);
        Set<String> allUserIdSet=new HashSet<>();
        //如果不传入ID,则把软件开发部的员工加到查询
        if(CollectionUtils.isEmpty(userIds)){
            allUserIdSet.addAll(getMemberIdListOfSoftwareDevelopmentDepartment());
        } else {
            allUserIdSet.addAll(userIds);
        }
        //如果传入了部门Id,则把部门人员添加到查询
        if(CollectionUtils.isNotEmpty(deptIds)){
            for(String deptId: deptIds) {
                List<String> ids=getMemberIdListOfDept(deptId);
                allUserIdSet.addAll(ids);
            }
        }
        List<String> allUserIdList=new LinkedList<>(allUserIdSet);
        String[] property = {"0","1"};
        List<String> budgetIdList = budgetEOService.getDao().findAllBudgetIdByNameNotLikeAndPropertyArr("旧-%", property);
        //构造json表头
        List<UserEO> userEOList = userEODao.getUserWithRolesByUserIds(allUserIdList);
        Collections.sort(userEOList, new Comparator<UserEO>() {
            @Override
            public int compare(UserEO o1, UserEO o2) {
                if(StringUtils.isEmpty(o2.getExtInfo())||StringUtils.isEmpty(o1.getExtInfo())){
                    return 0;
                }
                return Integer.parseInt(o2.getExtInfo())-Integer.parseInt(o1.getExtInfo());
            }
        });
        List<JsonObject> userIdNameJsonList = new LinkedList<>();
        List<String> userIdList = new LinkedList<>();
        for (UserEO userEO : userEOList) {
            JsonObject userJson = new JsonObject();
            userJson.addProperty("userId", userEO.getUsid());
            userJson.addProperty("userName", userEO.getUsname());
            userIdNameJsonList.add(userJson);
            userIdList.add(userEO.getUsid());
        }
        //表头，遍历员工列表
        for (JsonObject userIdNameJson : userIdNameJsonList) {
            String userId = userIdNameJson.get("userId").getAsString();
            String userName = userIdNameJson.get("userName").getAsString();
            String headerString = "{        field: '" +
                    userId +
                    "',\n" +
                    "            title: '" +
                    userName +
                    "',\n" +
                    "            align: 'center',\n" +
                    "            width: '80px', \n" +
                    "            status:'people' }";
            JsonObject headerJson = new JsonParser().parse(headerString).getAsJsonObject();
            headerArray.add(headerJson);
        }
        int[] approveStateArr = {1, 2};
        BoolQueryBuilder queryBuilderFilter = QueryBuilders.boolQuery()
                .mustNot(QueryBuilders.termQuery(DEL_FLAG, true))
                .must(QueryBuilders.rangeQuery("dailyCreateTime").from(startTime).to(endTime))
                .must(QueryBuilders.termsQuery("approveState", approveStateArr))
                .must(QueryBuilders.existsQuery("workCostTime"))
                .must(QueryBuilders.termsQuery("createUserId",allUserIdSet));
        if (StringUtils.isNotEmpty(projName)){
            queryBuilderFilter.must(QueryBuilders.wildcardQuery("projectNameES","*"+projName+"*"));
        }
        if (StringUtils.isNotEmpty(businName)){
            queryBuilderFilter.must(QueryBuilders.wildcardQuery("budgetNameES","*"+businName+"*"));
        }
        //构建查询
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder().withQuery(queryBuilderFilter);
        // 不查询任何结果
        nativeSearchQueryBuilder.withSourceFilter(new FetchSourceFilter(new String[] { "" }, null));
        TermsBuilder dailyAgg = AggregationBuilders.terms("budgetId").field("budgetId").size(10000);
        nativeSearchQueryBuilder.addAggregation(dailyAgg);
        AggregatedPage<Daily> queryPage = (AggregatedPage<Daily>) this.dailyRepository.search(nativeSearchQueryBuilder.build());
        StringTerms agg = (StringTerms) queryPage.getAggregation("budgetId");
        List<Terms.Bucket> buckets = agg.getBuckets();
        List<String> allBugetIdList = new ArrayList<>();
        for (Terms.Bucket bucket : buckets) {
            allBugetIdList.add(bucket.getKeyAsString());
        }
        List<String> searchBudgetIdList = new ArrayList<>();
        for (String bid : allBugetIdList){
            if (budgetIdList.contains(bid)){
                searchBudgetIdList.add(bid);
            }
        }
        int pageCount = searchBudgetIdList.size();
        List<String> budgetPageList = new ArrayList<>();
        if (pageNo < 0 || pageSize < 0){
            throw new AdcDaBaseException("页码错误！");
        }else {
            pageNo = pageNo - 1 ;
        }
        int indexStart = pageSize * pageNo;
        int indexEnd = pageSize * pageNo + pageSize > searchBudgetIdList.size()? searchBudgetIdList.size():pageSize * pageNo + pageSize;
        if (searchBudgetIdList.size() >= indexEnd) {
            budgetPageList = searchBudgetIdList.subList(indexStart, indexEnd);
        } else {
            jsonArray.toString();
        }
        List<ListTreeVO> treeList = new ArrayList<>();
        // 分组去重
        BoolQueryBuilder queryBuilderGroup = QueryBuilders.boolQuery()
                .must(QueryBuilders.rangeQuery("dailyCreateTime").from(startTime).to(endTime))
                .mustNot(QueryBuilders.termQuery(DEL_FLAG, true))
                .must(QueryBuilders.termsQuery("approveState", approveStateArr))
                .must(QueryBuilders.existsQuery("workCostTime"))
                .must(QueryBuilders.termsQuery("createUserId",allUserIdSet))
                .must(QueryBuilders.termsQuery("budgetId", budgetPageList));
        if (StringUtils.isNotEmpty(projName)){
            queryBuilderGroup.must(QueryBuilders.wildcardQuery("projectNameES","*"+projName+"*"));
        }
        if (StringUtils.isNotEmpty(businName)){
            queryBuilderGroup.must(QueryBuilders.wildcardQuery("budgetNameES","*"+businName+"*"));
        }
        //构建查询
        NativeSearchQueryBuilder nativeSearchQueryBuilderGroup = new NativeSearchQueryBuilder().withQuery(queryBuilderGroup);
        AggregationBuilder aggregationBuilder = AggregationBuilders
                .terms("projectId").script(new Script(" doc['budgetId'].values + doc['projectId'].values + doc['createUserId'].values")).size(10000)
                .subAggregation(AggregationBuilders.topHits("singleHit").setSize(1));
        nativeSearchQueryBuilderGroup.addAggregation(aggregationBuilder);
        SearchQuery searchQueryGroup = nativeSearchQueryBuilderGroup.build();
        AggregatedPage<Daily> queryPageGroup = (AggregatedPage<Daily>) this.dailyRepository.search(searchQueryGroup);
        StringTerms aggGroup = (StringTerms) queryPageGroup.getAggregation("projectId");
        List<Terms.Bucket> bucketsGroup = aggGroup.getBuckets();
        Set<String> projectIdSet = new HashSet<>();
//        // 构建业务、项目 与人员的关系表 用于反向索引
        Map<String,Set<String>> budgetIdORProjectIdAndUserIdSetMap = new HashMap();
        for (Terms.Bucket bucket : bucketsGroup) {
            TopHits singleHit = (TopHits)bucket.getAggregations().get("singleHit");
            for (SearchHit hit : singleHit.getHits()) {
                Map<String,Object> map =  hit.getSource();
                String createUserId = (String) map.get("createUserId");
                String id = (String) map.get("id");
                String projectId = (String) map.get("projectId");
                String budgetId = (String) map.get("budgetId");
                String budgetName = (String) map.get("budgetName");
                String projectName = (String) map.get("projectName");
                if(StringUtils.isNotEmpty(createUserId)&&StringUtils.isNotEmpty(id)){
                    if (StringUtils.isNotEmpty(projectId)){
                        Set<String> userIdSet = budgetIdORProjectIdAndUserIdSetMap.get(projectId);
                        if (null!= userIdSet) {
                            userIdSet.add(createUserId);
                        }else {
                            userIdSet = new HashSet<>();
                            userIdSet.add(createUserId);
                        }
                        budgetIdORProjectIdAndUserIdSetMap.put(projectId,userIdSet);
                        if(projectIdSet.add(projectId)) {
                            treeList.add(new ListTreeVO(projectId, projectName,
                                    budgetId, 1, "", ""));
                        }
                    }
                    if (StringUtils.isNotEmpty(budgetId)) {
                        if (StringUtils.isEmpty(budgetName)){
                            try {
                                BudgetEO budgetEO = budgetEOService.selectByPrimaryKey(budgetId);
                                if (null != budgetEO && StringUtils.isNotEmpty(budgetEO.getProjectName())){
                                    budgetName = budgetEO.getProjectName();
                                    final String finalBudgetName =  budgetEO.getProjectName();
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Daily daily = dailyRepository.findOne(id);
                                            daily.setBudgetName(finalBudgetName);
                                            daily.setBudgetNameES(finalBudgetName);
                                            dailyRepository.save(daily);
                                        }
                                    });
                                }
                            }catch (Exception e){
                                logger.info("查询业务失败 budgetId: " + budgetId);
                            }
                        }
                        Set<String> userIdSet = budgetIdORProjectIdAndUserIdSetMap.get(budgetId);
                        if (null != userIdSet) {
                            userIdSet.add(createUserId);
                        } else {
                            userIdSet = new HashSet<>();
                            userIdSet.add(createUserId);
                        }
                        budgetIdORProjectIdAndUserIdSetMap.put(budgetId, userIdSet);
                        if (budgetPageList.contains(budgetId)) {
                            treeList.add(new ListTreeVO(budgetId, budgetName,
                                    "0", 0, "", ""));
                            budgetPageList.remove(budgetId);
                        }
                    }
                }

            }
        }
        for(ListTreeVO listTreeVO: treeList){
            /**
             * 0 业务
             * 1 项目
             */
            //当前为业务
            Set<String> userIdSet = budgetIdORProjectIdAndUserIdSetMap.get(listTreeVO.getId());  //可以提到外面
            if(listTreeVO.getType() == 0){
                JsonObject tempJsonObject=new JsonObject();
                tempJsonObject.addProperty("id",listTreeVO.getId());
                tempJsonObject.addProperty("name",listTreeVO.getName());
                tempJsonObject.addProperty("parentId",0);
                tempJsonObject.addProperty("type",1);
                for(String userId: userIdList) {
                    String propertyName = userId + "";
                    if (CollectionUtils.isEmpty(userIdSet)) {
                        continue;
                    } else {
                        if (userIdSet.contains(userId)) {
                            tempJsonObject.addProperty(propertyName, "1");
                        }
                    }
                }
                dataArray.add(tempJsonObject);
            }else { //当前为项目
                JsonObject tempJsonObject=new JsonObject();
                tempJsonObject.addProperty("id",listTreeVO.getId());
                tempJsonObject.addProperty("name",listTreeVO.getName());
                tempJsonObject.addProperty("parentId",listTreeVO.getParentId());
                tempJsonObject.addProperty("type",2);
                for(String userId: userIdList) {
                    String propertyName = userId + "";
                    if (CollectionUtils.isEmpty(userIdSet)) {
                        continue;
                    } else {
                        if (userIdSet.contains(userId)) {
                            tempJsonObject.addProperty(propertyName, "2");
                        }
                    }
                }
                dataArray.add(tempJsonObject);
            }//当前为项目
        }
        countObject.addProperty("count",pageCount);
        jsonArray.add(headerArray);
        jsonArray.add(dataArray);
        jsonArray.add(countObject);
        return jsonArray.toString();
    }
    public Workbook exportDailyDistributionNewTem(List<String> userIds,List<String> deptIds,String startDate,String endDate,String businName,String projName) {
        DateTime dateTime = new DateTime();
        long endTime = dateTime.getMillis();
        long startTime = dateTime.minusDays(60).getMillis();
        if(StringUtils.isNotEmpty(startDate)&&StringUtils.isNotEmpty(endDate)) {
            try {
                startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startDate + " 0:0:0").getTime();
                endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endDate + " 23:59:59").getTime();
            } catch (Exception ex) {
                throw new AdcDaBaseException("请按正确的日期格式（yyyy-MM-dd)传递日期参数");
            }
        }
        Set<String> allUserIdSet=new HashSet<>();
        //如果不传入ID,则把软件开发部的员工加到查询
        if(CollectionUtils.isEmpty(userIds)){
            allUserIdSet.addAll(getMemberIdListOfSoftwareDevelopmentDepartment());
        } else {
            allUserIdSet.addAll(userIds);
        }
        //如果传入了部门Id,则把部门人员添加到查询
        if(CollectionUtils.isNotEmpty(deptIds)){
            for(String deptId: deptIds) {
                List<String> ids=getMemberIdListOfDept(deptId);
                allUserIdSet.addAll(ids);
            }
        }
        List<String> allUserIdList=new LinkedList<>(allUserIdSet);
        String[] property = {"0","1"};
        List<String> budgetIdList = budgetEOService.getDao().findAllBudgetIdByNameNotLikeAndPropertyArr("旧-%", property);
        //构造json表头
        List<UserEO> userEOList = userEODao.getUserWithRolesByUserIds(allUserIdList);
        Collections.sort(userEOList, new Comparator<UserEO>() {
            @Override
            public int compare(UserEO o1, UserEO o2) {
                if(StringUtils.isEmpty(o2.getExtInfo())||StringUtils.isEmpty(o1.getExtInfo())){
                    return 0;
                }
                return Integer.parseInt(o2.getExtInfo())-Integer.parseInt(o1.getExtInfo());
            }
        });
        List<String> userIdList = new LinkedList<>();
        // 构造excel 表头
        List<ExcelExportEntity> colList = new ArrayList<ExcelExportEntity>();
        // 构造第一列
        ExcelExportEntity firstCol = new ExcelExportEntity("业务或项目列表", "budgetOrProjectName");
        colList.add(firstCol);
        for (UserEO userEO : userEOList) {
            ExcelExportEntity colEntity = new ExcelExportEntity(userEO.getUsname(), userEO.getUsid());
            colList.add(colEntity);
            userIdList.add(userEO.getUsid());
        }
        int[] approveStateArr = {1, 2};
        BoolQueryBuilder queryBuilderFilter = QueryBuilders.boolQuery()
                .mustNot(QueryBuilders.termQuery(DEL_FLAG, true))
                .must(QueryBuilders.rangeQuery("dailyCreateTime").from(startTime).to(endTime))
                .must(QueryBuilders.termsQuery("approveState", approveStateArr))
                .must(QueryBuilders.existsQuery("workCostTime"))
                .must(QueryBuilders.termsQuery("createUserId",allUserIdSet));
        if (StringUtils.isNotEmpty(projName)){
            queryBuilderFilter.must(QueryBuilders.wildcardQuery("projectNameES","*"+projName+"*"));
        }
        if (StringUtils.isNotEmpty(businName)){
            queryBuilderFilter.must(QueryBuilders.wildcardQuery("budgetNameES","*"+businName+"*"));
        }
        //构建查询
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder().withQuery(queryBuilderFilter);
        // 不查询任何结果
        nativeSearchQueryBuilder.withSourceFilter(new FetchSourceFilter(new String[] { "" }, null));
        TermsBuilder dailyAgg = AggregationBuilders.terms("budgetId").field("budgetId").size(10000);
        nativeSearchQueryBuilder.addAggregation(dailyAgg);
        AggregatedPage<Daily> queryPage = (AggregatedPage<Daily>) this.dailyRepository.search(nativeSearchQueryBuilder.build());
        StringTerms agg = (StringTerms) queryPage.getAggregation("budgetId");
        List<Terms.Bucket> buckets = agg.getBuckets();
        List<String> allBugetIdList = new ArrayList<>();
        for (Terms.Bucket bucket : buckets) {
            allBugetIdList.add(bucket.getKeyAsString());
        }
        List<String> searchBudgetIdList = new ArrayList<>();
        for (String bid : allBugetIdList){
            if (budgetIdList.contains(bid)){
                searchBudgetIdList.add(bid);
            }
        }
        List<ListTreeVO> treeList = new ArrayList<>();
        // 分组去重
        BoolQueryBuilder queryBuilderGroup = QueryBuilders.boolQuery()
                .must(QueryBuilders.rangeQuery("dailyCreateTime").from(startTime).to(endTime))
                .mustNot(QueryBuilders.termQuery(DEL_FLAG, true))
                .must(QueryBuilders.termsQuery("approveState", approveStateArr))
                .must(QueryBuilders.existsQuery("workCostTime"))
                .must(QueryBuilders.termsQuery("createUserId",allUserIdSet))
                .must(QueryBuilders.termsQuery("budgetId", searchBudgetIdList));
        if (StringUtils.isNotEmpty(projName)){
            queryBuilderGroup.must(QueryBuilders.wildcardQuery("projectNameES","*"+projName+"*"));
        }
        if (StringUtils.isNotEmpty(businName)){
            queryBuilderGroup.must(QueryBuilders.wildcardQuery("budgetNameES","*"+businName+"*"));
        }
        //构建查询
        NativeSearchQueryBuilder nativeSearchQueryBuilderGroup = new NativeSearchQueryBuilder().withQuery(queryBuilderGroup);
        AggregationBuilder aggregationBuilder = AggregationBuilders
                .terms("projectId").script(new Script(" doc['budgetId'].values + doc['projectId'].values + doc['createUserId'].values")).size(10000)
                .subAggregation(AggregationBuilders.topHits("singleHit").setSize(1));
        nativeSearchQueryBuilderGroup.addAggregation(aggregationBuilder);
        SearchQuery searchQueryGroup = nativeSearchQueryBuilderGroup.build();
        AggregatedPage<Daily> queryPageGroup = (AggregatedPage<Daily>) this.dailyRepository.search(searchQueryGroup);
        StringTerms aggGroup = (StringTerms) queryPageGroup.getAggregation("projectId");
        List<Terms.Bucket> bucketsGroup = aggGroup.getBuckets();
        Set<String> projectIdSet = new HashSet<>();
//        // 构建业务、项目 与人员的关系表 用于反向索引
        Map<String,Set<String>> budgetIdORProjectIdAndUserIdSetMap = new HashMap();
        for (Terms.Bucket bucket : bucketsGroup) {
            TopHits singleHit = (TopHits)bucket.getAggregations().get("singleHit");
            for (SearchHit hit : singleHit.getHits()) {
                Map<String,Object> map =  hit.getSource();
                String createUserId = (String) map.get("createUserId");
                String id = (String) map.get("id");
                String projectId = (String) map.get("projectId");
                String budgetId = (String) map.get("budgetId");
                String budgetName = (String) map.get("budgetName");
                String projectName = (String) map.get("projectName");
                if(StringUtils.isNotEmpty(createUserId)&&StringUtils.isNotEmpty(id)){
                    if (StringUtils.isNotEmpty(projectId)){
                        Set<String> userIdSet = budgetIdORProjectIdAndUserIdSetMap.get(projectId);
                        if (null!= userIdSet) {
                            userIdSet.add(createUserId);
                        }else {
                            userIdSet = new HashSet<>();
                            userIdSet.add(createUserId);
                        }
                        budgetIdORProjectIdAndUserIdSetMap.put(projectId,userIdSet);
                        if(projectIdSet.add(projectId)) {
                            treeList.add(new ListTreeVO(projectId, projectName,
                                    budgetId, 1, "", ""));
                        }
                    }
                    if (StringUtils.isNotEmpty(budgetId)) {
                        if (StringUtils.isEmpty(budgetName)){
                            try {
                                BudgetEO budgetEO = budgetEOService.selectByPrimaryKey(budgetId);
                                if (null != budgetEO && StringUtils.isNotEmpty(budgetEO.getProjectName())){
                                    budgetName = budgetEO.getProjectName();
                                    final String finalBudgetName =  budgetEO.getProjectName();
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Daily daily = dailyRepository.findOne(id);
                                            daily.setBudgetName(finalBudgetName);
                                            daily.setBudgetNameES(finalBudgetName);
                                            dailyRepository.save(daily);
                                        }
                                    });
                                }
                            }catch (Exception e){
                                logger.info("查询业务失败 budgetId: " + budgetId);
                            }
                        }
                        Set<String> userIdSet = budgetIdORProjectIdAndUserIdSetMap.get(budgetId);
                        if (null != userIdSet) {
                            userIdSet.add(createUserId);
                        } else {
                            userIdSet = new HashSet<>();
                            userIdSet.add(createUserId);
                        }
                        budgetIdORProjectIdAndUserIdSetMap.put(budgetId, userIdSet);
                        if (searchBudgetIdList.contains(budgetId)) {
                            treeList.add(new ListTreeVO(budgetId, budgetName,
                                    "0", 0, "", ""));
                            searchBudgetIdList.remove(budgetId);
                        }
                    }
                }

            }
        }
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for(ListTreeVO listTreeVO: treeList){  /**0 业务 1 项目*/
            Set<String> userIdSet = budgetIdORProjectIdAndUserIdSetMap.get(listTreeVO.getId());  //可以提到外面
            Map<String, Object> dataMap = new HashMap<String, Object>();
            if(listTreeVO.getType() == 0){  //当前为业务
                dataMap.put("budgetOrProjectName",listTreeVO.getName());
                for(String userId: userIdList) {
                    if (CollectionUtils.isEmpty(userIdSet)) {
                        continue;
                    } else {
                        if (userIdSet.contains(userId)) {
                            dataMap.put(userId,"■");
                        }
                    }
                }
            }else { //当前为项目
                dataMap.put("budgetOrProjectName",listTreeVO.getName());
                for(String userId: userIdList) {
                    if (CollectionUtils.isEmpty(userIdSet)) {
                        continue;
                    } else {
                        if (userIdSet.contains(userId)) {
                            dataMap.put(userId,"●");
                        }
                    }
                }
            }
            list.add(dataMap);
        }
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("人员投入分布表", "人员投入分布"), colList, list);
        return workbook;
    }

    private Daily rebuildDaily(String id) {
        Daily daily = dailyRepository.findOne(id);
        String taskId  ="";
        if (CollectionUtils.isNotEmpty(daily.getTaskIdArray())){
            taskId = daily.getTaskIdArray()[0];
        }
        Task task = taskRepository.findById(taskId);
        if (task == null){
            ChildrenTask childrenTask = childTaskRepository.findById(taskId);
            if (null != childrenTask && StringUtils.isNotEmpty(childrenTask.getTaskId())){
                task = taskRepository.findById(childrenTask.getTaskId());
            }else {
                return null;
            }
        }
        if (null == task){
            return null;
        }else {
            if (StringUtils.isNotEmpty(task.getProjectId())){
                daily.setProjectId(task.getProjectId());
                daily.setProjectName(task.getProjectName());
            }
            if (StringUtils.isNotEmpty(task.getBudgetId())){
                daily.setBudgetId(task.getBudgetId());
                daily.setBudgetName(task.getBudgetName());
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    dailyRepository.save(daily);
                }
            });
            return daily;
        }
    }
    private Daily rebuildDaily(Daily daily) {
        String taskId  ="";
        if (CollectionUtils.isNotEmpty(daily.getTaskIdArray())){
            taskId = daily.getTaskIdArray()[0];
        }
        Task task = taskRepository.findById(taskId);
        if (task == null){
            ChildrenTask childrenTask = childTaskRepository.findById(taskId);
            if (null != childrenTask && StringUtils.isNotEmpty(childrenTask.getTaskId())){
                task = taskRepository.findById(childrenTask.getTaskId());
            }else {
                return null;
            }
        }
        if (null == task){
            return null;
        }else {
            if (StringUtils.isNotEmpty(task.getProjectId())){
                daily.setProjectId(task.getProjectId());
                daily.setProjectName(task.getProjectName());
            }
            if (StringUtils.isNotEmpty(task.getBudgetId())){
                daily.setBudgetId(task.getBudgetId());
                daily.setBudgetName(task.getBudgetName());
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    dailyRepository.save(daily);
                }
            });
            return daily;
        }
    }



    //从ES按日期查询指定员工的日报数据
    public  Map<String,List<JsonObject>> queryUsersDailyOld(List<String> userIds,String startDate,String endDate,String businessName,String projectName) {
        //当前用户的id
        JsonObject jsonObject = new JsonObject();

        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }

        Date date1 = null;
        Date date2 = null;


        if(StringUtils.isNotEmpty(startDate)&&StringUtils.isNotEmpty(endDate)) {
            try {
                date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startDate + " 0:0:0");
                date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endDate + " 23:59:59");
            } catch (Exception ex) {
                throw new AdcDaBaseException("请按正确的日期格式（yyyy-MM-dd)传递日期参数");
            }
        }

        else {
            date2=new Date();
            Calendar calendar=Calendar.getInstance();
            calendar.setTime(date2);


            calendar.add(Calendar.DATE,-30);
            date1=calendar.getTime();
        }


        int[] approveStateArr = {1, 2};
        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .mustNot(QueryBuilders.termQuery(DEL_FLAG, true))
                // .must(QueryBuilders.rangeQuery("dailyCreateTime").from(startTimestamp).to(endTimestamp))
                .must(QueryBuilders.termsQuery("approveState", approveStateArr))
                .must(QueryBuilders.existsQuery("workCostTime"))
                .must(QueryBuilders.termsQuery("createUserId",getMemberIdListOfSoftwareDevelopmentDepartment()))
                //.must(QueryBuilders.termsQuery("createUserId", userIds))
                ;
        if(date1!=null&&date2!=null){

            long startTimestamp = date1.getTime();
            long endTimestamp = date2.getTime();
            ((BoolQueryBuilder) queryBuilder).must(QueryBuilders.rangeQuery("dailyCreateTime").from(startTimestamp).to(endTimestamp));
        }


        if(StringUtils.isNotEmpty(projectName)){
            ((BoolQueryBuilder) queryBuilder).must(QueryBuilders.wildcardQuery("projectName","*" + projectName + "*"));
        }

        if(StringUtils.isNotEmpty(businessName)){
            ((BoolQueryBuilder) queryBuilder).must(QueryBuilders.wildcardQuery("budgetName","*" + businessName + "*"));
        }



        if(CollectionUtils.isNotEmpty(userIds)){
            ((BoolQueryBuilder) queryBuilder).must(QueryBuilders.termsQuery("createUserId",userIds));
        }



        //查出满足条件的日报
        Page<Daily> dailyList = dailyRepository.search(queryBuilder, new PageRequest(0, 99999));

        //定义返回结果
        Map<String,List<JsonObject>> result=new HashMap<>();

        //返回结果中的3个List的定义

        //保存员工业务信息
        List<JsonObject> empBusiness = new LinkedList<>();

        //保存员工项目信息
        List<JsonObject> empProjects = new LinkedList<>();



        //遍历日报
        for (Daily daily : dailyList) {
            String empId = daily.getCreateUserId();
            String empName = daily.getCreateUserName();


            int flag0=1;


            //遍历任务数组
            String[] taskIdArray = daily.getTaskIdArray();
            if (taskIdArray != null) {
                for (int i = 0; i < taskIdArray.length; i++) {
                    JsonObject obj =null;

                    JsonObject tempJsonObject=taskPool.get(taskIdArray[i]);

                    if(tempJsonObject!=null){
                        obj=tempJsonObject;
                    }else{
                        obj=getTaskById(taskIdArray[i]);
                        taskPool.put(taskIdArray[i],obj);
                    }

                    if(obj==null)continue;
                    obj.addProperty("empId",empId);
                    obj.addProperty("empName",empName);
                    String objType=obj.get("type").getAsString();
                    //如果是业务
                    if (objType.equals("1")) {
                        int flag=1;
                        for(JsonObject tmp:empBusiness){
                            if(tmp.get("id").getAsString().equals(obj.get("id").getAsString())&&tmp.get("empId").getAsString().equals(empId)){
                                flag=0;
                            }
                        }
                        if (flag==1) {
                            empBusiness.add(obj);
                        }
                    }

                    //如果是项目
                    if (objType.equals("2")) {
                        int flag=1;
                        for(JsonObject tmp:empProjects){
                            if(tmp.get("id").getAsString().equals(obj.get("id").getAsString())&&tmp.get("empId").getAsString().equals(empId)){
                                flag=0;
                            }
                        }
                        if (flag==1) {
                            empProjects.add(obj);
                        }
                    }
                }
            }
        }
        result.put("business",empBusiness);
        result.put("projects",empProjects);
        //返回结果
        return result;
    }



    //日报分布，给controller层调用
    public String getDailyDistributionOld(List<String> userIds,List<String> deptIds,String startDate,String endDate,String businName,String projName,int pageSize,int pageNo){
        Set<String> allUserIdSet=new HashSet<>();
        if(CollectionUtils.isEmpty(userIds)){
            allUserIdSet.addAll(getMemberIdListOfSoftwareDevelopmentDepartment());
        }
        else {
            allUserIdSet.addAll(userIds);
        }

        //如果传入了部门Id,则把部门人员添加到查询
        if(CollectionUtils.isNotEmpty(deptIds)){
            for(String deptId: deptIds)
            {
                List<String> ids=getMemberIdListOfDept(deptId);
                allUserIdSet.addAll(ids);
            }
        }

        List<String> allUserIdList=new LinkedList<>(allUserIdSet);

        List<UserEO> userEOList=userEODao.getUserWithRolesByUserIds(allUserIdList);
        List<JsonObject> empIdEmpName=new LinkedList<>();
        for(UserEO userEO: userEOList){
            JsonObject emp=new JsonObject();
            emp.addProperty("empId",userEO.getUsid());
            emp.addProperty("empName",userEO.getUsname());
            empIdEmpName.add(emp);
        }


        logger.info("********************************************** start query*****************************"+new Date());
        Map<String,List<JsonObject>> tasks=queryUsersDailyOld(allUserIdList,startDate,endDate,businName,projName);
        logger.info("********************************************** end query*****************************"+new Date());

        List<JsonObject> empBusiness=tasks.get("business");
        List<JsonObject> empProjects=tasks.get("projects");


        Map<String,String> businessMap=new HashMap<>();
        Map<String,String> projectMap=new HashMap<>();
        for(JsonObject jsonObject: empBusiness){
            String businessId=jsonObject.get("id").getAsString();
            String businessName=jsonObject.get("name").getAsString();
            businessMap.put(businessId,businessName);
        }

        for(JsonObject jsonObject: empProjects){
            String projectId=jsonObject.get("id").getAsString();
            String projectName=jsonObject.get("name").getAsString();
            projectMap.put(projectId,projectName);
        }

        //定义返回的JsonArray
        JsonArray jsonArray=new JsonArray();
        JsonArray headerArray=new JsonArray();
        JsonArray dataArray=new JsonArray();
        JsonObject countObject=new JsonObject();




        //表头
        String sequence=" { type: 'numbers', title: '序号', width: '60px' }";
        String title=" {field: 'name',  title: '业务或项目列表', width: '600px', status: 'project'  }";
        JsonObject sequenceObject=new JsonParser().parse(sequence).getAsJsonObject();
        headerArray.add(sequenceObject);
        JsonObject titleObject=new JsonParser().parse(title).getAsJsonObject();
        headerArray.add(titleObject);


        //表头，遍历员工列表
        for(JsonObject jsonObject:empIdEmpName){
            String empId=jsonObject.get("empId").getAsString();
            String empName=jsonObject.get("empName").getAsString();
            String empString="{        field: '" +
                    empId +
                    "',\n" +
                    "            title: '" +
                    empName +
                    "',\n" +
                    "            align: 'center',\n" +
                    "            width: '120px', \n" +
                    "            status:'people' }";
            JsonObject emp=new JsonParser().parse(empString).getAsJsonObject();
            headerArray.add(emp);
        }


        logger.info("**********************************************before business*****************************"+new Date());

        int count=0;

        //遍历业务数组************************************
        for(String businessId: businessMap.keySet()){
            count++;
            String businessName=businessMap.get(businessId);
            JsonObject tempJsonObject=new JsonObject();
            tempJsonObject.addProperty("id",businessId);
            tempJsonObject.addProperty("name",businessName);
            tempJsonObject.addProperty("parentId",0);
            tempJsonObject.addProperty("type",1);

            //遍历员工**********************************************
            for(JsonObject empJsonObject: empIdEmpName){
                //从人员object中获取id
                String empIdTemp=empJsonObject.get("empId").getAsString();
                String propertyName=empIdTemp+"";
                if(CollectionUtils.isEmpty(empBusiness)){
                    tempJsonObject.addProperty(propertyName,"null");
                }

                else
                {
                    //定义标志判断用户是否在该业务
                    int flag=0;
                    for(JsonObject businessObject: empBusiness){
                        if(businessObject.get("id").getAsString().equals(businessId)&&businessObject.get("empId").getAsString().equals(empIdTemp)){
                            flag=1;
                            break;
                        }
                    }

                    if(flag==1){
                        tempJsonObject.addProperty(propertyName,"1");
                    }
                    else{
                        tempJsonObject.addProperty(propertyName,"null");
                    }
                }

            }

            dataArray.add(tempJsonObject);
        }

        logger.info("********************************************** end business*****************************"+new Date());

        logger.info("********************************************** before projects*****************************"+new Date());


        Set<String> allProjectIds=new HashSet<>();
        for(JsonObject projectJsonObject: empProjects){
            allProjectIds.add(projectJsonObject.get("id").getAsString());
        }


        if(CollectionUtils.isNotEmpty(allProjectIds)){
            List<Project> projectList = projectRepository.findByIdIn(allProjectIds);
            if(projectList!=null) {
                for (Project proj : projectList) {
                    String projId = proj.getId();
                    String businId = proj.getBudgetId();
                    if (StringUtils.isEmpty(projId) || StringUtils.isEmpty(businId)) continue;
                    projectIdBusinessIdMap.put(projId, businId);
                }
            }


        }


        //遍历项目数组
        for(String projectId: projectMap.keySet()){
            count++;
            String projectName=projectMap.get(projectId);


            JsonObject tempJsonObject=new JsonObject();
            String businId=projectIdBusinessIdMap.get(projectId);
            if(StringUtils.isEmpty(businId)){
                continue;
            }

            tempJsonObject.addProperty("id",projectId);
            tempJsonObject.addProperty("name",projectName);
            tempJsonObject.addProperty("parentId",businId);
            tempJsonObject.addProperty("type",2);

            //遍历所有人
            for(JsonObject empJsonObject: empIdEmpName){

                String empIdTemp=empJsonObject.get("empId").getAsString();
                //String propertyName=empIdTemp+"ProjectTaskStatus";
                String propertyName=empIdTemp+"";

                //定义标志判断用户是否在该项目
                int flag=0;
                String empId=empJsonObject.get("empId").getAsString();
                for(JsonObject projectObject: empProjects){
                    if(projectObject.get("id").getAsString().equals(projectId)&&projectObject.get("empId").getAsString().equals(empId)){
                        flag=1;
                        break;
                    }
                }


                if(flag==1){
                    tempJsonObject.addProperty(propertyName,"2");
                }
                else{

                    tempJsonObject.addProperty(propertyName,"null");
                }

            }   //遍历所有人

            dataArray.add(tempJsonObject);

        }//遍历项目数组结束

        //开始分页
        int startIndex=(pageNo-1)*pageSize;
        int endIndex=pageNo*pageSize;

        if(startIndex>=count){
            return null;
        }
        if(endIndex>count){
            endIndex=count;
        }







        JsonArray pageDataArray=new JsonArray();
        for(int i=startIndex;i<endIndex;i++){
            pageDataArray.add(dataArray.get(i));
        }



        countObject.addProperty("count",count);
        jsonArray.add(headerArray);
        jsonArray.add(pageDataArray);
        jsonArray.add(countObject);


        String result=jsonArray.toString();

        logger.info("*******************************************before returning*****************************  "+new Date());

        return result;

    }
    private List<ListTreeVO> getTreeList(Set<String> businessIds,Set<String> childTaskIdSet) {
        List<ListTreeVO> listTreeVOList = new ArrayList<>();
        Set<String> projectIdSet = new HashSet<>();
        Set<String> taskIdSet = new HashSet<>();
        List<UserEPEntity> userEPEntityList = userEPDao.queryAllUserIdAndName();
        // 根据顶层budgetId构造树
        List<BudgetEO> budgetEOList = budgetEOService.findByIds(businessIds);
        for (BudgetEO budgetEO : budgetEOList) {
            String pm = budgetEO.getPm();
            String pmName = "";
            if (StringUtils.isNotEmpty(pm)) {
                for (UserEPEntity userEPEntity : userEPEntityList) {
                    if (pm.equals(userEPEntity.getUsid())) {
                        pmName = userEPEntity.getUsname();
                    }
                }
            }
            listTreeVOList.add(new ListTreeVO(budgetEO.getId(), budgetEO.getProjectName(),
                    "0", 1, pm, pmName));
        }
        List<Project> projectList = projectRepository.findByBudgetIdInAndDelFlagNot(businessIds,true);
        if (CollectionUtils.isNotEmpty(projectList)) {
            for (Project project : projectList) {
                listTreeVOList
                        .add(new ListTreeVO(
                                project.getId(),
                                project.getName(),
                                project.getBudgetId(),
                                2,
                                "0",
                                project.getContractNo(),
                                project.getName(),
                                project.getProjectOwner(), project.getProjectLeaderId(), project.getProjectLeader()));
                projectIdSet.add(project.getId());
            }
        }
        List<Task> taskList1 = taskRepository.findByProjectIdIn(projectIdSet);
        List<Task> taskList2 = taskRepository.findByBudgetIdIn(businessIds);
        taskList1.addAll(taskList2);
        for (Task task : taskList1){
            taskIdSet.add(task.getId());
        }
        List<ChildrenTask> childrenTaskList = childTaskRepository.findByTaskIdIn(taskIdSet);
        for (ChildrenTask childrenTask : childrenTaskList){
            childTaskIdSet.add(childrenTask.getId());
        }
        childTaskIdSet.addAll(taskIdSet);
        return listTreeVOList;
    }

    public String getDailyDistributionNewTem1(List<String> userIds,List<String> deptIds,String startDate,String endDate,String businName,String projName,int pageSize,int pageNo) {
        //定义返回的JsonArray
        JsonArray jsonArray = new JsonArray();
        JsonArray headerArray = new JsonArray();
        JsonArray dataArray = new JsonArray();
        JsonObject countObject = new JsonObject();
        //表头
        String sequence = " { type: 'numbers', title: '序号', width: '60px' }";
        String title = " {field: 'name',  title: '业务或项目列表', width: '600px', status: 'project'  }";
        JsonObject sequenceObject = new JsonParser().parse(sequence).getAsJsonObject();
        headerArray.add(sequenceObject);
        JsonObject titleObject = new JsonParser().parse(title).getAsJsonObject();
        headerArray.add(titleObject);
        Set<String> allUserIdSet = new HashSet<>();
        //如果不传入ID,则把软件开发部的员工加到查询
        if (CollectionUtils.isEmpty(userIds)) {
            allUserIdSet.addAll(getMemberIdListOfSoftwareDevelopmentDepartment());
        } else {
            allUserIdSet.addAll(userIds);
        }
        //如果传入了部门Id,则把部门人员添加到查询
        if (CollectionUtils.isNotEmpty(deptIds)) {
            for (String deptId : deptIds) {
                List<String> ids = getMemberIdListOfDept(deptId);
                allUserIdSet.addAll(ids);
            }
        }
        List<String> allUserIdList = new LinkedList<>(allUserIdSet);
        String[] property = {"0"};
        List<String> budgetIdList = budgetEOService.getDao().findAllBudgetIdByNameNotLikeAndPropertyArr("旧-%", property);
//        if(CollectionUtils.isEmpty(allUserIdList)){throw new AdcDaBaseException("请选择人员或部门！");};
//        List<UserWithProjects> userWithProjectsList = userWithProjectsRepository.findByUserIdIn(allUserIdSet);
//        Set<String> budgetIdSet = new HashSet<>();
//        Set<String> projectIdSet = new HashSet<>();
//        for (UserWithProjects userWithProjects : userWithProjectsList) {
//            budgetIdSet.addAll(userWithProjects.getBusinessIds());
//            projectIdSet.addAll(userWithProjects.getProjectIds());
//        }
//        budgetIdSet.removeAll(Collections.singleton(null));
//        ArrayList<String> searchBudgetIdList = new ArrayList<>();
//        // 去除非经营类业务
//        for (String budgetId : budgetIdSet) {
//            if (budgetIdList.contains(budgetId)) {
//                searchBudgetIdList.add(budgetId);
//            }
//        }

        //构造json表头
        List<UserEO> userEOList = userEODao.getUserWithRolesByUserIds(allUserIdList);
        List<JsonObject> userIdNameJsonList = new LinkedList<>();
        Map<String, String> userIdAndNameMap = new LinkedHashMap<>();
        for (UserEO userEO : userEOList) {
            JsonObject userJson = new JsonObject();
            userJson.addProperty("userId", userEO.getUsid());
            userJson.addProperty("userName", userEO.getUsname());
            userIdNameJsonList.add(userJson);
            userIdAndNameMap.put(userEO.getUsid(), userEO.getUsname());
        }
        //表头，遍历员工列表
        for (JsonObject userIdNameJson : userIdNameJsonList) {
            String userId = userIdNameJson.get("userId").getAsString();
            String userName = userIdNameJson.get("userName").getAsString();
            String headerString = "{        field: '" +
                    userId +
                    "',\n" +
                    "            title: '" +
                    userName +
                    "',\n" +
                    "            align: 'center',\n" +
                    "            width: '120px', \n" +
                    "            status:'people' }";
            JsonObject headerJson = new JsonParser().parse(headerString).getAsJsonObject();
            headerArray.add(headerJson);
        }
        //创建查询工具类，然后按照条件查询，可以分开也可以合在一起写
        int[] approveStateArr = {1, 2};
        QueryBuilder queryBuilderFilter = QueryBuilders.boolQuery()
                .mustNot(QueryBuilders.termQuery(DEL_FLAG, true))
                // .must(QueryBuilders.rangeQuery("dailyCreateTime").from(startDate).to(endDate))
                .must(QueryBuilders.termsQuery("approveState", approveStateArr))
                .must(QueryBuilders.existsQuery("workCostTime"))
                .must(QueryBuilders.termsQuery("createUserId",allUserIdSet));
        //构建查询
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder().withQuery(queryBuilderFilter);
        // 不查询任何结果
        nativeSearchQueryBuilder.withSourceFilter(new FetchSourceFilter(new String[] { "" }, null));
        TermsBuilder dailyAgg = AggregationBuilders.terms("budgetId").field("budgetId").size(10000);
        nativeSearchQueryBuilder.addAggregation(dailyAgg);
        AggregatedPage<Daily> queryPage = (AggregatedPage<Daily>) this.dailyRepository.search(nativeSearchQueryBuilder.build());
        StringTerms agg = (StringTerms) queryPage.getAggregation("budgetId");
        List<Terms.Bucket> buckets = agg.getBuckets();
        List<String> allBugetIdList = new ArrayList<>();
        for (Terms.Bucket bucket : buckets) {
            allBugetIdList.add(bucket.getKeyAsString());
        }
        List<String> searchBudgetIdList = new ArrayList<>();
        for (String bid : allBugetIdList){
            if (budgetIdList.contains(bid)){
                searchBudgetIdList.add(bid);
            }
        }
        List<String> budgetPageList = new ArrayList<>();
        if (searchBudgetIdList.size() > pageSize * pageNo + pageSize) {
            budgetPageList = searchBudgetIdList.subList(pageSize * pageNo, pageSize * pageNo + pageSize);
        } else {
            jsonArray.toString();
        }
        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .mustNot(QueryBuilders.termQuery(DEL_FLAG, true))
                // .must(QueryBuilders.rangeQuery("dailyCreateTime").from(startDate).to(endDate))
                .must(QueryBuilders.termsQuery("approveState", approveStateArr))
                .must(QueryBuilders.existsQuery("workCostTime"))
                .must(QueryBuilders.termsQuery("createUserId",allUserIdSet))
                .must(QueryBuilders.termsQuery("budgetId", budgetPageList));
        List<Daily> dailyList = Lists.newArrayList(dailyRepository.search(queryBuilder));
        //        // 构建业务、项目 与人员的关系表 用于反向索引
        Map<String, Set<String>> budgetIdORProjectIdAndUserIdSetMap = new HashMap();
        List<ListTreeVO> treeList = new ArrayList<>();
        for (Daily daily : dailyList) {
            final Daily daily1 = daily;
            String createUserId = daily.getCreateUserId();
            String projectId = daily.getProjectId();
            String budgetId = daily.getBudgetId();
            String budgetName = daily.getBudgetName();
            String projectName = daily.getProjectName();
            if(StringUtils.isEmpty(projectId)&& StringUtils.isNotEmpty(projectName)){
                daily = rebuildDaily(daily);
                if (StringUtils.isNotEmpty(daily.getProjectId())) {
                    projectId = daily.getProjectId();
                    projectName = daily.getProjectName();
                }
                if (StringUtils.isNotEmpty(daily.getBudgetId())) {
                    budgetId = daily.getBudgetId();
                    budgetName = daily.getBudgetName();
                }
            }
            if (StringUtils.isNotEmpty(projectId)) {
                Set<String> userIdSet = budgetIdORProjectIdAndUserIdSetMap.get(projectId);
                if (null != userIdSet) {
                    userIdSet.add(createUserId);
                } else {
                    userIdSet = new HashSet<>();
                    userIdSet.add(createUserId);
                }
                budgetIdORProjectIdAndUserIdSetMap.put(projectId, userIdSet);
                if (StringUtils.isEmpty(budgetId) || StringUtils.isEmpty(projectName)) {
                    Project project = projectRepository.findById(projectId);
                    if (null != project) {
                        projectName = project.getName();
                        budgetId = project.getBudgetId();
                        budgetName = project.getBudget();
                        final String budgetIdt = budgetId;
                        final String budgetNamet = budgetName;
                        final String projectNamet = projectName;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                daily1.setBudgetId(budgetIdt);
                                daily1.setBudgetName(budgetNamet);
                                daily1.setBudgetNameES(budgetNamet);
                                daily1.setProjectName(projectNamet);
                                daily1.setProjectNameES(projectNamet);
                                dailyRepository.save(daily1);
                            }
                        });
                    }
                }
                treeList.add(new ListTreeVO(projectId, projectName,
                        budgetId, 1, "", ""));
            }
            if (StringUtils.isNotEmpty(budgetId)) {
                Set<String> userIdSet = budgetIdORProjectIdAndUserIdSetMap.get(budgetId);
                if (null != userIdSet) {
                    userIdSet.add(createUserId);
                } else {
                    userIdSet = new HashSet<>();
                    userIdSet.add(createUserId);
                }
                budgetIdORProjectIdAndUserIdSetMap.put(budgetId, userIdSet);
                if (budgetPageList.contains(budgetId)) {
                    treeList.add(new ListTreeVO(budgetId, budgetName,
                            "0", 0, "", ""));
                    budgetPageList.remove(budgetId);
                }
            }
        }

        for (ListTreeVO listTreeVO : treeList) {
            /**
             * 0 业务
             * 1 项目
             */
            Set<String> userIdSet = budgetIdORProjectIdAndUserIdSetMap.get(listTreeVO.getId());
            //当前为业务
            if (listTreeVO.getType() == 0) {
                JsonObject tempJsonObject = new JsonObject();
                tempJsonObject.addProperty("id", listTreeVO.getId());
                tempJsonObject.addProperty("name", listTreeVO.getName());
                tempJsonObject.addProperty("parentId", 0);
                tempJsonObject.addProperty("type", 1);
                for (JsonObject userIdNameJson : userIdNameJsonList) {
                    String userId = userIdNameJson.get("userId").getAsString();
                    String propertyName = userId + "";
                    if (CollectionUtils.isEmpty(userIdSet)) {
                        continue;
                        //tempJsonObject.addProperty(propertyName, "null");
                    } else {
                        if (userIdSet.contains(userId)) {
                            tempJsonObject.addProperty(propertyName, "1");
                        } else {
                            continue;
                            //tempJsonObject.addProperty(propertyName, "null");
                        }
                    }
                }
                dataArray.add(tempJsonObject);
            } else { //当前为项目
                JsonObject tempJsonObject = new JsonObject();
                tempJsonObject.addProperty("id", listTreeVO.getId());
                tempJsonObject.addProperty("name", listTreeVO.getName());
                tempJsonObject.addProperty("parentId", listTreeVO.getParentId());
                tempJsonObject.addProperty("type", 2);

                for (JsonObject userIdNameJson : userIdNameJsonList) {
                    String userId = userIdNameJson.get("userId").getAsString();
                    String propertyName = userId + "";
                    if (CollectionUtils.isEmpty(userIdSet)) {
                        continue;
                        //tempJsonObject.addProperty(propertyName, "null");
                    } else {
                        if (userIdSet.contains(userId)) {
                            tempJsonObject.addProperty(propertyName, "2");
                        } else {
                            continue;
                            //tempJsonObject.addProperty(propertyName, "null");
                        }
                    }
                }
                dataArray.add(tempJsonObject);
            }//当前为项目
        }
        countObject.addProperty("count", searchBudgetIdList.size());
        jsonArray.add(headerArray);
        jsonArray.add(dataArray);
        jsonArray.add(countObject);
        String result = jsonArray.toString();
        return result;
    }

    public String getDailyDistributionNewTem2(List<String> userIds,List<String> deptIds,String startDate,String endDate,String businName,String projName,int pageSize,int pageNo) {
        long endTime = new Date().getTime();
        long startTime = endTime - 24*3600*60;
        if(StringUtils.isNotEmpty(startDate)&&StringUtils.isNotEmpty(endDate)) {
            try {
                startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startDate + " 0:0:0").getTime();
                endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endDate + " 23:59:59").getTime();
            } catch (Exception ex) {
                throw new AdcDaBaseException("请按正确的日期格式（yyyy-MM-dd)传递日期参数");
            }
        }
        //定义返回的JsonArray
        JsonArray jsonArray=new JsonArray();
        JsonArray headerArray=new JsonArray();
        JsonArray dataArray=new JsonArray();
        JsonObject countObject=new JsonObject();
        //表头
        String sequence=" { type: 'numbers', title: '序号', width: '60px' }";
        String title=" {field: 'name',  title: '业务或项目列表', width: '600px', status: 'project'  }";
        JsonObject sequenceObject=new JsonParser().parse(sequence).getAsJsonObject();
        headerArray.add(sequenceObject);
        JsonObject titleObject=new JsonParser().parse(title).getAsJsonObject();
        headerArray.add(titleObject);
        Set<String> allUserIdSet=new HashSet<>();
        //如果不传入ID,则把软件开发部的员工加到查询
        if(CollectionUtils.isEmpty(userIds)){
            allUserIdSet.addAll(getMemberIdListOfSoftwareDevelopmentDepartment());
        } else {
            allUserIdSet.addAll(userIds);
        }
        //如果传入了部门Id,则把部门人员添加到查询
        if(CollectionUtils.isNotEmpty(deptIds)){
            for(String deptId: deptIds) {
                List<String> ids=getMemberIdListOfDept(deptId);
                allUserIdSet.addAll(ids);
            }
        }
        List<String> allUserIdList=new LinkedList<>(allUserIdSet);
        Set<String> budgetIdSet = new HashSet<>();
        String [] property = {"0"};
        List<String> budgetIdList = budgetEOService.getDao().findAllBudgetIdByNameNotLikeAndPropertyArr("旧-%", property);
        budgetIdSet.addAll(new HashSet<>(budgetIdList));
        budgetIdSet.removeAll(Collections.singleton(null));

        //构造json表头
        List<UserEO> userEOList = userEODao.getUserWithRolesByUserIds(allUserIdList);
        List<JsonObject> userIdNameJsonList=new LinkedList<>();
        List<String> userIdList = new LinkedList<>();
        for(UserEO userEO: userEOList){
            JsonObject userJson=new JsonObject();
            userJson.addProperty("userId",userEO.getUsid());
            userJson.addProperty("userName",userEO.getUsname());
            userIdNameJsonList.add(userJson);
            userIdList.add(userEO.getUsid());
        }
        //表头，遍历员工列表
        for(JsonObject userIdNameJson:userIdNameJsonList){
            String userId = userIdNameJson.get("userId").getAsString();
            String userName = userIdNameJson.get("userName").getAsString();
            String headerString="{        field: '" +
                    userId +
                    "',\n" +
                    "            title: '" +
                    userName +
                    "',\n" +
                    "            align: 'center',\n" +
                    "            width: '120px', \n" +
                    "            status:'people' }";
            JsonObject headerJson = new JsonParser().parse(headerString).getAsJsonObject();
            headerArray.add(headerJson);
        }
        //创建查询工具类，然后按照条件查询，可以分开也可以合在一起写
        int[] approveStateArr = {1, 2};
        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .mustNot(QueryBuilders.termQuery(DEL_FLAG, true))
                .must(QueryBuilders.rangeQuery("dailyCreateTime").from(startDate).to(endDate))
                .must(QueryBuilders.termsQuery("approveState", approveStateArr))
                .must(QueryBuilders.existsQuery("workCostTime"))
                .must(QueryBuilders.termsQuery("budgetId",budgetIdSet))
                .must(QueryBuilders.termsQuery("createUserId",allUserIdSet));

        //构建查询
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder().withQuery(queryBuilder);
        // 不查询任何结果
        nativeSearchQueryBuilder.withSourceFilter(new FetchSourceFilter(new String[] { "" }, null));
        TermsBuilder dailyAgg = AggregationBuilders.terms("budgetId").field("budgetId").size(10000);
        nativeSearchQueryBuilder.addAggregation(dailyAgg);
        SearchQuery searchQuery = nativeSearchQueryBuilder.build();
        searchQuery.setPageable(new PageRequest(pageNo, pageSize));
        AggregatedPage<Daily> queryPage = (AggregatedPage<Daily>) this.dailyRepository.search(searchQuery);
        long count = (queryPage == null) ? 0 : queryPage.getTotalElements();
        StringTerms agg = (StringTerms) queryPage.getAggregation("budgetId");
        List<Terms.Bucket> buckets = agg.getBuckets();
        ArrayList<String> searchBudgetIdList = new ArrayList<>();
        for (Terms.Bucket bucket : buckets) {
            searchBudgetIdList.add(bucket.getKeyAsString());
        }
        List<String> searchPageList = new ArrayList<>();
        if (pageNo <1 || pageSize <1){
            return jsonArray.toString();
        }
        if (searchBudgetIdList.size() > pageSize * pageNo + pageSize) {
            searchPageList = searchBudgetIdList.subList(pageSize * pageNo, pageSize * pageNo + pageSize);
        } else {
            jsonArray.toString();
        }
        List<ListTreeVO> treeList = new ArrayList<>();
        List<Daily> dailyList = dailyRepository.findByBudgetIdInAndDelFlagNot(new HashSet<>(searchPageList),true);
        // 反向索引
        Map<String,Set<String>> budgetIdORProjectIdAndUserIdSetMap = new HashMap();
        //遍历所有业务和项目
        for (Daily daily : dailyList){
            final Daily daily1  = daily;
            String createUserId = daily.getCreateUserId();
            String projectId = daily.getProjectId();
            String budgetId =  daily.getBudgetId();
            String budgetName = daily.getBudgetName();
            String projectName =  daily.getProjectName();
            if(StringUtils.isEmpty(projectId)&&StringUtils.isNotEmpty(projectName)){
                daily  =  rebuildDaily(daily);
                if (StringUtils.isNotEmpty(daily.getProjectId())){
                    projectId = daily.getProjectId();
                    projectName = daily.getProjectName();
                }
                if (StringUtils.isNotEmpty(daily.getBudgetId())){
                    budgetId = daily.getBudgetId();
                    budgetName = daily.getBudgetName();
                }
            }
            if (StringUtils.isNotEmpty(projectId)){
                Set<String> userIdSet = budgetIdORProjectIdAndUserIdSetMap.get(projectId);
                if (null!= userIdSet) {
                    userIdSet.add(createUserId);
                }else {
                    userIdSet = new HashSet<>();
                    userIdSet.add(createUserId);
                }
                budgetIdORProjectIdAndUserIdSetMap.put(projectId,userIdSet);
                if(StringUtils.isEmpty(budgetId)|| StringUtils.isEmpty(projectName)){
                    Project project = projectRepository.findById(projectId);
                    if (null != project) {
                        projectName = project.getName();
                        budgetId = project.getBudgetId();
                        budgetName = project.getBudget();
                        final String budgetIdt = budgetId;
                        final String budgetNamet = budgetName;
                        final String projectNamet = projectName;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                daily1.setBudgetId(budgetIdt);
                                daily1.setBudgetName(budgetNamet);
                                daily1.setBudgetNameES(budgetNamet);
                                daily1.setProjectName(projectNamet);
                                daily1.setProjectNameES(projectNamet);
                                dailyRepository.save(daily1);
                            }
                        });
                    }
                }
                treeList.add(new ListTreeVO(projectId, projectName,
                        budgetId, 1, "", ""));
            }
            if (StringUtils.isNotEmpty(budgetId)) {
                Set<String> userIdSet = budgetIdORProjectIdAndUserIdSetMap.get(budgetId);
                if (null != userIdSet) {
                    userIdSet.add(createUserId);
                } else {
                    userIdSet = new HashSet<>();
                    userIdSet.add(createUserId);
                }
                budgetIdORProjectIdAndUserIdSetMap.put(budgetId, userIdSet);
                if (searchPageList.contains(budgetId)) {
                    treeList.add(new ListTreeVO(budgetId, budgetName,
                            "0", 0, "", ""));
                    searchPageList.remove(budgetId);
                }
            }
        }
        for(ListTreeVO listTreeVO: treeList){
            /**
             * 0 业务
             * 1 项目
             */
            //当前为业务
            Set<String> userIdSet = budgetIdORProjectIdAndUserIdSetMap.get(listTreeVO.getId());  //可以提到外面
            if(listTreeVO.getType() == 0){
                JsonObject tempJsonObject=new JsonObject();
                tempJsonObject.addProperty("id",listTreeVO.getId());
                tempJsonObject.addProperty("name",listTreeVO.getName());
                tempJsonObject.addProperty("parentId",0);
                tempJsonObject.addProperty("type",1);
                for(String userId: userIdList) {
                    String propertyName = userId + "";
                    if (CollectionUtils.isEmpty(userIdSet)) {
                        continue;
                    } else {
                        if (userIdSet.contains(userId)) {
                            tempJsonObject.addProperty(propertyName, "1");
                        }
                    }
                }
                dataArray.add(tempJsonObject);
            }else { //当前为项目
                JsonObject tempJsonObject=new JsonObject();
                tempJsonObject.addProperty("id",listTreeVO.getId());
                tempJsonObject.addProperty("name",listTreeVO.getName());
                tempJsonObject.addProperty("parentId",listTreeVO.getParentId());
                tempJsonObject.addProperty("type",2);
                for(String userId: userIdList) {
                    String propertyName = userId + "";
                    if (CollectionUtils.isEmpty(userIdSet)) {
                        continue;
                    } else {
                        if (userIdSet.contains(userId)) {
                            tempJsonObject.addProperty(propertyName, "2");
                        }
                    }
                }
                dataArray.add(tempJsonObject);
            }//当前为项目
        }
        countObject.addProperty("count",searchBudgetIdList.size());
        jsonArray.add(headerArray);
        jsonArray.add(dataArray);
        jsonArray.add(countObject);
        String result=jsonArray.toString();
        return result;
    }

}
